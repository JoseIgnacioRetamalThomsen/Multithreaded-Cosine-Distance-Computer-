package ie.gmit.sw.shingler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.Poison;

public class FileShingleParser implements Runnable
{
  private final BlockingQueue<Number> queue;

  BufferedReader fileIn;

  String line;

  private Shingable shingler;

  public FileShingleParser(BlockingQueue<Number> queue)
  {
    this.queue = queue;
    shingler = new KShingler(5);
  }

  public void setFile(String path)
  {

    try
    {
      fileIn = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

    } catch (FileNotFoundException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void run()
  {

    try
    {
      while ((line = fileIn.readLine()) != null)
      {
        shingler.addLine(line);

        while (shingler.hasNextShingle())
        {

          queue.put(shingler.nextShingle());

        }

      } // while ((line = fileIn.readLine()) != null)

      if (shingler.hasLast())
      {

        queue.put(shingler.lastShingle());

      }

      queue.put(new Poison());

    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();

    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();

    } catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
