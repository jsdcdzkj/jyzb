package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.PurchaseDetailDao;
import com.jsdc.rfid.mapper.PurchaseDetailMapper;
import com.jsdc.rfid.model.PurchaseDetail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: PurchaseDetailService
 * Description:
 * date: 2022/4/24 17:01
 *
 * @author bn
 */
@Transactional
@Service
@AllArgsConstructor
public class PurchaseDetailService extends BaseService<PurchaseDetailDao, PurchaseDetail> {


    private PurchaseDetailMapper purchaseDetailMapper;
    private SysUserService sysUserService;

    public void toAdd(PurchaseDetail purchaseDetail) {
        purchaseDetail.setCreate_time(new Date());
        if (Base.notEmpty(purchaseDetail.getUserId())) {
            purchaseDetail.setCreate_user(purchaseDetail.getUserId());
        } else {
            purchaseDetail.setCreate_user(sysUserService.getUser().getId());
        }
        purchaseDetail.setIs_del("0");

        purchaseDetail.insert();
    }

    public List<PurchaseDetail> toAll(PurchaseDetail purchaseDetail) {
        QueryWrapper<PurchaseDetail> queryWrapper = new QueryWrapper<>();
        if (purchaseDetail.getPurchase_apply_id() != null) {
            queryWrapper.eq("purchase_apply_id", purchaseDetail.getPurchase_apply_id());
        }

        queryWrapper.eq("is_del", "0");

        return selectList(queryWrapper);
    }

    public List<PurchaseDetail> toAssetsName(PurchaseDetail purchaseDetail) {
        if (null == purchaseDetail.getAssets_type_id()) {
            return new ArrayList<>();
        }

        QueryWrapper<PurchaseDetail> queryWrapper = new QueryWrapper<>();

        queryWrapper.groupBy("assets_name");

        queryWrapper.having("is_del", "0");


        return selectList(queryWrapper);

    }
}
