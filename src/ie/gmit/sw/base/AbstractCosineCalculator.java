/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */

package ie.gmit.sw.base;

import java.util.concurrent.Callable;

import ie.gmit.sw.data.CosineDistanceResult;

/**
 * Base clase for calculate cosine distance, set return type :
 * {@code CosineDistanceResult} of callable
 * 
 * @author Jose I. Retamal
 *
 */
public abstract class AbstractCosineCalculator implements Callable<CosineDistanceResult>{}
