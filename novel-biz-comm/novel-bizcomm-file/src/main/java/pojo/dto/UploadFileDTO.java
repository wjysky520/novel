package pojo.dto;

import com.wjysky.pojo.dto.BaseDTO;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName : uploadFileDTO
 * @Description : TODO
 * @Author : 王俊元(wjysky520@gmail.com)
 * @Date : 2023-06-09 16:27:24
 */
@Data
public class UploadFileDTO extends BaseDTO {

    @NotNull(message = "序号不能为空")
    @Min(1)
    private Integer sn; // 序号

    @NotNull(message = "Base64内容不能为空")
    private String base64; // 文件base64内容

    private String filename; // 文件名称
}