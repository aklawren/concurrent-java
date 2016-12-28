package com.alissa;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by m308707 on 11/23/16.
 */
public class ThingDoer implements Callable<String>{
    private int i;

    public ThingDoer(int i) {
        this.i = i;
    }

    @Override
    public String call() throws Exception {
        int timeout = ThreadLocalRandom.current().nextInt(1, 20);
        TimeUnit.SECONDS.sleep(timeout);
        return String.format("Timeout is %d for Thread %d", timeout, i);
    }
}
