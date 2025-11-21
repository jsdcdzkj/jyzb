package com.jsdc.rfid.controller;


import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.SpringUtils;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.AssetsManageMapper;
import com.jsdc.rfid.mapper.ConsInventoryManagementMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import lombok.AllArgsConstructor;
import net.hasor.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import vo.ConsPurchaseApplyVo;
import vo.ResultInfo;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ClassName: ConsPurchaseApplyController
 * Description:  易耗品采购申请
 * date: 2022/4/24 16:55
 *
 * @author bn
 */
@Controller
@RequestMapping("consPurchaseApply")
@AllArgsConstructor
public class ConsPurchaseApplyController extends BaseController {

    private ConsPurchaseApplyService consPurchaseApplyService;

    private OperationRecordService operationRecordService;

    private ConsPurchaseDetailService consPurchaseDetailService;

    private BrandManageService brandManageService;

    private ProcessMemberService processMemberService;

    private SysUserService sysUserService;

    private SysDictService sysDictService;

    private ConsumableService consumableService;

    private ConsInventoryManagementMapper inventoryManagementMapper;

    /**
     * 首页跳转
     *
     * @return
     */
    @RequestMapping(value = "toPurchaseApply.do")
    public String toPurchaseApply() {
        return "/haocai/capital/index";
    }

