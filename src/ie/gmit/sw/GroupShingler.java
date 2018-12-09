package ie.gmit.sw;
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
    if (this.lineArray != null)
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
    // split input into string in the lineArray
    this.lineArray = line.toString().split(" ");

    for (String s : lineArray)
    {
     // System.out.print("_" + s + "_");
    }
   // System.out.println();
    // reset position
    this.position = 0;

    return true;

  }// addLine(CharSequence line)

  @Override
  public long nextShingle()
  {
    long shingle = 0;
    // if (lineArray.length - position > groupSize)

    // check buffer
    if (buffer != null)
    {
      for (int i = bufferPosition; i < buffer.length; i++)
      {
        shingle += buffer[i].hashCode();
      }
      int i = position;
      // add the number of missing characters
      for (; i < position + groupSize - (buffer.length - bufferPosition); i++)
      {
        if (lineArray[i].hashCode() == 0)
        {

          position++;
          continue;
        }
        shingle += lineArray[i].hashCode();

      }
      position += i - position;

      buffer = null;
      return shingle;
    }

    int i = position;
    for (; i < position + groupSize; i++)
    {
      if (lineArray[i].hashCode() == 0)
      {

        position++;
        continue;
      }
      shingle += lineArray[i].hashCode();

    }
    // System.out.println("i after: " + i);
    position += i - position;

    return shingle;
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
  
  public boolean hasLast() {
    if(buffer!=null) {
      return true;
    }
    return false;
  }
  
  public long lastShingle() throws Exception {
    long shingle = 0;
    if(this.buffer !=null) {
      for (int i = bufferPosition; i < buffer.length; i++)
      {
        shingle += buffer[i].hashCode();
      }
      return shingle;
    }
    throw new Exception();
  }

}
