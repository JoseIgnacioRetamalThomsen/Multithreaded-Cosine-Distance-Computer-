/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */
package ie.gmit.sw;

import ie.gmit.sw.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Start point of main program.
 * 
 * @author Jose I. Retamal
 *
 */
public class Runner extends Application
{

  /**
   * Start application
   */
  public void start(Stage primaryStage) throws Exception
  {
    try
    {
      Parent root = new MainWindow();
      
      Scene scene = new Scene(root);
      
      primaryStage.setTitle("Document comparison service.");
      
      primaryStage.setScene(scene);
      
      primaryStage.show();
      
    } catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  /**
   * Runs start with no arguments.
   * 
   * @param args comand line parameter
   */
  public static void main(String[] args)
  {
    launch(args);
  }
}
