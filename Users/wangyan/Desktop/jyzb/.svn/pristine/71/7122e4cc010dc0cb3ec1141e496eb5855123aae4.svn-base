package com.jsdc.rfid.controller;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.PrintCommon;
import com.jsdc.rfid.SpringUtils;
import com.jsdc.rfid.mapper.RFIDConfigMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.utils.QrCodeUtil;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 资产管理 控制器
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Controller
@RequestMapping("/assetsManage")
public class AssetsManageController extends BaseController {

    @Autowired
    private AssetsManageService assetsManageService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OperationRecordService operationRecordService;

    @Autowired
    private SysPlaceService sysPlaceService;

    @Autowired
    private SysPositionService sysPositionService;

    /**
     *  页面跳转
     * @return
     */
    @GetMapping(value = "/property/{cate}/{name}")
    public String warehousing(@PathVariable String cate, @PathVariable String name, Model model) {
        model.addAttribute("user", sysUserService.getUser());
        if (StringUtils.equals("register", cate) && (StringUtils.equals("add", name) || StringUtils.equals("copy", name))) {
            List<SysPlace> sysPlaces = sysPlaceService.getList(new SysPlace());
            // 得到地点信息
            model.addAttribute("places", sysPlaces);
            // 得到位置
            model.addAttribute("positions", CollectionUtils.isEmpty(sysPlaces)?
                    Collections.emptyList():
                    sysPositionService.getList(SysPosition.builder().place_id(sysPlaces.get(0).getId()).build()));
        } else if (StringUtils.equals("maintain", cate) && StringUtils.equals("edit", name)) {
            List<SysPlace> sysPlaces = sysPlaceService.getList(new SysPlace());
            // 得到地点信息
            model.addAttribute("places", sysPlaces);
            // 得到位置
            model.addAttribute("positions", CollectionUtils.isEmpty(sysPlaces)?
                    Collections.emptyList():
                    sysPositionService.getList(SysPosition.builder().place_id(sysPlaces.get(0).getId()).build()));
        }
        return "/property/" + cate + "/" + name;
    }

    /**
     * 我的资产 查询列表 分页
     */
    @RequestMapping(value = "/my/list", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo myList(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "limit", defaultValue = "10") Integer pageSize,
                                         AssetsManage beanParam) {
        beanParam.setUse_people(sysUserService.getUser().getId());
        PageInfo<AssetsManage> pageList = assetsManageService.toList(pageNum, pageSize, beanParam);
        return ResultInfo.success(pageList);
    }

    /**
     * 资产管理 列表查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             AssetsManage beanParam) {
        PageInfo<AssetsManage> page = assetsManageService.toList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }

    /**
     * 资产管理 资产维护列表查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "toWHList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toWHList(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                               @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                               AssetsManage beanParam) {

        PageInfo<AssetsManage> page = assetsManageService.toWHList(pageIndex, pageSize, beanParam, sysUserService.getUser());

        return ResultInfo.success(page);
    }

    /**
     * 资产轨迹
     * 资产管理 列表查询
     * thr
     */
    @RequestMapping(value = "getLists.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getLists(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             AssetsManage beanParam) {
        PageInfo<AssetsManage> page = assetsManageService.toLists(pageIndex, pageSize, beanParam);
        return ResultInfo.success(page);
    }

     /**
     *  ID查询
     */
    @RequestMapping(value = "getById.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(Integer id){
        return ResultInfo.success(assetsManageService.getById(id));
    }


    /**
     *  添加
     */
    @RequestMapping(value = "toAdd.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addAssetsManage(AssetsManage assetsManage){
        assetsManage.setRegister_time(new Date());
        assetsManage.setPosition_change_time(new Date());
        return assetsManageService.addAssetsManage(assetsManage);
    }

    /**
     *  copy
     */
    @RequestMapping(value = "copyAssetsManage.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo copyAssetsManage(AssetsManage assetsManage){
        assetsManage.setRegister_time(new Date());
        assetsManage.setPosition_change_time(new Date());
        return assetsManageService.copyAssetsManage(assetsManage);
    }

    /**
     * 得到资产编号
     */
    @RequestMapping(value = "getAssetsCode.do",method = RequestMethod.POST)
    @ResponseBody
    public String getAssetsCode(Integer assets_type){
        return assetsManageService.getAssetsCode(assets_type);
    }

    /**
     * 批量得到资产编号
     */
    @RequestMapping(value = "getAssetsCodes.do",method = RequestMethod.POST)
    @ResponseBody
    public List<String> getAssetsCodes(Integer assets_type, Integer num){
        return assetsManageService.getAssetsCodes(assets_type, num);
    }

    /**
     * 根据部门id查询位置id
     */
    @RequestMapping(value = "getPositionIdByDeptId.do",method = RequestMethod.POST)
    @ResponseBody
    public SysDepartment getPositionIdByDeptId(Integer dept_id){
        return assetsManageService.getPositionIdByDeptId(dept_id);
    }

    /**
     *  编辑
     */
    @RequestMapping(value = "toEdit.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo editAssetsManage(AssetsManage assetsManage){

        return assetsManageService.editAssetsManage(assetsManage);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "toDelete.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo deleteAssetsManage(Integer id){

        return assetsManageService.deleteAssetsManage(AssetsManage.builder().id(id).build());
    }

    /**
     * 批量删除
     */
    @RequestMapping(value = "toBatchDelete.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo batchDeleteAssetsManage(@RequestBody List<Integer> ids){

        return assetsManageService.batchDeleteAssetsManage(ids);
    }


