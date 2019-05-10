/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw.base.poison;

import ie.gmit.sw.data.CosineDistanceResult;

/**
 * Poison for {@code CosineDistanceResult} queue.
 * 
 * @author Jose I. Retamal
 *
 */
public class CosineDistanceResultPoison extends CosineDistanceResult
{
    private static final long serialVersionUID = -6088092725656499961L;

    public CosineDistanceResultPoison()
    {
        super(null, null, null);
    }

}
