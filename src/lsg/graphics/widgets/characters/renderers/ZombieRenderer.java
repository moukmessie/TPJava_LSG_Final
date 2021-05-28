package lsg.graphics.widgets.characters.renderers;

import javafx.scene.image.Image;
import lsg.graphics.ImageFactory;

public class ZombieRenderer extends CharacterRenderer {

    public ZombieRenderer() {
        super();
        double rate = 0.5 ;
        getTimeline().setRate(rate);
    }

    @Override
    protected Image[] loadAttackSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.ZOMBIE_ATTACK_ANIMATION) ;
    }

    @Override
    protected Image[] loadHurtSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.ZOMBIE_HURT_ANIMATION) ;
    }

    @Override
    protected Image[] loadDieSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.ZOMBIE_DIE_ANIMATION) ;
    }

    @Override
    protected Image[] loadIdleSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.ZOMBIE_IDLE_ANIMATION) ;
    }

    @Override
    protected Image[] loadWalkSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.ZOMBIE_WALK_ANIMATION) ;
    }

}
