package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.SysUserDao;
import com.jsdc.rfid.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import vo.SysUserVo;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yanbin123
 * @since 2019-08-14
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @SelectProvider(type = SysUserDao.class, method = "getList")
    List<SysUserVo> getList(SysUser user);
}
