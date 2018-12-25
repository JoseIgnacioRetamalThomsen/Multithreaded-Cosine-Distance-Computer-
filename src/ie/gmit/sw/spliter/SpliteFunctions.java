package ie.gmit.sw.spliter;

public class SpliteFunctions
{

  public  String[]  splitWordsNumber(String line) {
    
    return line.split("[^a-zA-Z0-9-]++");
    
  }
  
  public String[] splitChar(String line,int splitLength) {
    
    
    StringBuilder sb = new StringBuilder();
    
    return line.split("[^a-z-]++");
    
  }
}
