package ie.gmit.sw.ui;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.TextArea;
import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ie.gmit.sw.ShingleType;
import ie.gmit.sw.Test1;
import ie.gmit.sw.base.Calculator;
import ie.gmit.sw.base.Counter;
import ie.gmit.sw.base.CounterMap;
import ie.gmit.sw.base.FileShingleParser;
import ie.gmit.sw.base.MapBuilder;
import ie.gmit.sw.base.SingleThreadMapBuilder;
import ie.gmit.sw.data.CosineDistanceResult;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.concurrent.Task;

public class MainWindow1 extends Application
{

  private static VBox mainVB;
  private static TextArea resultTF;

  public MainWindow1()
  {
    createUi();
  }

  private void createUi()
  {

    // Create main Containers
    mainVB = new VBox();
    resultTF = new TextArea();

    mainVB.getChildren().add(resultTF);
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

    Thread tt = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
/*
        System.out.println("runnig");
        File subjectDir = new File("sub");

        String[] files = subjectDir.list();

        BlockingQueue<CounterMap<Integer>> maps = new ArrayBlockingQueue<>(100);

        Counter mba = new Counter(maps, files, subjectDir,5,ShingleType.K_Mers);

        new Thread(mba).start();

        BlockingQueue<Future<CosineDistanceResult>> answers = new ArrayBlockingQueue<Future<CosineDistanceResult>>(100);

        ExecutorService mapBuilderExecutor = Executors.newFixedThreadPool(10);
        BlockingQueue<Number> que1 = new ArrayBlockingQueue<>(10);
        FileShingleParser pa1 = new FileShingleParser(que1,5,ShingleType.K_Mers);

        pa1.setFile(new File("WarAndPeace-LeoTolstoy.txt"));
        new Thread(pa1).start();

        MapBuilder m1 = new SingleThreadMapBuilder(que1, "war");
        Future<CounterMap<Integer>> res1 = mapBuilderExecutor.submit(m1);

        CosineCalculatorAll cc = null;
        try
        {
          try
          {
            cc = new CosineCalculatorAll(maps, answers, res1.get(),10);
          } catch (InterruptedException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        } catch (ExecutionException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

       
        new Thread(cc).start();

        int count = 0;
        int in = 0;
        while (true)
        {
          try
          {
            Future<CosineDistanceResult> cm1 =
            null;
            try
            {
              cm1 = answers.take();
            } catch (InterruptedException e)
            {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            CosineDistanceResult cm =
            null;
            try
            {
              cm = cm1.get();
            } catch (InterruptedException e)
            {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            String s = cm.getFileName() + " " + cm.getCosineDistance() + "\n";
            System.out.println(s);
            Label l = new Label("we");
            Platform.runLater(new WriteTask<String>(resultTF, s));

          } catch (ExecutionException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        }
        */
        // return null;
      }
      
    });
    tt.start();
    
    ExecutorService pr = Executors.newFixedThreadPool(100);
   // pr.submit(task);
   
  }

//WRITE TASK CLASS
  public class WriteTask<V> extends Task<V>
  {

    TextArea target;
    String line;

    public WriteTask(TextArea target, String line)
    {
      this.target = target;
      this.line = line;
    }

    @Override
    protected V call() throws Exception
    {
      target.appendText(line + "\n");
      return null;
    }
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
