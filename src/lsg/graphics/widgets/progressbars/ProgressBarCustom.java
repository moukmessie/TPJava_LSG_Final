package lsg.graphics.widgets.progressbars;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.ColorAdjust;

public class ProgressBarCustom extends ProgressBar{

    public ProgressBarCustom(String color) {
        this.setStyle("-fx-accent: " + color);
    }

}
