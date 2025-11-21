package com.jsdc.rfid.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.AssetsManageDao;
import com.jsdc.rfid.enums.AssetsStatusEnums;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import com.jsdc.rfid.utils.QrCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.DataChartVo;
import vo.ResultInfo;
import vo.StatisticsAssetStateVo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 资产管理 service 实现类
 *
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Service
@Transactional
public class AssetsManageService extends BaseService<AssetsManageDao, AssetsManage> {

    @Autowired
    private AssetsManageMapper assetsManageMapper;

    @Autowired
    private AssetsFileMemberMapper assetsFileMemberMapper;

    @Autowired
    private FileManageMapper fileManageMapper;

    @Autowired
    private AssetsTypeMapper assetsTypeMapper;

    @Autowired
    private CommonDataTools commonDataTools;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;

    @Autowired
    private OperationRecordService operationRecordService;

    @Autowired
    private SysPositionService sysPositionService;

    @Autowired
    private InventoryManagementMapper inventoryManagementMapper;

    @Autowired
    private SysPlaceService sysPlaceService;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private SysDictMapper sysDictMapper;

    @Autowired
    private BrandManageMapper brandManageMapper;

    @Autowired
    private SysPostMapper sysPostMapper;

    @Value("${file.upload-path}")
    private String uploadPath;

    /**
     * 统计报表
     * 按分类统计
     * thr
     */
    public List<DataChartVo> classificationData(DataChartVo vo) {
        return assetsManageMapper.classificationData(vo);
    }

    /**
     * 统计报表
     * 按部门统计统计
     * thr
     */
    public List<DataChartVo> departmentData(DataChartVo vo) {
        return assetsManageMapper.departmentData(vo);
    }

    /**
     * 统计报表
     * 按品牌统计统计
     * thr
     */
    public List<DataChartVo> brandData(DataChartVo vo) {
        return assetsManageMapper.brandData(vo);
    }

    /**
     * 统计报表
     * 按购置时间统计
     * thr
     */
    public List<DataChartVo> purchaseTimeData(DataChartVo vo) {
        return assetsManageMapper.purchaseTimeData(vo);
    }

    /**
     * 统计报表
     * "按外携状态统计:
     * 未授权外携统计
     * 授权外携统计"
     * thr
     */
    public List<DataChartVo> carryStatusData(DataChartVo vo) {
        return assetsManageMapper.carryStatusData(vo);
    }

    /**
     * 统计报表
     * 异常预警趋势
     * 最近6个月的报警数量
     * thr
     */
    public List<DataChartVo> alarmsNumsData(DataChartVo vo) {
        return assetsManageMapper.alarmsNumsData(vo);
    }

    /**
     * 统计报表
     * 正常外携 近6个月
     * thr
     */
    public List<DataChartVo> carryNumsData(DataChartVo vo) {
        return assetsManageMapper.carryNumsData(vo);
    }

    /**
     * 统计报表
     * 基础信息：资产总数（饼状图，库内、库外 可按品类统计，默认统计全部品类）
     * thr
     */
    public List<DataChartVo> assetsRegisterTypeData(DataChartVo vo) {
        return assetsManageMapper.assetsRegisterTypeData(vo);
    }

    /**
     * 统计报表
     * 已登记总数、闲置总数、使用总数、故障总数、异常总数、处置总数
     * thr
     */
    public List<DataChartVo> assetsTotalData(DataChartVo vo) {
        return assetsManageMapper.assetsTotalData(vo);
    }

    /**
     * 统计报表
     * 资产位置变动排名TOP10 （柱状图，按变动次数）
     * thr
     */
    public List<DataChartVo> positionChangeTop10Data(DataChartVo vo) {
        return assetsManageMapper.positionChangeTop10Data(vo);
    }

    /**
     * 统计报表
     * 资产报修TOP10（柱状图，按品类；点击按资产展示报修次数信息）
     * thr
     */
    public List<DataChartVo> assetRepairTop10Data(DataChartVo vo) {
        return assetsManageMapper.assetRepairTop10Data(vo);
    }

    /**
     * 统计报表
     * 资产报修TOP10（柱状图，按品类；点击按资产展示报修次数信息）
     * thr
     */
    public List<DataChartVo> assetRepairCountData(DataChartVo vo) {
        return assetsManageMapper.assetRepairCountData(vo);
    }

    public List<AssetsManage> afterList(List<AssetsManage> assetsManageVos){
        // 用户map
        Map<Integer, SysUser> userMap = commonDataTools.getUserMap();
        // 部门map
        Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();
        // 资产类型map
        Map<Integer, AssetsType> assetsTypeMap = commonDataTools.getAssetsTypeMap();
        // 品牌map
        Map<Integer, SysDict> brandMap = commonDataTools.getSysDictMap("brand_type");
        // 折旧方法map
        Map<Integer, SysDict> depreciationMap = commonDataTools.getSysDictMap("depreciation_type");
        // 供应商map
        Map<Integer, Supplier> supplierMap = commonDataTools.getSupplierMap();
        // 位置map
        Map<Integer, SysPosition> positionMap = commonDataTools.getPositionMap();
        for (AssetsManage bean : assetsManageVos) {
            bean.setUse_people_name(commonDataTools.getValue(userMap, bean.getUse_people(), "user_name"));
            bean.setAdmin_user_name(commonDataTools.getValue(userMap, bean.getAdmin_user(), "user_name"));
            bean.setCreate_user_name(commonDataTools.getValue(userMap, bean.getCreate_user(), "user_name"));
            bean.setRegister_user_name(commonDataTools.getValue(userMap, bean.getRegister_user(), "user_name"));
            bean.setDept_name(commonDataTools.getValue(deptMap, bean.getDept_id(), "dept_name"));
            bean.setAsset_type_name(commonDataTools.getValue(assetsTypeMap, bean.getAsset_type_id(), "assets_type_name"));
            // 品牌
            BrandManage brand = brandManageMapper.selectById(bean.getBrand_id());
            bean.setBrand_name(null != brand ? brand.getBrand_name() : "");
            String depreciation_name = bean.getDepreciation_method();
            if (null != bean.getDepreciation_method() && StringUtils.isNotBlank(bean.getDepreciation_method())) {
                bean.setDepreciation_method(!isNumeric(bean.getDepreciation_method()) ? "" : commonDataTools.getValue(depreciationMap, Integer.valueOf(bean.getDepreciation_method()), "label"));
            }
            bean.setSupplier_name(commonDataTools.getValue(supplierMap, bean.getSupplier_id(), "supplier_name"));
            bean.setPlace_name(commonDataTools.getValue(positionMap, bean.getPosition_id(), "position_name"));
            bean.setAsset_epc(bean.getRfid());
            bean.setQr_code(genratorQrCode(bean.getAsset_code()));

        }
        return assetsManageVos;
    }

