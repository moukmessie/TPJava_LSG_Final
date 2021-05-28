package lsg.graphics.widgets.skills;

import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.util.LinkedHashMap;

public class SkillBar extends HBox{

    private static LinkedHashMap<KeyCode, String> DEFAULT_BINDING = new LinkedHashMap<>() ;
    static {
        DEFAULT_BINDING.put(KeyCode.DIGIT1, "&") ;
        DEFAULT_BINDING.put(KeyCode.DIGIT2, "é") ;
        DEFAULT_BINDING.put(KeyCode.DIGIT3, "\"") ;
        DEFAULT_BINDING.put(KeyCode.DIGIT4, "'") ;
        DEFAULT_BINDING.put(KeyCode.DIGIT5, "(") ;
    }

    private SkillTrigger triggers[] ;

    private ConsumableTrigger consumableTrigger = new ConsumableTrigger(KeyCode.C, "c", null, null) ;

    public SkillBar() {
        super(10);
        this.setPrefHeight(110);

        this.setAlignment(Pos.CENTER);
        this.setFillHeight(false);

        init() ;
    }

    public SkillTrigger getTrigger(int slot){
        return triggers[slot] ;
    }

    public ConsumableTrigger getConsumableTrigger() {
        return consumableTrigger;
    }

    public void process(KeyCode code){
        if(isDisabled()) return;

        if(consumableTrigger.getKeyCode() == code){
            consumableTrigger.trigger();
            return;
        }

        for(SkillTrigger t: triggers){
            if(t.getKeyCode() == code){
                t.trigger();
                break;
            }
        }
    }

    public int getLength(){
        return triggers.length ;
    }

    private void init(){
        triggers = new SkillTrigger[DEFAULT_BINDING.size()] ;
        int i = 0 ;
        for(KeyCode c: DEFAULT_BINDING.keySet()){
            triggers[i] = new SkillTrigger(c, DEFAULT_BINDING.get(c), null, null) ;
            this.getChildren().add(triggers[i]) ;
            i++ ;
        }
        this.getChildren().addAll(new Rectangle(30, 0), consumableTrigger) ;
    }

}
