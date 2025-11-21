package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.AssetsTrajectoryDao;
import com.jsdc.rfid.mapper.AssetsTrajectoryMapper;
import com.jsdc.rfid.model.AssetsTrajectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author thr
 * @descript 资产轨迹
 */
@Service
@Transactional
public class AssetsTrajectoryService extends BaseService<AssetsTrajectoryDao, AssetsTrajectory> {
    @Autowired
    private AssetsTrajectoryMapper AssetsTrajectoryMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDepartmentService departmentService;

    /**
     * 分页查询
     *
     * @param pageIndex 起始页
     * @param pageSize  页大小
     * @param bean      对象参数
     * @return 分页列表数据
     * @author thr
     */
    public PageInfo<AssetsTrajectory> selectPageList(Integer pageIndex, Integer pageSize, AssetsTrajectory bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<AssetsTrajectory> list = AssetsTrajectoryMapper.selectPageList(bean);
        list.forEach(a -> {
//            SysUser sysUser = sysUserService.selectById(a.getUser_Id());
//            a.setUser_name(sysUser.getUser_name());
//            SysDepartment department = departmentService.selectById(a.getDept_Id());
//            a.setDept_name(department.getDept_name());
        });
        return new PageInfo<>(list);
    }

    //列表
    public List<AssetsTrajectory> toList(AssetsTrajectory bean) {
        return AssetsTrajectoryMapper.selectPageList(bean);
    }

    /**
     * 详情数据||修改数据||新增
     * 传递id修改、详情，无id新增操作
     * 读取部分下拉数据
     *
     * @param bean 对象参数
     * @return 对象数据
     * @author thr
     */
    public String selectByAssetsTrajectoryId(AssetsTrajectory bean) {
        JSONObject object = new JSONObject();
        if (Base.notEmpty(bean.getId())) {
            object.put("bean", selectById(bean));
        }
        return object.toJSONString();
    }

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    public Integer deleteAssetsTrajectory(Integer id) {
        AssetsTrajectory bean = selectById(id);
        if (Base.notEmpty(bean)) {
            return updateById(bean);
        }
        return 0;
    }

    /**
     * 保存||修改功能
     *
     * @param bean
     * @return
     */
    public Integer saveAssetsTrajectory(AssetsTrajectory bean) {
        bean.setCreate_time(new Date());
        bean.setCreate_user(1);
        bean.setUpdate_time(new Date());
        bean.setUpdate_user(1);
        //修改功能
        if (Base.notEmpty(bean.getId())) {
            return updateById(bean);
        } else {
            return insert(bean);
        }
    }

    /**
     * 生成位置变更记录
     */
    public Integer onSave(AssetsTrajectory bean) {
        bean.setCreate_time(new Date());
        bean.setCreate_user(1);
        bean.setUpdate_time(new Date());
        bean.setUpdate_user(1);
        bean.setIs_del("0");
        return insert(bean);
    }
}
