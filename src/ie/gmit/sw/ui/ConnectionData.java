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
import java.net.Socket;

/**
 * Data about 1 remote connection, store open {@code ObjectOutputStream} and
 * {@code ObjectInputStream references.
 * 
 * @author Jose I. Retamal
 *
 */
public class ConnectionData
{
    /**
     * Socket for the connection
     */
    private Socket socket;
    /**
     * Out stream
     */
    private ObjectOutputStream out;
    /**
     * In streak
     */
    private ObjectInputStream in;
    /**
     * true if streams are open
     */
    private transient boolean isAcitve;
    /**
     * for block another service in same connection when running
     */
    private transient boolean isRunning;
    

    /**
     * Create Object with streams and socket
     * 
     * @param out stream
     * @param in stream 
     * @param socket of the connection
     */
    public ConnectionData(ObjectOutputStream out, ObjectInputStream in, Socket socket)
    {
        super();
        this.out = out;
        this.in = in;
        isAcitve = true;
        isRunning = false;
        this.socket = socket;

    }

    /**
     * @return the out
     */
    public ObjectOutputStream getOut()
    {
        return out;
    }

    /**
     * @param out the out to set
     */
    public void setOut(ObjectOutputStream out)
    {
        this.out = out;
    }

    /**
     * @return the in
     */
    public ObjectInputStream getIn()
    {
        return in;
    }

    /**
     * @param in the in to set
     */
    public void setIn(ObjectInputStream in)
    {
        this.in = in;
    }

    /**
     * @return the isAcitve
     */
    public boolean isAcitve()
    {
        return isAcitve;
    }

    /**
     * @param isAcitve the isAcitve to set
     */
    public void setAcitve(boolean isAcitve)
    {
        this.isAcitve = isAcitve;
    }

    /**
     * @return the isRunning
     */
    public boolean isRunning()
    {
        return isRunning;
    }

    /**
     * @param isRunning the isRunning to set
     */
    public void setRunning(boolean isRunning)
    {
        this.isRunning = isRunning;
    }

    /**
     * @return the socket
     */
    public Socket getSocket()
    {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }

    /**
     * Close the connection streams
     * 
     * @throws IOException if streams can't be close
     */
    public void closeCoonection() throws IOException
    {
        out.close();
        in.close();
        this.isAcitve = false;
        this.isRunning = false;

    }
}
