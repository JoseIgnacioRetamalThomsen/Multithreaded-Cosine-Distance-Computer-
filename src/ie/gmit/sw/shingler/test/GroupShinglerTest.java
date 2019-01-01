package ie.gmit.sw.shingler.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ie.gmit.sw.base.GroupShingler;




class GroupShinglerTest
{
  
  String[] lines =
  {
      "This is the first line", "this is the second one...",

  };

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
  void test()
  {
    GroupShingler gs = new GroupShingler(2);
    gs.addLine(lines[0]);
    System.out.println(gs.nextShingle());
    System.out.println(gs.nextShingle());
    System.out.println(gs.nextShingle());
    System.out.println(gs.nextShingle());
    //fail("Not yet implemented");
  }

}
