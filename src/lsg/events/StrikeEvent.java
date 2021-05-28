package lsg.events;

import lsg.characters.Character;

public class StrikeEvent extends LSGEvent {

    private Character target ;
    private int attack, hit ;

    public StrikeEvent(Character source, Character target, int attack, int dammages) {
        super(source);
        this.target = target ;
        this.attack = hit ;
        this.hit = hit ;
    }

    public Character getTarget() {
        return target;
    }

    public int getAttack() {
        return attack;
    }

    public int getHit() {
        return hit;
    }
}
