package com.jsdc.rfid.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.SpringUtils;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.AssetsManageMapper;
import com.jsdc.rfid.mapper.PurchaseDetailMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import net.hasor.utils.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import vo.PurchaseApplyVo;
import vo.ResultInfo;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ClassName: PurchaseApplyController
 * Description:  采购申请
 * date: 2022/4/24 16:55
 *
 * @author bn
 */
@Controller
@RequestMapping("purchaseApply")
public class PurchaseApplyController extends BaseController {

    @Autowired
    private PurchaseApplyService purchaseApplyService;
    @Autowired
    private OperationRecordService operationRecordService;

    @Autowired
    private BrandManageService brandManageService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SysDepartmentService departmentService;

    @Autowired
    private AssetsTypeService assetsTypeService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private ProcessMemberService processMemberService;


    @Autowired
    private ProcessMemberHistoryService historyService;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AssetsManageMapper assetsManageMapper;

    /**
     * 首页跳转
     *
     * @return
     */
    @RequestMapping(value = "toPurchaseApply.do")
    public String toPurchaseApply(Model model) {
        return "/purchase/capital/index";
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
        // user
        model.addAttribute("user", sysUserService.getUser());
        return "/purchase/capital/add";
    }

    /**
     * 编辑跳转
     *
     * @return
     */
    @RequestMapping(value = "toEditPage.do")
    public String toEditPage(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("obj", null == id?null:purchaseApplyService.getById(id));
        // 得到品牌
        List<BrandManage> brandManages = brandManageService.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, G.ISDEL_NO));
        model.addAttribute("brandManages", brandManages);
        // 得到供应商
        List<Supplier> suppliers = supplierService.selectList(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getIs_del, G.ISDEL_NO));
        model.addAttribute("suppliers", suppliers);
        // 得到部门
        List<SysDepartment> departments = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("departments", departments);
        // 得到品类
        List<AssetsType> assetsTypes = assetsTypeService.selectList(Wrappers.<AssetsType>lambdaQuery().eq(AssetsType::getIs_del, G.ISDEL_NO));
        model.addAttribute("assetsTypes", assetsTypes);
        // 得到单位
        List<SysDict> sysDicts = sysDictService.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getIs_del, G.ISDEL_NO).eq(SysDict::getType, "unit"));
        model.addAttribute("sysDicts", sysDicts);
        return "/purchase/capital/edit";
    }

    ////assets_type_id:'',
    //				// assets_name:'',
    //				// 		assets_model:'',
    //				// 		assets_brand:'',
    //				// 		purchase_price:'',
    //				// 		purchase_num:'',
    //				// 		units:'',
    @RequestMapping(value = "toEditAssetPage.do")
    public String toEditAssetPage(@RequestParam(required = false) Integer id,
                                  @RequestParam(required = false) String assets_type_id,
                                  @RequestParam(required = false) String assets_name,
                                  @RequestParam(required = false) String assets_model,
                                  @RequestParam(required = false) String assets_brand,
                                  @RequestParam(required = false) String purchase_price,
                                  @RequestParam(required = false) String purchase_num,
                                  @RequestParam(required = false) String units,
                                  @RequestParam(required = false) String updateIndex,
                                  Model model) {
        Integer brand = null;
        if (StrUtil.isNotBlank(assets_brand) && !StrUtil.equals(assets_brand, "null")){
            brand = Integer.parseInt(assets_brand);
        }
        // 编辑的数据
        if (StrUtil.isNotBlank(assets_type_id) && StrUtil.isNotBlank(assets_name) && StrUtil.isNotBlank(assets_model)
                && StrUtil.isNotBlank(purchase_num) && StrUtil.isNotBlank(units)
                && StrUtil.isNotBlank(purchase_price) && StrUtil.isNotBlank(updateIndex)){
            model.addAttribute("purchaseDetail", PurchaseDetail.builder()
                    .units(Integer.parseInt(units))
                    .purchase_num(Integer.parseInt(purchase_num))
                    .assets_brand(brand)
                    .id(id)
                    .purchase_price(purchase_price)
                    .updateIndex(Integer.parseInt(updateIndex))
                    .assets_model(assets_model)
                    .assets_name(assets_name)
                    .assets_type_id(Integer.parseInt(assets_type_id))
                    .build()
            );
        }else {
            model.addAttribute("purchaseDetail", ConsPurchaseDetail.builder().build());
        }

        // 得到品牌
        model.addAttribute("brandManages", brandManageService.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, G.ISDEL_NO)));
        // 得到类型
        List<AssetsType> assetsTypes = assetsTypeService.selectList(Wrappers.<AssetsType>lambdaQuery()
                .eq(AssetsType::getIs_del, G.ISDEL_NO)
                .isNotNull(AssetsType::getAssets_type_code)
                .ne(AssetsType::getAssets_type_code,"")
        );
        model.addAttribute("assetsTypes", assetsTypes);
        //model.addAttribute("assetsTypesTree",assetsTypeService.loadTree(new AssetsType()));
        return "/purchase/capital/edit_asset";
    }
    @GetMapping("getModels.do")
    @ResponseBody
    public ResultInfo getModels() {
        Set<String> result = null;
        // 型号
        Set<String> models = null;
        List<AssetsManage> a = SpringUtils.getBean(AssetsManageMapper.class).selectList(Wrappers.<AssetsManage>query()
                .select("asset_name,specification")
                .eq("is_del", G.ISDEL_NO)
        );
        result = new HashSet<>();
        models = new HashSet<>();
        if (!CollectionUtils.isEmpty(a)){
            for (AssetsManage consumable : a){
                if (StringUtils.isNotBlank(consumable.getAsset_name())){
                    result.add(consumable.getAsset_name().trim());
                }
                if (StringUtils.isNotBlank(consumable.getSpecification())){

                    String specification = consumable.getSpecification().trim();
                    // 去除换行字符
                    specification = specification.replaceAll("\n", "");
                    models.add(specification);
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
        PurchaseApplyVo a = purchaseApplyService.getById(id);
        processMemberService.getProcessDataByBusId(a.getPurchaseApply().getId(), G.PROCESS_ZCCG, sysUserService.getUser(), a);
        model.addAttribute("purchase", a);
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode("资产采购");
        model.addAttribute("key", dictionary.getValue());
        return "/purchase/capital/view";
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
        List<OperationRecord> operationRecords = operationRecordService.selectList(Wrappers.<OperationRecord>query()
                .eq("field_fk", purchase_no)
                .eq("type", "2")
                .eq("is_del", G.ISDEL_NO)
                .orderByDesc("create_time"));
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
        return "/purchase/capital/check";
    }


    /**
     * 跳转打印详情页
     *
     * @return
     */
    @RequestMapping("toPrint.do")
    public String toPrint(Integer id, Model model) {
        PurchaseApplyVo purchaseApplyVo = purchaseApplyService.getById(id);
        PurchaseApply purchaseApply = purchaseApplyVo.getPurchaseApply();
        List<PurchaseDetail> purchaseDetails = purchaseApplyVo.getPurchaseDetails();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        model.addAttribute("purchaseApply", purchaseApply);
        model.addAttribute("purchaseDetails", purchaseDetails);
        model.addAttribute("time", time);
        return "/purchase/capital/print";
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
                             @RequestParam(defaultValue = "10") Integer limit, PurchaseApplyVo purchaseApplyVo) {
        PageInfo<PurchaseApply> pageInfo = purchaseApplyService.toList(page, limit, purchaseApplyVo);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 到货验收列表
     *
     * @param purchaseApplyVo
     * @return
     */
    @RequestMapping(value = "toPurchaseDetails.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toPurchaseDetails(@RequestBody PurchaseApplyVo purchaseApplyVo) {


        return ResultInfo.success(purchaseApplyService.toPurchaseDetails(purchaseApplyVo));
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
        return ResultInfo.success(purchaseApplyService.toPagePurchaseDetails(pageIndex, pageSize, id));
    }


    /**
     * 根据id获取数据
     *
     * @return
     */
    @RequestMapping(value = "getById.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(PurchaseApplyVo purchaseApplyVo) {


        return ResultInfo.success(purchaseApplyService.getById(purchaseApplyVo.getId()));
    }


    /**
     * 新增
     * {"purchaseApply":{"purchase_name":"名称","supplier":"上汽集团","purchase_amount":"40.00","approve_status":"1","inspected_status":"1","put_order":"1","put_status":"1"},
     * "purchaseDetails":[{"assets_type_id":1,"assets_name":"台式机","assets_model":"T500","assets_brand":"联想","purchase_price":"10","purchase_num":4,"units":"1"},{"assets_type_id":1,"assets_name":"台式机","assets_model":"T560","assets_brand":"联想","purchase_price":"10","purchase_num":4,"units":"1"},
     * {"assets_type_id":1,"assets_name":"台式机","assets_model":"T510","assets_brand":"联想","purchase_price":"10","purchase_num":4,"units":"1"}]
     * <p>
     * }
     *
     * @return
     */
    @RequestMapping(value = "toAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAdd(@RequestBody PurchaseApplyVo purchaseApplyVo) {

        return purchaseApplyService.toAdd(purchaseApplyVo);
    }

    /**
     * 新增并送审
     *
     * @param purchaseApplyVo
     * @return
     */
    @RequestMapping(value = "toAddAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAddAndSub(@RequestBody PurchaseApplyVo purchaseApplyVo) {
        // 送审状态
        //purchaseApplyVo.getPurchaseApply().setApprove_status("2");
        purchaseApplyVo.getPurchaseApply().setApprove_status("4");//去除审批流程，直接审批成功
        return purchaseApplyService.toAddAndSub(purchaseApplyVo, sysUserService.getUser());
    }

    /**
     * 审批
     *
     * @param purchaseApplyVo
     * @return
     */
    @RequestMapping(value = "toApply.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toApply(@RequestBody PurchaseApplyVo purchaseApplyVo) {


        return purchaseApplyService.toApply(purchaseApplyVo);
    }


    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping(value = "toEdit.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEdit(@RequestBody PurchaseApplyVo purchaseApplyVo) {
        purchaseApplyService.toEdit(purchaseApplyVo);

        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
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
    public ResultInfo toDel(PurchaseApply purchaseApply) {
        purchaseApplyService.toDel(purchaseApply);
        return ResultInfo.success();
    }

    /**
     * 采购送审
     *
     * @return
     */
    @RequestMapping(value = "toEditApply.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEditApply(@RequestBody PurchaseApplyVo purchaseApplyVo) {
        purchaseApplyService.toEditApply(purchaseApplyVo);
        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2").record("采购申请-采购单送审").build());

        return ResultInfo.success();
    }

    /**
     * 编辑并送审
     */
    @RequestMapping(value = "toEditAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toEditAndSub(@RequestBody PurchaseApplyVo purchaseApplyVo) {
        purchaseApplyService.toEditApply(purchaseApplyVo);
        // 日志
        operationRecordService.addOperationRecord(OperationRecord.builder()
                .field_fk(purchaseApplyVo.getPurchaseApply().getPurchase_no())
                .type("2").record("采购申请-编辑采购单并送审").build());

        return ResultInfo.success();
    }

    /**
     * 送审撤回
     *
     * @return
     */
    @RequestMapping(value = "toRevokeApply.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toRevokeApply(@RequestBody PurchaseApplyVo purchaseApplyVo) {
        try {
            purchaseApplyService.toRevokeApply(purchaseApplyVo);
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
    public ResultInfo toEditPurchaseDetails(@RequestBody PurchaseApplyVo purchaseApplyVo) {
        purchaseApplyService.toEditPurchaseDetails(purchaseApplyVo);
        return ResultInfo.success();
    }


    /**
     * 生成入库单
     *
     * @return
     */
    @RequestMapping(value = "toWarehousing.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toWarehousing(PurchaseApply purchaseApply) {
        purchaseApplyService.toWarehousing(purchaseApply);

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
        return "shenpimana/caigousp/index";
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
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, PurchaseApplyVo bean) {
        // 根据流程进行业务id筛选
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUserService.getUser(), G.PROCESS_ZCCG);
        if (CollectionUtils.isEmpty(busIds)) {
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        bean.setIs_adopt(1);
        PageInfo<PurchaseApply> pageInfo = purchaseApplyService.toList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 采购已审批数据
     */
    @RequestMapping(value = "finishAdopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo finishAdopt(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, PurchaseApplyVo bean) {
        // 根据流程进行业务id筛选
        List<Integer> busIds = processMemberService.getBusIdsByUserIdHistory(sysUserService.getUser(), G.PROCESS_ZCCG);
        if (CollectionUtils.isEmpty(busIds)) {
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);

        PageInfo<PurchaseApply> pageInfo = purchaseApplyService.finishAdopt(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 修改采购详情
     */
    @RequestMapping(value = "updatePurchaseDetails.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updatePurchaseDetails(PurchaseDetail purchaseDetail) {
        int count = purchaseApplyService.updatePurchaseDetails(purchaseDetail);
        if (count > 0) {
            return ResultInfo.success("操作成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    @Autowired
    private PurchaseDetailMapper purchaseDetailMapper;

    /**
     * 修改采购详情
     */
    @RequestMapping(value = "updateNumPurchaseDetails.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateNumPurchaseDetails(PurchaseDetail purchaseDetail) {
        PurchaseDetail a = purchaseDetailMapper.selectById(purchaseDetail.getId());
        if (null == a) {
            return ResultInfo.error("详情错误");
        }
        String accept_num = a.getAccept_num();
        if (StringUtils.isBlank(accept_num)) {
            return ResultInfo.error("验收数量错误");
        }
        // 判断accept_num是否为数字
        if (!NumberUtils.isNumber(accept_num)) {
            return ResultInfo.error("验收数量错误");
        }
        // 判断数量是否大于验收数量
        if (purchaseDetail.getInbound_num() > Integer.parseInt(accept_num)) {
            return ResultInfo.error("入库数量不能大于验收数量");
        }

        int count = purchaseApplyService.updatePurchaseDetails(purchaseDetail);
        if (count > 0) {
            return ResultInfo.success("操作成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 跳转到采购处理页面
     */
    @RequestMapping(value = "toPurchaseHander.do")
    public String toPurchaseHander() {
        return "/purchase/capital/cgcl_index";
    }

    /**
     * 采购申请列表展示
     *
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/cgcl/toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo tocgclList(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer limit, PurchaseApplyVo purchaseApplyVo) {
        PageInfo<PurchaseApply> pageInfo = purchaseApplyService.tocgclList(page, limit, purchaseApplyVo);
        return ResultInfo.success(pageInfo);
    }
}
