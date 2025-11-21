package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.SysPostPermissionDao;
import com.jsdc.rfid.mapper.SysPostPermissionMapper;
import com.jsdc.rfid.model.SysPermission;
import com.jsdc.rfid.model.SysPostPermission;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SysPostPermissionService extends BaseService<SysPostPermissionDao, SysPostPermission> {

    @Autowired
    private SysPostPermissionMapper mapper;

    /**
     * 新增岗位菜单权限
     *
     * @param postId
     * @param menuIds
     */
    public void add(Integer postId, String menuIds) {
        List<String> ids = Arrays.asList(StringUtils.split(menuIds, G.STRING_SEPARATE));
        ids.forEach(x -> {
            SysPostPermission postPermission = new SysPostPermission();
            postPermission.setPermission_id(Integer.parseInt(x));
            postPermission.setPost_id(postId);
            postPermission.setIs_del(G.ISDEL_NO);
            insert(postPermission);
        });
    }

    /**
     * 编辑岗位菜单权限
     * 先删除权限再新增权限
     *
     * @param postId
     * @param menuIds
     */
    public void edit(Integer postId, String menuIds) {
        LambdaUpdateWrapper<SysPostPermission> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysPostPermission::getIs_del, G.ISDEL_YES)
                .eq(SysPostPermission::getPost_id, postId);
        update(null, updateWrapper);
        add(postId, menuIds);
    }

    public List<SysPostPermission> getList(SysPostPermission postPermission) {
        LambdaUpdateWrapper<SysPostPermission> wrapper = new LambdaUpdateWrapper<>();
        if (null != postPermission.getPost_id()) {
            wrapper.eq(SysPostPermission::getPost_id, postPermission.getPost_id());
        }
        wrapper.eq(SysPostPermission::getIs_del, G.ISDEL_NO);
        return selectList(wrapper);
    }


    /**
     * 根据当前登录的岗位查询菜单
     * @param post_id
     * @return
     */
    public List<String> selectByPostPermissionCode(String post_id) {
        return  mapper.selectByPostPermissionCode(post_id);
    }

    /**
     * 获取所有权限
     * @param id 角色岗位id
     * @return
     */
    public String getPermissionIds(Integer id) {
        // 查询相关的 SysPostPermission 列表
        List<SysPostPermission> sysPostPermissionList = selectList(Wrappers.<SysPostPermission>lambdaQuery()
                        .eq(SysPostPermission::getIs_del,G.ISDEL_NO)
                        .eq(SysPostPermission::getPost_id, id));

        // 将 SysPostPermission 列表中的 菜单id 转换为 List<Integer>
        List<Integer> ids = sysPostPermissionList.stream()
                .map(SysPostPermission::getPermission_id)
                .collect(Collectors.toList());

        // 将 List<Integer> 转换为以逗号分隔的字符串
        return ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
