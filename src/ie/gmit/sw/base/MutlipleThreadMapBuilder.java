/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */
package ie.gmit.sw.base;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.base.poison.NumberPoison;

/**
 * Builds map using multiple threads, take number shingles from a FileShingleParser and
 * then several thread count those shingles using a CountingMap.
 * <p>
 * Return a Future of MapBuilder
 * </p>
 * 
 * @author Jose I. Retamal
 *
 */
public class MutlipleThreadMapBuilder extends MapBuilder
{
    /**
     * input shingle number queue
     */
    private final BlockingQueue<Number> queue;
    /**
     * CounterMap for count and return
     */
    CounterMap<Integer> map = new ConcurrentCounterHashMap<>();
    /**
     * execute the number of thread
     */
    ExecutorService es;
    /**
     * number of thread that count on map
     */
    private int threadNumber;

    /**
     * Create a object with all the parameters needed to execute.
     * 
     * @param queue        input queue of type numbers which are the hash code of shingles
     * @param fileName     the name of the file from where shingles come from
     * @param threadNumber number of thread that will count on the map
     */
    public MutlipleThreadMapBuilder(BlockingQueue<Number> queue, String fileName, int threadNumber)
    {
        super();
        this.queue = queue;
        this.map.setName(fileName);
        this.threadNumber = threadNumber;
        es = Executors.newFixedThreadPool(this.threadNumber);
    }

    /**
     * Spawn several MapPutter threads which take number from input queue then count those in
     * the map, return the map when finish.
     * <p>
     * Use private inner class MapPutter
     */
    public CounterMap<Integer> call() throws Exception
    {
        for (int i = 0; i < threadNumber; i++)
        {
            es.execute(new MapPutter());
        }

        // wait until all thread finish
        es.shutdown();
        es.awaitTermination(2000, TimeUnit.SECONDS);

        return map;

    }

    /**
     * Single thread that will take from queue and count on the map.
     * 
     * @author Jose I. Retamal
     *
     */
    private class MapPutter implements Runnable
    {

        public void run()
        {
            while (true)
            {
                Number n = null;

                try
                {
                    n = queue.take();
                } catch (InterruptedException e)
                {
                    // nothing to do
                    e.printStackTrace();
                }

                if (!(n instanceof NumberPoison))
                {
                    map.count(new Integer((int) n));

                } else
                {
                    break;

                }

            }

        }

    }

}
