package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ConsHandworkDao;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.ResultInfo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ConsHandworkService  extends BaseService<ConsHandworkDao, ConsHandwork> {

    private final ConsHandworkMapper consHandworkMapper;

    private final SysUserService sysUserService ;

    private final SysUserMapper sysUserMapper ;

    private final SysPostMapper sysPostMapper;

    private final SysDepartmentMapper sysDepartmentMapper;

    private final ConsReceiveAssetsMapper consReceiveAssetsMapper;

    private final ConsInventoryManagementMapper consInventoryManagementMapper;

    private final CommonDataTools commonDataTools;

    private final ConsCategoryMapper consCategoryMapper;

    private final ConsAssettypeMapper consAssettypeMapper;

    private final ConsSpecificationMapper consSpecificationMapper;

    private final ConsInventoryManagementService consInventoryManagementService;

    private final OperationRecordService operationRecordService;

    private final WarehouseMapper warehouseMapper;

    private final ConsInAndOutStatisticsService consInAndOutStatisticsService;

    private final ConsumableMapper consumableMapper;

    /**
     * 手动出库总数
     *
     * @Author thr
     */
    public ConsHandwork getTotalCount(ConsHandwork bean) {
        return consHandworkMapper.getTotalCount(bean);
    }

    //列表查询
    public ResultInfo getListByPage(ConsHandwork consHandwork, int pageIndex, int pageSize){
        return getListByPage(consHandwork,pageIndex,pageSize,false);
    }
    public ResultInfo getListByPage(ConsHandwork consHandwork, int pageIndex, int pageSize,boolean isFinish){
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

        QueryWrapper<ConsHandwork> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        if(isFinish){
            queryWrapper.eq("is_finish","2");
        }
        //仅查看个人通过审核数据
        if (G.DATAPERMISSION_PERSONAL == data_permission) {
            queryWrapper.eq("create_user",sysUserService.getUser().getId());

            //查看本部门数据
        } else if (G.DATAPERMISSION_DEPT == data_permission) {
            int department = sysUser.getDepartment();
            queryWrapper.eq("department_id",department);
            //查看所有部门数据
        } else {

        }

        if (null != consHandwork.getOutbound_code()){
            queryWrapper.like("out_code",consHandwork.getOutbound_code());
        }
        queryWrapper.orderByDesc("create_time");
        PageHelper.startPage(pageIndex, pageSize);
        List<ConsHandwork> list = consHandworkMapper.selectList(queryWrapper);

        for (ConsHandwork temp : list){
            Integer departId = temp.getDept_id();
            if (null != departId){
                SysDepartment sysDepartment = sysDepartmentMapper.selectById(departId);
                if ( null != sysDepartment){
                    temp.setDepartment_name(sysDepartment.getDept_name());
                }
            }
            Integer useId = temp.getUse_id();
            if (null != useId){
                sysUser = sysUserService.selectById(useId);
                if ( null != sysUser){
                    temp.setUse_name(sysUser.getUser_name());
                }
            }
            if (null != temp.getCreate_time()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                temp.setReal_create_time(simpleDateFormat.format(temp.getCreate_time()));
            }
        }
        PageInfo<ConsHandwork> pageInfo = new PageInfo<>(list);
        return ResultInfo.success(pageInfo);
    }


    //新增
    public ResultInfo addConsHand(List<ConsReceiveAssets> list ,ConsHandwork  consHandwork){
        SysUser sysUser = sysUserService.getUser();
        String code = commonDataTools.getNo(DataType.CONS_HAND_OUT_CODE.getType(), null);
        consHandwork.setOut_code(code);
        consHandwork.setDept_id(sysUser.getDepartment());
        consHandwork.setUse_id(sysUser.getId());
        consHandwork.setIs_finish("2");
        consHandwork.setCreate_time(new Date());
        consHandwork.setCreate_user(sysUser.getId());
        consHandwork.setIs_del("0");
        consHandworkMapper.insert(consHandwork);

        Integer count = 0 ;
        for (ConsReceiveAssets temp : list){
            temp.setType(2);
            temp.setReceive_id(consHandwork.getId());
            temp.setIs_del("0");
            temp.setCreate_time(new Date());
            temp.setCreate_user(sysUser.getId());
            temp.setUse_num(0);
            consReceiveAssetsMapper.insert(temp);
            count+= temp.getApply_num();

        }
        consHandwork.setNum(count);
        consHandworkMapper.updateById(consHandwork);

        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consHandwork.getOut_code()).
                operate_id(consHandwork.getId()).type("12").is_del("0").record("新增了一条数据").build());
        return ResultInfo.success();
    }

    //修改
    public ResultInfo updateConsHand(List<ConsReceiveAssets> list ,ConsHandwork  consHandwork){
        SysUser sysUser = sysUserService.getUser();
        consHandwork.setUpdate_time(new Date());
        consHandwork.setUpdate_user(sysUserService.getUser().getId());
        consHandworkMapper.updateById(consHandwork);


        //删除原有数据
        UpdateWrapper<ConsReceiveAssets> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("receive_id",consHandwork.getId());
        updateWrapper.eq("type",2);
        updateWrapper.set("is_del","1");
        consReceiveAssetsMapper.update(null,updateWrapper);

        Integer count = 0 ;
        for (ConsReceiveAssets temp : list){
            temp.setType(2);
            temp.setReceive_id(consHandwork.getId());
            temp.setIs_del("0");
            temp.setCreate_time(new Date());
            temp.setCreate_user(sysUser.getId());
            temp.setUse_num(0);
            consReceiveAssetsMapper.insert(temp);
            count+= temp.getApply_num();

        }
        consHandwork.setNum(count);
        consHandworkMapper.updateById(consHandwork);

        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consHandwork.getOut_code()).
                operate_id(consHandwork.getId()).type("12").is_del("0").record("修改了一条数据").build());
        return ResultInfo.success();
    }

    //删除
    public ResultInfo delConsHand(Integer id){
        ConsHandwork consHandwork = consHandworkMapper.selectById(id);
        consHandwork.setIs_del("1");
        consHandworkMapper.updateById(consHandwork);

        UpdateWrapper<ConsReceiveAssets> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("type",2);
        updateWrapper.eq("receive_id",consHandwork.getId());
        updateWrapper.set("is_del","1");
        consReceiveAssetsMapper.update(null,updateWrapper);
        return ResultInfo.success();
    }

    //确认出库
    public ResultInfo outWarehouse(Integer id){
        QueryWrapper<ConsReceiveAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        queryWrapper.eq("receive_id",id);
        queryWrapper.eq("type",2);
        List<ConsReceiveAssets> list = consReceiveAssetsMapper.selectList(queryWrapper);
        Integer count = 0 ;
        for (ConsReceiveAssets consReceiveAssets : list){
            //申领数量
            Integer temp_num = consReceiveAssets.getApply_num();

//            QueryWrapper<ConsInventoryManagement> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.eq("asset_type_id",consReceiveAssets.getAsset_type_id());
//            queryWrapper1.eq("asset_name_id",consReceiveAssets.getAsset_name());
//            queryWrapper1.eq("specifications",consReceiveAssets.getSpecifications());
//            queryWrapper1.gt("inventory_num",0);
//            queryWrapper1.orderByAsc("create_time");
//            List<ConsInventoryManagement> list1 =consInventoryManagementMapper.selectList(queryWrapper1);

            if(null == consReceiveAssets.getCons_id()){
                return ResultInfo.error("出库失败,库存数据异常");
            }
            ConsInventoryManagement temp = consInventoryManagementMapper.selectById(consReceiveAssets.getCons_id());


            //遍历集合 判断库存剩余的数量 是否满足实际还要出库的数量
//            for (ConsInventoryManagement temp : list1){
            //实际出库数量
            int real_out_num = 0;
            Integer inventory_num = temp.getInventory_num();
            //如果剩余数量大于要领用的数量
            if (inventory_num >= temp_num){
                // 实际出库数量 = temp_num
                real_out_num = temp_num;
                //当前库存记录剩余库存
                temp.setInventory_num(inventory_num - temp_num);
                consInventoryManagementMapper.updateById(temp);
                temp_num =0 ;
//                    break;
            }else {
                temp_num = temp_num - inventory_num;
                real_out_num = temp_num;
                temp.setInventory_num(0);
                consInventoryManagementMapper.updateById(temp);
//                    return ResultInfo.error("出库失败,库存不足");
            }
//            }
            // 加入出入库统计记录
            BigDecimal dj = new BigDecimal(temp.getActual_amount() == null ? "0" : temp.getActual_amount());
            BigDecimal sl = new BigDecimal(real_out_num);
            // 计算实际金额
            BigDecimal actualAmount = dj.multiply(sl);

            ConsHandwork consHandwork = consHandworkMapper.selectById(id);
            Consumable consumable = consumableMapper.selectById(temp.getConsumable_id());
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
                    .dept_id(consHandwork.getDept_id())
                    // 申领人
                    .user_id(consHandwork.getUse_id())
                    // 类型
                    .type(2)
                    .build());

            if (temp_num != 0){
                count++ ;
                break;
            }else {
                //如果temp_num == 0 则证明此库存已经领用完毕 把出库数量改成领用数量
                consReceiveAssets.setUse_num(consReceiveAssets.getApply_num());
                consReceiveAssetsMapper.updateById(consReceiveAssets);
            }



        }

        if (count > 0){
            return ResultInfo.error("库存不足，请修改出库单！！！");
        }



        //如果走到这证明都出库完成 则需要修改完成标志
        ConsHandwork consHandwork = consHandworkMapper.selectById(id);
        consHandwork.setIs_finish("1");
        consHandworkMapper.updateById(consHandwork);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(consHandwork.getOut_code()).
                operate_id(consHandwork.getId()).type("12").is_del("0").record("耗材手动出库成功").build());
        return ResultInfo.success();
    }



    //根据ID 查询单个手动出库单 的详细信息
    public ConsHandwork getOneInfo(Integer id) {
        ConsHandwork consHandwork = consHandworkMapper.selectById(id);
        Date t = consHandwork.getCreate_time();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        consHandwork.setReal_create_time(simpleDateFormat.format(t));
        Integer departId = consHandwork.getDept_id();
        if (null != departId) {
            SysDepartment sysDepartment = sysDepartmentMapper.selectById(departId);
            if (null != sysDepartment) {
                consHandwork.setDepartment_name(sysDepartment.getDept_name());
            }
        }
        Integer useId = consHandwork.getUse_id();
        if (null != useId) {
            SysUser sysUser = sysUserService.selectById(useId);
            if (null != sysUser) {
                consHandwork.setUse_name(sysUser.getUser_name());
            }
        }
        return consHandwork;
    }


    //根据领用单ID 查询出详情信息
    public List<ConsReceiveAssets> getOneInfoById(Integer id) {
        QueryWrapper<ConsReceiveAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receive_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("type", 2);
        List<ConsReceiveAssets> list = consReceiveAssetsMapper.selectList(queryWrapper);
        for (ConsReceiveAssets temp : list) {
            Integer asset_type_id = temp.getAsset_type_id();
            if (null != asset_type_id){
                temp.setAsset_type_name(consCategoryMapper.selectById(asset_type_id).getName());
            }

            Integer asset_name = temp.getAsset_name();
            if (null != asset_name){
                temp.setReal_asset_name(consAssettypeMapper.selectById(asset_name).getName());
            }

            Integer specifications = temp.getSpecifications();
            if (null != specifications){
                temp.setSpecifications_name( consSpecificationMapper.selectById(specifications).getTypename());
            }

            Integer warehouse_id = temp.getWarehouse_id();
            if (null != warehouse_id){
                temp.setWarehouse_name(warehouseMapper.selectById(warehouse_id).getWarehouse_name());
            }

            if(null != temp.getCons_id()){
                ConsInventoryManagement consInventoryManagement = consInventoryManagementMapper.selectById(temp.getCons_id());
                if(null != consInventoryManagement){
                    temp.setInventory_num(consInventoryManagement.getInventory_num());
                    Integer num = null == consInventoryManagement.getInventory_num()?0:consInventoryManagement.getInventory_num();
                    temp.setReduce_num(num - (null == temp.getApply_num()?0:temp.getApply_num()));
                }
            }

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

    //查询单条记录日志信息
    public  List<OperationRecord> getOperationRecordList (Integer id){
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operate_id",id);
        queryWrapper.eq("is_del","0");
        queryWrapper.eq("type","12");
        queryWrapper.orderByAsc("create_time");
        return operationRecordService.selectList(queryWrapper);
    }




}
