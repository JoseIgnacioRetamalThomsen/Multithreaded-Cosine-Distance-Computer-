package ie.gmit.sw.spliter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSpliteFunctions
{
  SpliteFunctions sp;

  @Before
  public void setUp()
  {
    sp = new SpliteFunctions();
  }

  @Test
  public void testSplitWordsNumber1()
  {

    String input = "one, two,     three-three,   four?, five!, seven. eight 345 567 ";
    String[] result =
    {
        "one", "two", "three-three", "four", "five", "seven", "eight", "345", "567"
    };

    Assert.assertEquals(result, sp.splitWordsNumber(input));

  }
  @Test
  public void testSplitWordsNumber2()
  {

    String input = "one";
    String[] result =
    {
        "one"
    };

    Assert.assertEquals(result, sp.splitWordsNumber(input));

  }
  @Test
  public void testSplitWordsNumber3()
  {

    String input = "";
    String[] result =
    {
       ""
    };

    Assert.assertEquals(result, sp.splitWordsNumber(input));

  }
  
  @Test
  public void testSplitWordsNumber4()
  {

    String input = "one1, two2,     three-three3";
    String[] result =
    {
        "one1", "two2", "three-three3"
    };

    Assert.assertEquals(result, sp.splitWordsNumber(input));

  }
  @Test
  public void testSplitWordsNumber5()
  {

    String input = "I think I may have, signed, my, political, death-warrant tonight";
    String[] result =
    {
        
          "I", "think","I", "may", "have", "signed", "my", "political", "death-warrant", "tonight"
          
    };

    Assert.assertEquals(result, sp.splitWordsNumber(input));

  }

 

  public static void main(String args[])
  {
    org.junit.runner.JUnitCore.main("ie.gmit.sw.spliter.TestSpliteFunctions");
  }

}
