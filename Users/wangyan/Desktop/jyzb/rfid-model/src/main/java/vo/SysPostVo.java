package vo;

import com.jsdc.rfid.model.SysPost;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysPostVo extends SysPost {
    private String dept_name;
}
