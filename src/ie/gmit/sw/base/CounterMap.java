/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */

package ie.gmit.sw.base;

import java.util.Collection;
import java.util.Set;

/**
 * An object that counts objects of type k. Store each object and the map them
 * to a integer count.
 * <p>
 * Each time a new element is counted it will be added to a set of all counted
 * elements and mapped to integer value, if the element is in the set the
 * counted value will be increased.
 * <p>
 * 
 * @author Jose I. Retamal
 * 
 *
 * @param <K> the type of object counted by this counter
 */

public interface CounterMap<K> extends Identifiable
{

  /**
   * Return the number of objects that are being counter.
   * 
   * @return number of counted objects
   */
  int size();

  /**
   * Increase the count of the object. If the object don't exist, add it to
   * counted object set and set his count to 1.
   * 
   * @param object to be counted
   * @return the old count of the object.
   * @throws ClassCastException   if the object is of an inappropriate type for
   *                              this counter
   * @throws NullPointerException if the specified object to count is null
   */
  int count(K object);

  /**
   * Return the count of the object.
   * 
   * @param object to get count of
   * @return count of the object
   * @throws ClassCastException   if the object is of an inappropriate type for
   *                              this counter
   * @throws NullPointerException if the specified object to get count is null
   */
  int getCount(K object);

  /**
   * Return a set with all the counted objects.
   * 
   * @return set of all counted objects
   */
  Set<K> getEntrySet();

  /**
   * Return all counted values in a collection of integers .
   * 
   * @return all count values
   */
  Collection<Integer> getCountAll();

  /**
   * Return true is the specified object have been counted, mean is in the set of
   * counted objects.
   * 
   * @param object check if this object has been counted
   * @return true if the object has been counted
   * @throws ClassCastException   if the object is of an inappropriate type for
   *                              this counter
   * @throws NullPointerException if the specified object to get count is null
   */
  boolean haveCount(K object);

  /**
   * Remove an object from counter, after removed same object should return a
   * count of 0.
   * 
   * @param object to remove from counter
   * @return count of the object.
   * @throws ClassCastException   if the object is of an inappropriate type for
   *                              this counter
   * @throws NullPointerException if the specified object to get count is null
   */
  int remove(K object);

  /**
   * Clear all counted objects(Optional).
   * 
   * @throws UnsupportedOperationException
   */
  void clear();

}
