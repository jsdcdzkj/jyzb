package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.ConsPurchaseDetailDao;
import com.jsdc.rfid.dao.PurchaseDetailDao;
import com.jsdc.rfid.mapper.ConsPurchaseDetailMapper;
import com.jsdc.rfid.mapper.PurchaseDetailMapper;
import com.jsdc.rfid.model.ConsPurchaseDetail;
import com.jsdc.rfid.model.PurchaseDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vo.ConsDataVo;

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
public class ConsPurchaseDetailService extends BaseService<ConsPurchaseDetailDao, ConsPurchaseDetail> {


    private ConsPurchaseDetailMapper consPurchaseDetailMapper;

    private SysUserService sysUserService;

    /**
     * 采购趋势：折线图展示近六个月的采购趋势（采购单数、耗材总数）
     * 耗材总数
     *
     * @Author thr
     */
    public List<ConsDataVo> getSixMonthCount(ConsPurchaseDetail bean) {
        return consPurchaseDetailMapper.getSixMonthCount(bean);
    }

    /**
     * 采购排行：柱状图展示采购量TOP5的耗材（按品类）；
     *
     * @Author thr
     */
    public List<ConsDataVo> getTop5ByCategory(ConsPurchaseDetail bean) {
        return consPurchaseDetailMapper.getTop5ByCategory(bean);
    }

    public void toAdd(ConsPurchaseDetail purchaseDetail) {
        purchaseDetail.setCreate_time(new Date());
        if(null == purchaseDetail.getUserId()){
            purchaseDetail.setCreate_user(sysUserService.getUser().getId());
        }else{
            purchaseDetail.setCreate_user(purchaseDetail.getUserId());
        }

        purchaseDetail.setIs_del("0");

        purchaseDetail.insert();
    }

    public List<ConsPurchaseDetail> toAll(ConsPurchaseDetail purchaseDetail) {
        QueryWrapper<ConsPurchaseDetail> queryWrapper=new QueryWrapper<>();
        if (purchaseDetail.getPurchase_apply_id()!=null){
            queryWrapper.eq("purchase_apply_id",purchaseDetail.getPurchase_apply_id());
        }

        queryWrapper.eq("is_del","0");

        return selectList(queryWrapper);
    }

    public List<ConsPurchaseDetail> toAssetsName(ConsPurchaseDetail purchaseDetail) {
        if (null==purchaseDetail.getAssets_type_id()){
            return new ArrayList<>();
        }

        QueryWrapper<ConsPurchaseDetail> queryWrapper=new QueryWrapper<>();

        queryWrapper.groupBy("assets_name");

        queryWrapper.having("is_del","0");



        return selectList(queryWrapper);

    }

}
