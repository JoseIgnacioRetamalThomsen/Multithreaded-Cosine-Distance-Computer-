package ie.gmit.sw.ui;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class ShowProgress extends HBox
{
  private String text;
  private ProgressBar progressBar;
  
  public ShowProgress(String text, ReadOnlyDoubleProperty readOnlyDoubleProperty)
  
  {
    super();
    this.text = text;
    this.progressBar = new ProgressBar();
    
    final Label label = new Label(text);
    
    this.setSpacing(5);
    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(label, progressBar);
    
    progressBar.progressProperty().bind(readOnlyDoubleProperty);
    
  }

  public void bind()
  {
    
  }
}
