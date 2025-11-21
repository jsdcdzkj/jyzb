package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ConsReceiveAssetsDao;
import com.jsdc.rfid.model.ConsReceiveAssets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.ConsDataVo;

import java.util.List;

@Mapper
public interface ConsReceiveAssetsMapper extends BaseMapper<ConsReceiveAssets> {

    /**
     * 领用排行：柱状图展示领用量TOP5的耗材（按品类）；
     *
     * @Author thr
     */
    @SelectProvider(type = ConsReceiveAssetsDao.class, method = "getTop5ByCategory")
    List<ConsDataVo> getTop5ByCategory(ConsReceiveAssets bean);

    /**
     * 部门领用占比：饼状图展示各部门耗材领用占比；
     *
     * @Author thr
     */
    @SelectProvider(type = ConsReceiveAssetsDao.class, method = "getCountByDept")
    List<ConsDataVo> getCountByDept(ConsReceiveAssets bean);

    /**
     * 以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     * 领用量
     *
     * @Author thr
     */
    @SelectProvider(type = ConsReceiveAssetsDao.class, method = "getApplyNum")
    List<ConsDataVo> getApplyNum(ConsReceiveAssets bean);

}
