package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.SysPostDao;
import com.jsdc.rfid.mapper.SysPostMapper;
import com.jsdc.rfid.model.SysPost;
import com.jsdc.rfid.model.SysPostPermission;
import com.jsdc.rfid.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.SysPostVo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SysPostService extends BaseService<SysPostDao, SysPost> {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPostPermissionService postPermissionService;
    @Autowired
    private SysPostMapper postMapper;

    /**
     * 分页查询
     * @param post
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<SysPostVo> getPage(SysPost post, Integer pageIndex, Integer pageSize){
        PageHelper.startPage(pageIndex, pageSize);
        List<SysPostVo> list = postMapper.getPage(post);
        return new PageInfo<>(list);
    }

    /**
     * 列表查询
     * @param post
     * @return
     */
    public List<SysPost> getList(SysPost post){
        return selectList(getWrapper(post));
    }

    /**
     * 新增岗位信息
     * @param post
     * @return
     */
    public SysPost add(SysPost post){
        SysUser user = userService.getUser();
        post.setCreate_time(new Date());
        post.setCreate_user(user.getId());
        post.setUpdate_time(new Date());
        post.setUpdate_user(user.getId());
        post.setIs_del(G.ISDEL_NO);
        post.setSign_data_permission(0);
        post.setIs_enable(G.ISENABLE_YES);
        post.setData_permission((null == post.getData_permission()) ? 0 : post.getData_permission());
        if(insert(post) > 0){
            postPermissionService.add(post.getId(), post.getPermissionIds());
            return post;
        }
        return null;
    }

    /**
     * 编辑岗位信息
     * @param post
     * @return
     */
    public SysPost edit(SysPost post){
        SysUser user = userService.getUser();
        SysPost original = selectById(post.getId());
        long oa_post_id = original.getOa_post_id();
        BeanUtils.copyProperties(post, original);
        original.setOa_post_id(oa_post_id);
        original.setUpdate_user(user.getId());
        original.setUpdate_time(new Date());
        if(updateById(original) > 0){
            postPermissionService.edit(original.getId(), post.getPermissionIds());
            return post;
        }
        return null;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Boolean del(Integer id){
        SysUser user = userService.getUser();
        SysPost post = selectById(id);
        post.setUpdate_time(new Date());
        post.setUpdate_user(user.getId());
        post.setIs_del(G.ISDEL_YES);
        return updateById(post) > 0;
    }

    /**
     * 根据岗位id查询绑定的权限
     * @param id
     * @return
     */
    public List<Integer> getPermissions(Integer id){
        SysPost post = selectById(id);
        List<SysPostPermission> postPermissions = postPermissionService.selectList(
                Wrappers.<SysPostPermission>lambdaQuery().eq(SysPostPermission::getPost_id, id).eq(SysPostPermission::getIs_del, G.ISDEL_NO)
        );
        return postPermissions.stream().map(SysPostPermission::getPermission_id).collect(Collectors.toList());
    }

    /**
     * 统一获取查询条件
     * @param post
     * @return
     */
    private LambdaUpdateWrapper<SysPost> getWrapper(SysPost post){
        LambdaUpdateWrapper<SysPost> wrapper = new LambdaUpdateWrapper<>();
        if(null != post){
            if(StringUtils.isNotEmpty(post.getPost_code())){
                wrapper.like(SysPost::getPost_code, post.getPost_code());
            }
            if(StringUtils.isNotEmpty(post.getPost_name())){
                wrapper.like(SysPost::getPost_name, post.getPost_name());
            }
            if(null != post.getDept_id()){
                wrapper.eq(SysPost::getDept_id, post.getDept_id());
            }
        }
        wrapper.eq(SysPost::getIs_del, G.ISDEL_NO);
        wrapper.eq(SysPost::getIs_enable, G.ISENABLE_YES);
        return wrapper;
    }
}
