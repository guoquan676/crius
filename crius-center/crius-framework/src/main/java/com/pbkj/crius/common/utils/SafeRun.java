package com.pbkj.crius.common.utils;

import com.github.phantomthief.util.ThrowableBiFunction;
import com.github.phantomthief.util.ThrowableFunction;
import com.github.phantomthief.util.ThrowableRunnable;
import com.github.phantomthief.util.ThrowableSupplier;
import com.pbkj.crius.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.function.Supplier;

public class SafeRun {

    private static Logger logger = LoggerFactory.getLogger(SafeRun.class);

    public static <T, X extends Throwable> T safeSupply(ThrowableSupplier<? extends T, X> supplier, Supplier<? extends T> onThrowable) {
        try {
            return supplier.get();
        } catch (Throwable t) {
            logger.warn("[fail safe] safeSupply error!!!!!!!,", t);
            return onThrowable.get();
        }
    }

    @Nullable
    public static <T, R, X extends Throwable> R safeFunction(ThrowableFunction<T, R, X> function, T t) {
        try {
            return function.apply(t);
        } catch (Throwable th) {
            return null;
        }
    }


    public static void safeRun(ThrowableRunnable runnable, Runnable onThrowable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            logger.warn("[fail safe] safeRun error!!!!!!!,", t);
            onThrowable.run();
        }
    }

    @Nullable
    public static <T, R, X extends Throwable> R retryFuncition(ThrowableFunction<T, R, X> function, T t, int retryCount){
        if (retryCount <= 0) {
            retryCount = 1;
        }
        int firstCount = retryCount;
        while (retryCount > 0) {
            try {
                if (firstCount != retryCount) {
                    Thread.sleep(1000L);
                }
                return function.apply(t);
            } catch (Throwable th) {
                logger.error("retryFuncition error, count:{},", retryCount, th);
                if (retryCount == 1) {
                    return null;
                }
            } finally {
                retryCount -= 1;
            }
        }
        return null;
    }

    @Nullable
    public static <R, X extends Throwable> R retrySupplier(ThrowableSupplier<R, X> supplier, int retryCount, ServiceException se) {
        if (retryCount <= 0) {
            retryCount = 1;
        }
        int firstCount = retryCount;
        while (retryCount > 0) {
            try {
                if (firstCount != retryCount) {
                    Thread.sleep(1000L);
                }
                return supplier.get();
            } catch (Throwable th) {
                logger.error("retryFunction error, count:{},", retryCount, th);
                if (retryCount == 1) {
                    if (se == null) {
                        return null;
                    } else {
                        throw se;
                    }
                }
            } finally {
                retryCount -= 1;
            }
        }
        if (se == null) {
            return null;
        } else {
            throw se;
        }
    }
    public static <T, U, R, X extends Throwable> R retryBiFunction(ThrowableBiFunction<T, U, R, X> supplier,T t, U u, int retryCount, ServiceException se) {
        if (retryCount <= 0) {
            retryCount = 1;
        }
        int firstCount = retryCount;
        while (retryCount > 0) {
            try {
                if (firstCount != retryCount) {
                    Thread.sleep(1000L);
                }
                return supplier.apply(t, u);
            } catch (Throwable th) {
                logger.error("retryFunction error, count:{},", retryCount, th);
                if (retryCount == 1) {
                    if (se == null) {
                        return null;
                    } else {
                        throw se;
                    }
                }
            } finally {
                retryCount -= 1;
            }
        }
        if (se == null) {
            return null;
        } else {
            throw se;
        }
    }
}
