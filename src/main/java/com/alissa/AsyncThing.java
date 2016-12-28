package com.alissa;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by m308707 on 11/25/16.
 */
public class AsyncThing implements Supplier<String>{

    private int i;

    public AsyncThing(int i){
        this.i = i;
    }

    public String get(){
        String ret = "";

          try {
              int timeout = ThreadLocalRandom.current().nextInt(1, 5);
              TimeUnit.SECONDS.sleep(timeout);
              System.out.println("finished sleeping.");
              ret = String.format("Timeout is %d for Thread %d", timeout, i);
          }
          catch(Exception e){
              e.printStackTrace();
          }
          return ret;
    }
}
