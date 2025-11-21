package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.PurchaseLogDao;
import com.jsdc.rfid.mapper.PurchaseLogMapper;
import com.jsdc.rfid.model.PurchaseLog;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * ClassName: PurchaseLogService
 * Description:
 * date: 2022/4/24 17:11
 *
 * @author bn
 */
@Transactional
@Service
@AllArgsConstructor
public class PurchaseLogService extends BaseService<PurchaseLogDao,PurchaseLog> {


    private PurchaseLogMapper purchaseLogMapper;


    public List<PurchaseLog> toAll(PurchaseLog purchaseLog) {
        QueryWrapper<PurchaseLog> queryWrapper=new QueryWrapper<>();
        if (null==purchaseLog.getPurchase_apply_id()){
            queryWrapper.eq("purchase_apply_id",purchaseLog.getPurchase_apply_id());
        }
        queryWrapper.eq("is_del","0");

        return selectList(queryWrapper);
    }
}
