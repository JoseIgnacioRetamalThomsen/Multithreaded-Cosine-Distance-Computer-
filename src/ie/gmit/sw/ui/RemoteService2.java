package ie.gmit.sw.ui;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import ie.gmit.sw.base.CountOne;
import ie.gmit.sw.base.CounterMap;
import ie.gmit.sw.base.ShingleType;
import ie.gmit.sw.base.poison.CosineDistanceResultPoison;
import ie.gmit.sw.data.CosineDistanceResult;
import ie.gmit.sw.data.ServiceData;
import ie.gmit.sw.ui.MainWindow.AddToResultListTask;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class RemoteService2 extends Service<String>
{
    private Socket individualconnection;
    private int socketid;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;
    private int selection;
    private CounterMap<Integer> cmIn;
    private ServiceData serviceData;
    private MainWindow mainWindow;// reference to MainWindow
    ObservableList<CosineDistanceResult> resultsObservable; // list of results that shows in MainWindow

    public RemoteService2(ConnectionData cs, CounterMap<Integer> cmIn, ServiceData serviceData, MainWindow mainWindow,
            ObservableList<CosineDistanceResult> resultsObservable)
    {

        this.out = cs.getOut();
        this.in = cs.getIn();
        this.cmIn = cmIn;
        this.serviceData = serviceData;
        this.mainWindow = mainWindow;
        this.resultsObservable = resultsObservable;
    }

    @Override
    protected Task<String> createTask()
    {
        return new Task<String>()
        {

            @Override
            protected String call() throws Exception
            {
                long start = System.nanoTime();
                try
                {

                    // create map for test
                    // CountOne c1 = new CountOne(5, ShingleType.K_Mers);
                    // c1.setFile(new File("w"));

                    // create serviceData for test
                    // ServiceData serviceData = new ServiceData();
                    // serviceData.setQueryFile(new File("w"));

                    try
                    {

                        // CounterMap<Integer> cmIn = c1.calculate().get();

                        // count time

                        /*
                         * out map
                         */
                        // send map
                        out.writeObject(cmIn);
                        out.flush();
                        /*
                         * out serive data
                         */
                        // send service data
                        out.writeObject(serviceData);
                        out.flush();

                        /*
                         * in number of files
                         * 
                         */
                        int totalNumberOfFiles = Integer.parseInt((String) in.readObject());
                        System.out.println(totalNumberOfFiles);
                        // progress bar
                        updateProgress(0, totalNumberOfFiles);
                        // wait for results
                        int j = 0;
                        while (true)
                        {

                            CosineDistanceResult result = (CosineDistanceResult) in.readObject();
                            if (result instanceof CosineDistanceResultPoison)
                                break;
                            System.out.println(result.getCosineDistance());
                            Platform.runLater(mainWindow.new AddToResultListTask<CosineDistanceResult>(
                                    resultsObservable, result));

                            // progress bar
                            updateProgress(++j, totalNumberOfFiles);
                        }
                    } catch (ClassNotFoundException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                finally
                {
                    System.out.println("terminated");
                }

                long elapsedTime = System.nanoTime() - start;
                return String.format("%.2f", elapsedTime / 1000000000.0);
            }

        };
    }

}
