package ie.gmit.sw.ui;

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
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import ie.gmit.sw.ui.MainWindow.AddToResultListTask;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
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

public class MainWindow extends BorderPane
{

  private ServiceData serviceData = new ServiceData();

  private static ListView<CosineDistanceResult> resultsView = new ListView<CosineDistanceResult>();
  ObservableList<CosineDistanceResult> resultsObservable = FXCollections.observableArrayList();

  private static Future<CounterMap<Integer>> queryMap;

  public MainWindow()
  {
    addTopVBox(this);
    addLeftVBox(this);
    center(this);
    addRightVBox(this);
  }

  VBox progresBarsMainVBox;

  private void addRightVBox(BorderPane borderPane)
  {
    final VBox mainContainer = new VBox();

    // title
    final HBox title = new HBox();
    // header
    // styling
    title.setPadding(new Insets(15, 60, 15, 60));
    title.setSpacing(10);
    title.setStyle("-fx-background-color: #336699;");
    
    addTitleText(title, "Tasks");

    mainContainer.getChildren().add(title);

    final ScrollPane sp = new ScrollPane();
    progresBarsMainVBox = new VBox();
    sp.setContent(progresBarsMainVBox);

    mainContainer.getChildren().add(sp);

    this.setRight(mainContainer);

  }

  private void center(BorderPane borderPane)
  {
    resultsView.setItems(resultsObservable);

    final HBox main = new HBox();

    resultsView.setPrefWidth(800);

    resultsView.setCellFactory(new Callback<ListView<CosineDistanceResult>, ListCell<CosineDistanceResult>>()
    {

      @Override
      public ListCell<CosineDistanceResult> call(ListView<CosineDistanceResult> param)
      {
        return new Cell();

      }

    });

    final VBox resultsVBox = new VBox();

    final HBox resultsHeaderHBox = new HBox();

    Label labelName = new Label("File Name");
    labelName.setMinWidth(300);
    labelName.setMaxWidth(300);
    Label labelCosDistance = new Label("Cosine Distance");
    labelCosDistance.setMinWidth(100);
    labelCosDistance.setMaxWidth(100);
    labelCosDistance.setTextAlignment(TextAlignment.CENTER);
    resultsHeaderHBox.getChildren().addAll(labelName, labelCosDistance);

    resultsVBox.getChildren().addAll(resultsHeaderHBox, resultsView);

    main.getChildren().add(resultsVBox);

    borderPane.setCenter(main);

    resultsObservable.add(new CosineDistanceResult("my file longgggggggggggggggggggggggggggggggggggggggggg", 0.5));

  }

  // format cell for view queue
  static class Cell extends ListCell<CosineDistanceResult>
  {
    @Override
    public void updateItem(CosineDistanceResult item, boolean empty)
    {
      super.updateItem(item, empty);

      HBox hb = new HBox();

      // hb.setPadding(new Insets(5, 2, 5, 2));
      // hb.setSpacing(2);
      // hb.setStyle("-fx-background-color: #336699;");

      Label labelName = new Label();
      labelName.setMinWidth(300);
      labelName.setMaxWidth(300);
      Label labelCosDistance = new Label();
      labelCosDistance.setMinWidth(100);
      labelCosDistance.setMaxWidth(100);
      labelCosDistance.setTextAlignment(TextAlignment.CENTER);
      hb.getChildren().addAll(labelName, labelCosDistance);

      if (item != null)
      {
        // rect.setFill(Color.web(item));
        labelName.setText(item.getFileName());
        labelCosDistance.setText(String.format("%.2f %s", item.getCosineDistancePerCent(), "%"));
        setGraphic(hb);
      }
    }
  }

