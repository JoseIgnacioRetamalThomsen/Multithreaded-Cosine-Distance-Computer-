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
 * Base class for calculate cosine distance, set the return type
 * {@code Callable<CosineDistanceResult>}.
 * <p>
 * So any implentation should implement callable with that return type, only requirement
 * for work fine with the API.
 * </p>
 * 
 * 
 * @author Jose I. Retamal
 *
 */
public abstract class AbstractCosineCalculator implements Callable<CosineDistanceResult>
{
}
