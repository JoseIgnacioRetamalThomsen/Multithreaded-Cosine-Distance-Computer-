package ie.gmit.sw;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.spi.DirStateFactory.Result;

import ie.gmit.sw.base.CountOne;
import ie.gmit.sw.base.CounterMap;
import ie.gmit.sw.base.ShingleType;
import ie.gmit.sw.base.poison.CosineDistanceResultPoison;
import ie.gmit.sw.data.CosineDistanceResult;
import ie.gmit.sw.data.ServiceData;



public class RemoteTest
{

    public static void main(String[] args)
    {
        int maxConnectionThreads = 100;

        ExecutorService connectionsExecutor = Executors.newFixedThreadPool(maxConnectionThreads);
        int serverSocket = 82;
        int maxRequestConnection = 10;
        ServerSocket listener;
        int clientid = 0;

        try
        {
            listener = new ServerSocket(serverSocket, maxRequestConnection);

            while (true)
            {
                // wait for conection
                System.out.println("Main thread listening for incoming new connections");
                Socket newconnection = listener.accept();

                System.out.println("New connection received and spanning a thread");
                // Connecthandler1 t = new Connecthandler1(newconnection, clientid);
                // ServerConnectHandler ch = new ServerConnectHandler(newconnection,clientid);
                connectionsExecutor.execute(new Connecthandler1(newconnection, clientid++));
                // clientid++;
                // t.start();
            }

        }

        catch (IOException e)
        {
            System.out.println("Socket not opened");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

class Connecthandler1 extends Thread
{

    Socket individualconnection;
    int socketid;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    int selection;

    public Connecthandler1(Socket s, int i)
    {
        individualconnection = s;
        socketid = i;
    }

    public void run()
    {

        try
        {

            out = new ObjectOutputStream(individualconnection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(individualconnection.getInputStream());
            System.out.println("Connection" + socketid + " from IP address " + individualconnection.getInetAddress());
            
            //create map for test
            CountOne c1 = new CountOne(5, ShingleType.K_Mers);
            c1.setFile(new File("w"));
            
            //create serviceData for test
            ServiceData serviceData = new ServiceData();
            serviceData.setQueryFile(new File("w"));
            
            try
            {
                
                CounterMap<Integer> cmIn = c1.calculate().get();
                
               
                
                //send map
                out.writeObject(cmIn);
                //send service data
                out.writeObject(serviceData);
                
                //wait for results
                while(true) {
                    
                    CosineDistanceResult result =  (CosineDistanceResult)in.readObject();
                    if(result instanceof CosineDistanceResultPoison) break;
                    System.out.println(result.getCosineDistance());
                }
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
            try
            {
                System.out.println("terminated");
                out.close();
                in.close();
                individualconnection.close();
            }

            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    void sendMessage(String msg)
    {
        try
        {
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

}
