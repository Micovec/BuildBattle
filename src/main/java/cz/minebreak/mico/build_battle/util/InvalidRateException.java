package cz.minebreak.mico.build_battle.util;

public class InvalidRateException extends ClassNotFoundException {

    public InvalidRateException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "No such rate option was found.\n" + super.getMessage();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
