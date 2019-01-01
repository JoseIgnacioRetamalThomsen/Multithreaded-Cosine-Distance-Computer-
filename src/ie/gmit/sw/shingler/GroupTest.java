package ie.gmit.sw.shingler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import ie.gmit.sw.base.GroupShingler;

public class GroupTest
{

  public static void main(String[] args) throws IOException
  {
    GroupShingler g = new GroupShingler(2);

    String[] lines =
    {
        "This is the first line ", "fgh dgfh dfgh gfh dgfh dfg", "fgh dfgh fgh dh gdh dgfh dgh ",
        "this is the second oneb hfgh fgh fg fgh fh fgh fg  fgh dgd dgh  ...",

    };

    /*
     * BufferedReader fileIn = new BufferedReader( new InputStreamReader(new
     * FileInputStream("WarAndPeace-LeoTolstoy.txt"))); String line; int count = 0;
     * while ((line = fileIn.readLine()) != null) { g.addLine(line);
     * 
     * while (g.hasNextShingle()) { System.out.println(count++);
     * System.out.println(g.nextShingle());
     * 
     * }
     * 
     * }
     */
      g.addLine(lines[0]);
      System.out.println(g.nextShingle());
      System.out.println(g.nextShingle());
      System.out.println(g.nextShingle());
  }

}
