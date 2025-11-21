package com.jsdc.rfid.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.utils.DateTimeUtils;
import com.jsdc.rfid.dao.InventoryManagementDao;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import com.jsdc.rfid.utils.ExcelUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存管理,入库管理service
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Service
@Transactional
@AllArgsConstructor
public class InventoryManagementService extends BaseService<InventoryManagementDao, InventoryManagement> {

    private final InventoryManagementMapper inventoryManagementMapper;

    private final InventoryWarehousingMemberMapper inventoryWarehousingMemberMapper;

    private final WarehousingManagementMapper warehousingManagementMapper;

    private final CommonDataTools commonDataTools;

    private final SysUserService sysUserService;

    private final PurchaseApplyMapper purchaseApplyMapper;

    private final PurchaseDetailMapper purchaseDetailMapper;

    private final BrandManageMapper brandManageMapper;

    /**
     * 列表查询 分页
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    public PageInfo<InventoryManagement> toList(Integer pageIndex, Integer pageSize, InventoryManagement beanParam) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");

        String startTime = null;
        String endTime = null;
        // 申请日期
        if (StringUtils.isNotBlank(beanParam.getTimeStr())){
            String[] split = beanParam.getTimeStr().split(" - ");
            startTime = split[0] + " 00:00:00";
            endTime = split[1] + " 23:59:59";
        }

        List<InventoryManagement> inventoryManagementVos = selectList(Wrappers.<InventoryManagement>lambdaQuery()
                .like(StringUtils.isNotBlank(beanParam.getAsset_name()), InventoryManagement::getAsset_name, beanParam.getAsset_name())
                .gt(InventoryManagement::getInventory_num, 0)
                .eq(null != beanParam.getAsset_type_id(), InventoryManagement::getAsset_type_id, beanParam.getAsset_type_id())
                .eq(InventoryManagement::getIs_del, String.valueOf(0))
                // 仓库
                .eq(null != beanParam.getWarehouse_id(), InventoryManagement::getWarehouse_id, beanParam.getWarehouse_id())
                // 品牌
                .eq(null != beanParam.getBrand_id(), InventoryManagement::getBrand_id, beanParam.getBrand_id())
                // 入库方式
                .eq(null != beanParam.getWarehousing_mode(), InventoryManagement::getWarehousing_mode, beanParam.getWarehousing_mode())
                // 入库时间
                .ge(StringUtils.isNotBlank(beanParam.getTimeStr()), InventoryManagement::getCreate_time, startTime)
                .le(StringUtils.isNotBlank(beanParam.getTimeStr()), InventoryManagement::getCreate_time, endTime)
        );
        // 资产品类map
        Map<Integer, AssetsType> assetsTypeMap = commonDataTools.getAssetsTypeMap();
        // 得到仓库map
        Map<Integer, Warehouse> warehouseMap = commonDataTools.getWarehouseMap();
        // 得到用户map
        Map<Integer, SysUser> sysUserMap = commonDataTools.getUserMap();
        // 得到计量单位
        Map<Integer, SysDict> dictMap = commonDataTools.getSysDictMap("unit");
        // 得到品牌map
        Map<Integer, SysDict> brandMap = commonDataTools.getSysDictMap("brand_type");

        for(InventoryManagement inventoryManagementVo : inventoryManagementVos){
            inventoryManagementVo.setAssetTypeName(commonDataTools.getValue(assetsTypeMap, inventoryManagementVo.getAsset_type_id(),"assets_type_name"));
            inventoryManagementVo.setWarehouse_name(commonDataTools.getValue(warehouseMap, inventoryManagementVo.getWarehouse_id(),"warehouse_name"));
            inventoryManagementVo.setCreateUserName(commonDataTools.getValue(sysUserMap, inventoryManagementVo.getCreate_user(),"user_name"));
            inventoryManagementVo.setUnit_name(commonDataTools.getValue(dictMap, inventoryManagementVo.getUnit_of_measurement(),"label"));
            BrandManage brandManage = brandManageMapper.selectById(inventoryManagementVo.getBrand_id());
            inventoryManagementVo.setBrand_name(null == brandManage ? "" : brandManage.getBrand_name());
        }

        return new PageInfo<>(inventoryManagementVos);
    }

    public List<InventoryManagement> getList(InventoryManagement beanParam) {

        return inventoryManagementMapper.toList(beanParam);
    }

    /**
     * 根据id 查询库存信息
     * @param id
     * @return
     */
    public ResultInfo getById(Integer id) {
        InventoryManagement inventoryManagement = selectById(id);
        if(null == inventoryManagement){
            return ResultInfo.error("没有找到对应的数据");
        }
        // 查询关联的入库单
        List<InventoryWarehousingMember> members = inventoryWarehousingMemberMapper.selectList(Wrappers.<InventoryWarehousingMember>lambdaQuery()
                .eq(InventoryWarehousingMember::getIs_del, String.valueOf(0))
                .eq(InventoryWarehousingMember::getInventory_id, inventoryManagement.getId()));
        if(CollectionUtils.isEmpty(members)){
            return ResultInfo.error("没有找到对应的入库单");
        }
        inventoryManagement.setWarehousingManagementList(warehousingManagementMapper.selectBatchIds(members.stream().map(InventoryWarehousingMember::getWarehousing_id).collect(Collectors.toList())));

        return ResultInfo.success(inventoryManagement);
    }

