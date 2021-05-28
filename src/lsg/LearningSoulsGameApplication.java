package lsg;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lsg.armor.DragonSlayerLeggings;
import lsg.buffs.rings.DragonSlayerRing;
import lsg.buffs.rings.RingOfDeath;
import lsg.characters.Character;
import lsg.characters.Hero;
import lsg.characters.Monster;
import lsg.characters.Zombie;
import lsg.consumables.food.SuperBerry;
import lsg.exceptions.*;
import lsg.graphics.CSSFactory;
import lsg.graphics.ImageFactory;
import lsg.graphics.panes.*;
import lsg.graphics.widgets.characters.renderers.CharacterRenderer;
import lsg.graphics.widgets.characters.renderers.HeroRenderer;
import lsg.graphics.widgets.characters.renderers.ZombieRenderer;
import lsg.graphics.widgets.characters.statbars.StatBar;
import lsg.graphics.widgets.skills.SkillBar;
import lsg.weapons.Sword;

import java.util.HashMap;

public class git LearningSoulsGameApplication extends Application{

    public static final String TITLE = "Learning Souls Game" ;

    public static final double DEFAULT_SCENE_WIDTH = 1200 ;
    public static final double DEFAULT_SCENE_HEIGHT = 800 ;

    private Scene scene ;
    private AnchorPane root ;

    private TitlePane gameTitle ;
    private CreationPane creationPane ;
    private AnimationPane animationPane ;
    private HUDPane hudPane ;
    private SkillBar skillBar;

    private Hero hero ;
    private Monster monster ;

    private String heroName ;

    private Character currentPlayer ;

    private BooleanProperty heroCanPlay = new SimpleBooleanProperty(false) ;

    private IntegerProperty score = new SimpleIntegerProperty(0);

    private HashMap<Character, CharacterRenderer> renderers = new HashMap<>() ;

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle(TITLE);
        createScene(stage);
        buildUI(scene, root) ;

        stage.show();
        startGame();

