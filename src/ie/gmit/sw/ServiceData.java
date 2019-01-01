package ie.gmit.sw;

import java.io.File;
import java.util.LinkedList;

public class ServiceData
{
  private File subjectDirectory;
  private File queryFile;
  private ShingleType shinglerType;
  private int shingleLength;

  public ServiceData()
  {
    super();
    this.subjectDirectory = null;
    this.queryFile = null;
    this.shinglerType = ShingleType.K_Mers;
    this.shingleLength = 5;

  }

  public File[] getFiles()
  {
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
   * @return the queryFile
   */
  public File getQueryFile()
  {
    return queryFile;
  }

  /**
   * @param queryFile the queryFile to set
   */
  public boolean setQueryFile(File queryFile)
  {
    this.queryFile = queryFile;
    if (this.queryFile != null && this.queryFile.isFile())
    {
      return true;

    } else
    {
      this.queryFile = null;
      return false;

    }

  }

  public boolean isQueryFile()
  {
    return queryFile != null;

  }

  /**
   * @return the subjectDirectory
   */
  public File getSubjectDirectory()
  {
    return subjectDirectory;

  }

  /**
   * @param subjectDirectory the subjectDirectory to set
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

  public boolean isSubjectDirectory()
  {
    return this.subjectDirectory != null;

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
