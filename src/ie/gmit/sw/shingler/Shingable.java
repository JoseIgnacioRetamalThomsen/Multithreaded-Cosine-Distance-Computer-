/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw.shingler;

/**
 * 
 * Object intended for take lines of input from a file, separate them into
 * strings shingles and return the hash code of those shingles.
 * <p>
 * A new line can be entered when all shingles have been produced from last line
 * or is the first.
 * </p>
 * <p>
 * Each line must be threaded as is from a full text file, so if there are
 * characters left after all shingles have been produced, they must be added to
 * the next line. Or if it is the last line they should be returned as last
 * shingle.
 * </p>
 * 
 * @author Jose I. Retamal
 *
 */
public interface Shingable
{
  /**
   * 
   * @param line
   * @return
   */
  boolean addLine(CharSequence line);

  int nextShingle();

  boolean hasNextShingle();

  boolean hasLast();

  int lastShingle();
}
