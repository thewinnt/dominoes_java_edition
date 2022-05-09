package net.thewinnt.dominoes.server;

public class Domino implements Cloneable {
    public final int a;
    public final int b;
    public Placement placement;

    public Domino(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public Domino(int a, int b, Placement placement) {
        this.a = a;
        this.b = b;
        this.placement = placement;
    }

    /**
     * Returns True if the given domino is similar (equal a and b) to this one
     * @param other The Domino to compare against
     * @return True if the two dominoes are similar, false if they are not
     */
    public boolean similar(Domino other) {
        return (this.a == other.a && this.b == other.b) || (this.a == other.b && this.b == other.b);
    }

    /**
     * Returns True if the given domino is the same (equal a and b respectively) as this one
     * @param other The Domino to compare against
     * @return True if the two dominoes are the same, false if they are not
     */
    public boolean sameAs(Domino other) {
        return (this.a == other.a && this.b == other.b);
    }

    /**
     * Returns True if the given domino is exactly the same as this one
     * @param other The Domino to compare against
     * @return True if the two dominoes are equal, false if they are not
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Domino) {
            Domino o = (Domino) other;
            return (this.a == o.a && this.b == o.b && this.placement == o.placement);
        } else {
            return false;
        }
    }

    /**
     * Returns the type of this Domino
     * @return A new {@link DominoType} representing this domino
     */
    public DominoType getType() {
        return new DominoType(a, b);
    }

    /**
     * Checks the compatibility between this and the specified Domino
     * @param other The Domino to compare against
     * @return The placement that the specified Domino should have when placed against this one,
     * or null, if they're incompatible
     */
    public Placement compatibilityType(Domino other) {
        // fun fact: this is copied from python, then adapted to java syntax
        if (this.a != other.a && this.a != other.b && this.b != other.a && this.b != other.b) {
            return null;
        } else if (this.placement == Placement.NORMAL) {
            if (this.b == other.a) {
                if (other.a == other.b) {
                    return Placement.DOUBLE;
                } else {
                    return Placement.NORMAL;
                }
            } else if (this.b == other.b) {
                if (other.a == other.b) {
                    return Placement.DOUBLE;
                } else {
                    return Placement.REVERSED;
                }
            } else {
                return null;
            }
        } else {
            if (this.a == other.a) {
                if (other.a == other.b) {
                    return Placement.DOUBLE;
                } else {
                    return Placement.NORMAL;
                }
            } else if (this.a == other.b) {
                if (other.a == other.b) {
                    return Placement.DOUBLE;
                } else {
                    return Placement.REVERSED;
                }
            } else {
                return null;
            }
        }
    }

    public record DominoType(int a, int b) {} // this is why you need java 14 and higher

    public enum Placement {
        NORMAL,
        REVERSED,
        DOUBLE;
    }

    @Override
    public Domino clone() {
        return new Domino(a, b, placement);
    }

    public boolean less_than(Domino other) {
        return a + b < other.a + other.b;
    }
}
