package com.jsdc.rfid.service;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.SysUserDao;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.model.warehouse.WarehousingEnter;
import com.jsdc.rfid.model.warehouse.WarehousingEnterDetail;
import com.jsdc.rfid.utils.UnifiedPortalUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;
import vo.SysUserVo;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yanbin123
 * @since 2019-08-14
 */
@Slf4j
@Service
@Transactional
public class SysUserService extends BaseService<SysUserDao, SysUser> {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private RFIDConfigMapper rfidConfigMapper;

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Autowired
    private FileManageMapper fileManageMapper;

    @Value("${file.upload-path}")
    private String uploadPath;

    /**
     * 分页查询
     *
     * @param user
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<SysUserVo> getPage(SysUser user, Integer pageIndex, Integer pageSize) {
        List<String> deptIds = sysDepartmentService.getTreeId(getUser().getDepartment()).stream().map(d -> d.toString()).collect(Collectors.toList());
        if(getUser().getPost() != 1){
            user.setDeptIds(deptIds);
        }
        PageHelper.startPage(pageIndex, pageSize, " CASE \n" +
                "    WHEN login_name like '%admin%' THEN 0\n" +
                "    ELSE 1\n" +
                "  END, oa_is_use asc, id desc");
        List<SysUserVo> list = sysUserMapper.getList(user);
        return new PageInfo<>(list);
    }

    /**
     * 列表查询
     *
     * @param user
     * @return
     */
    public List<SysUser> getList(SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = getWrapper(user);
        return selectList(wrapper);
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    public SysUser add(SysUser user) {
        if (validate(user)) {
            List<SysUser> list = selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getLogin_name, user.getLogin_name()).eq(SysUser::getIs_del, G.ISDEL_NO));
            if (!CollectionUtils.isEmpty(list)) {
                return null;
            }
            SysUser sysUser = getUser();
            user.setPassword(encryptPassword(user.getLogin_name(), user.getPassword()));
            user.setCreate_time(new Date());
            user.setCreate_user(sysUser.getId());
            user.setUpdate_time(new Date());
            user.setUpdate_user(sysUser.getId());
            user.setIs_del(G.ISDEL_NO);
            user.setIs_enable(G.ISENABLE_YES);
            if (insert(user) > 0) {
                return user;
            }
        }
        return null;
    }
    /**
     * 验证手机号是否唯一
     */
    public boolean phoneVerify(Integer id, String phone){
        int num = sysUserMapper.selectCount(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getTelephone, phone)
                .ne(null != id, SysUser::getId, id)
                .eq(SysUser::getIs_del, "0")
        );
        return num > 0;
    }

    /**
     * 密码加密
     *
     * @param loginName
     * @param password
     * @return
     */
    public static String encryptPassword(String loginName, String password) {
        return new Md5Hash(password).toHex();
    }

    /**
     * 编辑用户
     *
     * @param user
     * @return
     */
    public SysUser edit(SysUser user) {
        if (validate(user)) {
            List<SysUser> list = selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getLogin_name, user.getLogin_name()).ne(SysUser::getId, user.getId()).eq(SysUser::getIs_del, G.ISDEL_NO));
            if (!CollectionUtils.isEmpty(list)) {
                return null;
            }
            SysUser sysUser = getUser();
            SysUser original = selectById(user.getId());
            if (null != user.getIs_dept_leader() && user.getIs_dept_leader() == G.ISENABLE_YES) {
                update(null , Wrappers.<SysUser>lambdaUpdate()
                        .set(SysUser::getIs_dept_leader, G.ISENABLE_NO)
                        .set(SysUser::getIsloginty, "0")
                        .eq(SysUser::getDepartment, user.getDepartment())
                        .ne(SysUser::getId, user.getId()));
            }
            long oa_user_id = original.getOa_user_id();
            BeanUtils.copyProperties(user, original);

            //加密
            if (!user.getPassword().equals(sysUserMapper.selectById(sysUser.getId()).getPassword())) {
                original.setPassword(encryptPassword(user.getUser_name(), user.getPassword()));
            }

            original.setOa_user_id(oa_user_id);
            original.setUpdate_user(sysUser.getId());
            original.setUpdate_time(new Date());
            if (updateById(original) > 0) {
                return original;
            }
        }
        return null;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public Boolean delete(@NonNull Integer id) {
        if (Objects.equals(getUser().getId(), id)){
            throw new RuntimeException("用户自己不能删除自己账号");
        }
        SysUser user = selectById(id);
        SysUser sysUser = getUser();
        user.setUpdate_time(new Date());
        user.setUpdate_user(sysUser.getId());
        user.setIs_del(G.ISDEL_YES);

        if(StringUtils.equals("1",user.getIsloginty())){
            try {
                user.setIsloginty("0");
                syncUser(user);
            } catch (RuntimeException e) {
                throw new RuntimeException("同步数据的服务器异常,请检查统一门户");
            }
        }

        return updateById(user) > 0;
    }

    /**
     * 登录
     */
    public Boolean login(String userName, String passWord) {
        QueryWrapper<SysUser> queryWrapper = dao.queryByName(userName, encryptPassword(userName, passWord));
        SysUser user = sysUserMapper.selectOne(queryWrapper.eq("is_del", G.ISDEL_NO));
        if (user == null) {
            return false;
        }
        StpUtil.login(user.getId());
        return true;
    }

    /**
     * 登录
     */
    public Boolean login(String userName) {
        QueryWrapper<SysUser> queryWrapper = dao.queryByName(userName);
        SysUser user = sysUserMapper.selectOne(queryWrapper.eq("is_del", G.ISDEL_NO));
        if (user == null) {
            return false;
        }
        StpUtil.login(user.getId());
        return true;
    }

    /**
     * 统一门户登录
     */
    public Boolean logintymh(String phone) {
        List<SysUser> users = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery()
                        .eq(SysUser::getTelephone, phone)
                .eq(SysUser::getIs_del, G.ISDEL_NO));
        if (CollectionUtils.isEmpty(users)) {
            return false;
        }
        SysUser user = users.get(0);
        Subject subject = SecurityUtils.getSubject();
        // 利用Host属性存放其他属性，用逗号分隔保存，第二位是userId
        String host = "0.0.0.0," + user.getId();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getLogin_name(), user.getPassword(), true, host);
        subject.login(token);
        return true;
    }

    /**
     * 登录
     */
    public Boolean OAlogin(String userName, String passWord) {
        QueryWrapper<SysUser> queryWrapper = dao.queryByName(userName, passWord);
        SysUser user = sysUserMapper.selectOne(queryWrapper);
        if (user == null) {
            return false;
        }
        Subject subject = SecurityUtils.getSubject();
        // 利用Host属性存放其他属性，用逗号分隔保存，第二位是userId
        String host = "0.0.0.0," + user.getId();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getLogin_name(), user.getPassword(), true, host);
        subject.login(token);
        return true;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public SysUser getUser() {
        if(StpUtil.getLoginId() != null){
            SysUser user = this.selectById((Serializable) StpUtil.getLoginId());
            if (null == user) {
                return null;
            }
            if (null != user.getDepartment()){
                user.setDept_name(sysDepartmentMapper.selectById(user.getDepartment()).getDept_name());
            }
            SysPost post = sysPostMapper.selectById(user.getPost());
            if(null != post && post.getUser_type() == null){
                //默认为普通员工类型
                post.setUser_type(2);
            }
            user.setSysPost(post);
            return user;
        }
        return null;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public SysUser getUser(Integer userId) {
        SysUser user = selectById(userId);
        if (null == user) {
            return null;
        }
        if (null != user.getDepartment()){
            user.setDept_name(sysDepartmentMapper.selectById(user.getDepartment()).getDept_name());
        }
        SysPost post = sysPostMapper.selectById(user.getPost());
        if(null != post && post.getUser_type() == null){
            //默认为普通员工类型
            post.setUser_type(2);
        }
        user.setSysPost(post);

        return user;
    }

    /**
     * 退出登录
     */
    public void loginOut() {
        Subject subject = SecurityUtils.getSubject();

        subject.logout();
    }

    /**
     * 统一处理 查询条件
     *
     * @param user
     * @return
     */
    private LambdaQueryWrapper<SysUser> getWrapper(SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (null != user) {
            if (StringUtils.isNotEmpty(user.getLogin_name())) {
                wrapper.like(SysUser::getLogin_name, user.getLogin_name());
            }
            if (StringUtils.isNotEmpty(user.getUser_name())) {
                wrapper.like(SysUser::getUser_name, user.getUser_name());
            }
            if (null != user.getDepartment()) {
                wrapper.eq(SysUser::getDepartment, user.getDepartment());
            }
            if (null != user.getPost()) {
                wrapper.eq(SysUser::getPost, user.getPost());
            }
        }
        wrapper.eq(SysUser::getIs_del, G.ISDEL_NO);
        wrapper.eq(SysUser::getIs_enable, G.ISENABLE_YES);
        return wrapper;
    }

    /**
     * 验证必填项
     *
     * @param user
     * @return
     */
    private Boolean validate(SysUser user) {
        return !StringUtils.isEmpty(user.getUser_name()) && !StringUtils.isEmpty(user.getLogin_name())
                && null != user.getDepartment()
                && null != user.getPost();
    }

    /**
     * 修改密码
     *
     * @param originalPassword
     * @param newPassword
     * @return
     */
    public Boolean editPassword(String originalPassword, String newPassword) {
        SysUser user = getUser();
        originalPassword = encryptPassword(user.getLogin_name(), originalPassword);
        if (user.getPassword().equals(originalPassword)) {
            user.setPassword(encryptPassword(user.getLogin_name(), newPassword));
            user.setUpdate_user(user.getId());
            user.setUpdate_time(new Date());
            return updateById(user) > 0;
        }
        return false;
    }

    /**
     * 微信小程序
     * 修改密码
     */
    public JSONObject updPassword(Integer userId, String originalPassword, String newPassword) {
        JSONObject jsonObject = new JSONObject();
        if (!validatePassword(newPassword)) {
            jsonObject.put("flag", false);
            jsonObject.put("msg", "修改为弱密码");
            return jsonObject;
        }
        SysUser user = selectById(userId);
        originalPassword = encryptPassword(user.getLogin_name(), originalPassword);
        if (user.getPassword().equals(originalPassword)) {
            user.setPassword(encryptPassword(user.getLogin_name(), newPassword));
            user.setUpdate_user(user.getId());
            user.setUpdate_time(new Date());
            user.setSign_openid("");
            if (updateById(user) > 0) {
                jsonObject.put("msg", "修改密码成功");
                jsonObject.put("flag", true);
                return jsonObject;
            }
        }
        jsonObject.put("msg", "旧密码错误");
        jsonObject.put("flag", false);
        return jsonObject;
    }

    /**
     * 重置修改密码
     *
     * @param originalPassword
     * @param newPassword
     * @return
     */
    public Boolean resetPassword(String originalPassword, String newPassword) {
        if (!validatePassword(newPassword)) {
            return false;
        }
        SysUser user = getUser();

        user.setPassword(encryptPassword(user.getLogin_name(), newPassword));
        user.setUpdate_user(user.getId());
        user.setUpdate_time(new Date());
        return updateById(user) > 0;
    }

    /**
     * 验证密码复杂度
     *
     * @param pwd
     * @return
     */
    public Boolean validatePassword(String pwd) {
        //todo
        return true;
    }


    /**
     * 判断是否为强密码
     * 强密码规则：包含数字、字母，长度6-12位
     *
     * @param str
     * @return
     */
    public boolean isStongPassword(String str) {
        //密码的正则规则 6到12位  包含数字 字母
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z(`~!@#$%^&*()+=|{}':;',\\[\\].<>/?\\\\-_)*]{6,12}$";
        return str.matches(regex);
    }

    public boolean checkSpecialChar(String str) throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx = ".*[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public SysUser findByOpenId(String openId) {
        if (StringUtils.isNotBlank(openId)) {

            return selectOne(Wrappers.<SysUser>lambdaQuery().
                    eq(SysUser::getIs_del, "0").
                    eq(SysUser::getOpenid, openId));
        }

        return null;
    }

    public SysUser editInitPassword(Integer userId, String newPassword) {
        SysUser user = selectById(userId);
        user.setPassword(encryptPassword(user.getLogin_name(), newPassword));
        user.setUpdate_user(user.getId());
        user.setUpdate_time(new Date());
        if (updateById(user) > 0) {
            return user;
        }
        return null;

    }

    /**
     * 同步统一登录
     *
     * @param user
     */
    public void syncUser(SysUser user) throws RuntimeException{
        RFIDConfig rfid = rfidConfigMapper.selectById(1);
        if (null == rfid){
            throw new RuntimeException("请检查配置表是否有数据");
        }
        try {
            // 2.找到统一门户配置
            if(StringUtils.isBlank(rfid.getToken()) || null == rfid.getTokendate()
                    || !UnifiedPortalUtils.isSameDay(rfid.getTokendate(), new Date())){
                if (StringUtils.isBlank(rfid.getPortalurl()) || StringUtils.isBlank(rfid.getAppid()) || StringUtils.isBlank(rfid.getPrivatekey())){
                    log.info("配置文件的统一门户没有配置,请先配置.");
                }else {
                    String url = rfid.getPortalurl();
                    String token = UnifiedPortalUtils.getToken(url, rfid.getAppid(), rfid.getPrivatekey());
                    if (net.hasor.utils.StringUtils.equals("500",token)){
                        log.error("统一门户获取token错误,请检查参数接口等信息.");
                    }else {
                        rfid.setToken(token);
                        rfid.setTokendate(new Date());
                        rfidConfigMapper.updateById(rfid);
                        log.info("2.统一门户配置已经找到,并且token已经获取.");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("统一门户配置不正确: " + e.getMessage());
        }
        String card_no = "320311202308012222";
        // 启用1,禁用0
        String enable = StringUtils.equals("1", user.getIsloginty())?"1":"2";
        SysUser oldUser = sysUserMapper.selectById(user.getId());

        JSONObject param = new JSONObject();
        param.put("biz_id", user.getId() + "");
        param.put("loginName", oldUser.getTelephone());
        param.put("card_no", card_no);
        param.put("phone", oldUser.getTelephone());
        param.put("operateType", enable);

        List<JSONObject> params = new ArrayList<>();
        params.add(param);
        UnifiedPortalUtils.syncAccountInfo(rfid.getPortalurl(), rfid.getToken(),params);

    }

    public List<SysUser> syncUsers() throws RuntimeException{
        RFIDConfig rfid = rfidConfigMapper.selectById(1);
        if (null == rfid){
            throw new RuntimeException("请检查配置表是否有数据");
        }
        try {
            // 2.找到统一门户配置
            if(StringUtils.isBlank(rfid.getToken()) || null == rfid.getTokendate()
                    || !UnifiedPortalUtils.isSameDay(rfid.getTokendate(), new Date())){
                if (StringUtils.isBlank(rfid.getPortalurl()) || StringUtils.isBlank(rfid.getAppid()) || StringUtils.isBlank(rfid.getPrivatekey())){
                    log.info("配置文件的统一门户没有配置,请先配置.");
                }else {
                    String url = rfid.getPortalurl();
                    String token = UnifiedPortalUtils.getToken(url, rfid.getAppid(), rfid.getPrivatekey());
                    if (net.hasor.utils.StringUtils.equals("500",token)){
                        log.error("统一门户获取token错误,请检查参数接口等信息.");
                    }else {
                        rfid.setToken(token);
                        rfid.setTokendate(new Date());
                        rfidConfigMapper.updateById(rfid);
                        log.info("2.统一门户配置已经找到,并且token已经获取.");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("统一门户配置不正确: " + e.getMessage());
        }
        String card_no = "320311202308012222";
        List<JSONObject> params = new ArrayList<>();

        // 查询符合条件的用户
        List<SysUser> users = sysUserMapper.selectList(Wrappers.<SysUser>query()
                .eq("is_del",G.ISDEL_NO)
                // 电话不等于null
                .isNotNull("telephone")
                // 电话号码不能重复 where telephone in (select telephone from sys_user where is_del = 0 group by telephone having count(telephone) > 1)
                .inSql("telephone","select telephone from sys_user where is_del = 0 group by telephone having count(telephone) = 1")
                // 验证手机号码必须是11位字符
                .apply("LENGTH(telephone) = 11")
                // 排除 isloginty 为1的
                .ne("isloginty","1")
        );
        if (CollectionUtils.isEmpty(users)){
            throw new RuntimeException("满足的用户已经全部同步,无需同步操作.");
        }

        for (SysUser user : users){
            JSONObject param = new JSONObject();
            param.put("biz_id", user.getId() + "");
            param.put("loginName", user.getTelephone());
            param.put("card_no", card_no);
            param.put("phone", user.getTelephone());
            param.put("operateType", "1");
            params.add(param);
        }

        try {
            UnifiedPortalUtils.syncAccountInfo(rfid.getPortalurl(), rfid.getToken(),params);
            if (CollectionUtils.isEmpty(users)){
                return users;
            }
            // 同步成功后,更新数据库
            sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                    .in(SysUser::getId, users.stream().map(SysUser::getId).collect(Collectors.toList()))
                    .set(SysUser::getIsloginty, "1")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users;

    }


    public ResultInfo toImport(Integer fileId, String is_save){
        SysUser sysUser = getUser();
        FileManage fileManage = fileManageMapper.selectById(fileId);
        String path = uploadPath + File.separator + fileManage.getFile_name();

        //部门 保留最后一个值
        List<SysDepartment> departmentList = sysDepartmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        Map<String, Integer> deptMap = departmentList.stream().collect(Collectors.toMap(SysDepartment::getDept_name, SysDepartment::getId, (oldValue, newValue) -> newValue));
        //角色 默认装备管理员
        List<SysPost> sysPostList = sysPostMapper.selectList(Wrappers.<SysPost>lambdaQuery().eq(SysPost::getPost_name,"装备管理员"));
        Integer sysPostId = sysPostList != null && sysPostList.size() > 0 ? sysPostList.get(0).getId() : -1;
        //初始化导入 清空用户表数据
        UpdateWrapper<SysUser> sysUserUpdateWrapper = new UpdateWrapper<>();
        sysUserUpdateWrapper.eq("create_user",getUser().getId());
        sysUserUpdateWrapper.eq("is_import",G.ISIMPORT_YES);
        sysUserUpdateWrapper.set("is_del",G.ISDEL_YES);
        this.update(new SysUser(),sysUserUpdateWrapper);

        //初始化导入 清空部门表数据
        UpdateWrapper<SysDepartment> sysDepartmentUpdateWrapper = new UpdateWrapper<>();
        sysDepartmentUpdateWrapper.eq("create_user",getUser().getId());
        sysDepartmentUpdateWrapper.eq("is_import",G.ISIMPORT_YES);
        sysDepartmentUpdateWrapper.set("is_del",G.ISDEL_YES);
        sysDepartmentService.delete(sysDepartmentUpdateWrapper);

        File file = new File(path);

        try {
            ExcelReader reader = ExcelUtil.getReader(file);
            List<Map<String, Object>> readAll = reader.readAll();
            if (org.springframework.util.CollectionUtils.isEmpty(readAll)) {
                throw new RuntimeException("导入的数据为空");
            }
            List<SysUser> list = new ArrayList<>();
            // 设置errorList，加入校验失败的行号和原因
            List<Map<String, Object>> errorList = new ArrayList<>();
            for (Map<String, Object> map : readAll) {
                // map的所有value 去除前后空格
                map.forEach((k, v) -> map.put(k, null == v ? "" :v.toString().trim()));
                // 行号
                map.put("rowNum", readAll.indexOf(map) + 2);
                // 错误集合
                List<String> errList = new ArrayList<>();

                SysUser user  = new SysUser();
                user.setIs_import("1");
                String code = null == map.get("身份证号码") ? "" : map.get("身份证号码").toString();
                if (StringUtils.isBlank(code)) {
                    errList.add("身份证号码不能为空 ");
                    map.put("身份证号码tip", "身份证号码不能为空 ");
                } else {
                    user.setUser_name(code);
                    user.setLogin_name(code);
                    user.setPassword("123456");
                }

                String first = null == map.get("本人所在机构（一级）") ? "" : map.get("本人所在机构（一级）").toString();
                String two = null == map.get("本人所在机构（二级）") ? "" : map.get("本人所在机构（二级）").toString();
                String three = null == map.get("本人所在机构（三级）") ? "" : map.get("本人所在机构（三级）").toString();
                if (StringUtils.isBlank(first)) {
                    errList.add("本人所在机构（一级）不能为空 ");
                    map.put("本人所在机构（一级）tip", "本人所在机构（一级）不能为空 ");
                } else{
                    if(deptMap.get(first) == null){
                        SysDepartment sysDepartment = new SysDepartment();
                        sysDepartment.setDept_name(first);
                        sysDepartment.setLevel(1);

                        setDepartment(sysUser, deptMap, first, sysDepartment);
                    }
                }
                if (StringUtils.isBlank(two)) {
                    errList.add("本人所在机构（二级）不能为空 ");
                    map.put("本人所在机构（二级）tip", "本人所在机构（二级）不能为空 ");
                }

                if(StringUtils.isNotEmpty(first) && StringUtils.isNotEmpty(two)){
                    if(deptMap.get(two) == null){
                        SysDepartment sysDepartment = new SysDepartment();
                        sysDepartment.setDept_name(two);
                        sysDepartment.setLevel(2);
                        sysDepartment.setParent_dept(deptMap.get(first));
                        setDepartment(sysUser, deptMap, two, sysDepartment);

                        user.setDepartment(sysDepartment.getId());
                    } else {
                        user.setDepartment(deptMap.get(two));
                    }
                }

                if(StringUtils.isNotEmpty(first) && StringUtils.isNotEmpty(two) && StringUtils.isNotEmpty(three)){
                    if(deptMap.get(three) == null){
                        SysDepartment sysDepartment = new SysDepartment();
                        sysDepartment.setDept_name(three);
                        sysDepartment.setLevel(3);
                        sysDepartment.setParent_dept(deptMap.get(two));
                        setDepartment(sysUser, deptMap, three, sysDepartment);

                        user.setDepartment(sysDepartment.getId());
                    } else {
                        user.setDepartment(deptMap.get(three));
                    }


                }


                if (org.springframework.util.CollectionUtils.isEmpty(errList))  {
                    list.add(user);
                }else{
                    map.put("error", errList);
                    errorList.add(map);
                }
            }
            if ((StringUtils.isNotEmpty(is_save) && ("1").equals(is_save))) {
                for (SysUser user : list) {
                    user.setPost(sysPostId);
                    user.setPassword(encryptPassword(user.getLogin_name(), user.getPassword()));
                    user.setCreate_time(new Date());
                    user.setCreate_user(sysUser.getId());
                    user.setUpdate_time(new Date());
                    user.setUpdate_user(sysUser.getId());
                    user.setIs_del(G.ISDEL_NO);
                    user.setIs_enable(G.ISENABLE_YES);
                    insert(user);
                }
            } else {
                if (!org.springframework.util.CollectionUtils.isEmpty(errorList)) {
                    return ResultInfo.builder().code(0).msg("导入失败").data(errorList).build();
                }
                for (SysUser user : list) {
                    user.setPost(sysPostId);
                    user.setPassword(encryptPassword(user.getLogin_name(), user.getPassword()));
                    user.setCreate_time(new Date());
                    user.setCreate_user(sysUser.getId());
                    user.setUpdate_time(new Date());
                    user.setUpdate_user(sysUser.getId());
                    user.setIs_del(G.ISDEL_NO);
                    user.setIs_enable(G.ISENABLE_YES);
                    insert(user);
                }
            }


        } catch (Exception e) {
            return ResultInfo.error("导入失败: " + e.getMessage());
        }
        return ResultInfo.success();
    }

    private void setDepartment(SysUser sysUser, Map<String, Integer> deptMap, String deptName, SysDepartment sysDepartment) {
        sysDepartment.setDept_code(IdUtil.fastSimpleUUID());
        sysDepartment.setIs_import(G.ISIMPORT_YES);
        sysDepartment.setCreate_time(new Date());
        sysDepartment.setCreate_user(sysUser.getId());
        sysDepartment.setUpdate_time(new Date());
        sysDepartment.setUpdate_user(sysUser.getId());
        sysDepartment.setIs_del(G.ISDEL_NO);
        sysDepartment.setIs_enable(G.ISENABLE_YES);
        sysDepartment.setParent_dept(null == sysDepartment.getParent_dept() ? 0 : sysDepartment.getParent_dept());
        sysDepartmentService.insert(sysDepartment);
        deptMap.put(deptName, sysDepartment.getId());
    }

    public static Integer getFirstMatchValue(String input, Map<String, Integer> deptMap) {
        if (input == null || deptMap == null) return null;
        for (String key : deptMap.keySet()) {
            if (key.contains(input)) {
                return deptMap.get(key);
            }
        }
        return null; // 无匹配项
    }

}
