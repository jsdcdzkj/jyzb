package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.WarehousingPurchaseMember;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.WarehousingPurchaseMemberDao;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Mapper
public interface WarehousingPurchaseMemberMapper extends BaseMapper<WarehousingPurchaseMember> {

    @SelectProvider(method = "toList",type = WarehousingPurchaseMemberDao.class)
    List<WarehousingPurchaseMember> toList(WarehousingPurchaseMember beanParam);
}