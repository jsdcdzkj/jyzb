package com.jsdc.rfid.service.statisticalreport;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.common.utils.DateTimeUtils;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.vo.ApplySingleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import vo.ConsPurchaseApplyVo;
import vo.PurchaseApplyVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lb
 * @Date 2023/5/17 14:44
 * @Description RFID优化 小程序和页面优化service 如果复用StatisticalReportService容易循环引用
 **/

@Service
@Transactional
public class StatisticalService {
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private ProcessMemberService processMemberService;
    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private ApplySingleService applySingleService;
    @Autowired
    private CarryManageService carryManageService;

    @Autowired
    private ConsReceiveService consReceiveService;

    @Autowired
    private PurchaseApplyService purchaseApplyService;


    @Autowired
    private ChangeInfoService changeInfoService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ConsPurchaseApplyService consPurchaseApplyService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RepairApplyService repairApplyService;


    public PageInfo<AssetsManage> getMyAssetsNum(SysUser currentUser) {
        //我的固定资产数
        AssetsManage beanParam = new AssetsManage();
        beanParam.setUse_people(currentUser.getId());
        return assetsManageService.toList(1, 5, beanParam);
    }

    public PageInfo<Receive> getMyAssetsApplayNum(SysUser currentUser) {
//        固定资产申请数
        Receive bean = new Receive();
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(currentUser, G.PROCESS_ZCSL);
        PageInfo<Receive> receivePageInfo = new PageInfo<>();
        if (CollectionUtils.isEmpty(busIds)) {
            return  new PageInfo<>();
        }else {
            bean.setIds(busIds);
            bean.setIs_adopt(1);
            bean.setUserId(currentUser.getId());
            receivePageInfo = receiveService.getListByPage(bean, 1, 5);
            return receivePageInfo;
        }

    }
    public Integer getMyAssetsApplayNumCount(){
        SysUser user = sysUserService.getUser();
        //领用申请数
        QueryWrapper<Receive> qwReceive = new QueryWrapper<>();
        qwReceive.eq("use_id", user.getId());
        qwReceive.eq("is_del", "0");
        Integer receiveCount = receiveService.selectCount(qwReceive);
        //变更申请数
        QueryWrapper<ChangeInfo> qwChangeInfo = new QueryWrapper<>();
        qwChangeInfo.eq("apply_user", user.getId());
        qwChangeInfo.eq("is_del", "0");
        Integer changeInfoCount = changeInfoService.selectCount(qwChangeInfo);
        //处置申请数
        QueryWrapper<Management> qwManagement = new QueryWrapper<>();
        qwManagement.eq("apply_user", user.getId());
        qwManagement.eq("is_del", "0");
        Integer managementCount = managementService.selectCount(qwManagement);
        return receiveCount + changeInfoCount + managementCount;
    }

    public PageInfo<ApplySingleVo> getApplySingleNum(SysUser currentUser) {
        //报修申请数
        ApplySingleVo applySingleVo = new ApplySingleVo();
        applySingleVo.setSqr_id(currentUser.getId());
        PageInfo<ApplySingleVo> applySinglePageInfo = applySingleService.selectPageList(1, 5, applySingleVo);
        return applySinglePageInfo;
    }

    public PageInfo<RepairApply> getApplySingleNumCount(SysUser currentUser) {
//        //报修申请数
//
//        QueryWrapper<ApplySingle> qw = new QueryWrapper<>();
//        qw.eq("sqr_id", currentUser.getId());
//        qw.eq("is_del", "0");
//        return applySingleService.selectCount(qw);

        //        固定资产申请数
        RepairApply bean = new RepairApply();
//        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(currentUser, G.PROCESS_WXZC);
//        PageInfo<RepairApply> receivePageInfo = new PageInfo<>();
//        if (CollectionUtils.isEmpty(busIds)) {
//            return  new PageInfo<>();
//        }else {
//            bean.setIds(busIds);
//            bean.setUserId(currentUser.getId());
//            receivePageInfo = repairApplyService.getListByPage(bean, 1, 10, currentUser);
//            return receivePageInfo;
//        }
        bean.setStatus("-1");
        bean.setRepair_user_name(currentUser.getUser_name());
        return repairApplyService.getListByPage(bean, 1, 10, currentUser);

    }

