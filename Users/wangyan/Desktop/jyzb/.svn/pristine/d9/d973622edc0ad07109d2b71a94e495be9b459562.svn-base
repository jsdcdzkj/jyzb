package com.jsdc.rfid.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ConsReceiveDao;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.ConsDataVo;
import vo.ResultInfo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ConsReceiveService extends BaseService<ConsReceiveDao, ConsReceive> {

    @Autowired
    private ConsReceiveMapper consReceiveMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;
    @Autowired
    private ConsReceiveAssetsMapper consReceiveAssetsMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPostMapper sysPostMapper;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private ConsInventoryManagementMapper consInventoryManagementMapper;
    @Autowired
    private CommonDataTools commonDataTools;
    @Autowired
    private ConsCategoryMapper consCategoryMapper;
    @Autowired
    private ConsAssettypeMapper consAssettypeMapper;
    @Autowired
    private ConsSpecificationMapper consSpecificationMapper;
    @Autowired
    private ConsInventoryManagementService consInventoryManagementService;

    @Autowired
    private SysPostService sysPostService;

    @Autowired
    private ConsPurchaseDetailMapper consPurchaseDetailMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private ConsCategoryMapper categoryMapper;

    @Autowired
    private ConsAssettypeMapper assettypeMapper;

    @Autowired
    private ConsSpecificationMapper specificationMapper;

    @Autowired
    private ConsInAndOutStatisticsService consInAndOutStatisticsService;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private ProcessMemberHistoryService processMemberHistoryService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Autowired
    private ConsumableMapper consumableMapper ;

    @Autowired
    private RFIDConfigMapper rfidConfigMapper;

    @Autowired
    private WarehouseUserMapper warehouseUserMapper;
    /**
     * 耗材出库总数、申领出库总数
     *
     * @Author thr
     */
    public ConsReceive getTotalCount(ConsReceive bean) {
        return consReceiveMapper.getTotalCount(bean);
    }

    /**
     * 领用趋势：折线图展示近六个月的领用趋势（领用单数、耗材领用总数）；
     *
     * @Author thr
     */
    public List<ConsDataVo> getSixMonthCount(ConsReceive bean) {
        return consReceiveMapper.getSixMonthCount(bean);
    }

    public PageInfo<ConsReceive> getListByPage(ConsReceive consReceive, int pageIndex, int pageSize) {
        SysUser user = sysUserService.getUser();
        return getListByPage(consReceive,pageIndex,pageSize,user);
    }

    //列表展示
    public PageInfo<ConsReceive> getListByPage(ConsReceive consReceive, int pageIndex, int pageSize, SysUser user) {
        // 得到当前用户
        if (null == consReceive|| null == consReceive.getIs_adopt() || 0 == consReceive.getIs_adopt()){
            SysPost sysPost = sysPostService.selectById(user.getPost());

            if (sysPost.getData_permission() == G.DATAPERMISSION_PERSONAL) {
                if (consReceive != null) {
                    consReceive.setCreate_user(user.getId());
                }
            } else if (sysPost.getData_permission() == G.DATAPERMISSION_DEPT) {
                if (consReceive != null) {
                    consReceive.setDepartment_id(user.getDepartment());
                }
            }
        }
        //查询所有当前登录用户创建的申请单
        QueryWrapper<ConsReceive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        if (null != consReceive){
            if (Base.notEmpty(consReceive.getUse_id()) && null != consReceive.getUse_id()) {
                queryWrapper.eq("use_id", consReceive.getUse_id());
            }
            if (Base.notEmpty(consReceive.getDepartment_id()) && null != consReceive.getDepartment_id()) {
                queryWrapper.like("department_id", consReceive.getDepartment_id());
            }
            if (Base.notEmpty(consReceive.getStatus())) {
                queryWrapper.like("status", consReceive.getStatus());
            }
            if (null != consReceive.getReceive_code()) {
                queryWrapper.like("receive_code", consReceive.getReceive_code());
            }
            if (!CollectionUtils.isEmpty(consReceive.getIds())) {
                queryWrapper.in("id", consReceive.getIds());
            }
            if(StringUtils.isNotBlank(consReceive.getTransfer())){
                queryWrapper.like("receive_code", consReceive.getTransfer());
            }
            if (null != consReceive.getCreate_user()) {
                queryWrapper.eq("create_user", consReceive.getCreate_user());
            }
        }
        queryWrapper.orderByDesc("create_time");
        PageHelper.startPage(pageIndex, pageSize);
        List<ConsReceive> list = consReceiveMapper.selectList(queryWrapper);
        for (ConsReceive temp : list) {
            List<ConsReceiveAssets> receiveAssets = consReceiveAssetsMapper.selectList(Wrappers.<ConsReceiveAssets>lambdaQuery()
                    .eq(ConsReceiveAssets::getReceive_id, temp.getId())
                    .eq(ConsReceiveAssets::getIs_del, G.ISDEL_NO)
            );
            if (!CollectionUtils.isEmpty(receiveAssets)) {
                temp.setReceiveAssets(receiveAssets.stream().map(ConsReceiveAssets::getConsumable_name).collect(Collectors.joining(",")));
            }

            Integer departId = temp.getDepartment_id();
            if (null != departId) {
                SysDepartment sysDepartment = sysDepartmentMapper.selectById(departId);
                if (null != sysDepartment) {
                    temp.setDepartment_name(sysDepartment.getDept_name());
                }
            }
            Integer useId = temp.getUse_id();
            if (null != useId) {
                SysUser sysUser = sysUserService.selectById(useId);
                if (null != sysUser) {
                    temp.setUse_name(sysUser.getUser_name());
                }
            }
            if (null == temp.getOutbound_code()) {
                temp.setOutbound_code("");
            }
            // 得到当前流程节点
            processMemberService.getProcessDataByBusId(temp.getId(), G.PROCESS_HCSL, user, temp);
        }
        return new PageInfo<>(list);
    }

    public PageInfo<ConsReceive> finishAdopt(ConsReceive consReceive, int pageIndex, int pageSize) {
        //查询所有当前登录用户创建的申请单
        QueryWrapper<ConsReceive> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(consReceive.getIds())) {
            queryWrapper.in("id", consReceive.getIds());
        }
        if (StringUtils.isNotBlank(consReceive.getReceive_code())) {
            queryWrapper.like("receive_code", consReceive.getReceive_code());
        }
        if (StringUtils.isNotBlank(consReceive.getTransfer())){
            // receive_code like '%transfer%'
            queryWrapper.like("receive_code", consReceive.getTransfer());
        }
        queryWrapper.orderByDesc("create_time");
        PageHelper.startPage(pageIndex, pageSize);
        List<ConsReceive> list = consReceiveMapper.selectList(queryWrapper);
        for (ConsReceive temp : list) {
            List<ConsReceiveAssets> receiveAssets = consReceiveAssetsMapper.selectList(Wrappers.<ConsReceiveAssets>lambdaQuery()
                    .eq(ConsReceiveAssets::getReceive_id, temp.getId())
            );
            if (!CollectionUtils.isEmpty(receiveAssets)) {
                temp.setReceiveAssets(receiveAssets.stream().map(ConsReceiveAssets::getConsumable_name).collect(Collectors.joining(",")));
            }
            Integer departId = temp.getDepartment_id();
            if (null != departId) {
                SysDepartment sysDepartment = sysDepartmentMapper.selectById(departId);
                if (null != sysDepartment) {
                    temp.setDepartment_name(sysDepartment.getDept_name());
                }
            }
            Integer useId = temp.getUse_id();
            if (null != useId) {
                SysUser sysUser = sysUserService.selectById(useId);
                if (null != sysUser) {
                    temp.setUse_name(sysUser.getUser_name());
                }
            }
            if (null == temp.getOutbound_code()) {
                temp.setOutbound_code("");
            }
        }
        return new PageInfo<>(list);
    }

    //新增耗材领用申请
    public ResultInfo addConsReceive(List<ConsReceiveAssets> list, ConsReceive consReceive) {
        Integer userId = sysUserService.getUser().getId();
        SysUser sysUser = sysUserService.selectById(userId);

        String code = commonDataTools.getNo(DataType.CONS_RECEIVE_CODE.getType(), null);
        consReceive.setReceive_code(code);
        consReceive.setDepartment_id(sysUser.getDepartment());
        consReceive.setUse_id(userId);
        consReceive.setApply_date(new Date());
        consReceive.setStatus("1");
        consReceive.setIs_finish("2");
        consReceive.setCreate_time(new Date());
        consReceive.setCreate_user(userId);
        consReceive.setIs_del("0");
        consReceiveMapper.insert(consReceive);

        int count = 0;
        for (ConsReceiveAssets temp : list) {
            temp.setType(1);
            temp.setReceive_id(consReceive.getId());
            temp.setIs_del("0");
            temp.setCreate_time(new Date());
            temp.setCreate_user(userId);
            temp.setUse_num(0);
            consReceiveAssetsMapper.insert(temp);
            count += temp.getApply_num();
        }
        consReceive.setApply_num(count);
        consReceive.setOut_num(0);
        consReceiveMapper.updateById(consReceive);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record("领用申请-新增领用单").build());
        return ResultInfo.success();
    }


    //新增耗材领用申请并送审
    public ResultInfo addConsReceiveSub(List<ConsReceiveAssets> list, ConsReceive consReceive) {
        Integer userId = sysUserService.getUser().getId();
        SysUser sysUser = sysUserService.selectById(userId);

        String code = commonDataTools.getNo(DataType.CONS_RECEIVE_CODE.getType(), null);
        consReceive.setReceive_code(code);
        consReceive.setDepartment_id(sysUser.getDepartment());
        consReceive.setUse_id(userId);
        consReceive.setApply_date(new Date());
        consReceive.setStatus("2");
        consReceive.setIs_finish("2");
        consReceive.setCreate_time(new Date());
        consReceive.setCreate_user(userId);
        consReceive.setIs_del("0");
        consReceiveMapper.insert(consReceive);

        int count = 0;
        for (ConsReceiveAssets temp : list) {
            temp.setType(1);
            temp.setReceive_id(consReceive.getId());
            temp.setIs_del("0");
            temp.setCreate_time(new Date());
            temp.setCreate_user(userId);
            temp.setUse_num(0);
            consReceiveAssetsMapper.insert(temp);
            count += temp.getApply_num();
        }
        consReceive.setApply_num(count);
        consReceive.setOut_num(0);
        consReceiveMapper.updateById(consReceive);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record("领用申请-新增领用单并送审").build());
        // 启动流程
        processMemberService.startProcess(G.PROCESS_HCSL, consReceive.getId(), sysUserService.getUser().getId());
        return ResultInfo.success();
    }


    //修改耗材领用申请
    public ResultInfo updateConsReceive(List<ConsReceiveAssets> list, ConsReceive consReceive) {
        consReceive.setUpdate_time(new Date());
        consReceive.setUpdate_user(sysUserService.getUser().getId());
        consReceiveMapper.updateById(consReceive);

        //删除原有数据
//        UpdateWrapper<ConsReceiveAssets> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("receive_id", consReceive.getId());
//        updateWrapper.eq("type", 1);
//        updateWrapper.set("is_del", "1");
//        consReceiveAssetsMapper.update(null, updateWrapper);
        consReceiveAssetsMapper.delete(Wrappers.<ConsReceiveAssets>lambdaQuery()
                .eq(ConsReceiveAssets::getReceive_id, consReceive.getId()).eq(ConsReceiveAssets::getType, 1)
        );

        int count = 0;
        for (ConsReceiveAssets temp : list) {
            temp.setType(1);
            temp.setReceive_id(consReceive.getId());
            temp.setIs_del("0");
            temp.setCreate_time(new Date());
            temp.setCreate_user(sysUserService.getUser().getId());
            temp.setUse_num(0);
            consReceiveAssetsMapper.insert(temp);
            count += temp.getApply_num();
        }
        consReceive.setApply_num(count);
        consReceive.setOut_num(0);
        consReceiveMapper.updateById(consReceive);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record("领用申请-编辑领用单").build());
        return ResultInfo.success();
    }

    public ResultInfo updateReceiveNum(ConsReceiveAssets consReceiveAssets) {
        ConsReceiveAssets old = consReceiveAssetsMapper.selectById(consReceiveAssets.getId());
        if (null == old){
            return ResultInfo.error("数据不存在");
        }
//        if (old.getApply_num() < consReceiveAssets.getApply_num()){
//            return ResultInfo.error("领用数量不能大于申请数量");
//        }
        consReceiveAssets.setUpdate_time(new Date());
        consReceiveAssets.setUpdate_user(sysUserService.getUser().getId());
        consReceiveAssetsMapper.updateById(consReceiveAssets);
//        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(old.getReceive_code()).
//                operate_id(consReceiveAssets.getId()).type("11").is_del("0").record("领用申请-编辑领用单").build());
        // 查询领用单
        ConsReceive consReceive = consReceiveMapper.selectById(old.getReceive_id());
        // 查询领用单下的所有耗材
        List<ConsReceiveAssets> consReceiveAssetsList = consReceiveAssetsMapper.selectList(Wrappers.<ConsReceiveAssets>lambdaQuery()
                .eq(ConsReceiveAssets::getReceive_id, old.getReceive_id()).eq(ConsReceiveAssets::getType, 1)
        );
        int count = 0;
        for (ConsReceiveAssets temp : consReceiveAssetsList) {
            count += temp.getApply_num();
        }
        consReceive.setApply_num(count);
        consReceiveMapper.updateById(consReceive);

        // 得到当前用户
        SysUser current = sysUserService.getUser();
        String title = current.getUser_name() + "进行申请数量调整: 类型为 " + old.getConsumable_name() + " 的耗材, 数量从" + old.getApply_num() + " ,调整为" + consReceiveAssets.getApply_num();

        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record(title).build());
        return ResultInfo.success();
    }


    //修改耗材领用申请并送审
    public ResultInfo updateConsReceiveSub(List<ConsReceiveAssets> list, ConsReceive consReceive) {
        consReceive.setUpdate_time(new Date());
        consReceive.setUpdate_user(sysUserService.getUser().getId());
        consReceive.setStatus("2");
        consReceiveMapper.updateById(consReceive);

        //删除原有数据
        UpdateWrapper<ConsReceiveAssets> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("receive_id", consReceive.getId());
        updateWrapper.eq("type", 1);
        updateWrapper.set("is_del", "1");
        consReceiveAssetsMapper.update(null, updateWrapper);

        int count = 0;
        for (ConsReceiveAssets temp : list) {
            temp.setType(1);
            temp.setReceive_id(consReceive.getId());
            temp.setIs_del("0");
            temp.setCreate_time(new Date());
            temp.setCreate_user(sysUserService.getUser().getId());
            temp.setUse_num(0);
            consReceiveAssetsMapper.insert(temp);
            count += temp.getApply_num();
        }
        consReceive.setApply_num(count);
        consReceive.setOut_num(0);
        consReceiveMapper.updateById(consReceive);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record("领用申请-编辑领用单并送审").build());
        // 启动流程
        processMemberService.startProcess(G.PROCESS_HCSL, consReceive.getId(), sysUserService.getUser().getId());
        return ResultInfo.success();
    }


    //删除耗材领用表
    public ResultInfo delConsReceive(Integer id) {
        ConsReceive consReceive = consReceiveMapper.selectById(id);
        consReceive.setIs_del("1");
        consReceiveMapper.updateById(consReceive);
        UpdateWrapper<ConsReceiveAssets> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("type", 1);
        updateWrapper.eq("receive_id", consReceive.getId());
        updateWrapper.set("is_del", "1");
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(id).type("11").is_del("0").record("删除了一条数据").build());
        consReceiveAssetsMapper.update(null, updateWrapper);
        return ResultInfo.success();
    }

    //送审
    public ResultInfo subConReceive(Integer id) {
        ConsReceive consReceive = consReceiveMapper.selectById(id);
        consReceive.setStatus("2");
        consReceiveMapper.updateById(consReceive);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record("领用申请-领用单送审").build());
        // 启动流程
        processMemberService.startProcess(G.PROCESS_HCSL, id, sysUserService.getUser().getId());
        return ResultInfo.success();
    }

    //送审App
    public ResultInfo subConReceiveApp(Integer id, Integer userId) {
        ConsReceive consReceive = consReceiveMapper.selectById(id);
        consReceive.setStatus("2");
        consReceiveMapper.updateById(consReceive);
        OperationRecord operationRecord = OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record("送审了一条数据").build();
        operationRecordService.addOperationRecord1(operationRecord, userId);
        // 启动流程
        processMemberService.startProcess(G.PROCESS_HCSL, id, userId);
        return ResultInfo.success();
    }

    //确认App
    public ResultInfo sureConsReceive(Integer id, Integer userId) {
        ConsReceive consReceive = consReceiveMapper.selectById(id);
        consReceive.setStatus("6");
        consReceiveMapper.updateById(consReceive);
        OperationRecord operationRecord = OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record("用户确认一条耗材申领单").build();
        operationRecordService.addOperationRecord1(operationRecord, userId);
        return ResultInfo.success();
    }

    //撤回
    public ResultInfo backReceive(Integer id) {
        ConsReceive consReceive = consReceiveMapper.selectById(id);
        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_HCSL);
        ProcessMember processMember = processMemberService.getProcessMemberByBusId(consReceive.getId(), dictionary.getValue());
        if (null == processMember) {
            throw new RuntimeException("未查询到流程");
        }
        // 需求调整,现在都可以撤回
