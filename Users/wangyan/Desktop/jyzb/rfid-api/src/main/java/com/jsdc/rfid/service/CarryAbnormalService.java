package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.CarryAbnormalDao;
import com.jsdc.rfid.mapper.CarryAbnormalMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.vo.CarryManaveVo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zln
 * @descript 外携异常管理
 * @date 2022-04-24
 */
@Slf4j
@Service
@Transactional
public class CarryAbnormalService extends BaseService<CarryAbnormalDao, CarryAbnormal> {
    @Autowired
    private CarryAbnormalMapper carryAbnormalMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private EquipmentService equipmentService;

    /**
     * 分页查询
     *
     * @param pageIndex 起始页
     * @param pageSize  页大小
     * @param bean      对象参数
     * @return 分页列表数据
     * @author zln
     */
    public PageInfo selectPageList(Integer pageIndex, Integer pageSize, CarryAbnormal bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<CarryAbnormal> list = carryAbnormalMapper.selectPageList(bean);
        list.forEach(a -> {
            if (Base.notEmpty(a.getUser_id())) {
                SysUser sysUser = sysUserService.selectById(a.getUser_id());
                if (null != sysUser){
                    a.setUser_name(sysUser.getUser_name());
                }
            }
            if (Base.notEmpty(a.getDept_id())) {
                SysDepartment department = departmentService.selectById(a.getDept_id());
                a.setDept_name(null == department?"":department.getDept_name());
            }
        });
        return new PageInfo<>(list);
    }

    public void selectPageListWrappers(){}

    public List<CarryAbnormal> fiveMinData(CarryAbnormal bean) {
        List<CarryAbnormal> list = carryAbnormalMapper.selectList(Wrappers.<CarryAbnormal>query()
                .eq("is_del",G.ISDEL_NO)
                .eq("error_status", G.ISDEL_YES)
                // 5分钟内的数据
                .ge("carry_time", new DateTime().minusMinutes(5).toString("yyyy-MM-dd HH:mm:ss"))
                // 根据carry_time倒序
                .last(" order by to_char(carry_time) desc ")
        );
        for (CarryAbnormal a : list) {
            if (Base.notEmpty(a.getUser_id())) {
                SysUser sysUser = sysUserService.selectById(a.getUser_id());
                if (null != sysUser){
                    a.setUser_name(sysUser.getUser_name());
                }
            }
            if (Base.notEmpty(a.getDept_id())) {
                SysDepartment department = departmentService.selectById(a.getDept_id());
                a.setDept_name(null == department?"":department.getDept_name());
            }
            // 发现异常外携，资产名称：笔记本电脑，使用人：张德全，请尽快处理！ 通知话术
            a.setNotice("发现异常外携，资产名称：" + a.getAssetname() + "，使用人：" + (Base.notEmpty(a.getUser_name())?a.getUser_name():"未知") + "，请尽快处理！");
        };
        return list;
    }

    // 查询最近3分钟有没有异常数据, 返回1证明有异常数据, 返回0证明没有异常数据
    public Integer threeMinData() {
        try {
            List<CarryAbnormal> list = carryAbnormalMapper.selectList(Wrappers.<CarryAbnormal>query()
                    .eq("is_del",G.ISDEL_NO)
                    .eq("error_status", G.ISDEL_YES)
                    // 3分钟内的数据
                    .ge("carry_time", new DateTime().minusMinutes(3).toString("yyyy-MM-dd HH:mm:ss"))
                    // 3分钟内的数据
                    .last(" order by to_char(carry_time) desc ")
            );
            if (!CollectionUtils.isEmpty(list)) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error("查询最近3分钟有没有异常数据异常", e);
            return 0;
        }
    }

    // 构造查询
//    public List<CarryAbnormal> selectPageList(CarryAbnormal bean) {
//        List<CarryAbnormal> list = carryAbnormalMapper.selectList(Wrappers.<CarryAbnormal>lambdaQuery()
//                .eq(CarryAbnormal::getIs_del, G.ISDEL_NO)
//        );
//        return list;
//    }

    public PageInfo getPageList(Integer pageIndex, Integer pageSize, CarryAbnormal bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<CarryAbnormal> list = carryAbnormalMapper.getPageList(bean);
//        list.forEach(a->{
//            SysUser sysUser = sysUserService.selectById(a.getUser_Id());
//            a.setUser_name(sysUser.getUser_name());
//            SysDepartment department = departmentService.selectById(a.getDept_Id());
//            a.setDept_name(department.getDept_name());
//        });
        return new PageInfo<>(list);
    }

    //列表
    public List<CarryAbnormal> toList(CarryAbnormal bean) {
        return carryAbnormalMapper.selectPageList(bean);
    }

