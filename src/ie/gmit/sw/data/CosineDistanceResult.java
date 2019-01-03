package ie.gmit.sw.data;

public class CosineDistanceResult
{

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
 
  private String fileName;
 private String queryFileName;
  private Double cosineDistance;
 
  
  public CosineDistanceResult(String fileName,String queryFileName, Double cosineDistance)
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
  
  public Double getCosineDistancePerCent() {
    return cosineDistance*100;
  }
  @Override
  public String toString()
  {
    //return "CosineDistanceResult [fileName=" + fileName + ", cosineDistance=" + cosineDistance + "]";
    return String.format("%-100.30s   %4.2f%s", fileName, cosineDistance*100.0,"%");
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
