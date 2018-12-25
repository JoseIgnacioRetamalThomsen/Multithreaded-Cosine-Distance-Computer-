package ie.gmit.sw.mapbuilder;

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
import ie.gmit.sw.use.ConcurrentCounterHashMap;

public class SingleThreadMapBuilder implements MapBuilder
{

  private final BlockingQueue<Number> queue;

  Map<Long, Integer> map = new HashMap<>();
  ConcurrentCounterHashMap<Integer> countingMap = new ConcurrentCounterHashMap<>();
  MyMap map1 = new MyMap();
  ExecutorService es = Executors.newFixedThreadPool(10000);

  public SingleThreadMapBuilder(BlockingQueue<Number> queue)
  {
    super();
    this.queue = queue;
  }

  @Override
  public ConcurrentCounterHashMap<Integer> call() throws Exception
  {

    while (true)
    {
      Number n = queue.take();
      // System.out.println(n);

      if (!(n instanceof Poison))
      {
        countingMap.count((Integer) n);
        /*
         * //es.execute(new MapPutter((long) n)); if (map.containsKey(n)) {
         * map.put((Long) n, map.get(n) + 1); } else { map.put((Long) n, 1); }
         * 
         */
      } else
      {

        break;
      }

      // es.shutdown();
      // es.awaitTermination(500, TimeUnit.SECONDS);

    }
    /*
     * for (Long n : map.keySet()) { System.out.println(map.get(n)); }
     */

    // es.shutdown();
    // es.awaitTermination(500, TimeUnit.SECONDS);

    // return map1.getMat();
   // System.out.println("insede size " + countingMap.size());
    return countingMap;
  }

  private Object lock1 = new Object();

  class MapPutter implements Runnable
  {

    long n;

    public MapPutter(long n)
    {
      super();
      this.n = n;
    }

    @Override
    public void run()
    {
      map1.add(n);
    }

  }

  class MyMap
  {
    Map<Long, Integer> map = new HashMap<>();

    public synchronized void add(Long n)
    {
      if (map.containsKey(n))
      {
        map.put((Long) n, map.get(n) + 1);
      } else
      {
        map.put((Long) n, 1);
      }
    }

    Map<Long, Integer> getMat()
    {
      return map;
    }
  }

}
