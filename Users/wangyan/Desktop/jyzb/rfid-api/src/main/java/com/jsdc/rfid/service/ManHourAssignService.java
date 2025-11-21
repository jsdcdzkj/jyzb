package com.jsdc.rfid.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.ManHourAssignDao;
import com.jsdc.rfid.mapper.FileManageMapper;
import com.jsdc.rfid.mapper.ManHourAssignMapper;
import com.jsdc.rfid.model.FileManage;
import com.jsdc.rfid.model.ManHourAssign;
import com.jsdc.rfid.model.SysUser;
import com.jsdc.rfid.vo.ManHourAssignVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.ResultInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ManHourAssignService extends BaseService<ManHourAssignDao, ManHourAssign> {
    @Autowired
    private ManHourAssignMapper mapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private FileManageService fileManageService;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Autowired
    private FileManageMapper fileManageMapper;

    public PageInfo<ManHourAssignVo> getPageList(Integer page, Integer limit, ManHourAssignVo vo) {
        PageHelper.startPage(page, limit, "createTime desc");
        List<ManHourAssignVo> manHourAssignVoList = this.mapper.getPageList(vo);
        return new PageInfo<>(manHourAssignVoList);
    }

    public ResultInfo generateStatisticExcel(Integer fileId, SysUser user) {
        try {
            FileManage fileManage = fileManageMapper.selectById(fileId);
            String path = uploadPath + File.separator + fileManage.getFile_name();
            File file = new File(path);

            String file_name = fileManage.getFile_name();
            int lastIndexOf = file_name.lastIndexOf('.');
            String suffix = file_name.substring(lastIndexOf + 1);

            Workbook workbook = null;
            FileInputStream fileInputStream = null;

            try {
                fileInputStream = new FileInputStream(file);
                switch (suffix) {
                    case "xlsx": {
                        workbook = new XSSFWorkbook(fileInputStream);
                        break;
                    }
                    case "xls": {
                        workbook = new HSSFWorkbook(fileInputStream);
                        break;
                    }
                }
//                workbook = new XSSFWorkbook(fileInputStream);
                Sheet sheet = null;
                if (workbook != null) {
                    sheet = workbook.getSheet("工时费");
                }
                if (sheet == null) {
                    return ResultInfo.error("没有找到名称为工时费的Sheet");
                }
                Row row4 = sheet.getRow(3 - 1);
                // 获取倒数第二列
                Cell cellLast2 = row4.getCell(row4.getLastCellNum() - 2);
                // 获取项目数, 前4行是固定的
                int projectNum = (cellLast2.getColumnIndex() + 1 - 4) / 2;
                if (projectNum <= 0) {
                    return ResultInfo.error("项目数为空, 不能计算");
                }

                int projectCount = 0;

                int lastRowNum = sheet.getLastRowNum();

                // 这一行哪一列有工时占比
                ArrayList<Integer> gsfLocation = gsfLocation(projectNum);

                DataFormat dataFormat = workbook.createDataFormat();


                for (Row row : sheet) {
//                    ArrayList<Integer> clone = (ArrayList<Integer>) gsfLocation.clone();
                    // 人力成本
                    double rlcb = 0D;
                    int rowNum = row.getRowNum();
                    // 从第五行开始是数据
                    if (rowNum > (3 - 1) && rowNum < lastRowNum) {
                        // 获取这一行的人力成本
                        Cell cell4 = row.getCell(4 - 1);
                        CellType cellType = cell4.getCellType();
                        String name = cellType.name();
                        // 人力成本必须为数字
                        if (!name.equals("NUMERIC")) {
                            int rowIndex = cell4.getRowIndex();
                            int columnIndex = cell4.getColumnIndex();
                            return ResultInfo.error("第" + (columnIndex + 1) + "列, 第" + (rowIndex + 1) + "行, 人力成本必须为数字且不能为空");
                        }

                        rlcb = cell4.getNumericCellValue();


                        // 这一行计算几次工时费
                        for (Cell cell : row) {
                            int columnIndex = cell.getColumnIndex();

//                            // 如果没有了就跳出去
//                            if (clone.size() == 0) {
//                                break;
//                            }
                            // 开始计算公式费
                            // 获取projectNum数的工时占比
//                            if (columnIndex == clone.get(0)) {
                            if (gsfLocation.contains(columnIndex)) {

                                // 工时占比
                                CellType cellGszb = cell.getCellType();

                                if (!cellGszb.name().equals("NUMERIC") && !(cellGszb.name().equals("BLANK")) && !(cellGszb.name().equals("FORMULA"))) {
                                    int rowIndex1 = cell.getRowIndex();
                                    int columnIndex1 = cell.getColumnIndex();
                                    return ResultInfo.error("第" + (columnIndex1 + 1) + "列, 第" + (rowIndex1 + 1) + "行必须为百分比数字");
                                }

                                double gszb = cell.getNumericCellValue();
                                double sjFee = gszb * rlcb;

                                // 这工时费单元格设置值
                                int columnIndexNext = cell.getColumnIndex();
                                Cell cellGSF = row.getCell(++columnIndexNext);

                                // 设置样式, 工时费保留2位小数
                                CellStyle cellStyle = cellGSF.getCellStyle();
                                cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

                                cellGSF.setCellValue(sjFee);
                                System.out.println(sjFee);
//                                clone.remove(0);
                            }
                        }
                        System.out.println("\t");
                    }
                }
                statistic(sheet, gsfLocation, dataFormat);
                // 输出为文件
                String stringTime = getStringTime();
                String r = stringTime + file_name;

                File file1 = new File(uploadPath + File.separator + r);
                FileOutputStream fileOutputStream = new FileOutputStream(file1);
                workbook.write(fileOutputStream);
                long length = file1.length();


                FileManage fileManageNew = new FileManage();
                fileManageNew.setFile_type(suffix);
                fileManageNew.setFile_name(r);
                fileManageNew.setStore_name(fileManage.getStore_name());
                fileManageNew.setFile_size(String.valueOf(length));
                fileManageNew.setCreate_time(new Date());
                fileManageNew.setCreate_user(user.getId());
                this.fileManageService.insert(fileManageNew);

                ManHourAssign manHourAssign = new ManHourAssign();
                manHourAssign.setCreatetime(LocalDateTime.now());
                manHourAssign.setUserid(user.getId());
                manHourAssign.setNewfileid(fileManageNew.getId());
                manHourAssign.setOriginfileid(fileId);
                this.insert(manHourAssign);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (workbook != null) {
                        workbook.close();
                    }
                    fileInputStream.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) {
            return ResultInfo.error("计算失败, 请确认文件样式及位置是否正确");
        }
        return ResultInfo.success();
    }

    private static ArrayList<Integer> gsfLocation(int projectNum) {
        ArrayList<Integer> list = new ArrayList<>(projectNum);
        int count = 0;
        for (int i = 0; i < projectNum; i++) {
            int t = 5 + (2 * count);
            list.add(t - 1);
            count++;
        }
        return list;
    }

    private static String getStringTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");
        return now.format(dateTimeFormatter);
    }

    public static void statistic(Sheet sheet, ArrayList<Integer> gsfLocation, DataFormat dataFormat) {
        // 临时加一下人力成本的
        gsfLocation.add(2);
        int size = gsfLocation.size();

        for (int i = 0; i < size; i++) {
            Double cellSum = 0D;
            for (int j = 4 - 1; j < sheet.getLastRowNum(); j++) {
                // 如果是第四列, 则计算人力成本

                Row row = sheet.getRow(j);
                Cell cell = row.getCell(gsfLocation.get(0) + 1);
                cellSum += cell.getNumericCellValue();

            }
            int lastRowNum = sheet.getLastRowNum();
            Row row = sheet.getRow(lastRowNum);

            Cell cell = row.getCell(gsfLocation.get(0) + 1);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
            cell.setCellValue(cellSum);
            gsfLocation.remove(0);
        }
    }
}
//            try {
//                fileInputStream = new FileInputStream(file);
//                switch (suffix) {
//                    case "xlsx": {
//                         workbook = new XSSFWorkbook(fileInputStream);
//                    }
//                    case "xls": {
//                         workbook = new HSSFWorkbook(fileInputStream);
//                    }
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }


