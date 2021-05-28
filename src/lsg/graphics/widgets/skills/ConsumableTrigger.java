package lsg.graphics.widgets.skills;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import lsg.consumables.Consumable;
import lsg.graphics.CSSFactory;
import lsg.graphics.CollectibleFactory;
import lsg.graphics.ImageFactory;

public class ConsumableTrigger extends SkillTrigger{

    private Consumable consumable ;

    public ConsumableTrigger(KeyCode keyCode, String text, Consumable consumable, SkillAction action) {
        super(keyCode, text, CollectibleFactory.getImageFor(consumable), action);
        this.getStylesheets().add(CSSFactory.getStyleSheet("ConsumableTrigger.css")) ;
        this.getStyleClass().add("consumable") ;
    }

    public void setConsumable(Consumable consumable) {
        this.consumable = consumable;
        this.setImage(CollectibleFactory.getImageFor(consumable));
        consumable.isEmptyProperty().addListener(((observable, oldValue, newValue) -> {
            setDisable(newValue.booleanValue());
        }));
    }

}
