package ro.sda.watchtower;

/**
 * Culoarea unui nod dintr-un arbore red-black
 */
public enum RBNodeColor {
    BLACK,
    RED;

    @Override
    public String toString() {
        return switch (this){
            case RED -> "RED";
            case BLACK -> "BLACK";
        };
    }
}
