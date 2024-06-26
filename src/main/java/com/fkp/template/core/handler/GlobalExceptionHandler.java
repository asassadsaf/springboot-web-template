package com.fkp.template.core.handler;

import com.alibaba.fastjson2.JSONObject;
import com.fkp.template.core.constant.CommonConstant;
import com.fkp.template.modules.xkip.controller.TestController;
import com.fkp.template.modules.xkip.dto.request.RequestMetadata;
import com.fkp.template.modules.xkip.dto.response.ErrorResponse;
import com.fkp.template.core.exception.BusinessException;
import com.fkp.template.core.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @author fengkunpeng
 * @version 1.0
 * @description 全局异常处理器，用于将业务异常封装为响应体
 * @date 2024/5/13 16:50
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ErrorResponse businessException(BusinessException exception, HttpServletResponse response, HttpServletRequest request, HandlerMethod handler){
        Integer statusCode = exception.getStatusCode();
        String errorCode = exception.getErrorCode();
        String message = exception.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        String requestId = errorResponse.getRequestId();
        log.error("GlobalExceptionHandler: " +
                "\n-- ExceptionType:{} " +
                "\n-- RequestId: {} " +
                "\n-- StatusCode: {} " +
                "\n-- ErrorCode:{} " +
                "\n-- ErrorMessage:{}",
                exception.getClass().getName(), requestId, statusCode, errorCode, message, exception);
        response.setStatus(statusCode);
        printOperationLog(handler.getBeanType(), handler.getMethod().getName(), requestId, errorCode, message, request);
        return errorResponse;
    }

    //打印错误的操作日志
    private void printOperationLog(Class<?> clazz, String oper, String requestId, String errorCode, String errorMessage, HttpServletRequest request){
        if (clazz == TestController.class) {
            String kmsRequestId = StringUtils.EMPTY;
            String kmsInstanceId = StringUtils.EMPTY;
            String kmsOperation = StringUtils.EMPTY;
            String principalArn = StringUtils.EMPTY;
            String keyId = StringUtils.EMPTY;
            Object requestMetadataObj = request.getAttribute(CommonConstant.REQUEST_METADATA_NAME);
            if(requestMetadataObj != null){
                RequestMetadata requestMetadata = ((JSONObject) requestMetadataObj).toJavaObject(RequestMetadata.class);
                kmsRequestId = requestMetadata.getKmsRequestId();
                kmsInstanceId = requestMetadata.getKmsInstanceId();
                kmsOperation = requestMetadata.getKmsOperation();
                principalArn = requestMetadata.getPrincipalArn();
            }
            Object keyIdObj = request.getAttribute(CommonConstant.KEY_ID_NAME);
            if(keyIdObj != null){
                keyId = (String) keyIdObj;
            }
            LogUtils.printLog(false, oper, requestId, kmsRequestId, principalArn, kmsInstanceId, kmsOperation, keyId, errorCode, errorMessage);
        }
    }

