package com.jsdc.rfid.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class G {
    // 逻辑删除 是
    public String ISDEL_YES = "1";
    // 逻辑删除 否
    public String ISDEL_NO = "0";

    // 是否导入 是
    public String ISIMPORT_YES = "1";
    // 是否导入 否
    public String ISIMPORT_NO = "0";

    //是否启用 是
    public int ISENABLE_YES = 1;
    //是否启用 否
    public int ISENABLE_NO = 0;

    //数据权限 仅查看个人数据
    public int DATAPERMISSION_PERSONAL = 0;
    //数据权限 查看本部门数据
    public int DATAPERMISSION_DEPT = 1;
    //数据权限 查看所有部门数据
    public int DATAPERMISSION_ALLDEPT = 2;

    public String STRING_SEPARATE = ",";

    //设备状态 在线
    public Integer EQUIPMENT_ONLINE = 1;
    //设备状态 离线
    public Integer EQUIPMENT_OFFLINE = 0;

    //设备连接方式 0:作为客户端
    public Integer EQUIPMENT_CONNECT_CLIENT = 0;
    //设备连接方式 1:作为服务端
    public Integer EQUIPMENT_CONNECT_SERVER = 1;

    //设备类型 读码机
    public Integer EQUIPMENT_TYPE_READ = 0;
    //设备类型 写码机
    public Integer EQUIPMENT_TYPE_WRITE = 1;
    //设备类型 读写一体机
    public Integer EQUIPMENT_TYPE_READ_WRITE = 2;

    //送审状态 未送审
    public String NOT_SUBMITTED = "1";
    //送审状态 未审批
    public String NOT_APPROVED = "2";
    //送审状态 审批中
    public String UNDER_APPROVAL = "3";
    //送审状态 审批通过
    public String PASS_APPROVED = "4";
    //送审状态 审批拒绝退回
    public String REJECTION_APPROVAL = "5";


    //领用借用状态 1待领用/借用确认
    public String TO_BE_COLLECTED = "1";
    //领用借用状态 2待归还
    public String TO_BE_RETURNED = "2";
    //领用借用状态 3已归还
    public String RETURNED = "3";
    //借用状态 4延期未归还
    public String OVERDUE_RETURN = "4";


    //变更来源 1变更申请
    public String CHANGE_APPLICATION = "1";
    //变更来源 2手动变更
    public String CHANGE_MANUAL = "2";


    //处置来源 1处置申请
    public String DISPOSAL_APPLICATION = "1";
    //处置来源 2盘亏处理
    public String INVENTORY_LOSS_TREATMENT = "2";
    //处置来源 3盘点异常处理
    public String ABNORMAL_HANDLING_OF_INVENTORY = "3";


    //处置完成标志 1完成
    public String FINISH_YES = "1";
    //处置完成标志 2未完成
    public String FINISH_NO = "2";

    //进出状态 1进
    public String STATUS_IN = "1";
    //进出状态 2出
    public String STATUS_OUT = "2";

    /**
     * REID 读写器 读取方法类型
     */
    //资产盘点
    public String RFID_READ_CHECK = "1";
    //报警
    public String RFID_READ_ALARM = "2";
    //扫描查询
    public String RFID_READ_SEARCH = "3";
    //外携报警
    public String RFID_READ_CARRY_ALARM = "4";
    //定时扫描
    public String RFID_READ_SCANNING = "5";

    /**
     * 流程启动名称 资产申领
     */
    public String PROCESS_ZCSL = "资产申领";
    /**
     * 流程启动名称 资产采购
     */
    public String PROCESS_ZCCG = "资产采购";
    /**
     * 流程启动名称 资产变更
     */
    public String PROCESS_ZCBG = "资产变更";
    /**
     * 流程启动名称 资产处置
     */
    public String PROCESS_ZCCZ = "资产处置";
    /**
     * 流程启动名称 资产外携
     */
    public String PROCESS_ZCWX = "资产外携";
    /**
     * 流程启动名称 耗材采购
     */
    public String PROCESS_HCCG = "耗材采购";
    /**
     * 流程启动名称 耗材申领
     */
    public String PROCESS_HCSL = "耗材申领";

    /**
     * 流程启动名称 资产维修
     */
    public String PROCESS_WXZC = "资产维修";
    /**
     * 流程启动名称 外部维修
     */
    public String PROCESS_WBWX = "外部维修";



    //耗材采购待审批
    public String HCCG_PANDING_APPROVE = "pagesA/RFID/hcDsp/index";
    //耗材申领待审批
    public String HCSL_PANDING_APPROVE= "pagesA/RFID/applyHcShenPi/index";
   //资产采购待审批
    public String ZCCG_PANDING_APPROVE = "pagesA/RFID/zccgdsp/index";
    //资产申领待审批
    public String ZCSL_PANDING_APPROVE = "pagesA/RFID/applyZcSqDsp/index";
    //资产变更待审批
    public String ZCBG_PANDING_APPROVE = "pagesA/RFID/applyChangeSp/index";
   //资产外携待审批
    public String ZCCARRY_PANDING_APPROVE = "pagesA/RFID/carrySq/index";
    //资产处置待审批
    public String ZCCZ_PANDING_APPROVE = "pagesA/RFID/applyCzSq/index";
    //资产维修待审批
    public String ZCWX_PANDING_APPROVE = "pagesA/RFID/zcwxdsp/index";
    //资产采购
    public String ZCCG_APPLY = "pagesA/RFID/assetsCgList/index";
    //领用申请
    public String LYSQ_APPLY = "pagesA/RFID/applyZc/index";
   //变更申请
    public String BGSQ_APPLY = "pagesA/RFID/zcbgsq/index";
   //处置申请
    public String CZSQ_APPLY = "pagesA/RFID/applyCz/index";
  //外携申请
    public String CARRY_APPLY = "pagesA/RFID/carryPage/index";
 //维修申请
    public String WXSQ_APPLY = "pagesA/RFID/applyWeixiu/index";
   //耗材采购
    public String HCCG_APPLY = "pagesA/RFID/haocaiCgList/index";
   //耗材申领
    public String HCSL_APPLY = "pagesA/RFID/applyHc/index";
}
