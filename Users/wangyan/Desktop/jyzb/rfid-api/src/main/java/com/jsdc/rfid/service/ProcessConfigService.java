package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ProcessConfigDao;
import com.jsdc.rfid.model.ProcessConfig;
import com.jsdc.rfid.model.ProcessConfigInfo;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vo.ResultInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProcessConfigService extends BaseService<ProcessConfigDao, ProcessConfig> {

    @Autowired
    private ProcessConfigInfoService processConfigInfoService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表查询
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    public PageInfo<ProcessConfig> toList(Integer pageIndex, Integer pageSize, ProcessConfig beanParam) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<ProcessConfig> processConfigs = selectList(Wrappers.<ProcessConfig>lambdaQuery()
                .eq(ProcessConfig::getIs_del, G.ISDEL_NO)
                .like(StringUtils.isNotBlank(beanParam.getProcess_name()), ProcessConfig::getProcess_name, beanParam.getProcess_name())
                .like(StringUtils.isNotBlank(beanParam.getProcess_code()), ProcessConfig::getProcess_code, beanParam.getProcess_code())
        );
        for (ProcessConfig processConfig : processConfigs) {
            List<ProcessConfigInfo> infoList = processConfigInfoService.getInfosByProcessId(processConfig.getId());
            processConfig.setProcessConfigInfos(CollectionUtils.isEmpty(infoList) ? new ArrayList<>() : infoList);
            processConfig.setIsNew(CollectionUtils.isEmpty(infoList) ? 0 : 1);
        }
        return new PageInfo<>(processConfigs);
    }

    /**
     * 保存
     * @param bean
     * @return
     */
    public ResultInfo save(ProcessConfig bean) {
        bean.setIs_del(G.ISDEL_NO);
        bean.setUpdate_time(new Date());
        // 判断key是否重复
        ProcessConfig processConfig = selectOne(Wrappers.<ProcessConfig>lambdaQuery()
                .eq(ProcessConfig::getIs_del, G.ISDEL_NO)
                .eq(ProcessConfig::getProcess_code, bean.getProcess_code())
                .ne(null != bean.getId(), ProcessConfig::getId, bean.getId())
        );
        if (null != processConfig){
            return ResultInfo.error("流程编码重复");
        }
        // 更新
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
     * 删除
     * @param id
     * @return
     */
    public ResultInfo remove(Integer id) {
        ProcessConfig bean = selectById(id);
        if (null == bean){
            return ResultInfo.error("数据不存在");
        }
        bean.setIs_del(G.ISDEL_YES);
        updateById(bean);

        // 删除流程配置信息
        processConfigInfoService.removeInfoByProcessId(id);
        return ResultInfo.success();
    }

    /**
     * 根据类型获取配置
     * @param code
     */
    public ProcessConfig getConfigByType(String code) {
        ProcessConfig processConfig = selectOne(Wrappers.<ProcessConfig>lambdaQuery()
                .eq(ProcessConfig::getIs_del, G.ISDEL_NO)
                .eq(ProcessConfig::getProcess_code, code)
        );
        if (null == processConfig){
            return null;
        }
        List<ProcessConfigInfo> infoList = processConfigInfoService.getInfosByProcessId(processConfig.getId());
        processConfig.setProcessConfigInfos(CollectionUtils.isEmpty(infoList) ? new ArrayList<>() : infoList);
        processConfig.setIsNew(CollectionUtils.isEmpty(infoList) ? 0 : 1);
        return processConfig;
    }
}
