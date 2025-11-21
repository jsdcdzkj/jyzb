package com.jsdc.rfid.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.ConsInventoryManagementDao;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import com.jsdc.rfid.utils.ExcelUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import vo.ConsDataVo;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zhangdequan
 * @create 2022-06-07 11:53:30
 */
@Service
@Transactional
public class ConsInventoryManagementService extends BaseService<ConsInventoryManagementDao, ConsInventoryManagement> {

    @Autowired
    private ConsInventoryManagementMapper consInventoryManagementMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ConsWarehousingManagementMapper consWarehousingManagementMapper;

    @Autowired
    private CommonDataTools commonDataTools;

    @Autowired
    private ConsPurchaseApplyMapper consPurchaseApplyMapper;

    @Autowired
    private ConsPurchaseDetailMapper consPurchaseDetailMapper;

    @Autowired
    private SysDictMapper sysDictMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;

    @Autowired
    private FileManageMapper fileManageMapper;

    @Autowired
    private ConsCategoryMapper consCategoryMapper;

    @Autowired
    private ConsAssettypeMapper consAssettypeMapper;

    @Autowired
    private ConsSpecificationMapper consSpecificationMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private ConsInAndOutStatisticsService consInAndOutStatisticsService;

    @Autowired
    private BrandManageMapper brandManageMapper;

    @Autowired
    private ConsumableMapper consumableMapper ;

    @Value("${file.upload-path}")
    private String uploadPath;

    /**
     * 耗材库存总数
     *
     * @Author thr
     */
    public ConsInventoryManagement getTotalCount(ConsInventoryManagement bean) {
        return consInventoryManagementMapper.getTotalCount(bean);
    }

    /**
     * 以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     * 当前库存
     *
     * @Author thr
     */
    public List<ConsDataVo> getInventoryNum(ConsInventoryManagement bean) {
        return consInventoryManagementMapper.getInventoryNum(bean);
    }

    /**
     * 库存列表查询
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    public PageInfo<ConsInventoryManagement> toList(Integer pageIndex, Integer pageSize, ConsInventoryManagement beanParam) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");

        List<ConsInventoryManagement> consInventoryManagementVos = consInventoryManagementMapper.selectList(Wrappers.<ConsInventoryManagement>lambdaQuery()
                .like(StringUtils.isNotBlank(beanParam.getName()), ConsInventoryManagement::getName, beanParam.getName())
                .eq(null != beanParam.getAsset_name_id(), ConsInventoryManagement::getAsset_name_id, beanParam.getAsset_name_id())
                .eq(null != beanParam.getAsset_type_id(), ConsInventoryManagement::getAsset_type_id, beanParam.getAsset_type_id())
                .eq(null != beanParam.getWarehouse_id(), ConsInventoryManagement::getWarehouse_id, beanParam.getWarehouse_id())
//                .gt(null != beanParam.getInventory_num(), ConsInventoryManagement::getInventory_num, beanParam.getInventory_num())
                .eq(null != beanParam.getConsumable_id(), ConsInventoryManagement::getConsumable_id, beanParam.getConsumable_id())
                .eq(ConsInventoryManagement::getIs_del, String.valueOf(0))
        );
        //得到名称
        Map<Integer, ConsAssettype> assetNameMap = commonDataTools.getConsAssettypeMap();
        //得到品类
        Map<Integer, ConsCategory> categoryMap = commonDataTools.getConsCategoryMap();
        //得到仓库
        Map<Integer, Warehouse> warehouseMap = commonDataTools.getWarehouseMap();
        //得到品牌
        Map<Integer, SysDict> brandMap = commonDataTools.getSysDictMap("brand_type");
        //得到单位
        Map<Integer, SysDict> unitMap = commonDataTools.getSysDictMap("unit");
        //得到规格型号
        Map<Integer, ConsSpecification> specificationMap = commonDataTools.getConsSpecificationMap();

        for (ConsInventoryManagement consInventoryManagement : consInventoryManagementVos) {
            consInventoryManagement.setAsset_name(commonDataTools.getValue(assetNameMap, consInventoryManagement.getAsset_name_id(), "name"));
            if(null != consInventoryManagement.getConsumable_id() && null != consumableMapper.selectById(consInventoryManagement.getConsumable_id())){
                consInventoryManagement.setConsumable_name(consumableMapper.selectById(consInventoryManagement.getConsumable_id()).getConsumable_name());
            }
            consInventoryManagement.setAssetTypeName(commonDataTools.getValue(categoryMap, consInventoryManagement.getAsset_type_id(), "name"));
            consInventoryManagement.setWarehouse_name(commonDataTools.getValue(warehouseMap, consInventoryManagement.getWarehouse_id(), "warehouse_name"));
            BrandManage brandManage = brandManageMapper.selectById(consInventoryManagement.getBrand_id());
            consInventoryManagement.setBrand_name(null != brandManage ? brandManage.getBrand_name() : "");
            consInventoryManagement.setUnit_name(commonDataTools.getValue(unitMap, consInventoryManagement.getUnit_of_measurement(), "label"));
            consInventoryManagement.setSpecifications_name(commonDataTools.getValue(specificationMap, consInventoryManagement.getSpecifications(), "typename"));
        }

        PageInfo<ConsInventoryManagement> page = new PageInfo<>(consInventoryManagementVos);

        return page;
    }

    public List<ConsInventoryManagement> getList(ConsInventoryManagement beanParam) {

        List<ConsInventoryManagement> consInventoryManagementVos = consInventoryManagementMapper.toList(beanParam);

        return consInventoryManagementVos;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public ResultInfo getById(Integer id) {
        QueryWrapper<ConsInventoryManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del",0);
        queryWrapper.eq("id",id);
        ConsInventoryManagement consInventoryManagement = selectOne(queryWrapper);
        return ResultInfo.success(consInventoryManagement);
    }

    /**
     *  添加
     */
    public ResultInfo addConsInventoryManagement(ConsInventoryManagement bean) {
        // 删除状态
        bean.setIs_del(String.valueOf(0));
        // 创建时间
        bean.setCreate_time(new Date());
        // 创建者
        bean.setCreate_user(sysUserService.getUser().getId());
        insert(bean);
        return ResultInfo.success();
    }