    /**
     * 新增跳转
     *
     * @return
     */
    @RequestMapping(value = "toAddPage.do")
    public String toAddPage(Model model) {
        // 得到品牌
        List<BrandManage> brandManages = brandManageService.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, G.ISDEL_NO));
        model.addAttribute("brandManages", brandManages);
        // 得到当前人
        model.addAttribute("user", sysUserService.getUser());
        // 得到类型
        List<Consumable> consumables = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                .eq(Consumable::getParent_id, 0)
                .eq(Consumable::getIs_del, G.ISDEL_NO)
        );
        for (Consumable consumable : consumables) {
            List<Consumable> consumableList = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                    .eq(Consumable::getParent_id, consumable.getId())
                    .eq(Consumable::getIs_del, G.ISDEL_NO)
            );
            consumable.setChildren(consumableList);
        }
        model.addAttribute("consumables", consumables);

        Set<String> result = null;
        // 型号
        Set<String> models = null;
        List<ConsInventoryManagement> a = inventoryManagementMapper.selectList(Wrappers.<ConsInventoryManagement>lambdaQuery()
                .eq(ConsInventoryManagement::getIs_del, G.ISDEL_NO)
        );
        result = new HashSet<>();
        models = new HashSet<>();
        if (!CollectionUtils.isEmpty(a)){
            for (ConsInventoryManagement consumable : a){
                if (StringUtils.isNotBlank(consumable.getName())){
                    result.add(consumable.getName());
                }
                if (StringUtils.isNotBlank(consumable.getModel())){
                    models.add(consumable.getModel());
                }
            }
        }
        model.addAttribute("labels", result);
        model.addAttribute("models", models);

        return "/haocai/capital/add";
    }

    /**
     * 标签联想数据
     */
    @GetMapping("getConsumableName.do")
    @ResponseBody
    public ResultInfo getConsumableName(@RequestParam(required = false) String keywords){
        Set<String> result = null;
        try {
            List<ConsInventoryManagement> a = inventoryManagementMapper.selectList(Wrappers.<ConsInventoryManagement>lambdaQuery()
                    .like(StringUtils.isNotBlank(keywords), ConsInventoryManagement::getConsumable_name, keywords)
                    .eq(ConsInventoryManagement::getIs_del, G.ISDEL_NO)
            );
            result = new HashSet<>();
            if (CollectionUtils.isEmpty(a)){
                return ResultInfo.success(result);
            }
            for (ConsInventoryManagement consumable : a){
                result.add(consumable.getConsumable_name());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success(result);
    }

    /**
     * 编辑跳转
     *
     * @return
     */
    @RequestMapping(value = "toEditPage.do")
    public String toEditPage(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("bean", null == id? null :consPurchaseApplyService.getById(id));
        // 得到品牌
        List<BrandManage> brandManages = brandManageService.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, G.ISDEL_NO));
        model.addAttribute("brandManages", brandManages);
        // 得到类型
        List<Consumable> consumables = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                .eq(Consumable::getParent_id, 0)
                .eq(Consumable::getIs_del, G.ISDEL_NO)
        );
        for (Consumable consumable : consumables) {
            List<Consumable> consumableList = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                    .eq(Consumable::getParent_id, consumable.getId())
                    .eq(Consumable::getIs_del, G.ISDEL_NO)
            );
            consumable.setChildren(consumableList);
        }
        model.addAttribute("consumables", consumables);

        Set<String> result = null;
        // 型号
        Set<String> models = null;
        List<ConsInventoryManagement> a = inventoryManagementMapper.selectList(Wrappers.<ConsInventoryManagement>lambdaQuery()
                .eq(ConsInventoryManagement::getIs_del, G.ISDEL_NO)
        );
        result = new HashSet<>();
        models = new HashSet<>();
        if (!CollectionUtils.isEmpty(a)){
            for (ConsInventoryManagement consumable : a){
                if (StringUtils.isNotBlank(consumable.getName())){
                    result.add(consumable.getName());
                }
                if (StringUtils.isNotBlank(consumable.getModel())){
                    models.add(consumable.getModel());
                }
            }
        }
        model.addAttribute("labels", result);
        model.addAttribute("models", models);

        // 供应商
        model.addAttribute("suppliers", SpringUtils.getBean(SupplierService.class).getList(Supplier.builder().is_del(G.ISDEL_NO).build()));
        // 部门
        model.addAttribute("depts", SpringUtils.getBean(SysDepartmentService.class).getList(new SysDepartment()));
        return "/haocai/capital/edit";
    }


    @RequestMapping(value = "toEditAssetPage.do")
    public String toEditAssetPage(@RequestParam(required = false) Integer id,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) String assets_brand,
                                  @RequestParam(required = false) String consumable_id2,
                                  @RequestParam(required = false) String model2,
                                  @RequestParam(required = false) String purchase_num,
                                  @RequestParam(required = false) String units,
                                  @RequestParam(required = false) String purchase_price,
                                  @RequestParam(required = false) String updateIndex,
                                  Model model) {
        Integer brand = null;
        if (StrUtil.isNotBlank(assets_brand) && !StrUtil.equals(assets_brand, "null")){
            brand = Integer.parseInt(assets_brand);
        }
        // 编辑的数据
        if (StrUtil.isNotBlank(consumable_id2) && StrUtil.isNotBlank(model2)
                && StrUtil.isNotBlank(purchase_num) && StrUtil.isNotBlank(units)
                && StrUtil.isNotBlank(purchase_price) && StrUtil.isNotBlank(updateIndex)){
            model.addAttribute("purchaseDetail", ConsPurchaseDetail.builder()
                    .units(Integer.parseInt(units)).purchase_num(Integer.parseInt(purchase_num))
                    .consumable_id2(Integer.parseInt(consumable_id2))
                    .assets_brand(brand)
                    .id(id).purchase_price(purchase_price)
                    .model(model2).name(name)
                    .updateIndex(Integer.parseInt(updateIndex))
                    .build()
            );
        }else {
            model.addAttribute("purchaseDetail", ConsPurchaseDetail.builder().build());
        }

        // 得到品牌
        model.addAttribute("brandManages", brandManageService.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, G.ISDEL_NO)));
        // 得到类型
        List<Consumable> consumables = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                .eq(Consumable::getParent_id, 0)
                .eq(Consumable::getIs_del, G.ISDEL_NO)
        );
        for (Consumable consumable : consumables) {
            List<Consumable> consumableList = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                    .eq(Consumable::getParent_id, consumable.getId())
                    .eq(Consumable::getIs_del, G.ISDEL_NO)
            );
            consumable.setChildren(consumableList);
        }
        model.addAttribute("consumables", consumables);

        return "/haocai/capital/edit_asset";
    }

    @GetMapping("getModels.do")
    @ResponseBody
    public ResultInfo getModels() {
        Set<String> result = null;
        // 型号
        Set<String> models = null;
        List<ConsInventoryManagement> a = inventoryManagementMapper.selectList(Wrappers.<ConsInventoryManagement>lambdaQuery()
                .eq(ConsInventoryManagement::getIs_del, G.ISDEL_NO)
        );
        result = new HashSet<>();
        models = new HashSet<>();
        if (!CollectionUtils.isEmpty(a)){
            for (ConsInventoryManagement consumable : a){
                if (StringUtils.isNotBlank(consumable.getName())){
                    result.add(consumable.getName());
                }
                if (StringUtils.isNotBlank(consumable.getModel())){
                    models.add(consumable.getModel());
                }
            }
        }
        Map<String, Object> resultObj = new HashMap<>();
        resultObj.put("labels", result);
        resultObj.put("models", models);
        return ResultInfo.success(resultObj);
    }
    /**
     * 视图跳转
     *
     * @return
     */
    @RequestMapping(value = "toViewPage.do")
    public String toViewPage(Integer id, Integer type, Model model) {
        ConsPurchaseApplyVo consPurchaseApplyVo = consPurchaseApplyService.getById(id);
        SysUser sysUser = sysUserService.getUser();
        // 得到当前流程节点
        consPurchaseApplyVo = processMemberService.getProcessDataByBusId(consPurchaseApplyVo.getPurchaseApply().getId(), G.PROCESS_HCCG, sysUser, consPurchaseApplyVo);
        model.addAttribute("bean", consPurchaseApplyVo);
        model.addAttribute("id", id);
        model.addAttribute("type", type);

        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_HCCG);
        model.addAttribute("key", dictionary.getValue());
        return "/haocai/capital/view";
    }

    /**
     * 日志详情
     *
     * @param purchase_no
     * @param model
     * @return
     */
    @RequestMapping(value = "toLogView.do")
    public String toLogView(String purchase_no, Model model) {
        List<OperationRecord> operationRecords = operationRecordService.
                selectList(Wrappers.<OperationRecord>query().
                        eq("field_fk", purchase_no).
                        eq("type", "2").
                        eq("is_del", "0")
                        .orderByDesc("create_time")
                );
        model.addAttribute("operationRecords", operationRecords);

        return "/purchase/capital/view-log";
    }

    /**
     * 到货验收
     *
     * @return
     */
    @RequestMapping(value = "toCheckPage.do")
    public String toCheckPage(Integer id, String purchase_no, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("purchase_no", purchase_no);
        return "/haocai/capital/check";
    }


    /**
     * 跳转打印详情页
     *
     * @return
     */
    @RequestMapping("toPrint.do")
    public String toPrint(Integer id, Model model) {
        ConsPurchaseApplyVo consPurchaseApplyVo = consPurchaseApplyService.getById(id);
        ConsPurchaseApply consPurchaseApply = consPurchaseApplyVo.getPurchaseApply();
        List<ConsPurchaseDetail> consPurchaseDetails = consPurchaseApplyVo.getPurchaseDetails();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        model.addAttribute("purchaseApply", consPurchaseApply);
        model.addAttribute("purchaseDetails", consPurchaseDetails);
        model.addAttribute("time", time);
        return "/haocai/capital/print";
    }

    /**
     * 采购申请列表展示
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit, ConsPurchaseApplyVo consPurchaseApplyVo) {
        PageInfo<ConsPurchaseApply> pageInfo = consPurchaseApplyService.toList(page, limit, consPurchaseApplyVo);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 到货验收列表
     *
     * @param consPurchaseApplyVo
     * @return
     */
    @RequestMapping(value = "toPurchaseDetails.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toPurchaseDetails(@RequestBody ConsPurchaseApplyVo consPurchaseApplyVo) {


        return ResultInfo.success(consPurchaseApplyService.toPurchaseDetails(consPurchaseApplyVo));
    }

    /**
     * 根据采购id 得到采购表数据
     *
     * @param pageIndex
     * @param pageSize
     * @param id
     * @return zdq
     */
    @RequestMapping(value = "toPagePurchaseDetails.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toPagePurchaseDetails(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                                            @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                                            Integer id) {
        return ResultInfo.success(consPurchaseApplyService.toPagePurchaseDetails(pageIndex, pageSize, id));
    }

    /**
     * 得到采购表详情信息
     */
    @RequestMapping(value = "toPurchaseDetailById.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toPurchaseDetailById(Integer id) {
        return ResultInfo.success(consPurchaseApplyService.toPurchaseDetailById(id));
    }


    /**
     * 根据id获取数据
     *
     * @return
     */
    @RequestMapping(value = "getById.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(ConsPurchaseApplyVo consPurchaseApplyVo) {


        return ResultInfo.success(consPurchaseApplyService.getById(consPurchaseApplyVo.getId()));
    }


    /**
     * 新增
     *
     * @return
     */
    @RequestMapping(value = "toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAdd(@RequestBody ConsPurchaseApplyVo consPurchaseApplyVo) {
        return consPurchaseApplyService.toAdd(consPurchaseApplyVo);
    }

    /**
     * 新增并送审
     *
     * @param consPurchaseApplyVo
     * @return
     */
    @RequestMapping(value = "toAddAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAddAndSub(@RequestBody ConsPurchaseApplyVo consPurchaseApplyVo) {
        consPurchaseApplyVo.getPurchaseApply().setApprove_status("2");
        return consPurchaseApplyService.toAddAndSub(consPurchaseApplyVo, sysUserService.getUser());
    }

    /**
     * 审批
     *
     * @param consPurchaseApplyVo
     * @return
     */
    @RequestMapping(value = "toApply.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toApply(@RequestBody ConsPurchaseApplyVo consPurchaseApplyVo) {


        return consPurchaseApplyService.toApply(consPurchaseApplyVo);
    }


    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping(value = "toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEdit(@RequestBody ConsPurchaseApplyVo consPurchaseApplyVo) {
        consPurchaseApplyService.toEdit(consPurchaseApplyVo);

        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(consPurchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2").record("采购申请-编辑采购单").build());
        return ResultInfo.success();
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "toDel.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toDel(ConsPurchaseApply consPurchaseApply) {
        consPurchaseApplyService.toDel(consPurchaseApply);
        return ResultInfo.success();
    }

    /**
     * 采购送审
     *
     * @return
     */
    @RequestMapping(value = "toEditApply.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEditApply(@RequestBody ConsPurchaseApplyVo consPurchaseApplyVo) {
        consPurchaseApplyService.toEditApply(consPurchaseApplyVo);
        return ResultInfo.success();
    }

    /**
     * 编辑并送审
     *
     * @return
     */
    @RequestMapping(value = "toEditAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEditAndSub(@RequestBody ConsPurchaseApplyVo consPurchaseApplyVo) {
        consPurchaseApplyService.toEditAndSub(consPurchaseApplyVo);
        return ResultInfo.success();
    }

    /**
     * 送审撤回
     *
     * @return
     */
    @RequestMapping(value = "toRevokeApply.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toRevokeApply(@RequestBody ConsPurchaseApplyVo consPurchaseApplyVo) {
        try {
            consPurchaseApplyService.toRevokeApply(consPurchaseApplyVo);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    /**
     * 到货验收
     *
     * @return
     */
    @RequestMapping(value = "toEditPurchaseDetails.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEditPurchaseDetails(@RequestBody ConsPurchaseApplyVo consPurchaseApplyVo) {
        consPurchaseApplyService.toEditPurchaseDetails(consPurchaseApplyVo);
        return ResultInfo.success();
    }


    /**
     * 生成入库单
     *
     * @return
     */
    @RequestMapping(value = "toWarehousing.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toWarehousing(ConsPurchaseApply consPurchaseApply) {
        consPurchaseApplyService.toWarehousing(consPurchaseApply);

        return ResultInfo.success();
    }


    /**
     * 跳转采购待审批页面
     *
     * @return
     */
    @RequestMapping(value = "adopt.do")
    public String adoptList(Model model, Integer status) {
        model.addAttribute("status", status);
        return "haocai/shengpi/cgIndex";
    }


    /**
     * 采购待审批数据
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "adopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ConsPurchaseApplyVo bean) {
        // 根据流程进行业务id筛选
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUserService.getUser(), G.PROCESS_HCCG);
        if (CollectionUtils.isEmpty(busIds)) {
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        bean.setIs_adopt(1);
        PageInfo<ConsPurchaseApply> pageInfo = consPurchaseApplyService.toList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 跳转采购已审批
     */
    @RequestMapping(value = "finishAdopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo finishAdopt(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ConsPurchaseApplyVo bean) {
        // 根据流程进行业务id筛选
        List<Integer> busIds = processMemberService.getBusIdsByUserIdHistory(sysUserService.getUser(), G.PROCESS_HCCG);
        if (CollectionUtils.isEmpty(busIds)) {
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo<ConsPurchaseApply> pageInfo = consPurchaseApplyService.finishAdopt(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 修改采购详情
     */
    @RequestMapping(value = "updatePurchaseDetails.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updatePurchaseDetails(ConsPurchaseDetail consPurchaseDetail) {
        int count = consPurchaseApplyService.updatePurchaseDetails(consPurchaseDetail);
        if (count > 0) {
            return ResultInfo.success("操作成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 入库操作,修改数量
     */
    @RequestMapping(value = "updatePurchaseDetailsRK.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updatePurchaseDetailsRK(ConsPurchaseDetail consPurchaseDetail) {
        ConsPurchaseDetail old = consPurchaseDetailService.selectById(consPurchaseDetail.getId());
        if (null == old) {
            return ResultInfo.error("数据不存在");
        }
        int current_num = NumberUtil.parseInt(old.getAccept_num());
        Integer inbound_num = consPurchaseDetail.getInbound_num();
        // 比较数量
        if (current_num < inbound_num) {
            if (null != old.getInbound_num() && old.getInbound_num() > 0) {
                consPurchaseDetail.setInbound_num(0);
                consPurchaseApplyService.updatePurchaseDetails(consPurchaseDetail);
            }
            return ResultInfo.error("入库数量不能大于验收数量");
        }
        int count = consPurchaseApplyService.updatePurchaseDetails(consPurchaseDetail);
        if (count > 0) {
            return ResultInfo.success("操作成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }
}
