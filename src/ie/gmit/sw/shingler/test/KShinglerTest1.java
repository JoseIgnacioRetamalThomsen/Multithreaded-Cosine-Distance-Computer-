package ie.gmit.sw.shingler.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ie.gmit.sw.shingler.KShingler;

class KShinglerTest1
{

  int lenght = 5;
  String[] lines =
  {
      "This is the first line", "this is the second one...",

  };

  static BufferedReader fileIn;

  @BeforeAll
  static void setUpBeforeClass() throws Exception
  {
    try
    {
      fileIn = new BufferedReader(new InputStreamReader(new FileInputStream(new File("testo7.txt"))));

    } catch (FileNotFoundException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
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
  void test() throws IOException
  {
    KShingler ks = new KShingler(lenght);
    // fail("Not yet implemented");
    String line;
    while ((line = fileIn.readLine()) != null)
    {
      System.out.println(line);
      StringBuilder sb = new StringBuilder(line);
      System.out.println(sb.length());
      ks.addLine(line);
      while(ks.hasNextShingle()) {
        System.out.println(ks.nextShingle());
      }
    }
  }

}
