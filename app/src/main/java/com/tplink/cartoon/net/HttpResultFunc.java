/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-21, xufeng, Create file
 */
package com.tplink.cartoon.net;

import com.tplink.cartoon.Exception.ApiException;
import com.tplink.cartoon.data.bean.HttpResult;

import io.reactivex.functions.Function;

public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {

    @Override
    public T apply(HttpResult<T> httpResult) throws Exception {
        if (httpResult.getStatus() != 2) {
            throw new ApiException(httpResult.getStatus());
        }
        return httpResult.getData();
    }
}
