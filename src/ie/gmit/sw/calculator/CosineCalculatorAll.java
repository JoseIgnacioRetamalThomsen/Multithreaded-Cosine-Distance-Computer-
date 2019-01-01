package ie.gmit.sw.calculator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.CosineDistanceResult;
import ie.gmit.sw.counting.CounterMap;
import ie.gmit.sw.counting.PosionCounterMap;

public class CosineCalculatorAll implements Runnable
{

  private BlockingQueue<Future<CounterMap<Integer>>> maps;

  private BlockingQueue<Future<CosineDistanceResult>> answers;

  private CounterMap<Integer> queryFile;

  private ExecutorService executor;
  private int poolSize;

  public CosineCalculatorAll(BlockingQueue<Future<CounterMap<Integer>>> maps,
      BlockingQueue<Future<CosineDistanceResult>> answers, CounterMap<Integer> queryFile, int poolSize)
  {
    super();
    this.maps = maps;
    this.answers = answers;
    this.queryFile = queryFile;
    this.poolSize = poolSize;

    executor = Executors.newFixedThreadPool(poolSize);
  }

  @Override
  public void run()
  {
    // LinkedList<Future<Double>> result = new LinkedList<>();

    while (true)
    {
      System.out.println("is me..");
      try
      {

        CounterMap<Integer> temp = null;
        try
        {
          temp = maps.take().get();
        } catch (ExecutionException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        if (!(temp instanceof PosionCounterMap))
        {
          answers.put(executor.submit(new CosineCalculatorBig(queryFile, temp)));
        } else
        {
          break;
        }
      } catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    System.out.println("no is not...");
    executor.shutdown();

  }// run()

}