  private void addTopVBox(BorderPane borderPane)
  {
    final VBox mainVBox = new VBox();

    mainVBox.setPadding(new Insets(12, 50, 24, 50));
    mainVBox.setSpacing(10);
    mainVBox.setStyle("-fx-background-color: #336699;");

    Blend blend = new Blend();
    blend.setMode(BlendMode.MULTIPLY);

    DropShadow ds = new DropShadow();
    ds.setColor(Color.rgb(254, 235, 66, 0.3));
    ds.setOffsetX(5);
    ds.setOffsetY(5);
    ds.setRadius(5);
    ds.setSpread(0.2);

    blend.setBottomInput(ds);

    DropShadow ds1 = new DropShadow();
    ds1.setColor(Color.web("#336699"));
    ds1.setRadius(20);
    ds1.setSpread(0.2);

    Blend blend2 = new Blend();
    blend2.setMode(BlendMode.MULTIPLY);

    InnerShadow is = new InnerShadow();
    is.setColor(Color.web("#feeb42"));
    is.setRadius(9);
    is.setChoke(0.8);
    blend2.setBottomInput(is);

    InnerShadow is1 = new InnerShadow();
    is1.setColor(Color.web("#f13a00"));
    is1.setRadius(5);
    is1.setChoke(0.4);
    blend2.setTopInput(is1);

    Blend blend1 = new Blend();
    blend1.setMode(BlendMode.MULTIPLY);
    blend1.setBottomInput(ds1);
    blend1.setTopInput(blend2);

    blend.setTopInput(blend1);

    final Text text = new Text("Cosine Distance Calculator");

    text.setFont(Font.font(null, FontWeight.BOLD, 80));

    text.setFill(Color.ROYALBLUE);

    mainVBox.getChildren().add(text);

    borderPane.setTop(mainVBox);

    text.setEffect(blend);

  }

  // private DirectoryChooser directoryChooser;