//            Sheet sheet = null;
//            try {
//                sheet = workbook.getSheetAt(1);
//                // 获取第四行
//                Row row4 = sheet.getRow(4 - 1);
//                // 获取倒数第二列
//                Cell cellLast2 = row4.getCell(row4.getLastCellNum() - 2);
//
//                // 获取项目数, 前4行是固定的
//                int projectNum = (cellLast2.getColumnIndex() + 1 - 4) / 2;
//                if (projectNum <= 0) {
//                    return ResultInfo.error("项目数为空, 不能计算");
//                }
//                int projectCount = 0;
//
//                int lastRowNum = sheet.getLastRowNum();
//
//                // 这一行哪一列有工时占比
//                ArrayList<Integer> gsfLocation = gsfLocation(projectNum);
//
//                DataFormat dataFormat = workbook.createDataFormat();
//
//
//                for (Row row : sheet) {
//                    ArrayList<Integer> clone = (ArrayList<Integer>) gsfLocation.clone();
//                    // 人力成本
//                    Double rlcb = 0D;
//                    int rowNum = row.getRowNum();
//                    // 从第五行开始是数据
//                    if (rowNum > (4 - 1) && rowNum < lastRowNum) {
//                        // 获取这一行的人力成本
//                        Cell cell4 = row.getCell(4 - 1);
//                        CellType cellType = cell4.getCellType();
//                        String name = cellType.name();
//                        // 人力成本必须为数字
//                        if (!name.equals("NUMERIC")) {
//                            int rowIndex = cell4.getRowIndex();
//                            int columnIndex = cell4.getColumnIndex();
//                            return ResultInfo.error("第" + (columnIndex + 1) + "列, 第" + (rowIndex + 1) + "行, 人力成本必须为数字且不能为空");
//                        }
//
//                        rlcb = cell4.getNumericCellValue();
//
//
//                        // 这一行计算几次工时费
//                        for (Cell cell : row) {
//                            int columnIndex = cell.getColumnIndex();
//
//                            // 如果没有了就跳出去
//                            if (clone.size() == 0) {
//                                break;
//                            }
//                            // 开始计算公式费
//                            // 获取projectNum数的工时占比
//                            if (columnIndex == clone.get(0)) {
//                                // 工时占比
//                                CellType cellGszb = cell.getCellType();
//
//                                if (!cellGszb.name().equals("NUMERIC") && !(cellGszb.name().equals("BLANK"))) {
//                                    int rowIndex1 = cell.getRowIndex();
//                                    int columnIndex1 = cell.getColumnIndex();
//                                    return ResultInfo.error("第" + (columnIndex1 + 1) + "列, 第" + (rowIndex1 + 1) + "行必须为百分比数字");
//                                }
//
//                                double gszb = cell.getNumericCellValue();
//                                double sjFee = gszb * rlcb;
//
//                                // 这工时费单元格设置值
//                                int columnIndexNext = cell.getColumnIndex();
//                                Cell cellGSF = row.getCell(++columnIndexNext);
//
//                                // 设置样式, 工时费保留2位小数
//                                CellStyle cellStyle = cellGSF.getCellStyle();
//                                cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
//
//                                cellGSF.setCellValue(sjFee);
//                                System.out.println(sjFee);
//                                clone.remove(0);
//                            }
//
//                        }
//                        System.out.println("\t");
//                    }
//                }
//
//                statistic(sheet, gsfLocation, dataFormat);
//
//                // 输出为文件
//                String stringTime = getStringTime();
//                String r = stringTime + file_name;
//
//                File file1 = new File(uploadPath + File.separator + r);
//                FileOutputStream fileOutputStream = new FileOutputStream(file1);
//                workbook.write(fileOutputStream);
//                long length = file1.length();
//
//
//                FileManage fileManageNew = new FileManage();
//                fileManageNew.setFile_type("xlsx");
//                fileManageNew.setFile_name(r);
//                fileManageNew.setStore_name(fileManage.getStore_name());
//                fileManageNew.setFile_size(String.valueOf(length));
//                fileManageNew.setCreate_time(new Date());
//                fileManageNew.setCreate_user(user.getId());
//                this.fileManageService.insert(fileManageNew);
//
//                ManHourAssign manHourAssign = new ManHourAssign();
//                manHourAssign.setCreateTime(LocalDateTime.now());
//                manHourAssign.setUserId(user.getId());
//                manHourAssign.setNewFileId(fileManageNew.getId());
//                manHourAssign.setOriginFileId(fileId);
//                this.insert(manHourAssign);
//
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } finally {
//                try {
//                    if (workbook != null) {
//                        workbook.close();
//                    }
//                    fileInputStream.close();
//
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } catch (Exception e) {
//            return ResultInfo.error("计算失败, 请确认文件样式及位置是否正确");
//        }

//        }

//    }


//


//}
