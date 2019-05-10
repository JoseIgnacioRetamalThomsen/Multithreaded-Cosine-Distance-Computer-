package ie.gmit.sw.base.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ie.gmit.sw.base.KShingler;

class KShinglerTest
{
  int lenght = 5;
  String[] lines =
  {
      "This is the first line", "this is the second one...",

  };
  int[] expectedResult =
  {
      80778530, 100430122, 94330324, 109694876, 104711006, 107262729, 110324868, 96335111, 32968360, 1472

  };

  int[] result = new int[expectedResult.length];

  @BeforeAll
  static void setUpBeforeClass() throws Exception
  {
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
  void test() throws Exception
  {
    KShingler ks = new KShingler(lenght);

    int resultCount = 0;
    for (int i = 0; i < lines.length; i++)
    {

      ks.addLine(lines[i]);
      while (ks.hasNextShingle())
      {
        result[resultCount++] = ks.nextShingle();
      }

    }
    if (ks.hasLast())
    {
      result[resultCount++] = ks.lastShingle();
    }

    for (int i = 0; i < expectedResult.length; i++)
    {

      Assert.assertEquals(expectedResult[i], result[i]);
    }
  }

}
