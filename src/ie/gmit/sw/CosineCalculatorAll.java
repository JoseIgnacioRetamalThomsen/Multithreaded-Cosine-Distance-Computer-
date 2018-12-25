package ie.gmit.sw;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.use.CounterMap;

public class CosineCalculatorAll implements Runnable
{

  private BlockingQueue<CounterMap<Integer>> maps;

  private BlockingQueue<Future<Double>> answers;

  private CounterMap<Integer> queryFile;

  public CosineCalculatorAll(BlockingQueue<CounterMap<Integer>> maps, BlockingQueue<Future<Double>> answers,
      CounterMap<Integer> queryFile)
  {
    super();
    this.maps = maps;
    this.answers = answers;
    this.queryFile = queryFile;
  }

  private ExecutorService executor = Executors.newFixedThreadPool(10);

  @Override
  public void run()
  {
    LinkedList<Future<Double>> result = new LinkedList<>();

    while (true)
    {
      try
      {
        CounterMap<Integer> temp = maps.take();
        if (!(temp instanceof PosionCounterMap))
        {
          answers.put(executor.submit(new CosineCalculator(queryFile, temp)));
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
    /*
     * int total = result.size();
     * 
     * while (total > 0) {
     * 
     * if (result.peek().isDone()) { try { answers.put(result.poll().get());
     * total--; System.out.println(
     * "hapesnsiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"
     * );
     * 
     * } catch (InterruptedException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); } catch (ExecutionException e) { // TODO Auto-generated
     * catch block e.printStackTrace(); } } else {
     * //System.out.println("hapesnsoo"); result.offer(result.poll()); } }
     */

  }

}
