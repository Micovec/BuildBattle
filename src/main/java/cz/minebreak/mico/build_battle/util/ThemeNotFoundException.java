package cz.minebreak.mico.build_battle.util;

public class ThemeNotFoundException extends IndexOutOfBoundsException {

    public ThemeNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "No such theme option was found.\n" + super.getMessage();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
