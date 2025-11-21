package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.enums.ProcessEnums;
import com.jsdc.rfid.mapper.ProcessMemberNodeMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.utils.CommonDataTools;
import lombok.NonNull;
import net.hasor.utils.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.util.*;

/**
 * 流程控制器
 */
@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessConfigService processConfigService;

    @Autowired
    private ProcessConfigInfoService processConfigInfoService;

    @Autowired
    private SysPostService postService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private ProcessMemberHistoryService processMemberHistoryService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private AssetsTypeService assetsTypeService;

    @Autowired
    private ConsCategoryService consCategoryService;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private ConsReceiveService consReceiveService;

    @Autowired
    private PurchaseApplyService purchaseApplyService;

    @Autowired
    private ChangeInfoService changeInfoService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private CarryManageService carryManageService;

    @Autowired
    private ConsPurchaseApplyService consPurchaseApplyService;

    @Autowired
    private CommonDataTools commonDataTools;

    @Autowired
    private ConsumableService consumableService;


    /**
     * 跳转列表页
     *
     * @return
     */
    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "process/index";
    }

    @RequestMapping("edit.do")
    public String toAdd() {
        return "process/edit";
    }

    /**
     * 首页跳转列表
     */
    @RequestMapping("homeList.do")
    public String homeList() {
        return "event/eventTable";
    }

    @RequestMapping("toEdit.do")
    public String edit(@NonNull Integer id, Model model){
        ProcessConfig dict = processConfigService.selectById(id);
        model.addAttribute("obj", dict);
        return "process/edit";
    }

    /**
     * 列表查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             ProcessConfig beanParam) {
        PageInfo<ProcessConfig> page = processConfigService.toList(pageIndex, pageSize, beanParam);
        return ResultInfo.success(page);
    }

    /**
     *  添加
     */
    @RequestMapping(value = "toAdd.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo add(ProcessConfig bean){
        return processConfigService.save(bean);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "remove.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo delete(@RequestParam("id") Integer id){
        return processConfigService.remove(id);
    }


    // ------------------ 节点相关信息 ------------------

    /**
     * 跳转节点列表页
     *
     * @return
     */
    @RequestMapping("toNodeIndex.do")
    public String toNodeIndex(@RequestParam(required = false) Integer id, Model model) {
        if(null != id){
            model.addAttribute("processId", id);
        }
        return "process/node/index";
    }
    /**
     * 跳转新增节点页
     *
     */
    @RequestMapping("toNodeAdd.do")
    public String toNodeAdd(@NonNull Integer process_config_id, @RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("process_config_id", process_config_id);
        // 得到所有岗位
        List<SysPost> posts = postService.selectList(Wrappers.<SysPost>lambdaQuery().eq(SysPost::getIs_del, G.ISDEL_NO));
        model.addAttribute("posts", posts);
        // 得到所有用户
        List<SysUser> users = userService.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getIs_del, G.ISDEL_NO));
        model.addAttribute("users", users);
        // 得到对象
        if(null != id){
            ProcessConfigInfo info = processConfigInfoService.selectById(id);
            if (info.getParent_node().split(",").length > 1){
                for (String s : info.getParent_node().split(",")) {
                    info.getParent_node_list().add(s);
                }
            }else{
                info.setParent_node_list(Arrays.asList(info.getParent_node()));
            }
            model.addAttribute("obj", info);
            // 得到条件
            if (StringUtils.isNotBlank(info.getProcess_condition())) {
                Map<String, Object> map = new HashMap<>();
                String[] a = info.getProcess_condition().split(",");
                if (a.length == 2){
                    map.put("id", a[0]);
                    map.put("name", a[1]);
                    model.addAttribute("conditions", map);
                }
            }
        }
        // 得到所有节点
        List<ProcessConfigInfo> infos = processConfigInfoService.selectList(Wrappers.<ProcessConfigInfo>lambdaQuery()
                .eq(ProcessConfigInfo::getProcess_config_id, process_config_id)
                .eq(ProcessConfigInfo::getIs_del, G.ISDEL_NO)
                .orderByAsc(ProcessConfigInfo::getNode_order)
        );
        model.addAttribute("infos", infos);
        // 得到枚举 ProcessEnums 的type 为key,name 为value
        List<Map<String,String>> maps = new ArrayList<>();
        for (ProcessEnums e : ProcessEnums.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", e.getCode());
            map.put("name", e.getName());
            maps.add(map);
        }
        model.addAttribute("types", maps);
        // 得到资产品类
        List<AssetsType> assetsTypes = assetsTypeService.selectList(Wrappers.<AssetsType>lambdaQuery().eq(AssetsType::getIs_del, G.ISDEL_NO));
        model.addAttribute("assetsTypes", assetsTypes);
        // 得到耗材品类
        List<ConsCategory> consumablesTypes = consCategoryService.selectList(Wrappers.<ConsCategory>lambdaQuery().eq(ConsCategory::getIs_del, G.ISDEL_NO));
        model.addAttribute("consumablesTypes", consumablesTypes);

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
        return "process/node/edit";
    }

    /**
     * 列表查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "toInfoList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toInfoList(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             ProcessConfigInfo beanParam) {
        PageInfo<ProcessConfigInfo> page = processConfigInfoService.toList(pageIndex, pageSize, beanParam);
        return ResultInfo.success(page);
    }

    /**
     *  添加节点信息
     */
    @RequestMapping(value = "toInfoAdd.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toInfoAdd(ProcessConfigInfo bean){
        return processConfigInfoService.save(bean);
    }

    /**
     * 删除节点信息
     */
    @RequestMapping(value = "removeInfo.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo removeInfo(@RequestParam("id") Integer id){
        return processConfigInfoService.remove(id);
    }

    // ------------------ 历史流程 ------------------
    /**
     * 跳转节点列表页
     *
     * @return
     */
    @RequestMapping("toHistoryIndex.do")
    public String toHistoryIndex(@RequestParam(required = false) Integer bus_id, @RequestParam(required = false) String keyName, Model model) {
        if(null != bus_id && StringUtils.isNotBlank(keyName)){
            // 查询当前流程 查询字典表
            SysDict dictionary = sysDictService.getProcessByCode(keyName);
            model.addAttribute("key", dictionary.getValue());
            model.addAttribute("bus_id", bus_id);
        }
        return "process/historyList";
    }
    /**
     * 历史流程列表查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "toHistoryList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toHistoryList(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                                 @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                                 ProcessMemberHistory beanParam) {
        PageInfo<ProcessMemberHistory> page = processMemberHistoryService.getListByKeyAndBusId(pageIndex, pageSize, beanParam);
        return ResultInfo.success(page);
    }

    // ------------------ 流程运行 ------------------
    @Autowired
    private ProcessMemberNodeMapper nodeMapper;

    /**
     *  审批意见弹窗
     */
    @RequestMapping("examineEdit.do")
    public String examineEdit(String type, Model model) {
        model.addAttribute("type", type);
        return "shenpimana/examineEdit";
    }

    /**
     * 跳转流程运行列表页
     *
     * @return
     */
    @RequestMapping("forecast.do")
    public String toRunIndex(String keyName, Model model) {
        if(StringUtils.isNotBlank(keyName)){
            // 根据字典查询流程
            SysDict dictionary = sysDictService.getProcessByCode(keyName);
            // 根据流程key查询流程id
            ProcessConfig processConfig = processConfigService.selectOne(Wrappers.<ProcessConfig>lambdaQuery()
                    .eq(ProcessConfig::getProcess_code, dictionary.getValue())
                    .eq(ProcessConfig::getIs_del, G.ISDEL_NO));
            model.addAttribute("processId", processConfig.getId());
        }
        return "process/processNodeView";
    }
    /**
     * 根据登录人查询所有的待办任务
     */
    @RequestMapping(value = "toTaskList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toTaskList(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                                 @RequestParam(defaultValue = "10", value = "limit") Integer pageSize) {
        // 根据流程进行业务id筛选
        List<ProcessConfig> list = processConfigService.selectList(Wrappers.<ProcessConfig>lambdaQuery().eq(ProcessConfig::getIs_del, G.ISDEL_NO));
        // 得到所有的流程拼接的集合 存3个字段 key, bus_id, create_time
        List<Map<String, Object>> newList = new ArrayList<>();
        // 用户map
        Map<Integer, SysUser> userMap = commonDataTools.getUserAllMap();
        // 部门map
        Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();
        // 计算下列代码所用时间
        long startTime = System.currentTimeMillis();
        for (ProcessConfig config : list) {
            String codeName = getNameByCode(config.getProcess_code());
            if (StringUtils.isBlank(codeName)){
                continue;
            }
            // 根据流程进行业务id筛选
            List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUserService.getUser(), codeName);
            if (CollectionUtils.isEmpty(busIds)){
                continue;
            }
            getCountByCodeAndIds(config.getProcess_code(),busIds, newList, userMap, deptMap);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime));

        // 存放到redis
