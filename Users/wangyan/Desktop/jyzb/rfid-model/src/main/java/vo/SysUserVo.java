package vo;

import com.jsdc.rfid.model.SysUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysUserVo extends SysUser {
    private String dept_name;
    private String post_name;
}
