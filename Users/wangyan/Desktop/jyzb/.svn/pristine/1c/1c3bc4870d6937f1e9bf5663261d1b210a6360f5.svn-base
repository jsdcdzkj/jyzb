package com.jsdc.rfid.dao;


import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ManHourAssign;
import com.jsdc.rfid.vo.ManHourAssignVo;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vo.PurchaseApplyVo;

@Repository
public class ManHourAssignDao extends BaseDao<ManHourAssign> {
    public String getPageList(ManHourAssignVo vo){
        String sql = "SELECT\n" +
                "\tmha.*,\n" +
                "\tfm1.store_name AS originFileName,\n" +
                "\tfm2.store_name AS newFileName \n" +
                "FROM\n" +
                "\t`man_hour_assign` AS mha\n" +
                "\tLEFT JOIN file_manage AS fm1 ON mha.originFileId = fm1.id\n" +
                "\tLEFT JOIN file_manage AS fm2 ON mha.newFileId = fm2.id" +
                "\twhere 1=1";
        if (!StringUtils.isEmpty(vo.getOriginFileName())) {
            sql += "\tAND fm1.store_name like '%" + vo.getOriginFileName() + "%'";
        }

        if (null != vo.getStartDate() && null != vo.getEndDate()) {
            sql += "\tAND (mha.createTime >= '" + vo.getStartDate() + " 00:00:00" + "' AND mha.createTime <= '"+ vo.getEndDate() + " 23:59:59" + "')";
        }

        return sql.toString();
    }
}
