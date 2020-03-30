package cz.minebreak.mico.build_battle.util;

public class NotEnoughPlotsException extends ArrayIndexOutOfBoundsException {

    public NotEnoughPlotsException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "There are more players than plots.\n" + super.getMessage();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
