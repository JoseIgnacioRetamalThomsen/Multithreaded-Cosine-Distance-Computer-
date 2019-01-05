/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw.base;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Count shingles for one file, use for calculate counter map for query file.
 * 
 * @author Jose I. Retamal
 *
 */
public class CountOne
{
    private int queuuSize = 1000;// Parser queue size
    private int shingleSize;
    private ShingleType shingleType;
    private ExecutorService mapBuilderExecutor = Executors.newFixedThreadPool(1);
    private File file;
    private BlockingQueue<Number> queue;
    private FileShingleParser parser;
    private int numberOfThread = 10;

    /**
     * Create object with shingle size and type setting, file must be set before run.
     * 
     * @param shingleSize size of shingles
     * @param shingleType type of shingles
     */
    public CountOne(int shingleSize, ShingleType shingleType)
    {
        this.shingleSize = shingleSize;
        this.shingleType = shingleType;
        queue = new ArrayBlockingQueue<>(queuuSize);

    }

    /**
     * Set file , must be done before submit thread to a Executor service
     * 
     * @param file file to count shingles
     * @return true if the parameter file is fine
     */
    public boolean setFile(File file)
    {
        this.file = file;
        parser = new FileShingleParser(queue, shingleSize, shingleType, numberOfThread);
        return parser.setFile(this.file);

    }

    /**
     * Calculate cosine distance using FileShingleParser and MapBuilder.
     * 
     * @return future of countermap
     * @throws RuntimeException if file is not set
     */
    public Future<CounterMap<Integer>> calculate()
    {

        if (!parser.isFileSet())
            throw new RuntimeException();

        new Thread(parser).start();

        // create map builder with internal queue and the name of the file
        MapBuilder mapBuilder = new MutlipleThreadMapBuilder(queue, file.toString(), numberOfThread);

        Future<CounterMap<Integer>> result = mapBuilderExecutor.submit(mapBuilder);

        mapBuilderExecutor.shutdown();

        return result;

    }
}
