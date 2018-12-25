package ie.gmit.sw;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.mapbuilder.MapBuilder;
import ie.gmit.sw.mapbuilder.SingleThreadMapBuilder;
import ie.gmit.sw.shingler.FileShingleParser;
import ie.gmit.sw.use.ConcurrentCounterHashMap;
import ie.gmit.sw.use.CounterMap;

public class Test1
{

  public static void main(String[] args) throws InterruptedException
  {
    File subjectDir = new File("sub");

    String[] files = subjectDir.list();

    BlockingQueue<CounterMap<Integer>> maps = new ArrayBlockingQueue<>(100);

    MapBuilderAll mba = new MapBuilderAll(maps, files, subjectDir);

    new Thread(mba).start();

    BlockingQueue<Future<Double>> answers = new ArrayBlockingQueue<Future<Double>>(100);

    ExecutorService mapBuilderExecutor = Executors.newFixedThreadPool(10);
    BlockingQueue<Number> que1 = new ArrayBlockingQueue<>(10);
    FileShingleParser pa1 = new FileShingleParser(que1);
    
    pa1.setFile("WarAndPeace-LeoTolstoy.txt");
    new Thread(pa1).start();
    
    MapBuilder m1 = new SingleThreadMapBuilder(que1);
    Future<ConcurrentCounterHashMap<Integer>> res1 = mapBuilderExecutor.submit(m1);
    
    CosineCalculatorAll cc = null;
    try
    {
      cc = new CosineCalculatorAll(maps, answers, res1.get());
    } catch (ExecutionException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("yeasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
    new Thread(cc).start();
 
    int count =0;
    
    while (true)
    {
      try
      {
        Future<Double> cm1 = answers.take();
        Double cm = cm1.get();
        System.out.println(count++ +"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx "+cm);
      } catch (ExecutionException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }

  }

}
