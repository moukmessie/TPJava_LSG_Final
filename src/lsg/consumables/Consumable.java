package lsg.consumables;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lsg.bags.Collectible;
import lsg.exceptions.ConsumeEmptyException;

public class Consumable implements Collectible{

    private String name ;
    private int capacity;
    private String stat;

    private BooleanProperty isEmpty  ;

    public Consumable(String name, int capacity, String stat) {
        this.name = name ;
        this.capacity = capacity ;
        this.stat = stat ;
        isEmpty = new SimpleBooleanProperty(capacity == 0) ;
    }

    public BooleanProperty isEmptyProperty() {
        return isEmpty;
    }

    @Override
    public int getWeight() {
        return 1 ;
    }

    public int getCapacity() {
        return capacity;
    }

    protected void setCapacity(int capacity){
        this.capacity = capacity ;
        isEmpty.set(capacity==0) ;
    }

    public String getStat() {
        return stat;
    }

    public String getName() {
        return name;
    }

    public int use() throws ConsumeEmptyException {
        if(capacity == 0) throw new ConsumeEmptyException(this) ;
        int val = capacity;
        setCapacity(0) ;
        return val ;
    }

    @Override
    public String toString() {
        return getName() + " [" + getCapacity() + " " + getStat() + " point(s)]" ;
    }

}
