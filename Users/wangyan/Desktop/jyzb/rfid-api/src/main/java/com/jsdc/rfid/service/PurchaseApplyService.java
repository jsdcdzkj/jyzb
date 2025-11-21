package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.PurchaseApplyDao;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.BrandManageMapper;
import com.jsdc.rfid.mapper.PurchaseApplyMapper;
import com.jsdc.rfid.mapper.SupplierMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import lombok.AllArgsConstructor;
import net.hasor.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vo.AccessRecordVo;
import vo.PurchaseApplyVo;
import vo.ResultInfo;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
public class PurchaseApplyService extends BaseService<PurchaseApplyDao, PurchaseApply> {


    private PurchaseApplyMapper purchaseApplyMapper;

    private PurchaseDetailService purchaseDetailService;

    private SysUserService sysUserService;

    private CommonDataTools commonDataTools;

    private AssetsTypeService assetsTypeService;

    private SysDictService sysDictService;

    private SupplierService supplierService;

    private SysPostService sysPostService;

    private SysDepartmentService deptService;

    private OperationRecordService operationRecordService;

    private SupplierMapper supplierMapper;

    private BrandManageMapper brandManageMapper;

    private ProcessMemberService processMemberService;

    private ProcessMemberHistoryService processMemberHistoryService;

    private ProcessConfigInfoService processConfigInfoService;

    private ProcessConfigService processConfigService;

    public PageInfo<PurchaseApply> toList(Integer pageIndex, Integer pageSize, PurchaseApplyVo purchaseApplyVo) {
        SysUser current = sysUserService.getUser();
        return toList(pageIndex, pageSize, purchaseApplyVo, current);
    }


    public PageInfo<PurchaseApply> toList(Integer pageIndex, Integer pageSize, PurchaseApplyVo purchaseApplyVo, SysUser current) {

        // 数据权限
        List<Integer> createUsers = new ArrayList<>();
        if (null == purchaseApplyVo || null == purchaseApplyVo.getIs_adopt() || 0 == purchaseApplyVo.getIs_adopt()) {
            SysPost sysPost = sysPostService.selectById(current.getPost());
            if (sysPost.getData_permission() == G.DATAPERMISSION_PERSONAL) {
                createUsers.add(current.getId());
            } else if (sysPost.getData_permission() == G.DATAPERMISSION_DEPT) {
                List<SysUser> users = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                        .eq(SysUser::getDepartment, current.getDepartment())
                        .eq(SysUser::getIs_del, G.ISDEL_NO)
                );
                for (SysUser user : users) {
                    createUsers.add(user.getId());
                }
            }
        }