//        if (processMember.getIs_revoke() != 1){
//            return ResultInfo.error("当前流程已经审批，不能撤回!");
//        }
        processMember.setIsCH(1);
        processMemberService.removeProcessMember(processMember, JSON.toJSONString(consReceive));


        consReceive.setStatus("1");
        consReceiveMapper.updateById(consReceive);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record("领用申请-领用单撤回").build());

        return ResultInfo.success();
    }

    //确认
    public ResultInfo sureReceive(Integer id) {
        ConsReceive consReceive = consReceiveMapper.selectById(id);
        consReceive.setStatus("6");
        consReceive.setUpdate_time(new Date());
        consReceiveMapper.updateById(consReceive);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consReceive.getReceive_code()).
                operate_id(consReceive.getId()).type("11").is_del("0").record("用户确认一条耗材申领单").build());
        return ResultInfo.success();
    }


    //根据ID 查询单个申请表 的详细信息
    public ConsReceive getOneInfo(Integer id) {
        ConsReceive consReceive = consReceiveMapper.selectById(id);
        Date t = consReceive.getCreate_time();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        consReceive.setReal_create_time(simpleDateFormat.format(t));
        Integer departId = consReceive.getDepartment_id();
        if (null != departId) {
            SysDepartment sysDepartment = sysDepartmentMapper.selectById(departId);
            if (null != sysDepartment) {
                consReceive.setDepartment_name(sysDepartment.getDept_name());
            }
        }
        Integer useId = consReceive.getUse_id();
        if (null != useId) {
            SysUser sysUser = sysUserService.selectById(useId);
            if (null != sysUser) {
                consReceive.setUse_name(sysUser.getUser_name());
            }
        }
        SysUser user = null;
        try {
            user = sysUserService.getUser();
        } catch (Exception e) {
            user = sysUserService.getUser(null != useId ? useId:1);
        }
        if (null != consReceive.getStatus()) {
            switch (consReceive.getStatus()) {
                case "1":
                    consReceive.setStatusName("未送审");
                    break;
                case "2":
                    consReceive.setStatusName("未审批");
                    break;
                case "3":
                    consReceive.setStatusName("审批中");
                    break;
                case "4":
                    consReceive.setStatusName("审批通过");
                    break;
                case "5":
                    consReceive.setStatusName("审批驳回");
                    break;
                case "6":
                    consReceive.setStatusName("已完成");
                    break;
                default:
                    consReceive.setStatusName("");
                    break;
            }
        }
        if (null == consReceive.getOutbound_code()) {
            consReceive.setOutbound_code("");
        }
        return consReceive;
    }

    //根据领用单ID 查询出详情信息
    public List<ConsReceiveAssets> getOneInfoById(Integer id) {
        QueryWrapper<ConsReceiveAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receive_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("type", 1);
        List<ConsReceiveAssets> list = consReceiveAssetsMapper.selectList(queryWrapper);
        Integer count = 0;
        for (ConsReceiveAssets temp : list) {
            Integer asset_type_id = temp.getAsset_type_id();
            if (null != asset_type_id) {
                temp.setAsset_type_name(consCategoryMapper.selectById(asset_type_id).getName());
            }

            Integer asset_name = temp.getAsset_name();
            if (null != asset_name) {
                temp.setReal_asset_name(consAssettypeMapper.selectById(asset_name).getName());
            }

            Integer specifications = temp.getSpecifications();
            if (null != specifications) {
                temp.setSpecifications_name(consSpecificationMapper.selectById(specifications).getTypename());
            }
            Integer warehouse_id = temp.getWarehouse_id();
            if (null != warehouse_id) {
                temp.setWarehouse_name(warehouseMapper.selectById(warehouse_id).getWarehouse_name());
            }

            if (null != temp.getCons_id()) {
                ConsInventoryManagement consInventoryManagement = consInventoryManagementMapper.selectById(temp.getCons_id());
                if (null != consInventoryManagement) {
                    temp.setInventory_num(consInventoryManagement.getInventory_num());
                    int num = null == consInventoryManagement.getInventory_num() ? 0 : consInventoryManagement.getInventory_num();
                    temp.setReduce_num(num - (null == temp.getApply_num() ? 0 : temp.getApply_num()));

                    // 查询单位名称
                    if (null != consInventoryManagement.getUnit_of_measurement()){
                        try {
                            temp.setUnit_name(commonDataTools.getSysDictMap("unit").get(consInventoryManagement.getUnit_of_measurement()).getLabel());
                        } catch (Exception ignored) {
                        }
                    }
                }

            }
            if (null != temp.getApply_num()){
                temp.setUse_num_temp(temp.getApply_num());
            }
            count++;
            temp.setNum(count);


//            QueryWrapper<ConsInventoryManagement> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.eq("asset_type_id",asset_type_id);
//            queryWrapper1.eq("asset_name_id",asset_name);
//            queryWrapper1.eq("specifications",specifications);
//            queryWrapper1.eq("is_del","0");
//            List<ConsInventoryManagement> consInventoryManagement = consInventoryManagementService.selectList(queryWrapper1);
//            if (CollectionUtils.isEmpty(consInventoryManagement) ){
//                temp.setReduce_num(0);
//            }else {
//                Integer count = 0;
//                for (ConsInventoryManagement temp1 : consInventoryManagement){
//                    count += temp1.getInventory_num();
//                }
//                temp.setReduce_num(count);
//            }
        }
        return list;
    }

    /**
     * 微信小程序
     * 根据领用单ID 查询出详情信息 【解决剩余库存负数问题】
     */
    public List<ConsReceiveAssets> getOneInfoById2(Integer id) {
        QueryWrapper<ConsReceiveAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receive_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("type", 1);
        List<ConsReceiveAssets> list = consReceiveAssetsMapper.selectList(queryWrapper);
        Integer count = 0;
        for (ConsReceiveAssets temp : list) {
            Integer asset_type_id = temp.getAsset_type_id();
            if (null != asset_type_id) {
                temp.setAsset_type_name(consCategoryMapper.selectById(asset_type_id).getName());
            }

            Integer asset_name = temp.getAsset_name();
            if (null != asset_name) {
                temp.setReal_asset_name(consAssettypeMapper.selectById(asset_name).getName());
            }

            Integer specifications = temp.getSpecifications();
            if (null != specifications) {
                temp.setSpecifications_name(consSpecificationMapper.selectById(specifications).getTypename());
            }
            Integer warehouse_id = temp.getWarehouse_id();
            if (null != warehouse_id) {
                temp.setWarehouse_name(warehouseMapper.selectById(warehouse_id).getWarehouse_name());
            }

            if (null != temp.getCons_id()) {
                ConsInventoryManagement consInventoryManagement = consInventoryManagementMapper.selectById(temp.getCons_id());
                if (null != consInventoryManagement) {
                    temp.setInventory_num(consInventoryManagement.getInventory_num());
                    Integer num = null == consInventoryManagement.getInventory_num() ? 0 : consInventoryManagement.getInventory_num();
//                    temp.setReduce_num(num - (null == temp.getApply_num() ? 0 : temp.getApply_num()));
                    temp.setReduce_num(num);

                    Consumable consumable = consumableMapper.selectById(consInventoryManagement.getConsumable_id());
                    temp.setConsumable_name(null == consumable ? "" : consumable.getConsumable_name());
                }
            }
            count++;
            temp.setNum(count);


//            QueryWrapper<ConsInventoryManagement> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.eq("asset_type_id",asset_type_id);
//            queryWrapper1.eq("asset_name_id",asset_name);
//            queryWrapper1.eq("specifications",specifications);
//            queryWrapper1.eq("is_del","0");
//            List<ConsInventoryManagement> consInventoryManagement = consInventoryManagementService.selectList(queryWrapper1);
//            if (CollectionUtils.isEmpty(consInventoryManagement) ){
//                temp.setReduce_num(0);
//            }else {
//                Integer count = 0;
//                for (ConsInventoryManagement temp1 : consInventoryManagement){
//                    count += temp1.getInventory_num();
//                }
//                temp.setReduce_num(count);
//            }
        }
        return list;
    }

    //对于审批通过的领用单 生成出库单
    public ResultInfo checkOut(Integer id) {
        //首先状态改为审批通过 其次生成出库单
        ConsReceive consReceive = consReceiveMapper.selectById(id);
        consReceive.setStatus("4");
        String code = commonDataTools.getNo(DataType.CONS_RECEIVE_OUT_CODE.getType(), null);
        consReceive.setOutbound_code(code);
        consReceive.setOut_date(new Date());
        consReceiveMapper.updateById(consReceive);
        return ResultInfo.success();
    }

    //申领出库列表展示
    public ResultInfo getCheckOutListByPage(ConsReceive consReceive, int pageIndex, int pageSize) {
        return getCheckOutListByPage(consReceive,pageIndex,pageSize,false);
    }

    public ResultInfo getCheckOutListByPage(ConsReceive consReceive, int pageIndex, int pageSize,boolean isFinish) {
        SysUser sysUser = sysUserService.getUser();
        int userId = sysUser.getId();
        Integer postId = sysUserMapper.selectById(userId).getPost();
        if (null == postId) {
            return ResultInfo.error("没有权限查看");
        }
        SysPost sysPost = sysPostMapper.selectById(postId);
        if (null == sysPost) {
            return ResultInfo.error("没有权限查看");
        }
        Integer data_permission = sysPost.getData_permission();
        QueryWrapper<ConsReceive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        if(isFinish) {
            queryWrapper.in("status", Collections.singletonList("6"));
        }else{
            queryWrapper.in("status", Arrays.asList("4","6"));

        }
        if (StringUtils.isNotBlank(consReceive.getIs_finish())) {
            queryWrapper.eq("is_finish",consReceive.getIs_finish());
        }
//        queryWrapper.isNotNull("outbound_code");
        if (null != consReceive.getReceive_code()) {
            queryWrapper.like("receive_code", consReceive.getReceive_code());
        }
        if (null != consReceive.getOutbound_code()) {
            queryWrapper.like("outbound_code", consReceive.getOutbound_code());
        }
        // 判断是否为市检察院
        RFIDConfig rfid = rfidConfigMapper.selectById(1);
        // 是否为市检察院
        boolean isCity = false;
        if (null != rfid){
            if (null != rfid.getSystemcode() && 1 == rfid.getSystemcode()){
                //市检察院
                isCity = true;
            }
        }
        if (!isCity){
            //仅查看个人通过审核数据
            if (G.DATAPERMISSION_PERSONAL == data_permission) {
                queryWrapper.eq("create_user", sysUserService.getUser().getId());

                //查看本部门数据
            } else if (G.DATAPERMISSION_DEPT == data_permission) {
                int department = sysUser.getDepartment();
                queryWrapper.eq("department_id", department);
                //查看所有部门数据
            } else {

            }
        }
        queryWrapper.orderByDesc("create_time");
        PageHelper.startPage(pageIndex, pageSize);
        List<ConsReceive> list = consReceiveMapper.selectList(queryWrapper);

        for (ConsReceive temp : list) {
            Integer departId = temp.getDepartment_id();
            if (null != departId) {
                SysDepartment sysDepartment = sysDepartmentMapper.selectById(departId);
                if (null != sysDepartment) {
                    temp.setDepartment_name(sysDepartment.getDept_name());
                }
            }
            Integer useId = temp.getUse_id();
            if (null != useId) {
                sysUser = sysUserService.selectById(useId);
                if (null != sysUser) {
                    temp.setUse_name(sysUser.getUser_name());
                }
            }
            if (null != temp.getCreate_time()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                temp.setReal_create_time(simpleDateFormat.format(temp.getCreate_time()));
            }

//            ConsInventoryManagement consInventoryManagement = consInventoryManagementMapper.selectById(temp.getCons_id());
//            if (null != consInventoryManagement) {
//                temp.setInventory_num(consInventoryManagement.getInventory_num());
//                int num = null == consInventoryManagement.getInventory_num() ? 0 : consInventoryManagement.getInventory_num();
//                temp.setReduce_num(num - (null == temp.getApply_num() ? 0 : temp.getApply_num()));
//
//                // 查询单位名称
//                if (null != consInventoryManagement.getUnit_of_measurement()){
//                    try {
//                        temp.setUnit_name(commonDataTools.getSysDictMap("unit").get(consInventoryManagement.getUnit_of_measurement()).getLabel());
//                    } catch (Exception ignored) {
//                    }
//                }
//            }
        }
        PageInfo<ConsReceive> pageInfo = new PageInfo<>(list);
        return ResultInfo.success(pageInfo);
    }


    //出库操作
    public ResultInfo retrieval(Integer id, Integer outNum) {

        ConsReceiveAssets consReceiveAssets = consReceiveAssetsMapper.selectById(id);
        //申领数量
        Integer apply_num = consReceiveAssets.getApply_num();
        //已经出库数量
        Integer use_num = consReceiveAssets.getUse_num();
        //实际还需要出库的数量
        Integer temp_num = outNum;


//        QueryWrapper<ConsInventoryManagement> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("asset_type_id", consReceiveAssets.getAsset_type_id());
//        queryWrapper.eq("asset_name_id", consReceiveAssets.getAsset_name());
//        queryWrapper.eq("specifications", consReceiveAssets.getSpecifications());
//        queryWrapper.gt("inventory_num", 0);
//        queryWrapper.orderByAsc("create_time");
//        List<ConsInventoryManagement> list = consInventoryManagementMapper.selectList(queryWrapper);
        if (null == consReceiveAssets.getCons_id()) {
            return ResultInfo.error("出库失败,库存数据异常");
        }
        ConsInventoryManagement temp = consInventoryManagementMapper.selectById(consReceiveAssets.getCons_id());

        Integer count = 0;

        //遍历集合 判断库存剩余的数量 是否满足实际还要出库的数量
//        for (ConsInventoryManagement temp : list) {
        //实际出库数量
        int real_out_num = 0;
        Integer inventory_num = temp.getInventory_num();
        //如果剩余数量大于要领用的数量
        if (inventory_num >= temp_num) {
            // 实际出库数量 = temp_num
            real_out_num = temp_num;
            //当前库存记录剩余库存
            temp.setInventory_num(inventory_num - temp_num);
            temp_num = 0;
            consInventoryManagementMapper.updateById(temp);

//                break;
        } else {
            RFIDConfig rfidConfig = rfidConfigMapper.selectById(1);
            if (rfidConfig != null && rfidConfig.getSystemcode() == 1) {
                // 实际出库数量 = temp_num
                real_out_num = temp_num;
                //当前库存记录剩余库存
                temp.setInventory_num(inventory_num - temp_num);
                temp_num = 0;
                consInventoryManagementMapper.updateById(temp);
            }else{
                temp_num = temp_num - inventory_num;
                real_out_num = temp_num;
                temp.setInventory_num(0);
                count += inventory_num;
                consInventoryManagementMapper.updateById(temp);
            }


//                break;
        }
        // 加入出入库统计记录
        BigDecimal dj = new BigDecimal(temp.getActual_amount() == null ? "0" : temp.getActual_amount());
        BigDecimal sl = new BigDecimal(real_out_num);
        // 计算实际金额
        BigDecimal actualAmount = dj.multiply(sl);

        // 查询主表
        ConsReceive consReceive = consReceiveMapper.selectById(consReceiveAssets.getReceive_id());
        Consumable consumable = consumableMapper.selectById(temp.getConsumable_id());
        if (inventory_num > 0) {
            consInAndOutStatisticsService.insertCons(ConsInAndOutStatistics.builder()
                    //耗材品类id
                    .consumable_name(consumable == null ? "" : consumable.getConsumable_name())
                    //耗材名称
                    .name(temp.getName())
                    //生产日期
                    .production_date(temp.getProduction_date())
                    // 质保期
                    .warranty_period(temp.getWarranty_period())
                    // 单位
                    .unit_id(temp.getUnit_of_measurement())
                    // 单价
                    .unit_price(temp.getActual_amount())
                    // 期初数量
                    .initial_number(inventory_num)
                    // 入库数量
                    .in_number(0)
                    // 出库数量
                    .out_number(real_out_num)
                    // 期末数量
                    .final_number(temp.getInventory_num())
                    // 总金额
                    .total_amount(actualAmount.toString())
                    // 规格
                    .model(temp.getModel())
                    // 仓库id
                    .warehouse_id(temp.getWarehouse_id())
                    // 部门id
                    .dept_id(consReceive.getDepartment_id())
                    // 申领人
                    .user_id(consReceive.getUse_id())
                    // 类型
                    .type(2)
                    .build());
        }
//        }

        //如果temp_num == 0 则证明此次出库数量领用完毕
        if (temp_num == 0) {
            if (null == use_num) {
                use_num = 0;
            }
            consReceiveAssets.setUse_num(use_num + outNum);
        } else {
            if (null == use_num) {
                use_num = 0;
            }
            consReceiveAssets.setUse_num(use_num + count);
        }
        //修改关联表数据
        consReceiveAssetsMapper.updateById(consReceiveAssets);


        //对主表进行修改
//        Integer receive_id = consReceiveAssets.getReceive_id();
//        ConsReceive consReceive = consReceiveMapper.selectById(receive_id);
        if (temp_num == 0) {
            consReceive.setOut_num(consReceive.getOut_num() + outNum);
            if (consReceive.getOut_num().equals(consReceive.getApply_num())) {
                consReceive.setIs_finish("1");
            }
        } else {
            consReceive.setOut_num(consReceive.getOut_num() + count);
            if (consReceive.getOut_num().equals(consReceive.getApply_num())) {
                consReceive.setIs_finish("1");
            }
        }
        consReceive.setCancel_sign("0");
        String code = commonDataTools.getNo(DataType.CONS_RECEIVE_OUT_CODE.getType(), null);
        consReceive.setOutbound_code(code);
        consReceiveMapper.updateById(consReceive);


        Integer tempId = consReceiveAssets.getAsset_name();
        ConsAssettype consAssettype = consAssettypeMapper.selectById(tempId);
        if (null != consAssettype) {
            String name = StringUtils.isBlank(consAssettype.getName())?"":"名称为" + consAssettype.getName() + "的";
            if (temp_num == 0) {
                name = name + "易耗品出库数量+" + outNum;
            } else {
                name = name + "易耗品出库数量+" + count;
            }

            operationRecordService.addOperationRecord(OperationRecord.builder()
                    .field_fk(consReceive.getReceive_code())
                    .operate_id(consReceive.getId())
                    .type("11").is_del("0").record(name).build());
        }


        return ResultInfo.success();
    }

    //查询单条记录日志信息
    public List<OperationRecord> getOperationRecordList(Integer id) {
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operate_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("type", "11");
        queryWrapper.orderByAsc("create_time");
        return operationRecordService.selectList(queryWrapper);
    }

    //查询最新单条记录日志信息
    public OperationRecord getOperationRecordData(Integer id) {
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operate_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("type", "11");
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("limit 1");
        return operationRecordService.selectOne(queryWrapper);
    }


    //App列表展示
    public List<ConsReceive> getAllConsReceiveByUserId(Integer userId, String transfer) {
        //查询所有当前登录用户创建的申请单
        QueryWrapper<ConsReceive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("create_user", userId);
        if (StringUtils.isNotBlank(transfer)) {
            queryWrapper.like("receive_code", transfer);
        }
        queryWrapper.orderByDesc("create_time");
        List<ConsReceive> list = consReceiveMapper.selectList(queryWrapper);
        for (ConsReceive temp : list) {
            Integer departId = temp.getDepartment_id();
            if (null != departId) {
                SysDepartment sysDepartment = sysDepartmentMapper.selectById(departId);
                if (null != sysDepartment) {
                    temp.setDepartment_name(sysDepartment.getDept_name());
                }
            }
            Integer useId = temp.getUse_id();
            if (null != useId) {
                SysUser sysUser = sysUserService.selectById(useId);
                if (null != sysUser) {
                    temp.setUse_name(sysUser.getUser_name());
                }
            }
            if (null == temp.getOutbound_code()) {
                temp.setOutbound_code("");
            }
            if (null != temp.getCreate_time()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                temp.setReal_create_time(simpleDateFormat.format(temp.getCreate_time()));
                temp.setReal_use_date(simpleDateFormat.format(temp.getApply_date()));
            }
            if (null != temp.getStatus()) {
                switch (temp.getStatus()) {
                    case "1":
                        temp.setStatusName("未送审");
                        break;
                    case "2":
                        temp.setStatusName("未审批");
                        break;
                    case "3":
                        temp.setStatusName("审批中");
                        break;
                    case "4":
                        temp.setStatusName("审批通过");
                        break;
                    case "5":
                        temp.setStatusName("审批驳回");
                        break;
                    case "6":
                        temp.setStatusName("已完成");
                        break;
                    default:
                        temp.setStatusName("");
                        break;
                }
            }
        }

        return list;
    }

    /**
     *  耗材申领中数据展示
     * @param userId
     * @param transfer
     * @return
     */
    public List<ConsReceive> getAllConsReceivingByUserId(Integer userId, String transfer) {
        //查询所有当前登录用户创建的申请单
        QueryWrapper<ConsReceive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("create_user", userId);
        queryWrapper.in("status", "2","3");
        if (StringUtils.isNotBlank(transfer)) {
            queryWrapper.like("receive_code", transfer);
        }
        queryWrapper.orderByDesc("create_time");
        return consReceiveMapper.selectList(queryWrapper);
    }

    public void appAddConsReceive(String assetNameIds, String assetTypeIds, String specificationss, String remark, Integer userId, String nums, Integer type, String ids) {
        SysUser sysUser = sysUserService.selectById(userId);
        ConsReceive consReceive = new ConsReceive();

        String code = commonDataTools.getNo(DataType.CONS_RECEIVE_CODE.getType(), null);
        consReceive.setReceive_code(code);
        consReceive.setDepartment_id(sysUser.getDepartment());
        consReceive.setUse_id(userId);
        consReceive.setRemark(remark);
        consReceive.setApply_date(new Date());
        if (1 == type) {
            consReceive.setStatus("1");
        } else {
            consReceive.setStatus("2");
        }

        consReceive.setIs_finish("2");
        consReceive.setCreate_time(new Date());
        consReceive.setCreate_user(userId);
        consReceive.setIs_del("0");
        consReceiveMapper.insert(consReceive);

        String[] tempAssetNameIds = assetNameIds.split("-");
        String[] tempAssetTypeIds = assetTypeIds.split("-");
        String[] tempSpecificationss = specificationss.split("-");
        String[] tempNums = nums.split("-");
        String[] idList = ids.split("-");
        int count = 0;
        for (int i = 0; i < idList.length; i++) {
            ConsInventoryManagement consInventoryManagement = consInventoryManagementMapper.selectById(Integer.parseInt(idList[i]));
            ConsReceiveAssets temp = new ConsReceiveAssets();
            temp.setName(consInventoryManagement.getName());
            temp.setModel(consInventoryManagement.getModel());
            Consumable consumable = consumableMapper.selectById(consInventoryManagement.getConsumable_id());
            temp.setConsumable_name(consumable == null ? "" : consumable.getConsumable_name());
            temp.setApply_num(Integer.parseInt(tempNums[i]));
            temp.setType(1);
            temp.setReceive_id(consReceive.getId());
            temp.setIs_del("0");
            temp.setCreate_time(new Date());
            temp.setCreate_user(userId);
            temp.setUse_num(0);
            temp.setCons_id(Integer.parseInt(idList[i]));

            temp.setBrand(consInventoryManagement.getBrand_id() + "");
            temp.setProduction_date(consInventoryManagement.getProduction_date());
            temp.setWarranty_period(consInventoryManagement.getWarranty_period());
            temp.setWarehouse_id(consInventoryManagement.getWarehouse_id());

            consReceiveAssetsMapper.insert(temp);
            count += temp.getApply_num();
        }
        consReceive.setApply_num(count);
        consReceive.setOut_num(0);
        consReceiveMapper.updateById(consReceive);
        if (type == 1) {
            OperationRecord operationRecord = OperationRecord.builder().field_fk(code).
                    operate_id(consReceive.getId()).type("11").is_del("0").record("领用申请-新增领用单").build();
            operationRecordService.addOperationRecord1(operationRecord, userId);
        } else {
            OperationRecord operationRecord = OperationRecord.builder().field_fk(code).
                    operate_id(consReceive.getId()).type("11").is_del("0").record("领用申请-新增领用单并送审").build();
            operationRecordService.addOperationRecord1(operationRecord, userId);
            // 启动流程
            processMemberService.startProcess(G.PROCESS_HCSL, consReceive.getId(), userId);
        }


    }


    public PageInfo<ConsInventoryManagement> getConsPurchaseDetailPage(Integer page, Integer limit, ConsInventoryManagement consInventoryManagement) {

        PageHelper.startPage(page, limit, "create_time desc");
        LambdaQueryWrapper<ConsInventoryManagement> wrapper = Wrappers.<ConsInventoryManagement>lambdaQuery()
                .eq(ConsInventoryManagement::getIs_del, G.ISDEL_NO)
                //库存数量大于0
//                .gt(ConsInventoryManagement::getInventory_num, 0)
                // 仓库
                .eq(consInventoryManagement.getWarehouse_id() != null, ConsInventoryManagement::getWarehouse_id, consInventoryManagement.getWarehouse_id())
                // 品类
                .eq(consInventoryManagement.getAsset_type_id() != null, ConsInventoryManagement::getAsset_type_id, consInventoryManagement.getAsset_type_id())
                // 名称
                .eq(consInventoryManagement.getAsset_name_id() != null, ConsInventoryManagement::getAsset_name_id, consInventoryManagement.getAsset_name_id())
                .like(StringUtils.isNotBlank(consInventoryManagement.getName()), ConsInventoryManagement::getName, consInventoryManagement.getName())
                .like(StringUtils.isNotBlank(consInventoryManagement.getModel()), ConsInventoryManagement::getModel, consInventoryManagement.getModel())
                // 规格
                .eq(consInventoryManagement.getSpecifications() != null, ConsInventoryManagement::getSpecifications, consInventoryManagement.getSpecifications())
                // 类型名称
                .eq(null != consInventoryManagement.getConsumable_id(), ConsInventoryManagement::getConsumable_id, consInventoryManagement.getConsumable_id());
//                .orderByDesc(ConsInventoryManagement::getCreate_time)
        // 针对于检察院项目,进行特殊处理
        RFIDConfig rfidConfig = rfidConfigMapper.selectById(1);
        if (null != rfidConfig && 1 == rfidConfig.getSystemcode()){
            Set<Integer> wareIds = new HashSet<>();
            // 得到所有为全部权限的仓库
            List<Warehouse> warehouses = warehouseMapper.selectList(Wrappers.<Warehouse>lambdaQuery()
                    .eq(Warehouse::getPerm_mode, 1)
                    .eq(Warehouse::getIs_del, G.ISDEL_NO)
            );
            if (!CollectionUtils.isEmpty(warehouses)){
                for (Warehouse warehouse : warehouses) {
                    wareIds.add(warehouse.getId());
                }
            }
            // 根据用户得到所有仓库
            SysUser user = sysUserService.getUser();
            List<WarehouseUserMember> members = warehouseUserMapper.selectList(Wrappers.<WarehouseUserMember>lambdaQuery().eq(WarehouseUserMember::getUser_id, user.getId()));
            if (!CollectionUtils.isEmpty(members)){
                for (WarehouseUserMember member : members) {
                    wareIds.add(member.getWarehouse_id());
                }
            }
            if (!CollectionUtil.isEmpty(wareIds)){
                wrapper.in(ConsInventoryManagement::getWarehouse_id, wareIds);
            }else {
                wrapper.eq(ConsInventoryManagement::getWarehouse_id, -1);
            }
        }

        List<ConsInventoryManagement> list = consInventoryManagementMapper.selectList(wrapper);

        // 查询关联名称
        list.forEach(item -> {
            if(null != item.getConsumable_id()){
                item.setConsumable_name(consumableMapper.selectById(item.getConsumable_id()).getConsumable_name());

            }

            ConsAssettype consAssettype = assettypeMapper.selectById(item.getAsset_name_id());
            if (consAssettype != null) {
                item.setAsset_name(consAssettype.getName());
            }
            ConsCategory sysAssetsCategory = categoryMapper.selectById(item.getAsset_type_id());
            if (sysAssetsCategory != null) {
                item.setAssetTypeName(sysAssetsCategory.getName());
            }
            Warehouse sysWarehouse = warehouseMapper.selectById(item.getWarehouse_id());
            if (sysWarehouse != null) {
                item.setWarehouse_name(sysWarehouse.getWarehouse_name());
            }
            ConsSpecification sysAssetsSpecification = specificationMapper.selectById(item.getSpecifications());
            if (sysAssetsSpecification != null) {
                item.setSpecifications_name(sysAssetsSpecification.getTypename());
            }
            // 查询单位名称
            if (null != item.getUnit_of_measurement()){
                try {
                    item.setUnit_name(commonDataTools.getSysDictMap("unit").get(item.getUnit_of_measurement()).getLabel());
                } catch (Exception ignored) {
                }
            }
        });

        return new PageInfo<>(list);
    }

    /**
     * 查询资产列表
     *
     * @param page
     * @param limit
     * @return
     */
    public PageInfo getStatisticsList(ConsInAndOutStatistics consInAndOutStatistics, Integer page, Integer limit) {

        PageHelper.startPage(page, limit, "create_time desc");
        // 结束日期增加一天
        if (consInAndOutStatistics.getEnd_time() != null) {
            consInAndOutStatistics.setEnd_time(DateUtils.addDays(consInAndOutStatistics.getEnd_time(), 1));
        }
        List<ConsInAndOutStatistics> list = consInAndOutStatisticsService.selectList(Wrappers.<ConsInAndOutStatistics>lambdaQuery()
                .eq(ConsInAndOutStatistics::getIs_del, G.ISDEL_NO)
                // 仓库
                .eq(consInAndOutStatistics.getWarehouse_id() != null, ConsInAndOutStatistics::getWarehouse_id, consInAndOutStatistics.getWarehouse_id())
                // 品类
                .like(consInAndOutStatistics.getConsumable_name() != null, ConsInAndOutStatistics::getConsumable_name, consInAndOutStatistics.getConsumable_name())
                // 名称
                .like(consInAndOutStatistics.getName() != null, ConsInAndOutStatistics::getName, consInAndOutStatistics.getName())
                // 规格
                .like(null != consInAndOutStatistics.getModel(), ConsInAndOutStatistics::getModel, consInAndOutStatistics.getModel())
                // 部门
                .eq(consInAndOutStatistics.getDept_id() != null, ConsInAndOutStatistics::getDept_id, consInAndOutStatistics.getDept_id())
                // 类型
                .eq(consInAndOutStatistics.getType() != null, ConsInAndOutStatistics::getType, consInAndOutStatistics.getType())
                // 时间
                .ge(consInAndOutStatistics.getStart_time() != null, ConsInAndOutStatistics::getCreate_time, consInAndOutStatistics.getStart_time())
                .le(consInAndOutStatistics.getEnd_time() != null, ConsInAndOutStatistics::getCreate_time, consInAndOutStatistics.getEnd_time())
                // 领用人 user_id
                .eq(consInAndOutStatistics.getUser_id() != null, ConsInAndOutStatistics::getUser_id, consInAndOutStatistics.getUser_id())
//                .orderByDesc(ConsInAndOutStatistics::getCreate_time)
        );

        //得到单位
        Map<Integer, SysDict> unitMap = commonDataTools.getSysDictMap("unit");
        // 得到部门
        Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();
        for (ConsInAndOutStatistics item : list) {
            //得到品类名称
//            ConsCategory sysAssetsCategory = categoryMapper.selectById(item.getCons_type_id());
//            if (sysAssetsCategory != null) {
//                item.setCons_type_name(sysAssetsCategory.getName());
//            }
            //得到名称
//            ConsAssettype consAssettype = assettypeMapper.selectById(item.getCons_name_id());
//            if (consAssettype != null) {
//                item.setCons_name(consAssettype.getName());
//            }
            //得到仓库名称
            Warehouse sysWarehouse = warehouseMapper.selectById(item.getWarehouse_id());
            if (sysWarehouse != null) {
                item.setWarehouse_name(sysWarehouse.getWarehouse_name());
            }
//            ConsSpecification sysAssetsSpecification = specificationMapper.selectById(item.getSpecifications());
//            if (sysAssetsSpecification != null) {
//                item.setSpecifications_name(sysAssetsSpecification.getTypename());
//            }
            // 得到部门
            String dept_name = commonDataTools.getValue(deptMap, item.getDept_id(), "dept_name");
            item.setDept_name(dept_name);
            // 得到领用人
            SysUser sysUser = sysUserMapper.selectById(item.getUser_id());
            if (sysUser != null) {
                item.setUser_name(sysUser.getUser_name());
            }

            // 得到单位名称
            String unit_name = commonDataTools.getValue(unitMap, item.getUnit_id(), "label");
            item.setUnit_name(unit_name);
        }

        return new PageInfo<>(list);
    }

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：根据耗材品类名称展示耗材出库详情分页列表
     * @Date ：2023/3/15 下午 2:31
     * @Modified By：
     */
    public PageInfo<ConsReceive> getPageListByConsCategoryName(Integer pageIndex, Integer pageSize, ConsReceive beanParam) {
        String join = "";
        if (beanParam.getDepartment_id() != -1) {
            ArrayList<Integer> deptSon = this.sysDepartmentService.getDeptSon(beanParam.getDepartment_id());
            join = org.apache.commons.lang3.StringUtils.join(deptSon, ",");
        }

        PageHelper.startPage(pageIndex, pageSize);
        List<ConsReceive> list = this.consReceiveMapper.getPageListByConsCategoryName(beanParam, join);
        return new PageInfo<>(list);
    }


}
