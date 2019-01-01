package ie.gmit.sw;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.calculator.CosineCalculatorAll;
import ie.gmit.sw.counting.ConcurrentCounterHashMap;
import ie.gmit.sw.counting.CounterMap;
import ie.gmit.sw.counting.MapBuilder;
import ie.gmit.sw.counting.Counter;
import ie.gmit.sw.counting.SingleThreadMapBuilder;
import ie.gmit.sw.fileshingleparser.FileShingleParser;

public class Test1
{

  public static void main(String[] args) throws InterruptedException
  {
    /*
    
    //Thread.sleep(10000);
    
    File subjectDir = new File("input_files");

    String[] files = subjectDir.list();

    BlockingQueue<CounterMap<Integer>> maps = new ArrayBlockingQueue<>(100);

    Counter mba = new Counter(maps, files, subjectDir,1,ShingleType.K_Mers);

    new Thread(mba).start();

    BlockingQueue<Future<CosineDistanceResult>> answers = new ArrayBlockingQueue<Future<CosineDistanceResult>>(100);

    ExecutorService mapBuilderExecutor = Executors.newFixedThreadPool(10);
    BlockingQueue<Number> que1 = new ArrayBlockingQueue<>(10);
    FileShingleParser pa1 = new FileShingleParser(que1,1,ShingleType.K_Mers);
    
    pa1.setFile(new File("WarAndPeace-LeoTolstoy.txt"));
   // pa1.setFile("f.txt");
    new Thread(pa1).start();
    
    MapBuilder m1 = new SingleThreadMapBuilder(que1,"war");
    Future<CounterMap<Integer>> res1 = mapBuilderExecutor.submit(m1);
    
    CosineCalculatorAll cc = null;
    try
    {
      cc = new CosineCalculatorAll(maps, answers, res1.get(),10);
    } catch (ExecutionException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    //System.out.println("yeasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
    new Thread(cc).start();
 
    int count =0;
    
    while (true)
    {
      try
      {
        Future<CosineDistanceResult> cm1 = answers.take();
        CosineDistanceResult cm = cm1.get();
        System.out.println(cm.getFileName() + " " + cm.getCosineDistance());
      } catch (ExecutionException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
*/
  }

 
}

interface A{}
interface B{}
class C implements A,B{}
