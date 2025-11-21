package com.jsdc.rfid.common.security;

import cn.dev33.satoken.stp.StpInterface;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsdc.rfid.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SaTokenRoles implements StpInterface {

    @Autowired
    private SysPermissionService sysUserRoleService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }
    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List getRoleList(Object loginId, String loginType) {
        JSONArray permission = sysUserRoleService.getPermissionByPost(Integer.parseInt(loginId+""));
        ArrayList list = new ArrayList<>();
        for (int i = 0; i < permission.size(); i++) {
            for (int j = 0; j < ((List)((JSONObject) permission.get(i)).get("children")).size(); j++) {
                list.add( ((List)((JSONObject) permission.get(i)).get("children")).get(j));
            }
        }
        return list;
    }
}