  private void addLeftVBox(BorderPane borderPane)
  {
    final VBox mainVBox = new VBox();

    // subject directory selection

    // create and add container
    final VBox subjectDirectoryContainer = new VBox();
    mainVBox.getChildren().add(subjectDirectoryContainer);

    // create and add childrens
    final HBox subjectDirectoryHeaderHBox = new HBox();
    final HBox subjectDirectoryHBoxOne = new HBox();
    final HBox subjectDirectoryHBoxTwo = new HBox();
    subjectDirectoryContainer.getChildren().addAll(subjectDirectoryHeaderHBox, subjectDirectoryHBoxOne,
        subjectDirectoryHBoxTwo);

    // header
    // styling
    subjectDirectoryHeaderHBox.setPadding(new Insets(15, 12, 15, 12));
    subjectDirectoryHeaderHBox.setSpacing(10);
    subjectDirectoryHeaderHBox.setStyle("-fx-background-color: #336699;");

    // title
    addTitleText(subjectDirectoryHeaderHBox, "Select Subject directory");

    // info button
    String header = "Help";
    String title = "How to select subject directory";
    String info = "Subject directory is the directory that contains all the files for compare against the query file.\n "
        + "You can manually enter full path and then click OK.\nYou can navigate to directory by \"Choose path\" button.";
    addInfoButton(subjectDirectoryHeaderHBox, header, title, info);

    // HBox one
    // styling
    subjectDirectoryHBoxOne.setPadding(new Insets(15, 12, 1, 12));
    subjectDirectoryHBoxOne.setSpacing(10);
    subjectDirectoryHBoxOne.setStyle("-fx-background-color: #f2f2f2;");

    final TextField subjectDirectoryTextField = new TextField();

    subjectDirectoryTextField.setPromptText("Input full path.");

    final Button setPathManualyButton = new Button("OK");

    final Button setPathChooserButton = new Button("Select Path");

    subjectDirectoryHBoxOne.getChildren().addAll(subjectDirectoryTextField, setPathManualyButton, setPathChooserButton);

    // HBox two
    // styling
    subjectDirectoryHBoxTwo.setPadding(new Insets(1, 12, 15, 12));
    subjectDirectoryHBoxTwo.setSpacing(10);
    subjectDirectoryHBoxTwo.setStyle("-fx-background-color: #f2f2f2;");

    final Label pathInfoLabel = new Label("Please enter subject directory path above.");

    subjectDirectoryHBoxTwo.getChildren().add(pathInfoLabel);

    setPathManualyButton.setOnAction((ActionEvent t) -> {

      File tempDirectory = new File(subjectDirectoryTextField.getText());

      if (serviceData.setSubjectDirectory(new File(subjectDirectoryTextField.getText())))
      {
        pathInfoLabel.setText("path: " + tempDirectory.toString());
        pathInfoLabel.setTextFill(Color.GREEN);

      } else
      {
        pathInfoLabel.setText("Incorrect or empty directory,  Please enter a valid path.");
        pathInfoLabel.setTextFill(Color.RED);

      }

    });

    setPathChooserButton.setOnAction((ActionEvent t) -> {

      // directoryChooser = new DirectoryChooser();

      if (serviceData.setSubjectDirectory(new DirectoryChooser().showDialog(new Stage())))
      {

        pathInfoLabel.setText("path: " + serviceData.getSubjectDirectory().toString());
        pathInfoLabel.setTextFill(Color.GREEN);
      } else
      {
        pathInfoLabel.setText("Incorrect or empty directory,  Please enter a valid path.");
        pathInfoLabel.setTextFill(Color.RED);
      }

    });

    // query file selection

    // create and add container
    final VBox queryFileContainer = new VBox();
    mainVBox.getChildren().add(queryFileContainer);

    // create and add children's
    final HBox queryFileHeaderHBox = new HBox();
    final HBox queryFileHBoxOne = new HBox();
    final HBox queryFileHBoxTwo = new HBox();
    queryFileContainer.getChildren().addAll(queryFileHeaderHBox, queryFileHBoxOne, queryFileHBoxTwo);

    // header
    // styling
    queryFileHeaderHBox.setPadding(new Insets(15, 12, 15, 12));
    queryFileHeaderHBox.setSpacing(10);
    queryFileHeaderHBox.setStyle("-fx-background-color: #336699;");

    // title
    addTitleText(queryFileHeaderHBox, "Select query file");

    // info button
    String headerQueryFile = "Help";
    String titleQueryFile = "How to select query file";
    String infoQueryFile = "The query file is the one text file to compare against all files in the subject directory.\n "
        + "You can manually enter the full path an then press \"OK\".\n You can select the file using the navigation tool by pressing \"Select path\".";
    addInfoButton(queryFileHeaderHBox, headerQueryFile, titleQueryFile, infoQueryFile);

    // HBox one
    // styling
    queryFileHBoxOne.setPadding(new Insets(15, 12, 1, 12));
    queryFileHBoxOne.setSpacing(10);
    queryFileHBoxOne.setStyle("-fx-background-color: #f2f2f2;");

    final TextField queryFileTextField = new TextField();

    queryFileTextField.setPromptText("Input file full path");

    final Button setFileManualButton = new Button("OK");

    final Button setFileChooserButton = new Button("Select Path");

    queryFileHBoxOne.getChildren().addAll(queryFileTextField, setFileManualButton, setFileChooserButton);

    // HBox two
    // styling
    queryFileHBoxTwo.setPadding(new Insets(1, 12, 15, 12));
    queryFileHBoxTwo.setSpacing(10);
    queryFileHBoxTwo.setStyle("-fx-background-color: #f2f2f2;");

    final Label fileSelectionInfoLabel = new Label("Please enter query file path above.");

    queryFileHBoxTwo.getChildren().add(fileSelectionInfoLabel);

    // ok button action
    setFileManualButton.setOnAction((ActionEvent t) -> {

      if (serviceData.setQueryFile(new File(queryFileTextField.getText())))
      {
        fileSelectionInfoLabel.setText("file: " + serviceData.getQueryFile().toString());
        fileSelectionInfoLabel.setTextFill(Color.GREEN);

      } else
      {
        fileSelectionInfoLabel.setText("File not found, please enter correct file above.");
        fileSelectionInfoLabel.setTextFill(Color.RED);
      }

    });

    setFileChooserButton.setOnAction((ActionEvent t) -> {

      // fileChooser = new FileChooser();

      if (serviceData.setQueryFile(new FileChooser().showOpenDialog(new Stage())))
      {

        fileSelectionInfoLabel.setText("file: " + serviceData.getQueryFile().toString());
        fileSelectionInfoLabel.setTextFill(Color.GREEN);

      } else
      {
        fileSelectionInfoLabel.setText("File not found, please enter correct file above.");
        fileSelectionInfoLabel.setTextFill(Color.RED);
      }

    });

    // settings
    // create container and add
    final VBox settingsContainer = new VBox();
    mainVBox.getChildren().add(settingsContainer);

    // create and add childrens
    final HBox settingHeaderHBox = new HBox();
    final HBox settingsHBoxOne = new HBox();
    final HBox settingsHBoxTwo = new HBox();
    settingsContainer.getChildren().addAll(settingHeaderHBox, settingsHBoxOne, settingsHBoxTwo);

    // header
    // styling
    settingHeaderHBox.setPadding(new Insets(15, 12, 15, 12));
    settingHeaderHBox.setSpacing(10);
    settingHeaderHBox.setStyle("-fx-background-color: #336699;");

    // title
    addTitleText(settingHeaderHBox, "Select Shingles type and size");

    // info button
    String headerShingleType = "Help";
    String titleShingleType = "Shingles type and size";
    String infoShingleType = "You can select 2 types of shingles:\nK-Mers:the basic unit(element) is a fixed size  block of characters,"
        + " where size is the length of the block. Include white spaces\nGroup : the basic unit(elements) fixed-size group of words."
        + " Not case sensitive, not include punctuation, numbers are considered words, words attached by - are considered 1 word. \n Size is the length of character block or the number of words in the group.";
    addInfoButton(settingHeaderHBox, headerShingleType, titleShingleType, infoShingleType);

    // Hbox one
    // styling
    settingsHBoxOne.setPadding(new Insets(15, 12, 7, 12));
    settingsHBoxOne.setSpacing(10);
    settingsHBoxOne.setStyle("-fx-background-color: #f2f2f2;");

    final Label shingleTypeLabel = new Label("Shingles type: ");

    final ComboBox<ShingleType> shigleTypeComboBox = new ComboBox<ShingleType>();
    shigleTypeComboBox.getItems().addAll(ShingleType.K_Mers, ShingleType.Group);
    shigleTypeComboBox.setValue(serviceData.getShinglerType());

    settingsHBoxOne.getChildren().addAll(shingleTypeLabel, shigleTypeComboBox);

    // hbox two
    settingsHBoxTwo.setPadding(new Insets(7, 12, 15, 12));
    settingsHBoxTwo.setSpacing(10);
    settingsHBoxTwo.setStyle("-fx-background-color: #f2f2f2;");

    final Label shingleSizeLabel = new Label("Size:");

    Spinner<Integer> intSpinner = new Spinner<>(1, 100, serviceData.getShingleLength(), 1);
    // SpinnerValueFactory.IntegerSpinnerValueFactory intFactory =
    // (SpinnerValueFactory.IntegerSpinnerValueFactory) intSpinner
    // .getValueFactory();

    // intFactory.setValue(10);

    settingsHBoxTwo.getChildren().addAll(shingleSizeLabel, intSpinner);

    intSpinner.valueProperty().addListener(new ChangeListener<Integer>()
    {

      @Override
      public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue)
      {
        System.out.println(newValue);
        serviceData.setShingleLength(newValue);

      }

    });

