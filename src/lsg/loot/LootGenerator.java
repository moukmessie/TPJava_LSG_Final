package lsg.loot;

import lsg.armor.BlackWitchVeil;
import lsg.armor.DragonSlayerLeggings;
import lsg.armor.RingedKnightArmor;
import lsg.bags.Collectible;
import lsg.buffs.rings.DragonSlayerRing;
import lsg.buffs.rings.RingOfDeath;
import lsg.buffs.rings.RingOfSwords;
import lsg.consumables.drinks.Coffee;
import lsg.consumables.drinks.Whisky;
import lsg.consumables.drinks.Wine;
import lsg.consumables.food.Americain;
import lsg.consumables.food.Hamburger;
import lsg.consumables.repair.RepairKit;
import lsg.helper.Dice;

public class LootGenerator {

    private static Class<? extends Collectible>[] lootable = new Class[]{
            BlackWitchVeil.class,
            DragonSlayerLeggings.class,
            RingedKnightArmor.class,
            DragonSlayerRing.class,
            RingOfDeath.class,
            RingOfSwords.class,
            Coffee.class,
            Whisky.class,
            Wine.class,
            Americain.class,
            Hamburger.class,
            RepairKit.class
    };

    private static Dice dice = new Dice(lootable.length) ;

    public static Collectible generateLoot() throws IllegalAccessException, InstantiationException {
        return lootable[dice.roll()].newInstance() ;

    }

    public static void main(String[] args) {
        try {
            for (int i=0 ; i<100 ; i++) System.out.println(generateLoot());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
