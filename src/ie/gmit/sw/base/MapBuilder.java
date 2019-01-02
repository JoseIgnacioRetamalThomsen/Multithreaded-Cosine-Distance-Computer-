/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */

package ie.gmit.sw.base;

import java.util.concurrent.Callable;

/**
 * Base class for map builder.
 * <p>
 * All implementations must implement call that return a
 * {@code CounterMap<Integer>}
 * </p>
 * 
 * @author Jose I. Retamal
 *
 */
public abstract class MapBuilder implements Callable<CounterMap<Integer>>{}
