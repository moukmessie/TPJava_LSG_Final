package lsg.graphics.widgets.characters.statbars;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lsg.graphics.widgets.texts.GameLabel;
import lsg.graphics.widgets.progressbars.ProgressBarLife;
import lsg.graphics.widgets.progressbars.ProgressBarStamina;


public class StatBar extends BorderPane{

    private static double DEFAULT_WIDTH = 300 ;
    private static double DEFAULT_HEIGHT = 100 ;

    private ImageView avatar ;
    private ProgressBarLife lifeBar ;
    private ProgressBarStamina stamBar ;
    private HBox namePane ;
    private GameLabel name ;


    public StatBar() {
        buidUI() ;
    }

    public void flip(){
        name.setScaleX(name.getScaleX()*-1);
        this.setScaleX(this.getScaleX()*-1);
    }

    public DoubleProperty lifeProperty(){
        return lifeBar.progressProperty() ;
    }

    public DoubleProperty staminaProperty(){
        return stamBar.progressProperty() ;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public GameLabel getName() {
        return name;
    }

    public void setLife(double percent){
        lifeBar.setProgress(percent);
    }

    public void setStamina(double percent){
        stamBar.setProgress(percent);
    }

    private void buidUI(){
        avatar = new ImageView() ;
        avatar.setPreserveRatio(true);
        avatar.fitHeightProperty().bind(this.heightProperty());

        namePane = new HBox() ;
        namePane.prefHeightProperty().bind(this.heightProperty().multiply(0.55));
        namePane.setAlignment(Pos.BOTTOM_LEFT);
        name = new GameLabel() ;
        name.setStyle("-fx-font-size: 33px");
        namePane.getChildren().add(name) ;

        VBox statPane = new VBox() ;
        statPane.prefHeightProperty().bind(this.heightProperty().multiply(0.45));
        lifeBar = new ProgressBarLife() ;
        stamBar = new ProgressBarStamina() ;
        lifeBar.setMinHeight(0);
        stamBar.setMinHeight(0);
        lifeBar.prefHeightProperty().bind(statPane.prefHeightProperty().multiply(0.4));
        stamBar.prefHeightProperty().bind(statPane.prefHeightProperty().multiply(0.3));
        lifeBar.setMaxWidth(Double.MAX_VALUE);
        stamBar.maxWidthProperty().bind(lifeBar.widthProperty().multiply(0.6));

        statPane.getChildren().addAll(lifeBar,stamBar) ;

        VBox centerPane = new VBox() ;
        centerPane.getChildren().add(namePane) ;
        centerPane.getChildren().add(statPane) ;

        this.setLeft(avatar); ;
        this.setCenter(centerPane);

        this.setWidth(DEFAULT_WIDTH);
        this.setHeight(DEFAULT_HEIGHT);
    }
}