    /**
     * 列表查询 分页, 我的资产 和 台账 共用接口
     *
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    public PageInfo<AssetsManage> toList(Integer pageIndex, Integer pageSize, AssetsManage beanParam) {
        if (StringUtils.isNotBlank(beanParam.getSupplier_name())) {
            List<Supplier> list = supplierMapper.selectList(Wrappers.<Supplier>lambdaQuery()
                    .eq(Supplier::getSupplier_name, beanParam.getSupplier_name())
                    .eq(Supplier::getIs_del, "0")
            );
            if (!CollectionUtils.isEmpty(list)) {
                beanParam.setSupplier_id(list.get(0).getId());
            }
        }
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        // 支持按资产编号、品类、资产状态（闲置、使用、领用、借用、调拨、故障、处置）、品牌、存放部门、资产名称
        LambdaQueryWrapper<AssetsManage> wrappers = Wrappers.<AssetsManage>lambdaQuery();
        // 资产编号
        wrappers.like(StringUtils.isNotBlank(beanParam.getAsset_code()), AssetsManage::getAsset_code, beanParam.getAsset_code());
        // 品类
        wrappers.eq(null != beanParam.getAsset_type_id(), AssetsManage::getAsset_type_id, beanParam.getAsset_type_id());
        // 资产状态
        wrappers.eq(null != beanParam.getAsset_state(), AssetsManage::getAsset_state, beanParam.getAsset_state());
        // 品牌
        wrappers.eq(null != beanParam.getBrand_id(), AssetsManage::getBrand_id, beanParam.getBrand_id());
        // 存放部门
        wrappers.eq(null != beanParam.getDept_id(), AssetsManage::getDept_id, beanParam.getDept_id());
        // 资产名称
        wrappers.like(StringUtils.isNotBlank(beanParam.getAsset_name()), AssetsManage::getAsset_name, beanParam.getAsset_name());
        // 判断查询是我的资产还是全部资产
        wrappers.eq(null != beanParam.getUse_people(), AssetsManage::getUse_people, beanParam.getUse_people());
        // 复选框状态
        wrappers.in(!CollectionUtils.isEmpty(beanParam.getAssetStatusList()), AssetsManage::getAsset_state, beanParam.getAssetStatusList());
        // 供应商匹配
        wrappers.eq(null != beanParam.getSupplier_id(), AssetsManage::getSupplier_id, beanParam.getSupplier_id());
        // 存放位置
        wrappers.eq(null != beanParam.getPosition_id(), AssetsManage::getPosition_id, beanParam.getPosition_id());
        // 折旧统计
        wrappers.eq(null != beanParam.getIs_depreciation(), AssetsManage::getIs_depreciation, beanParam.getIs_depreciation());
        // 规格型号
        wrappers.like(StringUtils.isNotBlank(beanParam.getSpecification()), AssetsManage::getSpecification, beanParam.getSpecification());
        // 打印状态
        if (null != beanParam.getIs_print()) {
            if (1 == beanParam.getIs_print()){
                wrappers.eq(AssetsManage::getIs_print, beanParam.getIs_print());
            }else{
                // 等于null 或者 0
                wrappers.and(wp -> wp.isNull(AssetsManage::getIs_print).or().eq(AssetsManage::getIs_print, 0));
            }
        }
//        wrappers.eq(null != beanParam.getIs_print(), AssetsManage::getIs_print, beanParam.getIs_print());
        // 判断库内库外
        wrappers.eq(null != beanParam.getRegister_type(), AssetsManage::getRegister_type, beanParam.getRegister_type());
        // 当前位置
        wrappers.like(StringUtils.isNotBlank(beanParam.getRemarks()), AssetsManage::getRemarks, beanParam.getRemarks());
        if (Base.notEmpty(beanParam.getBx_type())) {//报修管理传值，根据使用人和所属管理员查询数据
//            wrappers.ne(AssetsManage::getAsset_state,AssetsStatusEnums.FAULT.getType());
            wrappers.isNull(AssetsManage::getAsset_state_bx);//已经故障的不给与申请
            wrappers.and(wp -> wp
                    .eq(AssetsManage::getUse_people, beanParam.getUser_id()).or()
                    .isNull(AssetsManage::getUse_people)
                    .eq(AssetsManage::getAdmin_user, beanParam.getUser_id())
            );
        }
        List<AssetsManage> assetsManageVos = assetsManageMapper.selectList(wrappers.eq(AssetsManage::getIs_del, G.ISDEL_NO));
        // 用户map
        Map<Integer, SysUser> userMap = commonDataTools.getUserMap();
        // 部门map
        Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();
        // 资产类型map
        Map<Integer, AssetsType> assetsTypeMap = commonDataTools.getAssetsTypeMap();
        // 品牌map
        Map<Integer, SysDict> brandMap = commonDataTools.getSysDictMap("brand_type");
        // 折旧方法map
        Map<Integer, SysDict> depreciationMap = commonDataTools.getSysDictMap("depreciation_type");
        // 供应商map
        Map<Integer, Supplier> supplierMap = commonDataTools.getSupplierMap();
        // 位置map
        Map<Integer, SysPosition> positionMap = commonDataTools.getPositionMap();
        for (AssetsManage bean : assetsManageVos) {
            bean.setUse_people_name(commonDataTools.getValue(userMap, bean.getUse_people(), "user_name"));
            bean.setAdmin_user_name(commonDataTools.getValue(userMap, bean.getAdmin_user(), "user_name"));
            bean.setCreate_user_name(commonDataTools.getValue(userMap, bean.getCreate_user(), "user_name"));
            bean.setRegister_user_name(commonDataTools.getValue(userMap, bean.getRegister_user(), "user_name"));
            bean.setDept_name(commonDataTools.getValue(deptMap, bean.getDept_id(), "dept_name"));
            bean.setAsset_type_name(commonDataTools.getValue(assetsTypeMap, bean.getAsset_type_id(), "assets_type_name"));
            // 品牌
            BrandManage brand = brandManageMapper.selectById(bean.getBrand_id());
            bean.setBrand_name(null != brand ? brand.getBrand_name() : "");
            String depreciation_name = bean.getDepreciation_method();
            if (null != bean.getDepreciation_method() && StringUtils.isNotBlank(bean.getDepreciation_method())) {
                bean.setDepreciation_method(!isNumeric(bean.getDepreciation_method()) ? "" : commonDataTools.getValue(depreciationMap, Integer.valueOf(bean.getDepreciation_method()), "label"));
            }
            bean.setSupplier_name(commonDataTools.getValue(supplierMap, bean.getSupplier_id(), "supplier_name"));
            bean.setPlace_name(commonDataTools.getValue(positionMap, bean.getPosition_id(), "position_name"));
            bean.setAsset_epc(bean.getRfid());
            bean.setQr_code(genratorQrCode(bean.getAsset_code()));

            // 折旧日期
            String nowDate = DateUtil.formatDate(new Date());
            if (StringUtils.isNotBlank(beanParam.getDepreciation_time_str())) {
                nowDate = beanParam.getDepreciation_time_str();
            }
            bean.setDepreciation_time_str(nowDate);

            // 判断是否是折旧资产
            if (null == bean.getIs_depreciation() || bean.getIs_depreciation() != 1) {
                continue;
            }
            // 判断折旧前置条件是否满足
            if (null == bean.getPurchase_amount() || null == bean.getAge_limit()
                    || null == bean.getDepreciation_method() || StringUtils.isBlank(depreciation_name)
                    || null == bean.getNet_residual_rate() || StringUtils.isBlank(bean.getNet_residual_rate())
                    || null == bean.getScrap_time()

            ) {
                continue;
            }
            // 净残值  net_residual_value  净残值=购买金额*净残率
            BigDecimal amount = new BigDecimal(bean.getPurchase_amount());
            BigDecimal rate = new BigDecimal(bean.getNet_residual_rate()).multiply(new BigDecimal(0.01));
            BigDecimal net_residual_value = amount.multiply(rate);
            bean.setNet_residual_value(net_residual_value.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");

            // 已使用月份
            String createTime = DateUtil.formatDate(bean.getCreate_time());
            // 计算两个日期相减并转换成月份 注: 不到一月,算一个月
            long mouth = ChronoUnit.MONTHS.between(LocalDate.parse(createTime), LocalDate.parse(nowDate)) + 1;
            bean.setUsed_month((int) mouth + "");
            // 本月折旧  本月折旧=固定资产原价×（1-残值率）*【（报废年月-折旧日期所在年月）/12】/预计使用年数总和÷12；
            //（1-残值率）
            BigDecimal num1 = new BigDecimal(1).subtract(rate);
            BigDecimal num2 = amount.multiply(num1);
            // 计算两个日期相减并转换成月份
            // 报废年月
            String scrapTime = DateUtil.formatDate(bean.getScrap_time());
            long mouth1 = ChronoUnit.MONTHS.between(LocalDate.parse(nowDate), LocalDate.parse(scrapTime)) + 1;
            BigDecimal num3 = new BigDecimal(mouth1).divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal num4 = num2.multiply(num3);
            BigDecimal num5 = num4.divide(new BigDecimal(bean.getAge_limit()), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal num6 = num5.divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
            bean.setThis_month_depreciation(num6.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
            // 累计折旧
            BigDecimal num7 = new BigDecimal(0);
            if ("1".equals(depreciation_name)) {
                // 1）平均年限法 月折旧额*已使用月份
                num7 = num6.multiply(new BigDecimal(bean.getUsed_month()));
            } else {
                // 2）年数总和法  ∑固定资产原价×（1-残值率）*【（报废年月-n）/12】/预计使用年数总和÷12
                List<String> listMouth = getTimeOriginalList(createTime, nowDate);
                for (String mouthStr : listMouth) {
                    // 计算两个日期相减并转换成月份
                    long mouth2 = ChronoUnit.MONTHS.between(LocalDate.parse(mouthStr), LocalDate.parse(scrapTime)) + 1;
                    BigDecimal mouth3 = new BigDecimal(mouth2).divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal mouth4 = num2.multiply(mouth3);
                    BigDecimal mouth5 = mouth4.divide(new BigDecimal(bean.getAge_limit()), 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal mouth6 = mouth5.divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
                    num7 = num7.add(mouth6);
                }
            }
            bean.setAccumulated_depreciation(num7.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
            // 净值
            bean.setNet_value(amount.subtract(num7).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");

        }
        ;

        return new PageInfo<>(assetsManageVos);
    }

    public static List<String> getTimeOriginalList(String startDate, String endDate) {
        SimpleDateFormat sdf;
        int calendarType;

//        switch (startDate.length()) {
//            case 19:
//                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                calendarType = Calendar.DATE;
//                break;
//            case 10:
//                sdf = new SimpleDateFormat("yyyy-MM-dd");
//                calendarType = Calendar.DATE;
//                break;
//            case 7:
//                sdf = new SimpleDateFormat("yyyy-MM");
//                calendarType = Calendar.MONTH;
//                break;
//            case 4:
//                sdf = new SimpleDateFormat("yyyy");
//                calendarType = Calendar.YEAR;
//                break;
//            default:
//                return null;
//        }
        sdf = new SimpleDateFormat("yyyy-MM");
        calendarType = Calendar.MONTH;

        List<String> result = new ArrayList<>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(startDate));
            min.add(calendarType, 0);
            max.setTime(sdf.parse(endDate));
            max.add(calendarType, 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar curr = min;
        while (curr.before(max)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            result.add(formatter.format(min.getTime()));
            curr.add(calendarType, 1);
        }
        return result;
    }


    /**
     * 资产轨迹
     * 列表查询 分页
     * thr
     */
    public PageInfo<AssetsManage> toLists(Integer pageIndex, Integer pageSize, AssetsManage beanParam) {
        PageHelper.startPage(pageIndex, pageSize);
        // 支持按资产编号、品类、资产状态（闲置、使用、领用、借用、调拨、故障、处置）、品牌、存放部门、资产名称
        LambdaQueryWrapper<AssetsManage> wrappers = Wrappers.<AssetsManage>lambdaQuery();
        // 资产编号
        wrappers.like(StringUtils.isNotBlank(beanParam.getAsset_code()), AssetsManage::getAsset_code, beanParam.getAsset_code());
        // 品类
        wrappers.eq(null != beanParam.getAsset_type_id(), AssetsManage::getAsset_type_id, beanParam.getAsset_type_id());
        // 资产状态
        wrappers.eq(null != beanParam.getAsset_state(), AssetsManage::getAsset_state, beanParam.getAsset_state());
        // 品牌
        wrappers.eq(null != beanParam.getBrand_id(), AssetsManage::getBrand_id, beanParam.getBrand_id());
        // 存放部门
        wrappers.eq(null != beanParam.getDept_id(), AssetsManage::getDept_id, beanParam.getDept_id());
        // 资产名称
        wrappers.like(StringUtils.isNotBlank(beanParam.getAsset_name()), AssetsManage::getAsset_name, beanParam.getAsset_name());
        // 判断查询是我的资产还是全部资产
        wrappers.eq(null != beanParam.getCreate_user(), AssetsManage::getCreate_user, beanParam.getCreate_user());
        // 复选框状态
        wrappers.in(!CollectionUtils.isEmpty(beanParam.getAssetStatusList()), AssetsManage::getAsset_state, beanParam.getAssetStatusList());

        wrappers.eq(null != beanParam.getSupplier_id(), AssetsManage::getSupplier_id, beanParam.getSupplier_id());
        // 存放位置
        wrappers.eq(null != beanParam.getPosition_id(), AssetsManage::getPosition_id, beanParam.getPosition_id());
        wrappers.like(StringUtils.isNotBlank(beanParam.getRfid()), AssetsManage::getRfid, beanParam.getRfid());
        wrappers.eq(AssetsManage::getIs_del, 0);
        wrappers.orderByDesc(AssetsManage::getPosition_change_time, AssetsManage::getId);
        List<AssetsManage> assetsManageVos = assetsManageMapper.selectList(wrappers);

        // 用户map
        Map<Integer, SysUser> userMap = commonDataTools.getUserMap();
        // 部门map
        Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();
        // 资产类型map
        Map<Integer, AssetsType> assetsTypeMap = commonDataTools.getAssetsTypeMap();
        // 品牌map
        Map<Integer, SysDict> brandMap = commonDataTools.getSysDictMap("brand_type");
        // 折旧方法map
        Map<Integer, SysDict> depreciationMap = commonDataTools.getSysDictMap("depreciation_type");
        // 供应商map
        Map<Integer, Supplier> supplierMap = commonDataTools.getSupplierMap();
        // 位置map
        Map<Integer, SysPosition> positionMap = commonDataTools.getPositionMap();
        for (AssetsManage bean : assetsManageVos) {
            bean.setUse_people_name(commonDataTools.getValue(userMap, bean.getUse_people(), "user_name"));
            bean.setAdmin_user_name(commonDataTools.getValue(userMap, bean.getAdmin_user(), "user_name"));
            bean.setCreate_user_name(commonDataTools.getValue(userMap, bean.getCreate_user(), "user_name"));
            bean.setRegister_user_name(commonDataTools.getValue(userMap, bean.getRegister_user(), "user_name"));
            bean.setDept_name(commonDataTools.getValue(deptMap, bean.getDept_id(), "dept_name"));
            bean.setAsset_type_name(commonDataTools.getValue(assetsTypeMap, bean.getAsset_type_id(), "assets_type_name"));
            bean.setBrand_name(commonDataTools.getValue(brandMap, bean.getBrand_id(), "label"));
            if (null != bean.getDepreciation_method() && StringUtils.isNotBlank(bean.getDepreciation_method())) {
                bean.setDepreciation_method(!isNumeric(bean.getDepreciation_method()) ? "" : commonDataTools.getValue(depreciationMap, Integer.valueOf(bean.getDepreciation_method()), "label"));
            }
            bean.setSupplier_name(commonDataTools.getValue(supplierMap, bean.getSupplier_id(), "supplier_name"));
            bean.setPlace_name(commonDataTools.getValue(positionMap, bean.getPosition_id(), "position_name"));
            bean.setAsset_epc(bean.getRfid());
        }
        ;

        return new PageInfo<>(assetsManageVos);
    }

