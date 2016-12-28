#General
This shows the difference in performance between synchronous execution, blocking futures, and callback-style completable futures.
_The main benefits between the two types of futures are noticed when there are several calls made to the same method since a future will still ultimately need to block at some point, but a completable future will release control until it's async tasks have finished accordingly.

#To build
mvn install

#To run
```cd target```
```java -cp AsyncRunner-1.jar com.alissa.Application```

### Notes
   * runnable / callable:
     _these are things that actually take some time to execute_

   * Executor is the thing which determines where the task will be done:
     _(e.g. new thread, threadpool, etc.)_

   * Supplier