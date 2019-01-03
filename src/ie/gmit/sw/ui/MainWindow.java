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

import ie.gmit.sw.ServiceData;
import ie.gmit.sw.ShingleType;
import ie.gmit.sw.base.Calculator;
import ie.gmit.sw.base.CountOne;
import ie.gmit.sw.base.Counter;
import ie.gmit.sw.base.CounterMap;
import ie.gmit.sw.base.FileShingleParser;
import ie.gmit.sw.base.MapBuilder;
import ie.gmit.sw.base.SingleThreadMapBuilder;
import ie.gmit.sw.data.CosineDistanceResult;
import ie.gmit.sw.data.UserSettings;
import ie.gmit.sw.ui.MainWindow.AddToResultListTask;
import ie.gmit.sw.ui.MainWindow1.WriteTask;
import javafx.beans.binding.ListBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class MainWindow extends BorderPane
{

    private ServiceData serviceData;// collect data for service(file,subject folder, settings)
    private static ListView<CosineDistanceResult> resultsView;// show results
    ObservableList<CosineDistanceResult> resultsObservable;// results list to place in List view above

    VBox progresBarsMainVBox;// box for dynamically add progress bar

    public MainWindow()
    {
        // initialization
        serviceData = new ServiceData();
        resultsView = new ListView<CosineDistanceResult>();
        resultsObservable = FXCollections.observableArrayList();

        addTopVBox(this);// banner
        addLeftVBox(this);// inputs and start work
        center(this);// show results
        addRightVBox(this);// task progress
    }

    /**
     * Add right UI to this border pane
     * 
     * @param borderPane main border pane (this)
     */
    private void addRightVBox(BorderPane borderPane)
    {
        final VBox mainContainer = new VBox();// main container

        final HBox title = new HBox();// box for title
        style1(title);// style title
        addTitleText(title, "Tasks");// add styled text
        mainContainer.getChildren().add(title);// add title to main container

        final ScrollPane sp = new ScrollPane();// scroll pane for tasks bar
        progresBarsMainVBox = new VBox(); // class variable box for tasks
        sp.setContent(progresBarsMainVBox);// add box to scrollpane

        mainContainer.getChildren().add(sp);// add to main container

        this.setRight(mainContainer);// add to window

    }

    /**
     * 
     * @param borderPane main border pane (this)
     */
    private void center(BorderPane borderPane)
    {
        resultsView.setItems(resultsObservable);// set items to result view

        final HBox main = new HBox();// main container for center

        resultsView.setPrefWidth(800);

        // set format for view cells
        resultsView.setCellFactory(new Callback<ListView<CosineDistanceResult>, ListCell<CosineDistanceResult>>()
        {
            @Override
            public ListCell<CosineDistanceResult> call(ListView<CosineDistanceResult> param)
            {
                return new Cell();

            }

        });

        final VBox resultsVBox = new VBox();// box for results
        final HBox resultsHeaderHBox = new HBox();// headers for reuslt
        // header
        Label labelName = new Label("File Name");
        labelName.setMinWidth(300);
        labelName.setMaxWidth(300);
        Label labelCosDistance = new Label("Cosine Distance");
        labelCosDistance.setMinWidth(100);
        labelCosDistance.setMaxWidth(100);
        labelCosDistance.setTextAlignment(TextAlignment.CENTER);
        resultsHeaderHBox.getChildren().addAll(labelName, labelCosDistance);
        // add header and result view to result box
        resultsVBox.getChildren().addAll(resultsHeaderHBox, resultsView);
        // add to center main
        main.getChildren().add(resultsVBox);
        // add to MainWindows
        borderPane.setCenter(main);

    }

    // format cell for view queue
    static private class Cell extends ListCell<CosineDistanceResult>
    {
        @Override
        public void updateItem(CosineDistanceResult item, boolean empty)
        {
            super.updateItem(item, empty);

            HBox hb = new HBox();

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

    /**
     * Top box, banner
     * 
     * @param borderPane
     */
    private void addTopVBox(BorderPane borderPane)
    {
        final VBox mainVBox = new VBox();

        style2(mainVBox, new Insets(12, 50, 24, 50));

        addHeaderText(mainVBox, "Cosine Distance Calculator");

        borderPane.setTop(mainVBox);

    }

    /**
     * All inputs and start service
     * 
     * @param borderPane this
     */
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
        style3(subjectDirectoryHeaderHBox, new Insets(15, 12, 15, 12));// add style
        addTitleText(subjectDirectoryHeaderHBox, "Select Subject directory");// add title
        // info button
        String header = "Help";
        String title = "How to select subject directory";
        String info = "Subject directory is the directory that contains all the files for compare against the query file.\n "
                + "You can manually enter full path and then click OK.\nYou can navigate to directory by \"Choose path\" button.";
        addInfoButton(subjectDirectoryHeaderHBox, header, title, info);

        // HBox one
        // styling
        style4(subjectDirectoryHBoxOne, new Insets(15, 12, 1, 12));
        // labels and input
        final TextField subjectDirectoryTextField = new TextField();
        subjectDirectoryTextField.setPromptText("Input full path.");
        final Button setPathManualyButton = new Button("OK");
        final Button setPathChooserButton = new Button("Select Path");
        subjectDirectoryHBoxOne.getChildren().addAll(subjectDirectoryTextField, setPathManualyButton,
                setPathChooserButton);

        // HBox two
        style4(subjectDirectoryHBoxTwo, new Insets(1, 12, 15, 12));// add style

        final Label pathInfoLabel = new Label("Please enter subject directory path above.");// for confirm input
        subjectDirectoryHBoxTwo.getChildren().add(pathInfoLabel);

        // set action to manual input button
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

        // set action to navigate button, use DirectoryChooser
        setPathChooserButton.setOnAction((ActionEvent t) -> {

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
        style3(queryFileHeaderHBox, new Insets(15, 12, 15, 12));// add style
        // title
        addTitleText(queryFileHeaderHBox, "Select query file");

        // info button
        String headerQueryFile = "Help";
        String titleQueryFile = "How to select query file";
        String infoQueryFile = "The query file is the one text file to compare against all files in the subject directory.\n "
                + "You can manually enter the full path an then press \"OK\".\n You can select the file using the navigation tool by pressing \"Select path\".";
        addInfoButton(queryFileHeaderHBox, headerQueryFile, titleQueryFile, infoQueryFile);

        // HBox one
        style4(queryFileHBoxOne, new Insets(15, 12, 1, 12));
        final TextField queryFileTextField = new TextField();

        queryFileTextField.setPromptText("Input file full path");

        final Button setFileManualButton = new Button("OK");

        final Button setFileChooserButton = new Button("Select Path");

        queryFileHBoxOne.getChildren().addAll(queryFileTextField, setFileManualButton, setFileChooserButton);

        // HBox two
        style4(queryFileHBoxTwo, new Insets(1, 12, 15, 12));// add style
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

        // navigate to path button action
        setFileChooserButton.setOnAction((ActionEvent t) -> {

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
        style3(settingHeaderHBox, new Insets(15, 12, 15, 12));// add style

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
        style4(settingsHBoxOne, new Insets(15, 12, 7, 12));
        final Label shingleTypeLabel = new Label("Shingles type: ");

        // combo box
        final ComboBox<ShingleType> shigleTypeComboBox = new ComboBox<ShingleType>();
        shigleTypeComboBox.getItems().addAll(ShingleType.K_Mers, ShingleType.Group);
        shigleTypeComboBox.setValue(serviceData.getShinglerType());

        settingsHBoxOne.getChildren().addAll(shingleTypeLabel, shigleTypeComboBox);

        // hbox two
        style4(settingsHBoxTwo, new Insets(7, 12, 15, 12));// add style

        final Label shingleSizeLabel = new Label("Size:");

        // spiner for shingles lengh
        Spinner<Integer> intSpinner = new Spinner<>(1, 100, serviceData.getShingleLength(), 1);
        settingsHBoxTwo.getChildren().addAll(shingleSizeLabel, intSpinner);

        // add on change listener to spinner
        intSpinner.valueProperty().addListener(new ChangeListener<Integer>()
        {
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue)
            {
                serviceData.setShingleLength(newValue);// set new value of shingle

            }

        });

        // change listenr to combo box
        shigleTypeComboBox.valueProperty().addListener(new ChangeListener<ShingleType>()
        {
            public void changed(ObservableValue<? extends ShingleType> ov, ShingleType s, ShingleType selected)
            {
                serviceData.setShinglerType(selected);// set new shingle type

            }

        });

        // last
        // create and add container
        final HBox startButtonContainer = new HBox();
        mainVBox.getChildren().add(startButtonContainer);

        startButtonContainer.setPadding(new Insets(7, 0, 15, 0));
        startButtonContainer.setSpacing(10);
        startButtonContainer.setStyle("-fx-background-color: #f2f2f2;");

        // start button
        Button startButton = new Button("Start comparing files...");
        styleButton1(startButton);
        startButtonContainer.setAlignment(Pos.CENTER);
        startButtonContainer.setStyle("-fx-border-width: 1 0 0 0;-fx-border-insets: 0 0 0 0 ;-fx-border-color: black;");
        startButtonContainer.getChildren().addAll(startButton);
        HBox.setHgrow(startButtonContainer, Priority.ALWAYS);

        borderPane.setLeft(mainVBox);

        /*
         * Main Code, start of calculations
         */
        startButton.setOnAction((ActionEvent e) -> {

            // calculate query file
            CountOne c = new CountOne(serviceData.getShingleLength(), serviceData.getShinglerType());
            c.setFile(serviceData.getQueryFile());

            Future<CounterMap<Integer>> queryMap = c.calculate();

            LocalService localService = new LocalService(serviceData, resultsObservable, this, queryMap);

            Label resultLabel = new Label();

            // add task bar to right pane
            Platform.runLater(new AddTaskBar<ShowProgress>(new ShowProgress(serviceData.getQueryFile().toString(),
                    localService.progressProperty(), resultLabel), progresBarsMainVBox));

            // add result when success
            localService.setOnSucceeded(new EventHandler<WorkerStateEvent>()
            {

                @Override
                public void handle(WorkerStateEvent t)
                {
                    System.out.println("call");
                    System.out.println("done:" + t.getSource().getValue());
                    Platform.runLater(new AddTextToLabel<Label>("done: " + t.getSource().getValue() + " seconds",resultLabel) );
                }
            });

            localService.start();

        });

    }

    // task for thread to add to UI

    
    public class AddTextToLabel<V> extends Task<V>
    {
        String result;
        Label target;

        public AddTextToLabel(String result, Label target)
        {
            this.result = result;
            this.target = target;
        }

        @Override
        protected V call() throws Exception
        {
            target.setText(result);;
            return null;
        }

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

    /*
     * Styling functions
     */

    /*
     * From https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
     */
    private void style1(HBox hbox)
    {
        hbox.setPadding(new Insets(15, 60, 15, 60));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");
    }

    // http://fxexperience.com/2011/12/styling-fx-buttons-with-css/
    private void styleButton1(Button button)
    {
        button.setStyle("-fx-background-color: \r\n" + "        #000000,\r\n"
                + "        linear-gradient(#7ebcea, #2f4b8f),\r\n" + "        linear-gradient(#426ab7, #263e75),\r\n"
                + "        linear-gradient(#336699, #223768);\r\n" + "    -fx-background-insets: 0,1,2,3;\r\n"
                + "    -fx-background-radius: 3,2,2,2;\r\n" + "    -fx-padding: 12 30 12 30;\r\n"
                + "    -fx-text-fill: white;\r\n" + "    -fx-font-size: 12px;");

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

    private void addInfoButton(HBox hb, String title, String header, String info)
    {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[]
        {
                new Stop(0, Color.web("#4977A3")), new Stop(0.5, Color.web("#B0C6DA")),
                new Stop(1, Color.web("#9CB6CF")),
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

    /*
     * from https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
     */
    private void addHeaderText(Pane pane, String headerText)
    {
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

        final Text text = new Text(headerText);

        text.setFont(Font.font(null, FontWeight.BOLD, 80));

        text.setFill(Color.ROYALBLUE);

        pane.getChildren().add(text);

        text.setEffect(blend);
    }

    private void style2(VBox vBox, Insets in)
    {
        vBox.setPadding(in);
        vBox.setSpacing(10);
        vBox.setStyle("-fx-background-color: #336699;");
    }

    private void style3(HBox vBox, Insets in)
    {
        vBox.setPadding(in);
        vBox.setSpacing(10);
        vBox.setStyle("-fx-background-color: #336699;");
    }

    private void style4(HBox vBox, Insets in)
    {
        vBox.setPadding(in);
        vBox.setSpacing(10);
        vBox.setStyle("-fx-background-color: #f2f2f2;");
    }
}
