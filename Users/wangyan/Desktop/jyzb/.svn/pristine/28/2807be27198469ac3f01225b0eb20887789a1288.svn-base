package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.BorrowDao;
import com.jsdc.rfid.dao.ReceiveDao;
import com.jsdc.rfid.model.Borrow;
import com.jsdc.rfid.model.Receive;
import com.jsdc.rfid.model.ReceiveAssets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface BorrowMapper extends BaseMapper<Borrow> {

    @SelectProvider(method = "getPageInfo", type = BorrowDao.class)
    List<Borrow> getPageInfo(Borrow borrow);


    @SelectProvider(method = "collectionBorrowByPage", type = BorrowDao.class)
    List<Borrow> collectionBorrowByPage(Borrow borrow , Integer userId ,Integer department_id );

}
