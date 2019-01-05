/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */
package ie.gmit.sw.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ie.gmit.sw.poison.NumberPoison;

/**
 * Build map of shingles from a file using a single thread, good enough for the design
 * because CounterMaps can counts one shingle at a time.
 * <p>
 * Use a input of Number that are the hash code of the shingles(they are basically used as
 * shingles), count appearance of each number in the queue using a
 * {@code CounterMap<Integer>}.
 * </P>
 * 
 * @author Jose I. Retamal
 *
 */
public class SingleThreadMapBuilder extends MapBuilder
{

    private final BlockingQueue<Number> queue;

    private CounterMap<Integer> countingMap = new ConcurrentCounterHashMap<>();

    /**
     * Create a {@code Callable} {@code SingleThreadMapBuilder} that counts shingles from
     * queue.
     * <p>
     * Must be executed in a {@code Executr}
     * </p>
     * 
     * @param queue    input queue of integer shingles
     * @param fileName name of the file from where created the shingles
     */
    public SingleThreadMapBuilder(BlockingQueue<Number> queue, String fileName)
    {
        super();
        this.queue = queue;
        this.countingMap.setName(fileName);
    }

    /**
     * take from queue use counting map for count then return the counted map.
     * 
     * @return {@code Future} of the counter map
     */
    public CounterMap<Integer> call() throws Exception
    {

        while (true)
        {
            Number n = queue.take();

            if (!(n instanceof NumberPoison))
            {
                countingMap.count((Integer) n);

            } else
            {

                break;
            }

        }

        return countingMap;
    }

}
