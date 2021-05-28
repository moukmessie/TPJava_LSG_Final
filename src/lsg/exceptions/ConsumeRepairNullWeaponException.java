package lsg.exceptions;

import lsg.consumables.Consumable;

public class ConsumeRepairNullWeaponException extends ConsumeException {

    private static String MSG = "Trying to repair null weapon !" ;

    public ConsumeRepairNullWeaponException(Consumable consumable) {
        super(MSG, consumable);
    }
}
