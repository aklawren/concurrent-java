package com.alissa;

import java.util.concurrent.TimeUnit;

/**
 * Created by m308707 on 12/7/16.
 */
public class Requester {

    public double asyncTask1(){
        try{
            TimeUnit.SECONDS.sleep(2);
            System.out.println("asyncTask1 finished.");
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        return new Double(1);

    }

    public double asyncTask2(){
        try{
            TimeUnit.SECONDS.sleep(5);
            System.out.println("asyncTask2 finished.");
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        return new Double(2);
    }

    public double asyncTask3(){
        try{
            TimeUnit.SECONDS.sleep(3);
            System.out.println("asyncTask3 finished.");
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        return new Double(3);
    }
}
