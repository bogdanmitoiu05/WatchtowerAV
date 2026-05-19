package ro.sda.watchtower;

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
