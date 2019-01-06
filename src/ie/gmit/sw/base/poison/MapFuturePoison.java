/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */

package ie.gmit.sw.base.poison;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import ie.gmit.sw.base.CounterMap;
/**
 * Poison for {@code CounterMap} queue.
 * 
 * @author Jose I. Retamal
 *
 * @param <V> type of counter map
 */
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
