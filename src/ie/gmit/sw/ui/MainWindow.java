package ie.gmit.sw.ui;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.*;
import java.io.*;
import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;

public class MainWindow extends Application
{

  private static VBox mainVB;

  
  public MainWindow() {
    createUi();
  }
  private void createUi()
  {

    // Create main Containers
    mainVB = new VBox();
  }

  @Override
  public void start(Stage stageOne) throws Exception
  {
    Scene scene = new Scene(mainVB, 950, 350);

    // set title to stage
    stageOne.setTitle("File Analyzer");
    // set scene to stage
    stageOne.setScene(scene);
    stageOne.show();

  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
