package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ConsSpecificationDao;
import com.jsdc.rfid.model.ConsSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ConsSpecificationService extends BaseService<ConsSpecificationDao, ConsSpecification> {
    /**
     * 列表查询
     * @param consSpecification
     * @return
     */
    public List<ConsSpecification> getList(ConsSpecification consSpecification){
        LambdaQueryWrapper<ConsSpecification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConsSpecification::getIs_del, G.ISDEL_NO);


        return selectList(wrapper);
    }

    public void toAdd(ConsSpecification build) {
        build.setCreate_time(new Date());
        build.setIs_del("0");
        build.insert();
    }
}
