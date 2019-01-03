/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw.base;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.data.CosineDistanceResult;

/**
 * Calculate cosine distance of two maps for big number of entrances.
 * 
 * @author Jose I. Retamal
 *
 */
public class CosineCalculatorBig extends AbstractCosineCalculator
{

    private CounterMap<Integer> map1;
    private CounterMap<Integer> map2;
    private Future<BigDecimal> sumQueryMapFuture;
    private Future<BigDecimal> sumSubjectMapFuture;
    private Future<BigDecimal> multFuture;
    private ExecutorService executor = Executors.newFixedThreadPool(3);

    /**
     * 
     * @param map1 first map, usually query file map
     * @param map2 second map , usually subject directory file
     */
    public CosineCalculatorBig(CounterMap<Integer> map1, CounterMap<Integer> map2)
    {
        super();
        this.map1 = map1;
        this.map2 = map2;
    }

    /**
     * Use 3 threads, one for calculate each sum.
     * <p>
     * Use {@code Callable} with anonymous inner class for convenience
     * </p>
     */
    public CosineDistanceResult call() throws Exception
    {

        //submit threads to executor
        sumQueryMapFuture = executor.submit(new Callable<BigDecimal>()
        {
            public BigDecimal call() throws Exception
            {
                BigDecimal sum = new BigDecimal(0);
                for (int i : map1.getCountAll())
                    sum = sum.add(new BigDecimal(i).pow(2));
                return sum;
                
            }

        });

        sumSubjectMapFuture = executor.submit(new Callable<BigDecimal>()
        {
            public BigDecimal call() throws Exception
            {
                BigDecimal sum = new BigDecimal(0);
                for (int i : map2.getCountAll())
                    sum = sum.add(new BigDecimal(i).pow(2));
                return sum;
            }

        });

        multFuture = executor.submit(new Callable<BigDecimal>()
        {
            public BigDecimal call() throws Exception
            {
                BigDecimal sum = new BigDecimal(0);
                for (Integer l : map1.getEntrySet())
                    if (map2.haveCount(l))
                        sum = sum.add(new BigDecimal(map1.getCount(l)).multiply(new BigDecimal(map2.getCount(l))));
                return sum;

            }

        });

        executor.shutdown();

        //calculate  and then return cosine distance
        MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
        BigDecimal num1 = sumQueryMapFuture.get();
        BigDecimal num2 = sumSubjectMapFuture.get();
        num1 = sqrt1(num1);// , 10);
        num2 = sqrt1(num2);// , 10);
        num1 = num1.multiply(num2);
        BigDecimal num3 = multFuture.get();
        BigDecimal res = num3.divide(num1, mc);
    
        return new CosineDistanceResult(map2.getName(),map1.getName(), res.doubleValue());

    }

    /*
     * From
     * https://stackoverflow.com/questions/13649703/square-root-of-bigdecimal-in-
     * java Don't give BigDecimal precision but is not really needed.
     */
    private BigDecimal sqrt1(BigDecimal value)
    {
        BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
        return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
    }

    private BigDecimal sqrt(BigDecimal in, int scale)
    {
        BigDecimal sqrt = new BigDecimal(1);
        sqrt.setScale(scale + 3, RoundingMode.FLOOR);
        BigDecimal store = new BigDecimal(in.toString());
        boolean first = true;
        do
        {
            if (!first)
            {
                store = new BigDecimal(sqrt.toString());
            } else
                first = false;
            store.setScale(scale + 3, RoundingMode.FLOOR);
            sqrt = in.divide(store, scale + 3, RoundingMode.FLOOR).add(store).divide(BigDecimal.valueOf(2), scale + 3,
                    RoundingMode.FLOOR);
        } while (!store.equals(sqrt));

        return sqrt.setScale(scale, RoundingMode.FLOOR);

    }

}
