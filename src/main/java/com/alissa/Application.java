package com.alissa;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * Created by m308707 on 11/23/16.
 */
public class Application {
    public static void main(String[] args) throws InterruptedException, ExecutionException{

       Console console = System.console();

       if(console == null){
          System.err.println("No console");
          System.exit(1);
       }

        console.printf("Welcome! Type 'start' to do something and 'exit' to quit.\n");
        Scanner scanner = new Scanner(console.reader());

        while(scanner.hasNext()){
            String token = scanner.next();

            switch(token){
                case "start": doThings();
                      break;
                case "exit": System.out.println("See ya!\n");
                       System.exit(0);

            }
        }
    }


    public static void doThings() throws ExecutionException, InterruptedException{
        long startTime1 = System.currentTimeMillis();
        runSlowTasks(startTime1);
        runSlowTasks(startTime1);
        runSlowTasks(startTime1);
        long startTime2 = System.currentTimeMillis();
        runFutures(startTime2);
        runFutures(startTime2);
        runFutures(startTime2);
        long startTime3 = System.currentTimeMillis();
        runCompletableFutures(startTime3);
        runCompletableFutures(startTime3);
        runCompletableFutures(startTime3);

    }

    public static void printExecutionTime(String methodName, long startTime){
        long finishTime = System.currentTimeMillis();
        System.out.println(methodName + "execution finished in: " + (finishTime - startTime) + " milliseconds.");
    }

    public static void runSlowTasks(long startTime){
        String methodName = "slowTasks";
        System.out.println("Running " + methodName + "...");

        Requester requester = new Requester();
        double total = 0;

        try {
            double res1 = requester.asyncTask1();
            double res2 = requester.asyncTask2();
            double res3 = requester.asyncTask3();
            total = res1 + res2 + res3;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        sendResponse("{'Total': " + total + "}");
        printExecutionTime(methodName, startTime);
    }

    public static void sendResponse(String response){
       System.out.println("result is: " + response);
    }

    public static void runFutures(long startTime){
        String methodName = "futures";
        System.out.println("Running " + methodName + "...");

        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        List<Future<Double>> futures = new ArrayList<>();
        Requester requester = new Requester();
        double total = 0;

        try {
            Future<Double> future1 = threadPool.submit(requester::asyncTask1);
            Future<Double> future2 = threadPool.submit(requester::asyncTask2);
            Future<Double> future3 = threadPool.submit(requester::asyncTask3);
            futures.add(future1);
            futures.add(future2);
            futures.add(future3);

            threadPool.shutdown(); //this indicates that we're not adding any new tasks to the threadpool queue
            while(!futures.isEmpty()){
                for(int j = 0; j < futures.size(); j++){
                    Future<Double> f = futures.get(j);
                    if(f.isDone()){
                        double result = f.get();
                        total += result;
                        futures.remove(f);
                    }
                }
            }
            sendResponse("{'Total': " + total + "}");
            printExecutionTime(methodName, startTime);

            threadPool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS); //
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void runCompletableFutures(long startTime) throws ExecutionException, InterruptedException {
        String methodName = "nonBlockingCompletableFutures";
        System.out.println("Running " + methodName + "...");

        Requester requester = new Requester();

        CompletableFuture<Double> completableFuture1 = CompletableFuture.supplyAsync( requester :: asyncTask1 );
        CompletableFuture<Double> completableFuture2 = CompletableFuture.supplyAsync( requester :: asyncTask2 );
        CompletableFuture<Double> completableFuture3 = CompletableFuture.supplyAsync( requester :: asyncTask3 );

        List<CompletableFuture<Double>> futureList = new ArrayList<>();
       // CompletableFuture<Double>[] futureArray = futureList.toArray();

        futureList.add(completableFuture1);
        futureList.add(completableFuture2);
        futureList.add(completableFuture3);

        //Signals when all futures are complete
        CompletableFuture<Void> finalCompletableFuture = CompletableFuture.allOf(completableFuture1, completableFuture2, completableFuture3);

        //once we know all futures are complete, iterate through them all and get their final values
        finalCompletableFuture.thenApply(v -> {
            double total = futureList.stream()
                       .mapToDouble(value -> value.join())
                       .sum();
            sendResponse("{'Total': " + total + "}");
            printExecutionTime(methodName, startTime);
            return total;
        });
        printExecutionTime(methodName + " DONE ", startTime);
    }


    //A good example of how *not* to implement completable futures :)
    /*public static void runCompletableFutures(long startTime) throws ExecutionException, InterruptedException {
        CompletableFuture<Double> completableFuture1,completableFuture2,completableFuture3;
        Executor executor = Executors.newFixedThreadPool(4);
        Requester requester = new Requester();
        System.out.println("In runCompletableFutures");

        //Annonymous Function
        completableFuture1 = CompletableFuture.supplyAsync(new Supplier<Double>() {
            @Override
            public Double get() {
                return requester.asyncTask1();
            }
        });

        completableFuture2 = CompletableFuture.supplyAsync( requester :: asyncTask2 );
        completableFuture3 = CompletableFuture.supplyAsync( requester :: asyncTask3 );

        CompletableFuture<Double> total = completableFuture1.thenCombine(completableFuture2, (d1, d2) -> {
            return d1+d2;
        });

        total = total.thenCombine(completableFuture3, (d1, d2) -> {
            return d1+d2;
        });

        Double val = total.get();
        sendResponse("{'Total': " + val + "}");
        printExecutionTime(methodName, startTime);
    }
    */

    public static void printThis(String response){
        System.out.println("result is: " + response);
    }

    private static void doStuff(String result) {
        System.out.println(result);
    }

}