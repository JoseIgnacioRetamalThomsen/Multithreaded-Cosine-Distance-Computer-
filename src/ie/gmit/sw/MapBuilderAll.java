package ie.gmit.sw;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

public class MapBuilderAll implements Runnable
{

  private BlockingQueue<CounterMap<Integer>> maps;

  private String[] files;

  File  subjectDir;

  private int threadNumber =10;

  private ExecutorService parcerExecutor;

  private ExecutorService mapBuilderExecutor;

  public MapBuilderAll(BlockingQueue<CounterMap<Integer>> queue, String[] files, File subjectDir)
  {
    maps = queue;
    
    this.files = files;
    this.subjectDir = subjectDir;

    parcerExecutor = Executors.newFixedThreadPool(threadNumber);
    mapBuilderExecutor = Executors.newFixedThreadPool(threadNumber);
  }

  @Override
  public void run()
  {
    LinkedList<Future<ConcurrentCounterHashMap<Integer>>> res = new LinkedList<>();

    for (String file : files)
    {
      BlockingQueue<Number> fileParseQueue = new ArrayBlockingQueue<>(1000);
      FileShingleParser pa = new FileShingleParser(fileParseQueue);

      pa.setFile(new File(subjectDir, file).toString());

      parcerExecutor.execute(pa);

      MapBuilder mapBuilder = new SingleThreadMapBuilder(fileParseQueue);

      res.offer(mapBuilderExecutor.submit(mapBuilder));
    }
    int total = res.size();

    while (total > 0)
    {

      if (res.peek().isDone())
      {
        try
        {
          maps.put(res.poll().get());
          total--;
         // System.out.println("hapesnsiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");

        } catch (InterruptedException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (ExecutionException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } else
      {
        //System.out.println("hapesnsoo");
        res.offer(res.poll());
      }
    }
    
    try
    {
      maps.put(new PosionCounterMap<Integer>());
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
