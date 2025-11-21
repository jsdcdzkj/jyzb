package com.jsdc.rfid.service;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.AssetsAccessRecordDao;
import com.jsdc.rfid.mapper.AssetsAccessRecordMapper;
import com.jsdc.rfid.model.AssetsAccessRecord;
import com.jsdc.rfid.model.AssetsManage;
import com.jsdc.rfid.model.Equipment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.AccessRecordVo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author thr
 * @descript 资产进出记录
 */
@Service
@Transactional
public class AssetsAccessRecordService extends BaseService<AssetsAccessRecordDao, AssetsAccessRecord> {
    @Autowired
    private AssetsAccessRecordMapper assetsAccessRecordMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private CarryManageService carryManageService;

    /**
     * 分页查询
     *
     * @param pageIndex 起始页
     * @param pageSize  页大小
     * @param bean      对象参数
     * @return 分页列表数据
     * @author thr
     */
    public PageInfo<AssetsAccessRecord> selectPageList(Integer pageIndex, Integer pageSize, AssetsAccessRecord bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<AssetsAccessRecord> list = assetsAccessRecordMapper.selectPageList(bean);
        list.forEach(a -> {
            if (a.getAccessstatus().equals(G.STATUS_IN)) {
                a.setAccess_status_name("进");
            } else {
                a.setAccess_status_name("出");
            }
//            SysUser sysUser = sysUserService.selectById(a.getUser_Id());
//            a.setUser_name(sysUser.getUser_name());
//            SysDepartment department = departmentService.selectById(a.getDept_Id());
//            a.setDept_name(department.getDept_name());
        });
        return new PageInfo<>(list);
    }

    //列表
    public List<AssetsAccessRecord> toList(AssetsAccessRecord bean) {
        return assetsAccessRecordMapper.selectPageList(bean);
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
    public String selectByAssetsAccessRecordId(AssetsAccessRecord bean) {
        JSONObject object = new JSONObject();
        if (Base.notEmpty(bean.getId())) {
            object.put("bean", selectById(bean));
        }
        return JSONObject.toJSONString(object);
    }

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    public Integer deleteAssetsAccessRecord(Integer id) {
        AssetsAccessRecord bean = selectById(id);
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
    public Integer saveAssetsAccessRecord(AssetsAccessRecord bean) {
        bean.setCreatetime(new Date());
        bean.setCreateuser(1);
        bean.setUpdatetime(new Date());
        bean.setUpdateuser(1);
        //修改功能
        if (Base.notEmpty(bean.getId())) {
            return updateById(bean);
        } else {
            return insert(bean);
        }
    }

    //生成资产进出记录
    public Integer onSave(AssetsAccessRecord bean, Equipment equipment) {
        LambdaQueryWrapper<AssetsManage> wrapper = new LambdaQueryWrapper<>();
        if (Base.notEmpty(bean)) {
            if (Base.notEmpty(bean.getAssetepc())) {
                wrapper.eq(AssetsManage::getRfid, bean.getAssetepc().toLowerCase());
            }
        }
        wrapper.eq(AssetsManage::getIs_del, G.ISDEL_NO);
        List<AssetsManage> assetsManages = assetsManageService.selectList(wrapper);
        if (!CollectionUtils.isEmpty(assetsManages)) {
            for (AssetsManage assetsManage : assetsManages) {
                // 资产id
                bean.setAssetid(assetsManage.getId());
                // 资产编号
                bean.setAssetcode(assetsManage.getAsset_code());
                // 资产名称
                bean.setAssetname(assetsManage.getAsset_name());
                if (Base.notEmpty(equipment)){
                    bean.setEquipmentid(equipment.getId());
                    bean.setPositionid(equipment.getEquipment_position());
                }
                bean.setUserid(assetsManage.getUse_people());
                //进出状态 1进 2出
                if (StringUtils.isNotBlank(bean.getAccessstatus())) {
                    // 转小写c
                    String epc = bean.getAssetepc().toLowerCase();
                    if (bean.getAccessstatus().equals("1")) {
                        //进入记录查询
                        carryManageService.inSelectByEpc(epc, equipment);
                    } else {
                        //外出记录查询
                        carryManageService.outSelectByEpc(epc, equipment);
                    }
                }
            }
        }
        bean.setCreatetime(new Date());
        bean.setCreateuser(1);
        bean.setUpdatetime(new Date());
        bean.setUpdateuser(1);
        bean.setIsdel("0");
        return insert(bean);
    }

    /**
     * 查询 出入排行TOP10
     * @return
     */
    public JSONObject accessRecordTop10(){
        List<AccessRecordVo> accessRecordVos = assetsAccessRecordMapper.accessRecordTop10();
        List<String> xValue = accessRecordVos.stream().map(AccessRecordVo::getName).collect(Collectors.toList());
        List<Long> yValue = accessRecordVos.stream().map(AccessRecordVo::getTotal).collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("xValue", xValue);
        jsonObject.put("yValue", yValue);
        return jsonObject;
    }
}
