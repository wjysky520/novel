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
public class FIleBO {

    private File file;

    private byte[] bytes;

    private String base64;

    private String filename;

    public FIleBO(File file, String filename) {
        this.file = file;
        this.filename = filename;
    }

    public FIleBO(byte[] bytes, String filename) {
        this.bytes = bytes;
        this.filename = filename;
    }

    public FIleBO(String base64, String filename) {
        this.base64 = base64;
        this.filename = filename;
    }
}