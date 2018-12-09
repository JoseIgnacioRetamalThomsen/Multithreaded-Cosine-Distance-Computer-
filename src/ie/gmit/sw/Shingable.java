package ie.gmit.sw;

public interface Shingable
{
  /**
   * 
   * @param line
   * @return
   */
  boolean addLine(CharSequence line);
  long nextShingle();
  boolean hasNextShingle();
  boolean hasLast();
  long lastShingle() throws Exception ;
}
