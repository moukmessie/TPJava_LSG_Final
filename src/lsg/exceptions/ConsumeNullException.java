package lsg.exceptions;

import lsg.consumables.Consumable;

public class ConsumeNullException extends ConsumeException {

    private static String MSG = "Consumable is null !" ;

    public ConsumeNullException(Consumable consumable) {
        super(MSG, consumable);
    }
}
