package ie.gmit.sw.ui;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionData
{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private transient boolean isAcitve;
    private transient boolean isRunning;

    public ConnectionData(ObjectOutputStream out, ObjectInputStream in, Socket socket )
    {
        super();
        this.out = out;
        this.in = in;
        isAcitve = true;
        isRunning = false;
        this.socket=socket;
       

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
}