//    /**
//     * 处理bean validation 异常
//     *
//     * @param e BindException及其子类对象
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
//    public BaseResponse<?> validException(BindException e){
//        StringBuilder sb = new StringBuilder();
//        try {
//            for (FieldError error : e.getFieldErrors()) {
//                log.error("GlobalExceptionHandler -- location:{}#{} -- message:{}",error.getObjectName(),error.getField(),error.getDefaultMessage());
//                String message = error.getDefaultMessage();
//                if (StringUtils.isNotBlank(message)) {
//                    sb.append(message).append(";");
//                }
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
//            return BaseResponse.fail(ErrorCodeEnum.GlobalInnerException.getCode(), ErrorCodeEnum.GlobalInnerException.getMsg() + ": " + ex.getMessage());
//        }
//        return BaseResponse.fail(ErrorCodeEnum.ValidException.getCode(),sb.toString());
//    }
//
//    /**
//     * 处理方法参数的校验，类上加@Validate,校验在方法参数上
//     *
//     * @param e 异常对象 ConstraintViolationException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(value = {ConstraintViolationException.class})
//    public ErrorResponse validException2(ConstraintViolationException e){
//
//        StringBuilder sb = new StringBuilder();
//        try {
//            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
//                log.error("GlobalExceptionHandler -- location:{}#{} -- message:{}", violation.getRootBeanClass(), violation.getPropertyPath(),violation.getMessage());
//                String message = violation.getMessage();
//                if (StringUtils.isNotBlank(message)) {
//                    sb.append(message).append(";");
//                }
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
//            return BaseResponse.fail(ErrorCodeEnum.GlobalInnerException.getCode(),ErrorCodeEnum.GlobalInnerException.getMsg() + ": " + ex.getMessage());
//        }
//        return BaseResponse.fail(ErrorCodeEnum.ValidException.getCode(),sb.toString());
//    }
//
//    /**
//     * 业务异常
//     * @param e 异常对象
//     * @return 返回统一错误信息
//     */
//    @ExceptionHandler(BusinessException.class)
//    public BaseResponse<?> businessError(BusinessException e){
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = e.getErrorCode();
//        if(StringUtils.isBlank(errorCode)){
//            errorCode = ErrorCodeEnum.BusinessException.getCode();
//        }
//        String errorMessage = e.getMessage();
//        if(StringUtils.isBlank(errorMessage)){
//            errorMessage = ErrorCodeEnum.BusinessException.getMsg();
//        }
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 连接异常
//     *
//     * @param e 异常对象ConnectException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(ConnectException.class)
//    public BaseResponse<?> connectionError(ConnectException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.NetworkError.getCode();
//        String errorMessage = ErrorCodeEnum.NetworkError.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 数据库连接异常
//     *
//     * @param e 异常对象 CommunicationsException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(CommunicationsException.class)
//    public BaseResponse<?> communicationsException(CommunicationsException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.DatabaseException.getCode();
//        String errorMessage = ErrorCodeEnum.DatabaseException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 无法使用 JTA 等底层事务 API 创建事务时引发异常
//     *
//     * @param e 异常对象 CannotCreateTransactionException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(CannotCreateTransactionException.class)
//    public BaseResponse<?> cannotCreateTransactionException(CannotCreateTransactionException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.CannotCreateTransactionException.getCode();
//        String errorMessage = ErrorCodeEnum.CannotCreateTransactionException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 遇到一般事务系统错误时抛出异常，例如提交或回滚
//     *
//     * @param e 异常对象 TransactionSystemException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(TransactionSystemException.class)
//    public BaseResponse<?> transactionSystemException(TransactionSystemException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.TransactionSystemException.getCode();
//        String errorMessage = ErrorCodeEnum.TransactionSystemException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 类型转换异常
//     *
//     * @param e 异常对象 ClassCastException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(ClassCastException.class)
//    public BaseResponse<?> classCastException(ClassCastException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.ClassCastException.getCode();
//        String errorMessage = ErrorCodeEnum.ClassCastException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 未知方法异常
//     *
//     * @param e 异常对象 NoSuchMethodException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(NoSuchMethodException.class)
//    public BaseResponse<?> noSuchMethodException(NoSuchMethodException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.NoSuchMethodException.getCode();
//        String errorMessage = ErrorCodeEnum.NoSuchMethodException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 数组越界异常
//     *
//     * @param e 异常对象 IndexOutOfBoundsException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(IndexOutOfBoundsException.class)
//    public BaseResponse<?> indexOutOfBoundsException(IndexOutOfBoundsException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.IndexOutOfBoundsException.getCode();
//        String errorMessage = ErrorCodeEnum.IndexOutOfBoundsException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 空指针异常
//     *
//     * @param e 异常对象 NullPointerException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(NullPointerException.class)
//    public BaseResponse<?> nullPointerExceptionHandler(NullPointerException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.NullPointerException.getCode();
//        String errorMessage = ErrorCodeEnum.NullPointerException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * IO异常
//     *
//     * @param e 异常对象 IOException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(IOException.class)
//    public BaseResponse<?> iOExceptionHandler(IOException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.IOException.getCode();
//        String errorMessage = ErrorCodeEnum.IOException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 400错误 参数格式错误
//     *
//     * @param e 异常对象 HttpMessageNotReadableException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler({HttpMessageNotReadableException.class})
//    public BaseResponse<?> requestNotReadable(HttpMessageNotReadableException e) {
//        log.error("ExceptionStackTrace:",e);
//        String returnMsg = StringUtils.EMPTY;
//        String message = e.getMessage();
//        if (StringUtils.isNotBlank(message)) {
//            returnMsg = message.substring(message.lastIndexOf(("field : ")));
//        }
//        String errorCode = ErrorCodeEnum.ParamsInvalid.getCode();
//        String errorMessage = returnMsg + StringUtils.SPACE + ErrorCodeEnum.ParamsInvalid.getMsg();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 400错误 参数缺失错误
//     *
//     * @param e 异常对象MissingServletRequestParameterException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler({MissingServletRequestParameterException.class})
//    public BaseResponse<?> requestMissingServletRequest(MissingServletRequestParameterException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.ParamMissing.getCode();
//        String errorMessage = ErrorCodeEnum.ParamMissing.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 400错误 尝试设置 bean 属性时在类型不匹配时引发异常
//     *
//     * @param e 异常对象 TypeMismatchException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler({TypeMismatchException.class})
//    public BaseResponse<?> typeMismatchException(TypeMismatchException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.TypeMismatchException.getCode();
//        String errorMessage = ErrorCodeEnum.TypeMismatchException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 404错误 找不到资源
//     *
//     * @param e 异常对象 NoHandlerFoundException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler({NoHandlerFoundException.class})
//    public BaseResponse<?> request404(NoHandlerFoundException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.NoHandlerFoundException.getCode();
//        String errorMessage = ErrorCodeEnum.NoHandlerFoundException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",NoHandlerFoundException.class.toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 405错误 不支持的请求方法
//     *
//     * @param e 异常对象 HttpRequestMethodNotSupportedException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
//    public BaseResponse<?> request405(HttpRequestMethodNotSupportedException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.HttpRequestMethodNotSupportedException.getCode();
//        String errorMessage = ErrorCodeEnum.HttpRequestMethodNotSupportedException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",HttpRequestMethodNotSupportedException.class.toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 415错误 请求的数据类型服务端不支持
//     * @param e 异常对象 HttpMediaTypeNotSupportedException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
//    public BaseResponse<?> request415(HttpMediaTypeNotSupportedException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.HttpMediaTypeNotSupportedException.getCode();
//        String errorMessage = ErrorCodeEnum.HttpMediaTypeNotSupportedException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",HttpRequestMethodNotSupportedException.class.toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 406错误 当请求处理程序无法生成客户端可接受的响应时引发异常
//     *
//     * @param e 异常对象 HttpMediaTypeNotAcceptableException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
//    public BaseResponse<?> request406(HttpMediaTypeNotAcceptableException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.HttpMediaTypeNotAcceptableException.getCode();
//        String errorMessage = ErrorCodeEnum.HttpMediaTypeNotAcceptableException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",HttpMediaTypeNotAcceptableException.class.toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 500错误 找不到适合 bean 属性的编辑器或转换器时抛出异常
//     *
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
//    public BaseResponse<?> server500(RuntimeException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.ConversionNotSupportedException.getCode();
//        String errorMessage = ErrorCodeEnum.ConversionNotSupportedException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",HttpMediaTypeNotAcceptableException.class.toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 运行时异常
//     *
//     * @param e 异常对象 RuntimeException
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(RuntimeException.class)
//    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.RuntimeException.getCode();
//        String errorMessage = ErrorCodeEnum.RuntimeException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }
//
//    /**
//     * 其他异常
//     *
//     * @param e 异常对象 Exception,Throwable
//     * @return 统一返回错误信息
//     */
//    @ExceptionHandler(value = {Exception.class, Throwable.class})
//    public BaseResponse<?> defaultErrorHandler(Throwable e) {
//        log.error("ExceptionStackTrace:",e);
//        String errorCode = ErrorCodeEnum.OtherException.getCode();
//        String errorMessage = ErrorCodeEnum.OtherException.getMsg() + ": " + e.getMessage();
//        log.error("GlobalExceptionHandler -- ExceptionType:{} -- ErrorCode:{} -- ErrorMessage:{}",e.getClass().toString(), errorCode, errorMessage);
//        return BaseResponse.fail(errorCode, errorMessage);
//    }

}
