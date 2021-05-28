package lsg.graphics.widgets.skills;

import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lsg.graphics.CSSFactory;


public class SkillTrigger extends AnchorPane{

    private static final int SIZE = 50 ;

    private ImageView view ;
    private Label text ;
    private KeyCode keyCode ;
    private SkillAction action ;

    private ColorAdjust desaturate ;

    public SkillTrigger(KeyCode keyCode, String text, Image image, SkillAction action) {

        this.view = new ImageView(image) ;
        this.text = new Label(text) ;
        this.keyCode = keyCode ;
        this.action = action ;

        desaturate = new ColorAdjust();
        desaturate.setSaturation(-1);
        desaturate.setBrightness(0.6);

        buildUI() ;
        addListeners() ;
    }

    public void setAction(SkillAction action) {
        this.action = action;
    }

    public void setImage(Image image){
        this.view.setImage(image);
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public void trigger(){
        if(isDisabled() || this.action == null) return;
        animate() ;
        this.action.execute();
    }

    private void animate(){
        ScaleTransition st = new ScaleTransition(Duration.millis(100)) ;
        st.setNode(this);
        double zoom = 1.3 ;
        st.setToX(zoom);
        st.setToY(zoom);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }

    private void buildUI(){
        //        this.getStylesheets().add(getClass().getResource("SkillTrigger.css").toExternalForm()) ;
        this.getStylesheets().add(CSSFactory.getStyleSheet("SkillTrigger.css")) ;

        this.getStyleClass().add("skill") ;

        view.setFitHeight(SIZE);
        view.setFitWidth(SIZE);

        text.setAlignment(Pos.CENTER);
        text.getStyleClass().addAll("skill-text") ;

        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);

        AnchorPane.setTopAnchor(text, 0.0);
        AnchorPane.setBottomAnchor(text, 0.0);
        AnchorPane.setLeftAnchor(text, 0.0);
        AnchorPane.setRightAnchor(text, 0.0);

        getChildren().addAll(view, text) ;
    }

    private void addListeners(){

        this.setOnMouseClicked(event -> {
            trigger();
        });

        disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    view.setEffect(desaturate);
                }else{
                    view.setEffect(null);
                }
            }
        }) ;
    }
}
