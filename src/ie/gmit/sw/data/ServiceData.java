/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * 
 */
package ie.gmit.sw.data;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import ie.gmit.sw.base.CosineCalculatorType;
import ie.gmit.sw.base.ShingleType;

/**
 * Class used for save all the different options that can be chose for perform a cosine
 * calculator service.
 * <p>
 * Include files needed and parameters for all objects needed for run the program.
 * </p>
 * <p>
 * Files are initiated to null, parameters to a default value.
 * </p>
 * 
 * @author Jose I. Retamal
 * 
 *
 */
public class ServiceData implements Serializable
{

    private static final long serialVersionUID = -6928726486161547469L;// serialization
    private File subjectDirectory;
    private File queryFile;
    private ShingleType shinglerType;
    private int shingleLength;
    private CosineCalculatorType cosineType;

    // constructors

    /**
     * Null constructor, files are initialized to null, other fields to defaults values.
     */
    public ServiceData()
    {
        super();
        this.subjectDirectory = null;
        this.queryFile = null;
        this.shinglerType = ShingleType.K_Mers;
        this.shingleLength = 5;
        this.cosineType = CosineCalculatorType.Normal;

    }

    // getters and setters
    /**
     * Retrieve assigned subject directory, will be null is not assigned
     * 
     * @return the subjectDirectory
     */
    public File getSubjectDirectory()
    {
        return subjectDirectory;

    }

    /**
     * Set subject directory, will return true only if the parameter value is a valid
     * directory and have files to read.
     * 
     * @param subjectDirectory the subject directory
     * @return true is the parameter value is valid
     */
    public boolean setSubjectDirectory(File subjectDirectory)
    {
        this.subjectDirectory = subjectDirectory;

        if (this.subjectDirectory != null && this.subjectDirectory.isDirectory()
                && this.subjectDirectory.listFiles().length > 0)
        {
            return true;
        } else
        {
            this.subjectDirectory = null;
            return false;

        }

    }

    /**
     * Retrieve assigned query file, will be null is not assigned
     * 
     * @return the queryFile
     */
    public File getQueryFile()
    {
        return queryFile;
    }

    /**
     * Set query file, will check if the file is a valid file and can be read. If is valid is
     * set if not query file is set to null.
     * 
     * 
     * @param queryFile the query file to set
     * @return true is parameter file is valid file and can be read
     */
    public boolean setQueryFile(File queryFile)
    {
        this.queryFile = queryFile;
        if (this.queryFile != null && this.queryFile.isFile() && this.queryFile.canRead())
        {
            return true;

        } else
        {
            this.queryFile = null;
            return false;

        }

    }

    /**
     * Get the assigned or default value of shingler type, default is k-mers shingles
     * 
     * @return the shinglerType the value of shingler type, can be Group or k-mers
     */
    public ShingleType getShinglerType()
    {
        return shinglerType;
    }

    /**
     * Set the shingler type can be Group or k-mers
     * 
     * @param shinglerType the shinglerType to set
     */
    public void setShinglerType(ShingleType shinglerType)
    {
        this.shinglerType = shinglerType;
    }

    /**
     * Length is the size of the k-mers shingles or the amount of words in group words
     * shingles
     * 
     * @return the shingleLength
     */
    public int getShingleLength()
    {
        return shingleLength;
    }

    /**
     * Length is the size of the k-mers shingles or the amount of words in group words
     * shingles
     * 
     * @param shingleLength the shingleLength to set
     */
    public void setShingleLength(int shingleLength)
    {
        this.shingleLength = shingleLength;
    }

    /**
     * Cosine calculator type can be normal, which is fine for no so big amount of shingles
     * counted(most of the time will be related to the size of the file but also depends on
     * the size of the shingles).Normal is faster but may give funny values if the size is to
     * big. Big can hold bigger size but is slower
     * 
     * @return the cosineType
     */
    public CosineCalculatorType getCosineType()
    {
        return cosineType;
    }

    /**
     * Cosine calculator type can be normal, which is fine for no so big amount of shingles
     * counted(most of the time will be related to the size of the file but also depends on
     * the size of the shingles).Normal is faster but may give funny values if the size is to
     * big. Big can hold bigger size but is slower.
     * 
     * @param cosineType the cosineType to set
     */
    public void setCosineType(CosineCalculatorType cosineType)
    {
        this.cosineType = cosineType;
    }

    // other methods

    /**
     * Use for get all files to process.
     * 
     * @return Returns all files that can be read from subject directory. null if the subject
     *         directory is null, mean not set yet.
     */
    public File[] getFiles()
    {
        if (subjectDirectory == null)
            return null;

        File[] all = subjectDirectory.listFiles();
        LinkedList<File> result = new LinkedList<>();
        for (File f : all)
        {
            if (f.isFile() && f.canRead())
                result.add(f);

        }

        return result.toArray(new File[result.size()]);
    }

    /**
     * Check if the query file has been set
     * 
     * @return true if the query file has been set
     */
    public boolean isQueryFile()
    {
        return queryFile != null;

    }

    /**
     * Check if subject directory has been set
     * 
     * @return true if subject directory is set
     */
    public boolean isSubjectDirectory()
    {
        return this.subjectDirectory != null;

    }

    /**
     * Used for check if all settings are done for perform service.
     * 
     * @return true if all settings are fine
     */
    public boolean isReady()
    {
        if (this.isQueryFile() && this.isSubjectDirectory())
            return true;
        else
            return false;
    }

}
