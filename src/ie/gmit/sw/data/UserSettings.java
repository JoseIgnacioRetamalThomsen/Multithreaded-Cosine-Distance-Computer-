package ie.gmit.sw.data;

import ie.gmit.sw.base.ShingleType;

public class UserSettings
{
  private int mapsQueueSize;
  
  private int resultQueueSize;
  
  private int counterPoolSize;
  
  private ShingleType shinglerType;
  private int shingleLength;

  
  public UserSettings()
  {
    super();
    this.mapsQueueSize = 1000;
    this.resultQueueSize = 1000;
    this.counterPoolSize = 1000;
    this.shinglerType = ShingleType.K_Mers;
    this.shingleLength = 5;
  }


  /**
   * @return the mapsQueueSize
   */
  public int getMapsQueueSize()
  {
    return mapsQueueSize;
  }

  /**
   * @param mapsQueueSize the mapsQueueSize to set
   */
  public void setMapsQueueSize(int mapsQueueSize)
  {
    this.mapsQueueSize = mapsQueueSize;
  }

  /**
   * @return the resultQueueSize
   */
  public int getResultQueueSize()
  {
    return resultQueueSize;
  }

  /**
   * @param resultQueueSize the resultQueueSize to set
   */
  public void setResultQueueSize(int resultQueueSize)
  {
    this.resultQueueSize = resultQueueSize;
  }

  /**
   * @return the counterPoolSize
   */
  public int getCounterPoolSize()
  {
    return counterPoolSize;
  }

  /**
   * @param counterPoolSize the counterPoolSize to set
   */
  public void setCounterPoolSize(int counterPoolSize)
  {
    this.counterPoolSize = counterPoolSize;
  }

  /**
   * @return the shinglerType
   */
  public ShingleType getShinglerType()
  {
    return shinglerType;
  }

  /**
   * @param shinglerType the shinglerType to set
   */
  public void setShinglerType(ShingleType shinglerType)
  {
    this.shinglerType = shinglerType;
  }

  /**
   * @return the shingleLength
   */
  public int getShingleLength()
  {
    return shingleLength;
  }

  /**
   * @param shingleLength the shingleLength to set
   */
  public void setShingleLength(int shingleLength)
  {
    this.shingleLength = shingleLength;
  }
  
  
}