    shigleTypeComboBox.valueProperty().addListener(new ChangeListener<ShingleType>()
    {
      public void changed(ObservableValue<? extends ShingleType> ov, ShingleType s, ShingleType selected)
      {

        serviceData.setShinglerType(selected);
        System.out.println(selected);
      }

    });

    // last
    // create and add container
    final HBox startButtonContainer = new HBox();
    mainVBox.getChildren().add(startButtonContainer);

    startButtonContainer.setPadding(new Insets(7, 0, 15, 0));
    startButtonContainer.setSpacing(10);
    startButtonContainer.setStyle("-fx-background-color: #f2f2f2;");

    // start button, style from
    // http://fxexperience.com/2011/12/styling-fx-buttons-with-css/
    Button startButton = new Button("Start comparing files...");
    startButton.setStyle(
        "-fx-background-color: \r\n" + "        #000000,\r\n" + "        linear-gradient(#7ebcea, #2f4b8f),\r\n"
            + "        linear-gradient(#426ab7, #263e75),\r\n" + "        linear-gradient(#336699, #223768);\r\n"
            + "    -fx-background-insets: 0,1,2,3;\r\n" + "    -fx-background-radius: 3,2,2,2;\r\n"
            + "    -fx-padding: 12 30 12 30;\r\n" + "    -fx-text-fill: white;\r\n" + "    -fx-font-size: 12px;");

