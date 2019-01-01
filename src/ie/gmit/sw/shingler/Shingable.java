/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
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
   * Add new line, new line can only be added after all shingles are taken from
   * last or is the first.
   * <p>
   * if there are still shingles to be taken will return false and line will not
   * be added.
   * </p>
   * 
   * @param line new line to add
   * @return true if was added, false if there are still shingles to take from
   *         last
   */
  boolean addLine(CharSequence line);

  /**
   * Used for get the next shingle of the line.
   * <p>
   * There should be check if there is a next shingle using
   * {@code hasNextShingle()} before call. Throws {@code NoSuchElementException}
   * if there is no next shingle.
   * </p>
   * 
   * @return the hash code of the next shingle
   * @throws NoSuchElementException if there in no shingle to return
   */
  int nextShingle();

  /**
   * Returns true if has another shingle to return from line
   * 
   * @return true if this shingable has another shingle to return
   */
  boolean hasNextShingle();

  /**
   * return true if this shingable have a last shingle after return all next
   * shingles, last shingle would be of a smaller size than the normal ones.
   * <p>
   * Should be called only after all shingles have been taken(hasNextShingle() =
   * false) , if not should throw a exception.
   * </p>
   * <p>
   * This should be use for check if there is a last shingle after all lines have
   * been added and all shingle have been taken.
   * </p>
   * 
   * @return true if has a smaller size last shingle
   * @throws IllegalStateException if there is another shingle
   */
  boolean hasLast();

  /**
   * Return the last shingle, should be use after checking if there is a last.
   * <p>
   * Should be called only after all shingles have been taken(hasNextShingle() =
   * false) , if not should throw a exception.
   * </p>
   * <p>
   * Should be called after check that there is a last shingle(hasLast()=true) if
   * there is not last and is call should throw a exception.
   * </p>
   * shingle.
   * 
   * @return the last smaller size shingle.
   * @throws IllegalStateException  if there is another shingle
   * @throws NoSuchElementException if there is not a last shingle
   **/
  int lastShingle();

}
