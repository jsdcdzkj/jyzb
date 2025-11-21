package com.jsdc.rfid.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ConsumableDao;
import com.jsdc.rfid.mapper.ConsumableMapper;
import com.jsdc.rfid.mapper.FileManageMapper;
import com.jsdc.rfid.model.Consumable;
import com.jsdc.rfid.model.FileManage;
import com.jsdc.rfid.model.SysUser;
import com.jsdc.rfid.vo.ConsumableVo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Service
@Transactional
public class ConsumableService extends BaseService<ConsumableDao, Consumable> {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ConsumableMapper consumableMapper ;

    @Autowired
    private FileManageMapper fileManageMapper;

    @Value("${file.upload-path}")
    private String uploadPath;


    /**
    *耗材类型新增
    * Author wzn
    * Date 2023/5/15 10:50
    */
    public ResultInfo addConsumable(Consumable consumable) {
        QueryWrapper<Consumable> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("consumable_name",consumable.getConsumable_name()) ;
        queryWrapper.eq("is_del","0") ;
        Integer count = consumableMapper.selectCount(queryWrapper) ;

        if(0 != count){
            return ResultInfo.error(consumable.getConsumable_name()+"名称已存在");
        }

        if(null == consumable.getParent_id()){
            consumable.setParent_id(0);
        }

        SysUser sysUser = sysUserService.getUser();
        consumable.setCreate_time(new Date());
        consumable.setCreate_user(sysUser.getId());
        consumable.setIs_del(G.ISDEL_NO);
        consumable.insert();
        return ResultInfo.success() ;
    }

    /**
    *耗材类型修改
    * Author wzn
    * Date 2023/5/15 10:52
    */
    public void updateConsumable(Consumable consumable) {

        Integer num = selectCount(Wrappers.<Consumable>lambdaQuery()
                .eq(Consumable::getConsumable_name, consumable.getConsumable_name())
                .eq(Consumable::getIs_del, G.ISDEL_NO)
                .ne(Consumable::getId, consumable.getId())
        );
        if (num > 0) {
            throw new RuntimeException("耗材名称已存在");
        }
        // 找到原本的耗材
        Consumable old = consumableMapper.selectById(consumable.getId());
        if (old.getId() == consumable.getParent_id()){
            throw new RuntimeException("不能选择自己作为父级");
        }
        SysUser sysUser = sysUserService.getUser();
        consumable.setUpdate_user(sysUser.getId());
        consumable.setUpdate_time(new Date());
        consumable.updateById();
    }

    /**
    *耗材类型列表
    * Author wzn
    * Date 2023/5/15 10:55
    */
    public PageInfo<Consumable> consumableList(Consumable consumable) {
        PageHelper.startPage(consumable.getPage(), consumable.getLimit());
        QueryWrapper<Consumable> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        if(StringUtils.isNotBlank(consumable.getConsumable_name())){
            queryWrapper.like("consumable_name", consumable.getConsumable_name());

        }
        List<Consumable> a = consumableMapper.selectList(queryWrapper);
        List<Consumable> b = new ArrayList<>() ;
        if(CollectionUtil.isNotEmpty(a)){
            for(Consumable c:a){
                if(null != c.getParent_id()){
                    Consumable consumable1 =  consumableMapper.selectById(c.getParent_id()) ;
                    if(null != consumable1){
                        c.setParent_name(consumable1.getConsumable_name());
                        // 判断集合中是否包含 consumable1 如果没有则加入a集合中
                        if(!a.contains(consumable1) && StringUtils.equals(consumable1.getIs_del(),"0")){
                            b.add(consumable1);
                        }
                    }

                }
                b.add(c) ;

            }
        }
        return new PageInfo<>(b);
    }

    /**
     *首页库存预警详情列表
     * Author wzn
     * Date 2023/5/17 10:02
     */
    public PageInfo<ConsumableVo> consumablePageList(ConsumableVo consumableVo) {
        PageHelper.startPage(consumableVo.getPage(), consumableVo.getLimit());
        return new PageInfo<>(consumableMapper.earlyWarningList());
    }

    /**
     *首页库存预警详情数量
     * Author wzn
     * Date 2023/5/17 10:02
     */
    public Integer consumableCountList() {

        return consumableMapper.earlyWarningList().size();
    }


    /**
    *耗材类型详情
    * Author wzn
    * Date 2023/5/15 11:03
    */
    public Consumable info(String id) {
        return consumableMapper.selectById(id);
    }




