package ie.gmit.sw.base;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.CosineDistanceResult;
import ie.gmit.sw.ShingleType;

public class CountOne
{
  private int queuuSize = 1000;

  private int shingleSize;

  private ShingleType shingleType;

  private ExecutorService mapBuilderExecutor = Executors.newFixedThreadPool(10);
  
  private File file;

  private BlockingQueue<Number> queue;
  
  private FileShingleParser parser;

  public CountOne(int shingleSize, ShingleType shingleType)
  {

    this.shingleSize = shingleSize;
    this.shingleType = shingleType;
   
    queue = new ArrayBlockingQueue<>(queuuSize);
    
  }

  public boolean setFile(File file) {
    
    parser = new FileShingleParser(queue, shingleSize, shingleType);
    return  parser.setFile(file);
                
  }
  
  public Future<CounterMap<Integer>> calculate()
  {
    
    if(!parser.isFileSet())
      throw new RuntimeException();
    
    
           
    new Thread(parser).start();
    
    MapBuilder mapBuilder = new SingleThreadMapBuilder(queue,"w");

    Future<CounterMap<Integer>> result = mapBuilderExecutor.submit(mapBuilder);
    mapBuilderExecutor.shutdown();
    return  result;
  }
}
