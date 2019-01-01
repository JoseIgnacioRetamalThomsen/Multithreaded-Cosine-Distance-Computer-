/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */

package ie.gmit.sw.base;

import java.util.NoSuchElementException;

import ie.gmit.sw.shingler.Shingable;
import ie.gmit.sw.spliter.SpliteFunctions;

/**
 * Implementation of shingable for create a word group shingles.
 * <p>
 * Considered only words and number, skip punctuation and white spaces, numbers
 * are considered words and words appended to number are considered one
 * word(e.g. neo07)
 * </p>
 * <p>
 * The group of words are append and then the hash code of all the words
 * appended together is returned.
 * </p>
 * 
 * @author Jose I. Retamal
 *
 */
public class GroupShingler implements Shingable
{
  /*
   * implemented using array of words, which is created using a split function.
   * Use 2 arrays one for the actual words and one for buffer words for next line.
   */

  private String[] lineArray;
  private String[] buffer = null;

  private int groupSize;
  private int position;
  private int bufferPosition;
  private boolean isFirst = true;

  private SpliteFunctions sp = new SpliteFunctions();

  /**
   * Create a group shingles that will produce shingles with selected size of
   * words group.
   * 
   * @param groupSize size of groups of words
   */
  public GroupShingler(int groupSize)
  {
    super();
    this.groupSize = groupSize;
    this.position = 0;
  }

  /**
   *
   * {@inheritDoc}
   * <p>
   * Split line in strings that are individual words, then add then to the array
   * of words.
   * </p>
   */
  public boolean addLine(CharSequence line)
  {
    if (line.equals(""))
      return false;

    if (this.isFirst == false)
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

    // reset position
    this.position = 0;

    if (this.isFirst == false)
      this.isFirst = false;

    return true;

  }

  /**
   * {@inheritDoc}
   */
  public int nextShingle()
  {
    if (!hasNextShingle())
      throw new NoSuchElementException();

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

    }

    position += i - position;

    return sb.toString().hashCode();

  }

  /**
   * {@inheritDoc}
   */
  public boolean hasNextShingle()
  {
    if (lineArray == null)
      return false;

    if (lineArray.length - position >= groupSize)
    {
      return true;
    }

    return false;

  }

  /**
   * {@inheritDoc}
   */
  public boolean hasLast()
  {
    if (hasNextShingle())
      throw new IllegalStateException();

    if (buffer != null)
    {
      return true;

    }

    return false;

  }

  /**
   * {@inheritDoc}
   */
  public int lastShingle()
  {

    StringBuilder sb = new StringBuilder();

    if (this.buffer != null)
    {
      for (int i = bufferPosition; i < buffer.length; i++)
      {
        sb.append(buffer[i]);

      }

      sb.toString().hashCode();
    }

    throw new NoSuchElementException();
  }

}
