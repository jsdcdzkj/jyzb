package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.SysPostDao;
import com.jsdc.rfid.model.SysPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.SysPostVo;

import java.util.List;

@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {
    @SelectProvider(type = SysPostDao.class, method = "getPage")
    List<SysPostVo> getPage(SysPost post);
}
