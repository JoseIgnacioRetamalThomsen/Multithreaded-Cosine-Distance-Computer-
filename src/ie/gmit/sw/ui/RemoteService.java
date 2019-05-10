/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */

package ie.gmit.sw.ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import ie.gmit.sw.base.CounterMap;
import ie.gmit.sw.base.poison.CosineDistanceResultPoison;
import ie.gmit.sw.data.ConnectionData;
import ie.gmit.sw.data.CosineDistanceResult;
import ie.gmit.sw.data.ServiceData;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Perform cosine calculations connected to a remote client.
 * 
 * @author Jose I. Retamal
 *
 */
public class RemoteService extends Service<String>
{
    /**
     * out stream object reference
     */
    private ObjectOutputStream out;
    /**
     * in stream object reference
     */
    private ObjectInputStream in;
    /**
     * map to send to client for calculate cosine distance
     */
    private CounterMap<Integer> cmIn;
    /**
     * data for perform service
     */
    private ServiceData serviceData;
    /**
     * reference to MainWindow
     */
    private MainWindow mainWindow;

    /**
     * // reference list of results that shows in MainWindow
     */
    private ObservableList<CosineDistanceResult> resultsObservable;

    /**
     * Create the service for be ready to run
     * 
     * @param cs                in out stream objects data
     * @param cmIn              map of query file
     * @param serviceData       data about how to perform single service
     * @param mainWindow        reference to MainWindow
     * @param resultsObservable reference to results list in MainWindow
     */
    public RemoteService(ConnectionData cs, CounterMap<Integer> cmIn, ServiceData serviceData, MainWindow mainWindow,
            ObservableList<CosineDistanceResult> resultsObservable)
    {
        this.out = cs.getOut();
        this.in = cs.getIn();
        this.cmIn = cmIn;
        this.serviceData = serviceData;
        this.mainWindow = mainWindow;
        this.resultsObservable = resultsObservable;
    }

    /**
     * Create and run service task, return the time that take to perform the task.
     */
    protected Task<String> createTask()
    {
        return new Task<String>()
        {

            @Override
            protected String call() throws Exception
            {
                // start counting time
                long start = System.nanoTime();
                try
                {

                    /*
                     * out map
                     */
                    // send map
                    out.writeObject(cmIn);
                    out.flush();
                    /*
                     * out service data
                     */
                    // send service data
                    out.writeObject(serviceData);
                    out.flush();

                    /*
                     * in number of files
                     * 
                     */
                    int totalNumberOfFiles = Integer.parseInt((String) in.readObject());

                    // progress bar
                    updateProgress(0, totalNumberOfFiles);
                    // wait for results
                    int j = 0;
                    while (true)
                    {
                        // read 1 cosine result
                        CosineDistanceResult result = (CosineDistanceResult) in.readObject();

                        // finish if is poison
                        if (result instanceof CosineDistanceResultPoison)
                            break;

                        // add result to MainWindow
                        Platform.runLater(
                                mainWindow.new AddToResultListTask<CosineDistanceResult>(resultsObservable, result));

                        // update progress bar
                        updateProgress(++j, totalNumberOfFiles);

                    }
                } catch (ClassNotFoundException e)
                {
                    // problems reading object or wrong object was sent
                    e.printStackTrace();
                }

                catch (IOException e)
                {
                    // nothing to do
                    e.printStackTrace();
                }

                finally
                {
                    // finish one service
                }

                // calculate time and return it for finish service
                long elapsedTime = System.nanoTime() - start;
                return String.format("%.2f", elapsedTime / 1000000000.0);

            }

        };
    }

}
