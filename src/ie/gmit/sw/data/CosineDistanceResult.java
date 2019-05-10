/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */

package ie.gmit.sw.data;

import java.io.Serializable;

/**
 * Store results of computed cosine distance of 2 files.
 * 
 * @author Jose I. Retamal
 *
 */
public class CosineDistanceResult implements Serializable
{

    /**
     * Serializable id
     */
    private static final long serialVersionUID = 3794392950694567157L;
    /**
     * Subject directory file name.
     */
    private String fileName;
    /**
     * Query file name.
     */
    private String queryFileName;
    /**
     * Cosine distance of 2 files.
     */
    private Double cosineDistance;

    /**
     * Create object with basic data.
     * 
     * @param fileName       Subject directory file name
     * @param queryFileName  Query file name
     * @param cosineDistance Cosine distance of 2 files
     */
    public CosineDistanceResult(String fileName, String queryFileName, Double cosineDistance)
    {
        super();
        this.fileName = fileName;
        this.queryFileName = queryFileName;
        this.cosineDistance = cosineDistance;
    }

    /**
     * @return the fileName
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * @return the cosineDistance
     */
    public Double getCosineDistance()
    {
        return cosineDistance;
    }

    /**
     * @param cosineDistance the cosineDistance to set
     */
    public void setCosineDistance(Double cosineDistance)
    {
        this.cosineDistance = cosineDistance;
    }

    public Double getCosineDistancePerCent()
    {
        return cosineDistance * 100;
    }

    /**
     * Return string of cosine distance, rounded two 2 decimals
     */
    public String toString()
    {

        return String.format("%-100.30s   %4.2f%s", fileName, cosineDistance * 100.0, "%");
    }

    /**
     * @return the queryFileName
     */
    public String getQueryFileName()
    {
        return queryFileName;
    }

    /**
     * @param queryFileName the queryFileName to set
     */
    public void setQueryFileName(String queryFileName)
    {
        this.queryFileName = queryFileName;
    }
}
