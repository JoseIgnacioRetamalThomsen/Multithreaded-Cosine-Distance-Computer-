package ie.gmit.sw.ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.data.CosineDistanceResult;
import ie.gmit.sw.ui.MainWindow.AddToResultListTask;
import javafx.application.Platform;
import javafx.collections.ObservableList;

public class RemoteThread implements Runnable
{

    int maxConnectionThreads = 100;

    ExecutorService connectionsExecutor = Executors.newFixedThreadPool(maxConnectionThreads);
    int serverSocket = 82;
    int maxRequestConnection = 10;
    private MainWindow main;

    private transient ServerSocket listener;
    private int clientid = 0;

    // List<Socket> sockets;
    private ObservableList<ConnectionData> cs;
    private transient boolean isRunning = true;

    // private List<RemoteService2> rs ;

    public RemoteThread(ObservableList<ConnectionData> cs, MainWindow main)
    {
        // this.sockets = sockets;
        // this.rs=rs;
        this.main = main;
        this.cs = cs;
    }

    @Override
    public void run()
    {
        try
        {
            listener = new ServerSocket(serverSocket, maxRequestConnection);

            while (isRunning)
            {

                // wait for conection

                Socket newconnection = listener.accept();

                ObjectOutputStream out = new ObjectOutputStream(newconnection.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(newconnection.getInputStream());

                // sockets.add(newconnection);

                // rs.add(new RemoteService2(new ConnectionData(out, in)));

                // cs.add(new ConnectionData(out, in, newconnection));
                // add to main windows view
                Platform.runLater(main.new AddConnectionToConnectioView<ConnectionData>(cs,
                        new ConnectionData(out, in, newconnection)));

            }

        }

        catch (IOException e)
        {
            System.out.println("Socket not opened");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void stop()
    {
        try
        {
            this.listener.close();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.isRunning = false;
    }

}
