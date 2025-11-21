package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ApplySingleDao;
import com.jsdc.rfid.enums.AssetsStatusEnums;
import com.jsdc.rfid.mapper.ApplySingleMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.statisticalreport.StatisticalReportService;
import com.jsdc.rfid.vo.ApplySingleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.StatisticsRepairVo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.jsdc.core.base.Base.empty;
import static com.jsdc.core.base.Base.notEmpty;

/**
 * @author zln
 * @descript 报修申请功能
 * @date 2022-04-24
 */
@Service
@Transactional
public class ApplySingleService extends BaseService<ApplySingleDao, ApplySingle> {


    @Autowired
    private ApplySingleMapper mapper;
    @Autowired
    private PictureImgService pictureImgService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AssetsManageService manageService;
    @Autowired
    private SysDepartmentService sysDepartmentService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private InventoryDetailService inventoryDetailService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private StatisticalReportService statisticalReportService;
    @Autowired
    private ProcessMemberService processMemberService;
    /**
     * 分页查询
     * 20230129做变更
     * 编写人：zln
     *
     * @param pageIndex 起始页
     * @param pageSize  页大小
     * @param bean      对象参数
     * @return 分页列表数据
     * @author zln
     */
    public PageInfo<ApplySingleVo> selectPageList(Integer pageIndex, Integer pageSize, ApplySingleVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ApplySingleVo> list = mapper.pageList(bean);
        list.forEach(a -> {
            if (notEmpty(a.getDept_id())) {
                //状态
                SysDict sysDict = sysDictService.selectOne(new QueryWrapper<SysDict>().eq("is_del", "0").eq("type", "bx_state").eq("value", a.getState()));
                if (notEmpty(sysDict)) {
                    a.setStateName(sysDict.getLabel());
                }
                List<PictureImg> lists = pictureImgService.getLists(a.getId(), bean.getPicType());
                if (!CollectionUtils.isEmpty(lists)) {
                    PictureImg picture = lists.get(0);
                    if (notEmpty(picture)) {
                        a.setImgUrl(picture.getPicurl());
                    }
                }
            }
        });
        return new PageInfo<>(list);
    }

    /**
     * 已派单分页
     *
     * @param pageIndex
     * @param pageSize
     * @param bean
     * @return
     */
    public PageInfo selectYPDPage(Integer pageIndex, Integer pageSize, ApplySingleVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ApplySingleVo> list = mapper.selectYPDPage(bean);
        return new PageInfo<>(list);
    }


    /**
     * 手机端-我的维修
     *
     * @param pageIndex
     * @param pageSize
     * @param bean
     * @return
     */
    public PageInfo selectByHistoryList(Integer pageIndex, Integer pageSize, ApplySingleVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ApplySingleVo> list = mapper.selectByHistoryList(bean);
        list.forEach(e -> {
            List<PictureImg> lists = pictureImgService.getLists(e.getId(), 1);
            if (!CollectionUtils.isEmpty(lists)) {
                PictureImg picture = lists.get(0);
                if (notEmpty(picture)) {
                    e.setImgUrl(picture.getPicurl());
                }
            }
        });
        return new PageInfo<>(list);
    }


    /**
     * 自动派单功能
     *
     * @param bean
     */
    public Integer onZdPd(ApplySingle bean) {
        List<ApplySingleVo> list = mapper.selectApplyCount(bean);
        list.sort(Comparator.comparing(ApplySingleVo::getCreation_time));
        if (!CollectionUtils.isEmpty(list)) {
            System.out.println(list.get(0).getRepair_id());
            return list.get(0).getRepair_id();
        }
        return 0;
    }


