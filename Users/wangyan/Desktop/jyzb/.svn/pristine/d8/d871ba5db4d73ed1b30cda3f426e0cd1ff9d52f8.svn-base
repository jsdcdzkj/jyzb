package com.jsdc.rfid.service;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.CarryManageDao;
import com.jsdc.rfid.mapper.CarryAbnormalMemberMapper;
import com.jsdc.rfid.mapper.CarryManageMapper;
import com.jsdc.rfid.mapper.SysPositionMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.vo.CarryManaveVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author zln
 * @descript 外携申请管理
 * @date 2022-04-24
 */
@Service
@Transactional
public class CarryManageService extends BaseService<CarryManageDao, CarryManage> {

    @Autowired
    private CarryManageMapper mapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CarryAbnormalService carryAbnormalService;
    @Autowired
    private AssetsManageService assetsManageService;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private OperationRecordService operationRecordService;

    @Autowired
    private SysPostService sysPostService;

    /**
     * 分页查询
     *
     * @param pageIndex 起始页
     * @param pageSize  页大小
     * @param bean      对象参数
     * @return 分页列表数据
     * @author zln
     */
    public PageInfo selectPageList(Integer pageIndex, Integer pageSize, CarryManage bean) {
        // 获取当前登录用户
        SysUser current;
        if (Base.empty(bean.getWx_userId())) {
            current = sysUserService.getUser();
        } else {
            current = sysUserService.selectById(bean.getWx_userId());
        }
        if (null == bean|| null == bean.getIs_adopt() || 0 == bean.getIs_adopt()){
            SysPost sysPost = sysPostService.selectById(current.getPost());

            if (sysPost.getData_permission() == G.DATAPERMISSION_PERSONAL) {
                bean.setCreation_user(current.getId());
            } else if (sysPost.getData_permission() == G.DATAPERMISSION_DEPT) {
                bean.setDeptManager(current.getDepartment());
            }
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<CarryManage> list = mapper.selectPageList(bean);
        for (CarryManage temp : list) {
            processMemberService.getProcessDataByBusId(temp.getId(), G.PROCESS_ZCWX,current, temp);
        }
        return new PageInfo<>(list);
    }

    /**
     * 已审批
     *
     * @return
     */
    public PageInfo finishAdopt(Integer pageIndex, Integer pageSize, CarryManage bean) {
        PageHelper.startPage(pageIndex, pageSize);
        if (!CollectionUtils.isEmpty(bean.getIds())){
            // 将ids去重
            bean.setIds(bean.getIds().stream().distinct().collect(Collectors.toList()));
        }
        List<CarryManage> list = mapper.selectPageList(CarryManage.builder()
                .ids(bean.getIds())
                .assetname(bean.getAssetname())
                .assetnumber(bean.getAssetnumber())
                .transfer(bean.getTransfer())
                .carry_name(bean.getCarry_name())
                .timeStr(bean.getTimeStr())
                .numbering(bean.getNumbering())
                .build());
        return new PageInfo<>(list);
    }

    public List<CarryManage> getList(CarryManage bean) {
        List<CarryManage> list = mapper.selectPageList(bean);
        return list;
    }

    /**
     * 审批方法
     * <p>
     * bean.id
     * bean.approval_state ( 0.待审批 1.审批拒绝  2.审批通过
     *
     * @return
     */
    public Integer updateState(CarryManage bean) {
        CarryManage carryManage = selectById(bean);
        carryManage.setApproval_state(bean.getApproval_state());
        return updateById(carryManage);
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
    public String selectByCarryManageId(CarryManage bean) {
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
    public Integer deleteCarryManage(Integer id) {
        CarryManage carryManage = selectById(id);
        if (Base.notEmpty(carryManage)) {
            carryManage.setIs_del("1");
            return updateById(carryManage);
        }
        return 0;
    }

    /**
     * 保存||修改功能
     *
     * @param bean
     * @return
     */
    public Integer saveCarryManage(CarryManage bean) {
        if (Base.empty(bean.getAsset_manage_id())) {
            throw new RuntimeException("请选择资产");
        }
        AssetsManage assetsManage = assetsManageService.selectById(bean.getAsset_manage_id());
        if (Base.notEmpty(assetsManage)) {
            bean.setRfid(assetsManage.getRfid());
        }
        Integer userId;
        if (Base.notEmpty(bean.getWx_userId())) {
            bean.setCreation_user(bean.getWx_userId());
            bean.setUpdate_user(bean.getWx_userId());
            userId = bean.getWx_userId();
        } else {
            bean.setCreation_user(sysUserService.getUser().getId());
            bean.setUpdate_user(sysUserService.getUser().getId());
            userId = sysUserService.getUser().getId();
        }
        bean.setUpdate_time(new Date());
        bean.setIs_del("0");
        bean.setOverdue("1");
        bean.setCarry_state("0");
        // 是否需要送审
        boolean isNeedApproval = StringUtils.equals(bean.getApproval_state(), "0");
        // 新增判断条件
        if (Base.empty(bean.getId())) {
            bean.setCreation_time(new Date());
            List<CarryManage> carryManages = mapper.selectList(Wrappers.<CarryManage>lambdaQuery().eq(CarryManage::getIs_del, G.ISDEL_NO)
                    .eq(CarryManage::getAsset_manage_id, bean.getAsset_manage_id())
                    .eq(CarryManage::getApproval_state, bean.getApproval_state())
            );
            // 如果数据为空,证明没有申请资产重复的数据,可以新增
            if (CollectionUtils.isEmpty(carryManages)) {
                int i = insert(bean);
                if (isNeedApproval) {
                    processMemberService.startProcess(G.PROCESS_ZCWX, bean.getId(), userId);
                }
                return i;
            }
            for (CarryManage carryManage : carryManages) {
                // 如果申请时间小于等于预计归还时间,判断申请时间是否大于原申请时间
                if (bean.getApp_time().getTime() <= carryManage.getEstimate_time().getTime()) {
                    if (bean.getApp_time().getTime() > carryManage.getApp_time().getTime()) {
                        throw new RuntimeException("该资产已申请外携,请勿重复申请");
                    } else {
                        //判断预计归还时间是否大于原申请时间
                        if (bean.getEstimate_time().getTime() > carryManage.getApp_time().getTime()) {
                            throw new RuntimeException("该资产已申请外携,请勿重复申请");
                        }
                    }
                } else {
                    if (bean.getApp_time().getTime() < carryManage.getEstimate_time().getTime()) {
                        throw new RuntimeException("该资产已申请外携,请勿重复申请");
                    }
                }
            }
            int i = insert(bean);
            if (isNeedApproval) {
                processMemberService.startProcess(G.PROCESS_ZCWX, bean.getId(), userId);
            }
            return i;
        }
        List<CarryManage> carryManages = mapper.selectByDetails(userId, bean.getApproval_state());
        if (carryManages.size() > 0) {
            int i = updateById(bean);
            if (isNeedApproval) {
                processMemberService.startProcess(G.PROCESS_ZCWX, bean.getId(), userId);
            }
            return i;
        } else {
            //修改功能
            if (Base.notEmpty(bean.getId())) {
                int i = updateById(bean);
                if (isNeedApproval) {
                    processMemberService.startProcess(G.PROCESS_ZCWX, bean.getId(), userId);
                }
                return i;
            } else {
                bean.setCarry_state("0");
                int i = insert(bean);
                if (isNeedApproval) {
                    processMemberService.startProcess(G.PROCESS_ZCWX, bean.getId(), userId);
                }
                return i;
            }
        }
    }

    /**
     * 查询外携数据
     * 根据id和外携状态、外携审批通过的数据
     *
     * @param id
     */
    public void selectByDetails(Integer id, Integer user_Id, String approval_state) {
        List<CarryManage> list = mapper.selectByDetails(id, approval_state);
        list.forEach(a -> {
            //逾期未归(0.已归还 1.未归还)
            a.setOverdue("1");
            updateById(a);
        });
    }

    /**
     * 进入记录
     * 根据资产epc标签查询外携监管数据
     */
    public void inSelectByEpc(String epc, Equipment equipment) {
        if (Base.notEmpty(epc)) {
            //外携监管数据查询
            LambdaQueryWrapper<CarryManage> wrapper = new LambdaQueryWrapper<>();
            if (Base.notEmpty(epc)) {
                wrapper.eq(CarryManage::getRfid, epc);
            }
            wrapper.eq(CarryManage::getIs_del, G.ISDEL_NO);
            wrapper.orderByDesc(CarryManage::getId);
            wrapper.last("LIMIT 1");
            CarryManage carryManage = selectOne(wrapper);

            //查询异常外携记录
            LambdaQueryWrapper<CarryAbnormal> wrapper2 = new LambdaQueryWrapper<>();
            if (Base.notEmpty(epc)) {
                wrapper2.eq(CarryAbnormal::getRfid, epc);
            }
            wrapper2.eq(CarryAbnormal::getIs_del, G.ISDEL_NO);
            wrapper2.orderByDesc(CarryAbnormal::getId);
            wrapper2.last("LIMIT 1");
            CarryAbnormal carryAbnormal = carryAbnormalService.selectOne(wrapper2);

            //若存在记录
            if (Base.notEmpty(carryManage)) {
                //判断外携单外携状态
                if (carryManage.getCarry_state().equals("1")) {
                    //外携状态（1.已外携、0.未外携）
                    carryManage.setCarry_state("0");
                    carryManage.setActual_time(new Date());
                    updateById(carryManage);
                } else {
                    //若状态为为外携，则查询有无异常外携记录
                    if (Base.notEmpty(carryAbnormal)) {
                        // 更新设备位置
                        if (Base.notEmpty(equipment)) {
                            carryAbnormal.setEquipment_id(equipment.getId());
                            carryAbnormal.setChange_position_id(equipment.getEquipment_position());
                        }
                        //更新人
                        carryAbnormal.setUser_id(carryManage.getCarry_id());
                        if (carryAbnormal.getIs_repaid().equals("0")) {
                            //是否返回 0否 1是
                            carryAbnormal.setIs_repaid("1");
                        }
                        carryAbnormalService.updateById(carryAbnormal);
                    }
                }
            } else {
                //无记录 查询异常外携记录
                //有无异常外携记录
                if (Base.notEmpty(carryAbnormal)) {
                    if (carryAbnormal.getIs_repaid().equals("0")) {
                        // 更新设备位置
                        if (Base.notEmpty(equipment)) {
                            carryAbnormal.setEquipment_id(equipment.getId());
                            carryAbnormal.setChange_position_id(equipment.getEquipment_position());
                        }
                        //是否返回 0否 1是
                        carryAbnormal.setIs_repaid("1");
                        carryAbnormalService.updateById(carryAbnormal);
                    }
                }
            }
        }
    }

    @Autowired
    private CarryAbnormalMemberMapper carryAbnormalMemberMapper;

    @Autowired
    private SysPositionMapper sysPositionMapper;
    /**
     * 外出记录
     * 根据资产epc标签查询外携监管数据
     */
    public void outSelectByEpc(String epc, Equipment equipment) {
        if (Base.notEmpty(epc)) {
            //有效外携监管数据查询
            LambdaQueryWrapper<CarryManage> wrapper = new LambdaQueryWrapper<>();
            if (Base.notEmpty(epc)) {
                wrapper.eq(CarryManage::getRfid, epc);
            }
            wrapper.eq(CarryManage::getIs_del, G.ISDEL_NO);
            //审批状态 0待审批 1 审批拒绝/未授权 2审批通过/已授权
            wrapper.eq(CarryManage::getApproval_state, "2");
            wrapper.ge(CarryManage::getEstimate_time, new Date());
            wrapper.orderByDesc(CarryManage::getId);
            wrapper.last("LIMIT 1");
            CarryManage carryManage = selectOne(wrapper);

            //查询异常外携记录
            LambdaQueryWrapper<CarryAbnormal> wrapper2 = new LambdaQueryWrapper<>();
            if (Base.notEmpty(epc)) {
                wrapper2.eq(CarryAbnormal::getRfid, epc);
            }
            wrapper2.eq(CarryAbnormal::getIs_del, G.ISDEL_NO);
            wrapper2.orderByDesc(CarryAbnormal::getId);
            wrapper2.last("LIMIT 1");
            CarryAbnormal carryAbnormal = carryAbnormalService.selectOne(wrapper2);

            //若存在记录
            if (Base.notEmpty(carryManage)) {
                //判断外携单外携状态
                if (carryManage.getCarry_state().equals("1")) {
                    //外携时间 更新
                    carryManage.setCarry_time(new Date());
                    updateById(carryManage);
                } else {
                    //外携时间 更新
                    carryManage.setCarry_time(new Date());
                    //外携状态（1.已外携、0.未外携）
                    carryManage.setCarry_state("1");
                    updateById(carryManage);
                }
            } else {
                //无记录 查询异常外携记录
                //有无异常外携记录
                if (Base.notEmpty(carryAbnormal)) {

                    //查询关联表,轨迹添加
                    List<CarryAbnormalMember> carryAbnormalMembers = carryAbnormalMemberMapper.selectList(Wrappers.<CarryAbnormalMember>lambdaQuery()
                            .eq(CarryAbnormalMember::getCarry_abnormal_id, carryAbnormal.getId())
                    );

                    //是否返回 0否 1是
                    if (carryAbnormal.getIs_repaid().equals("0")) {
                        // 更新设备,资产位置
                        if (Base.notEmpty(equipment)) {
                            carryAbnormal.setChange_position_id(equipment.getEquipment_position());
                            carryAbnormal.setEquipment_id(equipment.getId());

                            //轨迹处理
                            if (!CollectionUtils.isEmpty(carryAbnormalMembers)){
                                CarryAbnormalMember member = carryAbnormalMembers.get(0);
                                if (Base.notEmpty(member) && !Objects.equals(equipment.getEquipment_position(), member.getPosition_id())){
                                    CarryAbnormalMember abnormalMember = new CarryAbnormalMember();
                                    abnormalMember.setCarry_abnormal_id(carryAbnormal.getId());
                                    abnormalMember.setPosition_id(equipment.getEquipment_position());
                                    if (null != equipment.getEquipment_position()){
                                        SysPosition position = sysPositionMapper.selectById(equipment.getEquipment_position());
                                        if (null != position){
                                            abnormalMember.setPosition_info(position.getPosition_name());
                                        }
                                    }
                                    abnormalMember.setCreate_time(new Date());
                                    List<CarryAbnormalMember> a = carryAbnormalMemberMapper.selectList(Wrappers.<CarryAbnormalMember>query()
                                            .eq("carry_abnormal_id", carryAbnormal.getId())
                                            .orderByDesc("create_time")
                                    );
                                    if (!CollectionUtils.isEmpty(a)){
                                        CarryAbnormalMember b = a.get(0);
                                        if (Objects.equals(equipment.getEquipment_position(), b.getPosition_id())){
                                            b.setCreate_time(new Date());
                                            carryAbnormalMemberMapper.updateById(b);
                                        }else {
                                            carryAbnormalMemberMapper.insert(abnormalMember);
                                        }
                                    }else {
                                        carryAbnormalMemberMapper.insert(abnormalMember);
                                    }

                                }

                            }
                        }
                        //更新外携时间
                        carryAbnormal.setCarry_time(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

                        carryAbnormalService.updateById(carryAbnormal);
                    } else {
                        //生成新的异常外携记录
                        CarryAbnormal carryAbnormal2 = new CarryAbnormal();
                        carryAbnormal2.setRfid(epc);
                        //异常状态：1 未授权外携告警 2 位置变换告警 3 标签损毁告警
                        carryAbnormal2.setError_status("1");
                        if (Base.notEmpty(equipment)) {
                            carryAbnormal2.setChange_position_id(equipment.getEquipment_position());
                        }
                        carryAbnormal2 = carryAbnormalService.onSave(carryAbnormal2, equipment);
                        //生成轨迹记录
                        CarryAbnormalMember abnormalMember = new CarryAbnormalMember();
                        abnormalMember.setCarry_abnormal_id(carryAbnormal2.getId());
                        abnormalMember.setPosition_id(equipment.getEquipment_position());
                        if (null != equipment.getEquipment_position()){
                            SysPosition position = sysPositionMapper.selectById(equipment.getEquipment_position());
                            if (null != position){
                                abnormalMember.setPosition_info(position.getPosition_name());
                            }
                        }
                        abnormalMember.setCreate_time(new Date());
                        List<CarryAbnormalMember> a = carryAbnormalMemberMapper.selectList(Wrappers.<CarryAbnormalMember>query()
                                .eq("carry_abnormal_id", carryAbnormal.getId())
                                .orderByDesc("create_time")
                        );
                        if (!CollectionUtils.isEmpty(a)){
                            CarryAbnormalMember b = a.get(0);
                            if (Objects.equals(equipment.getEquipment_position(), b.getPosition_id())){
                                b.setCreate_time(new Date());
                                carryAbnormalMemberMapper.updateById(b);
                            }else {
                                carryAbnormalMemberMapper.insert(abnormalMember);
                            }
                        }else {
                            carryAbnormalMemberMapper.insert(abnormalMember);
                        }
                        System.out.println("生成新的异常外携记录***********************");
                    }
                } else {
                    //生成新的异常外携记录
                    CarryAbnormal carryAbnormal2 = new CarryAbnormal();
                    carryAbnormal2.setRfid(epc);
                    //异常状态：1 未授权外携告警 2 位置变换告警 3 标签损毁告警
                    carryAbnormal2.setError_status("1");
                    if (Base.notEmpty(equipment)) {
                        carryAbnormal2.setChange_position_id(equipment.getEquipment_position());
                    }
                    carryAbnormal2 = carryAbnormalService.onSave(carryAbnormal2, equipment);
                    //生成轨迹记录
                    CarryAbnormalMember abnormalMember = new CarryAbnormalMember();
                    abnormalMember.setCarry_abnormal_id(carryAbnormal2.getId());
                    abnormalMember.setPosition_id(equipment.getEquipment_position());
                    if (null != equipment.getEquipment_position()){
                        SysPosition position = sysPositionMapper.selectById(equipment.getEquipment_position());
                        if (null != position){
                            abnormalMember.setPosition_info(position.getPosition_name());
                        }
                    }
                    abnormalMember.setCreate_time(new Date());
                    List<CarryAbnormalMember> a = carryAbnormalMemberMapper.selectList(Wrappers.<CarryAbnormalMember>query()
                            .eq("carry_abnormal_id", carryAbnormal2.getId())
                            .orderByDesc("create_time")
                    );
                    if (!CollectionUtils.isEmpty(a)){
                        CarryAbnormalMember b = a.get(0);
                        if (Objects.equals(equipment.getEquipment_position(), b.getPosition_id())){
                            b.setCreate_time(new Date());
                            carryAbnormalMemberMapper.updateById(b);
                        }else {
                            carryAbnormalMemberMapper.insert(abnormalMember);
                        }
                    }else {
                        carryAbnormalMemberMapper.insert(abnormalMember);
                    }
                    System.out.println("生成新的异常外携记录***********************");
                }
            }
        }
    }

    //外携
    public JSONObject selectManageCountTop10() {
        JSONObject jsonObject = new JSONObject();
        List<CarryManaveVo> list = mapper.selectManageCount();
        for (CarryManaveVo carryManaveVo : list) {
            // 判断value 是否等于0 等于则转换为 1
            Long value = carryManaveVo.getValue();
            if (null != value && value == 0) {
                carryManaveVo.setValue(1L);
            }
            // 判断value 是否大于 60 大于则转换为小时 + 分钟
//            if (Integer.parseInt(carryManaveVo.getValue()) > 60) {
//                int hour = Integer.parseInt(carryManaveVo.getValue()) / 60;
//                int minute = Integer.parseInt(carryManaveVo.getValue()) % 60;
//                carryManaveVo.setValue(hour + " 小时 " + minute + " 分钟");
//            } else {
//                carryManaveVo.setValue(carryManaveVo.getValue() + " 分钟");
//            }
        }
        jsonObject.put("xData", list);
        return jsonObject;
    }

}
