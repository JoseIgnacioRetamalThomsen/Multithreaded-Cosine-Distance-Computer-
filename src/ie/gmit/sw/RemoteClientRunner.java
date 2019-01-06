/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw;

/**
 * Runner for remote client
 */
import ie.gmit.sw.ui.RemoteClient;

public class RemoteClientRunner
{
    public static void main(String[] args)
    {
        RemoteClient rc = new RemoteClient();
        rc.runClient();
    }
}
