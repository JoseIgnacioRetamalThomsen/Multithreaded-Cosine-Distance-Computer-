package ie.gmit.sw.shingler;

import ie.gmit.sw.spliter.SpliteFunctions;

/**
 * 
 * @author pepe
 *
 */
public class GroupShingler extends AbstractShingler
{

  private String[] lineArray;
  private String[] buffer = null;

  private int groupSize;

  private int position;
  private int bufferPosition;
  private boolean isFirst = true;

  private SpliteFunctions sp = new SpliteFunctions();

  /**
   * 
   * @param groupSize
   */
  public GroupShingler(int groupSize)
  {
    super();
    this.groupSize = groupSize;
    this.position = 0;
  }

  /**
   * j
   */
  @Override
  public boolean addLine(CharSequence line)
  {
    if(line.equals("")) return false;
    
    if (this.isFirst ==false)
    {
      if (this.hasNextShingle())
        return false;

      // check for something to add in buffer
      if (position < lineArray.length)
      {
        // point buffer to lineArray, extra memory but better speed
        buffer = lineArray;
        bufferPosition = position;
      } else
      {
        buffer = null;
      }
    }

    this.lineArray = sp.splitWordsNumber(line.toString().toLowerCase());

    String s1 = "";
    for (String s : lineArray)
    {
      s1 += ("_" + s + "_");
    }
    //System.out.println(s1);
    // reset position
    this.position = 0;

    if (this.isFirst ==false) this.isFirst=false;
    
    return true;

  }// addLine(CharSequence line)

  @Override
  public int nextShingle()
  {
    // long shingle = 0;
    StringBuilder sb = new StringBuilder();
    if (buffer != null)
    {
      for (int i = bufferPosition; i < buffer.length; i++)
      {

        sb.append(buffer[i]);
        // shingle += buffer[i].hashCode();
      }
      int i = position;
      // add the number of missing characters
      for (; i < position + groupSize - (buffer.length - bufferPosition); i++)
      {

        sb.append(buffer[i]);

       

      }
      position += i - position;

      buffer = null;
      bufferPosition = 0;
      return sb.toString().hashCode();
    }

    int i = position;
    for (; i < position + groupSize; i++)
    {

      sb.append(lineArray[i]);
     // shingle += lineArray[i].hashCode();

    }

    position += i - position;

    //return shingle;
   // System.out.println(sb);
    return sb.toString().hashCode();
  }

  @Override
  public boolean hasNextShingle()
  {
    if (lineArray.length - position >= groupSize)
    {
      return true;
    }
    return false;
  }

  public boolean hasLast()
  {
    if (buffer != null)
    {
      return true;
    }
    return false;
  }

  public int lastShingle() 
  {
    long shingle = 0;
    
    StringBuilder sb = new StringBuilder();
    
    if (this.buffer != null)
    {
      for (int i = bufferPosition; i < buffer.length; i++)
      {
        sb.append(buffer[i]);
        //shingle += buffer[i].hashCode();
      }
      //return shingle;
      //System.out.println(sb);
      sb.toString().hashCode();
    }
    throw new RuntimeException();
  }

}