    /**
     *  查看-生成二维码
     */
    @RequestMapping(value = "genratorQrCode.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo genratorQrCode(AssetsManage assetsManage){
        String qrCode = assetsManageService.genratorQrCode(assetsManage);
        return ResultInfo.success(qrCode);
    }


    /**
     *  查看-操作记录
     */
    @RequestMapping(value = "getLogList.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo  selectByCarryManageId(String field_fk, @RequestParam(required = false) String type) {
        return ResultInfo.success(operationRecordService.getOperationRecord(field_fk, type));
    }

    /**
     * 根据资产编号查询资产信息
     */
    @RequestMapping(value = "/{assets_code}/getAssetsByCode.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo getAssetsByCode(@PathVariable String assets_code){
        return ResultInfo.success(assetsManageService.getAssetsManageByCode(assets_code));
    }

    /**
     * 打印RFID标签
     */
    @RequestMapping(value = "/toPrintRFID.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo printRfid(@RequestBody List<String> codes){
        try {
            // 查到rfid配置类
            List<RFIDConfig> configs = SpringUtils.getBean(RFIDConfigMapper.class).selectList(Wrappers.<RFIDConfig>lambdaQuery().eq(RFIDConfig::getIs_del, "0"));
            String ipAddress = "";
            if(CollectionUtils.isEmpty(configs)){
                ipAddress = "192.168.0.132";
            }else{
                ipAddress = configs.get(0).getPrintconfigadd();
            }

            // ----------------------------------
//            for( String code : codes){
//                AssetsManage assetsManage = assetsManageService.getAssetsManageByCode(code);
//                assetsManage.setIs_print(1);
//                // 判断code是否符合的长度是否为16位
//                String ass_code = code;
//                if (ass_code.length() > 16 && ass_code.length() == 17){
//                    String temp1 = ass_code.substring(0,5);
//                    String temp2 = ass_code.substring(6);
//                    ass_code = temp1 + temp2;
//                }else if (ass_code.length() > 16 && ass_code.length() == 18){
//                    String temp1 = ass_code.substring(0,5);
//                    String temp2 = ass_code.substring(7);
//                    ass_code = temp1 + temp2;
//                }
//                assetsManage.setAsset_code(ass_code);
//                assetsManage.setRfid(AssetsManageService.toHexString(ass_code));
//                // 二维码
//                assetsManage.setQr_code(genratorQrCode1(ass_code));
//                assetsManageService.updateById(assetsManage);
//                PrintCommon.getInstance().printRFID(assetsManage);
//            }
            // ----------------------------------


            InetAddress address = InetAddress.getByName(ipAddress);
            boolean reachable = address.isReachable(3000);
            if (reachable) {
                System.out.println("IP地址可以通信");
                for( String code : codes){
                    AssetsManage assetsManage = assetsManageService.getAssetsManageByCode(code);
                    assetsManage.setIs_print(1);
                    // 判断code是否符合的长度是否为16位
                    String ass_code = code;
                    if (ass_code.length() == 17){
                        String temp1 = ass_code.substring(0,5);
                        String temp2 = ass_code.substring(6);
                        ass_code = temp1 + temp2;
                    }else if (ass_code.length() == 18){
                        String temp1 = ass_code.substring(0,5);
                        String temp2 = ass_code.substring(7);
                        ass_code = temp1 + temp2;
                    }
                    assetsManage.setAsset_code(ass_code);
                    assetsManage.setRfid(AssetsManageService.toHexString(ass_code));
                    // 二维码
                    assetsManage.setQr_code(genratorQrCode1(ass_code));
                    assetsManageService.updateById(assetsManage);
                    PrintCommon.getInstance().printRFID(assetsManage);
                }
            } else {
                throw new RuntimeException("IP地址无法通信");
            }

        } catch (Exception e) {
            String msg = e.getMessage();
            // 如果包含ip
            if (msg.contains("IP")){
                msg = "打印机IP地址无法通信";
            }
            return ResultInfo.error(msg);
        }
        return ResultInfo.success();
    }

    public String genratorQrCode1(String code) {
        JSONObject qrCodeJson = new JSONObject();
        //生成二维码AssetsManage 中字段太多 传参会报data太大 只取个别字段
        qrCodeJson.put("asset_code", code);//     资产编号
        String qrCode = "请刷新重新生成二维码";
        try {
//            qrCode = QrCodeUtil.encode_QR_CODE(qrCodeJson.toJSONString());
            qrCode = QrCodeUtil.encode_QR_CODE(code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qrCode;
    }

    /**
     * 根据部门id 得到位置信息
     */
    @RequestMapping(value = "/{dept_id}/getPosition.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo getPosition(@PathVariable Integer dept_id){
        return ResultInfo.success(assetsManageService.getPositionByDeptId(dept_id));
    }

    /**
     * 根据位置id 得到地点信息
     */
    @RequestMapping(value = "/{position_id}/getLocation.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo getLocation(@PathVariable Integer position_id){
        return ResultInfo.success(assetsManageService.getLocationByPositionId(position_id));
    }

    /**
     * 变更资产编号
     */
    @RequestMapping(value = "/correctionNoData",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo correctionNoData(){
        return assetsManageService.correctionNoData();
    }

    /**
     * 资产导出模板
     */
    @RequestMapping(value = "/toExportTemplate.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo toExportTemplate(HttpServletResponse response){
        return ResultInfo.success(assetsManageService.toExportTemplate(response));
    }

    /**
     * 资产登记导入
     */
    @RequestMapping(value = "/toAssetImport.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAssetImport(Integer fileId){
        return assetsManageService.toAssetImport(fileId);
    }
}
