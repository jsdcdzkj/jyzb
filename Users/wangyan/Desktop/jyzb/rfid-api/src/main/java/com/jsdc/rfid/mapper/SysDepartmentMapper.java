package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.SysDepartmentDao;
import com.jsdc.rfid.model.SysDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import vo.SysDepartmentVo;

import java.util.List;

@Mapper
public interface SysDepartmentMapper extends BaseMapper<SysDepartment> {

    @SelectProvider( type = SysDepartmentDao.class, method = "getPage")
    List<SysDepartmentVo> getPage(SysDepartment department);
}
