package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.AssetsFileMember;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.AssetsFileMemberDao;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-04-26 16:11:36
 */
@Mapper
public interface AssetsFileMemberMapper extends BaseMapper<AssetsFileMember> {

    @SelectProvider(method = "toList",type = AssetsFileMemberDao.class)
    List<AssetsFileMember> toList(AssetsFileMember beanParam);
}