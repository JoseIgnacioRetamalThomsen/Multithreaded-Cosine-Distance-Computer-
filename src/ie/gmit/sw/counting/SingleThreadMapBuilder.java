package ie.gmit.sw.counting;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ie.gmit.sw.Poison;

public class SingleThreadMapBuilder implements MapBuilder
{

  private final BlockingQueue<Number> queue;

  CounterMap<Integer> countingMap = new ConcurrentCounterHashMap<>();

  //ExecutorService es = Executors.newFixedThreadPool(10000);

  public SingleThreadMapBuilder(BlockingQueue<Number> queue,String fileName)
  {
    super();
    this.queue = queue;
    this.countingMap.setName(fileName);
  }

  @Override
  public CounterMap<Integer> call() throws Exception
  {

    while (true)
    {
      Number n = queue.take();

      if (!(n instanceof Poison))
      {
        countingMap.count((Integer) n);

      } else
      {

        break;
      }

    }

    return countingMap;
  }

}
