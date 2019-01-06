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
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class RemoteService extends Service<String>
{
    Socket individualconnection;
    int socketid;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    int selection;

    public RemoteService(Socket socket, int socketId)
    {

        individualconnection = socket;
        this.socketid = socketId;
    }

    @Override
    protected Task<String> createTask()
    {
        return new Task<String>()
        {

            @Override
            protected String call() throws Exception
            {
                try
                {

                    out = new ObjectOutputStream(individualconnection.getOutputStream());
                    out.flush();
                    in = new ObjectInputStream(individualconnection.getInputStream());
                    System.out.println(
                            "Connection" + socketid + " from IP address " + individualconnection.getInetAddress());

                    // create map for test
                    CountOne c1 = new CountOne(5, ShingleType.K_Mers);
                    c1.setFile(new File("w"));

                    // create serviceData for test
                    ServiceData serviceData = new ServiceData();
                    serviceData.setQueryFile(new File("w"));

                    try
                    {

                        CounterMap<Integer> cmIn = c1.calculate().get();

                        // send map
                        out.writeObject(cmIn);
                        // send service data
                        out.writeObject(serviceData);

                        // wait for results
                        while (true)
                        {

                            CosineDistanceResult result = (CosineDistanceResult) in.readObject();
                            if (result instanceof CosineDistanceResultPoison)
                                break;
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
                       // individualconnection.close();
                    }

                    catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return null;
            }

        };
    }

}
