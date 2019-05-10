/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw;


import ie.gmit.sw.ui.RemoteClient;

/**
 * Runner for remote client.
 */
public class RemoteClientRunner
{
    public static void main(String[] args)
    {
        RemoteClient rc = new RemoteClient();
        rc.runClient();
    }
}