    public ResultInfo toExportTemplate(HttpServletResponse response) {
        //导出excel模板
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("耗材名称", StringUtils.EMPTY);
        row.put("父级耗材类型", StringUtils.EMPTY);
        row.put("预警值", StringUtils.EMPTY);

        list.add(row);


        ExcelWriter writer = ExcelUtil.getWriter();
        StyleSet styleSet = writer.getStyleSet();
        Sheet sheet = writer.getSheet();
        //设置下拉数据 从第几行开始
        int firstRow = 1;

        // 所有耗材类型
        QueryWrapper<Consumable> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("is_del","0") ;
        queryWrapper.eq("parent_id","0") ;
        List<Consumable> consumableList = consumableMapper.selectList(queryWrapper);
        String[] unitList = consumableList.stream().map(Consumable::getConsumable_name).toArray(String[]::new);
        if (!CollectionUtils.isEmpty(consumableList)) {
            writer.addValidationData(setSelectCol(styleSet, sheet, unitList, firstRow, 1));
        }

        writer.setOnlyAlias(true);
        writer.autoSizeColumnAll();

        writer.write(list, true);

        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("耗材类型模板.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private DataValidation setSelectCol(StyleSet styleSet, Sheet sheet, String[] capacityAvi, int firstRow, int firstCol) {

        CellStyle cellStyle = styleSet.getCellStyle();
        //规定格式
        cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("text"));

        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 设置辅修下拉框数据
//        String[] capacityAvi = {"是", "否"};
        DataValidationConstraint capacityConstraint = helper.createExplicitListConstraint(capacityAvi);
        //需要被设置为下拉数据的单元格范围
        CellRangeAddressList capacityList = new CellRangeAddressList(firstRow, 5000, firstCol, firstCol);
        return helper.createValidation(capacityConstraint, capacityList);
    }


    /**
    *耗材类型导入接口
    * Author wzn
    * Date 2023/5/15 19:22
    */
    public ResultInfo toAssetImport(Integer fileId) {
        SysUser sysUser = sysUserService.getUser() ;
        FileManage fileManage = fileManageMapper.selectById(fileId);
        String path = uploadPath + File.separator + fileManage.getFile_name();
        File file = new File(path);
        try{
            ExcelReader reader = ExcelUtil.getReader(file);
            List<Map<String,Object>> readAll = reader.readAll();
            if(CollectionUtils.isEmpty(readAll)){
                return ResultInfo.error("导入的数据为空");
            }

            for (Map<String,Object> map : readAll) {
                String consumableName = MapUtils.getString(map, "耗材名称");
                if(StringUtils.isBlank(consumableName)){
                    return ResultInfo.error("耗材名称不能为空");
                }
                String rewarding_value = MapUtils.getString(map, "预警值");
                Integer count = consumableMapper.selectCount(Wrappers.<Consumable>query()
                        .eq("consumable_name", consumableName)
                        .eq("is_del", G.ISDEL_NO)
                );

                if(0 != count){
                    return ResultInfo.error(consumableName + "名称已存在");
                }
                String parent_name = MapUtils.getString(map, "父级耗材类型");

                List<Consumable> consumables = consumableMapper.selectList(Wrappers.<Consumable>query()
                        .eq("consumable_name", parent_name)
                        .and(x -> x.isNull("parent_id").or().eq("parent_id", "") )
                        .eq("is_del", G.ISDEL_NO)
                ) ;

                if(!CollectionUtils.isEmpty(consumables)){
                    Consumable consumable = consumables.get(0) ;
                    Integer parent_id = consumable.getId() ;
                    Consumable newConsumable = Consumable.builder()
                            .parent_id(parent_id)
                            .prewarning_value(StringUtils.isBlank(rewarding_value)?"0":rewarding_value)
                            .consumable_name(consumableName)
                            .create_time(new Date())
                            .create_user(sysUser.getId())
                            .is_del(G.ISDEL_NO)
                            .build();
                    consumableMapper.insert(newConsumable);
                }else {
                    Consumable newConsumable = Consumable.builder()
                            .consumable_name(parent_name)
                            .parent_id(0)
                            .create_time(new Date())
                            .create_user(sysUser.getId())
                            .is_del(G.ISDEL_NO)
                            .build();
                    consumableMapper.insert(newConsumable);

                    Consumable newConsumable1 = Consumable.builder()
                            .parent_id(newConsumable.getId())
                            .prewarning_value(StringUtils.isBlank(rewarding_value)?"0":rewarding_value)
                            .consumable_name(consumableName)
                            .create_time(new Date())
                            .create_user(sysUser.getId())
                            .is_del(G.ISDEL_NO)
                            .build();
                    consumableMapper.insert(newConsumable1);
                }
            }

        } catch (Exception e) {
            return ResultInfo.error("导入失败: " + e.getMessage());
        }
        return ResultInfo.success();
    }

    public List<Consumable> getTreeData() {
        List<Consumable> consumables  = selectList(Wrappers.<Consumable>lambdaQuery().eq(Consumable::getIs_del, G.ISDEL_NO));
        List<Consumable> treeData = new ArrayList<>();
        for (Consumable consumable : consumables) {
            if (consumable.getParent_id() == 0) {
                treeData.add(consumable);
            }
            for (Consumable consumable1 : consumables) {
                if (consumable1.getParent_id().equals(consumable.getId())) {
                    if (consumable.getChildren() == null) {
                        consumable.setChildren(new ArrayList<>());
                    }
                    consumable.getChildren().add(consumable1);
                }
            }
        }
        return treeData;
    }

    public List<Tree<String>> getTreeDataForUni() {
        List<Consumable> consumables  = selectList(Wrappers.<Consumable>lambdaQuery().eq(Consumable::getIs_del, G.ISDEL_NO));
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("value");
        treeNodeConfig.setNameKey("label");
        treeNodeConfig.setParentIdKey("parent_id");
        return TreeUtil.build(consumables, "0", treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId().toString());
            tree.setName(treeNode.getConsumable_name());
            tree.setParentId(treeNode.getParent_id().toString());
        });
    }
}
