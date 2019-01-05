/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw.base;

import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import ie.gmit.sw.data.CosineDistanceResult;

/**
 * Calculate cosine distance of 2 {@code CounterMap<Integer>} , works fine with not a big
 * amount of shingles.
 * <p>
 * Must be run in a executor and will return the calculated cosine distance as
 * {@code Future<CosineDistanceResult>>}.
 * <p>
 * The amount of shingles from a file is related to the size of the shingles and the size
 * of the file, for example, a file of medium size can have a big amount of shingles if we
 * use k-mers shingle of size 1 character.
 * <p>
 * One thread calculate the dot product and other 2 calculate the magnitude
 * 
 * @author Jose I. Retamal
 *
 */
public class CosineCalculator extends AbstractCosineCalculator
{
    private CounterMap<Integer> map1;// input map
    private CounterMap<Integer> map2;// input map
    Future<Double> sumQueryMapFuture;// for internal thread
    Future<Double> sumSubjectMapFuture;// for internal thread
    Future<Double> multFuture;// for internal thread
    ExecutorService executor = Executors.newFixedThreadPool(3);// runs the 3 internal threads

    /**
     * Create a CosineCalculator with all parameters needed, then the object must be executed
     * in a thread executor for get the result as future.
     * 
     * @param map1 first map to calculate cosine distance, normally query file map
     * @param map2 second map to calculate cosine distance, normally subject directory file
     */
    public CosineCalculator(CounterMap<Integer> map1, CounterMap<Integer> map2)
    {
        super();
        this.map1 = map1;
        this.map2 = map2;
    }

    /**
     * Spawn the internal thread that calculate the dot product and magnitude sum, then main
     * thread calculate and return result. Object must be executed in a ExecutorService and
     * will return the cosine distance as future.
     * <p>
     * Use anonymous inner classes for convenience.
     * 
     * @return the {@code Future<CosineDistanceResult>} calculated cosine distance
     */
    public CosineDistanceResult call() throws Exception
    {
        // magnitude of map 1
        sumQueryMapFuture = executor.submit(new Callable<Double>()
        {
            public Double call() throws Exception
            {
                double sum = 0;
                for (int i : map1.getCountAll())
                    sum += i * i;

                return sum;
            }

        });
        // magnitude of map 2
        sumSubjectMapFuture = executor.submit(new Callable<Double>()
        {

            @Override
            public Double call() throws Exception
            {
                double sum = 0;

                for (int i : map2.getCountAll())
                    sum += i * i;

                return sum;
            }

        });
        // dot product
        multFuture = executor.submit(new Callable<Double>()
        {

            @Override
            public Double call() throws Exception
            {
                double mult = 0;

                for (Integer l : map1.getEntrySet())
                    if (map2.haveCount(l))
                        mult += (map1.getCount(l) * map2.getCount(l));

                return mult;

            }

        });

        executor.shutdown();// will shout down after finish calculations

        DecimalFormat f = new DecimalFormat("##.00");
        // get and finish to calculate magnitudes
        double sumQueryMap = sumQueryMapFuture.get();
        double sumSubjectMap = sumSubjectMapFuture.get();
        sumQueryMap = Math.sqrt(sumQueryMap);
        sumSubjectMap = Math.sqrt(sumSubjectMap);
        // calculate cosine distance
        double result = Double.parseDouble(f.format(multFuture.get())) / ((sumQueryMap * sumSubjectMap));
        // create and return CosineDistanceResult
        return new CosineDistanceResult(map2.getName(), map1.getName(), result);

    }

}
