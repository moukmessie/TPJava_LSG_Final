package lsg.events;

import lsg.characters.Character;

public class LSGEvent {

    private Character source;

    public LSGEvent(Character source) {
        this.source = source ;
    }

    public Character getSource() {
        return source;
    }
}
