package com.cinema.common.base;

import com.cinema.common.exception.ErrorCode;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@ToString
@Data
public class ServiceResult<T> implements Serializable {
    private static final long serialVersionUID = 1428484501441111860L;
    private boolean success;
    private String code;
    private String message;
    private T data;

    public boolean isSuccess() {
        return "0".equals(code);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(String resultCode, String message) {
        this.success = false;
        this.code = resultCode;
        this.message = message;
    }

    public static <T> ServiceResult<T> ok(T data) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setData(data);
        result.setMessage(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        result.setSuccess(true);
        return result;
    }

    public static <T> ServiceResult<T> ok() {
        return ServiceResult.ok(null);
    }

    public static <T> ServiceResult<T> fail(String resultCode, String error) {
        //log.debug(error);
        ServiceResult<T> resp = new ServiceResult<>();
        resp.setMessage(resultCode, error);
        return resp;
    }

    public static <T> ServiceResult<T> fail(ErrorCode errorCode) {
        return fail(errorCode.getCode(), errorCode.getMessage());
    }


    public static <T> ServiceResult<T> fail(T data, String resultCode, String error) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setData(data);
        result.setMessage(resultCode, error);
        return result;
    }

    public static <T> ServiceResult<T> fail(T data,ErrorCode resultCode) {
        ServiceResult<T> result = new ServiceResult<>();

        result.setMessage(resultCode.getCode(), resultCode.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> ServiceResult<T> fails(String code, String message) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setSuccess(false);
        result.setMessage(message);
        result.setCode(code);
        return result;
    }

}
