package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ProcessMemberHistoryDao;
import com.jsdc.rfid.mapper.ProcessConfigInfoMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.AliSmsUtil;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ProcessMemberHistoryService extends BaseService<ProcessMemberHistoryDao, ProcessMemberHistory> {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPostService postService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private ProcessConfigInfoMapper processConfigInfoMapper;

    @Autowired
    private FileManageService fileManageService;

    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private AliSmsUtil aliSmsUtil;


    /**
     * 添加流程历史
     */
    public void addProcessMemberHistory(ProcessMember runProcessMember, String remark, String json) {
        addProcessMemberHistory(runProcessMember, remark, json, sysUserService.getUser().getId());
    }



    public void  finishSendMSG(ProcessMember member){
        try {
            String title = "";
            String content = "";
            String sending_phone = "";

            List<SysDict> sysDicts = sysDictService.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getValue, member.getProcess_key()).eq(SysDict::getType, "process_type").eq(SysDict::getIs_del, G.ISDEL_NO));
            if (!CollectionUtils.isEmpty(sysDicts)){
                SysDict sysDict = sysDicts.get(0);

                if (sysDict.getLabel().contains(G.PROCESS_ZCSL)){
                    //资产申领
                    title = "资产领用";
                    content = G.LYSQ_APPLY;

                } else if (sysDict.getLabel().contains(G.PROCESS_ZCCG)) {
                    //资产采购
                    title = "资产采购";
                    content = G.ZCCG_APPLY;
                } else if (sysDict.getLabel().contains(G.PROCESS_ZCBG)) {
                    //资产变更
                    title = "资产变更";
                    content = G.BGSQ_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_ZCCZ)) {
                    //资产处置
                    title = "资产处置";
                    content = G.CZSQ_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_ZCWX)) {
                    //资产外携
                    title = "资产外携";
                    content = G.CARRY_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_HCCG)) {
                    //耗材采购
                    title = "耗材采购";
                    content = G.HCCG_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_HCSL)) {
                    //耗材申领
                    title = "耗材申领";
                    content = G.HCSL_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_WXZC)) {
                    //资产维修
                    title = "资产维修";
                    content = G.WXSQ_APPLY;
                }

                SysUser sysUser = sysUserService.selectById(member.getCreate_user());
                if (null != sysUser && StringUtils.isNotBlank(sysUser.getTelephone())){
                    sending_phone = sysUser.getTelephone();
                }

                if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(content) && StringUtils.isNotBlank(sending_phone) ){
                    aliSmsUtil.passRfid(title,content,sending_phone);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    public void  rejectSendMSG(ProcessMember member,String remark){
        try {
            String title = "";
            String name = "";
            String time = "";
            String resons = "";
            String content = "";
            String sending_phone = "";

            List<SysDict> sysDicts = sysDictService.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getValue, member.getProcess_key()).eq(SysDict::getType, "process_type").eq(SysDict::getIs_del, G.ISDEL_NO));
            if (!CollectionUtils.isEmpty(sysDicts)){
                SysDict sysDict = sysDicts.get(0);
                //备注
                resons = remark;
                if (sysDict.getLabel().contains(G.PROCESS_ZCSL)){
                    //资产申领
                    title = "资产领用";
                    content = G.LYSQ_APPLY;

                } else if (sysDict.getLabel().contains(G.PROCESS_ZCCG)) {
                    //资产采购
                    title = "资产采购";
                    content = G.ZCCG_APPLY;
                } else if (sysDict.getLabel().contains(G.PROCESS_ZCBG)) {
                    //资产变更
                    title = "资产变更";
                    content = G.BGSQ_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_ZCCZ)) {
                    //资产处置
                    title = "资产处置";
                    content = G.CZSQ_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_ZCWX)) {
                    //资产外携
                    title = "资产外携";
                    content = G.CARRY_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_HCCG)) {
                    //耗材采购
                    title = "耗材采购";
                    content = G.HCCG_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_HCSL)) {
                    //耗材申领
                    title = "耗材申领";
                    content = G.HCSL_APPLY;

                }else if (sysDict.getLabel().contains(G.PROCESS_WXZC)) {
                    //资产维修
                    title = "资产维修";
                    content = G.WXSQ_APPLY;
                }
                Integer node_id = member.getNode_id();
                if (null != node_id){
                    ProcessConfigInfo processConfigInfo = processConfigInfoMapper.selectById(node_id);
                    if (null != processConfigInfo){
                        name = processConfigInfo.getNode_name();
                    }
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                time = simpleDateFormat.format(new Date());

                SysUser sysUser = sysUserService.selectById(member.getCreate_user());
                if (null != sysUser && StringUtils.isNotBlank(sysUser.getTelephone())){
                    sending_phone = sysUser.getTelephone();
                }

                if (StringUtils.isNotBlank(title)  && StringUtils.isNotBlank(resons) && StringUtils.isNotBlank(content) && StringUtils.isNotBlank(sending_phone) ){
                    aliSmsUtil.httpClientRfidReject(title,name,time,resons,content,sending_phone);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void addProcessMemberHistory(ProcessMember runProcessMember, String remark, String json,Integer userId) {
        ProcessMemberHistory history = new ProcessMemberHistory();
        history.setProcess_id(runProcessMember.getProcess_id());
        history.setBus_id(runProcessMember.getBus_id());
        history.setNode_id(runProcessMember.getNode_id());
        if (null == runProcessMember.getNode_id()) {
            history.setNode_name("流程结束");
        }else if(runProcessMember.getNode_id() == -1){
            history.setNode_name("流程开始");
        }
        StringBuilder node_name = new StringBuilder();
        if(CollectionUtils.isEmpty(runProcessMember.getNodes()) ){
            ProcessConfigInfo info = processConfigInfoMapper.selectById(runProcessMember.getNode_id());
            node_name = new StringBuilder(null == info ? "" : info.getNode_name());
        }else{
            for (ProcessMemberNode node : runProcessMember.getNodes()) {
                ProcessConfigInfo info = processConfigInfoMapper.selectById(node.getNode_id());
                node_name.append(null == info ? "" : info.getNode_name());
                //如果是最后一个节点，不加逗号
                if(!node.getNode_id().equals(runProcessMember.getNodes().get(runProcessMember.getNodes().size() - 1).getNode_id())){
                    node_name.append(",");
                }
            }
        }
        String file_ids = "";
        // 找到json中的附件ids
        if(StringUtils.isNotBlank(json) && !StringUtils.equals("流程终止", remark)){
            // 降json转为对象Map<String,Object>
            JSONObject jsonObject = JSONObject.parseObject(json);
            // 获取附件ids
            file_ids = jsonObject.getString("files");
        }
//        ProcessConfigInfo info = processConfigInfoMapper.selectById(runProcessMember.getNode_id());
        history.setNode_name(node_name.toString());
        history.setProcess_key(runProcessMember.getProcess_key());
        history.setProcess_remark(remark);
        history.setBus_json(json);
        history.setFile_ids(file_ids);
        history.setIs_del(G.ISDEL_NO);
        history.setCreate_time(new Date());
        if(null == runProcessMember.getUserId()){
            history.setCreate_user(userId);
            history.setUpdate_user(userId);
        }else{
            history.setCreate_user(runProcessMember.getUserId());
            history.setUpdate_user(runProcessMember.getUserId());
        }

        history.setUpdate_time(new Date());



        if (StringUtils.equals(remark, "流程驳回") && 2 == runProcessMember.getMsgStatus()){
            //发送短信
            try {
                rejectSendMSG(runProcessMember, null == runProcessMember.getRejectReason()?"流程驳回":runProcessMember.getRejectReason());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (StringUtils.equals(remark,"流程终止") && (null == runProcessMember.getMsgStatus() || 2 != runProcessMember.getMsgStatus())){
            //发送短信
            try {
                if (null == runProcessMember.getIsCH() || runProcessMember.getIsCH() != 1){
                    finishSendMSG(runProcessMember);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 流程驳回备注处理
        if (StringUtils.equals(remark, "流程驳回")){
            history.setProcess_remark("流程驳回: " + (null == runProcessMember.getRejectReason() ? "无" : runProcessMember.getRejectReason()));
        } else if(StringUtils.equals(remark, "一键审批")){
            history.setProcess_remark("一键审批: " + (null == runProcessMember.getApprovalReason() ? "无" : runProcessMember.getApprovalReason()));
        } else if(StringUtils.equals(remark, "流程终止")){
            // 当前时间增加30秒
            Date date = new Date(System.currentTimeMillis() + 30 * 1000);
            history.setCreate_time(date);
        }
        insert(history);
    }

    /**
     * 根据流程key和业务id获取流程历史
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    public PageInfo<ProcessMemberHistory> getListByKeyAndBusId(Integer pageIndex, Integer pageSize, ProcessMemberHistory beanParam) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ProcessMemberHistory> list = selectList(Wrappers.<ProcessMemberHistory>query()
                .eq("process_key", beanParam.getProcess_key())
                .eq("bus_id", beanParam.getBus_id())
                .eq("is_del", G.ISDEL_NO)
                .orderByDesc("update_time")
        );
        for (ProcessMemberHistory history : list) {
            if (history.getCreate_user() != null){
                SysUser user = userService.selectById(history.getCreate_user());
                history.setCreate_user_name(null == user?"":user.getUser_name());
            }
            if (history.getUpdate_user() != null){
                SysUser user = userService.selectById(history.getUpdate_user());
                history.setUpdate_user_name(null == user?"":user.getUser_name());
            }
            if (StringUtils.isBlank(history.getNode_name())){
                if (history.getNode_id() != null && history.getNode_id() != -1){
                    history.setNode_name(processConfigInfoMapper.selectById(history.getNode_id()).getNode_name());
                }else {
                    history.setNode_name("起始节点");
                }
            }
            // 获取附件列表
            if(StringUtils.isNotBlank(history.getFile_ids())){
                String[] ids = history.getFile_ids().split(",");
                // ids 转为list<Integer> 排除ids为空的情况
                List<Integer> idList = new ArrayList<>();
                for (String id : ids) {
                    if(StringUtils.isNotBlank(id)){
                        idList.add(Integer.valueOf(id));
                    }
                }
                List<FileManage> fileList = fileManageService.selectList(Wrappers.<FileManage>lambdaQuery()
                        .in(FileManage::getId, idList)
                        .eq(FileManage::getIs_del, G.ISDEL_NO)
                );
                history.setFileList(fileList);
            }
        }
        return new PageInfo<>(list);
    }


    /**
     * 根据流程key和业务id获取流程历史
     * @param beanParam
     * @return
     */
    public List<ProcessMemberHistory> getKeyAndBusIdList( ProcessMemberHistory beanParam) {

        List<ProcessMemberHistory> list = selectList(Wrappers.<ProcessMemberHistory>query()
                .eq("process_key", beanParam.getProcess_key())
                .eq("bus_id", beanParam.getBus_id())
                .eq("is_del", G.ISDEL_NO)
                .orderByDesc("create_time")
        );
        Integer count = 0 ;
        for (ProcessMemberHistory history : list) {
            if (history.getCreate_user() != null){
                SysUser user = userService.selectById(history.getCreate_user());
                history.setCreate_user_name(null == user?"":user.getUser_name());
            }
            if (history.getUpdate_user() != null){
                SysUser user = userService.selectById(history.getUpdate_user());
                history.setUpdate_user_name(null == user?"":user.getUser_name());
            }
            if (history.getNode_id() != null && history.getNode_id() != -1){
                history.setNode_name(processConfigInfoMapper.selectById(history.getNode_id()).getNode_name());
            }else {
                history.setNode_name("起始节点");
            }
            if(StringUtils.isNotBlank(history.getFile_ids())){
                List<String> ids = Arrays.asList(history.getFile_ids().split(","));
                List<FileManage> fileManages = fileManageService.selectList(Wrappers.<FileManage>lambdaQuery().in(FileManage::getId, ids));
                history.setFileList(fileManages);
            }
            if (StringUtils.isNotBlank(history.getProcess_remark())){
                // 如果包含<div>和</div> 则替换为空
                if (history.getProcess_remark().contains("<div>") && history.getProcess_remark().contains("</div>")){
                    history.setProcess_remark(history.getProcess_remark().replaceAll("<div>","").replaceAll("</div>",""));
                }
            }
            count++;
            history.setCount(count);

        }

        return list;
    }
}
