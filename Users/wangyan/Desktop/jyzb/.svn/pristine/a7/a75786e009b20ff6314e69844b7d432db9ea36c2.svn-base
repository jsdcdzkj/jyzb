package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ConsReceiveDao;
import com.jsdc.rfid.model.ConsReceive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.ConsDataVo;

import java.util.List;

@Mapper
public interface ConsReceiveMapper extends BaseMapper<ConsReceive> {

    /**
     * 耗材出库总数、申领出库总数
     *
     * @Author thr
     */
    @SelectProvider(method = "getTotalCount", type = ConsReceiveDao.class)
    ConsReceive getTotalCount(ConsReceive beanParam);

    /**
     * 领用趋势：折线图展示近六个月的领用趋势（领用单数、耗材领用总数）；
     *
     * @Author thr
     */
    @SelectProvider(method = "getSixMonthCount", type = ConsReceiveDao.class)
    List<ConsDataVo> getSixMonthCount(ConsReceive vo);

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：根据耗材品类名称展示耗材出库详情分页列表
     * @Date ：2023/3/15 下午 2:31
     * @Modified By：
     */
    @SelectProvider(method = "getPageListByConsCategoryName", type = ConsReceiveDao.class)
    List<ConsReceive> getPageListByConsCategoryName(ConsReceive beanParam, String deptId);
}
