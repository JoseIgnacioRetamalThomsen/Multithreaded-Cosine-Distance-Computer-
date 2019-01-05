/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */
package ie.gmit.sw.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Not concurrent version of CounterMap, use a hash map for map objects with they count.
 * 
 * 
 * @author Jose I. Retamal
 *
 */
public class HashCounterMap<K> implements CounterMap<K>, Serializable, Cloneable
{

    private static final long serialVersionUID = 3788122961081619444L;
    private HashMap<K, Integer> countingMap = null;
    private String fileName;

    /**
     * Create an instance of this object
     */
    public HashCounterMap()
    {
        super();
        this.countingMap = new HashMap<K, Integer>();
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Return size of internal HashMap
     * </p>
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

        return countingMap.get(object) == null ? countingMap.put(object, 1) : countingMap.put(object, old + 1) + 1;

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

    /**
     * {@inheritDoc}
     */
    public void setName(String name)
    {
        this.fileName = name;

    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return this.fileName;
    }

}