    /**
     * 判断String 是否是数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 查询所有
     *
     * @param beanParam
     * @return
     */
    public List<AssetsManage> getList(AssetsManage beanParam) {

        List<AssetsManage> assetsManageVos = assetsManageMapper.toList(beanParam);

        return assetsManageVos;
    }

    /**
     * 根据id查询 信息详情
     *
     * @param id
     * @return
     */
    public AssetsManage getById(Integer id) {
        AssetsManage assetsManage = selectById(id);
        if (null == assetsManage) {
            return null;
        }
        Map<Integer, SysDict> brandType = commonDataTools.getSysDictMap("brand_type");
        assetsManage.setBrand_name(commonDataTools.getValue(brandType, assetsManage.getBrand_id(), "label"));
        assetsManage.setRegister_user_name(commonDataTools.getValue(commonDataTools.getUserMap(), assetsManage.getRegister_user(), "user_name"));
        assetsManage.setUse_people_name(commonDataTools.getValue(commonDataTools.getUserMap(), assetsManage.getUse_people(), "user_name"));

        // 计量党委map
        Map<Integer, SysDict> unitMap = commonDataTools.getSysDictMap("unit");
        // 供应商map
        Map<Integer, Supplier> supplierMap = commonDataTools.getSupplierMap();
        assetsManage.setSupplier_name(commonDataTools.getValue(supplierMap, assetsManage.getSupplier_id(), "supplier_name"));
        assetsManage.setPurchase_time_str(DateUtil.formatDate(assetsManage.getPurchase_time()));
        assetsManage.setGuarantee_end_time_str(DateUtil.formatDate(assetsManage.getGuarantee_end_time()));
        assetsManage.setScrap_time_str(DateUtil.formatDate(assetsManage.getScrap_time()));
        // 判断是否为数字
        if (null != assetsManage.getUnit() && StringUtils.isNotBlank(assetsManage.getUnit())) {
            assetsManage.setUnit_name(!isNumeric(assetsManage.getUnit()) ? "" : commonDataTools.getValue(unitMap, Integer.valueOf(assetsManage.getUnit()), "label"));
        }
        // 折旧方法map
        Map<Integer, SysDict> depreciationMap = commonDataTools.getSysDictMap("depreciation_type");
        if (null != assetsManage.getDepreciation_method() && StringUtils.isNotBlank(assetsManage.getDepreciation_method())) {
            assetsManage.setDepreciation_method(!isNumeric(assetsManage.getDepreciation_method()) ? "" : commonDataTools.getValue(depreciationMap, Integer.valueOf(assetsManage.getDepreciation_method()), "label"));
        }
        // 部门map
        Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();
        assetsManage.setDept_name(commonDataTools.getValue(deptMap, assetsManage.getDept_id(), "dept_name"));
        //asset_type_name   admin_user_name
        if (null != assetsManage.getAdmin_user()) {
            SysUser sysUser = sysUserService.selectById(assetsManage.getAdmin_user());
            if (null != sysUser) {
                assetsManage.setAdmin_user_name(sysUser.getUser_name());
            }
        }

        if (null != assetsManage.getPosition_id()) {
            SysPosition sysPosition = sysPositionService.selectById(assetsManage.getPosition_id());
            if (null != sysPosition) {
                assetsManage.setPlace_name(sysPosition.getPosition_name());
            }
        }

        if (null != assetsManage.getAsset_type_id()) {
            AssetsType assetsType = assetsTypeMapper.selectById(assetsManage.getAsset_type_id());
            if (null != assetsType) {
                assetsManage.setAsset_type_name(assetsType.getAssets_type_name());
            }
        }
        List<AssetsFileMember> files = assetsFileMemberMapper.selectList(Wrappers.<AssetsFileMember>lambdaQuery().eq(AssetsFileMember::getAssets_id, id));
        if (!CollectionUtils.isEmpty(files)) {
            // 从files中取出file_id
            List<Integer> fileIds = files.stream().map(AssetsFileMember::getFile_id).collect(Collectors.toList());
            assetsManage.setFileManageList(fileManageMapper.selectList(Wrappers.<FileManage>lambdaQuery().in(FileManage::getId, fileIds)));
        }

        // 取得库存id
        List<InventoryManagement> inventoryManagements = inventoryManagementMapper.selectList(Wrappers.<InventoryManagement>lambdaQuery()
                .eq(InventoryManagement::getAsset_name, assetsManage.getAsset_name())
                .eq(InventoryManagement::getAsset_type_id, assetsManage.getAsset_type_id())
                .eq(InventoryManagement::getSpecifications, assetsManage.getSpecification())
                .eq(InventoryManagement::getUnit_of_measurement, assetsManage.getUnit())
                .eq(InventoryManagement::getActual_amount, assetsManage.getPurchase_amount())
                .eq(InventoryManagement::getCreate_time, assetsManage.getPurchase_time())
        );
        if (!CollectionUtils.isEmpty(inventoryManagements)) {
            assetsManage.setInventoryManagement_id(inventoryManagements.get(0).getId());
        }
        return assetsManage;
    }

