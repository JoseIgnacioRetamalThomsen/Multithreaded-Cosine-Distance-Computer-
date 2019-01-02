/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */
package ie.gmit.sw.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.ShingleType;

/**
 * 
 * @author Jose I. Retamal
 *
 */
public class FileShingleParser implements Runnable
{

    private final BlockingQueue<Number> queue;
    private BufferedReader fileIn;
    private String line;
    private Shingable shingler;

    /**
     * 
     * @param queue       for put produced integer shingles
     * @param shingleSize size of each shingle
     * @param shingleType can be group shingle or k-mers shingle
     */
    public FileShingleParser(BlockingQueue<Number> queue, int shingleSize, ShingleType shingleType)
    {
        this.queue = queue;

        switch (shingleType)
        {
        case Group:
            shingler = new GroupShingler(shingleSize);
            break;
        case K_Mers:
            shingler = new KShingler(shingleSize);
            break;

        }

    }

    /**
     * Set file for read and produce shingles
     * 
     * @param file file to read
     * @return true if the file exist and can be read
     */
    public boolean setFile(File file)
    {

        try
        {
            fileIn = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            return true;
        } catch (FileNotFoundException e)
        {
            // not a file return false
            return false;
        }
    }

    /**
     * Check if the file for read shingles is set
     * 
     * @return true if the file is set
     */
    public boolean isFileSet()
    {
        return fileIn != null;
    }

    /**
     * Runs from file, calculate shingles using {@code Shingler} and then put the
     * shingle in the queue.
     */
    public void run()
    {

        try
        {
            while ((line = fileIn.readLine()) != null)
            {
                shingler.addLine(line);

                while (shingler.hasNextShingle())
                {
                    queue.put(shingler.nextShingle());

                }

            } // while ((line = fileIn.readLine()) != null)

            if (shingler.hasLast())
            {
                queue.put(shingler.lastShingle());

            }

            queue.put(new NumberPoison());

        } catch (IOException e)
        {
            // problem reading file, should not happens, nothing to do if happens.
            e.printStackTrace();

        } catch (InterruptedException e)
        {
            // nothing to do
            e.printStackTrace();

        }
        /*
         * catch (Exception e) { // TODO Auto-generated catch block
         * 
         * e.printStackTrace(); }
         */

    }

}
