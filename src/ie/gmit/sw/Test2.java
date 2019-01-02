package ie.gmit.sw;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import ie.gmit.sw.base.CountOne;
import ie.gmit.sw.base.CounterMap;

public class Test2
{

  public static void main(String[] args)
  {

    CountOne c = new CountOne(5, ShingleType.K_Mers);
    c.setFile(new File("warandpeace-leotolstoy.txt"));

    Future<CounterMap<Integer>> queryMap = c.calculate();
    System.out.println("here");

    try
    {
      queryMap.get();
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ExecutionException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("done");
  }

}