    /**
     * 添加
     * @param bean
     * @return
     */
    public ResultInfo addInventoryManagement(ConsInventoryManagement bean) {
        // 删除状态
        bean.setIs_del(String.valueOf(0));
        // 创建时间
        bean.setCreate_time(new Date());
        // 创建者
        bean.setCreate_user(sysUserService.getUser().getId());
        insert(bean);
        return ResultInfo.success();
    }

    /**
     *  根据采购 存入库存
     */
    public ResultInfo addInventoryManagementById(Integer purchaseApply_id) {
        ConsPurchaseApply purchaseApply = consPurchaseApplyMapper.selectById(purchaseApply_id);
        if(null == purchaseApply){
            return ResultInfo.error("采购申请不存在");
        }
        List<ConsPurchaseDetail> purchaseDetails = consPurchaseDetailMapper.selectList(Wrappers.<ConsPurchaseDetail>lambdaQuery()
                .eq(ConsPurchaseDetail::getPurchase_apply_id, purchaseApply_id).eq(ConsPurchaseDetail::getIs_del, String.valueOf(0)));
        if(org.apache.shiro.util.CollectionUtils.isEmpty(purchaseDetails)){
            return ResultInfo.error("采购申请没有明细");
        }

        // 修改采购单,为已入库
        purchaseApply.setPut_status(String.valueOf(2));
        consPurchaseApplyMapper.updateById(purchaseApply);

        // 添加入库管理
        ConsWarehousingManagement warehousingManagement = new ConsWarehousingManagement();
        // 入库类型 1采购入库 2手动入库
        if(null == purchaseApply.getPurchase_type() || purchaseApply.getPurchase_type().equals(String.valueOf(1))){
            warehousingManagement.setInbound_type(1);
        }else {
            warehousingManagement.setInbound_type(2);
        }

        warehousingManagement.setOperation_record(warehousingManagement.getInbound_type() == 1?"采购入库":"手动入库");
        warehousingManagement.setInbound_order_number(commonDataTools.getNo(DataType.CONS_RECEIPT_CODE.getType(), warehousingManagement));
        warehousingManagement.setSupplier_id(purchaseApply.getSupplier_id());
        warehousingManagement.setInbound_amount(purchaseApply.getActual_amount());
        //inbound_status 1:待入库 2:已入库
        warehousingManagement.setInbound_status(2);

        warehousingManagement.setPurchase_apply_id(purchaseApply_id);
        warehousingManagement.setCreate_time(new Date());
        warehousingManagement.setCreate_user(sysUserService.getUser().getId());
        warehousingManagement.setIs_del(String.valueOf(0));
        consWarehousingManagementMapper.insert(warehousingManagement);

        // 添加库存管理
        for (ConsPurchaseDetail purchaseDetail : purchaseDetails) {
            ConsInventoryManagement inventoryManagement = new ConsInventoryManagement();
            inventoryManagement.setAsset_type_id(purchaseDetail.getCategory_id());
            inventoryManagement.setConsumable_id(purchaseDetail.getConsumable_id2());
            if(StringUtils.isNotBlank(purchaseDetail.getName())){
                inventoryManagement.setName(purchaseDetail.getName());
            }

            inventoryManagement.setModel(purchaseDetail.getModel());
            inventoryManagement.setAsset_name_id(purchaseDetail.getAssets_type_id());
            inventoryManagement.setSpecifications(purchaseDetail.getSpecification_id());
            inventoryManagement.setWarehouse_id(purchaseDetail.getWarehouse_id());
            inventoryManagement.setUnit_price(purchaseDetail.getPurchase_price());
            inventoryManagement.setActual_amount(purchaseDetail.getActual_price());
            inventoryManagement.setWarehousing_time(new Date());
            // 1:"初始化导入", 2:"入库单入库"
            inventoryManagement.setWarehousing_mode(2);
            inventoryManagement.setInventory_num(purchaseDetail.getInbound_num());
            inventoryManagement.setUnit_of_measurement(purchaseDetail.getUnits());
            // 品牌id
            inventoryManagement.setBrand_id(purchaseDetail.getAssets_brand());
            // 生产日期
            inventoryManagement.setProduction_date(purchaseDetail.getProduction_date());
            // 质保期
            inventoryManagement.setWarranty_period(purchaseDetail.getWarranty_period());

            inventoryManagement.setCreate_time(new Date());
            inventoryManagement.setCreate_user(sysUserService.getUser().getId());
            inventoryManagement.setIs_del(String.valueOf(0));
            consInventoryManagementMapper.insert(inventoryManagement);

            BigDecimal dj = new BigDecimal(inventoryManagement.getActual_amount() == null ? "0" : inventoryManagement.getActual_amount());
            BigDecimal sl = new BigDecimal(inventoryManagement.getInventory_num() == null ? 0 : inventoryManagement.getInventory_num());
            // 计算实际金额
            BigDecimal actualAmount = dj.multiply(sl);
            // 加入出入库统计记录
            consInAndOutStatisticsService.insertCons(ConsInAndOutStatistics.builder()
                    //耗材品类id
                    .consumable_name(consumableMapper.selectById(inventoryManagement.getConsumable_id()).getConsumable_name())
                    //耗材名称
                    .name(inventoryManagement.getName())
                    //生产日期
                    .production_date(inventoryManagement.getProduction_date())
                    // 质保期
                    .warranty_period(inventoryManagement.getWarranty_period())
                    // 单位
                    .unit_id(inventoryManagement.getUnit_of_measurement())
                    // 单价
                    .unit_price(inventoryManagement.getActual_amount())
                    // 期初数量
                    .initial_number(0)
                    // 入库数量
                    .in_number(inventoryManagement.getInventory_num())
                    // 出库数量
                    .out_number(0)
                    // 期末数量
                    .final_number(inventoryManagement.getInventory_num())
                    // 总金额
                    .total_amount(actualAmount.toString())
                    // 规格
                    .model(inventoryManagement.getModel())
                    // 仓库id
                    .warehouse_id(inventoryManagement.getWarehouse_id())
                    .type(1)
                    .build());
        }

        return ResultInfo.success("操作成功");
    }

