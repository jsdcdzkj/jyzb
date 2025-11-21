package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.BrandManageDao;
import com.jsdc.rfid.model.BrandManage;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.ResultInfo;

import java.util.Date;
import java.util.List;

@Service
public class BrandManageService extends BaseService<BrandManageDao, BrandManage> {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 品牌管理 列表查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<BrandManage> toList(Integer pageIndex, Integer pageSize, BrandManage beanParam) {
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<BrandManage> list = selectList(Wrappers.<BrandManage>lambdaQuery()
                .like(StringUtils.isNotBlank(beanParam.getBrand_name()), BrandManage::getBrand_name, beanParam.getBrand_name())
                .like(StringUtils.isNotBlank(beanParam.getBrand_code()), BrandManage::getBrand_code, beanParam.getBrand_code())
                .eq(BrandManage::getIs_del, G.ISDEL_NO)
        );
//                .orderByDesc(BrandManage::getCreate_time));
        return new PageInfo<>(list);
    }

    /**
     * 保存
     * @param bean
     * @return
     */
    public ResultInfo save(BrandManage bean) {
        bean.setIs_del(G.ISDEL_NO);
        bean.setUpdate_time(new Date());
        // 更新
        if (null != bean.getId() && null != selectById(bean.getId())){
            updateById(bean);
        } else {
            bean.setCreate_user(sysUserService.getUser().getId());
            bean.setCreate_time(new Date());
            insert(bean);
        }
        return ResultInfo.success();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public ResultInfo remove(Integer id) {
        BrandManage bean = selectById(id);
        if (null == bean){
            return ResultInfo.error("数据不存在");
        }
        bean.setIs_del(G.ISDEL_YES);
        updateById(bean);
        return ResultInfo.success();
    }
}
