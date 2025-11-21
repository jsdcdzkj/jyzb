package com.jsdc.rfid.common.enums;

public class CommonEnum {
    //角色类型英文名称
    public enum RoleGroupType {

        TYPE1("1", "超级管理人"),
        TYPE2("2", "患者"),
        TYPE3("3", "医生"),
        TYPE4("4", "药剂师"),
        TYPE5("5", "护士"),
        TYPE6("6", "医保局");
        private String type;
        private String desc;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        RoleGroupType(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }


    public enum CheckStatus {
        //1未提交2待三定3待审批4审批退回5待复核6复核退回7已入组
        TYPE1("1", "未提交"),
        TYPE2("2", "待三定"),
        TYPE3("3", "待审批"),
        TYPE4("4", "审批退回"),
        TYPE5("5", "待复核"),
        TYPE6("6", "复核退回"),
        TYPE7("7", "已入组"),
        TYPE8("8", "已过期"),
        TYPE9("9", "患者撤回"),
        TYPE10("10", "待三定"),
        TYPE11("11", "患者撤回");

        private String type;
        private String desc;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        CheckStatus(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    public enum InvalidCode {
        TYPE1("1", "康复出组"),
        TYPE2("2", "评估超期"),
        TYPE3("3", "入组到期"),
        TYPE4("4", "强制出组");
        private String type;
        private String desc;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        InvalidCode(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    public enum ActionType {
        //1药剂师提交 2 患者提交 3 医生审核 4 医保局审核 5 患者撤回
        TYPE1("1", "药剂师提交"),
        TYPE2("2", "患者提交"),
        TYPE3("3", "医生审核"),
        TYPE4("4", "医保局审核"),
        TYPE5("5", "患者撤回"),
        TYPE6("6", "患者提交");
        private String type;
        private String desc;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        ActionType(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    public enum CertificatStatus {
        // 1审核中(待审核)2驳回3通过4 90天过期
        TYPE1("1", "审核中"),
        TYPE2("2", "驳回"),
        TYPE3("3", "通过"),
        TYPE4("4", "90天过期");
        private String type;
        private String desc;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        CertificatStatus(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

}