        hudPane.scoreProperty().bind(score);
    }

    private void startGame(){
        root.getChildren().add(gameTitle) ;
        gameTitle.zoomIn((actionEvent) -> {
            creationPane.setOpacity(0);
            creationPane.getNameField().setOnAction((actionEvent1)->{
                if(!(heroName = creationPane.getNameField().getText()).isEmpty()){
                    root.getChildren().remove(creationPane);
                    gameTitle.zoomOut((event)->{
                        play() ;
                    });
                }
            });
            root.getChildren().add(creationPane) ;
            creationPane.fadeIn((event -> {
                ImageFactory.preloadAll(() ->{
                    System.out.println("fini");
                } );
            }));
        });
    }

    private void play(){
        root.getChildren().add(animationPane) ;
        root.getChildren().add(hudPane) ;
        hudPane.getMessagePane().showMessage("START");
        hudPane.toFront();
        score.setValue(0);

        createHero();
        createSkills();

        createMonster(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                heroCanPlay.set(true) ;
            }
        });
    }

    private void createScene(Stage stage){
        root = new AnchorPane() ;
        scene = new Scene(root, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
        scene.getStylesheets().add(CSSFactory.getStyleSheet("LSG.css")) ;
        stage.setResizable(false);
        stage.setScene(scene);
    }

    private void createHero(){
        currentPlayer = hero = new Hero(heroName) ;
        hero.setWeapon(new Sword());
        hero.setArmorItem(new DragonSlayerLeggings(), 1);
        hero.setRing(new RingOfDeath(), 1);
        hero.setRing(new DragonSlayerRing(), 2);
        hero.setConsumable(new SuperBerry());

        StatBar heroStatBar = hudPane.getHeroStatBar() ;
        heroStatBar.getName().setText(hero.getName());
        heroStatBar.lifeProperty().bind(hero.lifeRateProperty());
        heroStatBar.staminaProperty().bind(hero.staminaRateProperty());

        HeroRenderer heroRenderer = (HeroRenderer)animationPane.createHeroRenderer() ;
        renderers.put(hero, heroRenderer) ;
        heroRenderer.goTo(scene.getWidth()*0.5 - heroRenderer.getFitWidth()*0.65, null);
    }

    private void createMonster(EventHandler<ActionEvent> finishedHandler){
        monster = new Zombie() ;

        StatBar monsterStatBar = hudPane.getMonsterStatBar() ;
        monsterStatBar.getName().setText(monster.getName());
        monsterStatBar.lifeProperty().bind(monster.lifeRateProperty());
        monsterStatBar.staminaProperty().bind(monster.staminaRateProperty());

        ZombieRenderer zombieRenderer = (ZombieRenderer)animationPane.createZombieRenderer() ;
        renderers.put(monster, zombieRenderer) ;
        zombieRenderer.goTo(scene.getWidth()*0.5 - zombieRenderer.getBoundsInLocal().getWidth() * 0.15, finishedHandler);
    }

    private void nextTurn(){

        if(currentPlayer == hero) {

            currentPlayer = monster ;
            if(!monster.isAlive()){
                score.set(score.get()+1) ;
                createMonster(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        characterAttack(monster, hero);
                    }
                });
            } else {
                characterAttack(monster, hero);
            }

        } else {

            currentPlayer = hero ;
            if(hero.isAlive()){
               heroCanPlay.set(true) ;
            }else{
                // GAME OVER
            }

        }
    }

    private void characterAttack(Character agressor, Character target){
        int attack ;
        try {
            attack = agressor.attack() ;
            renderers.get(agressor).attack(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int hit = target.getHitWith(attack) ;
                    if(target.isAlive()){
                        renderers.get(target).hurt(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                nextTurn();
                            }
                        });
                    }else{
                        renderers.get(target).die(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                animationPane.getChildren().removeAll(renderers.get(monster)) ;
                                nextTurn();
                            }
                        });
                    }
                }
            });
        } catch (WeaponNullException e) {
            hudPane.toFront();
            hudPane.getMessagePane().showMessage("NO WEAPON HAS BEEN EQUIPED");
            renderers.get(agressor).attack(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    nextTurn();
                }
            });
        } catch (WeaponBrokenException e) {
            hudPane.toFront();
            hudPane.getMessagePane().showMessage("WEAPON IS BROKEN");
            renderers.get(agressor).attack(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    nextTurn();
                }
            });
        } catch (StaminaEmptyException e) {
            hudPane.toFront();
            hudPane.getMessagePane().showMessage("NO MORE STAMINA");
            renderers.get(agressor).attack(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    nextTurn();
                }
            });
        }
    }

    private void characterRecuperate(Character character){
        character.recuperate();
        nextTurn();
    }

    private void heroConsume(){
        try {
            heroCanPlay.set(false) ;
            hero.consume();
            nextTurn();
        } catch (ConsumeException e) {
            e.printStackTrace();
            hudPane.getMessagePane().showMessage(e.getMessage());
            heroCanPlay.set(true) ;
        }
    }

    private void buildUI(Scene scene, AnchorPane root) {
        this.scene = scene ;

        gameTitle = new TitlePane(scene, LearningSoulsGameApplication.TITLE) ;
        AnchorPane.setTopAnchor(gameTitle, 0.0);
        AnchorPane.setLeftAnchor(gameTitle, 0.0);
        AnchorPane.setRightAnchor(gameTitle, 0.0);

        creationPane = new CreationPane() ;
        AnchorPane.setTopAnchor(creationPane, 0.0);
        AnchorPane.setBottomAnchor(creationPane, 0.0);
        AnchorPane.setLeftAnchor(creationPane, 0.0);
        AnchorPane.setRightAnchor(creationPane, 0.0);

        animationPane = new AnimationPane(root) ;

        hudPane = new HUDPane() ;
        AnchorPane.setTopAnchor(hudPane, 0.0);
        AnchorPane.setBottomAnchor(hudPane, 0.0);
        AnchorPane.setLeftAnchor(hudPane, 0.0);
        AnchorPane.setRightAnchor(hudPane, 0.0);

    }

    private void createSkills(){
        skillBar = hudPane.getSkillBar() ;
        skillBar.setDisable(!heroCanPlay.getValue());

        skillBar.getTrigger(0).setImage(ImageFactory.getSprites(ImageFactory.SPRITES_ID.ATTACK_SKILL)[0]);
        skillBar.getTrigger(0).setAction((() -> {
            heroCanPlay.set(false) ;
            characterAttack(hero, monster);
        }));

        skillBar.getTrigger(1).setImage(ImageFactory.getSprites(ImageFactory.SPRITES_ID.RECUPERATE_SKILL)[0]);
        skillBar.getTrigger(1).setAction((() -> {
            if(heroCanPlay.getValue()) {
                heroCanPlay.set(false) ;
                characterRecuperate(hero);
            }
        }));

        skillBar.getConsumableTrigger().setConsumable(hero.getConsumable());
        skillBar.getConsumableTrigger().setAction(()->{
            try {
                heroCanPlay.set(false) ;
                hero.consume();
                nextTurn();
            } catch (ConsumeException e) {
                hudPane.getMessagePane().showMessage(e.getMessage());
                e.printStackTrace();
            }
        });

        heroCanPlay.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                skillBar.setDisable(!newValue.booleanValue());
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                skillBar.process(event.getCode());
            }
        });
    }

}

