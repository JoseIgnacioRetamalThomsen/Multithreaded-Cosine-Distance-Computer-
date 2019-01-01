package ie.gmit.sw.counting;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MapFuturePoison<V>  implements Future<CounterMap<Integer>>
{

  

  @Override
  public boolean cancel(boolean mayInterruptIfRunning)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public CounterMap<Integer> get() throws InterruptedException, ExecutionException
  {
   
    return new PosionCounterMap<Integer>();
  }

  @Override
  public CounterMap<Integer> get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException
  {
    return null;
  }

  @Override
  public boolean isCancelled()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isDone()
  {
    // TODO Auto-generated method stub
    return false;
  }

}
