package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ConsCategoryDao;
import com.jsdc.rfid.model.ConsCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ConsCategoryService extends BaseService<ConsCategoryDao, ConsCategory> {

    /**
     * 列表查询
     * @param consCategory
     * @return
     */
    public List<ConsCategory> getList(ConsCategory consCategory){
        LambdaQueryWrapper<ConsCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConsCategory::getIs_del, G.ISDEL_NO);


        return selectList(wrapper);
    }

    public void toAdd(ConsCategory build) {
        build.setIs_del("0");
        build.setCreate_time(new Date());
        build.insert();
    }
}
