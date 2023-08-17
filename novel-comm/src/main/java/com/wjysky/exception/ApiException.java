package com.wjysky.exception;

import com.wjysky.enums.ErrorEnum;
import lombok.Getter;

/**
 * @author : 王俊元(wjysky520@gmail.com)
 * @ClassName : ApiException
 * @Description : api指定异常
 * @Date : 2023-08-14 10:26:43
 */
@Getter
public class ApiException extends RuntimeException {

    private final int errCode;

    public ApiException(ErrorEnum errorEnum) {
        this(errorEnum.getCode(), errorEnum.getMsg());
    }

    public ApiException(int errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    public ApiException(String errMsg) {
        this(ErrorEnum.ERROR.getCode(), errMsg);
    }

    public ApiException(int errCode, String errMsg, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errMsg, cause, enableSuppression, writableStackTrace);
        this.errCode = errCode;
    }

}