//        ReadsU.set("taskList", newList);

        // 根据时间排序
        newList.sort((o1, o2) -> {
            Date date1 = o1.get("create_time") == null ? new Date() : (Date) o1.get("create_time");
            Date date2 = o2.get("create_time") == null ? new Date() : (Date) o2.get("create_time");
            return date2.compareTo(date1);
        });
        // 分页
        int start = (pageIndex - 1) * pageSize;
        int end = pageIndex * pageSize;
        if (end > newList.size()){
            end = newList.size();
        }
        List<Map<String, Object>> subList = newList.subList(start, end);
        // 格式化时间
        subList.forEach(map1 -> {
            Date date = (Date) map1.get("create_time");
            if (null == date){
                date = new Date();
            }
            map1.put("create_time", DateUtils.formatDate(date, "yyyy-MM-dd"));
        });
        ResultInfo resultInfo = ResultInfo.success(subList);
        resultInfo.setCount((long) newList.size());
        return resultInfo;
    }

    /**  7个流程业务service  */


    private void getCountByCodeAndIds(String code, List<Integer> ids, List<Map<String, Object>> newList, Map<Integer, SysUser> userMap, Map<Integer, SysDepartment> deptMap) {
        switch (code){
            case "rfid-zcsl":
                receiveService.selectList(Wrappers.<Receive>lambdaQuery().in(Receive::getId,ids).eq(Receive::getIs_del,G.ISDEL_NO)).forEach(receive -> {
                    // 申请人
                    SysUser user = userMap.get(receive.getUse_id());
                    // 申请人部门
                    SysDepartment sysDepartment = null == user ? null : deptMap.get(user.getDepartment() == null ? 0 : user.getDepartment());
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", code);
                    map.put("keyName", "资产申领");
                    map.put("bus_id", receive.getId());
                    map.put("create_time", receive.getCreate_time());
                    map.put("apply_user", receive.getUse_id() == null ? "" : user.getUser_name());
                    map.put("link", "/receive/toViewIndex.do?id="+receive.getId()+"&type=2");
                    map.put("no_code", receive.getReceive_code());
                    // 申请人部门
                    map.put("apply_dept", sysDepartment == null ? "" : sysDepartment.getDept_name());
                    newList.add(map);
                });
                return;
            case "rfid-hcsl":
                consReceiveService.selectList(Wrappers.<ConsReceive>lambdaQuery().in(ConsReceive::getId,ids).eq(ConsReceive::getIs_del,G.ISDEL_NO)).forEach(consReceive -> {
                    // 申请人
                    SysUser user = userMap.get(consReceive.getUse_id());
                    // 申请人部门
                    SysDepartment sysDepartment = null == user ? null : deptMap.get(user.getDepartment() == null ? 0 : user.getDepartment());
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", code);
                    map.put("keyName", "耗材申领");
                    map.put("bus_id", consReceive.getId());
                    map.put("create_time", consReceive.getCreate_time());
                    map.put("apply_user", consReceive.getUse_id() == null ? "" : user.getUser_name());
                    map.put("link", "/consReceive/toViewIndex.do?id=" + consReceive.getId() + "&type=2&pageType=0");
                    map.put("no_code", consReceive.getReceive_code());
                    map.put("apply_dept", sysDepartment == null ? "" : sysDepartment.getDept_name());
                    newList.add(map);
                });
                return;
            case "rfid-zccg":
                purchaseApplyService.selectList(Wrappers.<PurchaseApply>lambdaQuery().in(PurchaseApply::getId,ids).eq(PurchaseApply::getIs_del,G.ISDEL_NO)).forEach(purchaseApply -> {
                    // 申请人
                    SysUser user = userMap.get(purchaseApply.getApply_user());
                    // 申请人部门
                    SysDepartment sysDepartment = null == user ? null : deptMap.get(user.getDepartment() == null ? 0 : user.getDepartment());
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", code);
                    map.put("keyName", "资产采购");
                    map.put("bus_id", purchaseApply.getId());
                    map.put("create_time", purchaseApply.getCreate_time());
                    map.put("apply_user", purchaseApply.getApply_user() == null ? "" : user.getUser_name());
                    map.put("link","/purchaseApply/toViewPage.do?id=" + purchaseApply.getId() + "&type=2");
                    map.put("no_code", purchaseApply.getPurchase_no());
                    map.put("apply_dept", sysDepartment == null ? "" : sysDepartment.getDept_name());
                    newList.add(map);
                });
                return;
            case "rfid-zcbg":
                changeInfoService.selectList(Wrappers.<ChangeInfo>lambdaQuery().in(ChangeInfo::getId,ids).eq(ChangeInfo::getIs_del,G.ISDEL_NO)).forEach(changeInfo -> {
                    // 申请人
                    SysUser user = userMap.get(changeInfo.getApply_user());
                    // 申请人部门
                    SysDepartment sysDepartment = null == user ? null : deptMap.get(user.getDepartment() == null ? 0 : user.getDepartment());
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", code);
                    map.put("keyName", "资产变更");
                    map.put("bus_id", changeInfo.getId());
                    map.put("create_time", changeInfo.getCreate_time());
                    map.put("apply_user", changeInfo.getApply_user() == null ? "" : user.getUser_name());
                    map.put("link", "/changeInfo/toViewIndex.do?id=" + changeInfo.getId() + "&type=2");
                    map.put("no_code", changeInfo.getChange_code());
                    map.put("apply_dept", sysDepartment == null ? "" : sysDepartment.getDept_name());
                    newList.add(map);
                });
                return;
            case "rfid-zccz":
                managementService.selectList(Wrappers.<Management>lambdaQuery().in(Management::getId,ids).eq(Management::getIs_del,G.ISDEL_NO)).forEach(management -> {
                    // 申请人
                    SysUser user = userMap.get(management.getApply_user());
                    // 申请人部门
                    SysDepartment sysDepartment = null == user ? null : deptMap.get(user.getDepartment() == null ? 0 : user.getDepartment());
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", code);
                    map.put("keyName", "资产处置");
                    map.put("bus_id", management.getId());
                    map.put("create_time", management.getCreate_time());
                    map.put("apply_user", management.getApply_user() == null ? "" : user.getUser_name());
                    map.put("link", "/management/toViewIndex.do?id=" + management.getId() + "&type=2");
                    map.put("no_code", management.getManagement_code());
                    map.put("apply_dept", sysDepartment == null ? "" : sysDepartment.getDept_name());
                    newList.add(map);
                });
                return;
            case "rfid-zcwx":
                carryManageService.selectList(Wrappers.<CarryManage>lambdaQuery().in(CarryManage::getId,ids).eq(CarryManage::getIs_del,G.ISDEL_NO)).forEach(carryManage -> {
                    // 申请人
                    SysUser user = userMap.get(carryManage.getCarry_id());
                    // 申请人部门
                    SysDepartment sysDepartment = null == user ? null : deptMap.get(user.getDepartment() == null ? 0 : user.getDepartment());
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", code);
                    map.put("keyName", "资产外携");
                    map.put("bus_id", carryManage.getId());
                    map.put("create_time", carryManage.getCreation_time());
                    map.put("apply_user", carryManage.getCarry_id() == null ? "" : user.getUser_name());
                    map.put("link", "/carrymanage/selectByDetails.do?id=" + carryManage.getId() + "&type=0");
                    map.put("no_code", carryManage.getNumbering());
                    map.put("apply_dept", sysDepartment == null ? "" : sysDepartment.getDept_name());
                    newList.add(map);
                });
                return;
            case "rfid-hccg":
                consPurchaseApplyService.selectList(Wrappers.<ConsPurchaseApply>lambdaQuery().in(ConsPurchaseApply::getId,ids).eq(ConsPurchaseApply::getIs_del,G.ISDEL_NO)).forEach(consPurchaseApply -> {
                    // 申请人
                    SysUser user = userMap.get(consPurchaseApply.getApply_user());
                    // 申请人部门
                    SysDepartment sysDepartment = null == user ? null : deptMap.get(user.getDepartment() == null ? 0 : user.getDepartment());
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", code);
                    map.put("keyName", "耗材采购");
                    map.put("bus_id", consPurchaseApply.getId());
                    map.put("create_time", consPurchaseApply.getCreate_time());
                    map.put("apply_user", consPurchaseApply.getApply_user() == null ? "" : user.getUser_name());
                    map.put("link", "/consPurchaseApply/toViewPage.do?id=" + consPurchaseApply.getId() + "&type=2");
                    map.put("no_code", consPurchaseApply.getPurchase_no());
                    map.put("apply_dept", sysDepartment == null ? "" : sysDepartment.getDept_name());
                    newList.add(map);
                });
                return;
            default:
                return;
        }
    }

    private String getNameByCode(String code){
        switch (code){
            case "rfid-zcsl":
                return G.PROCESS_ZCSL;
            case "rfid-hcsl":
                return G.PROCESS_HCSL;
            case "rfid-zccg":
                return G.PROCESS_ZCCG;
            case "rfid-zcbg":
                return G.PROCESS_ZCBG;
            case "rfid-zccz":
                return G.PROCESS_ZCCZ;
            case "rfid-zcwx":
                return G.PROCESS_ZCWX;
            case "rfid-hccg":
                return G.PROCESS_HCCG;
            default:
                return null;
        }
    }
}