    public ResultInfo editInventoryManagement(ConsInventoryManagement bean) {
        // 修改者
        bean.setUpdate_user(sysUserService.getUser().getId());
        // 修改时间
        bean.setUpdate_time(new Date());
        updateById(bean);
        return ResultInfo.success();
    }

    /**
     * 分页查询入库信息
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    public PageInfo<ConsWarehousingManagement> toWarehousingList(Integer pageIndex, Integer pageSize, ConsWarehousingManagement beanParam) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");

        List<ConsWarehousingManagement> warehousingManagementVos = consWarehousingManagementMapper.selectList(Wrappers.<ConsWarehousingManagement>lambdaQuery()
                .like(StringUtils.isNotBlank(beanParam.getInbound_order_number()), ConsWarehousingManagement::getInbound_order_number, beanParam.getInbound_order_number())
                .eq(null != beanParam.getInbound_type(), ConsWarehousingManagement::getInbound_type, beanParam.getInbound_type())
                .eq(ConsWarehousingManagement::getIs_del, String.valueOf(0)));
        Map<Integer, Supplier> supplierMap = commonDataTools.getSupplierMap();
        for (ConsWarehousingManagement warehousingManagementVo : warehousingManagementVos) {
            warehousingManagementVo.setSupplier_name(commonDataTools.getValue(supplierMap, warehousingManagementVo.getSupplier_id(), "supplier_name"));
        }

        return new PageInfo<>(warehousingManagementVos);
    }

    /**
     *  入库管理 ID查询
     */
    public ResultInfo getWarehousingById(Integer id) {
        ConsWarehousingManagement warehousingManagement = consWarehousingManagementMapper.selectById(id);
        if (null == warehousingManagement) {
            return ResultInfo.error("没有找到对应的数据");
        }
        // 查询采购的详情信息

        return ResultInfo.success(warehousingManagement);
    }

    public ResultInfo importWarehousing(MultipartFile importFile) {
        List<ConsWarehousingManagement> warehousingManagementList = ExcelUtils.read(importFile, WarehousingManagement.class);
        if(CollectionUtils.isEmpty(warehousingManagementList)){
            return ResultInfo.error("非法入参");
        }
        for(ConsWarehousingManagement member : warehousingManagementList){
            ConsWarehousingManagement bean = new ConsWarehousingManagement();
            // 必选字段判断
            if(StringUtils.isBlank(member.getWarehouse_name())){
                return ResultInfo.error("必填字段不能为空");
            }
            consWarehousingManagementMapper.insert(bean);
        }

        return ResultInfo.success();
    }

    /**
     * 入库管理 导入模板下载
     */
    public ResultInfo download(HttpServletResponse response) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> row = new LinkedHashMap<>();
//        服务群组、部门、状态、标题、描述、请求类型、工单类别、优先级、开单人、
//        开单时间、处理人、响应时间、关单时间、解决方案、 用户必填项
//        row.put("编号", member.getId());

        row.put("服务群组", StringUtils.EMPTY);
        row.put("部门名称", StringUtils.EMPTY);
        row.put("状态", StringUtils.EMPTY);
        row.put("描述", StringUtils.EMPTY);
        row.put("请求类型", StringUtils.EMPTY);
        row.put("工单类别", StringUtils.EMPTY);
        row.put("优先级", StringUtils.EMPTY);
        row.put("开单人", StringUtils.EMPTY);
        row.put("开单时间", StringUtils.EMPTY);
        row.put("处理人", StringUtils.EMPTY);
        row.put("响应时间", StringUtils.EMPTY);
        row.put("关单时间", StringUtils.EMPTY);
        row.put("解决方案", StringUtils.EMPTY);
        row.put("用户username", StringUtils.EMPTY);

