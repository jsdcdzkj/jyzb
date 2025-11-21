package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.CarryWhiteDao;
import com.jsdc.rfid.mapper.AssetsManageMapper;
import com.jsdc.rfid.mapper.CarryWhiteMapper;
import com.jsdc.rfid.model.AssetsManage;
import com.jsdc.rfid.model.CarryWhite;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2024-02-28 10:42:26
 */
@Service
@Transactional
public class CarryWhiteService extends BaseService<CarryWhiteDao, CarryWhite> {

    @Autowired
    private CarryWhiteMapper carryWhiteMapper;
    @Autowired
    private AssetsManageMapper assetsManageMapper;
    @Autowired
    private SysUserService sysUserService;

    public PageInfo<CarryWhite> toList(Integer pageIndex, Integer pageSize, CarryWhite beanParam) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");

        List<CarryWhite> carryWhiteVos = carryWhiteMapper.selectList(Wrappers.<CarryWhite>query()
                .like(StringUtils.isNotBlank(beanParam.getAsset_code()), "asset_code", beanParam.getAsset_code())
                .like(StringUtils.isNotBlank(beanParam.getAsset_name()), "asset_name", beanParam.getAsset_name())
                .eq("is_del", G.ISDEL_NO)
        );

        return new PageInfo<>(carryWhiteVos);
    }

    public List<CarryWhite> getList(CarryWhite beanParam) {

        return carryWhiteMapper.toList(beanParam);
    }

    /**
     * 保存白名单
     * @param assetIds
     * @return
     */
    public void toSave(List<Integer> assetIds) {

        // 已经存在的白名单
        List<Integer> exitsAssetIds = new ArrayList<>();
        // 1. 如果carryWhite表中已经存在了这个资产的白名单，则不再添加,进行更改操作,white_flag=1
        List<CarryWhite> carryWhites = carryWhiteMapper.selectList(Wrappers.<CarryWhite>lambdaQuery()
                .in(CarryWhite::getAsset_id, assetIds)
                .eq(CarryWhite::getIs_del, G.ISDEL_NO)
        );
        if(!CollectionUtils.isEmpty(carryWhites)){
            assetsManageMapper.selectList(Wrappers.<AssetsManage>lambdaQuery()
                    .in(AssetsManage::getId, assetIds).eq(AssetsManage::getIs_del, G.ISDEL_NO)
            ).forEach(assetsManage -> {
                exitsAssetIds.add(assetsManage.getId());
                carryWhiteMapper.update(null, Wrappers.<CarryWhite>lambdaUpdate()
                        .eq(CarryWhite::getAsset_id, assetsManage.getId())
                        .set(CarryWhite::getWhite_flag, G.ISDEL_YES)
                        .set(CarryWhite::getRfid, assetsManage.getRfid())
                        .set(CarryWhite::getAsset_code, assetsManage.getAsset_code())
                        .set(CarryWhite::getAsset_name, assetsManage.getAsset_name())
                        .set(CarryWhite::getUpdate_time, new Date())
                );
            });
        }

        // 2. 如果carryWhite表中不存在这个资产的白名单，则进行批量添加
        List<Integer> notExitsAssetIds = new ArrayList<>();
        assetIds.forEach(assetId -> {
            if(!exitsAssetIds.contains(assetId)){
                notExitsAssetIds.add(assetId);
            }
        });
        if(!CollectionUtils.isEmpty(notExitsAssetIds)){
            List<AssetsManage> assetsManages = assetsManageMapper.selectList(Wrappers.<AssetsManage>lambdaQuery()
                    .in(AssetsManage::getId, notExitsAssetIds).eq(AssetsManage::getIs_del, G.ISDEL_NO)
            );
            assetsManages.forEach(assetsManage -> {
                CarryWhite carryWhite = new CarryWhite();
                carryWhite.setAsset_id(assetsManage.getId());
                carryWhite.setRfid(assetsManage.getRfid());
                carryWhite.setAsset_code(assetsManage.getAsset_code());
                carryWhite.setAsset_name(assetsManage.getAsset_name());
                carryWhite.setWhite_flag(G.ISDEL_YES);
                carryWhite.setCreate_time(new Date());
                carryWhite.setUpdate_time(new Date());

                carryWhite.setIs_del(G.ISDEL_NO);
                carryWhite.setCreate_user(sysUserService.getUser().getId());
                carryWhiteMapper.insert(carryWhite);
            });

        }
    }
}