    /**
     *  添加
     */
    public ResultInfo addInventoryManagement(InventoryManagement bean) {
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
     *  编辑
     */
    public ResultInfo editInventoryManagement(InventoryManagement bean) {
        // 修改者
//        bean.setUpdate_user(sysUserService.getUser().getId());
        // 修改时间
        bean.setUpdate_time(new Date());
        updateById(bean);
        return ResultInfo.success();
    }

    /**
     * 查询入库管理数据 分页
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    public PageInfo<WarehousingManagement> toWarehousingList(Integer pageIndex, Integer pageSize, WarehousingManagement beanParam) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");

        LambdaQueryWrapper<WarehousingManagement> wrapper = Wrappers.<WarehousingManagement>lambdaQuery()
                .like(StringUtils.isNotBlank(beanParam.getInbound_order_number()), WarehousingManagement::getInbound_order_number, beanParam.getInbound_order_number())
                // 入库类型
                .eq(null != beanParam.getInbound_type(), WarehousingManagement::getInbound_type, beanParam.getInbound_type())
                .eq(WarehousingManagement::getIs_del, String.valueOf(0));

        // 申请日期
        if (StringUtils.isNotBlank(beanParam.getTimeStr())){
            String[] split = beanParam.getTimeStr().split(" - ");
            wrapper.ge(WarehousingManagement::getCreate_time, split[0] + " 00:00:00");
            wrapper.le(WarehousingManagement::getCreate_time, split[1] + " 23:59:59");
        }
        List<WarehousingManagement> warehousingManagementVos = warehousingManagementMapper.selectList(wrapper);
        Map<Integer, Supplier> supplierMap = commonDataTools.getSupplierMap();
        for (WarehousingManagement warehousingManagementVo : warehousingManagementVos) {
            warehousingManagementVo.setSupplier_name(commonDataTools.getValue(supplierMap, warehousingManagementVo.getSupplier_id(), "supplier_name"));
        }

        return new PageInfo<>(warehousingManagementVos);
    }

    /**
     * 根据id 查看入库管理详情信息
     * @param id
     * @return
     */
    public ResultInfo getWarehousingById(Integer id) {
        WarehousingManagement warehousingManagement = warehousingManagementMapper.selectById(id);
        if (null == warehousingManagement) {
            return ResultInfo.error("没有找到对应的数据");
        }
        // 查询采购的详情信息

        return ResultInfo.success(warehousingManagement);
    }

    /**
     * 添加入库管理 数据信息
     * @param warehousingManagement
     * @return
     */
    public ResultInfo addWarehousing(WarehousingManagement warehousingManagement) {
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
        warehousingManagementMapper.insert(warehousingManagement);
        return ResultInfo.success();
    }

    /**
     * 编辑入库管理 数据信息
     * @param warehousingManagement
     * @return
     */
    public ResultInfo editWarehousing(WarehousingManagement warehousingManagement) {
        // 设置修改时间
        warehousingManagement.setUpdate_time(new Date());
        // 设置修改者
        warehousingManagement.setUpdate_user(sysUserService.getUser().getId());
        warehousingManagementMapper.updateById(warehousingManagement);
        return ResultInfo.success();
    }

    /**
     * 删除入库管理 数据信息
     * 删除入库：可删除选中的入库记录，删除后该入库记录对应的入库单状态置为未完成，同时库存管理列表的库存数据同步更新。
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultInfo delWarehousing(Integer id) {
        // 设置删除状态
        WarehousingManagement warehousingManagement = new WarehousingManagement();
        warehousingManagement.setId(id);
        warehousingManagement.setIs_del(String.valueOf(1));
        warehousingManagement.setUpdate_time(new Date());
        warehousingManagement.setUpdate_user(sysUserService.getUser().getId());
        warehousingManagementMapper.updateById(warehousingManagement);

        // 删除后该入库记录对应的入库单状态置为未完成

        // 库存管理列表的库存数据同步更新
        return ResultInfo.success();
    }

    /**
     * 导入入库管理数据
     * @param importFile
     * @return
     */
    public ResultInfo importWarehousing(MultipartFile importFile) {
        //        String fileName = "d:/test/writeBeanTest.xlsx";
//        File file = new File(fileName);
        // 这里默认读取第一个sheet
        List<WarehousingManagement> warehousingManagementList = ExcelUtils.read(importFile, WarehousingManagement.class);
        if(org.springframework.util.CollectionUtils.isEmpty(warehousingManagementList)){
            return ResultInfo.error("非法入参");
        }
        for(WarehousingManagement member : warehousingManagementList){
            WarehousingManagement bean = new WarehousingManagement();
            // 必选字段判断
            if(StringUtils.isBlank(member.getWarehouse_name())){
                return ResultInfo.error("必填字段不能为空");
            }
            warehousingManagementMapper.insert(bean);
        }

        return ResultInfo.success();
    }

    /**
     * 导出 入库管理模板
     * @param response
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
     * 根据采购部门添加
     * @param purchaseApply_id
     * @return
     */
    public ResultInfo addInventoryManagementById(Integer purchaseApply_id) {
        PurchaseApply purchaseApply = purchaseApplyMapper.selectById(purchaseApply_id);
        if(null == purchaseApply){
            return ResultInfo.error("采购申请不存在");
        }
        List<PurchaseDetail> purchaseDetails = purchaseDetailMapper.selectList(Wrappers.<PurchaseDetail>lambdaQuery()
                .eq(PurchaseDetail::getPurchase_apply_id, purchaseApply_id).eq(PurchaseDetail::getIs_del, String.valueOf(0)));
        if(CollectionUtils.isEmpty(purchaseDetails)){
            return ResultInfo.error("采购申请没有明细");
        }

        // 修改采购单,为已入库
        purchaseApply.setPut_status(String.valueOf(2));
        purchaseApplyMapper.updateById(purchaseApply);

        // 添加入库管理
        WarehousingManagement warehousingManagement = new WarehousingManagement();
        // 入库类型 1采购入库 2手动入库
        if(null == purchaseApply.getPurchase_type() || purchaseApply.getPurchase_type().equals(String.valueOf(1))){
            warehousingManagement.setInbound_type(1);
        }else {
            warehousingManagement.setInbound_type(2);
        }

        warehousingManagement.setOperation_record(warehousingManagement.getInbound_type() == 1?"采购入库":"手动入库");
        warehousingManagement.setInbound_order_number(commonDataTools.getNo(DataType.INBOUND_ORDER_NUMBER.getType(), warehousingManagement));
        warehousingManagement.setSupplier_id(purchaseApply.getSupplier_id());
        warehousingManagement.setInbound_amount(purchaseApply.getActual_amount());
        //inbound_status 1:待入库 2:已入库
        warehousingManagement.setInbound_status(2);

        warehousingManagement.setPurchase_apply_id(purchaseApply_id);
        warehousingManagement.setCreate_time(new Date());
        warehousingManagement.setCreate_user(sysUserService.getUser().getId());
        warehousingManagement.setIs_del(String.valueOf(0));
        warehousingManagementMapper.insert(warehousingManagement);

        // 添加库存管理
        for (PurchaseDetail purchaseDetail : purchaseDetails) {
            InventoryManagement inventoryManagement = new InventoryManagement();
            inventoryManagement.setAsset_type_id(purchaseDetail.getAssets_type_id());
            inventoryManagement.setAsset_name(purchaseDetail.getAssets_name());
            inventoryManagement.setSpecifications(purchaseDetail.getAssets_model());
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

            inventoryManagement.setCreate_time(new Date());
            inventoryManagement.setCreate_user(sysUserService.getUser().getId());
            inventoryManagement.setIs_del(String.valueOf(0));
            inventoryManagementMapper.insert(inventoryManagement);
        }

        return ResultInfo.success("操作成功");
    }

    public PageInfo<InventoryManagement> getListByAssetsTypeName(Integer pageIndex, Integer pageSize,String assetTypeName , Integer year) {
        String startDay = "";
        String endDay = "";
        if (null != year && year > 0) {
            startDay = DateTimeUtils.getYearFirstStr(year);
            endDay = DateTimeUtils.getCurrYearLast(year);
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<InventoryManagement> inventoryManagementList = this.inventoryManagementMapper.getListByAssetsTypeName(assetTypeName,startDay,endDay);
        return new PageInfo<>(inventoryManagementList);
    }
}
