package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Tester
{

  public static void main(String[] args)
  {/*
    * GroupShingler g = new GroupShingler(1);
    * 
    * // g.addLine("I think I may have signed my political death-warrant tonight");
    * g.addLine("a b c d e"); /* System.out.println(g.hasNextShingle());
    * System.out.println(g.nextShingle()); System.out.println(g.nextShingle());
    * System.out.println(g.nextShingle()); System.out.println(g.nextShingle());
    * System.out.println(g.nextShingle()); System.out.println(g.nextShingle());
    * System.out.println(g.nextShingle()); System.out.println(g.nextShingle());
    * System.out.println(g.nextShingle()); System.out.println(g.hasNextShingle());
    * System.out.println(g.nextShingle()); System.out.println(g.hasNextShingle());
    */
    // System.out.println(g.nextShingle());
    /*
     * while (g.hasNextShingle()) { System.out.println(g.nextShingle()); }
     * 
     * g.addLine("a b c d e");
     * 
     * while (g.hasNextShingle()) { System.out.println(g.nextShingle()); }
     * g.addLine("a b c d e");
     * 
     * while (g.hasNextShingle()) { System.out.println(g.nextShingle()); }
     * 
     */
    long start = System.nanoTime();
    BlockingQueue<Number> queue = new LinkedBlockingQueue<>(100);

    FileShingleParser p = new FileShingleParser(queue);
    p.setFile("f.txt");

    Thread te = new Thread(p);
    te.start();
    /*
     * try { te.join(); } catch (InterruptedException e) { // TODO Auto-generated
     * catch block e.printStackTrace(); }
     */
    Map<Long, Integer> map = new HashMap<>();

    MapBuilder mp = new MapBuilder(queue);

    List<Future<Map<Long, Integer>>> result = new ArrayList<>();
    ExecutorService pool = Executors.newFixedThreadPool(3);
    result.add(pool.submit(mp));
    for (Future<Map<Long, Integer>> r : result)
    {
      Map<Long, Integer> mk = null;
      try
      {
        System.out.println(r.isDone());
        mk = r.get();
        System.out.println(r.isDone());
        System.out.println(mk.size());
      } catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ExecutionException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      for (Long n : mk.keySet())
      {
        System.out.println(mk.get(n));
      }
    }
    pool.shutdown();
    try
    {
      pool.awaitTermination(500, TimeUnit.SECONDS);
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    long elapsedTime = System.nanoTime() - start;
    System.out.println(((double)elapsedTime) / 1000);
    /*
     * for (Number n : queue) { System.out.println(n); if (!(n instanceof Poison)) {
     * if (map.containsKey(n)) { map.put((Long) n, map.remove(n) + 1); } else {
     * map.put((Long) n, 1); } } } for (Long n : map.keySet()) {
     * System.out.println(map.get(n)); }
     */

  }

}