    /**
     * 资产登记
     */
    public ResultInfo addAssetsManage(AssetsManage bean) {
        // 判断是否存在
        if (null == bean || null == bean.getAsset_type_id() || null == assetsTypeMapper.selectById(bean.getAsset_type_id())) {
            return ResultInfo.error("资产类型不能为空");
        }
        if (null == bean.getQuantity() || bean.getQuantity() <= 0) {
            return ResultInfo.error("数量不能为空");
        }

        // 判断是否是库内登记
        if (null != bean.getRegister_type() && bean.getRegister_type() == 1) {
            if (null == bean.getQuantity() || null == bean.getInventoryManagement_id()
                    || null == inventoryManagementMapper.selectById(bean.getInventoryManagement_id())
                    || null == inventoryManagementMapper.selectById(bean.getInventoryManagement_id()).getInventory_num()
            ) {
                return ResultInfo.error("库内登记数量有误, 请检查");
//            if (null != inventoryManagement.getInventory_num() && inventoryManagement.getInventory_num() >= bean.getQuantity()) {
//
            } else {
                InventoryManagement inventoryManagement = inventoryManagementMapper.selectById(bean.getInventoryManagement_id());
                if (inventoryManagement.getInventory_num() >= bean.getQuantity()) {
                    inventoryManagement.setInventory_num(inventoryManagement.getInventory_num() - bean.getQuantity());
                    inventoryManagementMapper.updateById(inventoryManagement);
                } else {
                    return ResultInfo.error("库存数量不足, 请检查");
                }
            }
        }

        // 定义num 数值
        Integer num = bean.getQuantity();
        // 资产编号（自动生成-根据所选类型品类、登记日期、自增规则进行自动拼接）
//        bean.setAsset_code(commonDataTools.getNo(DataType.ASSET_MANAGE.getType(), assetsTypeMapper.selectById(bean.getAsset_type_id())));
        for (int i = 0; i < num; i++) {
            bean.setAsset_code(commonDataTools.getNo(DataType.ASSET_MANAGE.getType(), assetsTypeMapper.selectById(bean.getAsset_type_id())));
            bean.setIs_del(String.valueOf(0));
            bean.setRfid(toHexString(bean.getAsset_code()));
            // 创建时间
            bean.setCreate_time(new Date());
            // 创建者
            bean.setCreate_user(sysUserService.getUser().getId());
            // 二维码
            bean.setQr_code(genratorQrCode(bean.getAsset_code()));
            insert(bean);

            // 附件处理
            if (!CollectionUtils.isEmpty(bean.getFileMemberList())) {
                for (Integer file_id : bean.getFileMemberList()) {
                    assetsFileMemberMapper.insert(AssetsFileMember.builder().file_id(file_id).assets_id(bean.getId()).build());
                }
            }
            operationRecordService.addOperationRecord(bean.getId(), bean.getAsset_code(), "资产登记", OperationRecordService.MODE_TYPE_ASSETS);
        }

        return ResultInfo.success();
    }

