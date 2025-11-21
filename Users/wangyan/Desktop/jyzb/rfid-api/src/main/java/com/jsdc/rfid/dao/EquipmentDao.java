package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.Equipment;
import org.springframework.stereotype.Repository;

@Repository
public class EquipmentDao extends BaseDao<Equipment> {

    /**
     * 获取ip和端口列表
     */
    public String getIpList(Equipment bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT concat_ws(':', t.ip, t.port) ip FROM equipment t where t.is_del = '0' ");
        if (notEmpty(bean)) {
            //用处 【字典 1扫描 2报警】
            if (notEmpty(bean.getEquipment_usage())) {
                buffer.append(" and t.equipment_usage = ").append(bean.getEquipment_usage());
            }
        }
        return buffer.toString();
    }

    public String getList(Equipment bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT t.*,p.position_name as positionName,p.lat,p.lng FROM equipment t ");
        buffer.append(" left join sys_position p on p.id=t.equipment_position ");
        buffer.append(" left join sys_place pa on pa.id=p.place_id ");
        buffer.append(" where t.is_del = '0' ");
        if (notEmpty(bean)) {
            if (notEmpty(bean.getEquipment_name())) {
                buffer.append(" and t.equipment_name like '%").append(bean.getEquipment_name().trim() + "%'");
            }
            if (notEmpty(bean.getEquipment_type())) {
                buffer.append(" and t.equipment_type = ").append(bean.getEquipment_type());
            }
            if (notEmpty(bean.getIp())) {
                buffer.append(" and t.ip like '%").append(bean.getIp().trim() + "%'");
            }
            if (notEmpty(bean.getMac())) {
                buffer.append(" and t.mac like '%").append(bean.getMac().trim() + "%'");
            }
            // 存放位置
            if (notEmpty(bean.getEquipment_position())) {
                buffer.append(" and t.equipment_position = ").append(bean.getEquipment_position());
            }
            //报警状态 1正常 2报警
            if (notEmpty(bean.getWarning_status())) {
                buffer.append(" and t.warning_status = ").append(bean.getWarning_status());
            }
            //设备状态 0:离线 1:在线
            if (notEmpty(bean.getStatus())) {
                buffer.append(" and t.status = ").append(bean.getStatus());
            }
            //用处 【字典 1扫描 2报警】
            if (notEmpty(bean.getEquipment_usage())) {
                buffer.append(" and t.equipment_usage = ").append(bean.getEquipment_usage());
            }
            //地点
            if (notEmpty(bean.getPlace_id())) {
                buffer.append(" and pa.id = ").append(bean.getPlace_id());
            }
        }
        return buffer.toString();
    }
}