        LambdaQueryWrapper<PurchaseApply> wrapper = Wrappers.<PurchaseApply>lambdaQuery()
                .like(StringUtils.isNotBlank(purchaseApplyVo.getPurchase_name()), PurchaseApply::getPurchase_name, purchaseApplyVo.getPurchase_name())
                .like(StringUtils.isNotBlank(purchaseApplyVo.getPurchase_no()), PurchaseApply::getPurchase_no, purchaseApplyVo.getPurchase_no())
                .eq(StringUtils.isNotBlank(purchaseApplyVo.getPut_order()), PurchaseApply::getPut_order, "2")
                .eq(PurchaseApply::getIs_del, G.ISDEL_NO)
                .in(!CollectionUtils.isEmpty(purchaseApplyVo.getIds()), PurchaseApply::getId, purchaseApplyVo.getIds())
                .eq(Base.notEmpty(purchaseApplyVo.getApprove_status()), PurchaseApply::getApprove_status, purchaseApplyVo.getApprove_status());
        if (null == purchaseApplyVo || null == purchaseApplyVo.getIs_adopt() || 0 == purchaseApplyVo.getIs_adopt()) {
            wrapper.in(!CollectionUtils.isEmpty(createUsers), PurchaseApply::getCreate_user, createUsers);
        }
        // 增加关键字查询条件
        if (StringUtils.isNotBlank(purchaseApplyVo.getTransfer())) {
            // purchase_no like '%关键字%' or purchase_name like '%关键字%'
            wrapper.and(x -> x.like(PurchaseApply::getPurchase_no, purchaseApplyVo.getTransfer()).or().like(PurchaseApply::getPurchase_name, purchaseApplyVo.getTransfer()));
        }
        if (StringUtils.isNotBlank(purchaseApplyVo.getPurchase_type())) {
            wrapper.eq(PurchaseApply::getPurchase_type, "2");
        } else {
            wrapper.isNull(PurchaseApply::getPurchase_type);
        }
        // 申请人
        if (StringUtils.isNotBlank(purchaseApplyVo.getApply_user_name())) {
            List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                    .like(SysUser::getUser_name, purchaseApplyVo.getApply_user_name())
                    .eq(SysUser::getIs_del, G.ISDEL_NO)
            );
            List<Integer> userIds = sysUsers.stream().map(SysUser::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIds)) {
                userIds.add(-1);
            }
            wrapper.in(StringUtils.isNotBlank(purchaseApplyVo.getApply_user_name()), PurchaseApply::getApply_user, userIds);
        }
        // 入库状态
        if (StringUtils.isNotBlank(purchaseApplyVo.getPut_status())) {
            wrapper.eq(PurchaseApply::getPut_status, purchaseApplyVo.getPut_status());
        }

        // 申请日期
        if (StringUtils.isNotBlank(purchaseApplyVo.getApplyTimeStr())) {
            String[] split = purchaseApplyVo.getApplyTimeStr().split(" - ");
            wrapper.ge(PurchaseApply::getCreate_time, split[0] + " 00:00:00");
            wrapper.le(PurchaseApply::getCreate_time, split[1] + " 23:59:59");
        }

