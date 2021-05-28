package lsg.graphics.panes;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lsg.graphics.widgets.texts.GameLabel;

public class CreationPane extends VBox{

    private TextField nameField ;

    public CreationPane() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        GameLabel invite = new GameLabel() ;
        invite.setText("Player Name");
        invite.setAlignment(Pos.CENTER);

        nameField = new TextField() ;
        nameField.setMaxWidth(200);

        this.getChildren().addAll(invite, nameField) ;
    }

    public TextField getNameField() {
        return nameField;
    }

    public void fadeIn(EventHandler<ActionEvent> finishedHandler){
        FadeTransition ft = new FadeTransition() ;
        ft.setNode(this);
        ft.setDuration(Duration.millis(1000));
        ft.setToValue(1);
        ft.setOnFinished((event)->{
            finishedHandler.handle(event);
        });
        ft.play();
    }
}
