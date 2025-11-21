package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.ManagementAssetsFileMember;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.ManagementAssetsFileMemberDao;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-08-24 15:50:59
 */
@Mapper
public interface ManagementAssetsFileMemberMapper extends BaseMapper<ManagementAssetsFileMember> {

    @SelectProvider(method = "toList",type = ManagementAssetsFileMemberDao.class)
    List<ManagementAssetsFileMember> toList(ManagementAssetsFileMember beanParam);
}