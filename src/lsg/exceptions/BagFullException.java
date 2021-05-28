package lsg.exceptions;

import lsg.bags.Bag;

public class BagFullException extends Exception {

    private Bag bag ;

    public BagFullException(Bag bag) {
        super(bag.getClass().getSimpleName() + " is full") ;
        this.bag = bag;
    }

    public Bag getBag() {
        return bag;
    }

}
