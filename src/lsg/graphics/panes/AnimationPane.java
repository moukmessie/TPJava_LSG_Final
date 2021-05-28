package lsg.graphics.panes;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lsg.graphics.widgets.characters.renderers.HeroRenderer;
import lsg.graphics.widgets.characters.renderers.ZombieRenderer;

public class AnimationPane extends Pane {

    private double groundY = 0.8 ;

    private AnchorPane parent ;

    /**
     * Crée un panneau d'animation qui occupe tout l'espace de son parent
     * @param parent
     */
    public AnimationPane(AnchorPane parent) {
        this.parent = parent ;
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        this.maxWidthProperty().bind(parent.maxWidthProperty());
        this.prefWidthProperty().bind(parent.widthProperty());
    }

    public HeroRenderer createHeroRenderer(){
        HeroRenderer hero = new HeroRenderer() ;

        this.getChildren().add(hero) ;

        double size = 480 ;
        hero.setFitHeight(size);
        hero.setFitWidth(size);
        hero.setX(-size);

        hero.yProperty().bind(this.heightProperty().multiply(groundY).subtract(hero.fitHeightProperty().multiply(0.76)));

        return hero ;
    }

    public ZombieRenderer createZombieRenderer(){
        ZombieRenderer monster = new ZombieRenderer() ;

        double size = 400 ;
        monster.setFitHeight(size);
        monster.setFitWidth(size);
        this.getChildren().add(monster) ;
        monster.setX(this.getPrefWidth()) ;

        monster.yProperty().bind(this.heightProperty().multiply(groundY).subtract(monster.fitHeightProperty()));

        return monster ;
    }

    public void startDemo(){
        HeroRenderer heroRenderer = createHeroRenderer() ;
        heroRenderer.goTo(this.getPrefWidth()*0.5 - heroRenderer.getFitWidth()*0.65, null);
        ZombieRenderer zombieRenderer = createZombieRenderer() ;
        zombieRenderer.goTo(this.getPrefWidth()*0.5 - zombieRenderer.getBoundsInLocal().getWidth() * 0.15, null); ;
    }

    public void endDemo(){
        this.getChildren().removeAll(getChildren()) ;
    }

}

