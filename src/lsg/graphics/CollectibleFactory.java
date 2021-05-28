package lsg.graphics;

import javafx.scene.image.Image;
import lsg.bags.Collectible;
import lsg.consumables.drinks.SmallStamPotion;
import lsg.consumables.food.SuperBerry;

public class CollectibleFactory {

    public static Image getImageFor(Collectible collectible){
        Image image = null ;

        if(collectible instanceof SmallStamPotion){
            image = ImageFactory.getSprites(ImageFactory.SPRITES_ID.SMALL_STAM_POTION)[0] ;
        }else if(collectible instanceof SuperBerry){
            image = ImageFactory.getSprites(ImageFactory.SPRITES_ID.SUPER_BERRY)[0] ;
        }

        return image ;
    }

}
