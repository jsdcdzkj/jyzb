package com.jsdc.rfid.service.warehouse.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.rfid.dto.WarehousingStockDto;
import com.jsdc.rfid.mapper.warehouse.BorrowDetailWarehousingMapper;
import com.jsdc.rfid.mapper.warehouse.BorrowWarehousingMapper;
import com.jsdc.rfid.mapper.warehouse.WarehouseStockDetailMapper;
import com.jsdc.rfid.model.warehouse.*;
import com.jsdc.rfid.service.SysDepartmentService;
import com.jsdc.rfid.service.SysUserService;
import com.jsdc.rfid.service.warehouse.BorrowDetailWarehousingService;
import com.jsdc.rfid.service.warehouse.BorrowWarehousingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BorrowWarehousingServiceImpl extends ServiceImpl<BorrowWarehousingMapper, WarehousingBorrow> implements BorrowWarehousingService {
    private final SysUserService sysUserService;
    private final SysDepartmentService sysDepartmentService;
    private final WarehouseStockDetailMapper warehouseStockDetailMapper;



    @Override
    public void deliveryStock(WarehousingBorrow warehousingBorrow) {
        //todo 加入锁机制
        List<WarehousingStockDetail> stockList = new ArrayList<>();
        WarehousingStockDto warehousingStockDto = new WarehousingStockDto();
        warehousingStockDto.setEquip_status(warehousingBorrow.getEquip_status());
        warehousingStockDto.setWarehouse_id(warehousingBorrow.getWarehouse_id());
        warehousingStockDto.setUse_dept(warehousingBorrow.getUse_dept());
        List<WarehousingStockDetail> stockDetails = buildStockDetail(warehousingBorrow,warehousingStockDto);
//            List<WarehousingStockDetail> stockDetails = new ArrayList<>();
        stockList.addAll(stockDetails);
        //未真正出库，需把库存再还回去，再生成逻辑入库记录
        stockDetails.forEach(s-> {
            WarehousingStockDetail stockDetail1 = new WarehousingStockDetail();
            BeanUtil.copyProperties(s, stockDetail1);
            stockDetail1.setEquip_out_num(0);
            stockDetail1.setEquip_in_num(s.getEquip_out_num());
            stockDetail1.setStock_type(0);
            stockDetail1.setEquip_status(1);
            if (warehousingBorrow.getUse_dept() != null) {
                stockDetail1.setDept_id(warehousingBorrow.getUse_dept());
                stockDetail1.setUse_dept(warehousingBorrow.getUse_dept());
            }
            stockDetail1.setWarehouse_id(null);
            stockList.add(stockDetail1);
        });
        warehousingBorrow.updateById();
    }

    public List<WarehousingStockDetail> buildStockDetail(WarehousingBorrow warehousingDelivery, WarehousingStockDto warehousingStockDto){
        warehousingStockDto.setEquip_type(warehousingDelivery.getEquip_type());
        warehousingStockDto.setEquip_name(warehousingDelivery.getEquip_name());
        warehousingStockDto.setEquip_model(warehousingDelivery.getEquip_model());
        warehousingStockDto.setProduceDate(DateUtil.formatDateTime(warehousingDelivery.getProduce_date()));
        List<String> deptIds = sysDepartmentService.getTreeId(sysUserService.getUser().getDepartment()).stream().map(d->d.toString()).collect(Collectors.toList());
        warehousingStockDto.setDeptIds(deptIds);
        List<WarehousingStockDetail> details = warehouseStockDetailMapper.detailList(warehousingStockDto);
        Map<BigDecimal,List<WarehousingStockDetail>> detailsMap = details.stream().collect(Collectors.groupingBy(WarehousingStockDetail::getUnit_price));
        List<WarehousingStockDetail> stockDetailList = new ArrayList<>();
        detailsMap.forEach((unitPrice,stockDetails)->{
            if(warehousingDelivery.getDelivery_num()==0)
                return;
            WarehousingStockDetail stockDetail = new WarehousingStockDetail();
            int inNum = stockDetails.stream().collect(Collectors.summingInt(WarehousingStockDetail::getEquip_in_num));
            int outNum = stockDetails.stream().collect(Collectors.summingInt(WarehousingStockDetail::getEquip_out_num));
            int stockNum = inNum-outNum;
            if(warehousingDelivery.getDelivery_num()<=stockNum){
                stockDetail.setEquip_out_num(warehousingDelivery.getDelivery_num());
                warehousingDelivery.setDelivery_num(0);
            }else{
                stockDetail.setEquip_out_num(stockNum);
                warehousingDelivery.setDelivery_num(warehousingDelivery.getDelivery_num()-stockNum);
            }
            stockDetail.setStock_id(stockDetails.get(0).getStock_id());
            stockDetail.setDelivery_id(warehousingDelivery.getId());
            stockDetail.setDelivery_no(warehousingDelivery.getDelivery_no());
            stockDetail.setEquip_type(warehousingDelivery.getEquip_type());
            stockDetail.setEquip_name(warehousingDelivery.getEquip_name());
            stockDetail.setEquip_model(warehousingDelivery.getEquip_model());
            if(warehousingDelivery.getWarehouse_id()!=null){
                stockDetail.setWarehouse_id(warehousingDelivery.getWarehouse_id());
            }
            stockDetail.setUse_dept(stockDetails.get(0).getUse_dept());
            stockDetail.setDept_id(stockDetails.get(0).getDept_id());
            stockDetail.setEquip_status(stockDetails.get(0).getEquip_status());
            stockDetail.setUnit_price(unitPrice);
            stockDetail.setUse_year(stockDetails.get(0).getUse_year());
            stockDetail.setProduce_date(warehousingDelivery.getProduce_date());
            stockDetail.setExpired_date(stockDetails.get(0).getExpired_date());
            stockDetail.setCritical_date(stockDetails.get(0).getCritical_date());
            stockDetail.setAccount_time(warehousingDelivery.getDelivery_time());
            stockDetail.setIs_del(String.valueOf(0));
            stockDetail.setCreate_time(new Date());
            stockDetail.setCreate_user(sysUserService.getUser().getId());
            stockDetailList.add(stockDetail);
        });
        return stockDetailList;
    }
}