        list.add(row);

        ExcelWriter writer = ExcelUtil.getWriter();
//        ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeBeanTest.xlsx");
//
        writer.setOnlyAlias(true);
        writer.write(list, true);
//        writer.close();

        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("123.xls", "UTF-8"));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultInfo.success(null);
    }

    /**
     * 编辑入库
     * @param warehousingManagement
     * @return
     */
    public ResultInfo editWarehousing(ConsWarehousingManagement warehousingManagement) {
        // 设置修改时间
        warehousingManagement.setUpdate_time(new Date());
        // 设置修改者
        warehousingManagement.setUpdate_user(sysUserService.getUser().getId());
        consWarehousingManagementMapper.updateById(warehousingManagement);
        return ResultInfo.success();
    }

    /**
     *  入库管理 添加
     */
    public ResultInfo addWarehousing(ConsWarehousingManagement warehousingManagement) {
        if(null == warehousingManagement || null == warehousingManagement.getInbound_type()){
            return ResultInfo.error("没有找到对应的数据");
        }
        // 入库单编码：RK+入库单来源（采购申请：SQ；手动创建：SD）+日期+三位自增码（每日重置）
        warehousingManagement.setInbound_order_number(commonDataTools.getNo(DataType.INBOUND_ORDER_NUMBER.getType(), warehousingManagement));
        switch (warehousingManagement.getInbound_type()){
            case 1:
                // 采购入库

                break;
            case 2:
                // 手动入库

                break;
            default:
                return ResultInfo.error("入库类型错误");
        }

        // 设置创建时间
        warehousingManagement.setCreate_time(new Date());
        // 设置存在状态
        warehousingManagement.setIs_del(String.valueOf(0));
        consWarehousingManagementMapper.insert(warehousingManagement);
        return ResultInfo.success();
    }

    /**
     * 入库管理 导入
     * @param response
     * @return
     */
    public ResultInfo toExportTemplate(HttpServletResponse response) {
        //导出excel模板
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> row = new LinkedHashMap<>();
        for (int i = 0; i < 50; i++) {
            row.put("耗材类型", StringUtils.EMPTY);
            row.put("标签", StringUtils.EMPTY);
            row.put("规格型号", StringUtils.EMPTY);
            row.put("单价", StringUtils.EMPTY);
            row.put("实际金额", StringUtils.EMPTY);
            row.put("入库数量", StringUtils.EMPTY);
            row.put("计量单位", StringUtils.EMPTY);
            row.put("品牌", StringUtils.EMPTY);
            row.put("供应商", StringUtils.EMPTY);
            row.put("部门", StringUtils.EMPTY);
            row.put("仓库", StringUtils.EMPTY);
            row.put("生产日期(xxxx-xx-xx)", StringUtils.EMPTY);
            row.put("质保期(单位:月)", StringUtils.EMPTY);
            list.add(row);
        }

        ExcelWriter writer = ExcelUtil.getWriter();
        StyleSet styleSet = writer.getStyleSet();
        Sheet sheet = writer.getSheet();
        //设置下拉数据 从第几行开始
        int firstRow = 1;


        // 设置只导出有别名的字段
        writer.setOnlyAlias(true);
        // 设置默认行高
        writer.setDefaultRowHeight(20);
        // 设置冻结行
        writer.setFreezePane(1);

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        int colNum = 0;

        writeCell(writer, colNum, "耗材类型", true);
        colNum = colNum + 1;
        writeCell(writer, colNum, "标签", false);
        colNum = colNum + 1;
        writeCell(writer, colNum, "规格型号", true);
        colNum = colNum + 1;
        writeCell(writer, colNum, "单价", true);
        colNum = colNum + 1;
        writeCell(writer, colNum, "实际金额", true);
        colNum = colNum + 1;
        writeCell(writer, colNum, "入库数量", true);
        colNum = colNum + 1;
        writeCell(writer, colNum, "计量单位", true);
        colNum = colNum + 1;
        writeCell(writer, colNum, "品牌", false);
        colNum = colNum + 1;
        writeCell(writer, colNum, "供应商", false);
        colNum = colNum + 1;
        writeCell(writer, colNum, "部门", true);
        colNum = colNum + 1;
        writeCell(writer, colNum, "仓库", true);
        colNum = colNum + 1;
        writeCell(writer, colNum, "生产日期(xxxx-xx-xx)", false);
        colNum = colNum + 1;
        writeCell(writer, colNum, "质保期(单位:月)", true);

        //耗材类型
        QueryWrapper<Consumable> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("is_del","0") ;
        queryWrapper.ne("parent_id","0") ;
        List<Consumable> consumableList = consumableMapper.selectList(queryWrapper);
        List<String> unitList1 = consumableList.stream().map(Consumable::getConsumable_name).collect(Collectors.toList());

        // 计量单位信息
        List<SysDict> units = sysDictMapper.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getType, "unit").eq(SysDict::getIs_del, "0"));
        List<String> unitList = units.stream().map(SysDict::getLabel).collect(Collectors.toList());

        //仓库信息
        List<Warehouse> warehouses = warehouseMapper.selectList(Wrappers.<Warehouse>lambdaQuery().eq(Warehouse::getIs_del, "0"));
        List<String> warehouseList = warehouses.stream().map(Warehouse::getWarehouse_name).collect(Collectors.toList());

        //品牌信息
        List<BrandManage> brands = brandManageMapper.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, "0"));
        List<String> brandList = brands.stream().map(BrandManage::getBrand_name).collect(Collectors.toList());
        //供应商信息
        List<Supplier> suppliers = supplierMapper.selectList(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getIs_del, "0"));
        List<String> supplierList = suppliers.stream().map(Supplier::getSupplier_name).collect(Collectors.toList());
        //部门信息
        List<SysDepartment> depts = sysDepartmentMapper.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, "0"));
        List<String> deptList = depts.stream().map(SysDepartment::getDept_name).collect(Collectors.toList());

        //构建数据
        List<List<?>> rows = CollUtil.newArrayList(unitList1,unitList, brandList, supplierList, deptList, warehouseList);

        String[] types = {"A", "B","C", "D", "E", "F", "G", "H", "I", "J", "K", "L","M", "N","O", "P", "Q", "R", "S", "T", "U", "V", "W","X","Y","Z"};