    /**
     * 详情数据||修改数据||新增
     * 传递id修改、详情，无id新增操作
     * 读取部分下拉数据
     *
     * @param bean 对象参数
     * @return 对象数据
     * @author zln
     */
    public String selectByCarryAbnormalId(CarryAbnormal bean) {
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
    public Integer deleteCarryAbnormal(Integer id) {
        CarryAbnormal bean = selectById(id);
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
    public Integer saveCarryAbnormal(CarryAbnormal bean) {
        bean.setCreation_time(new Date());
        bean.setCreation_user(1);
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
     * 处理报警
     */
    public Integer warningUpd(CarryAbnormal bean) {
        bean.setHandle_time(new Date());
        bean.setHandle_user(sysUserService.getUser().getId());
        bean.setUpdate_time(new Date());
        bean.setUpdate_user(sysUserService.getUser().getId());
        return updateById(bean);
    }

    /**
     * 根据存放位置查询报警信息是否存在未处理的数据
     */
    public List<CarryAbnormal> getListByPositionId(CarryAbnormal bean) {
        LambdaQueryWrapper<CarryAbnormal> wrappers = Wrappers.<CarryAbnormal>lambdaQuery();
        // 存放位置
        wrappers.eq(null != bean.getChange_position_id(), CarryAbnormal::getChange_position_id, bean.getChange_position_id());
        //未处理
        wrappers.eq(CarryAbnormal::getIs_handle, 0);
        return selectList(wrappers.eq(CarryAbnormal::getIs_del, 0));
    }

    /**
     * 生成外携报警记录
     */
    public CarryAbnormal onSave(CarryAbnormal bean, Equipment equipment2) {
        LambdaQueryWrapper<AssetsManage> wrappers = Wrappers.<AssetsManage>lambdaQuery();
        if (StringUtils.isNotEmpty(bean.getRfid())) {
            wrappers.eq(AssetsManage::getRfid, bean.getRfid().toLowerCase());
        }
        wrappers.eq(AssetsManage::getIs_del, G.ISDEL_NO);
        AssetsManage assetsManage = assetsManageService.selectOne(wrappers);

        bean.setRfid(assetsManage.getRfid());
        //资产id
        bean.setAssets_id(assetsManage.getId());
        //资产编号
        bean.setAssetnumber(assetsManage.getAsset_code());
        //规格型号
        bean.setSpecification(assetsManage.getSpecification());
        //资产名称
        bean.setAssetname(assetsManage.getAsset_name());
        //使用人
        bean.setUser_id(assetsManage.getUse_people());
        //存放部门
        bean.setDept_id(assetsManage.getDept_id());
        //存放位置
        bean.setPosition_id(assetsManage.getPosition_id());
        //外携时间
        bean.setCarry_time(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        //是否返回
        bean.setIs_repaid("0");
        //是否处理报警异常 0否（默认） 1是
        bean.setIs_handle("0");
        bean.setCreation_time(new Date());
        bean.setCreation_user(1);
        bean.setUpdate_time(new Date());
        bean.setUpdate_user(1);
        //逻辑删除 0否 1是
        bean.setIs_del("0");

        //修改报警设备状态
        if (Base.notEmpty(equipment2)) {
            //报警状态 1正常 2报警
            equipment2.setWarning_status(2);
            equipmentService.updateById(equipment2);
        }

        // 使用websorket推送报警信息
        //WebSocketServer.sendInfo("有新客户呼入,sltAccountId:" + bean.getRFID());

        insert(bean);
        return bean;
    }

    /**
     * 生成位置变动报警记录
     */
    public Integer onChangeSave(CarryAbnormal bean) {
        bean.setCreation_time(new Date());
        bean.setCreation_user(1);
        bean.setUpdate_time(new Date());
        bean.setUpdate_user(1);
        //逻辑删除 0否 1是
        bean.setIs_del("0");
        //是否返回
        bean.setIs_repaid("0");
        return insert(bean);
    }


    //异常
    public JSONObject selectAbnormalCountTop10() {
        JSONObject jsonObject = new JSONObject();
        List<CarryManaveVo> list = carryAbnormalMapper.selectAbnormalCount();
        List<String> xData = new ArrayList<>();
        List<Long> yData = new ArrayList<>();
        //资产品类id集合
        List<Integer> typeIdData = new ArrayList<>();
        for (CarryManaveVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getValue());
            typeIdData.add(vo1.getTypeId());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        jsonObject.put("typeIdData", typeIdData);
        return jsonObject;
    }


    public PageInfo selectAbnormalDataCountPage(Integer pageIndex, Integer pageSize, Integer asset_type_id) {
        PageHelper.startPage(pageIndex, pageSize);
        List<CarryManaveVo> list = carryAbnormalMapper.selectAbnormalDataCountPage(asset_type_id);
        return new PageInfo<>(list);
    }
}
