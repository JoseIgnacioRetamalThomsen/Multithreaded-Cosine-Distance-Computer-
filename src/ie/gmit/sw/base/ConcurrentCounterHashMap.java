package ie.gmit.sw.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concurrent implementation of CounterMap.
 * 
 * @author Jose I. Retamal
 *
 * @param <K> the type of object counted by this counter
 */
public class ConcurrentCounterHashMap<K> implements Cloneable, Serializable, CounterMap<K>
{

  /*
   * Use a inner ConcurrentHashMap 
   */
  private static final long serialVersionUID = -5419016465194643642L;

  private String name;
  
  private ConcurrentHashMap<K, Integer> countingMap = null;

  /**
   * Create new empty.
   */
  public ConcurrentCounterHashMap()
  {
    countingMap = new ConcurrentHashMap<>();

  }

  /**
   * Create new empty map with initial capacity of specified number.
   * 
   * @param initialCapacity 
   * @throws IllegalArgumentException if the initial capacity of elements is
   *                                  negative.
   */
  public ConcurrentCounterHashMap(int initialCapacity) 
  {
    countingMap = new ConcurrentHashMap<>(initialCapacity);

  }

  /**
   * 
   * @param initialCapacity initial capacity of hashmap
   * @param loadFactor level of inner hashpmap for double capacity
   */
  public ConcurrentCounterHashMap(int initialCapacity, float loadFactor)
  {
    countingMap = new ConcurrentHashMap<>(initialCapacity, loadFactor);

  }

  /**
   * 
   * @param initialCapacity initial capacity of Hashmap
   * @param loadFactor 
   * @param concurrencyLevel number of thread that can access map
   */
  public ConcurrentCounterHashMap(int initialCapacity, float loadFactor, int concurrencyLevel)
  {
    countingMap = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);

  }

  /**
   * {@inheritDoc}
   * 
   */
  public int size()
  {
    return countingMap.size();

  }

  /**
   * {@inheritDoc}
   * 
   */
  public int count(K object) 
  {
    return countingMap.compute(object, (key, value) -> (value == null) ? 1 : value + 1);

  }

  /**
   * {@inheritDoc}
   * 
   */
  public int getCount(K object) 
  {
    return countingMap.get(object);

  }

  /**
   * {@inheritDoc}
   * 
   */
  public Set<K> getEntrySet()
  {
    return countingMap.keySet();

  }

  /**
   * {@inheritDoc}
   * 
   */
  public Collection<Integer> getCountAll()
  {
    return countingMap.values();

  }

  /**
   * {@inheritDoc}
   * 
   */
  public boolean haveCount(K object) 
  {
    return countingMap.containsKey(object);
    
  }

  /**
   * {@inheritDoc}
   * 
   */
  public synchronized int remove(K object)
  {
    Integer value = countingMap.remove(object);

    return value == null ? 0 : value;

  }

  /**
   * {@inheritDoc}
   * 
   */
  public void clear() 
  {
    countingMap.clear();

  }

  @Override
  public void setName(String name)
  {
    this.name = name;
    
  }

  @Override
  public String getName()
  {
    return this.name;
    
  }

}