    public PageInfo getCarryManageNum(SysUser currentUser) {
        //外携申请数
        CarryManage carryManage = new CarryManage();
        carryManage.setCarry_id(currentUser.getId());
        carryManage.setWx_userId(currentUser.getId());
        PageInfo carryManagePageInfo = carryManageService.selectPageList(1, 5, carryManage);
        return carryManagePageInfo;
    }

    public PageInfo<ConsReceive> getConsReceiveNum(SysUser currentUser) {
        //耗材申领数
        PageInfo<ConsReceive> consReceivePageInfo = consReceiveService.getListByPage(new ConsReceive(), 1, 5);
        return consReceivePageInfo;
    }

    public PageInfo<ConsReceive> getConsReceiveNumWX(SysUser currentUser) {
        //耗材申领数
        PageInfo<ConsReceive> consReceivePageInfo = consReceiveService.getListByPage(new ConsReceive(), 1, 5, currentUser);
        return consReceivePageInfo;
    }

    /**
     * 跳转到 员工工作台页面默认数据
     * @param model
     */
    public JSONObject toNormalPage(Model model, SysUser currentUser) {

        JSONObject jsonObject = new JSONObject();
        //我的固定资产数
        PageInfo<AssetsManage> assetsManagePageList =getMyAssetsNum(currentUser);
        jsonObject.put("myAssetsNum", assetsManagePageList.getTotal());

        //固定资产申请数
        PageInfo<Receive> receivePageInfo = getMyAssetsApplayNum(currentUser);
        jsonObject.put("myAssetsApplayNum", receivePageInfo.getTotal());

        //报修申请数
        PageInfo<ApplySingleVo> applySinglePageInfo = getApplySingleNum(currentUser);
        jsonObject.put("applySingleNum", applySinglePageInfo.getTotal());

        //外携申请数
        PageInfo carryManagePageInfo = getCarryManageNum(currentUser);
        jsonObject.put("carryManageNum", carryManagePageInfo.getTotal());

        //耗材申领数
        PageInfo<ConsReceive> consReceivePageInfo = getConsReceiveNum(currentUser);
        jsonObject.put("consReceiveNum", consReceivePageInfo.getTotal());

        if(null != model){
            model.addAttribute("myAssetsNum", assetsManagePageList.getTotal());
            model.addAttribute("myAssetsApplayNum", receivePageInfo.getTotal());
            model.addAttribute("applySingleNum", applySinglePageInfo.getTotal());
            model.addAttribute("carryManageNum", carryManagePageInfo.getTotal());
            model.addAttribute("consReceiveNum", consReceivePageInfo.getTotal());
        }

        return  jsonObject;
    }




