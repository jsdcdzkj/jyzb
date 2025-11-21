package com.jsdc.rfid.service;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.SysPermissionDao;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.model.SysPermission;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SysPermissionService extends BaseService<SysPermissionDao, SysPermission> {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPostPermissionService postPermissionService;

    public JSONArray getTree(Integer id) {
        SysPermission sysPermission = new SysPermission();
        String ids = postPermissionService.getPermissionIds(id);
        sysPermission.setIs_show(1);
        sysPermission.setIds(ids);
        return getTree(sysPermission);
    }

    public JSONArray getTree(SysPermission sysPermission) {
        // 查询所有未删除的权限记录
        List<SysPermission> allSysPermission = selectList(Wrappers.<SysPermission>lambdaQuery()
                .eq(SysPermission::getIs_del, "0"));

        // 解析传入的 ids 字符串为一个 Integer 列表
        List<Integer> idsList;
        if (sysPermission != null && sysPermission.getIds() != null) {
            idsList = Arrays.stream(sysPermission.getIds().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } else {
            idsList = new ArrayList<>();
        }

        // 根据传入的 SysPermission 进行模糊匹配和筛选
        List<SysPermission> filteredPermissions = allSysPermission.stream()
                .filter(permission -> {
                    // 检查名称是否符合条件：名称为空或匹配
                    boolean nameMatches = sysPermission == null || StringUtils.isEmpty(sysPermission.getPermission_name()) ||
                            permission.getPermission_name().contains(sysPermission.getPermission_name());

                    // 检查 is_show 是否符合条件：只包含 is_show 与传入值相同的权限
                    boolean isShowMatches = sysPermission == null || sysPermission.getIs_show() == null || permission.getIs_show() == null ||
                            permission.getIs_show().equals(sysPermission.getIs_show());

                    // 检查 ids 是否符合条件：如果传入了 ids 列表，则检查当前权限的 id 是否在列表中
                    boolean idsMatches = sysPermission == null || idsList.isEmpty() || idsList.contains(permission.getId());

                    // 检查父类ID是否符合条件：如果传入了父类ID，则进行匹配；如果没有传入，则忽略父类ID过滤
                    boolean parentIdMatches = sysPermission == null || sysPermission.getPermission_code() == null ||
                            permission.getPermission_code().equals(sysPermission.getPermission_code());

                    // 只有在名称、父类ID、is_show 和 ids 都满足条件时，才包含在结果中
                    return nameMatches && parentIdMatches && isShowMatches && idsMatches;
                })
                .collect(Collectors.toList());

        // 将筛选结果及其所有父级节点添加到目标集合中
        Set<SysPermission> targetPermissions = new HashSet<>(filteredPermissions);
        addParentDepartments(targetPermissions, allSysPermission);

        // 按照 sort 排序
        List<SysPermission> sortedPermissions = targetPermissions.stream()
                .sorted(Comparator.comparing(SysPermission::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        // 构建树形结构数据
        JSONArray treeData = new JSONArray();
        sortedPermissions.forEach(x -> {
            JSONObject item = new JSONObject();
            item.put("id", x.getId());
            item.put("permission_name", x.getPermission_name());
            item.put("label", x.getPermission_name());
            item.put("permission_code", x.getPermission_code());
            item.put("route_url", x.getRoute_url());
            item.put("route_name", x.getRoute_name());
            item.put("vue_path", x.getVue_path());
            item.put("redirect_type", x.getRedirect_type());
            item.put("sort", x.getSort());
            item.put("icon", x.getIcon());
            item.put("is_show", x.getIs_show());
            item.put("hasChildren", true);
            item.put("parent_permission", x.getParent_permission() == null || x.getParent_permission().equals(0) ? 0 : x.getParent_permission());
            treeData.add(item);
        });

        // 返回构建的树形结构
        return treeList(treeData, 0, 0);
    }

    // 递归查找并添加所有父级部门
    private void addParentDepartments(Set<SysPermission> departmentsTarget, List<SysPermission> allSysPermission) {
        // 使用队列迭代父节点
        Queue<Integer> parentIdsQueue = new LinkedList<>();
        departmentsTarget.stream()
                .map(SysPermission::getParent_permission)
                .filter(Objects::nonNull) // 确保父权限不为null
                .filter(parent_permission -> parent_permission != 0)
                .forEach(parentIdsQueue::offer);

        // 查找所有父节点并加入集合
        while (!parentIdsQueue.isEmpty()) {
            Integer parentId = parentIdsQueue.poll();

            if (parentId == null) {
                continue; // 跳过 null parentId
            }

            allSysPermission.stream()
                    .filter(sysPermission -> sysPermission != null && sysPermission.getId() != null && sysPermission.getId().equals(parentId))
                    .findFirst()
                    .ifPresent(parentDepartment -> {
                        if (parentDepartment != null && departmentsTarget.add(parentDepartment) && parentDepartment.getParent_permission() != null && parentDepartment.getParent_permission() != 0) {
                            parentIdsQueue.offer(parentDepartment.getParent_permission());
                        }
                    });
        }
    }

    // 递归构建树形结构
    public JSONArray treeList(JSONArray dataList, int parentId, int depth) {
        JSONArray children = new JSONArray();
        for (Object obj : dataList) {
            JSONObject jsonItem = (JSONObject) obj;
            int id = jsonItem.getInteger("id");
            int parent = jsonItem.getInteger("parent_permission");

            if (parentId == parent) {
                JSONArray childNodes = treeList(dataList, id, depth + 1);
                if (childNodes.isEmpty()) {
                    jsonItem.put("hasChildren", false);
                }
                jsonItem.put("children", childNodes);
                jsonItem.put("level", depth); // 为节点设置深度
                children.add(jsonItem);
            }
        }
        return children;
    }

    public JSONArray getPermissionByPost(Integer postId) {
        // 查询 SysPostPermission 列表
        List<SysPostPermission> list = postPermissionService.selectList(
                Wrappers.<SysPostPermission>lambdaQuery()
                        .eq(SysPostPermission::getPost_id, postId)
                        .eq(SysPostPermission::getIs_del, G.ISDEL_NO)
        );

        // 生成权限 ID 列表
        List<Integer> permissionIds = list.stream()
                .map(SysPostPermission::getPermission_id)
                .filter(Objects::nonNull) // 过滤掉可能的 null 值
                .collect(Collectors.toList());

        // 查询 SysPermission 列表
        List<SysPermission> permissions = selectList(
                Wrappers.<SysPermission>query()
                        .in("id", permissionIds)
                        .eq("is_del", G.ISDEL_NO)
                        .orderByAsc("sort")
        );

        // 过滤出 parent_permission 为 0 的父节点权限
        List<SysPermission> parent = permissions.stream()
                .filter(x -> x.getParent_permission() != null && x.getParent_permission() == 0) // 检查是否为 null
                .sorted(Comparator.comparing(SysPermission::getSort))
                .collect(Collectors.toList());

        JSONArray top = new JSONArray();

        // 遍历父节点，构建树结构
        parent.forEach(x -> {
            int id = x.getId();

            // 筛选出当前父节点的子节点权限
            List<SysPermission> children = permissions.stream()
                    .filter(y -> y.getParent_permission() != null && y.getParent_permission() == id) // 检查 null
                    .sorted(Comparator.comparing(SysPermission::getSort))
                    .collect(Collectors.toList());

            JSONObject item = new JSONObject();
            item.put("parent", x);
            item.put("children", children);
            top.add(item);
        });

        return top;
    }


    /**
     * 新增菜单
     *
     * @param sysPermission
     * @return
     */
    public SysPermission add(@NonNull SysPermission sysPermission) {
        SysUser user = userService.getUser();
        sysPermission.setCreate_time(new Date());
        sysPermission.setCreate_user(user.getId());
        sysPermission.setUpdate_time(new Date());
        sysPermission.setUpdate_user(user.getId());
        sysPermission.setIs_del(G.ISDEL_NO);
        if (insert(sysPermission) > 0) {
            return sysPermission;
        } else {
            return null;
        }
    }

    /**
     * 编辑菜单
     *
     * @param sysPermission
     * @return
     */
    public SysPermission edit(@NonNull SysPermission sysPermission) {
        SysUser user = userService.getUser();
        SysPermission original = selectById(sysPermission.getId());
        BeanUtils.copyProperties(sysPermission, original);
        original.setUpdate_user(user.getId());
        original.setUpdate_time(new Date());
        if (updateById(original) > 0) {
            return original;
        } else {
            return null;
        }
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    public Boolean delete(@NonNull Integer id) {
        SysUser user = new SysUser();
        SysPermission sysPermission = selectById(id);
        sysPermission.setUpdate_time(new Date());
        sysPermission.setUpdate_user(user.getId());
        sysPermission.setIs_del(G.ISDEL_YES);

        if(updateById(sysPermission) > 0){
            return true;
        }else{
            return false;
        }
    }
}
