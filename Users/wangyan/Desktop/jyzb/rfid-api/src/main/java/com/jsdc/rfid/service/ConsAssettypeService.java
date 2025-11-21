package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ConsAssettypeDao;
import com.jsdc.rfid.model.ConsAssettype;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ConsAssettypeService extends BaseService<ConsAssettypeDao, ConsAssettype> {
    /**
     * 列表查询
     * @param assetsType
     * @return
     */
    public List<ConsAssettype> getList(ConsAssettype assetsType){
        LambdaQueryWrapper<ConsAssettype> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConsAssettype::getIs_del, G.ISDEL_NO);


        return selectList(wrapper);
    }


    public void toAdd(ConsAssettype bean) {
        bean.setCreate_time(new Date());
        bean.setIs_del("0");

        bean.insert();
    }
}
