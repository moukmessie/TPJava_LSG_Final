package lsg.characters;

import lsg.weapons.Claw;
import lsg.weapons.Weapon;

public class Zombie extends Monster{

    private static String NAME = "Zombie" ;

    public Zombie() {
        super(NAME) ;
        setWeapon(new Weapon("Zombie's hands", 5, 20, 1, 1000));
        setSkinThickness(10);
    }
}
