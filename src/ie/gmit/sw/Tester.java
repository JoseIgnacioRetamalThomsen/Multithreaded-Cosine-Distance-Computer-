package ie.gmit.sw;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.calculator.CosineCalculator;
import ie.gmit.sw.counting.ConcurrentCounterHashMap;
import ie.gmit.sw.counting.CounterMap;
import ie.gmit.sw.counting.MapBuilder;
import ie.gmit.sw.counting.SingleThreadMapBuilder;
import ie.gmit.sw.fileshingleparser.FileShingleParser;

public class Tester
{

  public static void main(String[] args) throws InterruptedException, ExecutionException
  {
    
    //Thread.sleep(10000);
    System.out.println(new String("la").hashCode());
    /*
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

    List<Future<CounterMap<Integer>>> res = new ArrayList<>();

    File subjectDir = new File("sub");

    String[] files = subjectDir.list();

    ExecutorService parcerExecutor = Executors.newFixedThreadPool(100);
    ExecutorService mapBuilderExecutor = Executors.newFixedThreadPool(100);

    //the one file
    
    BlockingQueue<Number> que1 = new ArrayBlockingQueue<>(100);
    FileShingleParser pa1 = new FileShingleParser(que1,5,ShingleType.K_Mers);
    pa1.setFile(new File("WarAndPeace-LeoTolstoy.txt"));
    new Thread(pa1).start();
    MapBuilder m1 = new SingleThreadMapBuilder(que1,"w");
    
    Future<CounterMap<Integer>> res1 = mapBuilderExecutor.submit(m1);
    
    for (String f : files)
    {

      BlockingQueue<Number> que = new ArrayBlockingQueue<>(100);
      FileShingleParser pa = new FileShingleParser(que,5,ShingleType.K_Mers);
     // pa.setFile(subjectDir.getPath()+"/" +f);
     // System.out.println(new File(subjectDir,f).toString());
      pa.setFile(new File(subjectDir,f));
      parcerExecutor.execute(pa);

      MapBuilder m = new SingleThreadMapBuilder(que,f);

      res.add(mapBuilderExecutor.submit(m));
    }
   
    parcerExecutor.shutdown();
    try
    {
      parcerExecutor.awaitTermination(500, TimeUnit.SECONDS);
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    mapBuilderExecutor.shutdown();
    try
    {
      mapBuilderExecutor.awaitTermination(500, TimeUnit.SECONDS);
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
   ExecutorService las = Executors.newFixedThreadPool(100);
    
    System.out.println("d"+res.size());
    
    ArrayList<Future<CosineDistanceResult>> rr = new ArrayList<>(); 
    for(Future<CounterMap<Integer>> mk : res) {
      
      rr.add(las.submit(new CosineCalculator(res1.get(),mk.get() ))) ;
     // System.out.println(mk.get());
    }
    

    las.shutdown();
    try
    {
      las.awaitTermination(500, TimeUnit.SECONDS);
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    for(Future<CosineDistanceResult> dou:rr) {
      System.out.println(dou.get().getCosineDistance());
    }
    /*
     * long start = System.nanoTime(); BlockingQueue<Number> queue = new
     * LinkedBlockingQueue<>(100);
     * 
     * BlockingQueue<Number> queue1 = new LinkedBlockingQueue<>(100);
     * 
     * FileShingleParser p = new FileShingleParser(queue); FileShingleParser p1 =
     * new FileShingleParser(queue1); p.setFile("WarAndPeace-LeoTolstoy.txt"); //
     * p.setFile("a.txt"); p1.setFile("f.txt"); // p1.setFile("aa.txt"); Thread te =
     * new Thread(p); Thread t1 = new Thread(p1); te.start(); t1.start();
     * 
     * try { te.join(); } catch (InterruptedException e) { // TODO Auto-generated
     * catch block e.printStackTrace(); }
     * 
     * Map<Long, Integer> map = new HashMap<>();
     * 
     * MapBuilder mp = new SingleThreadMapBuilder(queue); MapBuilder mp1 = new
     * SingleThreadMapBuilder(queue1);
     * 
     * List<Future<ConcurrentCounterHashMap<Integer>>> result = new ArrayList<>();
     * 
     * ExecutorService pool = Executors.newFixedThreadPool(3);
     * 
     * result.add(pool.submit(mp));
     * 
     * result.add(pool.submit(mp1));
     * 
     * ArrayList< ConcurrentCounterHashMap<Integer>> mk = new ArrayList<>();
     * 
     * for (Future<ConcurrentCounterHashMap<Integer>> r : result) {
     * 
     * try { System.out.println("is done:"+ r.isDone()); mk.add(r.get());
     * 
     * 
     * } catch (InterruptedException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); } catch (ExecutionException e) { // TODO Auto-generated
     * catch block e.printStackTrace(); } } for (Integer n :
     * mk.get(0).getEntrySet()) { // System.out.println(n+ " "+mk.get(n)); }
     * pool.shutdown(); try { pool.awaitTermination(500, TimeUnit.SECONDS); } catch
     * (InterruptedException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); }
     * 
     * System.out.println("this size 1 "+mk.get(0).size());
     * System.out.println("this size 2 "+mk.get(1).size()); //
     * System.out.println("this"+mk.get(0).get(3445L));
     * 
     * 
     * 
     * 
     * 
     * 
     * CosineCalculator cos = new CosineCalculator( mk.get(0), mk.get(1));
     * ExecutorService pool1 = Executors.newFixedThreadPool(3); pool1.submit(cos);
     * 
     * pool1.shutdown(); try { pool1.awaitTermination(500, TimeUnit.SECONDS); }
     * catch (InterruptedException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); }
     * 
     * for (Number n : queue) { System.out.println(n); if (!(n instanceof Poison)) {
     * if (map.containsKey(n)) { map.put((Long) n, map.remove(n) + 1); } else {
     * map.put((Long) n, 1); } } } for (Long n : map.keySet()) {
     * System.out.println(map.get(n)); }
     * 
     * long elapsedTime = System.nanoTime() - start;
     * System.out.println("last"+((double)elapsedTime)/1000000 );
     */
  }

}
