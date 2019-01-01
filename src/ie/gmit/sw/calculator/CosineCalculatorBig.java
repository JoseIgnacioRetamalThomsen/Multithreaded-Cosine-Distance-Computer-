package ie.gmit.sw.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.CosineDistanceResult;
import ie.gmit.sw.counting.CounterMap;

public class CosineCalculatorBig implements Callable<CosineDistanceResult>
{

    private CounterMap<Integer> map1;
    private CounterMap<Integer> map2;

    // Future<Double> sumQueryMapFuture;
    // Future<Double> sumSubjectMapFuture;
    // Future<Double> multFuture;
    Future<BigDecimal> sumQueryMapFuture;
    Future<BigDecimal> sumSubjectMapFuture;
    Future<BigDecimal> multFuture;

    ExecutorService executor = Executors.newFixedThreadPool(3);

    public CosineCalculatorBig(CounterMap<Integer> map1, CounterMap<Integer> map2)
    {
        super();
        this.map1 = map1;
        this.map2 = map2;
    }

    @Override
    public CosineDistanceResult call() throws Exception
    {

        sumQueryMapFuture = executor.submit(new Callable<BigDecimal>()
        {

            @Override
            public BigDecimal call() throws Exception
            {
                // double sum = 0;
                BigDecimal sum = new BigDecimal(0);
                for (int i : map1.getCountAll())
                    // sum += i * i;
                    sum = sum.add(new BigDecimal(i).pow(2));
                return sum;
            }

        });

        sumSubjectMapFuture = executor.submit(new Callable<BigDecimal>()
        {

            @Override
            public BigDecimal call() throws Exception
            {
                // double sum = 0;
                BigDecimal sum = new BigDecimal(0);
                for (int i : map2.getCountAll())
                    // sum += i * i;
                    sum = sum.add(new BigDecimal(i).pow(2));
                return sum;
            }

        });

        multFuture = executor.submit(new Callable<BigDecimal>()
        {

            @Override
            public BigDecimal call() throws Exception
            {
                // double mult = 0;
                BigDecimal sum = new BigDecimal(0);
                for (Integer l : map1.getEntrySet())
                    if (map2.haveCount(l))
                        // mult += (map1.getCount(l) * map2.getCount(l));
                        sum = sum.add(new BigDecimal(map1.getCount(l)).multiply(new BigDecimal(map2.getCount(l))));

                return sum;

            }

        });

        executor.shutdown();

        DecimalFormat f = new DecimalFormat("##.00");

        MathContext mc = new MathContext(4, RoundingMode.HALF_UP);

        BigDecimal num1 = sumQueryMapFuture.get();
        BigDecimal num2 = sumSubjectMapFuture.get();

        num1 = CosineCalculator.sqrt(num1, 10);
        num2 = CosineCalculator.sqrt(num2, 10);

        num1 = num1.multiply(num2);
        BigDecimal num3 = multFuture.get();
        // System.out.println(num1);
        // System.out.println(Double.MAX_VALUE );
        BigDecimal res = num3.divide(num1, mc);
        // System.out.println(res);

        // double sumQueryMap = sumQueryMapFuture.get();
        // double sumSubjectMap = sumSubjectMapFuture.get();
        // sumQueryMap = Math.sqrt(sumQueryMap);
        // sumSubjectMap = Math.sqrt(sumSubjectMap);
        // double result = Double.parseDouble(f.format(multFuture.get())) /
        // ((sumQueryMap * sumSubjectMap));

        // return new CosineDistanceResult(map2.getName(), result);
        System.out.println(res.doubleValue());
        return new CosineDistanceResult(map2.getName(), res.doubleValue());
    }

    
    public static BigDecimal sqrt(BigDecimal value) {
        BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
        return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
    }
    
    public static BigDecimal sqrt1(BigDecimal in, int scale)
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
