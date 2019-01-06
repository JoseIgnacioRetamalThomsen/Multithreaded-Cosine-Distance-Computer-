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
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;
import javafx.collections.ObservableList;
/**
 * A Runnable that listen for remote connections.
 * 
 * @author Jose I. Retamal
 *
 */
public class RemoteThreadConnector implements Runnable
{
    /**
     * socket of connection
     */
    private int serverSocket = 82;
    /**
     * max number of connections for socket
     */
    private int maxRequestConnection = 10;
    /**
     * reference to main windows
     */
    private MainWindow main;
    /**
     * open socked
     */
    private transient ServerSocket listener;
    /**
     * // reference to the list of connection data linked to a view in main windows
     */
    private ObservableList<ConnectionData> cs;
    /**
     * keep thread waiting for several connections
     */
    private transient boolean isRunning = true;

    /**
     * Create object with references to main windows
     * 
     * @param cs   reference to list of connection in {@code MainWindow}
     * @param main reference to {@code MainWindow}
     */
    public RemoteThreadConnector(ObservableList<ConnectionData> cs, MainWindow main)
    {
        this.main = main;
        this.cs = cs;

    }

    /**
     * Listen for connections
     */
    public void run()
    {
        try
        {
            listener = new ServerSocket(serverSocket, maxRequestConnection);

            while (isRunning)
            {

                // wait for connection
                Socket newconnection = listener.accept();

                // create stream for the connection
                ObjectOutputStream out = new ObjectOutputStream(newconnection.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(newconnection.getInputStream());

                // add connection to CS in MainWindow
                Platform.runLater(main.new AddConnectionToConnectioView<ConnectionData>(cs,
                        new ConnectionData(out, in, newconnection)));

            }

        }

        catch (IOException e)
        {
            // socket may be closed or we stop the connection
            // e.printStackTrace();
        }

    }

    /**
     * Stop listening for connections
     */
    public void stop()
    {
        try
        {
            this.listener.close();

        } catch (IOException e)
        {
            // nothing to do
            e.printStackTrace();
        }

        this.isRunning = false;

    }

}
