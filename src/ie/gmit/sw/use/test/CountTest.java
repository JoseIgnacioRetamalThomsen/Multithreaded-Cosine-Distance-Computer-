package ie.gmit.sw.use.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ie.gmit.sw.counting.ConcurrentCounterHashMap;

class CountTest
{

  static ConcurrentCounterHashMap<Integer> counter;

  @BeforeAll
  static void setUpBeforeClass() throws Exception
  {
    counter = new ConcurrentCounterHashMap<>();
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception
  {
  }

  @BeforeEach
  void setUp() throws Exception
  {
  }

  @AfterEach
  void tearDown() throws Exception
  {
  }

  @Test
  void test()
  {
    ExecutorService pool = Executors.newFixedThreadPool(100);

    int value[] =
    {
        1234, 2343, 32423, 432432
    };
    int count[] =
    {
        2346767, 2367674, 67876234, 23467684
    };

    int times = 20;
    
    for (int m = 0; m < value.length; m++)
    {
      final int temporal = m;
      for (int i = 0; i < times; i++)
      {

        pool.execute(new Runnable()
        {

          @Override
          public void run()
          {
            for (int j = 0; j < count[temporal]; j++)
            {
              counter.count(value[temporal]);
            }
          }
        });
      }
    }
    pool.shutdown();
    try
    {
      pool.awaitTermination(500, TimeUnit.SECONDS);
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    for (int i = 0; i < value.length; i++)
    {
      Assert.assertEquals(count[i] * times, counter.getCount(value[i]));
    }

  }

}
