package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.rfid.mapper.FastEntranceMapper;
import com.jsdc.rfid.model.FastEntrance;
import com.jsdc.rfid.model.SysPermission;
import com.jsdc.rfid.model.SysUser;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.ResultInfo;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FastEntranceService extends ServiceImpl<FastEntranceMapper,FastEntrance> {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private FastEntranceMapper fastEntranceMapper;

    public ResultInfo saveFast(String ids) {
        SysUser user = userService.getUser();
        //删除此用户所有快捷入口菜单
        QueryWrapper<FastEntrance> qw = new QueryWrapper<FastEntrance>();
        qw.eq("userId", user.getId());
        remove(qw);
        //重新插入所有
        if (StringUtils.isNotBlank(ids)){
            ArrayList<FastEntrance> list = new ArrayList<>();
            String[] strArr = ids.split(",");
            for (String s : strArr) {
                FastEntrance fe = new FastEntrance();
                QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
                wrapper.eq("id",Integer.parseInt(s));
                SysPermission sysPermission = sysPermissionService.selectOne(wrapper);
                fe.setPermissionid(sysPermission.getId());
                fe.setUserid(user.getId());
                list.add(fe);
            }
            boolean b = saveBatch(list);
            if (b) {
                return ResultInfo.success();
            }else {
                return ResultInfo.error("保存失败");
            }
        }
        return ResultInfo.success();
    }

    public List<FastEntrance> showUserIcon() {
        SysUser user = userService.getUser();
        //查询用户所有快捷
        QueryWrapper<FastEntrance> wrapper = new QueryWrapper<>();
        wrapper.eq("userid",user.getId());
        List<FastEntrance> list = list(wrapper);
        list.forEach(x->{
            QueryWrapper<SysPermission> wrappers = new QueryWrapper<>();
            wrappers.eq("id",x.getPermissionid());
            SysPermission sysPermission = sysPermissionService.selectOne(wrappers);
            x.setPermissionName(sysPermission.getPermission_name());
            x.setPermissionUrl(sysPermission.getRoute_url());
            x.setPermissionIcon(sysPermission.getIcon());
        });
        return list;
    }
}
