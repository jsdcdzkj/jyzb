package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ConsPurchaseApplyDao;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.BrandManageMapper;
import com.jsdc.rfid.mapper.ConsPurchaseApplyMapper;
import com.jsdc.rfid.mapper.ConsumableMapper;
import com.jsdc.rfid.mapper.WarehouseMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import lombok.AllArgsConstructor;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vo.AccessRecordVo;
import vo.ConsDataVo;
import vo.ConsPurchaseApplyVo;
import vo.ResultInfo;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: PurchaseApplyService
 * Description:
 * date: 2022/4/24 16:58
 *
 * @author bn
 */
@Transactional
@Service
@AllArgsConstructor
public class ConsPurchaseApplyService extends BaseService<ConsPurchaseApplyDao, ConsPurchaseApply> {

    private ConsPurchaseApplyMapper purchaseApplyMapper;

    private ConsPurchaseDetailService purchaseDetailService;

    private SysUserService sysUserService;

    private CommonDataTools commonDataTools;

    private ConsAssettypeService assetsTypeService;

    private ConsCategoryService consCategoryService;

    private ConsSpecificationService consSpecificationService;

    private SysDictService sysDictService;

    private SupplierService supplierService;

    private SysPostService sysPostService;

    private SysDepartmentService deptService;

    private OperationRecordService operationRecordService;

    private BrandManageMapper brandManageMapper;

    private ProcessMemberService processMemberService;

    private ProcessMemberHistoryService processMemberHistoryService;

    @Autowired
    private ConsumableMapper consumableMapper ;

    /**
     * 采购趋势：折线图展示近六个月的采购趋势（采购单数、耗材总数）
     *
     * @Author thr
     */
    public List<ConsDataVo> getSixMonthCount(ConsPurchaseApply bean) {
        return purchaseApplyMapper.getSixMonthCount(bean);
    }

    public PageInfo<ConsPurchaseApply> toList(Integer pageIndex, Integer pageSize, ConsPurchaseApplyVo consPurchaseApplyVo) {
        SysUser current = null;
        if (null == consPurchaseApplyVo.getUserId()) {
            current = sysUserService.getUser();
        } else {
            current = sysUserService.getUser(consPurchaseApplyVo.getUserId());
        }

        if (null == consPurchaseApplyVo.getIs_adopt() || 0 == consPurchaseApplyVo.getIs_adopt()) {
            SysPost sysPost = sysPostService.selectById(current.getPost());

            if (sysPost.getData_permission() == G.DATAPERMISSION_PERSONAL) {
                consPurchaseApplyVo.setCreate_user(current.getId());
            } else if (sysPost.getData_permission() == G.DATAPERMISSION_DEPT) {
                consPurchaseApplyVo.setDept_id(current.getDepartment());
            }
        }

        int deptid = current.getDepartment();
        SysDepartment department = deptService.selectById(deptid);
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");

        LambdaQueryWrapper<ConsPurchaseApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(consPurchaseApplyVo.getPurchase_name()), ConsPurchaseApply::getPurchase_name, consPurchaseApplyVo.getPurchase_name())
                .like(StringUtils.isNotBlank(consPurchaseApplyVo.getPurchase_no()), ConsPurchaseApply::getPurchase_no, consPurchaseApplyVo.getPurchase_no())
                .eq(StringUtils.isNotBlank(consPurchaseApplyVo.getPut_order()), ConsPurchaseApply::getPut_order, "2")
                .eq(ConsPurchaseApply::getIs_del, "0")
                .in(!CollectionUtils.isEmpty(consPurchaseApplyVo.getIds()), ConsPurchaseApply::getId, consPurchaseApplyVo.getIds())
                .eq(Base.notEmpty(consPurchaseApplyVo.getApprove_status()), ConsPurchaseApply::getApprove_status, consPurchaseApplyVo.getApprove_status());
        if (null == consPurchaseApplyVo.getIs_adopt() || 0 == consPurchaseApplyVo.getIs_adopt()) {
            wrapper.eq(null != consPurchaseApplyVo.getDept_id(), ConsPurchaseApply::getDept_id, consPurchaseApplyVo.getDept_id());
            wrapper.eq(null != consPurchaseApplyVo.getCreate_user(), ConsPurchaseApply::getCreate_user, consPurchaseApplyVo.getCreate_user());
        }

