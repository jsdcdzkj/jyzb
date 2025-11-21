package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.BorrowDao;
import com.jsdc.rfid.enums.AssetsStatusEnums;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.ResultInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class BorrowService extends BaseService<BorrowDao, Borrow> {

    @Autowired
    private BorrowMapper borrowMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BorrowAssetsMapper borrowAssetsMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPostMapper sysPostMapper;
    @Autowired
    private AssetsManageMapper assetsManageMapper;
    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;
    @Autowired
    private CommonDataTools commonDataTools;

    @Autowired
    private OperationRecordService operationRecordService;




    /**
     * 得到用户的键值对Map<id,SysUser>
     */
    public Map<Integer, SysUser> getUserNameMap() {
        List<SysUser> users = sysUserService.selectList(null);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyMap();
        }
        return users.stream().collect(Collectors.toMap(SysUser::getId, Function.identity(), (key1, key2) -> key2));
    }


    /**
     * 借用申请首页分页展示数据
     * @param borrow
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<Borrow> getPageInfo(Borrow borrow ,int pageIndex, int pageSize){
        Map<Integer, SysUser> userMap = getUserNameMap();
        PageHelper.startPage(pageIndex, pageSize);
        List<Borrow> list = borrowMapper.getPageInfo(borrow);
        if (!CollectionUtils.isEmpty(userMap)) {
            for (Borrow temp : list) {
                temp.setUse_name(StringUtils.EMPTY);
                temp.setHandle_name(StringUtils.EMPTY);
                if (null == temp.getUse_id() || userMap.get(temp.getUse_id()) == null && null == temp.getHandle_id() || userMap.get(temp.getHandle_id()) == null) {
                    continue;
                }
                temp.setUse_name(userMap.get(temp.getUse_id()).getUser_name());
                temp.setHandle_name(userMap.get(temp.getHandle_id()).getUser_name());

                int id = temp.getId();
                List<BorrowAssets> detail = borrowAssetsMapper.getBorrowAssets(id);
                if (detail != null) {
                    temp.setDetail(detail);
                }
            }
        }
        PageInfo<Borrow> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }


    /**
     * 借用单新增
     * @param borrow
     * @return
     */
    public ResultInfo addBorrow(Borrow borrow,List<BorrowAssets> list){
        //借用单号：JY+日期+四位自增码（每日重置）
        String code = commonDataTools.getNo(DataType.BORROW.getType(), null);
        SysUser sysUser = sysUserService.getUser();
        borrow.setUse_id(sysUser.getId());
        borrow.setDepartment_id(sysUser.getDepartment());
        borrow.setBorrow_code(code);
        borrow.setUse_date(new Date());
        borrow.setCreate_time(new Date());
        borrow.setCreate_user(sysUserService.getUser().getId());
        borrow.setIs_del(G.ISDEL_NO);
        borrow.setStatus(G.NOT_SUBMITTED);
        insert(borrow);

        //关联的资产
        for (BorrowAssets temp : list) {
            int id = temp.getAssets_id();
            BorrowAssets borrowAssets = new BorrowAssets();
            borrowAssets.setAssets_id(id);
            borrowAssets.setBorrow_id(borrow.getId());
            borrowAssets.setBorrow_status(G.TO_BE_COLLECTED);
            borrowAssets.setIs_del(G.ISDEL_NO);
            borrowAssets.setCreate_time(new Date());
            borrowAssets.setCreate_user(sysUserService.getUser().getId());
            borrowAssetsMapper.insert(borrowAssets);
            AssetsManage assetsManage = assetsManageMapper.selectById(id);
            operationRecordService.addOperationRecord(borrowAssets.getId(),assetsManage.getAsset_code(),"资产借用",OperationRecordService.MODE_TYPE_ASSETS);
        }
        return ResultInfo.success();
    }


    /**
     * 修改借用单
     * 作者：xuaolong
     *
     * @param borrow
     * @return
     */
    public ResultInfo updateBorrow(Borrow borrow,List<Integer> list){
        if (!G.NOT_SUBMITTED.equals(borrow.getStatus())) {
            return ResultInfo.error("借用单状态必须是未送审");
        }
        borrow.setUpdate_time(new Date());
        borrow.setUpdate_user(sysUserService.getUser().getId());
        updateById(borrow);

        UpdateWrapper<BorrowAssets> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("borrow_id",borrow.getId());
        updateWrapper.set("is_del",G.ISDEL_YES);
        borrowAssetsMapper.update(null,updateWrapper);

        //关联的资产
        Set<Integer> set = new HashSet<>();
        for (Integer temp : list){
            if (null != temp){
                set.add(temp);
            }
        }

        for (Integer temp : set) {
            int id = temp;
            BorrowAssets borrowAssets = new BorrowAssets();
            borrowAssets.setAssets_id(id);
            borrowAssets.setBorrow_id(borrow.getId());
            borrowAssets.setBorrow_status(G.TO_BE_COLLECTED);
            borrowAssets.setIs_del(G.ISDEL_NO);
            borrowAssets.setCreate_time(new Date());
            borrowAssets.setCreate_user(sysUserService.getUser().getId());
            borrowAssetsMapper.insert(borrowAssets);
        }
        return ResultInfo.success();
    }


    /**
     * 删除借用单
     * 作者：xuaolong
     * @param id
     * @return
     */
    public ResultInfo deleteBorrow(Integer id){
        Borrow borrow = borrowMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(borrow.getStatus())) {
            return ResultInfo.error("借用单状态必须是未送审");
        }
        borrow.setIs_del(G.ISDEL_YES);
        updateById(borrow);

        UpdateWrapper<BorrowAssets> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_del", G.ISDEL_YES);
        updateWrapper.eq("borrow_id", borrow.getId());
        borrowAssetsMapper.update(null, updateWrapper);
        return ResultInfo.success();
    }


    /**
     * 借用单送审
     * @param id
     * @return
     */
    public ResultInfo submitBorrow(Integer id){
        Borrow borrow = borrowMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(borrow.getStatus())) {
            return ResultInfo.error("借用单状态必须是未送审");
        }
        borrow.setStatus(G.NOT_APPROVED);
        updateById(borrow);
        return ResultInfo.success();
    }

    /**
     * 打印借用单单据
     * @param borrow
     * @return
     */
    public ResultInfo printBillBorrow(Borrow borrow){
        if (G.NOT_SUBMITTED.equals(borrow.getStatus()) || G.REJECTION_APPROVAL.equals(borrow.getStatus())) {
            return ResultInfo.error("借用单状态不能是未送审或者被退回的");
        }
        return ResultInfo.success(borrow);
    }


    /**
     * 分页展示借用管理
     * @param borrow
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultInfo collectionBorrowByPage(Borrow borrow, int pageIndex, int pageSize){
        SysUser sysUser = sysUserService.getUser();
        int userId = sysUser.getId();
        Integer postId = sysUserMapper.selectById(userId).getPost();
        if (null == postId) {
            return ResultInfo.error("没有权限查看");
        }
        SysPost sysPost = sysPostMapper.selectById(postId);
        if (null == sysPost) {
            return ResultInfo.error("没有权限查看");
        }
        Integer data_permission = sysPost.getData_permission();

        List<Borrow> list = new ArrayList<>();

        Map<Integer, SysUser> userMap = getUserNameMap();
        PageHelper.startPage(pageIndex, pageSize);
        //仅查看个人通过审核数据
        if (G.DATAPERMISSION_PERSONAL == data_permission) {
            list = borrowMapper.collectionBorrowByPage(borrow, userId, null);
            //查看本部门数据
        } else if (G.DATAPERMISSION_DEPT == data_permission) {
            int department = sysUser.getDepartment();
            list = borrowMapper.collectionBorrowByPage(borrow, null, department);
            //查看所有部门数据
        } else {
            list = borrowMapper.collectionBorrowByPage(borrow, null, null);
        }

        if (!CollectionUtils.isEmpty(userMap)) {
            for (Borrow temp : list) {
                temp.setUse_name(StringUtils.EMPTY);
                temp.setHandle_name(StringUtils.EMPTY);
                if (null == temp.getUse_id() || userMap.get(temp.getUse_id()) == null && null == temp.getHandle_id() || userMap.get(temp.getHandle_id()) == null) {
                    continue;
                }
                temp.setUse_name(userMap.get(temp.getUse_id()).getUser_name());
                temp.setHandle_name(userMap.get(temp.getHandle_id()).getUser_name());

                int id = temp.getId();
                List<BorrowAssets> detail = borrowAssetsMapper.getBorrowAssets(id);
                if (detail != null) {
                    temp.setDetail(detail);
                }
            }
        }
        PageInfo<Borrow> pageInfo = new PageInfo<>(list);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 根据Id查询借用单详情
     * @param id
     * @return
     */
    public Borrow  getOneInfo(Integer id){
        Borrow borrow = borrowMapper.selectById(id);
        Integer dId = borrow.getDepartment_id();
        if (null != dId){
            SysDepartment sysDepartment = sysDepartmentMapper.selectById(dId);
            if (null != sysDepartment){
                borrow.setDepartment_name(sysDepartment.getDept_name());
            }
        }

        if (null != borrow.getUse_id()){
            SysUser sysUser = sysUserMapper.selectById(borrow.getUse_id());
            if (null != sysUser){
                borrow.setUse_name(sysUser.getUser_name());
            }
        }


        if (null != borrow.getHandle_id()){
            SysUser sysUser = sysUserMapper.selectById(borrow.getHandle_id());
            if (null != sysUser){
                borrow.setHandle_name(sysUser.getUser_name());
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (null != borrow.getCreate_time()){
            borrow.setReal_create_time(simpleDateFormat.format(borrow.getCreate_time()));
        }else {
            borrow.setReal_create_time("暂无数据");
        }

        if (null != borrow.getEstimate_return_date()){
            borrow.setReal_return_date(simpleDateFormat.format(borrow.getEstimate_return_date()));
        }else {
            borrow.setReal_return_date("暂无数据");
        }
        if (null != borrow.getUse_date()){

            String tempTime = simpleDateFormat.format(borrow.getUse_date());
            borrow.setReal_use_date(tempTime);
        }else {
            borrow.setReal_use_date("暂无信息");
        }
        if (null != borrow.getStatus()){
            if (borrow.getStatus().equals("1")){
                borrow.setStatusName("未送审");
            }else if (borrow.getStatus().equals("2")){
                borrow.setStatusName("未审批");
            }else if (borrow.getStatus().equals("3")){
                borrow.setStatusName("审批中");
            }else if (borrow.getStatus().equals("4")){
                borrow.setStatusName("审批通过");
            }else if (borrow.getStatus().equals("5")){
                borrow.setStatusName("审批驳回");
            }
        }


        List<BorrowAssets> list = getOneById(id);
        borrow.setDetail(list);
        return borrow;
    }


    /**
     * 根据Id查询借用单详情
     * @param id
     * @return
     */
    public List<BorrowAssets> getOneById(Integer id){
        List<BorrowAssets> list = borrowAssetsMapper.getBorrowAssets(id);
        Map<Integer, SysUser> userMap = commonDataTools.getUserMap();
        Map<Integer, SysDict> brandType = commonDataTools.getSysDictMap("brand_type");
        for (BorrowAssets temp : list){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (null != temp.getReal_date() ){
                temp.setRealUseDate(simpleDateFormat.format(temp.getReal_date()));
            }else {
                temp.setRealUseDate("暂无数据");
            }
            if (null != temp.getReturn_date() ){
                temp.setRealBackDate(simpleDateFormat.format(temp.getReturn_date()));
            }else {
                temp.setRealBackDate("暂无数据");
            }
            if (null != temp.getBorrow_status()){
                if (temp.getBorrow_status().equals("1")){
                    temp.setStatusName("待借用确认");
                }else if (temp.getBorrow_status().equals("2")){
                    temp.setStatusName("待归还");
                }else if (temp.getBorrow_status().equals("3")){
                    temp.setStatusName("已归还");
                }else if (temp.getBorrow_status().equals("4")){
                    temp.setStatusName("延期未归还");
                }
            }else {
                temp.setStatusName("暂无数据");
            }
            AssetsManage assetsManage = assetsManageMapper.selectById(temp.getAssets_id());
            temp.setSpecification(assetsManage.getSpecification());
            temp.setBrand_name(commonDataTools.getValue(brandType, assetsManage.getBrand_id(), "label"));
            temp.setReturn_deal_name(commonDataTools.getValue(userMap, temp.getReturn_deal_user(), "user_name"));
            if (null != temp.getReturn_deal_user()){
                SysUser sysUser = sysUserMapper.selectById(temp.getReturn_deal_user());
                if (null != sysUser){
                    temp.setReturn_deal_name(sysUser.getUser_name());
                }else {
                    temp.setReturn_deal_name("暂无数据");
                }
            }else {
                temp.setReturn_deal_name("暂无数据");
            }
        }
        return list;
    }


    /**
     * 借用单确认
     * @param id
     * @return
     */
    public ResultInfo confirmBorrowAssets(Integer id){
        BorrowAssets borrowAssets = borrowAssetsMapper.selectById(id);
        if (!G.TO_BE_COLLECTED.equals(borrowAssets.getBorrow_status())) {
            ResultInfo.error("状态必须是待借用确认");
        }
        borrowAssets.setBorrow_status(G.TO_BE_RETURNED);
        borrowAssets.setReal_date(new Date());
        borrowAssetsMapper.updateById(borrowAssets);

        //获取使用人和使用人所在的部门Id
        Integer borrow_id = borrowAssets.getBorrow_id();
        Borrow borrow = selectById(borrow_id);
        int use_id = borrow.getUse_id();
        int department_id = borrow.getDepartment_id();
        //获取资产Id
        int assets_id = borrowAssets.getAssets_id();


        AssetsManage assetsManage = assetsManageMapper.selectById(assets_id);
        //修改状态为借用
        assetsManage.setAsset_state(AssetsStatusEnums.BORROW.getType());
        //存放部门
        assetsManage.setDept_id(department_id);
        //借用人
        assetsManage.setUse_people(use_id);
        //存放位置
        SysDepartment sysDepartment = sysDepartmentMapper.selectById(department_id);
        //如果部门位置不为空 则需要添加存放位置信息
        if (sysDepartment != null) {
            Integer dept_position = sysDepartment.getDept_position();
            assetsManage.setPosition_id(dept_position);
        }
        assetsManageMapper.updateById(assetsManage);
        return ResultInfo.success();
    }


    /**
     * 借用归还
     * @param id
     * @return
     */
    public ResultInfo returnBorrow(Integer id){
        BorrowAssets borrowAssets = borrowAssetsMapper.selectById(id);
        if (!G.TO_BE_RETURNED.equals(borrowAssets.getBorrow_status())) {
            ResultInfo.error("状态必须是待归还");
        }
        borrowAssets.setBorrow_status(G.RETURNED);
        borrowAssets.setReturn_date(new Date());
        borrowAssets.setReturn_deal_user(sysUserService.getUser().getId());
        borrowAssetsMapper.updateById(borrowAssets);

        //更改资产管理的信息
        int assets_id = borrowAssets.getAssets_id();
        AssetsManage assetsManage = assetsManageMapper.selectById(assets_id);
        assetsManage.setAsset_state(AssetsStatusEnums.IDLE.getType());
        assetsManageMapper.updateById(assetsManage);
        return ResultInfo.success();
    }


    /**
     * 获取当前登陆用户逾期未归还的资产
     * @return
     */
    public List<BorrowAssets> beOverdue(){
        //判断当前登陆的权限
        SysUser sysUser = sysUserService.getUser();
        int userId = sysUser.getId();
        Integer postId = sysUserMapper.selectById(userId).getPost();
        if (null == postId) {
            return null;
        }
        SysPost sysPost = sysPostMapper.selectById(postId);
        if (null == sysPost) {
            return null;
        }
        Integer data_permission = sysPost.getData_permission();

        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del",G.ISDEL_NO);
        queryWrapper.lt("estimate_return_date",new Date());
        queryWrapper.eq("status",G.PASS_APPROVED);
//        LambdaQueryWrapper<Borrow> wrapper = queryWrapper.lambda();
        //仅查看个人通过审核数据
        if (G.DATAPERMISSION_PERSONAL == data_permission) {
            queryWrapper.eq("create_user",userId);
            //查看本部门数据
        } else if (G.DATAPERMISSION_DEPT == data_permission) {
            int department = sysUser.getDepartment();
            queryWrapper.eq("department_id",department);
        }
        List<Borrow> list = borrowMapper.selectList(queryWrapper);

        List<BorrowAssets> result = new ArrayList<>();
        //根据借用单ID查询出来状态为待归还 和 延期未归还的资产
        for (Borrow temp : list){
            Integer id = temp.getId();
            List<BorrowAssets> list1 = borrowAssetsMapper.beOverdue(id);
            for (int i = 0 ; i<list1.size();i++){
                //把状态为带归还的更新为 延期未归还
                BorrowAssets borrowAssets =  list1.get(i);
                if (G.TO_BE_RETURNED.equals(list1.get(i).getBorrow_status())){
                    borrowAssets.setBorrow_status(G.OVERDUE_RETURN);
                    borrowAssetsMapper.updateById(borrowAssets);
                }
                if (null != borrowAssets.getReal_date()){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    borrowAssets.setRealUseDate(simpleDateFormat.format(borrowAssets.getReal_date()));
                }
                result.add(borrowAssets);
            }
        }
        return result;
    }

    /**
     * 获取当前用户所有的借用单
     * @return
     */
    public List<Borrow> getAllBorrow(Integer id){
        Borrow borrow = new Borrow();
        borrow.setCreate_user(id);
        List<Borrow> list = borrowMapper.getPageInfo(borrow);
        for (Borrow temp : list){
            if (null != temp .getUse_id()){
                SysUser sysUser = sysUserMapper.selectById(temp.getUse_id());
                if (null != sysUser){
                    temp.setUse_name(sysUser.getUser_name());
                }
            }
            if (null != temp.getUse_date()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String tempTime = simpleDateFormat.format(temp.getUse_date());
                temp.setReal_use_date(tempTime);
            }else {
                temp.setReal_use_date("暂无信息");
            }
            if (null != temp.getEstimate_return_date()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String tempTime = simpleDateFormat.format(temp.getEstimate_return_date());
                temp.setReal_return_date(tempTime);
            }else {
                temp.setReal_return_date("暂无信息");
            }
            if (null != temp.getHandle_id()){
                SysUser sysUser = sysUserMapper.selectById(temp.getHandle_id());
                if (null != sysUser){
                    temp.setHandle_name(sysUser.getUser_name());
                }else {
                    temp.setHandle_name("暂无数据");
                }
            }else {
                temp.setHandle_name("暂无数据");
            }
            if (null != temp.getStatus()){
                switch (temp.getStatus()) {
                    case "1":
                        temp.setStatusName("未送审");
                        break;
                    case "2":
                        temp.setStatusName("未审批");
                        break;
                    case "3":
                        temp.setStatusName("审批中");
                        break;
                    case "4":
                        temp.setStatusName("审批通过");
                        break;
                    case "5":
                        temp.setStatusName("审批驳回");
                        break;
                }
            }
        }
        return list;
    }


    public void  addAPPBorrow(String ids, Integer handle_id,String remark,Integer userId,String estimate_return_date){
        SysUser sysUser = sysUserMapper.selectById(userId);
        String code = commonDataTools.getNo(DataType.BORROW.getType(), null);
        Borrow borrow = new Borrow();
        borrow.setUse_id(sysUser.getId());
        borrow.setDepartment_id(sysUser.getDepartment());
        borrow.setBorrow_code(code);
        borrow.setUse_date(new Date());
        borrow.setCreate_time(new Date());
        borrow.setCreate_user(sysUser.getId());
        borrow.setIs_del(G.ISDEL_NO);
        borrow.setStatus(G.NOT_SUBMITTED);
        borrow.setHandle_id(handle_id);
        borrow.setRemark(remark);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date temp = simpleDateFormat.parse(estimate_return_date);
            borrow.setEstimate_return_date(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        borrowMapper.insert(borrow);
        String[] idList = ids.split("-");
        for (String temp : idList){
            Integer assetsId = Integer.parseInt(temp);
            BorrowAssets borrowAssets = new BorrowAssets();
            borrowAssets.setAssets_id(assetsId);
            borrowAssets.setBorrow_id(borrow.getId());

            borrowAssets.setIs_del(G.ISDEL_NO);
            borrowAssets.setCreate_time(new Date());
            borrowAssets.setCreate_user(sysUser.getId());

            borrowAssetsMapper.insert(borrowAssets);
        }
    }



}
