package lsg.graphics.panes;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lsg.characters.Character;
import lsg.characters.Hero;
import lsg.characters.Monster;
import lsg.graphics.widgets.characters.renderers.CharacterRenderer;
import lsg.graphics.widgets.characters.renderers.HeroRenderer;
import lsg.graphics.widgets.characters.renderers.ZombieRenderer;

public class AnimationPane_old extends Pane {

    private double groundY = 0.8 ;

    private AnchorPane parent ;

    /**
     * Crée un panneau d'animation qui pren la taille maximale
     * @param parent
     */
    public AnimationPane_old(AnchorPane parent) {
        this.parent = parent ;
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        this.maxWidthProperty().bind(parent.maxWidthProperty());
        this.prefWidthProperty().bind(parent.widthProperty());
    }

    public CharacterRenderer createRendererFor(Character character){
        CharacterRenderer renderer = null ;
        if(character instanceof Hero){
            renderer = createHeroRenderer() ;
        }else if(character instanceof Monster){
            renderer = createZombieRenderer() ;
        }
        return renderer ;
    }

    private HeroRenderer createHeroRenderer(){
        HeroRenderer hero = new HeroRenderer() ;

        this.getChildren().add(hero) ;

        double size = 480 ;
        hero.setFitHeight(size);
        hero.setFitWidth(size);
        hero.setX(-size);

        hero.yProperty().bind(this.heightProperty().multiply(groundY).subtract(hero.fitHeightProperty().multiply(0.76)));

        return hero ;
    }

    private ZombieRenderer createZombieRenderer(){
        ZombieRenderer monster = new ZombieRenderer() ;

        double size = 400 ;
        monster.setFitHeight(size);
        monster.setFitWidth(size);
        this.getChildren().add(monster) ;
        monster.setX(this.getPrefWidth()) ;

        monster.yProperty().bind(this.heightProperty().multiply(groundY).subtract(monster.fitHeightProperty()));

        return monster ;
    }

}

