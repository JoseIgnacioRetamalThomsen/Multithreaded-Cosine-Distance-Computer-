package ie.gmit.sw.shingler;

public class KShingler extends AbstractShingler
{

  private int length = 5;
  private int position = 0;
  private StringBuilder line = new StringBuilder();

  public KShingler(int i)
  {
    this.length = i;
  }

  @Override
  public boolean addLine(CharSequence line)
  {
    // check if there is next(Only can add line if there is not)
    if (hasNextShingle())
      return false;

    if (this.line.equals(""))
    {
      this.line.append(line);
    } else
    {
      this.line = new StringBuilder(this.line.substring(position, this.line.length()));
      this.line.append(line);
      position = 0;
    }

    return true;
  }

  @Override
  public int nextShingle()
  {
    int hash = line.substring(position, position + length).toString().hashCode();
    position += length;

    return hash;
  }

  @Override
  public boolean hasNextShingle()
  {
    // check if one more shingler can be made it from the string

    return (line.length() - position) >= length;
  }

  @Override
  public boolean hasLast()
  {
    return (line.length() - position) > 0;
  }

  @Override
  public int lastShingle()
  {
    int hash = line.substring(position, line.length()).toString().hashCode();
    line = new StringBuilder();
    position = 0;

    return hash;
  }

  public static void main(String[] args) throws Exception
  {
    StringBuilder s = new StringBuilder("today i went to my house. conme and see.");
    s.substring(4);

    System.out.println(new String("This ").hashCode() + ",");
    System.out.println(new String("is th").hashCode() + ",");
    System.out.println(new String("e fir").hashCode() + ",");
    System.out.println(new String("st li").hashCode() + ",");
    System.out.println(new String("nethi").hashCode() + ",");
    System.out.println(new String("s is ").hashCode() + ",");
    System.out.println(new String("the s").hashCode() + ",");
    System.out.println(new String("econd").hashCode() + ",");
    System.out.println(new String(" one.").hashCode() + ",");
    System.out.println(new String("..").hashCode());
    /*
     * System.out.println(s.substring(5, 10).toString().hashCode());
     * System.out.println("|" + s.substring(5, 10) + "|");
     * 
     * KShingler k = new KShingler(); k.addLine("today i went to my");
     * System.out.println("has " + k.hasNextShingle()); System.out.println("first "
     * + k.nextShingle()); System.out.println("has " + k.hasNextShingle());
     * System.out.println("second " + k.nextShingle()); System.out.println("has " +
     * k.hasNextShingle()); System.out.println("second " + k.nextShingle());
     * System.out.println("has " + k.hasNextShingle());
     * System.out.println("has last " + k.hasLast()); k.addLine(" house. conme");
     * System.out.println("has l " + k.hasNextShingle());
     * System.out.println("second " + k.nextShingle());
     */
  }
}
