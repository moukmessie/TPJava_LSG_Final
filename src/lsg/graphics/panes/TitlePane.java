package lsg.graphics.panes;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lsg.graphics.widgets.texts.GameLabel;

public class TitlePane extends VBox{

    private static final Duration ANIMATION_DURATION = Duration.millis(1500) ;
    private static final double ZOOM_SCALE = 1.5 ;
    private static final double ZOOM_Y = 0.25 ;

    private Scene scene ;
    private GameLabel titleLabel;

    public TitlePane(Scene scene, String title) {
        this.scene = scene ;

        this.setPadding(new Insets(10,0,0,0));

        titleLabel = new GameLabel(title) ;
        titleLabel.setText(title);
        titleLabel.setAlignment(Pos.CENTER);

        this.setAlignment(Pos.CENTER);
        this.getChildren().add(titleLabel) ;
    }

    public void zoomIn(EventHandler<ActionEvent> finishedHandler){
        titleLabel.translateYProperty().unbind();

        TranslateTransition tt = new TranslateTransition(ANIMATION_DURATION) ;
        tt.setToY(scene.getHeight()*ZOOM_Y);

        ScaleTransition st = new ScaleTransition(ANIMATION_DURATION) ;
        st.setToX(ZOOM_SCALE);
        st.setToY(ZOOM_SCALE);

        ParallelTransition pt = new ParallelTransition(tt, st) ;
        pt.setNode(titleLabel);
        pt.setCycleCount(1);
        pt.setOnFinished((actionEvent) ->{
            titleLabel.translateYProperty().bind(scene.heightProperty().multiply(ZOOM_Y));
            finishedHandler.handle(actionEvent);
        });
        pt.play();
    }

    public void zoomOut(EventHandler<ActionEvent> finishedHandler){
        titleLabel.translateYProperty().unbind();

        TranslateTransition tt = new TranslateTransition(ANIMATION_DURATION) ;
        tt.setToY(0);

        ScaleTransition st = new ScaleTransition(ANIMATION_DURATION) ;
        st.setToX(1);
        st.setToY(1);

        ParallelTransition pt = new ParallelTransition(tt, st) ;
        pt.setNode(titleLabel);
        pt.setCycleCount(1);
        pt.setOnFinished((event) ->{
            finishedHandler.handle(event);
        });
        pt.play();
    }
}
