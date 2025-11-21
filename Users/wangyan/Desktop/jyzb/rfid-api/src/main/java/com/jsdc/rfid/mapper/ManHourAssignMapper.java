package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ManHourAssignDao;
import com.jsdc.rfid.dao.ManagementDao;
import com.jsdc.rfid.model.ManHourAssign;
import com.jsdc.rfid.vo.ManHourAssignVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ManHourAssignMapper extends BaseMapper<ManHourAssign> {
    @SelectProvider(method = "getPageList", type = ManHourAssignDao.class)
    List<ManHourAssignVo> getPageList(ManHourAssignVo vo);
}
