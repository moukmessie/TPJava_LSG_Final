package lsg.graphics.widgets.characters.renderers;

import javafx.scene.image.Image;
import lsg.graphics.ImageFactory;

public class HeroRenderer extends CharacterRenderer {

    @Override
    protected Image[] loadAttackSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.HERO_ATTACK_ANIMATION) ;
    }

    @Override
    protected Image[] loadHurtSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.HERO_HURT_ANIMATION) ;
    }

    @Override
    protected Image[] loadDieSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.HERO_DIE_ANIMATION) ;
    }

    @Override
    protected Image[] loadIdleSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.HERO_IDLE_ANIMATION) ;
    }

    @Override
    protected Image[] loadWalkSprites(){
        return ImageFactory.getSprites(ImageFactory.SPRITES_ID.HERO_WALK_ANIMATION) ;
    }

}
