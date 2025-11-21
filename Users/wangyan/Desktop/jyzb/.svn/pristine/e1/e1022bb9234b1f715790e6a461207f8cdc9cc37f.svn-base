package com.jsdc.rfid.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.ConsReceiveAssetsDao;
import com.jsdc.rfid.mapper.ConsReceiveAssetsMapper;
import com.jsdc.rfid.model.ConsReceiveAssets;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.ConsDataVo;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ConsReceiveAssetsService extends BaseService<ConsReceiveAssetsDao, ConsReceiveAssets> {

    private ConsReceiveAssetsMapper consReceiveAssetsMapper;

    /**
     * 领用排行：柱状图展示领用量TOP5的耗材（按品类）；
     *
     * @Author thr
     */
    public List<ConsDataVo> getTop5ByCategory(ConsReceiveAssets bean) {
        return consReceiveAssetsMapper.getTop5ByCategory(bean);
    }

    /**
     * 部门领用占比：饼状图展示各部门耗材领用占比；
     *
     * @Author thr
     */
    public List<ConsDataVo> getCountByDept(ConsReceiveAssets bean) {
        return consReceiveAssetsMapper.getCountByDept(bean);
    }

    /**
     * 以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     * 领用量
     *
     * @Author thr
     */
    public List<ConsDataVo> getApplyNum(ConsReceiveAssets bean) {
        return consReceiveAssetsMapper.getApplyNum(bean);
    }

}
