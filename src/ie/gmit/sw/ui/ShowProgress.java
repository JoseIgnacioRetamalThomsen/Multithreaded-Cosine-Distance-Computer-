/*
 * A Multithreaded Cosine Distance Computer. 
 * Object Oriented Programming. 
 * Galway-Mayo Institute of technologies.
 * Jose I. Retamal
 * 
 */

package ie.gmit.sw.ui;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A VBox with all progress bar components to show in UI.
 * 
 * @author Jose I. Retamal
 *
 */
public class ShowProgress extends VBox
{

    private final ProgressBar progressBar;
    private final ProgressIndicator pin;

    /**
     * Create object with all references and parameters needed
     * 
     * @param text                   name of the task
     * @param readOnlyDoubleProperty for link to progress bar
     * @param resultLabel            for show result, only reference which is use also in
     *                               {@code MainWindow}
     */
    public ShowProgress(String text, ReadOnlyDoubleProperty readOnlyDoubleProperty, Label resultLabel)

    {
        super();
        final HBox hb1 = new HBox();// task bar
        final HBox hb2 = new HBox();// time result label
        this.progressBar = new ProgressBar();
        this.pin = new ProgressIndicator();
        final Label label = new Label(text);// set text and create label
        label.setMaxWidth(120);

        hb1.setSpacing(5);
        hb1.setAlignment(Pos.CENTER);
        hb1.getChildren().addAll(label, progressBar, pin);

        resultLabel.setText("Working...");

        hb2.setSpacing(5);
        hb2.setAlignment(Pos.CENTER);
        hb2.getChildren().addAll(resultLabel);

        this.getChildren().addAll(hb1, hb2);

        // bind progress bar to task
        progressBar.progressProperty().bind(readOnlyDoubleProperty);

        pin.progressProperty().bind(readOnlyDoubleProperty);

    }

}
