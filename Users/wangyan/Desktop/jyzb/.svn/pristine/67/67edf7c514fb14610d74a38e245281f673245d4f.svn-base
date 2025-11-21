package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.SysPostPermissionDao;
import com.jsdc.rfid.model.SysPermission;
import com.jsdc.rfid.model.SysPostPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SysPostPermissionMapper extends BaseMapper<SysPostPermission> {

    /**
     * 根据当前登录的岗位查询菜单
     * @param post_id
     * @return
     */
    @SelectProvider(type = SysPostPermissionDao.class, method = "selectByPostPermissionCode")
    List<String> selectByPostPermissionCode(String post_id);
}
