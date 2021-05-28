package lsg.graphics.widgets.texts;

import javafx.scene.Node;
import javafx.scene.control.Label;
import lsg.graphics.CSSFactory;

public class GameLabel extends Label {

    public GameLabel(String text, Node graphic) {
        super(text, graphic);
        this.applyStyle();
    }

    public GameLabel(String text) {
        super(text) ;
        this.applyStyle();
    }

    public GameLabel() {
        super();
        this.applyStyle();
    }

    private void applyStyle(){
        this.getStylesheets().add(CSSFactory.getStyleSheet("LSGFont.css")) ;
        this.getStyleClass().addAll("game-font", "game-font-fx") ;
    }
}
