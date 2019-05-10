/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw.ui;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import ie.gmit.sw.base.Calculator;
import ie.gmit.sw.base.Counter;
import ie.gmit.sw.base.CounterMap;
import ie.gmit.sw.base.poison.CosineDistanceResultPoison;
import ie.gmit.sw.data.CosineDistanceResult;
import ie.gmit.sw.data.ServiceData;

/**
 * Remote clients with console UI.
 * 
 * @author Jose I. Retamal
 *
 */
public class RemoteClient
{
    /**
     * for input data from console
     */
    private Scanner console;
    /**
     * IP address for connection
     */
    private String ipAddress;
    /**
     * Port address for connection
     */
    private int portAddress;
    /**
     * Subject directory for calculate cosine distance
     */
    private File subjectDirectory;
    /**
     * out stream object reference
     */
    private ObjectOutputStream out;
    /**
     * in stream object reference
     */
    private ObjectInputStream in;
    /**
     * socket reference
     */
    private Socket connection;

    /**
     * Input connection data and subject directory from console when created
     */
    public RemoteClient()
    {
        console = new Scanner(System.in);
        System.out.println("Welcome to Cosine Calculator Remote Client\n");
        System.out.print("Please enter main client IP:");
        ipAddress = console.nextLine();

        System.out.println("Please enter socket number");
        portAddress = console.nextInt();

        System.out.println("Please enter full subject folder path:");
        console.nextLine();
        String filePath = console.nextLine();
        System.out.println(filePath);

        subjectDirectory = new File(filePath);

        if (subjectDirectory.isDirectory())
        {
            System.out.println("All fine waiting for main client to send work....");
        }

    }

    /**
     * connect to main client, input query file from main client, calculate cosine distance
     * again subject file and send results to main client.
     */
    public void runClient()
    {
        try
        {
            // create conections
            connection = new Socket(ipAddress, portAddress);
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            System.out.println("Connection ready waiting for client to send work...");
            // wait for file to process
            while (true)
            {
                /*
                 * in map
                 */
                // get map
                CounterMap<Integer> queryMap = (CounterMap<Integer>) in.readObject();

                System.out.println("Working...");

                // stop connection
                if (queryMap instanceof ShowProgress)
                    break;

                /*
                 * in data service
                 */
                // get service data
                ServiceData serviceData = (ServiceData) in.readObject();

                // queue for calculate distance
                BlockingQueue<Future<CounterMap<Integer>>> maps = new LinkedBlockingQueue<>(100);
                BlockingQueue<Future<CosineDistanceResult>> results = new LinkedBlockingQueue<Future<CosineDistanceResult>>(
                        1000);

                // set local directory in serviceDAta
                serviceData.setSubjectDirectory(subjectDirectory);
                File[] files = serviceData.getFiles();

                int totalFiles = files.length;

                /*
                 * out total number of files for progress bard
                 * 
                 */
                sendMessage("" + totalFiles);

                // ArrayList<CounterMap<Integer>> mb = new ArrayList<>();

                // create and strart counter Object
                Counter mba = new Counter(maps, files, serviceData.getSubjectDirectory(),
                        serviceData.getShingleLength(), serviceData.getShinglerType());
                new Thread(mba).start();

                // create and start Calculator objects
                Calculator cosineCalculator = null;
                cosineCalculator = new Calculator(maps, results, queryMap, 10, serviceData.getCosineType());

                new Thread(cosineCalculator).start();

                int j = 0;
                while (j++ < totalFiles)
                {
                    try
                    {
                        // take results from queue
                        Future<CosineDistanceResult> cosineFuture = null;
                        cosineFuture = results.take();
                        CosineDistanceResult cm;
                        cm = cosineFuture.get();

                        // send result to client
                        out.writeObject(cm);
                        out.flush();

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
                // send poison after finish
                out.writeObject(new CosineDistanceResultPoison());
                out.flush();

            }
            // close connection when service stop
            out.close();
            in.close();
            connection.close();

        } catch (IOException e)
        {
            // nothing to do
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            // class serilization problems
            e.printStackTrace();
        }

    }

    void sendMessage(String msg)
    {
        try
        {
            out.writeObject(msg);
            out.flush();

        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
}
