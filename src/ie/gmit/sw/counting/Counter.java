package ie.gmit.sw.counting;

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
import java.util.concurrent.LinkedBlockingQueue;

import ie.gmit.sw.ShingleType;
import ie.gmit.sw.shingler.FileShingleParser;

public class Counter implements Runnable
{

  private BlockingQueue<CounterMap<Integer>> maps;

  private String[] files;

  private File subjectDir;

  private int threadNumber = 10;

  private ExecutorService parcerExecutor;

  private ExecutorService mapBuilderExecutor;
  private int shingleSize;
  
  private ShingleType shingleType;
  
  private int internalQueueSize = 10000; //this limit memory usage, higher value more memory use.

  public Counter(BlockingQueue<CounterMap<Integer>> queue, String[] files, File subjectDir, int shingleSize,ShingleType shingleType)
  {
    maps = queue;

    this.files = files;
    this.subjectDir = subjectDir;

    parcerExecutor = Executors.newFixedThreadPool(threadNumber);
    mapBuilderExecutor = Executors.newFixedThreadPool(threadNumber);
    
    this.shingleSize = shingleSize;
    
    this.shingleType = shingleType;
  }
  

  @Override
  public void run()
  {
   // LinkedList<Future<CounterMap<Integer>>> res = new LinkedList<>();
    BlockingQueue<Future<CounterMap<Integer>>> res1 = new LinkedBlockingQueue<>(internalQueueSize);

    new Thread( new Runnable() {

      @Override
      public void run()
      {
        for (String file : files)
        {
          BlockingQueue<Number> fileParseQueue = new LinkedBlockingQueue<>(1000);

          FileShingleParser parcer = new FileShingleParser(fileParseQueue,shingleSize,shingleType);

          parcer.setFile(new File(subjectDir, file));

          parcerExecutor.execute(parcer);

          MapBuilder mapBuilder = new SingleThreadMapBuilder(fileParseQueue, file);

          //res.offer(mapBuilderExecutor.submit(mapBuilder));
          try
          {
            res1.put(mapBuilderExecutor.submit(mapBuilder));
          } catch (InterruptedException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        }
         try
        {
          res1.put(new MapFuturePoison<String>());
        } catch (InterruptedException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      
    }).start();
    
    new Thread(new Runnable() {

      @Override
      public void run()
      {
        while(true) {
          
          try
          {
            Future<CounterMap<Integer>> temp = res1.take();
            
            if(temp instanceof MapFuturePoison) {
              maps.put(new PosionCounterMap<Integer>());
              break;
            }
            
            maps.put(temp.get());
          } catch (InterruptedException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (ExecutionException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          
        }
        parcerExecutor.shutdown();
        mapBuilderExecutor.shutdown();
        
      }
      
    }).start();
    
/*
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
        // System.out.println("hapesnsoo");
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
*/
    
    
  }//run()

}
