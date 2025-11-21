package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ProcessMemberDao;
import com.jsdc.rfid.enums.ProcessEnums;
import com.jsdc.rfid.mapper.ProcessConfigInfoMapper;
import com.jsdc.rfid.mapper.ProcessConfigMapper;
import com.jsdc.rfid.mapper.ProcessMemberNodeMapper;
import com.jsdc.rfid.mapper.SysPostMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.AliSmsUtil;
import lombok.Synchronized;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessMemberService extends BaseService<ProcessMemberDao, ProcessMember> {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private ProcessConfigInfoMapper processConfigInfoMapper;

    @Autowired
    private ProcessConfigMapper processConfigMapper;

    @Autowired
    private ProcessConfigService processConfigService;

    @Autowired
    private ProcessMemberNodeMapper nodeMapper;

    @Autowired
    private ProcessMemberHistoryService historyService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private AliSmsUtil aliSmsUtil;

    /**
     * 根据流程id获取流程配置信息
     *
     * @param process_config_id
     * @return
     */
    public List<ProcessMember> toList(Integer process_config_id) {
        return selectList(Wrappers.<ProcessMember>lambdaQuery()
                .eq(ProcessMember::getIs_del, G.ISDEL_NO)
        );
    }

    /**
     * 根据流程id删除流程配置信息
     *
     * @param id
     */
    public void removeInfoByProcessId(Integer id) {
        update(null, Wrappers.<ProcessMember>lambdaUpdate()
                .set(ProcessMember::getIs_del, G.ISDEL_YES)
                .eq(ProcessMember::getId, id)
        );
    }

    /**
     * 新增流程
     *
     * @param build
     */
    public void addProcessMember(ProcessMember build) {
        build.setCreate_time(new Date());
        build.setIs_del(G.ISDEL_NO);
        insert(build);
    }

    /**
     * 根据业务id和流程key得到当前流程信息
     *
     * @param bus_id
     * @param value
     * @return
     */
    public ProcessMember getProcessMemberByBusId(Integer bus_id, String value) {
        ProcessMember member = selectOne(Wrappers.<ProcessMember>lambdaQuery()
                .eq(ProcessMember::getBus_id, bus_id)
                .eq(ProcessMember::getProcess_key, value)
                .eq(ProcessMember::getIs_del, G.ISDEL_NO)
        );
        if (member == null) {
            return null;
        }
        List<ProcessMemberNode> nodes = nodeMapper.selectList(Wrappers.<ProcessMemberNode>lambdaQuery()
                .eq(ProcessMemberNode::getProcess_member_id, member.getId())
                .eq(ProcessMemberNode::getFinish_status, 0)
                .eq(ProcessMemberNode::getIs_del, G.ISDEL_NO)
        );
        member.setNodes(nodes);

        member.setIs_revoke(0);
        for (ProcessMemberNode node : nodes) {
            ProcessConfigInfo info = processConfigInfoMapper.selectById(node.getNode_id());
            if (info.getNode_order() == 0 || info.getNode_order() == 1) {
                member.setIs_revoke(1);
            }
        }
        return member;
    }

    /**
     * 根据业务id和流程key得到当前流程节点
     */
    public ProcessConfig getInfoByBusId(Integer bus_id, String value) {
        // 流程控制 得到当前流程
        SysDict dict = sysDictService.getProcessByCode(value);
        ProcessMember member = getProcessMemberByBusId(bus_id, dict.getValue());
        if (member == null) {
            return null;
        }
        ProcessConfig config = processConfigMapper.selectById(member.getProcess_id());
        config.setProcessMember(member);

        List<ProcessConfigInfo> currentNodes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(member.getNodes())) {
            for (ProcessMemberNode node : member.getNodes()) {
                ProcessConfigInfo processConfigInfo = new ProcessConfigInfo();
                if (node.getNode_id() == 0) {
                    processConfigInfo = processConfigInfoMapper.selectOne(Wrappers.<ProcessConfigInfo>lambdaQuery()
                            .eq(ProcessConfigInfo::getProcess_config_id, member.getProcess_id())
                            .eq(ProcessConfigInfo::getIs_del, G.ISDEL_NO)
                            .eq(ProcessConfigInfo::getParent_node, 0)
                    );

                } else {
                    processConfigInfo = processConfigInfoMapper.selectById(node.getNode_id());
                }
                // 得到流程节点信息
                if (processConfigInfo != null) {
                    // 判断类型
                    if (processConfigInfo.getNode_type() == 0) {
                        // 查询岗位名称
                        SysPost sysPost = sysPostMapper.selectById(processConfigInfo.getNode_handler());
                        processConfigInfo.setNode_handler_name(null == sysPost ? "" : sysPost.getPost_name());
                    } else if (processConfigInfo.getNode_type() == 1) {
                        // 查询用户名称
                        SysUser sysUser = sysUserService.selectById(processConfigInfo.getNode_handler());
                        processConfigInfo.setNode_handler_name(null == sysUser ? "" : sysUser.getUser_name());
                    } else if (processConfigInfo.getNode_type() == 2) {
                        // 查询用户名称
                        SysUser sysUser = sysUserService.selectById(member.getApply_dept_leader_id());
                        processConfigInfo.setNode_handler_name(null == sysUser ? "" : sysUser.getUser_name());
                    }
                }
                currentNodes.add(processConfigInfo);
            }
        }

        config.setCurrentInfos(currentNodes);
        config.setProcessConfigInfos(processConfigInfoMapper.selectList(Wrappers.<ProcessConfigInfo>lambdaQuery()
                .eq(ProcessConfigInfo::getProcess_config_id, member.getProcess_id())
                .eq(ProcessConfigInfo::getIs_del, G.ISDEL_NO)
        ));
        return config;
    }

    /**
     * 根据流程key得到流程信息
     *
     * @param value
     * @return
     */
    public List<ProcessMember> getProcessMemberByBuskey(String value) {
        List<ProcessMember> list = selectList(Wrappers.<ProcessMember>lambdaQuery()
                .eq(ProcessMember::getProcess_key, value)
                .eq(ProcessMember::getIs_del, G.ISDEL_NO)
        );
        for (ProcessMember member : list) {
            List<ProcessMemberNode> nodes = nodeMapper.selectList(Wrappers.<ProcessMemberNode>lambdaQuery()
                    .eq(ProcessMemberNode::getProcess_member_id, member.getId())
                    .eq(ProcessMemberNode::getFinish_status, 0)
                    .eq(ProcessMemberNode::getIs_del, G.ISDEL_NO));
            member.setNodes(nodes);
        }
        return list;
    }

    /**
     * 删除流程
     *
     * @param processMember
     */
    public void removeProcessMember(ProcessMember processMember, String json) {
        removeProcessMember(processMember, json, sysUserService.getUser().getId());
    }

    public void removeProcessMember(ProcessMember processMember, String json, Integer userId) {
        historyService.addProcessMemberHistory(processMember, "流程终止", json, userId);

        update(null, Wrappers.<ProcessMember>lambdaUpdate()
                .set(ProcessMember::getIs_del, G.ISDEL_YES)
                .eq(ProcessMember::getId, processMember.getId())
        );
        nodeMapper.delete(Wrappers.<ProcessMemberNode>lambdaQuery()
                .eq(ProcessMemberNode::getProcess_member_id, processMember.getId())
        );
    }


    /**
     * 微信小程序删除流程
     *
     * @param processMember
     */
    public void removeWxProcessMember(ProcessMember processMember, String json, Integer userId) {
        historyService.addProcessMemberHistory(processMember, "流程终止", json, userId);
        update(null, Wrappers.<ProcessMember>lambdaUpdate()
                .set(ProcessMember::getIs_del, G.ISDEL_YES)
                .eq(ProcessMember::getId, processMember.getId())
        );
        nodeMapper.delete(Wrappers.<ProcessMemberNode>lambdaQuery()
                .eq(ProcessMemberNode::getProcess_member_id, processMember.getId())
        );
    }

    /**
     * 根据岗位或者用户id得到业务id集合
     */
    public List<Integer> getBusIdsByPostOrUserId(SysUser user, String processLabel) {
        if (user == null) {
            return null;
        }
        // 流程控制 得到当前流程
        SysDict dict = sysDictService.getProcessByCode(processLabel);
//        ProcessConfig config = processConfigService.getConfigByType(dict.getValue());
        // 得到业务的所有正在运行的流程
        List<ProcessMember> members = getProcessMemberByBuskey(dict.getValue());
        // 找到业务的所有流程, 筛选出来所有的bus_id, 然后根据bus_id去查询业务数据
        List<Integer> busIds = new ArrayList<>();
        for (ProcessMember member : members) {
            // 得到当前节点信息
            for (ProcessMemberNode node : member.getNodes()) {
                ProcessConfigInfo configInfo = null;
                if (node.getNode_id() == 0) {
                    // 当前节点是开始节点
                    configInfo = processConfigInfoMapper.selectOne(Wrappers.<ProcessConfigInfo>lambdaQuery()
                            .eq(ProcessConfigInfo::getProcess_config_id, member.getProcess_id())
                            .eq(ProcessConfigInfo::getIs_del, G.ISDEL_NO)
                            .eq(ProcessConfigInfo::getParent_node, 0)
                    );
                } else {
                    configInfo = processConfigInfoMapper.selectById(node.getNode_id());
                }
                if (configInfo == null) {
                    continue;
                }
                // 得到当前的办理人或者是办理岗位
                if (configInfo.getNode_type() == 0) {
                    // 岗位
                    if (configInfo.getNode_handler().equals(user.getPost())) {
                        // 当前岗位
                        busIds.add(member.getBus_id());
                    }
                } else if (configInfo.getNode_type() == 1) {
                    // 人员
                    if (configInfo.getNode_handler().equals(user.getId())) {
                        // 当前人员
                        busIds.add(member.getBus_id());
                    }
                } else if (configInfo.getNode_type() == 2) {
                    // 部门领导
                    if (member.getApply_dept_leader_id().equals(user.getId())) {
                        busIds.add(member.getBus_id());
                    }
                }
            }
        }
        return busIds;
    }

    /**
     * 得到下一个流程节点信息
     *
     * @param currentInfo
     * @param processNum
     * @return
     */
    public List<ProcessConfigInfo> getNextProcess(ProcessConfigInfo currentInfo, Integer processNum, List<Integer> type_ids) {
        List<ProcessConfigInfo> oldlist = processConfigInfoMapper.selectList(Wrappers.<ProcessConfigInfo>lambdaQuery()
                        .eq(ProcessConfigInfo::getProcess_config_id, currentInfo.getProcess_config_id())
//                .eq(ProcessConfigInfo::getParent_node, currentInfo.getId())
                        .eq(ProcessConfigInfo::getIs_del, G.ISDEL_NO)
        );
        List<ProcessConfigInfo> list = new ArrayList<>();
        for (ProcessConfigInfo info : oldlist) {
            // 判断info.getParent_node() 是否存在逗号,如果存在则是多个父节点,判断分隔后是否存在当前节点id,如果不存在则不是当前节点的子节点
            if (info.getParent_node().contains(",")) {
                String[] split = info.getParent_node().split(",");
                for (String s : split) {
                    if (s.equals(currentInfo.getId().toString())) {
                        list.add(info);
                    }
                }
            } else if (info.getParent_node().equals(currentInfo.getId().toString())) {
                list.add(info);
            }
        }
        List<ProcessConfigInfo> newList = new ArrayList<>();
        // 判断是否是并行 false:串行 true:并行
        boolean isParallel = false;
        // 判断集合中是否有多个节点,如果有判断节点condition是否为空,如果为空则是并行,如果不为空则是串行,串行进行条件判断
        if (list.size() > 1) {
            for (ProcessConfigInfo info : list) {
                if (StringUtils.isBlank(info.getProcess_condition()) || ",".equals(info.getProcess_condition().trim())) {
                    isParallel = true;
                    break;
                }
            }
        }

        for (ProcessConfigInfo info : list) {
            if (list.size() == 1) {
                // 只有一个节点
                newList.add(info);
                break;
            }
            // 判断是否是并行
            if (isParallel) {
                // 并行
                // info.getParent_node()根据,分隔进行id判断
                String[] split = info.getParent_node().split(",");
                for (String s : split) {
                    if (s.equals(currentInfo.getId().toString())) {
                        newList.add(info);
                    }
                }
            } else {
                // 串行
                String[] conditions = info.getProcess_condition().split(",");
                if (conditions.length == 2) {
                    // 验证string 是否为数字
                    Integer value = null;
                    if (StringUtils.isNumeric(conditions[1])) {
                        value = Integer.valueOf(conditions[1]);
                        if (ProcessEnums.calculate(conditions[0], value, processNum, type_ids)) {
                            newList.add(info);
                        }
                    }
                }

            }

        }
        return newList;
    }

    /**
     * 设置下一个节点
     */
    public void setNextNode(ProcessMember member, ProcessConfigInfo info) {
        Integer assignee = null;
        List<String> phones = new ArrayList<>();
        if (info.getNode_type() == 0) {
            // 1:岗位
            assignee = info.getNode_handler();
            phones = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                    .eq(SysUser::getIs_del, G.ISDEL_NO)
                    .eq(SysUser::getPost, assignee)
                    .isNotNull(SysUser::getTelephone)
            ).stream().map(SysUser::getTelephone).collect(Collectors.toList());


        } else if (info.getNode_type() == 1) {
            // 2:处理人
            assignee = info.getNode_handler();

            SysUser sysUser = sysUserService.selectById(assignee);
            if (null != sysUser && StringUtils.isNotBlank(sysUser.getTelephone())) {
                phones.add(sysUser.getTelephone());
            }

        } else if (info.getNode_type() == 2) {
            // 3:发起人负责人
            assignee = member.getApply_dept_leader_id();

            SysUser sysUser = sysUserService.selectById(assignee);
            if (null != sysUser && StringUtils.isNotBlank(sysUser.getTelephone())) {
                phones.add(sysUser.getTelephone());
            }
        }
        nodeMapper.insert(ProcessMemberNode.builder()
                .process_member_id(member.getId())
                .node_id(info.getId())
                .assignee_type(info.getNode_type())
                .assignee(assignee)
                .create_time(new Date())
                .finish_status(0)
                .is_del(G.ISDEL_NO)
                .build()
        );
    }

    /**
     * 发送信息
     *
     * @param member
     * @param phones
     */
    public void sendMSG(ProcessMember member, List<String> phones) {
        //String title(标题),String name（申请人名字）, String time（申请时间）,String content（地址）, String sending_phone（手机号）
        //判断属于哪个流程并获取地址
        try {
            List<SysDict> sysDicts = sysDictService.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getValue, member.getProcess_key()).eq(SysDict::getType, "process_type").eq(SysDict::getIs_del, G.ISDEL_NO));
            if (!CollectionUtils.isEmpty(sysDicts)) {
                SysDict sysDict = sysDicts.get(0);
                String title = "";
                String name = "";
                String time = "";
                String content = "";
                String sending_phone = "";

                if (sysDict.getLabel().contains(G.PROCESS_ZCSL)) {
                    //资产申领
                    title = "资产领用";
                    content = G.ZCSL_PANDING_APPROVE;

                } else if (sysDict.getLabel().contains(G.PROCESS_ZCCG)) {
                    //资产采购
                    title = "资产采购";
                    content = G.ZCCG_PANDING_APPROVE;
                } else if (sysDict.getLabel().contains(G.PROCESS_ZCBG)) {
                    //资产变更
                    title = "资产变更";
                    content = G.ZCBG_PANDING_APPROVE;

                } else if (sysDict.getLabel().contains(G.PROCESS_ZCCZ)) {
                    //资产处置
                    title = "资产处置";
                    content = G.ZCCZ_PANDING_APPROVE;

                } else if (sysDict.getLabel().contains(G.PROCESS_ZCWX)) {
                    //资产外携
                    title = "资产外携";
                    content = G.ZCCARRY_PANDING_APPROVE;

                } else if (sysDict.getLabel().contains(G.PROCESS_HCCG)) {
                    //耗材采购
                    title = "耗材采购";
                    content = G.HCCG_PANDING_APPROVE;

                } else if (sysDict.getLabel().contains(G.PROCESS_HCSL)) {
                    //耗材申领
                    title = "耗材申领";
                    content = G.HCSL_PANDING_APPROVE;

                } else if (sysDict.getLabel().contains(G.PROCESS_WXZC)) {
                    //资产维修
                    title = "资产维修";
                    content = G.ZCWX_PANDING_APPROVE;

                }
                //获得申请人名字 时间
                SysUser sysUser = sysUserService.selectById(member.getCreate_user());
                if (null != sysUser) {
                    name = sysUser.getUser_name();
                }
                if (null != member.getCreate_time()) {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    time = s.format(member.getCreate_time());
                }

                //手机号
                sending_phone = phones.stream().collect(Collectors.joining(","));

                if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(name) && StringUtils.isNotBlank(content) && StringUtils.isNotBlank(sending_phone)) {

                    aliSmsUtil.httpClientRfidSP(title, name, time, content, sending_phone);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 流程启动
     */
    public void startProcess(String processLabel, Integer busId, Integer applyUserId) {

        // 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(processLabel);
        // 查询流程
        ProcessConfig config = processConfigService.getConfigByType(dictionary.getValue());
        if (null == config) {
            throw new RuntimeException("未配置流程");
        }
        // 查询流程bud_id,和key
        update(null, Wrappers.<ProcessMember>lambdaUpdate()
                .set(ProcessMember::getIs_del, G.ISDEL_YES)
                .eq(ProcessMember::getBus_id, busId)
                .eq(ProcessMember::getProcess_key, config.getProcess_code())
        );
        // 领用送审,加入流程
        ProcessMember processMember = new ProcessMember();
        // 流程id
        processMember.setProcess_id(config.getId());
        // 业务id
        processMember.setBus_id(busId);
        // 流程key
        processMember.setProcess_key(config.getProcess_code());
        // 得到发起人id
        processMember.setCreate_user(applyUserId);
        // 得到发起人负责人
        SysUser currentUser = sysUserService.selectById(applyUserId);
        List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getIs_del, G.ISDEL_NO)
                .eq(SysUser::getIs_dept_leader, 1)
                .eq(SysUser::getDepartment, currentUser.getDepartment())
        );
        if (CollectionUtils.isEmpty(sysUsers)) {
            currentUser.setIs_dept_leader(1);
            sysUserService.updateById(currentUser);
            sysUsers.add(currentUser);
        }
        processMember.setApply_dept_leader_id(sysUsers.get(0).getId());
        // 得到节点id
        List<ProcessConfigInfo> infos = processConfigInfoMapper.selectList(Wrappers.<ProcessConfigInfo>query()
                .eq("is_del", G.ISDEL_NO)
                .eq("process_config_id", config.getId())
                .orderByAsc("node_order")
        );
        if (CollectionUtils.isEmpty(infos)) {
            throw new RuntimeException("未配置流程节点");
        }
        addProcessMember(processMember);
        // 得到第一个节点信息
        ProcessConfigInfo first = infos.get(0);
        Integer assignee = null;


        List<String> phones = new ArrayList<>();

        if (first.getNode_type() == 0) {
            // 1:岗位
            assignee = first.getNode_handler();

            phones = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                    .eq(SysUser::getIs_del, G.ISDEL_NO)
                    .eq(SysUser::getPost, assignee)
                    .isNotNull(SysUser::getTelephone)
            ).stream().map(SysUser::getTelephone).collect(Collectors.toList());

        } else if (first.getNode_type() == 1) {
            // 2:处理人
            assignee = first.getNode_handler();

            SysUser sysUser = sysUserService.selectById(assignee);
            if (null != sysUser && StringUtils.isNotBlank(sysUser.getTelephone())) {
                phones.add(sysUser.getTelephone());
            }

        } else if (first.getNode_type() == 2) {
            // 3:发起人负责人
            assignee = processMember.getApply_dept_leader_id();


            SysUser sysUser = sysUserService.selectById(assignee);
            if (null != sysUser && StringUtils.isNotBlank(sysUser.getTelephone())) {
                phones.add(sysUser.getTelephone());
            }
        }
        nodeMapper.insert(ProcessMemberNode.builder()
                .process_member_id(processMember.getId())
                .node_id(first.getId())
                .assignee_type(first.getNode_type())
                .assignee(assignee)
                .create_time(new Date())
                .finish_status(0)
                .is_del(G.ISDEL_NO)
                .build()
        );
        processMember.setNode_id(-1);
        Map<String, Object> map = new HashMap<>();
        map.put("id", busId);
        historyService.addProcessMemberHistory(processMember, currentUser.getUser_name() + "启动流程", JSON.toJSONString(map), applyUserId);

        //发送短信
        sendMSG(processMember, phones);


    }

    /**
     * 设置当前节点完成状态
     *
     * @param member
     * @param currentInfo
     * @param i           0:未完成 1:完成
     */
    public Integer setCurrentNode(ProcessMember member, ProcessConfigInfo currentInfo, int i) {
        nodeMapper.update(null, Wrappers.<ProcessMemberNode>lambdaUpdate()
                .set(ProcessMemberNode::getFinish_status, i)
                .eq(ProcessMemberNode::getProcess_member_id, member.getId())
                .eq(ProcessMemberNode::getNode_id, currentInfo.getId())
        );
        return currentInfo.getId();
    }

    /**
     * 得到当前节点信息
     */
    public ProcessConfigInfo getCurrentNodeInfo(ProcessMember member, SysUser sysUser) {
        ProcessConfig config = processConfigMapper.selectById(member.getProcess_id());
        config.setProcessMember(member);

        List<ProcessConfigInfo> currentInfos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(member.getNodes())) {
            for (ProcessMemberNode node : member.getNodes()) {
                ProcessConfigInfo processConfigInfo = new ProcessConfigInfo();
                if (node.getNode_id() == 0) {
                    processConfigInfo = processConfigInfoMapper.selectOne(Wrappers.<ProcessConfigInfo>lambdaQuery()
                            .eq(ProcessConfigInfo::getProcess_config_id, member.getProcess_id())
                            .eq(ProcessConfigInfo::getIs_del, G.ISDEL_NO)
                            .eq(ProcessConfigInfo::getParent_node, 0)
                    );

                } else {
                    processConfigInfo = processConfigInfoMapper.selectById(node.getNode_id());
                }
                currentInfos.add(processConfigInfo);
            }
        }

        config.setCurrentInfos(currentInfos);
        config.setProcessConfigInfos(processConfigInfoMapper.selectList(Wrappers.<ProcessConfigInfo>lambdaQuery()
                .eq(ProcessConfigInfo::getProcess_config_id, member.getProcess_id())
                .eq(ProcessConfigInfo::getIs_del, G.ISDEL_NO)
        ));
        // 根据当前登录人判断在哪个子节点
        ProcessConfigInfo currentInfo = null;
        for (ProcessConfigInfo info : currentInfos) {
            if (info.getNode_type() == 0 && info.getNode_handler().equals(sysUser.getPost())) {
                currentInfo = info;
                break;
            } else if (info.getNode_type() == 1 && info.getNode_handler().equals(sysUser.getId())) {
                currentInfo = info;
                break;
            } else if (info.getNode_type() == 2 && member.getApply_dept_leader_id().equals(sysUser.getId())) {
                currentInfo = info;
                break;
            }
        }
        return currentInfo;
    }

    /**
     * 流程处理
     * <p>
     * <p>
     * web端-待审批管理	处置待审批	页面	页面无数据
     *
     * @param bus_id
     * @param processZcsl
     * @param sysUser
     * @param logs
     * @param processNum
     * @param json
     * @return
     */
    @Synchronized
    public boolean processing(Integer bus_id, String processZcsl, SysUser sysUser, String logs, Integer processNum, List<Integer> type_ids, String json) {
        // 得到当前流程节点信息
        ProcessConfig processConfig = getInfoByBusId(bus_id, processZcsl);
        if (null == processConfig) {
            throw new RuntimeException("流程节点信息不存在");
        }
        List<ProcessConfigInfo> currentInfos = processConfig.getCurrentInfos();
        // 根据当前登录人判断在哪个子节点
        ProcessConfigInfo currentInfo = null;
        for (ProcessConfigInfo info : currentInfos) {
            if (info.getNode_type() == 0 && info.getNode_handler().equals(sysUser.getPost())) {
                currentInfo = info;
                break;
            } else if (info.getNode_type() == 1 && info.getNode_handler().equals(sysUser.getId())) {
                currentInfo = info;
                break;
            } else if (info.getNode_type() == 2 && processConfig.getProcessMember().getApply_dept_leader_id().equals(sysUser.getId())) {
                currentInfo = info;
                break;
            }
        }
        // 得到当前流程节点的下一个节点信息
        List<ProcessConfigInfo> nextProcessConfig = null;
        if (currentInfo != null) {
            nextProcessConfig = getNextProcess(currentInfo, processNum, type_ids);
        }
        if (!CollectionUtils.isEmpty(nextProcessConfig)) {
            // 更新当前流程节点信息
            ProcessMember member = processConfig.getProcessMember();
            // 设置当前节点信息的完成状态
            int node = setCurrentNode(member, currentInfo, 1);
            // 设置未经过的节点信息的完成状态
            for (ProcessConfigInfo info : nextProcessConfig) {
                if (info.getParent_node().contains(",")) {
                    String[] split = info.getParent_node().split(",");
                    for (String s : split) {
                        if (!Integer.valueOf(s).equals(currentInfo.getId())) {
                            setCurrentNode(member, processConfigInfoMapper.selectById(Integer.valueOf(s)), 2);
                        }
                    }
                }
            }
            member.setNode_id(node);
            // 有下一个节点
            for (ProcessConfigInfo info : nextProcessConfig) {
                // 添加下一个节点信息
                setNextNode(member, info);
            }
            // 增加短息状态
            member.setMsgStatus(1);
            historyService.addProcessMemberHistory(member, logs, json, sysUser.getId());

            Set<String> phoneSet = new HashSet<>();
            for (ProcessConfigInfo info : nextProcessConfig) {
                Integer assignee = null;
                if (info.getNode_type() == 0) {
                    // 1:岗位
                    assignee = info.getNode_handler();
                    List<String> phonesList = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                            .eq(SysUser::getIs_del, G.ISDEL_NO)
                            .eq(SysUser::getPost, assignee)
                            .isNotNull(SysUser::getTelephone)
                    ).stream().map(SysUser::getTelephone).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(phonesList)) {
                        phoneSet.addAll(phonesList);
                    }


                } else if (info.getNode_type() == 1) {
                    // 2:处理人
                    assignee = info.getNode_handler();

                    SysUser sysUser1 = sysUserService.selectById(assignee);
                    if (null != sysUser1 && StringUtils.isNotBlank(sysUser1.getTelephone())) {
                        phoneSet.add(sysUser1.getTelephone());
                    }

                } else if (info.getNode_type() == 2) {
                    // 3:发起人负责人
                    assignee = member.getApply_dept_leader_id();

                    SysUser sysUser1 = sysUserService.selectById(assignee);
                    if (null != sysUser1 && StringUtils.isNotBlank(sysUser1.getTelephone())) {
                        phoneSet.add(sysUser1.getTelephone());
                    }
                }
            }

            List<String> phones = new ArrayList<>(phoneSet);

            //发送信息
            try {
                sendMSG(member, phones);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return true;
        } else {
            // 没有下一个节点, 结束流程
            ProcessMember member = processConfig.getProcessMember();
            member.setUserId(sysUser.getId());
            member.setMsgStatus(3);
            historyService.addProcessMemberHistory(member, logs, json, sysUser.getId());
            removeProcessMember(member, json, sysUser.getId());
            return false;
        }

    }

    /**
     * 得到流程历史数据
     *
     * @param user
     * @param processZccg
     * @return
     */
    public List<Integer> getBusIdsByUserIdHistory(SysUser user, String processZccg) {
        if (user == null) {
            return null;
        }
        // 流程控制 得到当前流程
        SysDict dict = sysDictService.getProcessByCode(processZccg);
        // 得到业务的所有正在运行的流程
        List<ProcessMemberHistory> histories = historyService.selectList(Wrappers.<ProcessMemberHistory>lambdaQuery()
                .eq(ProcessMemberHistory::getProcess_key, dict.getValue())
                .eq(ProcessMemberHistory::getIs_del, G.ISDEL_NO)
                .eq(ProcessMemberHistory::getUpdate_user, user.getId())
        );

        // 找到业务的所有流程, 筛选出来所有的bus_id, 然后根据bus_id去查询业务数据
        return histories.stream().distinct().map(ProcessMemberHistory::getBus_id).collect(Collectors.toList());
    }

    /**
     * 得到列表list 或者 getbyid 中 流程的信息
     */
    public <T> T getProcessDataByBusId(Integer bus_id, String processZccg, SysUser current, T temp) {
        // 得到当前流程节点
        ProcessConfig process = getInfoByBusId(bus_id, processZccg);
        String taskName = "";
        String assigneeName = "";
        List<Integer> currentTaskIds = new ArrayList<>();
        String currentTaskName = "";
        ProcessConfigInfo info1 = new ProcessConfigInfo();
        if (null != process && !CollectionUtils.isEmpty(process.getCurrentInfos())) {
            taskName = process.getCurrentInfos().stream().map(ProcessConfigInfo::getNode_name).collect(Collectors.joining(","));
            assigneeName = process.getCurrentInfos().stream().map(ProcessConfigInfo::getNode_handler_name).collect(Collectors.joining(","));
            currentTaskIds = process.getCurrentInfos().stream().map(ProcessConfigInfo::getId).collect(Collectors.toList());
            for (ProcessConfigInfo info : process.getCurrentInfos()) {
                if (info.getNode_type() == 0 && current.getPost().equals(info.getNode_handler())) {
                    currentTaskName = info.getNode_name();
                    info1 = info;
                } else if (info.getNode_type() == 1 && current.getId().equals(info.getNode_handler())) {
                    currentTaskName = info.getNode_name();
                    info1 = info;
                } else if (info.getNode_type() == 2 && process.getProcessMember().getApply_dept_leader_id().equals(current.getId())) {
                    currentTaskName = info.getNode_name();
                    info1 = info;
                }
            }
        }
        // 根据processZccg 得到T的实体类
        switch (processZccg){
            case "资产申领":
                // 则实体类是 Receive
                ((Receive) temp).setTaskName(taskName);
                ((Receive) temp).setAssigneeName(assigneeName);
                ((Receive) temp).setCurrentTaskIds(currentTaskIds);
                ((Receive) temp).setCurrentTaskName(currentTaskName);
                ((Receive) temp).setInfo(info1);
                return (T) temp;
            case "资产采购":
                // 则实体类是 Purchase
                ((PurchaseApply) temp).setTaskName(taskName);
                ((PurchaseApply) temp).setAssigneeName(assigneeName);
                ((PurchaseApply) temp).setCurrentTaskIds(currentTaskIds);
                ((PurchaseApply) temp).setCurrentTaskName(currentTaskName);
                ((PurchaseApply) temp).setInfo(info1);
                return (T) temp;
            case "资产变更":
                // 则实体类是 Change
                ((ChangeInfo) temp).setTaskName(taskName);
                ((ChangeInfo) temp).setAssigneeName(assigneeName);
                ((ChangeInfo) temp).setCurrentTaskIds(currentTaskIds);
                ((ChangeInfo) temp).setCurrentTaskName(currentTaskName);
                ((ChangeInfo) temp).setInfo(info1);
                return (T) temp;
            case "资产处置":
                // 则实体类是 Disposition
                ((Management) temp).setTaskName(taskName);
                ((Management) temp).setAssigneeName(assigneeName);
                ((Management) temp).setCurrentTaskIds(currentTaskIds);
                ((Management) temp).setCurrentTaskName(currentTaskName);
                ((Management) temp).setInfo(info1);
                return (T) temp;
            case "资产外携":
                // 则实体类是 Carry
                ((CarryManage) temp).setTaskName(taskName);
                ((CarryManage) temp).setAssigneeName(assigneeName);
                ((CarryManage) temp).setCurrentTaskIds(currentTaskIds);
                ((CarryManage) temp).setCurrentTaskName(currentTaskName);
                ((CarryManage) temp).setInfo(info1);
                return (T) temp;
            case "耗材采购":
                // 则实体类是 Consumable
                ((ConsPurchaseApply) temp).setTaskName(taskName);
                ((ConsPurchaseApply) temp).setAssigneeName(assigneeName);
                ((ConsPurchaseApply) temp).setCurrentTaskIds(currentTaskIds);
                ((ConsPurchaseApply) temp).setCurrentTaskName(currentTaskName);
                ((ConsPurchaseApply) temp).setInfo(info1);
                return (T) temp;
            case "耗材申领":
                // 则实体类是 Consumable
                ((ConsReceive) temp).setTaskName(taskName);
                ((ConsReceive) temp).setAssigneeName(assigneeName);
                ((ConsReceive) temp).setCurrentTaskIds(currentTaskIds);
                ((ConsReceive) temp).setCurrentTaskName(currentTaskName);
                ((ConsReceive) temp).setInfo(info1);
                return (T) temp;
            case "资产维修":
                // 则实体类是 Repair
                ((RepairApply) temp).setTaskName(taskName);
                ((RepairApply) temp).setAssigneeName(assigneeName);
                ((RepairApply) temp).setCurrentTaskIds(currentTaskIds);
                ((RepairApply) temp).setCurrentTaskName(currentTaskName);
                ((RepairApply) temp).setInfo(info1);
                return (T) temp;

        }

        return null;
    }
}