        if (StringUtils.isNotBlank(consPurchaseApplyVo.getQuery())) {
            wrapper.and(w -> {
                w.like(StringUtils.isNotBlank(consPurchaseApplyVo.getQuery()), ConsPurchaseApply::getPurchase_no, consPurchaseApplyVo.getQuery());
                w.or();
                w.like(StringUtils.isNotBlank(consPurchaseApplyVo.getQuery()), ConsPurchaseApply::getPurchase_name, consPurchaseApplyVo.getQuery());
                return w;
            });
        }
        if (StringUtils.isNotBlank(consPurchaseApplyVo.getPurchase_type())) {
            wrapper.eq(ConsPurchaseApply::getPurchase_type, "2");
        } else {
            wrapper.isNull(ConsPurchaseApply::getPurchase_type);
        }
        // 申请人
        if (StringUtils.isNotBlank(consPurchaseApplyVo.getApply_user_name())){
            List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                    .like(SysUser::getUser_name, consPurchaseApplyVo.getApply_user_name())
                    .eq(SysUser::getIs_del, G.ISDEL_NO)
            );
            List<Integer> userIds = sysUsers.stream().map(SysUser::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIds)){
                userIds.add(-1);
            }
            wrapper.in(StringUtils.isNotBlank(consPurchaseApplyVo.getApply_user_name()), ConsPurchaseApply::getApply_user, userIds);
        }
        // 入库状态
        if (StringUtils.isNotBlank(consPurchaseApplyVo.getPut_status())){
            wrapper.eq(ConsPurchaseApply::getPut_status, consPurchaseApplyVo.getPut_status());
        }

        // 申请日期
        if (StringUtils.isNotBlank(consPurchaseApplyVo.getApplyTimeStr())){
            String[] split = consPurchaseApplyVo.getApplyTimeStr().split(" - ");
            wrapper.ge(ConsPurchaseApply::getCreate_time, split[0] + " 00:00:00");
            wrapper.le(ConsPurchaseApply::getCreate_time, split[1] + " 23:59:59");
        }
        List<ConsPurchaseApply> purchaseApplies = selectList(wrapper);

        SysUser finalCurrent = current;
        purchaseApplies.forEach(x -> {
            SysUser sysUser = sysUserService.selectById(x.getApply_user());
            if (null != sysUser) {
                x.setApply_user_name(sysUser.getUser_name());
            }
            Supplier supplier = supplierService.selectById(x.getSupplier_id());
            if (null != supplier) {
                x.setSupplier_name(supplier.getSupplier_name());
            }
            SysDepartment dept = deptService.selectById(x.getDept_id());
            x.setDept_name(dept.getDept_name());

            // 得到当前流程节点
            processMemberService.getProcessDataByBusId(x.getId(), G.PROCESS_HCCG, finalCurrent, x);
        });

        return new PageInfo<>(purchaseApplies);
    }

    /**
     * 已审批
     */
    public PageInfo<ConsPurchaseApply> finishAdopt(Integer pageIndex, Integer pageSize, ConsPurchaseApplyVo consPurchaseApplyVo) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        LambdaQueryWrapper<ConsPurchaseApply> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .in(!CollectionUtils.isEmpty(consPurchaseApplyVo.getIds()), ConsPurchaseApply::getId, consPurchaseApplyVo.getIds())
                .like(StringUtils.isNotBlank(consPurchaseApplyVo.getPurchase_name()), ConsPurchaseApply::getPurchase_name, consPurchaseApplyVo.getPurchase_name())
                .like(StringUtils.isNotBlank(consPurchaseApplyVo.getPurchase_no()), ConsPurchaseApply::getPurchase_no, consPurchaseApplyVo.getPurchase_no());
        if (StringUtils.isNotBlank(consPurchaseApplyVo.getQuery())) {
            wrapper.and(w -> {
                w.like(ConsPurchaseApply::getPurchase_no, consPurchaseApplyVo.getQuery());
                w.or();
                w.like(ConsPurchaseApply::getPurchase_name, consPurchaseApplyVo.getQuery());
                return w;
            });
        }

        List<ConsPurchaseApply> purchaseApplies = selectList(wrapper);
        purchaseApplies.forEach(x -> {
            if (null != x.getApply_user() && sysUserService.selectById(x.getApply_user()) != null) {
                SysUser sysUser = sysUserService.getUser(x.getApply_user());
                x.setApply_user_name(sysUser.getUser_name());
                x.setDept_name(sysUser.getDept_name());
            }

            Supplier supplier = supplierService.selectById(x.getSupplier_id());
            if (null != supplier) {
                x.setSupplier_name(supplier.getSupplier_name());
            }
        });
        return new PageInfo<>(purchaseApplies);
    }

    /**
     * 动态字典
     *
     * @param details
     */
    public void dynamicDictionary(List<ConsPurchaseDetail> details) {
        if (CollectionUtils.isEmpty(details)) {
            return;
        }
        List<ConsAssettype> assetType = assetsTypeService.selectList(Wrappers.<ConsAssettype>lambdaQuery().eq(ConsAssettype::getIs_del, "0"));
        List<String> assetType_name = assetType.stream().map(ConsAssettype::getName).collect(Collectors.toList());

        List<ConsCategory> categories = consCategoryService.selectList(Wrappers.<ConsCategory>lambdaQuery().eq(ConsCategory::getIs_del, "0"));
        List<String> categories_name = categories.stream().map(ConsCategory::getName).collect(Collectors.toList());

        List<ConsSpecification> specifications = consSpecificationService.selectList(Wrappers.<ConsSpecification>lambdaQuery().eq(ConsSpecification::getIs_del, "0"));
        List<String> specification_name = specifications.stream().map(ConsSpecification::getTypename).collect(Collectors.toList());

        details.forEach(x -> {
            if (!assetType_name.contains(x.getAssets_type_name())) {
                ConsAssettype consAssettype = new ConsAssettype();
                consAssettype.setName(x.getAssets_type_name());
                assetsTypeService.toAdd(consAssettype);
                x.setAssets_type_id(consAssettype.getId());
            }
            if (!categories_name.contains(x.getCategory_name())) {
                ConsCategory consCategory = new ConsCategory();
                consCategory.setName(x.getCategory_name());
                consCategoryService.toAdd(consCategory);
                x.setCategory_id(consCategory.getId());
            }
            if (!specification_name.contains(x.getSpecification_name())) {
                ConsSpecification consSpecification = new ConsSpecification();
                consSpecification.setTypename(x.getSpecification_name());
                consSpecificationService.toAdd(consSpecification);
                x.setSpecification_id(consSpecification.getId());
            }
        });
    }

    public ResultInfo toAdd(ConsPurchaseApplyVo bean) {
        if (null == bean.getPurchaseApply()) {
            return ResultInfo.error("无数据！");
        }
        dynamicDictionary(bean.getPurchaseDetails());
        bean.getPurchaseApply().setPurchase_no(commonDataTools.getNo(DataType.CONS_PURCHASE_ORDER_CODE.getType(), null));
        bean.getPurchaseApply().setApply_time(new Date());
        if (null == bean.getUserId()) {
            bean.getPurchaseApply().setCreate_user(sysUserService.getUser().getId());
            bean.getPurchaseApply().setApply_user(sysUserService.getUser().getId());
        } else {
            bean.getPurchaseApply().setCreate_user(bean.getUserId());
            bean.getPurchaseApply().setApply_user(bean.getUserId());
        }
        if (null == bean.getPurchaseApply().getApprove_status()) {
            bean.getPurchaseApply().setApprove_status("1");
        }
        bean.getPurchaseApply().setInspected_status("1");
        if (null == bean.getPurchaseApply().getPut_order()) {
            bean.getPurchaseApply().setPut_order("1");
        }
        bean.getPurchaseApply().setPut_status("1");
        bean.getPurchaseApply().setCreate_time(new Date());
        bean.getPurchaseApply().setIs_del("0");
        bean.getPurchaseApply().insert();
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(bean.getPurchaseApply().getPurchase_no())
                .type("2").record("采购申请-新增采购单").userId(bean.getUserId()).build());
        bean.getPurchaseDetails().forEach(x -> {
            x.setPurchase_apply_id(bean.getPurchaseApply().getId());
            x.setUserId(bean.getUserId());
            purchaseDetailService.toAdd(x);
        });
        return ResultInfo.success(bean);
    }

    /**
     * 新增并送审
     *
     * @param bean
     * @return
     */
    public ResultInfo toAddAndSub(ConsPurchaseApplyVo bean, SysUser user) {
        if (null == bean.getPurchaseApply()) {
            return ResultInfo.error("无数据！");
        }
        dynamicDictionary(bean.getPurchaseDetails());
        bean.getPurchaseApply().setPurchase_no(commonDataTools.getNo(DataType.CONS_PURCHASE_ORDER_CODE.getType(), null));
        bean.getPurchaseApply().setApply_time(new Date());
        if (null == bean.getUserId()) {
            bean.getPurchaseApply().setCreate_user(user.getId());
            bean.getPurchaseApply().setApply_user(user.getId());
        } else {
            bean.getPurchaseApply().setCreate_user(bean.getUserId());
            bean.getPurchaseApply().setApply_user(bean.getUserId());
        }
        if (null == bean.getPurchaseApply().getApprove_status()) {
            bean.getPurchaseApply().setApprove_status("1");
        }
        bean.getPurchaseApply().setInspected_status("1");
        if (null == bean.getPurchaseApply().getPut_order()) {
            bean.getPurchaseApply().setPut_order("1");
        }
        bean.getPurchaseApply().setPut_status("1");
        bean.getPurchaseApply().setCreate_time(new Date());
        bean.getPurchaseApply().setIs_del("0");
        bean.getPurchaseApply().insert();
        bean.getPurchaseDetails().forEach(x -> {
            x.setPurchase_apply_id(bean.getPurchaseApply().getId());
            x.setUserId(bean.getUserId());
            purchaseDetailService.toAdd(x);
        });

        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(bean.getPurchaseApply().getPurchase_no())
                .type("2").record("采购申请-新增采购单并送审").userId(bean.getUserId()).build());

        processMemberService.startProcess(G.PROCESS_HCCG, bean.getPurchaseApply().getId(), user.getId());
        return ResultInfo.success(bean);
    }

    public ResultInfo toApply(ConsPurchaseApplyVo purchaseApplyVo) {

        // 修改审批状态
        purchaseApplyVo.getPurchaseApply().setUpdate_time(new Date());
        purchaseApplyVo.getPurchaseApply().updateById();
        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2").record("资产采购审批").build());

        return ResultInfo.success();
    }

    public void toEdit(ConsPurchaseApplyVo purchaseApplyVo) {
        // 动态字典
        dynamicDictionary(purchaseApplyVo.getPurchaseDetails());

        Integer userid = purchaseApplyVo.getUserId();
        if (null == userid) {
            userid = sysUserService.getUser().getId();
        }
        purchaseApplyVo.getPurchaseApply().setUpdate_user(userid);
        purchaseApplyVo.getPurchaseApply().setUpdate_time(new Date());
        purchaseApplyVo.getPurchaseApply().updateById();
        // 详情表
        if (CollectionUtils.isEmpty(purchaseApplyVo.getPurchaseDetails())) {
            return;
        }

//        List<ConsPurchaseDetail> purchaseDetails = purchaseDetailService.selectList(Wrappers.<ConsPurchaseDetail>lambdaQuery()
//                .eq(ConsPurchaseDetail::getPurchase_apply_id, purchaseApplyVo.getPurchaseApply().getId())
//                .eq(ConsPurchaseDetail::getIs_del, "0")
//        );
//
//        List<Integer> details_id = purchaseDetails.stream().map(ConsPurchaseDetail::getId).collect(Collectors.toList());
//
//        List<Integer> detailsVo_id = purchaseApplyVo.getPurchaseDetails().stream().map(ConsPurchaseDetail::getId).collect(Collectors.toList());
//
//        List<Integer> ids = details_id.stream().filter(item -> !detailsVo_id.contains(item)).collect(Collectors.toList());
//
//        Integer finalUserid = userid;
//        ids.forEach(x -> {
//            purchaseDetailService.update(null, Wrappers.<ConsPurchaseDetail>lambdaUpdate().
//                    set(ConsPurchaseDetail::getIs_del, "1").set(ConsPurchaseDetail::getUpdate_time, new Date()).
//                    set(ConsPurchaseDetail::getUpdate_user, finalUserid).
//                    eq(ConsPurchaseDetail::getId, x));
//        });

        for (ConsPurchaseDetail detail : purchaseApplyVo.getPurchaseDetails()) {
            if (null != detail.getId() && null != purchaseDetailService.selectById(detail.getId())) {
                detail.setUpdate_time(new Date());
                detail.setUpdate_user(userid);
                purchaseDetailService.updateById(detail);
            }else {
                detail.setPurchase_apply_id(purchaseApplyVo.getPurchaseApply().getId());
                purchaseDetailService.toAdd(detail);
            }
        }

    }

    public void toWarehousing(ConsPurchaseApply purchaseApply) {
        purchaseApply.setPut_order("2");
        purchaseApply.updateById();

        ConsPurchaseApply consPurchaseApply = selectById(purchaseApply.getId());
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(consPurchaseApply.getPurchase_no())
                .type("2").record("生成入库单").userId(consPurchaseApply.getCreate_user()).build());
    }

    public ConsPurchaseApplyVo getById(Integer id) {
        ConsPurchaseApplyVo purchaseApplyVo = new ConsPurchaseApplyVo();

        purchaseApplyVo.setPurchaseApply(selectById(id));

        Supplier supplier = supplierService.selectById(purchaseApplyVo.getPurchaseApply().getSupplier_id());
        purchaseApplyVo.getPurchaseApply().setSupplier_name(supplier == null ? "" : supplier.getSupplier_name());

        SysDepartment sysDepartment = deptService.selectById(purchaseApplyVo.getPurchaseApply().getDept_id());
        SysUser user = sysUserService.selectById(purchaseApplyVo.getPurchaseApply().getApply_user());
        purchaseApplyVo.getPurchaseApply().setDept_name(sysDepartment == null ? "" : sysDepartment.getDept_name());
        purchaseApplyVo.getPurchaseApply().setApply_user_name(user.getUser_name());

        purchaseApplyVo.setPurchaseDetails(purchaseDetailService.selectList(Wrappers.<ConsPurchaseDetail>lambdaQuery().eq(ConsPurchaseDetail::getPurchase_apply_id, id).eq(ConsPurchaseDetail::getIs_del, "0")));

        purchaseApplyVo.getPurchaseDetails().forEach(x -> {
            ConsAssettype consAssettype = assetsTypeService.selectById(x.getAssets_type_id());
            x.setAssets_type_name(consAssettype == null ? "" : consAssettype.getName());

            ConsCategory consCategory = consCategoryService.selectById(x.getCategory_id());
            x.setCategory_name(consCategory == null ? "" : consCategory.getName());

            ConsSpecification consSpecification = consSpecificationService.selectById(x.getSpecification_id());
            x.setSpecification_name(consSpecification == null ? "" : consSpecification.getTypename());


            Consumable consumable1 = consumableMapper.selectById(x.getConsumable_id2()) ;
            if(null != consumable1){
                x.setConsumable_name2(consumable1.getConsumable_name());
                //获取一级耗材类型
                QueryWrapper<Consumable> queryWrapper = new QueryWrapper<>() ;
                Consumable consumable = consumableMapper.selectById(consumable1.getParent_id()) ;
                if(null != consumable){
                    x.setConsumable_name(consumable.getConsumable_name());
                }
            }else {
                x.setConsumable_name2("");
                x.setConsumable_name("");

            }



            SysDict sysDict = sysDictService.selectOne(Wrappers.<SysDict>lambdaQuery().
                    eq(SysDict::getType, "unit").
                    eq(SysDict::getValue, x.getUnits() == null ? "" : String.valueOf(x.getUnits())).
                    eq(SysDict::getIs_del, "0"));
            x.setUnits_name(sysDict == null ? "" : sysDict.getLabel());
            BrandManage brandManage = brandManageMapper.selectById(x.getAssets_brand());
            x.setBrand_name(null == brandManage ? "" : brandManage.getBrand_name());
        });


        return purchaseApplyVo;

    }

    public List<ConsPurchaseDetail> toPurchaseDetails(ConsPurchaseApplyVo purchaseApplyVo) {

        List<ConsPurchaseDetail> purchaseDetails = purchaseDetailService.selectList(Wrappers.<ConsPurchaseDetail>lambdaQuery().
                eq(ConsPurchaseDetail::getIs_del, "0").
                eq(ConsPurchaseDetail::getPurchase_apply_id, purchaseApplyVo.getPurchaseApply().getId()));

        purchaseDetails.forEach(x -> {
            if (null != assetsTypeService.selectById(x.getAssets_type_id())) {
                x.setAssets_type_name(assetsTypeService.selectById(x.getAssets_type_id()).getName());
            }
            if (null != consCategoryService.selectById(x.getCategory_id())) {
                x.setCategory_name(consCategoryService.selectById(x.getCategory_id()).getName());
            }
            if (null != consSpecificationService.selectById(x.getSpecification_id())) {
                x.setSpecification_name(consSpecificationService.selectById(x.getSpecification_id()).getTypename());
            }
            SysDict sysDict = sysDictService.selectOne(Wrappers.<SysDict>lambdaQuery().
                    eq(SysDict::getType, "unit").
                    eq(SysDict::getValue, x.getUnits() == null ? "" : String.valueOf(x.getUnits())).
                    eq(SysDict::getIs_del, "0"));
            x.setUnits_name(sysDict == null ? "" : sysDict.getLabel());
            BrandManage brand = brandManageMapper.selectById(x.getAssets_brand());
            x.setBrand_name(null == brand ? "" : brand.getBrand_name());

            // 采购数量为空时，采购数量等于申请数量
//            x.setAccept_num(StringUtils.isBlank(x.getAccept_num()) ? String.valueOf(x.getPurchase_num()) : x.getAccept_num());

            Consumable consumable1 = consumableMapper.selectById(x.getConsumable_id2()) ;
            if(null != consumable1){
                x.setConsumable_name2(consumable1.getConsumable_name());
                //获取一级耗材类型
                QueryWrapper<Consumable> queryWrapper = new QueryWrapper<>() ;
                Consumable consumable = consumableMapper.selectById(consumable1.getParent_id()) ;
                if(null != consumable){
                    x.setConsumable_name(consumable.getConsumable_name());
                }
            }else {
                x.setConsumable_name2("");
                x.setConsumable_name("");

            }
        });

        return purchaseDetails;
    }

    /**
     *
     */
    @Autowired
    private WarehouseMapper warehouseMapper;

    public PageInfo<ConsPurchaseDetail> toPagePurchaseDetails(Integer pageIndex, Integer pageSize, Integer id) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<ConsPurchaseDetail> purchaseDetails = purchaseDetailService.selectList(Wrappers.<ConsPurchaseDetail>lambdaQuery().
                eq(ConsPurchaseDetail::getIs_del, "0").
                eq(ConsPurchaseDetail::getPurchase_apply_id, id));
        Map<Integer, ConsCategory> categoryMap = commonDataTools.getConsCategoryMap();
        Map<Integer, ConsAssettype> assettypeMap = commonDataTools.getConsAssettypeMap();
        Map<Integer, SysDict> brandMap = commonDataTools.getSysDictMap("brand_type");
        Map<Integer, SysDict> unitMap = commonDataTools.getSysDictMap("unit");
        purchaseDetails.forEach(x -> {
            x.setAssets_type_name(commonDataTools.getValue(assettypeMap, x.getAssets_type_id(), "name"));
            x.setCategory_name(commonDataTools.getValue(categoryMap, x.getCategory_id(), "name"));
            x.setSpecification_name(null == consSpecificationService.selectById(x.getSpecification_id()) ? "" :
                    consSpecificationService.selectById(x.getSpecification_id()).getTypename());
            BrandManage brand = brandManageMapper.selectById(x.getAssets_brand());
            x.setBrand_name(null == brand ? "" : brand.getBrand_name());
            x.setUnits_name(commonDataTools.getValue(unitMap, x.getUnits(), "label"));
            //查找仓库名称
            Warehouse consWarehouse = warehouseMapper.selectById(x.getWarehouse_id());
            x.setWarehouse_name(consWarehouse == null ? "" : consWarehouse.getWarehouse_name());

            if(null !=x.getConsumable_id2()){
                Consumable consumable = consumableMapper.selectById(x.getConsumable_id2()) ;
                if(null != consumable){
                    x.setConsumable_name2(consumable.getConsumable_name());
                    Consumable consumable1 = consumableMapper.selectById(consumable.getParent_id()) ;
                    if(null != consumable1){
                        x.setConsumable_name(consumable1.getConsumable_name());
                    }
                }

            }



        });

        return new PageInfo<>(purchaseDetails);
    }

    public void toEditPurchaseDetails(ConsPurchaseApplyVo purchaseApplyVo) {

        purchaseApplyVo.getPurchaseDetails().forEach(x -> {
            x.setUpdate_time(new Date());
            x.setUpdate_user(sysUserService.getUser().getId());
            BigDecimal actual_price = new BigDecimal(x.getActual_price()).multiply(new BigDecimal(x.getAccept_num()));

            String actual_amount = (
                    purchaseApplyVo.getPurchaseApply().getActual_amount() == null ?
                            actual_price.toString() :
                            new BigDecimal(purchaseApplyVo.getPurchaseApply().getActual_amount()).add(actual_price).toString());
            // 保留两位小数
            actual_amount = new BigDecimal(actual_amount).setScale(2, RoundingMode.HALF_UP).toString();
            purchaseApplyVo.getPurchaseApply().setActual_amount(actual_amount);
            x.updateById();
        });

        purchaseApplyVo.getPurchaseApply().setInspected_status("2");
        purchaseApplyVo.getPurchaseApply().setInspected_time(new Date());
        purchaseApplyVo.getPurchaseApply().updateById();

        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2").record("资产采购到货验收").build());


    }

    public void toEditApply(ConsPurchaseApplyVo purchaseApplyVo) {
        toEdit(purchaseApplyVo);
        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2")
                .record("采购申请-采购单送审")
                .userId(purchaseApplyVo.getUserId())
                .build());
        // 流程启动
        if (null == purchaseApplyVo.getUserId()) {
            processMemberService.startProcess(G.PROCESS_HCCG, purchaseApplyVo.getPurchaseApply().getId(), sysUserService.getUser().getId());
        } else {
            processMemberService.startProcess(G.PROCESS_HCCG, purchaseApplyVo.getPurchaseApply().getId(), purchaseApplyVo.getUserId());
        }
    }

    public void toEditAndSub(ConsPurchaseApplyVo purchaseApplyVo) {
        toEdit(purchaseApplyVo);
        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2")
                .record("采购申请-编辑采购单并送审")
                .userId(purchaseApplyVo.getUserId())
                .build());
        // 流程启动
        if (null == purchaseApplyVo.getUserId()) {
            processMemberService.startProcess(G.PROCESS_HCCG, purchaseApplyVo.getPurchaseApply().getId(), sysUserService.getUser().getId());
        } else {
            processMemberService.startProcess(G.PROCESS_HCCG, purchaseApplyVo.getPurchaseApply().getId(), purchaseApplyVo.getUserId());
        }
    }

    public void toRevokeApply(ConsPurchaseApplyVo purchaseApplyVo) {
        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_HCCG);
        ProcessMember processMember = processMemberService.getProcessMemberByBusId(purchaseApplyVo.getPurchaseApply().getId(), dictionary.getValue());
        if (null == processMember) {
            throw new RuntimeException("未查询到流程");
        }
        // 需求调整,所有环节都可以撤回
