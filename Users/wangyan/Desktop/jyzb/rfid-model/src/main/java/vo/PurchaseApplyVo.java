package vo;

import com.jsdc.rfid.model.PurchaseApply;
import com.jsdc.rfid.model.PurchaseDetail;
import com.jsdc.rfid.model.PurchaseLog;
import lombok.Data;

import java.util.List;

/**
 * ClassName: PurchaseApplyVo
 * Description:
 * date: 2022/4/25 9:48
 *
 * @author bn
 */
@Data
public class PurchaseApplyVo extends PurchaseApply {

    private String put_order;

    private PurchaseLog purchaseLog;

    private List<PurchaseLog> purchaseLogs;

    private PurchaseApply purchaseApply;

    private List<PurchaseDetail> purchaseDetails;

    private List<Integer> ids ;

    /**
     * 微信传关键字
     */
    private String transfer;

    private String jjyy;

    // 0-申请页面,1-审批页面
    private Integer is_adopt;


    /**
     * 资产采购top10
     * 订单数量
     */
    private Integer count;

    /**
     * 采购商名称
     */
    private String companyName;
    /**
     * 总额
     */
    private String money;

    /**
     * 同比年份月份
     */
    private String month;

    private String applyTimeStr;
}
