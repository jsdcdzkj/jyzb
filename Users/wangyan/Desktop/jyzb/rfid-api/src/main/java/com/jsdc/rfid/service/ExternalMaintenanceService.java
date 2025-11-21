package com.jsdc.rfid.service;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ExternalMaintenanceDao;
import com.jsdc.rfid.mapper.ExternalMaintenanceMapper;
import com.jsdc.rfid.model.ExternalMaintenance;
import com.jsdc.rfid.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ExternalMaintenanceService extends BaseService<ExternalMaintenanceDao, ExternalMaintenance> {

    @Autowired
    private ExternalMaintenanceMapper externalMaintenanceMapper;

    @Autowired
    private SysUserService userService;


    /**
     * 维修单位分页查询
     *
     * @param externalMaintenance
     * @param page
     * @param limit
     * @return
     */
    public Object getPage(ExternalMaintenance externalMaintenance, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<ExternalMaintenance> list = externalMaintenanceMapper.getPage(externalMaintenance);
        return new PageInfo<>(list);
    }

    /**
     * 验证必填项
     * @param externalMaintenance
     * @return
     */
    private Boolean validate(ExternalMaintenance externalMaintenance){
        if(StringUtils.isEmpty(externalMaintenance.getUnitname()) || StringUtils.isEmpty(externalMaintenance.getUnitcode())){
            throw new RuntimeException("单位名称和单位编码不能为空");
        }else{
            if (!CollectionUtils.isEmpty(selectList(Wrappers.<ExternalMaintenance>lambdaQuery()
                    .eq(ExternalMaintenance::getUnitname, externalMaintenance.getUnitname().trim())
                    .ne(null != externalMaintenance.getId(), ExternalMaintenance::getId, externalMaintenance.getId())
                    .eq(ExternalMaintenance::getIs_del, G.ISDEL_NO)))) {
                throw new RuntimeException("单位名称已存在");
            }
            return true;
        }
    }

    public ExternalMaintenance add(ExternalMaintenance externalMaintenance) {
        if(validate(externalMaintenance)){
            SysUser user = userService.getUser();
            externalMaintenance.setCreate_time(new Date());
            externalMaintenance.setCreate_user(user.getId());
            externalMaintenance.setUpdate_time(new Date());
            externalMaintenance.setUpdate_user(user.getId());
            externalMaintenance.setIs_del(G.ISDEL_NO);
            if(insert(externalMaintenance) > 0){
                return externalMaintenance;
            }
        }
        return null;
    }

    public ExternalMaintenance edit(ExternalMaintenance externalMaintenance) {
        if(validate(externalMaintenance)){
            SysUser user = userService.getUser();
            ExternalMaintenance original = selectById(externalMaintenance.getId());
            BeanUtils.copyProperties(externalMaintenance, original);
            original.setUpdate_user(user.getId());
            original.setUpdate_time(new Date());
            if(updateById(original) > 0){
                return original;
            }
        }
        return null;
    }

    public Boolean deleteEm(Integer id) {
        SysUser user = userService.getUser();
        ExternalMaintenance externalMaintenance = selectById(id);
        externalMaintenance.setUpdate_time(new Date());
        externalMaintenance.setUpdate_user(user.getId());
        externalMaintenance.setIs_del(G.ISDEL_YES);
        if(updateById(externalMaintenance) > 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询所有未删除的维修单位
     * @return
     */
    public List<ExternalMaintenance> selectAll() {
        return selectList(Wrappers.<ExternalMaintenance>lambdaQuery()
                .eq(ExternalMaintenance::getIs_del,G.ISDEL_NO));
    }
}
