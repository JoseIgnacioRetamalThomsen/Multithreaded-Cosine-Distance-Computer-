/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw.base;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.base.poison.PosionCounterMap;
import ie.gmit.sw.data.CosineDistanceResult;

/**
 * Calculate the cosine distance of several {@code CounterMap<Integer>} , compare one
 * {@code CounterMap<Integer>} against a queue of them, put results on another queue.
 * <p>
 * Spawn a {@code CosineCalculator} thread for each couple of maps, then put the
 * {@code CosineDistanceResult} in the output queue. Results are in future for and when
 * they are reayd can be show.
 * 
 * @author Jose I. Retamal
 *
 */
public class Calculator implements Runnable
{

    private BlockingQueue<Future<CounterMap<Integer>>> maps;// input query
    private BlockingQueue<Future<CosineDistanceResult>> answers;// results query
    private CounterMap<Integer> queryFile; // query file map
    private ExecutorService executor;// runs CosineCalculator
    private int poolSize;
    private CosineCalculatorType cosineType;

    /**
     * Create Calculator with all parameters needed, after use this constructor object will be
     * ready to run.
     * 
     * @param maps       input query of {@code CounterMap<Integer>}
     * @param answers    output query of {@code CosineDistanceResult}, calculated results of
     *                   query file again queue
     * @param queryFile  query file to compare again input queue
     * @param poolSize   size of executor, number of {@code CosineDistanceResult} threads that
     *                   * run at a time
     * @param cosineType type cosine calculator to use, can be normal or big, normal is faster
     *                   but can calculate whit smaller amount if shingles.
     */
    public Calculator(BlockingQueue<Future<CounterMap<Integer>>> maps,
            BlockingQueue<Future<CosineDistanceResult>> answers, CounterMap<Integer> queryFile, int poolSize,
            CosineCalculatorType cosineType)
    {
        super();
        this.maps = maps;
        this.answers = answers;
        this.queryFile = queryFile;
        this.poolSize = poolSize;
        this.cosineType = cosineType;
        executor = Executors.newFixedThreadPool(this.poolSize);
    }

    /**
     * Take maps from input queue , calculate cosine distance against query file of those.
     * Then place results in result query.
     * <P>
     * submit {@code CosineCalculator} thread to executor and put results to answers query
     * </p>
     * <p>
     * Stops when get {@code PosionCounterMap} object in input queue
     * </p>
     * 
     */
    public void run()
    {
        while (true)
        {
            try
            {
                CounterMap<Integer> temp = null;
                try
                {
                    temp = maps.take().get();// wait until map have been counted

                } catch (ExecutionException e)
                {
                    // not much we can do if executor fails
                    e.printStackTrace();
                }
                if (!(temp instanceof PosionCounterMap))
                {
                    // use normal or big calculator
                    switch (cosineType)
                    {
                    case Normal:
                        answers.put(executor.submit(new CosineCalculator(queryFile, temp)));// put future in answers
                                                                                            // queue
                        break;
                    case Big:
                        answers.put(executor.submit(new CosineCalculatorBig(queryFile, temp)));// put future in answers
                                                                                               // queue
                        break;
                    }

                } else
                {
                    // stop because got poison
                    break;

                }
            } catch (InterruptedException e)
            {
                // nothing to do just throw unexpected exception
                e.printStackTrace();
            }
        }

        executor.shutdown();

    }

}