//        wrapper.orderByAsc(PurchaseApply::getApprove_status);
//        wrapper.orderByDesc(PurchaseApply::getCreate_time);
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<PurchaseApply> purchaseApplies = selectList(wrapper);

        purchaseApplies.forEach(x -> {
            SysUser sysUser = sysUserService.selectById(x.getApply_user());
            if (null != sysUser) {
                x.setApply_user_name(sysUser.getUser_name());
            }
            Supplier supplier = supplierService.selectById(x.getSupplier_id());
            if (null != supplier) {
                x.setSupplier_name(supplier.getSupplier_name());
            }
            // 得到当前流程节点
            //processMemberService.getProcessDataByBusId(x.getId(), G.PROCESS_ZCCG, current, x);
        });

        return new PageInfo<>(purchaseApplies);
    }

    public PageInfo<PurchaseApply> finishAdopt(Integer pageIndex, Integer pageSize, PurchaseApplyVo purchaseApplyVo) {
        return finishAdopt(pageIndex, pageSize, purchaseApplyVo, sysUserService.getUser());
    }

    public PageInfo<PurchaseApply> finishAdopt(Integer pageIndex, Integer pageSize, PurchaseApplyVo purchaseApplyVo, SysUser current) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");


        LambdaQueryWrapper<PurchaseApply> wrapper = Wrappers.<PurchaseApply>lambdaQuery()
                .like(StringUtils.isNotBlank(purchaseApplyVo.getPurchase_name()), PurchaseApply::getPurchase_name, purchaseApplyVo.getPurchase_name())
                .like(StringUtils.isNotBlank(purchaseApplyVo.getPurchase_no()), PurchaseApply::getPurchase_no, purchaseApplyVo.getPurchase_no())
                .eq(PurchaseApply::getIs_del, G.ISDEL_NO)
                .in(!CollectionUtils.isEmpty(purchaseApplyVo.getIds()), PurchaseApply::getId, purchaseApplyVo.getIds())
                .and(StringUtils.isNotBlank(purchaseApplyVo.getTransfer()), x -> x.like(PurchaseApply::getPurchase_no, purchaseApplyVo.getTransfer()).or().like(PurchaseApply::getPurchase_name, purchaseApplyVo.getTransfer()));
        // 申请人
        if (StringUtils.isNotBlank(purchaseApplyVo.getApply_user_name())) {
            List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                    .like(SysUser::getUser_name, purchaseApplyVo.getApply_user_name())
                    .eq(SysUser::getIs_del, G.ISDEL_NO)
            );
            List<Integer> userIds = sysUsers.stream().map(SysUser::getId).collect(Collectors.toList());
            wrapper.in(StringUtils.isNotBlank(purchaseApplyVo.getApply_user_name()), PurchaseApply::getApply_user, userIds);
        }

        // 申请日期
        if (StringUtils.isNotBlank(purchaseApplyVo.getApplyTimeStr())) {
            String[] split = purchaseApplyVo.getApplyTimeStr().split(" - ");
            wrapper.ge(PurchaseApply::getCreate_time, split[0] + " 00:00:00");
            wrapper.le(PurchaseApply::getCreate_time, split[1] + " 23:59:59");
        }
        List<PurchaseApply> purchaseApplies = selectList(wrapper);

        purchaseApplies.forEach(x -> {
            SysUser sysUser = sysUserService.selectById(x.getApply_user());
            if (null != sysUser) {
                x.setApply_user_name(sysUser.getUser_name());
            }
            Supplier supplier = supplierService.selectById(x.getSupplier_id());
            if (null != supplier) {
                x.setSupplier_name(supplier.getSupplier_name());
            }
            // 得到当前流程节点
            ProcessConfig process = processMemberService.getInfoByBusId(x.getId(), G.PROCESS_ZCCG);
            if (null != process && !CollectionUtils.isEmpty(process.getCurrentInfos())) {
                for (ProcessConfigInfo info : process.getCurrentInfos()) {
                    if (info.getNode_type() == 0 && current.getPost().equals(info.getNode_handler())) {
                        x.setCurrentTaskName(info.getNode_name());
                    } else if (info.getNode_type() == 1 && current.getId().equals(info.getNode_handler())) {
                        x.setCurrentTaskName(info.getNode_name());
                    } else if (info.getNode_type() == 2 && process.getProcessMember().getApply_dept_leader_id().equals(current.getId())) {
                        x.setCurrentTaskName(info.getNode_name());
                    }
                }
            }

        });


        return new PageInfo<>(purchaseApplies);
    }

    public ResultInfo toAdd(PurchaseApplyVo purchaseApplyVo) {
        return toAdd(purchaseApplyVo, sysUserService.getUser());
    }

    public ResultInfo toAdd(PurchaseApplyVo purchaseApplyVo, SysUser sysUser) {

        if (null == purchaseApplyVo.getPurchaseApply()) {
            return ResultInfo.error("无数据！");
        }

        purchaseApplyVo.getPurchaseApply().setPurchase_no(commonDataTools.getNo(DataType.PURCHASE_APPLY.getType(), null));
        purchaseApplyVo.getPurchaseApply().setApply_time(new Date());
        purchaseApplyVo.getPurchaseApply().setCreate_user(sysUser.getId());
        purchaseApplyVo.getPurchaseApply().setApply_user(sysUser.getId());
        if (null == purchaseApplyVo.getPurchaseApply().getApprove_status()) {
            //purchaseApplyVo.getPurchaseApply().setApprove_status("1");
            purchaseApplyVo.getPurchaseApply().setApprove_status("4");//去除审批流程，直接审批成功
        }
        purchaseApplyVo.getPurchaseApply().setInspected_status("1");
        if (null == purchaseApplyVo.getPurchaseApply().getPut_order()) {
            purchaseApplyVo.getPurchaseApply().setPut_order("1");
        }
        purchaseApplyVo.getPurchaseApply().setPut_status("1");
        purchaseApplyVo.getPurchaseApply().setCreate_time(new Date());
        purchaseApplyVo.getPurchaseApply().setIs_del("0");

        purchaseApplyVo.getPurchaseApply().insert();
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no()).userId(sysUser.getId())
                .type("2").record("采购申请-新增采购单").build());
        purchaseApplyVo.getPurchaseDetails().forEach(x -> {
            x.setPurchase_apply_id(purchaseApplyVo.getPurchaseApply().getId());
            x.setUserId(sysUser.getId());
            purchaseDetailService.toAdd(x);
        });
        return ResultInfo.success(purchaseApplyVo);

    }

    public ResultInfo toAddAndSub(PurchaseApplyVo purchaseApplyVo, SysUser sysUser) {
        if (null == purchaseApplyVo.getPurchaseApply()) {
            return ResultInfo.error("无数据！");
        }

        purchaseApplyVo.getPurchaseApply().setPurchase_no(commonDataTools.getNo(DataType.PURCHASE_APPLY.getType(), null));
        purchaseApplyVo.getPurchaseApply().setApply_time(new Date());
        purchaseApplyVo.getPurchaseApply().setCreate_user(sysUser.getId());
        purchaseApplyVo.getPurchaseApply().setApply_user(sysUser.getId());
        if (null == purchaseApplyVo.getPurchaseApply().getApprove_status()) {
            //purchaseApplyVo.getPurchaseApply().setApprove_status("1");
            purchaseApplyVo.getPurchaseApply().setApprove_status("4");
        }
        purchaseApplyVo.getPurchaseApply().setInspected_status("1");
        if (null == purchaseApplyVo.getPurchaseApply().getPut_order()) {
            purchaseApplyVo.getPurchaseApply().setPut_order("1");
        }
        purchaseApplyVo.getPurchaseApply().setPut_status("1");
        purchaseApplyVo.getPurchaseApply().setCreate_time(new Date());
        purchaseApplyVo.getPurchaseApply().setIs_del("0");

        purchaseApplyVo.getPurchaseApply().insert();
        purchaseApplyVo.getPurchaseDetails().forEach(x -> {
            x.setPurchase_apply_id(purchaseApplyVo.getPurchaseApply().getId());
            x.setUserId(sysUser.getId());
            purchaseDetailService.toAdd(x);
        });
        // 流程启动
        //processMemberService.startProcess(G.PROCESS_ZCCG, purchaseApplyVo.getPurchaseApply().getId(), sysUser.getId());
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no()).operate_id(purchaseApplyVo.getPurchaseApply().getId())
                .type("2").is_del("0").record("采购申请-新增采购单").build());
        return ResultInfo.success(purchaseApplyVo);
    }

    //暂存+送审=微信app专用
    public ResultInfo toWxAdd(PurchaseApplyVo purchaseApplyVo, SysUser sysUser) {
        if (null == purchaseApplyVo.getPurchaseApply()) {
            return ResultInfo.error("无数据！");
        }
        purchaseApplyVo.getPurchaseApply().setPurchase_no(commonDataTools.getNo(DataType.PURCHASE_APPLY.getType(), null));
        purchaseApplyVo.getPurchaseApply().setApply_time(new Date());
        purchaseApplyVo.getPurchaseApply().setCreate_user(sysUser.getId());
        purchaseApplyVo.getPurchaseApply().setApply_user(sysUser.getId());
        if (null == purchaseApplyVo.getPurchaseApply().getApprove_status()) {
            purchaseApplyVo.getPurchaseApply().setApprove_status("1");
        }
        purchaseApplyVo.getPurchaseApply().setInspected_status("1");
        if (null == purchaseApplyVo.getPurchaseApply().getPut_order()) {
            purchaseApplyVo.getPurchaseApply().setPut_order("1");
        }
        purchaseApplyVo.getPurchaseApply().setPut_status("1");
        purchaseApplyVo.getPurchaseApply().setCreate_time(new Date());
        purchaseApplyVo.getPurchaseApply().setIs_del("0");
        purchaseApplyVo.getPurchaseApply().insert();
        toEditApply(purchaseApplyVo);//送审
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no()).userId(sysUser.getId())
                .type("2").record("采购申请-新增采购单并送审").build());
        return ResultInfo.success(purchaseApplyVo);

    }


    public ResultInfo toApply(PurchaseApplyVo purchaseApplyVo) {

        // 修改审批状态
        purchaseApplyVo.getPurchaseApply().setUpdate_time(new Date());
        purchaseApplyVo.getPurchaseApply().updateById();
        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2").record("资产采购审批").build());

        return ResultInfo.success();
    }

    public void toEdit(PurchaseApplyVo purchaseApplyVo) {
        Integer userId = null;
        if (Base.notEmpty(purchaseApplyVo.getUserId())) {
            userId = purchaseApplyVo.getUserId();
        } else {
            userId = sysUserService.getUser().getId();
        }

        purchaseApplyVo.getPurchaseApply().setUpdate_user(userId);
        purchaseApplyVo.getPurchaseApply().setUpdate_time(new Date());
        purchaseApplyVo.getPurchaseApply().updateById();

        // 详情表
        if (CollectionUtils.isEmpty(purchaseApplyVo.getPurchaseDetails())) {
            return;
        }
        List<PurchaseDetail> purchaseDetails = purchaseDetailService.
                selectList(Wrappers.<PurchaseDetail>lambdaQuery().
                        eq(PurchaseDetail::getPurchase_apply_id, purchaseApplyVo.getPurchaseApply().getId()).
                        eq(PurchaseDetail::getIs_del, "0"));

        List<Integer> details_id = purchaseDetails.stream().map(PurchaseDetail::getId).collect(Collectors.toList());

        List<Integer> detailsVo_id = purchaseApplyVo.getPurchaseDetails().stream().map(PurchaseDetail::getId).collect(Collectors.toList());

        List<Integer> ids = details_id.stream().filter(item -> !detailsVo_id.contains(item)).collect(Collectors.toList());

        Integer finalUserId = userId;
        ids.forEach(x -> {
            purchaseDetailService.update(null, Wrappers.<PurchaseDetail>lambdaUpdate().
                    set(PurchaseDetail::getIs_del, "1").set(PurchaseDetail::getUpdate_time, new Date()).
                    set(PurchaseDetail::getUpdate_user, finalUserId).
                    eq(PurchaseDetail::getId, x));
        });


        purchaseApplyVo.getPurchaseDetails().forEach(x -> {
            if (x.getId() == null) {
                x.setPurchase_apply_id(purchaseApplyVo.getPurchaseApply().getId());
                purchaseDetailService.toAdd(x);
            }
        });

    }

    public void toWarehousing(PurchaseApply purchaseApply) {
        purchaseApply.setPut_order("2");
        purchaseApply.updateById();

        PurchaseApply purchaseApply1 = selectById(purchaseApply.getId());
        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApply1.getPurchase_no())
                .type("2").record("生成入库单").build());

    }

    public PurchaseApplyVo getById(Integer id) {
        PurchaseApplyVo purchaseApplyVo = new PurchaseApplyVo();

        purchaseApplyVo.setPurchaseApply(selectById(id));

        Supplier supplier = supplierService.selectById(purchaseApplyVo.getPurchaseApply().getSupplier_id());
        purchaseApplyVo.getPurchaseApply().setSupplier_name(supplier == null ? "" : supplier.getSupplier_name());

        SysDepartment sysDepartment = deptService.selectById(purchaseApplyVo.getPurchaseApply().getDept_id());
        purchaseApplyVo.getPurchaseApply().setDept_name(sysDepartment == null ? "" : sysDepartment.getDept_name());

        String apply_userName = "";
        Integer apply_user = purchaseApplyVo.getPurchaseApply().getApply_user();
        if (null != apply_user && null != sysUserService.getUser(apply_user)){
            apply_userName = sysUserService.getUser(apply_user).getUser_name();
        }

        purchaseApplyVo.getPurchaseApply().setApply_user_name(apply_userName);

        List<PurchaseDetail> details = purchaseDetailService.selectList(Wrappers.<PurchaseDetail>lambdaQuery()
                .eq(PurchaseDetail::getPurchase_apply_id, id)
                .eq(PurchaseDetail::getIs_del, "0"));
        for (PurchaseDetail detail : details) {
            AssetsType assetsType = assetsTypeService.selectById(detail.getAssets_type_id());
            detail.setAssets_type_name(assetsType == null ? "" : assetsType.getAssets_type_name());
            SysDict sysDict = sysDictService.selectOne(Wrappers.<SysDict>lambdaQuery().
                    eq(SysDict::getType, "unit").
                    eq(SysDict::getValue, detail.getUnits()).
                    eq(SysDict::getIs_del, "0"));
            detail.setUnits_name(sysDict == null ? "" : sysDict.getLabel());
            BrandManage sysDict1 = brandManageMapper.selectById(detail.getAssets_brand());
            detail.setBrand_name(sysDict1 == null ? "" : sysDict1.getBrand_name());
        }
        purchaseApplyVo.setPurchaseDetails(details);

        return purchaseApplyVo;

    }

    public List<PurchaseDetail> toPurchaseDetails(PurchaseApplyVo purchaseApplyVo) {

        List<PurchaseDetail> purchaseDetails = purchaseDetailService.selectList(Wrappers.<PurchaseDetail>lambdaQuery().
                eq(PurchaseDetail::getIs_del, "0").
                eq(PurchaseDetail::getPurchase_apply_id, purchaseApplyVo.getPurchaseApply().getId()));

        purchaseDetails.forEach(x -> {
            x.setAssets_type_name(assetsTypeService.
                    selectById(x.getAssets_type_id()).
                    getAssets_type_name());
            SysDict sysDict = sysDictService.selectOne(Wrappers.<SysDict>lambdaQuery().
                    eq(SysDict::getType, "unit").
                    eq(SysDict::getValue, x.getUnits()).
                    eq(SysDict::getIs_del, "0"));
            x.setUnits_name(sysDict == null ? "" : sysDict.getLabel());
            BrandManage sysDict1 = brandManageMapper.selectById(x.getAssets_brand());
            x.setBrand_name(sysDict1 == null ? "" : sysDict1.getBrand_name());
        });

        return purchaseDetails;
    }

    public PageInfo<PurchaseDetail> toPagePurchaseDetails(Integer pageIndex, Integer pageSize, Integer id) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<PurchaseDetail> purchaseDetails = purchaseDetailService.selectList(Wrappers.<PurchaseDetail>lambdaQuery().
                eq(PurchaseDetail::getIs_del, "0").
                eq(PurchaseDetail::getPurchase_apply_id, id));
        // 品牌map
        Map<Integer, SysDict> brandMap = commonDataTools.getSysDictMap("brand_type");
        // 计量单位map
        Map<Integer, SysDict> unitMap = commonDataTools.getSysDictMap("unit");
        purchaseDetails.forEach(x -> {
            x.setAssets_type_name(assetsTypeService.selectById(x.getAssets_type_id()).getAssets_type_name());
            BrandManage brandManage = brandManageMapper.selectById(x.getAssets_brand());
            x.setBrand_name(brandManage == null ? "" : brandManage.getBrand_name());
            x.setUnits_name(commonDataTools.getValue(unitMap, x.getUnits(), "label"));
        });

        return new PageInfo<>(purchaseDetails);
    }

    public void toEditPurchaseDetails(PurchaseApplyVo purchaseApplyVo) {
        Integer userId = null;
        if (Base.notEmpty(purchaseApplyVo.getUserId())) {
            userId = purchaseApplyVo.getUserId();
        } else {
            userId = sysUserService.getUser().getId();
        }

        Integer finalUserId = userId;
        purchaseApplyVo.getPurchaseDetails().forEach(x -> {
            x.setUpdate_time(new Date());
            x.setUpdate_user(finalUserId);
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
                .type("2").userId(userId).record("资产采购到货验收").build());


    }

    public void toEditApply(PurchaseApplyVo purchaseApplyVo) {
        Integer userId = null;
        if (Base.notEmpty(purchaseApplyVo.getUserId())) {
            userId = purchaseApplyVo.getUserId();
        } else {
            userId = sysUserService.getUser().getId();
        }
        purchaseApplyVo.getPurchaseApply().setUpdate_user(userId);
        purchaseApplyVo.getPurchaseApply().setUpdate_time(new Date());

//        purchaseApplyVo.getPurchaseApply().updateById();

        toEdit(purchaseApplyVo);

        // 流程启动
        processMemberService.startProcess(G.PROCESS_ZCCG, purchaseApplyVo.getPurchaseApply().getId(), userId);
    }

    public void toRevokeApply(PurchaseApplyVo purchaseApplyVo) {
        Integer userId = null;
        if (Base.notEmpty(purchaseApplyVo.getUserId())) {
            userId = purchaseApplyVo.getUserId();
        } else {
            userId = sysUserService.getUser().getId();
        }

        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCCG);
        ProcessMember processMember = processMemberService.getProcessMemberByBusId(purchaseApplyVo.getPurchaseApply().getId(), dictionary.getValue());
        if (null == processMember) {
            throw new RuntimeException("未查询到流程");
        }
        if (processMember.getIs_revoke() != 1) {
            throw new RuntimeException("当前流程已经审批，不能撤回!");
        }
        processMember.setUserId(userId);
        processMember.setIsCH(1);
        processMemberService.removeProcessMember(processMember, JSON.toJSONString(purchaseApplyVo.getPurchaseApply()));

        // 修改状态
        purchaseApplyVo.getPurchaseApply().setUpdate_user(userId);
        purchaseApplyVo.getPurchaseApply().setUpdate_time(new Date());

        purchaseApplyVo.getPurchaseApply().updateById();

        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2").userId(userId).record("采购申请-采购单撤回").build());
    }

    public int updatePurchaseDetails(PurchaseDetail purchaseDetail) {
        return purchaseDetailService.updateById(purchaseDetail);
    }

    public void toDel(PurchaseApply purchaseApply) {
        Integer userId = null;
        if (Base.notEmpty(purchaseApply.getUserId())) {
            userId = purchaseApply.getUserId();
        } else {
            userId = sysUserService.getUser().getId();
        }

        purchaseApply.setUpdate_time(new Date());
        purchaseApply.setUpdate_user(userId);

        purchaseApply.updateById();

        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApply.getPurchase_no())
                .type("2").userId(userId).record("资产采购删除").build());


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

    public PageInfo purchaseFromSupplierList(Integer pageIndex, Integer pageSize, PurchaseApply beanParam) {
        if (StringUtils.isNotBlank(beanParam.getSupplier_name())) {
            List<Supplier> list = supplierMapper.selectList(Wrappers.<Supplier>lambdaQuery()
                    .eq(Supplier::getSupplier_name, beanParam.getSupplier_name())
                    .eq(Supplier::getIs_del, "0")
            );
            if (!CollectionUtils.isEmpty(list)) {
                beanParam.setSupplier_id(list.get(0).getId());
            }
        }

        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<PurchaseApply> purchaseApplies = purchaseApplyMapper.selectList(Wrappers.<PurchaseApply>lambdaQuery()
                .eq(PurchaseApply::getIs_del, "0")
                .eq(null != beanParam.getSupplier_id(), PurchaseApply::getSupplier_id, beanParam.getSupplier_id())
        );
        for (PurchaseApply purchaseApply : purchaseApplies) {
            SysUser sysUser = sysUserService.selectById(purchaseApply.getApply_user());
            if (null != sysUser) {
                purchaseApply.setApply_user_name(sysUser.getUser_name());
            }
            Supplier supplier = supplierService.selectById(purchaseApply.getSupplier_id());
            if (null != supplier) {
                purchaseApply.setSupplier_name(supplier.getSupplier_name());
            }
        }
        return new PageInfo<>(purchaseApplies);
    }

    public PageInfo<PurchaseApply> tocgclList(Integer pageIndex, Integer pageSize, PurchaseApplyVo purchaseApplyVo) {

        LambdaQueryWrapper<PurchaseApply> wrapper = Wrappers.<PurchaseApply>lambdaQuery().eq(PurchaseApply::getIs_del, G.ISDEL_NO)
                .like(StringUtils.isNotBlank(purchaseApplyVo.getPurchase_name()), PurchaseApply::getPurchase_name, purchaseApplyVo.getPurchase_name())
                .like(StringUtils.isNotBlank(purchaseApplyVo.getPurchase_no()), PurchaseApply::getPurchase_no, purchaseApplyVo.getPurchase_no())
                .eq(StringUtils.isNotBlank(purchaseApplyVo.getPut_order()), PurchaseApply::getPut_order, "2")
                .in(!CollectionUtils.isEmpty(purchaseApplyVo.getIds()), PurchaseApply::getId, purchaseApplyVo.getIds())
                .eq(Base.notEmpty(purchaseApplyVo.getApprove_status()), PurchaseApply::getApprove_status, purchaseApplyVo.getApprove_status());
        // 增加关键字查询条件
        if (StringUtils.isNotBlank(purchaseApplyVo.getTransfer())) {
            // purchase_no like '%关键字%' or purchase_name like '%关键字%'
            wrapper.and(x -> x.like(PurchaseApply::getPurchase_no, purchaseApplyVo.getTransfer()).or().like(PurchaseApply::getPurchase_name, purchaseApplyVo.getTransfer()));
        }
        if (StringUtils.isNotBlank(purchaseApplyVo.getPurchase_type())) {
            wrapper.eq(PurchaseApply::getPurchase_type, "2");
        } else {
            wrapper.isNull(PurchaseApply::getPurchase_type);
        }
        // 申请人
        if (StringUtils.isNotBlank(purchaseApplyVo.getApply_user_name())) {
            List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                    .like(SysUser::getUser_name, purchaseApplyVo.getApply_user_name())
                    .eq(SysUser::getIs_del, G.ISDEL_NO)
            );
            List<Integer> userIds = sysUsers.stream().map(SysUser::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIds)) {
                userIds.add(-1);
            }
            wrapper.in(StringUtils.isNotBlank(purchaseApplyVo.getApply_user_name()), PurchaseApply::getApply_user, userIds);
        }
        // 入库状态
        if (StringUtils.isNotBlank(purchaseApplyVo.getPut_status())) {
            wrapper.eq(PurchaseApply::getPut_status, purchaseApplyVo.getPut_status());
        }
        // 申请日期范围
        if (StringUtils.isNotBlank(purchaseApplyVo.getApplyTimeStr())) {

        }

        wrapper.orderByAsc(PurchaseApply::getApprove_status);
        wrapper.orderByDesc(PurchaseApply::getCreate_time);

        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<PurchaseApply> purchaseApplies = selectList(wrapper);
        for (PurchaseApply purchaseApply : purchaseApplies) {
            SysUser sysUser = sysUserService.selectById(purchaseApply.getApply_user());
            if (null != sysUser) {
                purchaseApply.setApply_user_name(sysUser.getUser_name());
            }
            Supplier supplier = supplierService.selectById(purchaseApply.getSupplier_id());
            if (null != supplier) {
                purchaseApply.setSupplier_name(supplier.getSupplier_name());
            }
        }
        return new PageInfo<>(purchaseApplies);
    }
}