//        if (processMember.getIs_revoke() != 1) {
//            throw new RuntimeException("当前流程已经审批，不能撤回!");
//        }
        processMember.setIsCH(1);
        processMemberService.removeProcessMember(processMember, JSON.toJSONString(purchaseApplyVo.getPurchaseApply()));

        purchaseApplyVo.getPurchaseApply().setUpdate_user(sysUserService.getUser().getId());
        purchaseApplyVo.getPurchaseApply().setUpdate_time(new Date());

        purchaseApplyVo.getPurchaseApply().updateById();

        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2").record("采购申请-采购单撤回").build());

    }

    public int updatePurchaseDetails(ConsPurchaseDetail purchaseDetail) {
        return purchaseDetailService.updateById(purchaseDetail);
    }

    public void toDel(ConsPurchaseApply purchaseApply) {
        purchaseApply.setUpdate_time(new Date());
        purchaseApply.setUpdate_user(sysUserService.getUser().getId());

        purchaseApply.updateById();

        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApply.getPurchase_no())
                .type("2").record("资产采购删除").build());


    }

    public JSONObject purchaseFromSupplierTo10() {
        List<AccessRecordVo> accessRecordVos = purchaseApplyMapper.purchaseFromSupplierTo10();
        List<String> xValue = accessRecordVos.stream().map(AccessRecordVo::getName).collect(Collectors.toList());
        List<Long> yValue = accessRecordVos.stream().map(AccessRecordVo::getTotal).collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("xValue", xValue);
        jsonObject.put("yValue", yValue);
        return jsonObject;
    }

    /**
     * 得到采购表详情信息
     */
    public ConsPurchaseDetail toPurchaseDetailById(Integer id) {
        return purchaseDetailService.selectById(id);
    }


}
