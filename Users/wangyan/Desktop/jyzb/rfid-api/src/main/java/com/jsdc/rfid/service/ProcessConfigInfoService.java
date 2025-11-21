package com.jsdc.rfid.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ProcessConfigInfoDao;
import com.jsdc.rfid.enums.ProcessEnums;
import com.jsdc.rfid.mapper.AssetsTypeMapper;
import com.jsdc.rfid.mapper.ConsCategoryMapper;
import com.jsdc.rfid.mapper.ConsumableMapper;
import com.jsdc.rfid.model.*;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.ResultInfo;

import java.util.Date;
import java.util.List;

@Service
public class ProcessConfigInfoService extends BaseService<ProcessConfigInfoDao, ProcessConfigInfo> {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPostService postService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private AssetsTypeMapper assetsTypeMapper;

    @Autowired
    private ConsCategoryMapper consCategoryMapper;

    @Autowired
    private ConsumableMapper consumableMapper;

    /**
     * 根据流程id获取流程配置信息
     * @param process_config_id
     * @return
     */
    public List<ProcessConfigInfo> getInfosByProcessId(Integer process_config_id) {
        List<ProcessConfigInfo> processConfigInfos = selectList(Wrappers.<ProcessConfigInfo>lambdaQuery()
                .eq(ProcessConfigInfo::getProcess_config_id, process_config_id)
                .eq(ProcessConfigInfo::getIs_del, G.ISDEL_NO)
        );
        return processConfigInfos;
    }

    /**
     * 根据流程id删除流程配置信息
     * @param id
     */
    public void removeInfoByProcessId(Integer id) {
        update(null, Wrappers.<ProcessConfigInfo>lambdaUpdate()
                .set(ProcessConfigInfo::getIs_del, G.ISDEL_YES)
                .eq(ProcessConfigInfo::getProcess_config_id, id)
        );
    }

    /**
     * 保存节点信息
     * @param bean
     * @return
     */
    public ResultInfo save(ProcessConfigInfo bean) {
        if(null == bean.getProcess_config_id()){
            return ResultInfo.error("流程id不能为空");
        }
        bean.setIs_del(G.ISDEL_NO);
        bean.setUpdate_time(new Date());
        if (null != bean.getId() && null != selectById(bean.getId())){
            updateById(bean);
        } else {
            bean.setCreate_user(sysUserService.getUser().getId());
            bean.setCreate_time(new Date());
            insert(bean);
        }
        return ResultInfo.success();
    }

    /**
     * 列表查询
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    public PageInfo<ProcessConfigInfo> toList(Integer pageIndex, Integer pageSize, ProcessConfigInfo beanParam) {
        PageHelper.startPage(pageIndex, pageSize, "node_order asc");
        List<ProcessConfigInfo> list = selectList(Wrappers.<ProcessConfigInfo>lambdaQuery()
                .eq(ProcessConfigInfo::getIs_del, G.ISDEL_NO)
                .eq(ProcessConfigInfo::getProcess_config_id, beanParam.getProcess_config_id())
                .like(null != beanParam.getNode_name(), ProcessConfigInfo::getNode_name, beanParam.getNode_name())
                .eq(null != beanParam.getNode_type(), ProcessConfigInfo::getNode_type, beanParam.getNode_type())
                .eq(null != beanParam.getNode_handler(), ProcessConfigInfo::getNode_handler, beanParam.getNode_handler())
                .eq(null != beanParam.getNode_order(), ProcessConfigInfo::getNode_order, beanParam.getNode_order())
//                .orderByAsc(ProcessConfigInfo::getNode_order)
        );
        for (ProcessConfigInfo processConfigInfo : list) {
            if (processConfigInfo.getNode_type() == 0){
                SysPost post = postService.selectById(processConfigInfo.getNode_handler());
                processConfigInfo.setNode_handler_name(null != post ? post.getPost_name() : "");
            } else if (processConfigInfo.getNode_type() == 1){
                SysUser user = sysUserService.selectById(processConfigInfo.getNode_handler());
                processConfigInfo.setNode_handler_name(null != user ? user.getUser_name() : "");
            } else if (processConfigInfo.getNode_type() == 2){
                processConfigInfo.setNode_handler_name("申请人的部门领导");
            } else {
                processConfigInfo.setNode_handler_name("无");
            }
            String parent_node = processConfigInfo.getParent_node();
            String parent_node_name = "";
            if (StringUtils.isNotBlank(parent_node) && parent_node.contains(",")){
                String[] split = parent_node.split(",");
                parent_node = "";
                for (String s : split) {
                    Integer id = Integer.valueOf(s);
                    ProcessConfigInfo processConfigInfo1 = selectById(id);
                    parent_node_name += processConfigInfo1.getNode_name() + ",";
                }
                parent_node_name = parent_node_name.substring(0, parent_node_name.length() - 1);
            } else {
                int id = Integer.parseInt(StringUtils.isBlank(parent_node) ? "0" : parent_node);
                parent_node_name = 0 == id?"起始节点":selectById(id).getNode_name();
            }
            processConfigInfo.setParent_node_name(parent_node_name);
            // 条件回显
            String condition = processConfigInfo.getProcess_condition();
            if (StringUtils.isNotBlank(condition) && condition.split(",").length == 2){
                String[] split = condition.split(",");
                String code = split[0];
                if ("11".equals(code) || "12".equals(code)) {
                    String b = split[1];
                    String[] c = b.split("-");
                    String result = "";
                    for (String s : c) {
                        AssetsType assetsType = assetsTypeMapper.selectById(s);
                        // 判断是否为最后一个
                        result += assetsType.getAssets_type_name() + ",";
                    }
                    if (StrUtil.endWith(StrUtil.trim(result), ",")){
                        result = result.substring(0, result.length() - 1);
                    }

                    processConfigInfo.setProcess_condition(ProcessEnums.getNameByCode(split[0]) + result);
                } else if ("13".equals(code) || "14".equals(code)) {
                    String b = split[1];
                    String[] c = b.split("-");
                    String result = "";
                    for (String s : c) {
                        Consumable assetsType = consumableMapper.selectById(s);
                        if (null != assetsType){
                            // 判断是否为最后一个
                            result += assetsType.getConsumable_name() + ",";
                        }
                    }
                    if (StrUtil.endWith(StrUtil.trim(result), ",")){
                        result = result.substring(0, result.length() - 1);
                    }

                    processConfigInfo.setProcess_condition(ProcessEnums.getNameByCode(split[0]) + result);
                }else{
                    processConfigInfo.setProcess_condition(ProcessEnums.getNameByCode(split[0]) + split[1]);
                }
            }else {
                processConfigInfo.setProcess_condition("无");
            }
        }
        return new PageInfo<>(list);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public ResultInfo remove(Integer id) {
        update(null, Wrappers.<ProcessConfigInfo>lambdaUpdate()
                .set(ProcessConfigInfo::getIs_del, G.ISDEL_YES)
                .eq(ProcessConfigInfo::getId, id)
        );
        return ResultInfo.success();
    }
}
