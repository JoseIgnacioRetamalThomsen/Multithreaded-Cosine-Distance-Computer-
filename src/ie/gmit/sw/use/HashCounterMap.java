/**
 * 
 */
package ie.gmit.sw.use;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * 
 * 
 * 
 * @author Jose I. Retamal
 *
 */
public class HashCounterMap<K> implements  CounterMap<K>, Serializable, Cloneable
{

  /**
   * 
   */
  private static final long serialVersionUID = 3788122961081619444L;
 
  private HashMap<K, Integer> countingMap = null;

  public HashCounterMap()
  {
    super();
    this.countingMap = new HashMap<K, Integer>();
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Return size of internal HashMap<\p>
   */
  public int size()
  {
    return countingMap.size();
  }

  /**
   * {@inheritDoc}
   */
  public int count(K object) throws ClassCastException, NullPointerException
  {

    Integer old = countingMap.get(object);

    return countingMap.get(object) == null ? countingMap.put(object,  1) : countingMap.put(object, old + 1) + 1;

  }

  /**
   * {@inheritDoc}
   */
  public int getCount(K object) throws ClassCastException, NullPointerException
  {
    return countingMap.get(object);
  }

  /**
   * {@inheritDoc}
   */
  public Set<K> getEntrySet()
  {
    return countingMap.keySet();
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Integer> getCountAll()
  {
    return countingMap.values();
  }

  /**
   * {@inheritDoc}
   */
  public boolean haveCount(K object) throws ClassCastException, NullPointerException
  {
    return countingMap.containsValue(object);
  }

  /**
   * {@inheritDoc}
   */
  public int remove(K object) throws ClassCastException, NullPointerException
  {
    Integer value = countingMap.remove(object);

    return value == null ? 0 : value;
  }

  /**
   * {@inheritDoc}
   */
  public void clear() throws UnsupportedOperationException
  {
    countingMap.clear();

  }
  
}
