package vo;

import com.jsdc.rfid.model.AssetsManage;
import com.jsdc.rfid.model.InventoryDetail;
import com.jsdc.rfid.model.InventoryJob;
import lombok.Data;

import java.util.List;

/**
 * ClassName: InventoryJobVo
 * Description:
 * date: 2022/4/25 17:40
 *
 * @author bn
 */
@Data
public class InventoryJobVo extends InventoryJob {


    private InventoryJob inventoryJob;
    //盘点任务id
    private Integer inventory_job_id;
    //盘点状态 1.应盘、2.未盘、3.正常、4.盘盈、5.盘亏、6.异常
    private String inventory_status;
    /**
     *  资产品类
     */
    private Integer asset_type_id;

    /**
     *  存放部门
     */
    private Integer dept_id;
    /**
     *  使用人
     */
    private Integer use_people;

    // 资产当前位置
    private String location;

    private AssetsManage assetsManage;

    private List<InventoryDetail> inventoryDetails;
}
