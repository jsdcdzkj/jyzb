package vo;

import com.jsdc.rfid.model.ConsPurchaseApply;
import com.jsdc.rfid.model.ConsPurchaseDetail;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: PurchaseApplyVo
 * Description:
 * date: 2022/4/25 9:48
 *
 * @author bn
 */
@Data
public class ConsPurchaseApplyVo extends ConsPurchaseApply {

    private Integer userId;

    private String put_order;

    private String query;

    private ConsPurchaseApply purchaseApply;

    private List<ConsPurchaseDetail> purchaseDetails;

    private List<Integer> ids = new ArrayList<>();

    // 0-申请页面,1-审批页面
    private Integer is_adopt;

    private Integer amount;

    private Integer category_id;
}
