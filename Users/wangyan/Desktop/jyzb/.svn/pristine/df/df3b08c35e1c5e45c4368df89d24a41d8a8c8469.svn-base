package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.FeedbackDao;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.vo.JsonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.jsdc.core.base.Base.notEmpty;


@Service
@Transactional
public class FeedbackService extends BaseService<FeedbackDao, Feedback> {

    @Autowired
    private AssetsTypeService typeService;
    @Autowired
    private AssetsManageService manageService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PictureImgService pictureImgService;
    @Autowired
    private ApplySingleService singleService;
    @Autowired
    private ProcessMemberService processMemberService;

    //详情
    public Feedback findFeedbackDetils(Feedback feedback) {
        return selectById(feedback.getId());
    }

    /**
     * 反馈功能
     *
     * @param feedback
     * @param files
     * @return
     */
    public String onSave(Feedback feedback, Integer state, String files) {
        JSONObject jsonObject = new JSONObject();
        ApplySingle applySingle = singleService.selectById(feedback.getApplysingle_id());
        applySingle.setState(state);
        feedback.setApplysingle_id(feedback.getApplysingle_id());
        if (insert(feedback) > 0) {
            singleService.updateById(applySingle);
            //设置为资产故障(根据维修成功和维修失败,判断是否变更维修状态)
            if (1 == feedback.getRepair_status()) {//维修成功,更改状态
                singleService.assetManageUpdate(applySingle.getAssetmanage_id(), 2);
            }
            //图片上传保存
            pictureImgService.savePicture(feedback.getId(), files, 2);
        }
        jsonObject.put("success", true);
        jsonObject.put("message", "操作成功");
        return jsonObject.toJSONString();
    }


    //新增或修改
    public String saveOrUpdate(Feedback feedback, Integer state, Integer applyId, String files) {
        JSONObject jsonObject = new JSONObject();
        if (notEmpty(feedback) && notEmpty(feedback.getId())) {
            int count = updateById(feedback);
            if (count > 0) {
                pictureImgService.savePicture(feedback.getId(), files, 2);
                jsonObject.put("success", true);
                jsonObject.put("message", "修改成功");
            } else {
                jsonObject.put("success", true);
                jsonObject.put("message", "修改失败");
            }
        } else {
            feedback.setApplysingle_id(applyId.toString());
            insert(feedback);
            ApplySingle applySingle = new ApplySingle();
            applySingle.setId(applyId);
            applySingle.setState(state);
            //图片上传保存
            pictureImgService.savePicture(feedback.getId(), files, 2);
            int count = singleService.updateById(applySingle);
            if (count > 0) {
                jsonObject.put("success", true);
                jsonObject.put("message", "操作成功");
            } else {
                jsonObject.put("success", true);
                jsonObject.put("message", "操作失败");
            }
        }
        return jsonObject.toJSONString();
    }


    //所取参数
    public JSONObject getJsonList(Integer type, Integer dept_id, Integer user_id, Integer asset_type_id) {
        JSONObject jsonObject = new JSONObject();
        if (notEmpty(type)) {
            if (type == 1) {
                //紧急情况
                List<JsonVo> degreeList = new ArrayList<>();
                degreeList.add(new JsonVo("1", "特急"));
                degreeList.add(new JsonVo("2", "较急"));
                degreeList.add(new JsonVo("3", "一般"));
                //资产类型AssetsType
                //查询所有的资产类别
                List<AssetsType> stdmodeList = typeService.selectList(new QueryWrapper<AssetsType>().eq("is_del", "0").eq("is_enable", "1"));
                List<JsonVo> list = new ArrayList<>();
                for (AssetsType st : stdmodeList) {
                    JsonVo vo = new JsonVo();
                    vo.setId(st.getId().toString());
                    vo.setName(st.getAssets_type_name());
                    list.add(vo);
                }
                if (!CollectionUtils.isEmpty(list)) {
                    jsonObject.put("list", list);
                }
                jsonObject.put("degreeList", degreeList);
            } else if (type == 2) {
                //查询资产列表数据，传参用户id+类型id
                AssetsManage stvo = new AssetsManage();
                stvo.setDept_id(dept_id);
                stvo.setAsset_type_id(asset_type_id);
                List<AssetsManage> stdmodes = manageService.selectList(new QueryWrapper<AssetsManage>()
                        .eq("asset_type_id", asset_type_id).eq("use_people", user_id).or().isNull("use_people").eq("admin_user", user_id).eq("is_del", "0"));
                List<JsonVo> stList = new ArrayList<>();
                for (AssetsManage st : stdmodes) {
                    JsonVo vo = new JsonVo();
                    if (notEmpty(st.getUse_people())) {
                        SysUser sysUser = sysUserService.selectById(st.getUse_people());
                        vo.setId(st.getId().toString() + "," + sysUser.getId());
                        vo.setName(st.getAsset_name() + "," + sysUser.getUser_name());
                        stList.add(vo);
                    } else {
                        vo.setId(st.getId().toString());
                        vo.setName(st.getAsset_name());
                        vo.setValue(st.getAsset_code());
                        stList.add(vo);
                    }
                }
                if (!CollectionUtils.isEmpty(stList)) {
                    jsonObject.put("stList", stList);
                } else {
                    jsonObject.put("stSize", "0");
                }
            }
        }
        return jsonObject;
    }

    /**
     * 提交外部申请
     * @return
     */
    public String externalApplication( ApplySingle bean) {
        JSONObject jsonObject = new JSONObject();
        bean.setIsexternal(1);
        bean.setApproval_state("0");
        bean.setExtrnaltime(new Date());
        singleService.updateById(bean);
        SysUser user = sysUserService.getUser();
        // 启动流程
        processMemberService.startProcess(G.PROCESS_WBWX, bean.getId(), user.getId());
        jsonObject.put("success", true);
        jsonObject.put("message", "操作成功");
        return jsonObject.toJSONString();
    }
}