    public PageInfo<PurchaseApply> getCgdspNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        //采购待审批数
        PurchaseApplyVo purchaseApplyVo = new PurchaseApplyVo();
        purchaseApplyVo.setApprove_status("2");
        List<Integer> purchaseApplyVousIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCCG);
        if(CollectionUtils.isEmpty(purchaseApplyVousIds)){
            return new PageInfo<>();
        }
        purchaseApplyVo.setIds(purchaseApplyVousIds);
        purchaseApplyVo.setIs_adopt(1);
        PageInfo<PurchaseApply> purchaseApplyPageInfo = purchaseApplyService.toList(pageIndex, pageSize, purchaseApplyVo);
        return purchaseApplyPageInfo;
    }


    public  PageInfo<Receive> getLydspNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        //领用待审批数
        Receive receive = new Receive();
        receive.setStatus("2");
        List<Integer> receiveBusIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCSL);
        if(CollectionUtils.isEmpty(receiveBusIds)){
            return new PageInfo<>();
        }
        receive.setIds(receiveBusIds);
        receive.setIs_adopt(1);
        PageInfo<Receive> receivePageInfo = receiveService.getListByPage(receive, pageIndex, pageSize);
        return receivePageInfo;
    }


    public PageInfo<ChangeInfo> getBgdspNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        // 变更待审批数
        ChangeInfo changeInfo = new ChangeInfo();
        changeInfo.setApprove_status("2");
        // 根据流程进行业务id筛选
        List<Integer> changeInfoBusIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCBG);
        if(CollectionUtils.isEmpty(changeInfoBusIds)){
            return new PageInfo<>();
        }
        changeInfo.setIds(changeInfoBusIds);
        PageInfo<ChangeInfo> changeInfoPageInfo = changeInfoService.getChangeListByPage(changeInfo, pageIndex, pageSize);
        return changeInfoPageInfo;
    }


    public PageInfo<Management> getCzdspNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        // 处置待审批数
        Management management = new Management();
        management.setStatus("2");
        List<Integer> managementBusIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCCZ);

        if(CollectionUtils.isEmpty(managementBusIds)){
            return new PageInfo<>();
        }
        management.setIds(managementBusIds);
        PageInfo<Management> managementPageInfo = managementService.getPageByUserId(management, pageIndex, pageSize);
        return managementPageInfo;
    }
  public PageInfo getWxdspNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
      // 外携待审批数；
      CarryManage carryManage = new CarryManage();
      carryManage.setApproval_state("0");
      List<Integer> carryManageBusIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCWX);
      if (CollectionUtils.isEmpty(carryManageBusIds)) {
         return new PageInfo<>();
      }
      carryManage.setIds(carryManageBusIds);
      carryManage.setIs_adopt(1);
      PageInfo carryManagePageInfo = carryManageService.selectPageList(pageIndex, pageSize, carryManage);
      return carryManagePageInfo;
    }

    public PageInfo getWxzcdspNum(SysUser sysUser){
        int pageIndex=1, pageSize=5;
        RepairApply bean = new RepairApply();
        // 维修待审批数
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_WXZC);
        if(CollectionUtils.isEmpty(busIds)){
            return new PageInfo<>(new ArrayList<>());
        }
        bean.setIds(busIds);
        bean.setIs_adopt(1);
        bean.setStatus("2");
        PageInfo<RepairApply> pageInfo = repairApplyService.getListByPage(bean, pageIndex, pageSize, sysUser);
        return pageInfo;
    }


    /**
     *  //固定资产待办事项
     * @param model
     * @param sysUser
     */
    public void toNormalTodo(Model model, SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        //采购待审批数
        PageInfo<PurchaseApply> purchaseApplyPageInfo = getCgdspNum(sysUser);
        model.addAttribute("cgdspNum", purchaseApplyPageInfo.getTotal());


        //领用待审批数
        PageInfo<Receive> receivePageInfo =getLydspNum(sysUser);
        model.addAttribute("lydspNum", receivePageInfo.getTotal());


        // 变更待审批数
        PageInfo<ChangeInfo> changeInfoPageInfo = getBgdspNum(sysUser);
        model.addAttribute("bgdspNum", changeInfoPageInfo.getTotal());


        // 处置待审批数
        PageInfo<Management> managementPageInfo =getCzdspNum(sysUser);
        model.addAttribute("czdspNum", managementPageInfo.getTotal());

        // 外携待审批数；
        PageInfo carryManagePageInfo = getWxdspNum(sysUser);
        model.addAttribute("wxdspNum", carryManagePageInfo.getTotal());
    }



    public PageInfo getHaoCaicgdspNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        //采购待审批数
        ConsPurchaseApplyVo consPurchaseApplyVo = new ConsPurchaseApplyVo();
        consPurchaseApplyVo.setApprove_status("2");
        List<Integer> consPurchaseApplyBusIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_HCCG);
        if (CollectionUtils.isEmpty(consPurchaseApplyBusIds)) {
            return new PageInfo<>();
        }
        consPurchaseApplyVo.setIds(consPurchaseApplyBusIds);
        consPurchaseApplyVo.setIs_adopt(1);
        PageInfo<ConsPurchaseApply> pageInfo = consPurchaseApplyService.toList(pageIndex, pageSize, consPurchaseApplyVo);
        return pageInfo;
    }



    public PageInfo getHaoCailydspNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        ConsReceive consReceive = new ConsReceive();
        consReceive.setStatus("2");
        List<Integer> consReceiveBusIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_HCSL);
        if(CollectionUtils.isEmpty(consReceiveBusIds)){
            return new PageInfo<>();
        }
        consReceive.setIds(consReceiveBusIds);
        consReceive.setIs_adopt(1);
        PageInfo<ConsReceive> consReceivePageInfo = consReceiveService.getListByPage(consReceive, pageIndex, pageSize);
        return consReceivePageInfo;
    }

    /**
     * 耗材待办事项
     * @param model
     * @param sysUser
     */
    public void toNormalHaoCaiTodo(Model model, SysUser sysUser) {
        int pageIndex=1, pageSize=5;

        //采购待审批数
        PageInfo<ConsPurchaseApply> consPurchaseApplyPageInfo = getHaoCaicgdspNum(sysUser);
        model.addAttribute("haoCaicgdspNum", consPurchaseApplyPageInfo.getTotal());

        //领用待审批数
        PageInfo<ConsReceive> consReceivePageInfo =getHaoCailydspNum(sysUser);
        model.addAttribute("haoCailydspNum",consReceivePageInfo.getTotal());
    }


    /**
     *  报修本月总数
     * @param sysUser
     * @return
     */
    public PageInfo getApplySingleVoMonthNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        //报修本月总数
        ApplySingleVo applySingleVoMonth = new ApplySingleVo();
        applySingleVoMonth.setEnd(DateTimeUtils.getMonthLastTimeStr());
        applySingleVoMonth.setStart(DateTimeUtils.getMonthStartTimeStr());
        PageInfo applySingleVoMonthPageInfo = applySingleService.getRepairMonthList(pageIndex, pageSize, applySingleVoMonth);
        return applySingleVoMonthPageInfo;
    }


    /**
     *  我的任务数
     * @param sysUser
     * @return
     */
    public PageInfo getApplySingleVoNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        //我的任务数
        ApplySingleVo applySingleVo = new ApplySingleVo();
        applySingleVo.setRepair_id(sysUser.getId());
        PageInfo applySingleVoPageInfo = applySingleService.getRepairList(pageIndex, pageSize, applySingleVo);
        return applySingleVoPageInfo;
    }

    /**
     *  外携申请
     * @param sysUser
     * @return
     */
    public PageInfo getYwwxsqNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        //外携申请
        CarryManage carryManage = new CarryManage();
        carryManage.setCarry_id(sysUser.getId());
        PageInfo carryManagePageInfo = carryManageService.selectPageList(pageIndex, pageSize, carryManage);
        return carryManagePageInfo;
    }

    /**
     * 耗材申领
     * @param sysUser
     * @return
     */
    public PageInfo getYwhcslNum(SysUser sysUser) {
        int pageIndex=1, pageSize=5;
        //耗材申领
        ConsReceive consReceive = new ConsReceive();
        PageInfo<ConsReceive> consReceiveageInfo = consReceiveService.getListByPage(consReceive, 1, 5);
        return consReceiveageInfo;
    }
    public void toOperationPage(Model model, SysUser sysUser) {

        JSONObject jsonObject = new JSONObject();

        //我的固定资产数
        AssetsManage beanParam = new AssetsManage();
        beanParam.setUse_people(sysUser.getId());
        PageInfo<AssetsManage> assetsManagePageList = assetsManageService.toList(1, 5, beanParam);

        jsonObject.put("myAssetsNum", assetsManagePageList.getTotal());
        model.addAttribute("myAssetsNum", assetsManagePageList.getTotal());

        //固定资产申请数
        Receive bean = new Receive();
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCSL);
        PageInfo<Receive> receivePageInfo = new PageInfo<>();
        if (CollectionUtils.isEmpty(busIds)) {
            jsonObject.put("myAssetsApplayNum", 0);
            model.addAttribute("myAssetsApplayNum", 0);
        } else {
            bean.setIds(busIds);
            bean.setIs_adopt(1);
            receivePageInfo = receiveService.getListByPage(bean, 1, 5);
            jsonObject.put("myAssetsApplayNum", receivePageInfo.getTotal());
            model.addAttribute("myAssetsApplayNum", receivePageInfo.getTotal());
        }

        //报修本月总数
        PageInfo applySingleVoMonthPageInfo = getApplySingleVoMonthNum(sysUser);
        model.addAttribute("applySingleVoMonthNum", receivePageInfo.getTotal());

        //我的任务数
        PageInfo applySingleVoPageInfo = getApplySingleVoNum(sysUser);
        model.addAttribute("applySingleVoNum", receivePageInfo.getTotal());

        //外携申请
        PageInfo carryManagePageInfo = getYwwxsqNum(sysUser);
        model.addAttribute("ywwxsqNum", carryManagePageInfo.getTotal());

        //耗材申领
        PageInfo<ConsReceive> consReceiveageInfo = getYwhcslNum(sysUser);
        model.addAttribute("ywhcslNum", consReceiveageInfo.getTotal());
    }

}
