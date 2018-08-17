package com.tplink.cartoon.utils;
/*
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-9, xufeng, Create file
 */

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ShowErrorTextUtil {

    private static boolean isShowReason = true;

    public static String ShowErrorText(Throwable throwable) {
        String error = "未知错误" + throwable.toString();
        if (throwable instanceof ConnectException) {
            error = "无法访问服务器接口";
        } else if (throwable instanceof UnknownHostException) {
            error = "未知的域名，请检查网络是否连接";
        } else if(throwable instanceof SocketTimeoutException){
            error = "连接超时，接口炸了，程序员拿刀在去修接口的路上";
        }
        if (isShowReason) {
            error = error + throwable.toString();
        }
        return error;
    }
}
