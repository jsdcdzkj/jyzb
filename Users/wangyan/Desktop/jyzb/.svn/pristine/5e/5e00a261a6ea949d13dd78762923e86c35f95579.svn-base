package com.jsdc.rfid.controller.app.sys;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.model.SysDepartment;
import com.jsdc.rfid.model.SysUser;
import com.jsdc.rfid.service.SysDepartmentService;
import com.jsdc.rfid.service.SysUserService;
import com.jsdc.rfid.vo.JsonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 *
 * @author zonglina
 */
@Controller
@RequestMapping("app/user")
public class UserController extends BaseController {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysDepartmentService dwService;


    //获取部门以及用户
    public String dwOrUser(Integer userId) {
        JSONObject jsonObject = new JSONObject();
        SysUser user = userService.selectById(userId);
        List<SysDepartment> dwList = dwService.getList(null);
        List<JsonVo> tv = new ArrayList<>();
        if (dwList.size() > 0) {
            for (int i = 0; i < dwList.size(); i++) {
                JsonVo typeVo = new JsonVo();
                typeVo.setName(dwList.get(i).getDept_name());
                typeVo.setValue(dwList.get(i).getId().toString());
                List<SysUser> userList = userService.selectList(new QueryWrapper<SysUser>().eq("department", dwList.get(i).getId()));
                List<Map> subs = new ArrayList<>();
                if (userList.size() > 0) {
                    for (int j = 0; j < userList.size(); j++) {
                        Map map = new HashMap();
                        map.put("id", userList.get(j).getId());
                        map.put("name", userList.get(j).getUser_name());
                        subs.add(map);
                    }
                }
                typeVo.setSub(subs);
                tv.add(typeVo);
            }
            jsonObject.put("list", tv);
            jsonObject.put("user", user);
        }
        return jsonObject.toJSONString(jsonObject);
    }

    //用户列表
    @RequestMapping(value = "userList.do", method = RequestMethod.POST)
    @ResponseBody
    public String userList(Integer userId, Integer dwId) {
        JSONObject jsonObject = new JSONObject();
        SysUser user = userService.selectById(userId);
        List<SysUser> userList = userService.getList(new SysUser(dwId));
        List<JsonVo> list = new ArrayList<>();
        for (SysUser u : userList) {
            JsonVo vo = new JsonVo();
            vo.setId(u.getId().toString());
            vo.setName(u.getUser_name());
            list.add(vo);
        }
        jsonObject.put("list", list);
        jsonObject.put("user", user);
        return jsonObject.toJSONString();
    }

}
