package ie.gmit.sw.base;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.data.CosineDistanceResult;

public class CosineCalculator extends AbstractCosineCalculator
{
  private CounterMap<Integer> map1;
  private CounterMap<Integer> map2;

  Future<Double> sumQueryMapFuture;
  Future<Double> sumSubjectMapFuture;
  Future<Double> multFuture;
  

  ExecutorService executor = Executors.newFixedThreadPool(3);

  public CosineCalculator(CounterMap<Integer> map1, CounterMap<Integer> map2)
  {
    super();
    this.map1 = map1;
    this.map2 = map2;
  }

  @Override
  public CosineDistanceResult call() throws Exception
  {

    sumQueryMapFuture = executor.submit(new Callable<Double>()
    {

      @Override
      public Double call() throws Exception
      {
        double sum = 0;
        for (int i : map1.getCountAll())
          sum += i * i;

        return sum;
      }

    });

    sumSubjectMapFuture = executor.submit(new Callable<Double>()
    {

      @Override
      public Double call() throws Exception
      {
        double sum = 0;

        for (int i : map2.getCountAll())
          sum += i * i;

        return sum;
      }

    });

    multFuture = executor.submit(new Callable<Double>()
    {

      @Override
      public Double call() throws Exception
      {
        double mult = 0;

        for (Integer l : map1.getEntrySet())
          if (map2.haveCount(l))
            mult += (map1.getCount(l) * map2.getCount(l));

        return mult;

      }

    });
    
    executor.shutdown();

    DecimalFormat f = new DecimalFormat("##.00");

    MathContext mc = new MathContext(2, RoundingMode.HALF_UP);

    

    double sumQueryMap = sumQueryMapFuture.get();
    double sumSubjectMap = sumSubjectMapFuture.get();
    sumQueryMap = Math.sqrt(sumQueryMap);
    sumSubjectMap = Math.sqrt(sumSubjectMap);
    double result = Double.parseDouble(f.format(multFuture.get())) / ((sumQueryMap * sumSubjectMap));

    
    return new CosineDistanceResult(map2.getName(),map1.getName(), result);
    // return new CosineDistanceResult(map2.getName(), res.doubleValue());

  }//call()


}
