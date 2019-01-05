package ie.gmit.sw.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concurrent implementation of CounterMap using a HashMap. Allow all operations needed
 * for count shingle.
 * <p>
 * Basically a hash map that link objects with his count, when a new object is counted,
 * his mapped value will be increased by 1.
 * </p>
 * <p>
 * When new objects are added they will be keep on a key set and his count set to 1.
 * </p>
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
     * @param initialCapacity the initial capacity of the map.
     * @throws IllegalArgumentException if the initial capacity of elements is negative.
     */
    public ConcurrentCounterHashMap(int initialCapacity)
    {
        countingMap = new ConcurrentHashMap<>(initialCapacity);

    }

    /**
     * 
     * @param initialCapacity initial capacity of hashmap
     * @param loadFactor      level of inner hashpmap for double capacity
     */
    public ConcurrentCounterHashMap(int initialCapacity, float loadFactor)
    {
        countingMap = new ConcurrentHashMap<>(initialCapacity, loadFactor);

    }

    /**
     * 
     * @param initialCapacity  initial capacity of Hashmap
     * @param loadFactor initial table density
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

    /**
     * Set name of this CounterHashMap , use for know what file is counting
     */
    public void setName(String name)
    {
        this.name = name;

    }

    /**
     * // * Retrieve the assigned name of this CounterHashMap
     */
    public String getName()
    {
        return this.name;

    }

}
