package ie.gmit.sw.shingler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.Poison;
import ie.gmit.sw.ShingleType;

public class FileShingleParser implements Runnable
{
  private final BlockingQueue<Number> queue;

  BufferedReader fileIn;

  String line;

  private Shingable shingler;

  public FileShingleParser(BlockingQueue<Number> queue, int shingleSize, ShingleType shingleType)
  {
    this.queue = queue;
    switch (shingleType)
    {
    case Group:
      shingler = new GroupShingler(shingleSize);
      break;
    case K_Mers:
      shingler = new KShingler(shingleSize);
      break;
      
    }

  }

  public boolean setFile(File file)
  {

    try
    {
      fileIn = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      return true;
    } catch (FileNotFoundException e)
    {
      //not a file return false
      return false;
    }
  }

  public boolean isFileSet() {
    return fileIn!=null;
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