    startButtonContainer.setAlignment(Pos.CENTER);
    startButtonContainer.setStyle("-fx-border-width: 1 0 0 0;-fx-border-insets: 0 0 0 0 ;-fx-border-color: black;");
    startButtonContainer.getChildren().addAll(startButton);
    HBox.setHgrow(startButtonContainer, Priority.ALWAYS);

    borderPane.setLeft(mainVBox);

    startButton.setOnAction((ActionEvent e) -> {

      // calculate query file
      CountOne c = new CountOne(serviceData.getShingleLength(), serviceData.getShinglerType());
      c.setFile(serviceData.getQueryFile());

      Future<CounterMap<Integer>> queryMap = c.calculate();

      LocalService localService = new LocalService(serviceData, resultsObservable, this, queryMap);

      Platform.runLater(
          new AddTaskBar<ShowProgress>(new ShowProgress("my", localService.progressProperty()), progresBarsMainVBox));

      localService.start();

    });

  }

  public class AddTaskBar<V> extends Task<V>
  {
    ShowProgress sp;
    VBox target;

    public AddTaskBar(ShowProgress sp, VBox target)
    {
      this.sp = sp;
      this.target = target;
    }

    @Override
    protected V call() throws Exception
    {
      target.getChildren().add(sp);
      return null;
    }

  }

  public class AddToResultListTask<V> extends Task<V>
  {

    CosineDistanceResult c;
    ObservableList<CosineDistanceResult> target;

    public AddToResultListTask(ObservableList<CosineDistanceResult> target, CosineDistanceResult c)
    {
      this.target = target;
      this.c = c;
    }

    @Override
    protected V call() throws Exception
    {
      target.add(c);
      return null;
    }
  }

  /**
   * From https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
   * 
   * @param hb
   */
  private void addInfoButton(HBox hb, String title, String header, String info)
  {
    StackPane stack = new StackPane();
    Rectangle helpIcon = new Rectangle(30.0, 25.0);
    helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[]
    {
        new Stop(0, Color.web("#4977A3")), new Stop(0.5, Color.web("#B0C6DA")), new Stop(1, Color.web("#9CB6CF")),
    }));
    helpIcon.setStroke(Color.web("#D0E6FA"));
    helpIcon.setArcHeight(3.5);
    helpIcon.setArcWidth(3.5);

    Text helpText = new Text("?");
    helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
    helpText.setFill(Color.WHITE);
    helpText.setStroke(Color.web("#7080A0"));

    stack.getChildren().addAll(helpIcon, helpText);

    stack.setAlignment(Pos.CENTER_RIGHT); // Right-justify nodes in stack
    StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"

    hb.getChildren().add(stack); // Add to HBox from Example 1-2
    HBox.setHgrow(stack, Priority.ALWAYS); // Give stack any extra space

    stack.setOnMouseClicked((MouseEvent e) -> {

      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle(title);
      alert.setHeaderText(header);
      alert.setContentText(info);

      alert.showAndWait();
    });
  }

  private void addTitleText(HBox hb, String title)
  {
    final Text text = new Text(title);
    DropShadow ds = new DropShadow();
    ds.setOffsetY(3.0f);
    ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
    text.setEffect(ds);
    text.setCache(true);
    text.setX(10.0f);
    text.setY(270.0f);
    text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    text.setFill(Color.WHITE);

    hb.getChildren().add(text);

  }
}
