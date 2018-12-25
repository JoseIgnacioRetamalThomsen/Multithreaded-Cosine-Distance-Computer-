package ie.gmit.sw;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.Callable;

import ie.gmit.sw.use.ConcurrentCounterHashMap;
import ie.gmit.sw.use.CounterMap;

public class CosineCalculator implements Callable<Double>
{
  private CounterMap<Integer> map1;
  private CounterMap<Integer> map2;

  public CosineCalculator(CounterMap<Integer> map1, CounterMap<Integer> map2)
  {
    super();
    this.map1 = map1;
    this.map2 = map2;
  }

  

  double sum1;
  double sum2;
  double mult;

  @Override
  public Double call() throws Exception
  {

    for (int i : map1.getCountAll())
    {
      sum1 += i*i ;
    }
    for (int i : map2.getCountAll())
    {
      sum2 += i*i ;
    }

    for (Integer l : map1.getEntrySet())
    {
      Integer ll =0;
      if ( map2.haveCount(l))
      {
        mult += (map1.getCount(l) * map2.getCount(l));
      }
    }

    //System.out.println("s1 " + sum1);
    //System.out.println("s2 " + sum2);
    DecimalFormat f = new DecimalFormat("##.00");
   // sum1 =Double.parseDouble(f.format(Math.sqrt(sum1)));
  //  sum2 =Double.parseDouble(f.format(Math.sqrt(sum2)));

    sum1 =Math.sqrt(sum1);
    sum2 =Math.sqrt(sum2);
    
    //System.out.println("m" + mult);
  //  System.out.println("s1 " + sum1);
  //  System.out.println("s2 " + sum2);

    double result = (double)mult / ((sum1 * sum2));
  //  System.out.println("t " + (result));
    return result;
  }

}