    /**
     * 10进制 字符串 转为 16进制字符串
     */
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        //标签规则设定
        int b = str.length() % ((s.length() - 1) / 2 + 1) == 0 ? 0 : ((s.length() - 1) / 2 + 1) - str.length() % ((s.length() - 1) / 2 + 1);
        for (int i = 0; i < b; i++) {
            str += "0";
        }
        return str;
    }

    /**
     * 资产复制
     */
    public ResultInfo copyAssetsManage(AssetsManage bean) {
        // 判断是否存在
        if (null == bean || null == bean.getAsset_type_id() || null == assetsTypeMapper.selectById(bean.getAsset_type_id())) {
            return ResultInfo.error("资产类型不能为空");
        }
        if (null == bean.getQuantity() || bean.getQuantity() <= 0) {
            return ResultInfo.error("数量不能为空");
        }
        // 判断是否是库内登记
        if (null != bean.getRegister_type() && bean.getRegister_type() == 1) {
            if (null == bean.getQuantity() || null == bean.getInventoryManagement_id()
                    || null == inventoryManagementMapper.selectById(bean.getInventoryManagement_id())
                    || null == inventoryManagementMapper.selectById(bean.getInventoryManagement_id()).getInventory_num()
            ) {
                return ResultInfo.error("库内登记数量有误, 请检查");
//            if (null != inventoryManagement.getInventory_num() && inventoryManagement.getInventory_num() >= bean.getQuantity()) {
//
            } else {
                InventoryManagement inventoryManagement = inventoryManagementMapper.selectById(bean.getInventoryManagement_id());
                if (inventoryManagement.getInventory_num() >= bean.getQuantity()) {
                    inventoryManagement.setInventory_num(inventoryManagement.getInventory_num() - bean.getQuantity());
                    inventoryManagementMapper.updateById(inventoryManagement);
                } else {
                    return ResultInfo.error("库存数量不足, 请检查");
                }
            }
        }
        // 资产编号（自动生成-根据所选类型品类、登记日期、自增规则进行自动拼接）
        bean.setAsset_code(commonDataTools.getNo(DataType.ASSET_MANAGE.getType(), assetsTypeMapper.selectById(bean.getAsset_type_id())));
        bean.setIs_del(String.valueOf(0));
        bean.setRfid(toHexString(bean.getAsset_code()));
        // 创建时间
        bean.setCreate_time(new Date());
        // 创建者
        bean.setCreate_user(sysUserService.getUser().getId());
        // 二维码
        bean.setQr_code(genratorQrCode(bean.getAsset_code()));
        insert(bean);
        // 附件处理
        if (!CollectionUtils.isEmpty(bean.getFileMemberList())) {
            for (Integer file_id : bean.getFileMemberList()) {
                assetsFileMemberMapper.insert(AssetsFileMember.builder().file_id(file_id).assets_id(bean.getId()).build());
            }
        }
        operationRecordService.addOperationRecord(bean.getId(), bean.getAsset_code(), "资产登记", OperationRecordService.MODE_TYPE_ASSETS);
        return ResultInfo.success();
    }

    /**
     * 编辑
     * 资产维护  可对已登记的资产进行修改、删除、设置折旧信息操作
     */
    public ResultInfo editAssetsManage(AssetsManage bean) {
        if (null != bean && StringUtils.isNotBlank(bean.getUnit_price()) && ".00".equals(bean.getUnit_price().trim())) {
            bean.setUnit_price("");
        }
        bean.setQr_code(genratorQrCode(bean.getAsset_code()));
        // 修改者
        bean.setUpdate_user(sysUserService.getUser().getId());
        // 修改时间
        bean.setUpdate_time(new Date());
        // 原资产编号
        AssetsManage old = assetsManageMapper.selectById(bean.getId());

        if(null != old.getUse_people() && null != bean.getUse_people() && !Objects.equals(old.getUse_people(), bean.getUse_people())) {
            // 当前用户(操作人)
            String currentUser = sysUserService.getUser().getUser_name();
            // 原使用人
            String oldUsePeople = sysUserService.selectById(old.getUse_people()) == null ? "-" : sysUserService.selectById(old.getUse_people()).getUser_name();
            // 日志消息
            String msg = "资产维护-" +currentUser + ": 编号为" + old.getAsset_code() + "的资产, 使用人从" + oldUsePeople
                    + "修改为" + (null == bean.getUse_people() ? "-" : sysUserService.selectById(bean.getUse_people()).getUser_name());
            // 添加日志
            operationRecordService.addOperationRecord(bean.getId(), bean.getAsset_code(), msg, OperationRecordService.MODE_TYPE_ASSETS);
        }

        updateById(bean);
        // 附件处理
        assetsFileMemberMapper.delete(Wrappers.<AssetsFileMember>lambdaQuery().eq(AssetsFileMember::getAssets_id, bean.getId()));
        if (!CollectionUtils.isEmpty(bean.getFileMemberList())) {
            for (Integer file_id : bean.getFileMemberList()) {
                assetsFileMemberMapper.insert(AssetsFileMember.builder().file_id(file_id).assets_id(bean.getId()).build());
            }
        }
        return ResultInfo.success();
    }

    /**
     * 删除
     */
    public ResultInfo deleteAssetsManage(AssetsManage bean) {
        // 修改者
        bean.setUpdate_user(sysUserService.getUser().getId());
        // 修改时间
        bean.setUpdate_time(new Date());
        // 逻辑删除
        bean.setIs_del(String.valueOf(1));
        updateById(bean);
        return ResultInfo.success();
    }

    /**
     * 根据资产编号查询一条数据
     */
    public AssetsManage getAssetsManageByCode(String asset_code) {
        List<AssetsManage> list = selectList(Wrappers.<AssetsManage>lambdaQuery()
                .eq(AssetsManage::getAsset_code, asset_code)
                .eq(AssetsManage::getIs_del, "0")
        );
        if (!CollectionUtils.isEmpty(list)) {
            AssetsManage assetsManage = list.get(0);
            // 用户map
            Map<Integer, SysUser> userMap = commonDataTools.getUserMap();
            // 部门map
            Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();
            // 资产类型map
            Map<Integer, AssetsType> assetsTypeMap = commonDataTools.getAssetsTypeMap();
            // 品牌map
            Map<Integer, SysDict> brandMap = commonDataTools.getSysDictMap("brand_type");
            // 折旧方法map
            Map<Integer, SysDict> depreciationMap = commonDataTools.getSysDictMap("depreciation_type");
            // 计量党委map
            Map<Integer, SysDict> unitMap = commonDataTools.getSysDictMap("unit");
            // 供应商map
            Map<Integer, Supplier> supplierMap = commonDataTools.getSupplierMap();
            // 位置map
            Map<Integer, SysPosition> positionMap = commonDataTools.getPositionMap();
            assetsManage.setUse_people_name(commonDataTools.getValue(userMap, assetsManage.getUse_people(), "user_name"));
            assetsManage.setAdmin_user_name(commonDataTools.getValue(userMap, assetsManage.getAdmin_user(), "user_name"));
            assetsManage.setCreate_user_name(commonDataTools.getValue(userMap, assetsManage.getCreate_user(), "user_name"));
            assetsManage.setRegister_user_name(commonDataTools.getValue(userMap, assetsManage.getRegister_user(), "user_name"));
            assetsManage.setDept_name(commonDataTools.getValue(deptMap, assetsManage.getDept_id(), "dept_name"));
            assetsManage.setAsset_type_name(commonDataTools.getValue(assetsTypeMap, assetsManage.getAsset_type_id(), "assets_type_name"));
            assetsManage.setBrand_name(commonDataTools.getValue(brandMap, assetsManage.getBrand_id(), "label"));
            if (null != assetsManage.getDepreciation_method() && StringUtils.isNotBlank(assetsManage.getDepreciation_method())) {
                assetsManage.setDepreciation_method(!isNumeric(assetsManage.getDepreciation_method()) ? "" : commonDataTools.getValue(depreciationMap, Integer.valueOf(assetsManage.getDepreciation_method()), "label"));
            }
            assetsManage.setSupplier_name(commonDataTools.getValue(supplierMap, assetsManage.getSupplier_id(), "supplier_name"));
            assetsManage.setPlace_name(commonDataTools.getValue(positionMap, assetsManage.getPosition_id(), "position_name"));
            assetsManage.setPurchase_time_str(DateUtil.formatDate(assetsManage.getPurchase_time()));
            assetsManage.setGuarantee_end_time_str(DateUtil.formatDate(assetsManage.getGuarantee_end_time()));
            assetsManage.setScrap_time_str(DateUtil.formatDate(assetsManage.getScrap_time()));
            assetsManage.setUnit_name(commonDataTools.getValue(unitMap, Integer.valueOf(assetsManage.getUnit()), "label"));

            return assetsManage;
        }
        return null;
    }

    /**
     * 生成二维码
     *
     * @param assetsManage
     * @return
     */
    public String genratorQrCode(AssetsManage assetsManage) {
        AssetsManage am = selectById(assetsManage.getId());
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(am);
        JSONObject qrCodeJson = new JSONObject();
        //生成二维码AssetsManage 中字段太多 传参会报data太大 只取个别字段
        qrCodeJson.put("asset_code", jsonObject.get("asset_code"));//     资产编号
        String qrCode = "请刷新重新生成二维码";
        try {
            qrCode = QrCodeUtil.encode_QR_CODE(qrCodeJson.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qrCode;
    }

    public String genratorQrCode(String code) {
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
     * 得到资产编号
     *
     * @return
     */
    public String getAssetsCode(Integer asset_type_id) {
        return commonDataTools.getNo(DataType.ASSET_MANAGE.getType(), assetsTypeMapper.selectById(asset_type_id));
    }

    /**
     * 得到部门
     *
     * @param dept_id
     * @return
     */
    public SysDepartment getPositionIdByDeptId(Integer dept_id) {
        return sysDepartmentMapper.selectById(dept_id);
    }

    public List<String> getAssetsCodes(Integer assets_type, Integer num) {
        List<String> codes = new ArrayList<>();
        AssetsType assetsType = assetsTypeMapper.selectById(assets_type);
        String code = assetsType.getAssets_type_code().toUpperCase();
        for (int i = 0; i < num; i++) {
            int codeNum = i + 1;
            codes.add(
                    String.format("%s%s%03d", code, CommonDataTools.getToday(),
                            assetsManageMapper.selectCount(
                                    Wrappers.<AssetsManage>query().apply(true, "to_char(NOW(), 'yyyy-MM-dd') =  to_char(create_time, 'yyyy-MM-dd')")) + codeNum
                    )
            );
        }
        return codes;
    }

    public SysPosition getPositionByDeptId(Integer dept_id) {
        SysDepartment dept = sysDepartmentMapper.selectById(dept_id);
        SysPosition position = sysPositionService.selectById(dept.getDept_position());
        return position;
    }

    public SysPlace getLocationByPositionId(Integer position_id) {
        SysPosition position = sysPositionService.selectById(position_id);
        if (position != null) {
            return sysPlaceService.selectById(position.getPlace_id());
        }
        return null;
    }


    /**
     * 变更资产编号
     */
    public ResultInfo correctionNoData() {
        // 得到全部资产
        List<AssetsManage> assetsManages = assetsManageMapper.selectList(Wrappers.<AssetsManage>query());
        for (int i = 0; i < assetsManages.size(); i++) {
            AssetsManage bean = assetsManages.get(i);
            // 得到资产类型
            AssetsType assetsType = assetsTypeMapper.selectById(bean.getAsset_type_id());
            if (null == assetsType) {
                continue;
            }
            String code = assetsType.getAssets_type_code().toUpperCase();
            String today = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

            String num = "";
            if (i < 1000) {
                // 得到当前日期 -1 天
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date date = calendar.getTime();
                today = dateFormat.format(date);
                num = String.format("%03d", i);
            } else {
                num = String.format("%03d", i - 999);
                today = dateFormat.format(new Date());
            }
            // 将 i 转为3位数的字符串


            String number = String.format("%s%s%s", code, today, num);
            bean.setAsset_code(number);
            bean.setRfid(toHexString(bean.getAsset_code()));
            // 二维码
            bean.setQr_code(genratorQrCode(bean.getAsset_code()));
            assetsManageMapper.updateById(bean);
        }

        return ResultInfo.success();
    }

    /**
     * 按资产状态统计资产数量
     *
     * @param asstestTypeId
     * @return
     */
    public List<StatisticsAssetStateVo> statisticsByAssetsState(Integer asstestTypeId) {
        return assetsManageMapper.statisticsByAssetsState(asstestTypeId);
    }

    /**
     * 批量登记,导出模板
     *
     * @return
     */
    public ResultInfo toExportTemplate(HttpServletResponse response) {
        //导出excel模板
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("资产分类", StringUtils.EMPTY);
            row.put("资产名称", StringUtils.EMPTY);
            row.put("状态", StringUtils.EMPTY);
            row.put("数量", StringUtils.EMPTY);
            row.put("预算单价", StringUtils.EMPTY);
            row.put("计量单位", StringUtils.EMPTY);
            row.put("实际金额", StringUtils.EMPTY);
            row.put("品牌", StringUtils.EMPTY);
            row.put("规格型号", StringUtils.EMPTY);
            row.put("购置日期", StringUtils.EMPTY);
            row.put("所属管理员", StringUtils.EMPTY);
            row.put("使用人", StringUtils.EMPTY);

            row.put("英文名称", StringUtils.EMPTY);
            row.put("供应商名称", StringUtils.EMPTY);
            row.put("厂牌号码", StringUtils.EMPTY);
            row.put("部门", StringUtils.EMPTY);
            row.put("保修期", StringUtils.EMPTY);
            row.put("保修截止", StringUtils.EMPTY);
            row.put("当前位置", StringUtils.EMPTY);
            row.put("备注", StringUtils.EMPTY);
            list.add(row);
        }

        ExcelWriter writer = ExcelUtil.getWriter();
        StyleSet styleSet = writer.getStyleSet();
        Sheet sheet = writer.getSheet();
        //设置下拉数据 从第几行开始
        int firstRow = 1;


        //writer.setFreezePane(3);


//        //资产分类信息
//        List<AssetsType> assetsTypeList = assetsTypeMapper.selectList(Wrappers.<AssetsType>lambdaQuery()
//                .eq(AssetsType::getIs_del, "0"));
//        String [] typeList = assetsTypeList.stream().distinct().map(AssetsType::getAssets_type_name).toArray(String[]::new);
//        if(!CollectionUtils.isEmpty(assetsTypeList)){
//            writer.addValidationData(setSelectCol(styleSet, sheet, typeList, firstRow, 0));
//        }
        //资产状态信息
        String[] statusList = {"闲置", "使用", "故障", "处置", "异常"};
        writer.addValidationData(setSelectCol(styleSet, sheet, statusList, firstRow, 2));
        //计量单位信息
        List<SysDict> units = sysDictMapper.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getType, "unit").eq(SysDict::getIs_del, "0"));
        String[] unitList = units.stream().map(SysDict::getLabel).toArray(String[]::new);
        if (!CollectionUtils.isEmpty(units)) {
            writer.addValidationData(setSelectCol(styleSet, sheet, unitList, firstRow, 5));
        }

//        //品牌信息
//        List<SysDict> brands = sysDictMapper.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getType, "brand_type").eq(SysDict::getIs_del, "0"));
//        String[] brandList = brands.stream().map(SysDict::getLabel).toArray(String[]::new);
//        if (!CollectionUtils.isEmpty(brands)) {
//            writer.addValidationData(setSelectCol(styleSet, sheet, brandList, firstRow, 7));
//        }
//        //管理员信息
//        List<SysUser> users = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getIs_del, "0"));
//        String[] userList = users.stream().map(SysUser::getUser_name).toArray(String[]::new);
//        if (!CollectionUtils.isEmpty(users)) {
//            writer.addValidationData(setSelectCol(styleSet, sheet, userList, firstRow, 10));
//            //使用人信息
//            writer.addValidationData(setSelectCol(styleSet, sheet, userList, firstRow, 11));
//        }
//        //供应商信息
//        List<Supplier> suppliers = supplierMapper.selectList(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getIs_del, "0"));
//        String[] supplierList = suppliers.stream().map(Supplier::getSupplier_name).toArray(String[]::new);
//        if (!CollectionUtils.isEmpty(suppliers)) {
//            writer.addValidationData(setSelectCol(styleSet, sheet, supplierList, firstRow, 13));
//        }
//        //部门信息
//        List<SysDepartment> depts = sysDepartmentMapper.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, "0"));
//        String[] deptList = depts.stream().map(SysDepartment::getDept_name).toArray(String[]::new);
//        if (!CollectionUtils.isEmpty(depts)) {
//            writer.addValidationData(setSelectCol(styleSet, sheet, deptList, firstRow, 15));
//        }
        // 设置只导出有别名的字段
        writer.setOnlyAlias(true);
        // 设置默认行高
        writer.setDefaultRowHeight(20);
        // 设置冻结行
        writer.setFreezePane(1);

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        writeCell(writer, 0, "资产分类", true);
        writeCell(writer, 1, "资产名称", true);
        writeCell(writer, 2, "状态", true);
        writeCell(writer, 3, "数量", true);
        writeCell(writer, 4, "预算单价", false);
        writeCell(writer, 5, "计量单位", true);
        writeCell(writer, 6, "实际金额", true);
        writeCell(writer, 7, "品牌", false);
        writeCell(writer, 8, "规格型号", true);
        writeCell(writer, 9, "购置日期", false);
        writeCell(writer, 10, "所属管理员", true);
        writeCell(writer, 11, "使用人", false);
        writeCell(writer, 12, "英文名称", false);
        writeCell(writer, 13, "供应商名称", false);
        writeCell(writer, 14, "厂牌号码", false);
        writeCell(writer, 15, "部门", true);
        writeCell(writer, 16, "保修期", false);
        writeCell(writer, 17, "保修截止", false);
        writeCell(writer, 18, "当前位置", false);
        writeCell(writer, 19, "备注", false);

//        writer.setRowHeight(0, 30);

//        writer.setOnlyAlias(true);
//        writer.write(list, true);

//        OutputStream outputStream = null;


        //资产分类信息
        List<AssetsType> assetsTypeList = assetsTypeMapper.selectList(Wrappers.<AssetsType>lambdaQuery()
                .eq(AssetsType::getIs_del, "0"));
        List<String> typeList = assetsTypeList.stream().distinct().map(AssetsType::getAssets_type_name).collect(Collectors.toList());

        //品牌信息
        List<BrandManage> brands = brandManageMapper.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, "0"));
        List<String> brandList = brands.stream().map(BrandManage::getBrand_name).collect(Collectors.toList());
        //管理员信息
        List<SysUser> users = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getIs_del, "0"));
        List<String> userList = users.stream().map(SysUser::getUser_name).collect(Collectors.toList());
        //供应商信息
        List<Supplier> suppliers = supplierMapper.selectList(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getIs_del, "0"));
        List<String> supplierList = suppliers.stream().map(Supplier::getSupplier_name).collect(Collectors.toList());
        //部门信息
        List<SysDepartment> depts = sysDepartmentMapper.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, "0"));
        List<String> deptList = depts.stream().map(SysDepartment::getDept_name).collect(Collectors.toList());

        //构建数据
        List<List<?>> rows = CollUtil.newArrayList(typeList, brandList, userList, userList, supplierList, deptList);

        String[] types = {"A", "B","C", "D", "E", "F", "G", "H", "I", "J", "K", "L","M", "N","O", "P", "Q", "R", "S", "T", "U", "V", "W","X","Y","Z"};

        Integer[] firstCol = {0, 7, 10, 11, 13, 15};

        for (int i=0; i< rows.size(); i++){
            List<?> cols = rows.get(i);
            String dictSheet = "dict" + i;
            //创建第二个Sheet
            writer.setSheet(dictSheet);
            //将Sheet2中的数据引用到Sheet1中的下拉框
            Workbook workbook = writer.getWorkbook();
            Name namedCell = workbook.createName();
            namedCell.setNameName(dictSheet);
            //加载数据,将名称为hidden的
            DVConstraint constraint = DVConstraint.createFormulaListConstraint(dictSheet);
            for (int j=0; j<cols.size(); j++){
                writer.writeCellValue(i, j, cols.get(j));
            }

            if (CollectionUtils.isEmpty(cols)){
                continue;
            }
            namedCell.setRefersToFormula(dictSheet + "!$" + types[i] + "$1:$" + types[i] + "$" + cols.size());
            // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
            HSSFDataValidation validation = new HSSFDataValidation(new CellRangeAddressList(1, 1000, firstCol[i], firstCol[i]), constraint);
            writer.getSheets().get(0).addValidationData(validation);

            workbook.setSheetHidden(i+1, true);
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        try (OutputStream outputStream = response.getOutputStream()){
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("资产登记模板.xls", "UTF-8"));
//            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
            return ResultInfo.success(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输入标题到excel
     * @param writer excel对象
     * @param column 当前列位置
     * @param cellValue 标题内容
     * @param requiredFlag 是否标红
     */
    private void writeCell(ExcelWriter writer, int column, String cellValue, Boolean requiredFlag){
        // 根据x,y轴设置单元格内容
        writer.writeCellValue(column , 0, cellValue);
        Font font = writer.createFont();
        font.setBold(true);
        if (requiredFlag){
            // 根据x,y轴获取当前单元格样式
            CellStyle cellStyle = writer.createCellStyle(column, 0);
            // 内容水平居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            // 内容垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 设置边框
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            // 设置高度
            writer.setColumnWidth(column, 15);
            // 字体颜色标红
            font.setColor(Font.COLOR_RED);
            cellStyle.setFont(font);
            // 填充前景色
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else {
            // 根据x,y轴获取当前单元格样式
            CellStyle cellStyle = writer.createCellStyle(column, 0);
            // 内容水平居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            // 内容垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 设置边框
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            // 设置高度
            writer.setColumnWidth(column, 15);
            // 填充前景色
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cellStyle.setFont(font);
        }
    }

    /**
     * 设置下拉选项
     */
    private DataValidation setSelectCol(StyleSet styleSet, Sheet sheet, String[] capacityAvi, int firstRow, int firstCol) {

        CellStyle cellStyle = styleSet.getCellStyle();
        //规定格式
        cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("text"));

        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 设置辅修下拉框数据
//        String[] capacityAvi = {"是", "否"};
        DataValidationConstraint capacityConstraint = helper.createExplicitListConstraint(capacityAvi);
        //需要被设置为下拉数据的单元格范围
        CellRangeAddressList capacityList = new CellRangeAddressList(firstRow, 5000, firstCol, firstCol);
        return helper.createValidation(capacityConstraint, capacityList);
    }

    /**
     * 资产信息导入
     *
     * @return
     */
    public ResultInfo toAssetImport(Integer fileId) {
        FileManage fileManage = fileManageMapper.selectById(fileId);
        String path = uploadPath + File.separator + fileManage.getFile_name();
        File file = new File(path);
        try {
            ExcelReader reader = ExcelUtil.getReader(file);
            List<Map<String, Object>> readAll = reader.readAll();
            if (CollectionUtils.isEmpty(readAll)) {
                throw new RuntimeException("导入的数据为空");
            }
            List<AssetsManage> list = new ArrayList<>();
            // 设置errorList，加入校验失败的行号和原因
            List<Map<String, Object>> errorList = new ArrayList<>();
            for (Map<String, Object> map : readAll) {
                // map的所有value 去除前后空格
                map.forEach((k, v) -> map.put(k, null == v ? "" :v.toString().trim()));
                // 行号
                map.put("rowNum", readAll.indexOf(map) + 2);
                // 错误集合
                List<String> errList = new ArrayList<>();
                //资产分类	资产名称	状态	数量	预算单价	计量单位	实际金额	品牌	规格型号	购置日期	所属管理员	使用人	英文名称	供应商名称	厂牌号码	部门	保修期	保修截至	备注
                String assetType = null == map.get("资产分类") ? "" : map.get("资产分类").toString();
                Integer asset_type_id = null;

                if (StringUtils.isBlank(assetType)) {
                    map.put("资产分类tip", "资产分类不能为空 ");
                    errList.add("资产分类不能为空 ");
//                    throw new RuntimeException("资产分类不能为空");
                } else{
                    List<AssetsType> assetsTypeList = assetsTypeMapper.selectList(Wrappers.<AssetsType>lambdaQuery()
                            .eq(AssetsType::getIs_del, "0")
                            .eq(AssetsType::getAssets_type_name, assetType));
                    if (CollectionUtils.isEmpty(assetsTypeList)) {
                        map.put("资产分类tip", "资产分类匹配失败: " + assetType);
                        errList.add("资产分类匹配失败: " + assetType);
//                    throw new RuntimeException("资产分类不存在");
                    }else {
                        asset_type_id = assetsTypeList.get(0).getId();
                    }
                }


                String asset_name = null == map.get("资产名称") ? "" : map.get("资产名称").toString();
                if (StringUtils.isBlank(asset_name)) {
//                    throw new RuntimeException("资产名称不能为空");
                    errList.add("资产名称不能为空 ");
                    map.put("资产名称tip", "资产名称不能为空 ");
                }

                String status = null == map.get("状态") ? "" : map.get("状态").toString();
                Integer asset_state = null;
                if (StringUtils.isBlank(status)) {
//                    throw new RuntimeException("状态不能为空");
                    errList.add("状态不能为空 ");
                    map.put("状态tip", "状态不能为空 ");
                } else{
                    asset_state = AssetsStatusEnums.getType(status);
                    if (asset_state == null) {
                        map.put("状态tip", "状态匹配失败: " + status);
                        errList.add("状态匹配失败: " + status);
                    }
                }

                String num = null == map.get("数量") ? "" : map.get("数量").toString();
                Integer asset_num = null;
                if (StringUtils.isBlank(num) || !NumberUtil.isNumber(num)) {
//                    throw new RuntimeException("数量不能为空");
                    errList.add("数量不能为空 ");
                    map.put("数量tip", "数量不能为空 ");
                } else{
                    asset_num = NumberUtil.parseInt(num);
                }

                String budgetPrice = null == map.get("预算单价") ? "" : map.get("预算单价").toString();
//                if (StringUtils.isNotBlank(budgetPrice) && !NumberUtil.isNumber(budgetPrice)) {
////                    throw new RuntimeException("预算单价不能为空");
//                    errList.add("预算单价不能为空 ");
//                    map.put("预算单价tip", "预算单价不能为空 ");
//                }

                String unit = null == map.get("计量单位") ? "" : map.get("计量单位").toString();
                String unit_id = "";
                if (StringUtils.isBlank(unit)) {
//                    throw new RuntimeException("计量单位不能为空");
                    errList.add("计量单位不能为空 ");
                    map.put("计量单位tip", "计量单位不能为空 ");
                } else{
                    List<SysDict> units_list = sysDictMapper.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getIs_del, "0")
                            .eq(SysDict::getType, "unit").eq(SysDict::getLabel, unit));
                    if (CollectionUtils.isEmpty(units_list)) {
//                    throw new RuntimeException("计量单位不存在");
                        errList.add("计量单位匹配失败: " + unit);
                        map.put("计量单位tip", "计量单位匹配失败: " + unit);
                    } else {
                        unit_id = units_list.get(0).getValue();
                    }
                }


                String realPrice = null == map.get("实际金额") ? "" : map.get("实际金额").toString();
                if (StringUtils.isBlank(realPrice) || !NumberUtil.isNumber(realPrice)) {
//                    throw new RuntimeException("实际金额不能为空");
                    errList.add("实际金额不能为空 ");
                    map.put("实际金额tip", "实际金额不能为空 ");
                }
//                double real_price = NumberUtil.parseDouble(realPrice);

                String brand = null == map.get("品牌") ? "" : map.get("品牌").toString();
                Integer brand_id = null;
                if (StringUtils.isNotBlank(brand)) {
                    List<BrandManage> brandManages = brandManageMapper.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, "0")
                            .eq(BrandManage::getBrand_name, brand));
                    if (!CollectionUtils.isEmpty(brandManages)) {
                        brand_id = brandManages.get(0).getId();
                    }
                }

                String specification = null == map.get("规格型号") ? "" : map.get("规格型号").toString();
                if (StringUtils.isBlank(specification)) {
//                    throw new RuntimeException("规格型号不能为空");
                    errList.add("规格型号不能为空 ");
                    map.put("规格型号tip", "规格型号不能为空 ");
                }

                String buyDate = null == map.get("购置日期") ? "" : map.get("购置日期").toString();
                Date buy_date = null;
                try {
                    buy_date = DateUtil.parse(buyDate, "yyyy-MM-dd");
                } catch (Exception e) {
                    buy_date = new Date();
                }
                map.put("购置日期", DateUtil.format(buy_date, "yyyy-MM-dd"));

                String admin = null == map.get("所属管理员") ? "" : map.get("所属管理员").toString();
                Integer admin_user = null;
                if (StringUtils.isBlank(admin)) {
//                    throw new RuntimeException("所属管理员不能为空");
                    errList.add("所属管理员不能为空 ");
                    map.put("所属管理员tip", "所属管理员不能为空 ");
                } else {
                    admin = admin.trim();
                    List<SysUser> admin_list = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                            .eq(SysUser::getIs_del, "0")
                            .eq(SysUser::getUser_name, admin));
                    if (CollectionUtils.isEmpty(admin_list)) {
//                    throw new RuntimeException("所属管理员不存在:" + admin);
                        errList.add("所属管理员匹配失败: " + admin);
                        map.put("所属管理员tip", "所属管理员匹配失败: " + admin);
                    } else {
                        admin_user = admin_list.get(0).getId();
                    }
                }


                String user = null == map.get("使用人") ? "" : map.get("使用人").toString();
                Integer use_people = null;
                if (StringUtils.isNotBlank(user)) {
                    List<SysUser> user_list = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                            .eq(SysUser::getIs_del, "0")
                            .eq(SysUser::getUser_name, user));
                    if (!CollectionUtils.isEmpty(user_list)) {
                        use_people = user_list.get(0).getId();
                    }
                }

                String englishName = null == map.get("英文名称") ? "" : map.get("英文名称").toString();
                String supplier = null == map.get("供应商名称") ? "" : map.get("供应商名称").toString();
                Integer supplier_id = null;
                if (StringUtils.isNotBlank(supplier)) {
                    List<Supplier> suppliers = supplierMapper.selectList(Wrappers.<Supplier>lambdaQuery().eq(Supplier::getIs_del, "0")
                            .eq(Supplier::getSupplier_name, supplier));
                    if (!CollectionUtils.isEmpty(suppliers)) {
                        supplier_id = suppliers.get(0).getId();
                    }
                }

                String factoryNumber = null == map.get("厂牌号码") ? "" : map.get("厂牌号码").toString();
                String dept = null == map.get("部门") ? "" : map.get("部门").toString();
                Integer dept_id = null;
                Integer position_id = null;
                if (StringUtils.isBlank(dept)) {
//                    throw new RuntimeException("部门不能为空");
                    errList.add("部门不能为空 ");
                    map.put("部门tip", "部门不能为空 ");
                } else {
                    dept = dept.trim();
                    List<SysDepartment> dept_list = sysDepartmentMapper.selectList(Wrappers.<SysDepartment>lambdaQuery()
                            .eq(SysDepartment::getIs_del, "0")
                            .eq(SysDepartment::getDept_name, dept));

                    if (CollectionUtils.isEmpty(dept_list)) {
                        dept_list = sysDepartmentMapper.selectList(Wrappers.<SysDepartment>lambdaQuery()
                                .eq(SysDepartment::getIs_del, "0")
                                .eq(SysDepartment::getDept_name, "其他"));
                        errList.add("部门匹配失败: " + dept);
                        map.put("部门tip", "部门匹配失败: " + dept);
//                    return ResultInfo.error("部门不存在");
                    }else{
                        SysDepartment department = dept_list.get(0);
                        if (null != department){
                            dept_id = department.getId();
                            position_id = null == department.getDept_position()?0 : department.getDept_position();
                        }
                    }
                }



                String warranty = null == map.get("保修期") ? "" : map.get("保修期").toString();
                if (StringUtils.isNotBlank(warranty) && !NumberUtil.isNumber(warranty)) {
//                    throw new RuntimeException("保修期只能是数字");
                    errList.add("保修期只能是数字 ");
                    map.put("保修期tip", "保修期只能是数字 ");
                }
                Integer guarantee = NumberUtil.parseInt(warranty);
                String warrantyEnd = null == map.get("保修截止") ? "" : map.get("保修截止").toString();
                Date warranty_end = null;
                try {
                    warranty_end = DateUtil.parse(warrantyEnd, "yyyy-MM-dd");
                } catch (Exception e) {
                    warranty_end = new Date();
                }
                // 判断String 是否是日期格式
                String remark = null == map.get("当前位置") ? "" : map.get("当前位置").toString();

                // 判断String 是否是日期格式
                String remark2 = null == map.get("备注") ? "" : map.get("备注").toString();
                // 新增资产方法

                if (CollectionUtils.isEmpty(errList)) {
                    list.add(AssetsManage.builder()
                            .asset_name(asset_name).asset_type_id(asset_type_id).asset_state(asset_state)
                            .quantity(asset_num).unit_price(budgetPrice).unit(unit_id.toString())
                            .purchase_amount(realPrice).brand_id(brand_id)
                            .specification(specification).admin_user(admin_user).use_people(use_people)
                            .esname(englishName).supplier_id(supplier_id).brand_number(factoryNumber)
                            .dept_id(dept_id).position_id(position_id).guarantee(guarantee)
                            .purchase_time(buy_date).guarantee_end_time(warranty_end)
                            .remarks(remark).remarks2(remark2)
                            //设置默认字段
                            .is_depreciation(2).register_user(sysUserService.getUser().getId())
                            .register_time(new Date()).register_type(0)
                            .build());
                }else{
                    map.put("error", errList);
                    errorList.add(map);
                }
            }
            if (!CollectionUtils.isEmpty(errorList)){
                return ResultInfo.builder().code(500).msg("导入失败").data(errorList).build();
            }
            for (AssetsManage assetsManage : list) {
                addAssetsManage(assetsManage);
            }

        } catch (Exception e) {

            return ResultInfo.error("导入失败: " + e.getMessage());
//            throw new RuntimeException(e);
        }
        return ResultInfo.success();
    }

    /**
     * 资产维护列表查询
     * */
    public PageInfo<AssetsManage> toWHList(Integer pageIndex, Integer pageSize, AssetsManage beanParam, SysUser currentUser) {
        // 增加数据权限
        Integer post_id = currentUser.getPost();
        SysPost post = sysPostMapper.selectById(post_id);
        if (null == post) {
            return new PageInfo<>();
        }
        Integer permission = post.getData_permission();
        List<Integer> userIds = new ArrayList<>();
        // 0-个人 1-本部门 2-全部
        if (0 == permission) {
            userIds.add(currentUser.getId());
        } else if (1 == permission) {
            sysUserService.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDepartment, currentUser.getDepartment())).forEach(user -> {
                userIds.add(user.getId());
            });
        }

        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        // 支持按资产编号、品类、资产状态（闲置、使用、领用、借用、调拨、故障、处置）、品牌、存放部门、资产名称
        LambdaQueryWrapper<AssetsManage> wrappers = Wrappers.<AssetsManage>lambdaQuery();
        // 资产编号
        wrappers.like(StringUtils.isNotBlank(beanParam.getAsset_code()), AssetsManage::getAsset_code, beanParam.getAsset_code());
        // 品类
        wrappers.eq(null != beanParam.getAsset_type_id(), AssetsManage::getAsset_type_id, beanParam.getAsset_type_id());
        // 资产状态
        wrappers.eq(null != beanParam.getAsset_state(), AssetsManage::getAsset_state, beanParam.getAsset_state());
        // 品牌
        wrappers.eq(null != beanParam.getBrand_id(), AssetsManage::getBrand_id, beanParam.getBrand_id());
        // 存放部门
        wrappers.eq(null != beanParam.getDept_id(), AssetsManage::getDept_id, beanParam.getDept_id());
        // 资产名称
        wrappers.like(StringUtils.isNotBlank(beanParam.getAsset_name()), AssetsManage::getAsset_name, beanParam.getAsset_name());
        // 规格型号
        wrappers.like(StringUtils.isNotBlank(beanParam.getSpecification()), AssetsManage::getSpecification, beanParam.getSpecification());
        // 打印状态
        if (null != beanParam.getIs_print()) {
            if (1 == beanParam.getIs_print()){
                wrappers.eq(AssetsManage::getIs_print, beanParam.getIs_print());
            }else{
                // 等于null 或者 0
                wrappers.and(wp -> wp.isNull(AssetsManage::getIs_print).or().eq(AssetsManage::getIs_print, 0));
            }
        }
        // 判断库内库外
        wrappers.eq(null != beanParam.getRegister_type(), AssetsManage::getRegister_type, beanParam.getRegister_type());
        // 当前位置
        wrappers.like(StringUtils.isNotBlank(beanParam.getRemarks()), AssetsManage::getRemarks, beanParam.getRemarks());
        // 使用人
        wrappers.eq(null != beanParam.getUse_people(), AssetsManage::getUse_people, beanParam.getUse_people());
        // 如果 permission 不等于 2, 则增加数据权限, 符合条件的用户id,匹配使用人或者管理员
        if (2 != permission) {
            wrappers.and(wp -> wp.in(AssetsManage::getUse_people, userIds).or().in(AssetsManage::getAdmin_user, userIds));
        }

        List<AssetsManage> assetsManageVos = assetsManageMapper.selectList(wrappers.eq(AssetsManage::getIs_del, G.ISDEL_NO));
        // 用户map
        Map<Integer, SysUser> userMap = commonDataTools.getUserMap();
        // 部门map
        Map<Integer, SysDepartment> deptMap = commonDataTools.getDeptMap();
        // 资产类型map
        Map<Integer, AssetsType> assetsTypeMap = commonDataTools.getAssetsTypeMap();
        // 品牌map
        Map<Integer, SysDict> brandMap = commonDataTools.getSysDictMap("brand_type");
        // 折旧方法map
        Map<Integer, SysDict> depreciationMap = commonDataTools.getSysDictMap("depreciation_type");
        // 供应商map
        Map<Integer, Supplier> supplierMap = commonDataTools.getSupplierMap();
        // 位置map
        Map<Integer, SysPosition> positionMap = commonDataTools.getPositionMap();
        for (AssetsManage bean : assetsManageVos) {
            bean.setUse_people_name(commonDataTools.getValue(userMap, bean.getUse_people(), "user_name"));
            bean.setAdmin_user_name(commonDataTools.getValue(userMap, bean.getAdmin_user(), "user_name"));
            bean.setCreate_user_name(commonDataTools.getValue(userMap, bean.getCreate_user(), "user_name"));
            bean.setRegister_user_name(commonDataTools.getValue(userMap, bean.getRegister_user(), "user_name"));
            bean.setDept_name(commonDataTools.getValue(deptMap, bean.getDept_id(), "dept_name"));
            bean.setAsset_type_name(commonDataTools.getValue(assetsTypeMap, bean.getAsset_type_id(), "assets_type_name"));
            // 品牌
            BrandManage brand = brandManageMapper.selectById(bean.getBrand_id());
            bean.setBrand_name(null != brand ? brand.getBrand_name() : "");
            String depreciation_name = bean.getDepreciation_method();
            if (null != bean.getDepreciation_method() && StringUtils.isNotBlank(bean.getDepreciation_method())) {
                bean.setDepreciation_method(!isNumeric(bean.getDepreciation_method()) ? "" : commonDataTools.getValue(depreciationMap, Integer.valueOf(bean.getDepreciation_method()), "label"));
            }
            bean.setSupplier_name(commonDataTools.getValue(supplierMap, bean.getSupplier_id(), "supplier_name"));
            bean.setPlace_name(commonDataTools.getValue(positionMap, bean.getPosition_id(), "position_name"));
            bean.setAsset_epc(bean.getRfid());
            bean.setQr_code(genratorQrCode(bean.getAsset_code()));

            // 折旧日期
            String nowDate = DateUtil.formatDate(new Date());
            if (StringUtils.isNotBlank(beanParam.getDepreciation_time_str())) {
                nowDate = beanParam.getDepreciation_time_str();
            }
            bean.setDepreciation_time_str(nowDate);


        }
        return new PageInfo<>(assetsManageVos);
    }

    public ResultInfo batchDeleteAssetsManage(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return ResultInfo.error("请选择要删除的资产");
        }
        for (Integer id : ids) {
            AssetsManage assetsManage = assetsManageMapper.selectById(id);
            if (null == assetsManage) {
                return ResultInfo.error("资产不存在");
            }
            assetsManage.setIs_del(G.ISDEL_YES);
            assetsManageMapper.updateById(assetsManage);
        }
        return ResultInfo.success();
    }
}
