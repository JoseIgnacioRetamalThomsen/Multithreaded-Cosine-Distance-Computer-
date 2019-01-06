/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */

package ie.gmit.sw.ui;

import java.util.concurrent.BlockingQueue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import ie.gmit.sw.base.Calculator;
import ie.gmit.sw.base.Counter;
import ie.gmit.sw.base.CounterMap;
import ie.gmit.sw.data.CosineDistanceResult;
import ie.gmit.sw.data.ServiceData;
import javafx.concurrent.Task;
import javafx.application.Platform;

/**
 * Puts all class together for calculate cosine distance of a local query file against a
 * local subject directory. 
 * <p>
 * Place results in a {@code ObservableList} which show
 * results in {@code MainWindows}, all data needed come in {@code Servicedata} object and
 * the query {@code File} is entered as parameter.
 *  
 * 
 * @author Jose I. Retamal
 *
 */
public class LocalService extends Service<String>
{

    /**
     * list of results that shows in MainWindow
     */
    private ObservableList<CosineDistanceResult> resultsObservable; 
    /**
     * user settings and files names
     */
    private ServiceData serviceData = new ServiceData(); 
    /**
     * queue that get maps from Counter
     */
    private BlockingQueue<Future<CounterMap<Integer>>> maps = new LinkedBlockingQueue<>(1000);
    /**
     *  queue that get results from Calculator
     */
    private BlockingQueue<Future<CosineDistanceResult>> results = new LinkedBlockingQueue<Future<CosineDistanceResult>>(
            1000);
    /**
     *  reference to MainWindow
     */
    private MainWindow mainWindow;
    /**
     * query file map
     */
    Future<CounterMap<Integer>> queryMap;

    /**
     * Create a {@code LocalService} object with all parameters needed to run.
     * 
     * @param serviceData       user settings and file names
     * @param resultsObservable list of results that shows in {@code MainWindows}
     * @param mainWindow        reference to {@code MainWindows}
     * @param queryMap          query file map
     */
    public LocalService(ServiceData serviceData, ObservableList<CosineDistanceResult> resultsObservable,
            MainWindow mainWindow, Future<CounterMap<Integer>> queryMap)
    {
        this.serviceData = serviceData;
        this.resultsObservable = resultsObservable;
        this.mainWindow = mainWindow;
        this.queryMap = queryMap;
    }

    /**
     * Create all classes and perform all work, return the total time that take to perform the
     * work.
     * 
     * @return {@codeTask<String>} time to finish in seconds as string
     */
    protected Task<String> createTask()
    {
        //Anonymous task for return
        return new Task<String>()
        {
            public String call()
            {
                // count time
                long start = System.nanoTime();

                // subject files
                File[] files = serviceData.getFiles();
                int totalFiles = files.length;

                // create and start counter thread with maps queue
                Counter mba = new Counter(maps, files, serviceData.getSubjectDirectory(),
                        serviceData.getShingleLength(), serviceData.getShinglerType());
                new Thread(mba).start();

                // cosine calculator withs maps queue and results queue
                Calculator cosineCalculator = null;
                try
                {

                    cosineCalculator = new Calculator(maps, results, queryMap.get(), 10, serviceData.getCosineType());

                } catch (ExecutionException e)
                {
                    // nothing to do
                    e.printStackTrace();
                } catch (InterruptedException e)
                {
                    // nothing to do
                    e.printStackTrace();
                }

                new Thread(cosineCalculator).start();

                // progress bar
                updateProgress(0, totalFiles);
                // get results and place in mainWindow
                int j = 0;
                while (j++ < totalFiles)
                {
                    try
                    {
                        Future<CosineDistanceResult> cosineFuture = null;

                        cosineFuture = results.take();

                        CosineDistanceResult cm;

                        cm = cosineFuture.get();

                        Platform.runLater(
                                mainWindow.new AddToResultListTask<CosineDistanceResult>(resultsObservable, cm));

                        // progress bar
                        updateProgress(j, totalFiles);

                    } catch (InterruptedException e)
                    {
                        // nothing to do
                        e.printStackTrace();
                    } catch (ExecutionException e)
                    {
                        // nothing to do
                        e.printStackTrace();
                    }

                }

                long elapsedTime = System.nanoTime() - start;

                // return time elapsed as string in seconds
                return timeToString(elapsedTime);

            }
        };//end task

    }

    /**
     * Transform time in {@code long}, nanosecond to a string in seconds
     * 
     * @param elapsedTime time to transform
     * @return transformed time
     */
    private String timeToString(long elapsedTime)
    {
        return String.format("%.2f", elapsedTime / 1000000000.0);
    }
}
