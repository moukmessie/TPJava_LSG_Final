package lsg.graphics.panes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import lsg.graphics.ImageFactory;
import lsg.graphics.widgets.characters.statbars.StatBar;
import lsg.graphics.widgets.skills.SkillBar;
import lsg.graphics.widgets.texts.GameLabel;

public class HUDPane extends BorderPane{

    public static final String DEFAULT_TITLE = "Learning Souls Game" ;

    private MessagePane messagePane ;
    private StatBar heroStatBar, monsterStatBar;
    private GameLabel scoreLabel ;
    private SkillBar skillBar;

    private IntegerProperty score = new SimpleIntegerProperty();

    public HUDPane() {
        buildUI() ;
        score.addListener(((observable, oldValue, newValue) -> {
            scoreLabel.setText(String.format("%d", newValue.intValue()));
        }));
    }

    public MessagePane getMessagePane() {
        return messagePane;
    }

    public StatBar getHeroStatBar() {
        return heroStatBar;
    }

    public StatBar getMonsterStatBar() {
        return monsterStatBar;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public SkillBar getSkillBar() {
        return skillBar;
    }

    private void buildUI() {
        setPadding(new Insets(100, 10, 10, 10));
        buildCenter() ;
        buildTop() ;
        buildBottom() ;
    }

    private void buildCenter(){
        messagePane = new MessagePane() ;
        setCenter(messagePane);
    }

    private void buildBottom(){
        skillBar = new SkillBar() ;
        this.setBottom(skillBar);
    }

    private void buildTop(){

        BorderPane topPane = new BorderPane() ;
        heroStatBar = new StatBar() ;
        heroStatBar.getAvatar().setImage(ImageFactory.getSprites(ImageFactory.SPRITES_ID.HERO_HEAD)[0]);
        heroStatBar.prefWidthProperty().bind(topPane.widthProperty().multiply(0.3));
        heroStatBar.setMaxHeight(100);
        heroStatBar.setPrefHeight(100);
        topPane.setLeft(heroStatBar); ;

        monsterStatBar = new StatBar() ;
        monsterStatBar.getAvatar().setImage(ImageFactory.getSprites(ImageFactory.SPRITES_ID.ZOMBIE_HEAD)[0]) ;
        monsterStatBar.getAvatar().setRotate(30);
        monsterStatBar.prefWidthProperty().bind(topPane.widthProperty().multiply(0.3));
        monsterStatBar.setMaxHeight(100);
        monsterStatBar.setPrefHeight(100);
        monsterStatBar.flip();
        topPane.setRight(monsterStatBar); ;

        scoreLabel = new GameLabel() ;
        scoreLabel.setText("0");
        scoreLabel.setAlignment(Pos.CENTER);
        double zoom = 1.3 ;
        scoreLabel.setScaleX(zoom);
        scoreLabel.setScaleY(zoom);
        scoreLabel.setTranslateY(40);
        topPane.setCenter(scoreLabel);

        topPane.setTranslateY(-50);
        this.setTop(topPane);
    }

}
