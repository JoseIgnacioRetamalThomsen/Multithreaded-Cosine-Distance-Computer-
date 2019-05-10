/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */

package ie.gmit.sw.base;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import ie.gmit.sw.base.poison.MapFuturePoison;

/**
 * Produce {@code CounterMap<Integer>}(which are a count of the selected type of shingles
 * of each file) and place then in a output queue, take a array of File as parameter, for
 * each file use a {@code FileShinglerParser} and {@code MapBuilder} for create the maps .
 * 
 * 
 * 
 * @author Jose I. Retamal
 *
 */
public class Counter implements Runnable
{

    /**
     * out put queue of {@code CounterMap}.
     */
    private BlockingQueue<Future<CounterMap<Integer>>> maps;
    /**
     * array of files for calculate cosine distance on each
     */
    private File[] files;
    /**
     * subject directory path
     */
    private File subjectDir;
    /**
     * number of thread for cosine calculator
     */
    private int threadNumber = 10;
    /**
     * for execute parser file threads.
     */
    private ExecutorService parcerExecutor;
    /**
     * for execute map building threads.
     */
    private ExecutorService mapBuilderExecutor;
    /**
     * Size of shingles.
     */
    private int shingleSize;
    /**
     * Types of shingles
     */
    private ShingleType shingleType;
    /**
     * number of thread for build maps.
     */
    private int numberOfBuildingThreads = 10;
    /**
     * limit memory usage
     */
    private int fileParserQueueSize =1000;

    /**
     * Create object with all necessary parameters.
     * 
     * @param queue       for put the calculated counter maps
     * @param files       files for calculate counter maps
     * @param subjectDir  directory of the subject files
     * @param shingleSize size of the shingles
     * @param shingleType type of shingles k-mers or group
     */
    public Counter(BlockingQueue<Future<CounterMap<Integer>>> queue, File[] files, File subjectDir, int shingleSize,
            ShingleType shingleType)
    {
        maps = queue;
        this.files = files;
        this.subjectDir = subjectDir;
        parcerExecutor = Executors.newFixedThreadPool(threadNumber);
        mapBuilderExecutor = Executors.newFixedThreadPool(threadNumber);
        this.shingleSize = shingleSize;
        this.shingleType = shingleType;
    }

    /**
     * Create a {@code FileShinglerParser} and {@code MapBuilder} for each file.
     */
    public void run()
    {

        // for each file we create a {@code FileShinglerParser} and {@code MapBuilder}
        for (File file : files)
        {

            BlockingQueue<Number> fileParseQueue = new LinkedBlockingQueue<>(fileParserQueueSize);

            FileShingleParser parcer = new FileShingleParser(fileParseQueue, shingleSize, shingleType,
                    numberOfBuildingThreads);

            parcer.setFile(file);

            parcerExecutor.execute(parcer);

            MapBuilder mapBuilder = new MutlipleThreadMapBuilder(fileParseQueue, file.toString(),
                    numberOfBuildingThreads);

            try
            {
                // puts future in the outpuit queue
                maps.put(mapBuilderExecutor.submit(mapBuilder));

            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        try
        {

            maps.put(new MapFuturePoison<Integer>());

        } catch (InterruptedException e)
        {
            // nothing to do...
            e.printStackTrace();
        } finally
        {
            parcerExecutor.shutdown();
            mapBuilderExecutor.shutdown();

        }

    }

}
