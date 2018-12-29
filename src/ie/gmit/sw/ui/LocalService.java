package ie.gmit.sw.ui;

import java.util.concurrent.BlockingQueue;

import ie.gmit.sw.CosineDistanceResult;
import ie.gmit.sw.ServiceData;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;

import java.awt.Event;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import ie.gmit.sw.CosineDistanceResult;
import ie.gmit.sw.ServiceData;
import ie.gmit.sw.ShingleType;
import ie.gmit.sw.UserSettings;
import ie.gmit.sw.calculator.CosineCalculatorAll;
import ie.gmit.sw.counting.CountOne;
import ie.gmit.sw.counting.Counter;
import ie.gmit.sw.counting.CounterMap;
import ie.gmit.sw.counting.MapBuilder;
import ie.gmit.sw.counting.SingleThreadMapBuilder;
import ie.gmit.sw.shingler.FileShingleParser;
import ie.gmit.sw.ui.MainWindow1.WriteTask;
import javafx.beans.binding.ListBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

public class LocalService extends Service<String>
{

  ObservableList<CosineDistanceResult> resultsObservable;

  private ServiceData serviceData = new ServiceData();

  private BlockingQueue<CounterMap<Integer>> maps = new ArrayBlockingQueue<>(100);

  private BlockingQueue<Future<CosineDistanceResult>> results = new LinkedBlockingQueue<Future<CosineDistanceResult>>(
      1000);

  private MainWindow mainWindow;

  Future<CounterMap<Integer>> queryMap;

  public LocalService(ServiceData serviceData, ObservableList<CosineDistanceResult> resultsObservable,
      MainWindow mainWindow, Future<CounterMap<Integer>> queryMap)
  {
    this.serviceData = serviceData;
    this.resultsObservable = resultsObservable;
    this.mainWindow = mainWindow;
    this.queryMap = queryMap;
  }

  @Override
  protected Task<String> createTask()
  {

    return new Task<String>()
    {
      public String call()
      {

        try
        {
          System.out.println(queryMap.get().size());
        } catch (InterruptedException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (ExecutionException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        // calculate again files

        String[] files = serviceData.getSubjectDirectory().list();
        int totalFiles = files.length;

        // progress bar
        int totalWorkTaskBar = totalFiles * 2;
        int workDoneTaskBar = 1;
        updateProgress(workDoneTaskBar, totalWorkTaskBar);

        Counter mba = new Counter(maps, files, serviceData.getSubjectDirectory(), serviceData.getShingleLength(),
            serviceData.getShinglerType());

        new Thread(mba).start();

        // progress bar
        updateProgress(totalFiles / 4, totalWorkTaskBar);
        workDoneTaskBar = totalFiles;

        CosineCalculatorAll cosineCalculator = null;

        try
        {
          try
          {
            cosineCalculator = new CosineCalculatorAll(maps, results, queryMap.get(), 10);
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

        new Thread(cosineCalculator).start();

        // progress bar
        updateProgress(totalFiles, totalWorkTaskBar);
        int j = 0;
        while (j++ < totalFiles)
        {
          try
          {
            LinkedList<Future<CosineDistanceResult>> buffer = new LinkedList<>();

            Future<CosineDistanceResult> cosineFuture = null;

            cosineFuture = results.take();

            CosineDistanceResult cm;

            cm = cosineFuture.get();

            Platform.runLater(mainWindow.new AddToResultListTask<CosineDistanceResult>(resultsObservable, cm));

            // progres bar
            updateProgress(workDoneTaskBar + j, totalWorkTaskBar);

          } catch (InterruptedException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (ExecutionException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        }

        return null;

      }
    };

  }
}
