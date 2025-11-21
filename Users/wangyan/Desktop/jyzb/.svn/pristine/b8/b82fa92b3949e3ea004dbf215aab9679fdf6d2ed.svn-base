package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.AssetsAccessRecordDao;
import com.jsdc.rfid.model.AssetsAccessRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.AccessRecordVo;

import java.util.List;

/**
 * @author thr
 * @descript 资产进出记录
 */
@Mapper
public interface AssetsAccessRecordMapper extends BaseMapper<AssetsAccessRecord> {

    //分页查询
    @SelectProvider(method = "selectPageList", type = AssetsAccessRecordDao.class)
    List<AssetsAccessRecord> selectPageList(AssetsAccessRecord bean);

    @SelectProvider(method = "accessRecordTop10", type = AssetsAccessRecordDao.class)
    List<AccessRecordVo> accessRecordTop10();
}
