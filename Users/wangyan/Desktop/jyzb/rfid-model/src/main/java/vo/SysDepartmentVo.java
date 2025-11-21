package vo;

import com.jsdc.rfid.model.SysDepartment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysDepartmentVo extends SysDepartment {
    private String parent_name;
    private String position_name;
}
