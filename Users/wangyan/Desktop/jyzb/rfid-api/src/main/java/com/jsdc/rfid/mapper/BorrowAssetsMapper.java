package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.BorrowAssetsDao;
import com.jsdc.rfid.dao.BorrowDao;
import com.jsdc.rfid.model.Borrow;
import com.jsdc.rfid.model.BorrowAssets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface BorrowAssetsMapper  extends BaseMapper<BorrowAssets> {

    @SelectProvider(method = "getBorrowAssets", type = BorrowAssetsDao.class)
    List<BorrowAssets> getBorrowAssets(Integer id);


    @SelectProvider(method = "beOverdue", type = BorrowAssetsDao.class)
    List<BorrowAssets> beOverdue(Integer id);
}
