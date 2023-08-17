package pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : UploadFileVO
 * @Description : TODO
 * @Author : 王俊元(wjysky520@gmail.com)
 * @Date : 2023-06-09 16:46:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileVO {

    private Integer sn; // 序号

    private String url; // 文件下载路径
}