    /**
     * 新增或修改
     *
     * @param bean  参数
     * @param files 图片
     * @param ids
     * @return
     */
    public Integer saveOrUpdate(ApplySingle bean, String files, String ids) {
        if (notEmpty(bean) && notEmpty(bean.getId())) {
            //删除图片
            if (notEmpty(ids)) {
                List<String> lists = Arrays.asList(ids.split(","));
                if (!CollectionUtils.isEmpty(lists)) {
                    for (String list : lists) {
                        pictureImgService.deleteById(Integer.parseInt(list));
                    }
                }
            }
            //保存图片
            pictureImgService.savePicture(bean.getId(), files, 1);
            //拒签后重新派单
            if (notEmpty(bean.getRepair_id())) {
                ApplySingle single = selectById(bean.getId());
                //提交维修人员和之前的维修人员不同
                if (!bean.getRepair_id().equals(single.getRepair_id())) {
                    bean.setState(2);
                    //修改
                    if (updateById(bean) > 0) {
                        return 0;
                    }
                }
            }
            return 1;//失败
        } else {
            bean.setState(2);
            bean.setIs_del(0);
            //自动派单
            if (empty(bean.getPd_type()) || bean.getPd_type() == 1) {
                Integer userId = onZdPd(bean);
                if (userId != 0) {
                    bean.setRepair_id(userId);
                } else {
                    return -1;
                }
            }
            SysDepartment dept = sysDepartmentService.selectById(bean.getDept_id());
            bean.setDept_name(null == dept ? "" : dept.getDept_name());
            SysUser sysUser = sysUserService.selectById(bean.getRepair_id());
            bean.setRepair_name(sysUser.getUser_name());
            AssetsManage assetsManage = manageService.selectById(bean.getAssetmanage_id());
            bean.setGood_number(assetsManage.getAsset_code());
            bean.setCreation_time(new Date());//创建时间
            //设置为资产故障
            assetManageUpdate(bean.getAssetmanage_id(), 1);
            int count = insert(bean);
            System.out.println(bean.getId());
            if (count > 0) {
                //图片上传保存
                if (notEmpty(files)) {
                    pictureImgService.savePicture(bean.getId(), files, 1);
                }
                return 0;//成功
            } else {
                return 1;//失败
            }
        }
    }


    //详情
    public ApplySingleVo findApplySingleDetils(Integer applyId) {
        return mapper.selectByDetails(applyId);
    }

    public Integer saveSingle(ApplySingle bean, String files, Integer id) {
        bean.setState(2);
        bean.setIs_del(0);
        //自动派单
        if (notEmpty(bean.getPd_type())) {
            Integer userId = onZdPd(bean);
            if (userId != 0) {
                bean.setRepair_id(userId);
            } else {
                return -1;
            }
        }
        SysDepartment dept = sysDepartmentService.selectById(bean.getDept_id());
        bean.setDept_name(dept.getDept_name());
        SysUser sysUser = sysUserService.selectById(bean.getRepair_id());
        bean.setRepair_name(sysUser.getUser_name());
        AssetsManage assetsManage = manageService.selectById(bean.getAssetmanage_id());
        bean.setGood_number(assetsManage.getAsset_code());
        bean.setCreation_time(new Date());//创建时间
        int count = insert(bean);
        System.out.println(bean.getId());
        if (count > 0) {
            //图片上传保存
            pictureImgService.savePicture(bean.getId(), files, 1);
            // 修改盘点详情
            InventoryDetail inventoryDetail = inventoryDetailService.selectById(id);
            inventoryDetail.setUpdate_user(sysUser.getId());
            inventoryDetail.setUpdate_time(new Date());
            inventoryDetail.setIs_deal("1");

            inventoryDetail.setApply_single_id(bean.getId());
            inventoryDetail.updateById();
            return 0;//成功
        } else {
            return 1;//失败
        }
    }

    /**
     * //更改资产状态为故障
     *
     * @param assetManage_id
     */
    public void assetManageUpdate(String assetManage_id, Integer type) {
        AssetsManage manage = assetsManageService.selectById(assetManage_id);
        if (1 == type) {
            manage.setAsset_state_bx(manage.getAsset_state());//记住之前的状态
            manage.setAsset_state(AssetsStatusEnums.FAULT.getType());
        } else {
//            manage.setAsset_state(manage.getAsset_state_bx());//之前是记住原来状态，维修完成后改变成原来状态
            manage.setAsset_state_bx(null);
            manage.setAsset_state(AssetsStatusEnums.USE.getType());//维修成功状态改为使用中
        }
        assetsManageService.updateById(manage);
    }

    /**
     * 按部门统计报修数据
     *
     * @return
     */
    public List<StatisticsRepairVo> statisticsByDept() {
        return mapper.statisticsByDept();
    }