//        writeCell(writer, 8, "品牌", false);
//        writeCell(writer, 9, "供应商", true);
//        writeCell(writer, 10, "部门", true);
        Integer[] firstCol = {0,6,7,8,9, 10};

        for (int i=0; i< rows.size(); i++){
            List<?> cols = rows.get(i);
            String dictSheet = "dict" + i;
            //创建第二个Sheet
            writer.setSheet(dictSheet);
            //将Sheet2中的数据引用到Sheet1中的下拉框
            Workbook workbook = writer.getWorkbook();
            Name namedCell = workbook.createName();
            namedCell.setNameName(dictSheet);
            //加载数据,将名称为hidden的
            DVConstraint constraint = DVConstraint.createFormulaListConstraint(dictSheet);
            for (int j=0; j<cols.size(); j++){
                writer.writeCellValue(i, j, cols.get(j));
            }

            if (CollectionUtils.isEmpty(cols)){
                continue;
            }
            namedCell.setRefersToFormula(dictSheet + "!$" + types[i] + "$1:$" + types[i] + "$" + cols.size());
            // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
            HSSFDataValidation validation = new HSSFDataValidation(new CellRangeAddressList(1, 1000, firstCol[i], firstCol[i]), constraint);
            writer.getSheets().get(0).addValidationData(validation);

            workbook.setSheetHidden(i+1, true);
        }


        OutputStream outputStream = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("耗材入库模板.xls", "UTF-8"));
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

    /**
     * 输入标题到excel
     * @param writer excel对象
     * @param column 当前列位置
     * @param cellValue 标题内容
     * @param requiredFlag 是否标红
     */
    private void writeCell(ExcelWriter writer, int column, String cellValue, boolean requiredFlag){
        // 根据x,y轴设置单元格内容
        writer.writeCellValue(column , 0, cellValue);
        Font font = writer.createFont();
        font.setBold(true);
        // 设置字体宽度
        Integer width = 15;
        if (cellValue.length() > 5 && cellValue.length() <= 8){
            width = 18;
        } else if (cellValue.length() > 8){
            width = 25;
        }
        if (requiredFlag){
            // 根据x,y轴获取当前单元格样式
            CellStyle cellStyle = writer.createCellStyle(column, 0);
            // 内容水平居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            // 内容垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 设置边框
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            // 设置高度
            writer.setColumnWidth(column, width);
            // 字体颜色标红
            font.setColor(Font.COLOR_RED);
            cellStyle.setFont(font);
            // 填充前景色
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else {
            // 根据x,y轴获取当前单元格样式
            CellStyle cellStyle = writer.createCellStyle(column, 0);
            // 内容水平居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            // 内容垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 设置边框
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            // 设置高度
            writer.setColumnWidth(column, width);
            // 填充前景色
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cellStyle.setFont(font);
        }
    }

    /**
     * 设置下拉选项
     */
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
     * 耗材入库
     * @param fileId
     * @return
     */
    public ResultInfo toAssetImport(Integer fileId) {
        FileManage fileManage = fileManageMapper.selectById(fileId);
        String path = uploadPath + File.separator + fileManage.getFile_name();
        File file = new File(path);
        try{
            ExcelReader reader = ExcelUtil.getReader(file);
            List<Map<String,Object>> readAll = reader.readAll();
            if(CollectionUtils.isEmpty(readAll)){
                throw new RuntimeException("导入的数据为空");
            }

            List<ConsInventoryManagement> list = new ArrayList<>();
            // 设置errorList，加入校验失败的行号和原因
            List<Map<String, Object>> errorList = new ArrayList<>();
            for (Map<String,Object> map : readAll) {
                // map的所有value 去除前后空格
                map.forEach((k, v) -> map.put(k, v.toString().trim()));
                // 行号
                map.put("rowNum", readAll.indexOf(map) + 2);
                // 错误集合
                List<String> errList = new ArrayList<>();

                // ----------------- 耗材名称 start -----------------
                String consumable_name = MapUtils.getString(map, "耗材类型");
                Integer consumable_id = null;
                if(StringUtils.isBlank(consumable_name)){
                    map.put("耗材类型tip", "耗材类型不能为空 ");
                    errList.add("耗材类型不能为空 ");
                } else {
                    List<Consumable> consumables = consumableMapper.selectList(Wrappers.<Consumable>lambdaQuery()
                            .eq(Consumable::getConsumable_name, consumable_name).eq(Consumable::getIs_del, "0"));
                    if(CollectionUtils.isEmpty(consumables)){
                        map.put("耗材类型tip", "耗材类型不存在 ");
                        errList.add("耗材类型不存在 ");
                    }else {
                        consumable_id = consumables.get(0).getId();
                    }
                }

//                String assetType =  MapUtils.getString(map,"耗材品类");
//                Integer asset_type_id = null;
//                if(StringUtils.isBlank(assetType)){
//                    map.put("耗材品类tip", "耗材品类不能为空 ");
//                    errList.add("耗材品类不能为空 ");
//                } else {
//                    List<ConsCategory> categories = consCategoryMapper.selectList(Wrappers.<ConsCategory>lambdaQuery()
//                            .eq(ConsCategory::getName, assetType).eq(ConsCategory::getIs_del, "0"));
//                    if(CollectionUtils.isEmpty(categories)){
//                        ConsCategory category = ConsCategory.builder().name(assetType).is_del("0").create_time(new Date()).build();
//                        consCategoryMapper.insert(category);
//                        asset_type_id = category.getId();
//                    }else {
//                        asset_type_id = categories.get(0).getId();
//                    }
//                }

                // ----------------- 耗材名称 end -----------------
                // ----------------- 耗材名称 start -----------------
                String name = MapUtils.getString(map, "标签");
//                if(StringUtils.isBlank(name)){
//                    map.put("标签tip", "标签不能为空 ");
//                    errList.add("标签不能为空 ");
//                }
                // ----------------- 耗材名称 end -----------------
                // ----------------- 状态 start -----------------
//                String status = MapUtils.getString(map, "状态");
//                Integer asset_state;
//                if(StringUtils.isBlank(status)){
//                    map.put("状态tip", "状态不能为空 ");
//                    errList.add("状态不能为空 ");
//                } else {
//                    asset_state = AssetsStatusEnums.getType(status);
//                    if(asset_state == null){
//                        map.put("状态tip", "状态不存在 ");
//                        errList.add("状态不存在 ");
//                    }
//
//                }
                // ----------------- 状态 end -----------------
                // ----------------- 规格型号 start -----------------
                String model = MapUtils.getString(map, "规格型号");
                Integer model_id = null;
                if(StringUtils.isBlank(model)){
                    map.put("规格型号tip", "规格型号不能为空 ");
                    errList.add("规格型号不能为空 ");
                } else {
                    List<ConsSpecification> specifications = consSpecificationMapper.selectList(Wrappers.<ConsSpecification>lambdaQuery()
                            .eq(ConsSpecification::getTypename, model).eq(ConsSpecification::getIs_del, "0"));

                    if(CollectionUtils.isEmpty(specifications)){
                        ConsSpecification specification = ConsSpecification.builder().typename(model).is_del("0").create_time(new Date()).build();
                        consSpecificationMapper.insert(specification);
                        model_id = specification.getId();
                    }else {
                        model_id = specifications.get(0).getId();
                    }
                }
                // ----------------- 规格型号 end -----------------
                // ----------------- 单价 start -----------------
                String price = MapUtils.getString(map, "单价");
                if(StringUtils.isBlank(price)){
                    map.put("单价tip", "单价不能为空 ");
                    errList.add("单价不能为空 ");
                }
                // 验证单价是否为数字
                if(!NumberUtils.isCreatable(price)){
                    map.put("单价tip", "单价必须为数字 ");
                    errList.add("单价必须为数字 ");
                }
                // ----------------- 单价 end -----------------
                // ----------------- 实际金额 start -----------------
                String actual_amount = MapUtils.getString(map, "实际金额");
                if(StringUtils.isBlank(actual_amount)){
                    map.put("实际金额tip", "实际金额不能为空 ");
                    errList.add("实际金额不能为空 ");
                }
                // 验证实际金额是否为数字
                if(!NumberUtils.isCreatable(actual_amount)){
                    map.put("实际金额tip", "实际金额必须为数字 ");
                    errList.add("实际金额必须为数字 ");
                }
                // ----------------- 实际金额 end -----------------
                // ----------------- 库存数量 start -----------------
                String stock = MapUtils.getString(map, "入库数量");
                if(StringUtils.isBlank(stock)){
                    map.put("入库数量tip", "入库数量不能为空 ");
                    errList.add("入库数量不能为空 ");
                }
                // 验证库存数量是否为数字
                if(!NumberUtils.isCreatable(stock)){
                    map.put("入库数量tip", "入库数量必须为数字 ");
                    errList.add("入库数量必须为数字 ");
                }
                // ----------------- 库存数量 end -----------------
                // ----------------- 计量单位 start -----------------
                String unit = MapUtils.getString(map, "计量单位");
                Integer unit_id = null;
                if(StringUtils.isBlank(unit)){
                    map.put("计量单位tip", "计量单位不能为空 ");
                    errList.add("计量单位不能为空 ");
                } else{
                    List<SysDict> units_list = sysDictMapper.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getIs_del, "0")
                            .eq(SysDict::getType, "unit").eq(SysDict::getLabel, unit));
                    if(CollectionUtils.isEmpty(units_list)){
                        map.put("计量单位tip", "计量单位不存在 ");
                        errList.add("计量单位不存在 ");
                    }else {
                        unit_id = units_list.get(0).getValue() == null ? null : Integer.parseInt(units_list.get(0).getValue());
                    }
                }
                // ----------------- 计量单位 end -----------------
                // ----------------- 供应商 start -----------------
                String supplier = MapUtils.getString(map, "供应商");
                Integer supplier_id = null;
                if (StringUtils.isNotBlank(supplier)) {
                    List<Supplier> suppliers = supplierMapper.selectList(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getIs_del, "0")
                            .eq(Supplier::getSupplier_name, supplier));
                    if(!CollectionUtils.isEmpty(suppliers)){
                        supplier_id = suppliers.get(0).getId();
                    }
                }
                // ----------------- 供应商 end -----------------
                // ----------------- 品牌 start -----------------
                String brand = MapUtils.getString(map, "品牌");
//                if (StringUtils.isBlank(brand)) {
//                    return ResultInfo.error("品牌不能为空");
//                }
                Integer brand_id = null;
                if(StringUtils.isNotBlank(brand)){
                    List<BrandManage> brand_typeList = brandManageMapper.selectList(Wrappers.<BrandManage>lambdaQuery()
                            .eq(BrandManage::getBrand_name, brand));
                    if(!CollectionUtils.isEmpty(brand_typeList)){
                        brand_id = brand_typeList.get(0).getId() == null ? null : brand_typeList.get(0).getId();
                    }
                }
                // ----------------- 品牌 end -----------------
                // ----------------- 部门 start -----------------
                String dept = MapUtils.getString(map, "部门");
                Integer dept_id;
                if (StringUtils.isBlank(dept)) {
                    map.put("部门tip", "部门不能为空 ");
                    errList.add("部门不能为空 ");
                } else {
                    List<SysDepartment> dept_list = sysDepartmentMapper.selectList(Wrappers.<SysDepartment>lambdaQuery()
                            .eq(SysDepartment::getIs_del, "0")
                            .eq(SysDepartment::getDept_name, dept));
                    if(CollectionUtils.isEmpty(dept_list)){
                        map.put("部门tip", "部门不存在 ");
                        errList.add("部门不存在 ");
                    }else {
                        dept_id = dept_list.get(0).getId();
                    }
                }
                // ----------------- 部门 end -----------------
                // ----------------- 仓库 start -----------------
                String warehouse = MapUtils.getString(map, "仓库");
                Integer warehouse_id = null;
                if (StringUtils.isBlank(warehouse)) {
                    map.put("仓库tip", "仓库不能为空 ");
                    errList.add("仓库不能为空 ");
                } else {
                    List<Warehouse> warehouse_list = warehouseMapper.selectList(Wrappers.<Warehouse>lambdaQuery()
                            .eq(Warehouse::getIs_del, "0")
                            .eq(Warehouse::getWarehouse_name, warehouse));
                    if(CollectionUtils.isEmpty(warehouse_list)){
                        map.put("仓库tip", "仓库不存在 ");
                        errList.add("仓库不存在 ");
                    }else {
                        warehouse_id = warehouse_list.get(0).getId();
                    }
                }
                // ----------------- 仓库 end -----------------
                // ----------------- 生产日期 start -----------------
                String production_date = MapUtils.getString(map, "生产日期(xxxx-xx-xx)");
                Date production_date_date = null;
                if(StringUtils.isNotBlank(production_date)){
                    try {
                        production_date_date = DateUtils.parseDate(production_date, "yyyy-MM-dd");
                    } catch (Exception e) {
                        map.put("生产日期", production_date);
                        map.put("生产日期tip", "生产日期格式错误 ");
                        errList.add("生产日期格式错误 ");
                    }
                }
                // ----------------- 生产日期 end -----------------
                // ----------------- 质保期 start -----------------
                String quality_guarantee_period = MapUtils.getString(map, "质保期(单位:月)") ;
                map.put("质保期", quality_guarantee_period);
                Integer quality_guarantee_period_int = null;
                if(StringUtils.isBlank(quality_guarantee_period)){
                    map.put("质保期tip", "质保期不能为空 ");
                    errList.add("质保期不能为空 ");
                } else {
                    try {
                        quality_guarantee_period_int = NumberUtil.parseInt(quality_guarantee_period);
                    } catch (Exception e) {
                        map.put("质保期tip", "质保期格式错误 ");
                        errList.add("质保期格式错误 ");
                    }
                }

                if (CollectionUtils.isEmpty(errList)) {
                    ConsInventoryManagement inventoryManagement = new ConsInventoryManagement();
                    inventoryManagement.setConsumable_id(consumable_id);
                    inventoryManagement.setName(name);
                    inventoryManagement.setModel(model);

                    inventoryManagement.setSpecifications(model_id);
                    inventoryManagement.setWarehouse_id(warehouse_id);
                    inventoryManagement.setUnit_price(price);
                    inventoryManagement.setActual_amount(actual_amount);
                    inventoryManagement.setWarehousing_time(new Date());
                    // 1:"初始化导入", 2:"入库单入库"
                    inventoryManagement.setWarehousing_mode(1);
                    int stock_num = Integer.parseInt(stock);
                    inventoryManagement.setInventory_num(stock_num);
                    inventoryManagement.setUnit_of_measurement(unit_id);
                    // 品牌id
                    inventoryManagement.setBrand_id(brand_id);
                    // 生产日期
                    inventoryManagement.setProduction_date(production_date_date);
                    // 质保期
                    inventoryManagement.setWarranty_period(quality_guarantee_period);
                    inventoryManagement.setCreate_time(new Date());
                    inventoryManagement.setCreate_user(sysUserService.getUser().getId());
                    inventoryManagement.setIs_del(String.valueOf(0));
                    list.add(inventoryManagement);
                } else {
                    map.put("errList", errList);
                    errorList.add(map);
                }
            }
            if (!CollectionUtils.isEmpty(errorList)){
                return ResultInfo.builder().code(500).msg("导入失败").data(errorList).build();
            }
            for (ConsInventoryManagement inventoryManagement : list) {
                consInventoryManagementMapper.insert(inventoryManagement);

                BigDecimal dj = new BigDecimal(inventoryManagement.getActual_amount() == null ? "0" : inventoryManagement.getActual_amount());
                BigDecimal sl = new BigDecimal(inventoryManagement.getInventory_num() == null ? 0 : inventoryManagement.getInventory_num());
                // 计算实际金额
                BigDecimal actualAmount = dj.multiply(sl);
                // 加入出入库统计记录
                consInAndOutStatisticsService.insertCons(ConsInAndOutStatistics.builder()
                        //耗材品类id
                        .consumable_name(inventoryManagement.getConsumable_name())
                        //耗材名称
                        .name(inventoryManagement.getName())
                        //生产日期
                        .production_date(inventoryManagement.getProduction_date())
                        // 质保期
                        .warranty_period(inventoryManagement.getWarranty_period())
                        // 单位
                        .unit_id(inventoryManagement.getUnit_of_measurement())
                        // 单价
                        .unit_price(inventoryManagement.getActual_amount())
                        // 期初数量
                        .initial_number(0)
                        // 入库数量
                        .in_number(inventoryManagement.getInventory_num())
                        // 出库数量
                        .out_number(0)
                        // 期末数量
                        .final_number(inventoryManagement.getInventory_num())
                        // 总金额
                        .total_amount(actualAmount.toString())
                        // 规格
                        .model(inventoryManagement.getModel())
                        // 仓库id
                        .warehouse_id(inventoryManagement.getWarehouse_id())
                        .type(1)
                        .build());
            }

        } catch (Exception e) {
            return ResultInfo.error("导入失败: " + e.getMessage());
        }
        return ResultInfo.success();
    }

    /**
     * 填充库存
     * @param inventoryManagement
     */
    public void updateNum(ConsInventoryManagement inventoryManagement) {
        // 查询原本数据
        ConsInventoryManagement old = consInventoryManagementMapper.selectById(inventoryManagement.getId());
        // 更新改动的数量
//        consInventoryManagementMapper.update(null, Wrappers.<ConsInventoryManagement>lambdaUpdate()
//                .set(ConsInventoryManagement::getInventory_num, inventoryManagement.getInventory_num())
//                .eq(ConsInventoryManagement::getId, inventoryManagement.getId())
//        );
        consInventoryManagementMapper.updateById(inventoryManagement);

        if(null == inventoryManagement.getInventory_num() || Objects.equals(old.getInventory_num(), inventoryManagement.getInventory_num())){
            return;
        }

        BigDecimal dj = new BigDecimal(old.getActual_amount() == null ? "0" : old.getActual_amount());
        BigDecimal sl = new BigDecimal(inventoryManagement.getInventory_num() == null ? 0 : inventoryManagement.getInventory_num());
        // 计算实际金额
        BigDecimal actualAmount = dj.multiply(sl);
        // 计算入库数量
        // 原本库存数量
        BigDecimal oldNum = new BigDecimal(old.getInventory_num() == null ? 0 : old.getInventory_num());
        BigDecimal num = sl.subtract(oldNum);
        // 加入出入库统计记录
        consInAndOutStatisticsService.insertCons(ConsInAndOutStatistics.builder()
                //耗材品类id
                .consumable_name(consumableMapper.selectById(old.getConsumable_id()).getConsumable_name())
                //耗材名称
                .name(old.getName())
                //生产日期
                .production_date(old.getProduction_date())
                // 质保期
                .warranty_period(old.getWarranty_period())
                // 单位
                .unit_id(old.getUnit_of_measurement())
                // 单价
                .unit_price(old.getActual_amount())
                // 期初数量
                .initial_number(old.getInventory_num())
                // 入库数量
                .in_number(num.intValue())
                // 出库数量
                .out_number(0)
                // 期末数量
                .final_number(inventoryManagement.getInventory_num())
                // 总金额
                .total_amount(actualAmount.toString())
                // 规格
                .model(old.getModel())
                // 仓库id
                .warehouse_id(old.getWarehouse_id())
                .type(1)
                .build());
    }
}
