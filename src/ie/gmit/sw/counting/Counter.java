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
import ie.gmit.sw.fileshingleparser.FileShingleParser;

public class Counter implements Runnable
{

  private BlockingQueue<Future<CounterMap<Integer>>> maps;

  private File[] files;

  private File subjectDir;

  private int threadNumber = 10;

  private ExecutorService parcerExecutor;

  private ExecutorService mapBuilderExecutor;
  private int shingleSize;

  private ShingleType shingleType;

  public Counter(BlockingQueue<Future<CounterMap<Integer>>> queue, File[] files, File subjectDir, int shingleSize,
      ShingleType shingleType)
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

    for (File file : files)
    {

      BlockingQueue<Number> fileParseQueue = new LinkedBlockingQueue<>(1000);

      FileShingleParser parcer = new FileShingleParser(fileParseQueue, shingleSize, shingleType);

      parcer.setFile(file);

      parcerExecutor.execute(parcer);

      MapBuilder mapBuilder = new SingleThreadMapBuilder(fileParseQueue, file.toString());

      try
      {
        maps.put(mapBuilderExecutor.submit(mapBuilder));
      } catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    try
    {
      maps.put(new MapFuturePoison<Integer>());
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    parcerExecutor.shutdown();
    mapBuilderExecutor.shutdown();

  }// run()

}
