package ie.gmit.sw;

public class CosineDistanceResult
{

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    //return "CosineDistanceResult [fileName=" + fileName + ", cosineDistance=" + cosineDistance + "]";
    return String.format("%-100.30s   %4.2f%s", fileName, cosineDistance*100.0,"%");
  }
  private String fileName;
  private Double cosineDistance;
  
  public CosineDistanceResult(String fileName, Double cosineDistance)
  {
    super();
    this.fileName = fileName;
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
  
}
