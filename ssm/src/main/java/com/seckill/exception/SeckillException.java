package com.seckill.exception;

/**
 * Created by Kohler on 2017/3/30.
 */
public class SeckillException extends RuntimeException {
    public SeckillException() {
    }

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillException(Throwable cause) {
        super(cause);
    }

    public SeckillException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
