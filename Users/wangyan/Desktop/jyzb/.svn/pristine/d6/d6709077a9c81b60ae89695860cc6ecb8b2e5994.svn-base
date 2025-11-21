package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.InventoryWarehousingMember;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.InventoryWarehousingMemberDao;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Mapper
public interface InventoryWarehousingMemberMapper extends BaseMapper<InventoryWarehousingMember> {

    @SelectProvider(method = "toList",type = InventoryWarehousingMemberDao.class)
    List<InventoryWarehousingMember> toList(InventoryWarehousingMember beanParam);
}