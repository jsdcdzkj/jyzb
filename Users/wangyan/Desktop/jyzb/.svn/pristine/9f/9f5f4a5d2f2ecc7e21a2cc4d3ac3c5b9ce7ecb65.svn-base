package com.jsdc.rfid.service.warehouse.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dto.WarehousingStockDto;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.enums.DeliveryType;
import com.jsdc.rfid.enums.EquipStatusEnums;
import com.jsdc.rfid.mapper.warehouse.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.model.warehouse.*;
import com.jsdc.rfid.service.FileManageService;
import com.jsdc.rfid.service.SysDepartmentService;
import com.jsdc.rfid.service.SysUserService;
import com.jsdc.rfid.service.warehouse.DeliveryWarehouseDetailService;
import com.jsdc.rfid.service.warehouse.DeliveryWarehouseService;
import com.jsdc.rfid.service.warehouse.SystemNoticeService;
import com.jsdc.rfid.service.warehouse.WarehouseStockService;
import com.jsdc.rfid.utils.CommonDataTools;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class DeliveryWarehouseServiceImpl extends ServiceImpl<DeliveryWarehouseMapper, WarehousingDelivery> implements DeliveryWarehouseService {

    private final CommonDataTools commonDataTools;

    private final SysUserService sysUserService;

    private final DeliveryWarehouseDetailMapper deliveryWarehouseDetailMapper;
    private final EnterWarehouseDetailMapper enterWarehouseDetailMapper;

    @Resource
    private DeliveryWarehouseDetailService deliveryWarehouseDetailService;

    @Resource
    private WarehouseStockService warehouseStockService;

    private final WarehouseStockDetailMapper warehouseStockDetailMapper;

    private final SysDepartmentService sysDepartmentService;

    private final WarehouseStockCarryOverMapper warehouseStockCarryOverMapper;

    private final SystemNoticeService systemNoticeService;
    @Autowired
    private FileManageService fileManageService;


    @Override
    public PageInfo<WarehousingDelivery> pageQuery(Integer pageIndex, Integer pageSize, WarehousingDelivery warehousingDelivery) {
        QueryWrapper<WarehousingDelivery> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(warehousingDelivery.getDelivery_no())) {
            queryWrapper.like("delivery_no", warehousingDelivery.getDelivery_no());
        }
        if (ObjectUtil.isNotEmpty(warehousingDelivery.getDelivery_type())) {
            queryWrapper.eq("delivery_type", warehousingDelivery.getDelivery_type());
        }
        if (StringUtils.isNotEmpty(warehousingDelivery.getDelivery_start_time())) {
            queryWrapper.ge("delivery_time", warehousingDelivery.getDelivery_start_time());
        }
        if (StringUtils.isNotEmpty(warehousingDelivery.getDelivery_end_time())) {
            queryWrapper.le("delivery_time", warehousingDelivery.getDelivery_end_time());
        }
        queryWrapper.eq("is_del", "0");
        List<Integer> deptIds = sysDepartmentService.getTreeId(sysUserService.getUser().getDepartment());
        queryWrapper.in("dept_id", deptIds);

        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<WarehousingDelivery> pageList = list(queryWrapper);

        Map<Integer, Warehouse> warehouseMap = commonDataTools.getWarehouseMap();
        Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();
        Map<Integer, SysUser> userMap = commonDataTools.getUserAllMap();
        pageList.stream().map(d -> {
            if (d.getWarehouse_id() != null) {
                d.setWarehouse_name(warehouseMap.get(d.getWarehouse_id()) != null ? warehouseMap.get(d.getWarehouse_id()).getWarehouse_name() : "");
            }
            if(d.getDelivery_type() != null){
                d.setDelivery_type_desc(DeliveryType.getDesc(d.getDelivery_type()));
            }
            if (d.getUse_dept() != null) {
                d.setUse_dept_name(deptMap.get(d.getUse_dept()) != null ? deptMap.get(d.getUse_dept()).getDept_name() : "");
            }
            if(d.getCreate_user() != null){
                d.setCreate_user_name(userMap.get(d.getCreate_user()) != null ? userMap.get(d.getCreate_user()).getUser_name() : "");
            }
            return d;
        }).collect(Collectors.toList());

        return new PageInfo<>(pageList);
    }

    @Override
    public List<String> detailByName(Integer equip_name, Integer warehouseId, Integer use_dept) {
        QueryWrapper<WarehousingEnterDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT equip_model"); // 使用 DISTINCT 关键字去重
        if (null != equip_name) {
            queryWrapper.eq("equip_name", equip_name);
        }
        if (null != warehouseId) {
            queryWrapper.eq("warehouse_id", warehouseId);
        }
        if (null != use_dept) {
            queryWrapper.eq("use_dept", use_dept);
        }
        List<WarehousingEnterDetail> list = enterWarehouseDetailMapper.selectList(queryWrapper);

        // 提取 equip_name 字段并去重
        return list.stream()
                .map(WarehousingEnterDetail::getEquip_model) // 提取 equip_name 字段
                .distinct() // 去重
                .collect(Collectors.toList()); // 收集到列表
    }

    public List<String> detailByModel(String equip_model, Integer equip_name, Integer warehouseId, Integer use_dept) {
        QueryWrapper<WarehousingEnterDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT produce_date"); // 使用 DISTINCT 关键字去重
        if (StringUtils.isNotEmpty(equip_model)) {
            queryWrapper.eq("equip_model", equip_model);
        }
        if (null != equip_name) {
            queryWrapper.eq("equip_name", equip_name);
        }
        if (null != warehouseId) {
            queryWrapper.eq("warehouse_id", warehouseId);
        }
        if (null != use_dept) {
            queryWrapper.eq("use_dept", use_dept);
        }
        List<WarehousingEnterDetail> list = enterWarehouseDetailMapper.selectList(queryWrapper);

        // 将 produce_date 转换为字符串并按日期排序
        return list.stream()
                .map(detail -> detail.getProduce_date()) // 提取日期字段
                .filter(date -> date != null) // 过滤掉 null 值
                .sorted() // 按日期排序
                .map(date -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 自定义格式
                    return sdf.format(date); // 转换为字符串
                })
                .distinct() // 去重
                .collect(Collectors.toList()); // 收集到列表
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WarehousingDelivery bean) {
        Integer year = DateUtil.year(bean.getDelivery_time());
        Integer month = DateUtil.month(bean.getDelivery_time()) + 1;
        //出库时间校验
        WarehousingStockDto warehousingStockDto = new WarehousingStockDto();
        warehousingStockDto.setYear(year);
        warehousingStockDto.setMonth(month);
        List<String> deptIds = sysDepartmentService.getFjDept(sysUserService.getUser().getDepartment()).stream().map(d -> d.toString()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(deptIds)) {
            deptIds = sysDepartmentService.getTreeId(sysUserService.getUser().getDepartment()).stream().map(d -> d.toString()).collect(Collectors.toList());
        }
        warehousingStockDto.setDeptIds(deptIds);
        List<WarehousingStockCarryOver> carryOvers = warehouseStockCarryOverMapper.carryOverList(warehousingStockDto);
        if (!CollectionUtils.isEmpty(carryOvers)) {
            throw new RuntimeException("当月结转数据已生成,无法入库!");
        }
        //库存校验
        warehousingStockDto.setEquip_status(bean.getEquip_status());
        warehousingStockDto.setWarehouse_id(bean.getWarehouse_id());
        warehousingStockDto.setUse_dept(bean.getUse_dept());
        deptIds = sysDepartmentService.getTreeId(sysUserService.getUser().getDepartment()).stream().map(d -> d.toString()).collect(Collectors.toList());
        warehousingStockDto.setDeptIds(deptIds);
        if (!CollectionUtils.isEmpty(bean.getDeliveryDetails())) {
            bean.getDeliveryDetails().forEach(d -> {
                warehousingStockDto.setEquip_type(d.getEquip_type());
                warehousingStockDto.setEquip_name(d.getEquip_name());
                warehousingStockDto.setEquip_model(d.getEquip_model());
                warehousingStockDto.setProduceDate(DateUtil.formatDate(d.getProduce_date()));
                List<WarehousingStockDetail> stockDetails = warehouseStockDetailMapper.equipList(warehousingStockDto);
                int inNum = stockDetails.stream().collect(Collectors.summingInt(WarehousingStockDetail::getEquip_in_num));
                int outNum = stockDetails.stream().collect(Collectors.summingInt(WarehousingStockDetail::getEquip_out_num));
                if (d.getDelivery_num() > (inNum - outNum)) {
                    throw new RuntimeException("出库数量大于库存！！");
                }

            });
        }
        //生成出库库单号
        bean.setDelivery_no(commonDataTools.getNo(DataType.DELIVER_NUMBER.getType(), null));
        bean.setIs_del(String.valueOf(0));
        bean.setCreate_time(new Date());
        bean.setCreate_user(sysUserService.getUser().getId());
        bean.setDept_id(null != bean.getDept_id() ? bean.getDept_id() : sysUserService.getUser().getDepartment());
        if (null != bean.getBorrow_use_dept()) {
            bean.setUse_dept(bean.getBorrow_use_dept());
        }
        save(bean);//保存主表
        //保存从表
        if (!CollectionUtils.isEmpty(bean.getDeliveryDetails())) {
            List<WarehousingDeliveryDetail> details = bean.getDeliveryDetails().stream().map(d -> {
                d.setDelivery_no(bean.getDelivery_no());
                d.setDelivery_id(bean.getId());
                d.setIs_del(G.ISDEL_NO);
                return d;
            }).collect(Collectors.toList());
            deliveryWarehouseDetailService.batchSave(details);
        }
        warehouseStockService.deliveryStock(bean);
        systemNoticeService.addNotice("新增出库单" + bean.getDelivery_no(), "出库");
    }

    @Override
    public WarehousingDelivery detail(Integer id) {
        WarehousingDelivery warehousingDelivery = getById(id);
        if(null != warehousingDelivery.getFileId()){
            FileManage fileManage = fileManageService.selectById(warehousingDelivery.getFileId());
            warehousingDelivery.setFileName(fileManage.getStore_name());
        }

        Map<Integer, Warehouse> warehouseMap = commonDataTools.getWarehouseMap();
        Map<Integer, SysDict> dictMap = commonDataTools.getSysDictMap("unit");
        //Map<String, AssetsType> assetsMap = commonDataTools.getAssetsTypeCodeMap();
        Map<Integer, AssetsType> assetsMap = commonDataTools.getAssetsTypeMap();
        Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();

        warehousingDelivery.setDelivery_type_desc(DeliveryType.getDesc(warehousingDelivery.getDelivery_type()));
        warehousingDelivery.setEquip_status_desc(EquipStatusEnums.getDesc(warehousingDelivery.getEquip_status()));
        if (warehousingDelivery.getWarehouse_id() != null) {
            warehousingDelivery.setWarehouse_name(warehouseMap.get(warehousingDelivery.getWarehouse_id()) != null ? warehouseMap.get(warehousingDelivery.getWarehouse_id()).getWarehouse_name() : "");
        }
        if (warehousingDelivery.getUse_dept() != null) {
            warehousingDelivery.setUse_dept_name(deptMap.get(warehousingDelivery.getUse_dept()) != null ? deptMap.get(warehousingDelivery.getUse_dept()).getDept_name() : "");
        }
        //查询明细
        QueryWrapper<WarehousingDeliveryDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delivery_no", warehousingDelivery.getDelivery_no());
        queryWrapper.eq("delivery_id", warehousingDelivery.getId());
        queryWrapper.eq("is_del", "0");
        List<WarehousingDeliveryDetail> details = deliveryWarehouseDetailMapper.selectList(queryWrapper).stream().map(d -> {
            d.setEquip_type_name(assetsMap.get(d.getEquip_type()) != null ? assetsMap.get(d.getEquip_type()).getAssets_type_name() : "");
            d.setEquip_name_desc(assetsMap.get(d.getEquip_name()) != null ? assetsMap.get(d.getEquip_name()).getAssets_type_name() : "");
            d.setAssets_type_code(assetsMap.get(d.getEquip_name()) != null ? assetsMap.get(d.getEquip_name()).getAssets_type_code() : "");
            d.setEquip_unit_name(dictMap.get(d.getEquip_unit()) != null ? dictMap.get(d.getEquip_unit()).getLabel() : "");
            return d;
        }).collect(Collectors.toList());
        warehousingDelivery.setDeliveryDetails(details);
        return warehousingDelivery;
    }

}