    /**
     * 统计资产报修列表
     *
     * @param pageIndex
     * @param pageSize
     * @param deptId
     * @return
     */
    public PageInfo getPage(Integer pageIndex, Integer pageSize, Integer deptId) {

        List<Integer> deptIds=statisticalReportService.getDeptSon(deptId);

        LambdaQueryWrapper<ApplySingle> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ApplySingle::getDept_id, deptIds);
        wrapper.eq(ApplySingle::getIs_del, G.ISDEL_NO);
        wrapper.ne(ApplySingle::getState, 1);
        wrapper.ne(ApplySingle::getState, 9);
        PageHelper.startPage(pageIndex, pageSize);
        List<ApplySingle> list = selectList(wrapper);
        return new PageInfo<>(list);
    }


    /**
     * 报修申请
     * 20230128做变更
     * 编写人：zln
     *
     * @param bean
     * @param files
     * @param ids
     * @return
     */
    public Integer save(ApplySingle bean, String files, String ids) {
        if (notEmpty(bean) && notEmpty(bean.getId())) {
            //删除图片
            if (notEmpty(ids)) {
                List<String> lists = Arrays.asList(ids.split(","));
                if (!CollectionUtils.isEmpty(lists)) {
                    for (String list : lists) {
                        pictureImgService.deleteById(Integer.parseInt(list));
                    }
                }
            }
            //保存图片
            pictureImgService.savePicture(bean.getId(), files, 1);
            return 1;//失败
        } else {
            bean.setState(2);
            bean.setIs_del(0);
            //自动派单
            if (empty(bean.getPd_type()) || bean.getPd_type() == 1) {
                Integer userId = onZdPd(bean);
                if (userId != 0) {
                    bean.setRepair_id(userId);
                } else {
                    return -1;
                }
            }
            SysDepartment dept = sysDepartmentService.selectById(bean.getDept_id());
            bean.setDept_name(null == dept ? "" : dept.getDept_name());
            bean.setRepair_name(sysUserService.selectById(bean.getRepair_id()).getUser_name());
            bean.setSqr_name(sysUserService.selectById(bean.getSqr_id()).getUser_name());
            AssetsManage assetsManage = manageService.selectById(bean.getAssetmanage_id());
            bean.setGood_number(assetsManage.getAsset_code());
            bean.setCreation_time(new Date());//创建时间
            //设置为资产故障
            assetManageUpdate(bean.getAssetmanage_id(), 1);
            int count = insert(bean);
            if (count > 0) {
                //图片上传保存
                if (notEmpty(files)) {
                    pictureImgService.savePicture(bean.getId(), files, 1);
                }
                return 0;//成功
            } else {
                return 1;//失败
            }
        }
    }
    public PageInfo<ApplySingleVo> getApprovedListByPage(ApplySingleVo bean, Integer page, Integer limit, SysUser current) {
        PageHelper.startPage(page, limit);
        List<ApplySingleVo> list = mapper.getApprovedListByPage(bean);
        for (ApplySingleVo x : list) {
            // 得到当前流程节点
            ProcessConfig process = processMemberService.getInfoByBusId(x.getId(), G.PROCESS_WBWX);
            if (null != process && !CollectionUtils.isEmpty(process.getCurrentInfos())){
                for (ProcessConfigInfo info : process.getCurrentInfos()){
                    if (info.getNode_type() == 0 && current.getPost().equals(info.getNode_handler())){
                        x.setCurrentTaskName(info.getNode_name());
                    } else if (info.getNode_type() == 1 && current.getId().equals(info.getNode_handler())){
                        x.setCurrentTaskName(info.getNode_name());
                    } else if (info.getNode_type() == 2 && process.getProcessMember().getApply_dept_leader_id().equals(current.getId())) {
                        x.setCurrentTaskName(info.getNode_name());
                    }
                }
            }
        }
        return new PageInfo<>(list);
    }

    /**
     * 审批方法
     * <p>
     * bean.id
     * bean.approval_state ( 0.待审批 1.审批拒绝  2.审批通过
     *
     * @return
     */
    public Integer updateState(ApplySingleVo bean) {
        ApplySingle applySingle = selectById(bean);
        applySingle.setApproval_state(bean.getApproval_state());
        return updateById(applySingle);
    }


    public PageInfo finishAdopt(Integer page, Integer limit, ApplySingleVo bean) {
        PageHelper.startPage(page, limit);
        List<ApplySingleVo> list = mapper.getApprovedListByPage(bean);
        return new PageInfo<>(list);
    }


    /**
     * 获取维修人员待维修数量
     * @param pageIndex
     * @param pageSize
     * @param bean
     * @return
     */
    public PageInfo getRepairList(Integer pageIndex, Integer pageSize, ApplySingleVo bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ApplySingleVo> list = mapper.getRepairList(bean);

        return new PageInfo<>(list);
    }

    /**
     * 报修本月总数
     * @param pageIndex
     * @param pageSize
     * @param applySingleVoMonth
     */
    public PageInfo getRepairMonthList(int pageIndex, int pageSize, ApplySingleVo applySingleVoMonth) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ApplySingleVo> list = mapper.getRepairMonthList(applySingleVoMonth);

        return new PageInfo<>(list);
    }

}
