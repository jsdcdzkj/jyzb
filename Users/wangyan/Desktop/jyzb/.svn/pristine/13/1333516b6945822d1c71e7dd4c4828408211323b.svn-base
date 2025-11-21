package com.jsdc.rfid.enums;

import lombok.Getter;

/**
 * ClassName: DataType
 * Description: 数据类型
 * date: 2022/3/14 14:22
 *
 * @author zdq
 */
@Getter
public enum DataType {

    ASSET_MANAGE(1,"资产管理"),
    INBOUND_ORDER_NUMBER(2,"入库单号"),
    CHANGE(3,"变更单号"),
    DISPOSE_OF(4,"处置单号"),
    BORROW(5,"借用单号"),
    RECEIVE(6,"领用单号"),
    TRANSFER(7,"调拨"),
    FAULT(8,"故障"),

    PURCHASE_APPLY(9,"资产采购"),
    INVENTORY_JOB(10,"盘点任务"),
    CARRYMANAGE_WX(11,"外携单号"),
    APPLYSINGLE_BX(12,"报修单号"),
    CONS_PURCHASE_ORDER_CODE(13,"耗材采购单编码"),
    CONS_RECEIPT_CODE(14,"耗材入库单编码"),
    CONS_RECEIVE_CODE(15,"耗材申领单编码"),
    CONS_RECEIVE_OUT_CODE(16,"耗材申领出库单编码"),
    CONS_HAND_OUT_CODE(17,"耗材手动出库单编码"),
    CONS_WXZC_CODE(18,"资产维修编码"),
    ENTER_NUMBER(19,"入库管理单号"),
    DELIVER_NUMBER(20,"出库管理单号")
    ;

    DataType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private Integer type;

    private String desc;

    private Integer type() {
        return this.type;
    }

    private String desc() {
        return this.desc;
    }

    public static String getValue(Integer type) {
        DataType[] carTypeEnums = values();
        for (DataType carTypeEnum : carTypeEnums) {
            if (carTypeEnum.type().equals(type)) {
                return carTypeEnum.desc();
            }
        }
        return null;
    }

    public static Integer getType(String desc) {
        DataType[] carTypeEnums = values();
        for (DataType carTypeEnum : carTypeEnums) {
            if (carTypeEnum.desc().equals(desc)) {
                return carTypeEnum.type();
            }
        }
        return null;
    }

    public static DataType getById(Integer id){
        for(DataType transactType : values()){
            if (transactType.getType() == id) {
                //获取指定的枚举
                return transactType;
            }
        }
        return null;
    }



}
