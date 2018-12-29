package ie.gmit.sw;

import ie.gmit.sw.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Runner extends Application
{

  @Override
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

  public static void main(String[] args)
  {
    launch(args);
  }
}
