package pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @ClassName : FIleBO
 * @Description : TODO
 * @Author : 王俊元(wjysky520@gmail.com)
 * @Date : 2023-06-09 15:14:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileBO {

    private Integer sn; // 序号，用于确定上传文件的下载路径

    private File file;

    private byte[] bytes;

    private String base64;

    private String filename;

    public FileBO(Integer sn, File file, String filename) {
        this.sn = sn;
        this.file = file;
        this.filename = filename;
    }

    public FileBO(Integer sn, byte[] bytes, String filename) {
        this.sn = sn;
        this.bytes = bytes;
        this.filename = filename;
    }

    public FileBO(Integer sn, String base64, String filename) {
        this.sn = sn;
        this.base64 = base64;
        this.filename = filename;
    }
}