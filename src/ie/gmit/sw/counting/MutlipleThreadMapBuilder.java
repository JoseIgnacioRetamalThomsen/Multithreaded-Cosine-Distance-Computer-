package ie.gmit.sw.counting;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.Poison;

//import ie.gmit.sw.SingleThreadMapBuilder.MyMap;

public class MutlipleThreadMapBuilder implements MapBuilder
{
  private final BlockingQueue<Number> queue;

  // Map<Long, Integer> map = new HashMap<>();
  MyMap map1 = new MyMap();
  ExecutorService es = Executors.newFixedThreadPool(10);

  public MutlipleThreadMapBuilder(BlockingQueue<Number> queue)
  {
    super();
    this.queue = queue;
  }

  @Override
  public ConcurrentCounterHashMap<Integer> call() throws Exception
  {

    es.execute(new MapPutter());

    es.shutdown();
    es.awaitTermination(500, TimeUnit.SECONDS);

    /*
     * for (Long n : map.keySet()) { System.out.println(map.get(n)); }
     */

    // es.shutdown();
    // es.awaitTermination(500, TimeUnit.SECONDS);

    return null;
    // return map;
  }

  private Object lock1 = new Object();

  class MapPutter implements Runnable
  {

    long n;

    public MapPutter()
    {
      super();

    }

    @Override
    public void run()
    {
      while (true)
      {
        Number n = null;
        try
        {
          n = queue.take();
        } catch (InterruptedException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        // System.out.println(n);

        if (!(n instanceof Poison))
        {
          // es.execute(new MapPutter((long) n));
          map1.add((Long) n);

        } else
        {

          break;
        }
      }

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
