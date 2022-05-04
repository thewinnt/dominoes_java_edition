package net.thewinnt.dominoes.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.thewinnt.dominoes.Dominoes;
import net.thewinnt.dominoes.server.Domino.DominoType;
import net.thewinnt.dominoes.server.Domino.Placement;

public class DominoOperator {
    private Map<DominoType, Domino> dominoes;
    private Side[] sides;
    private Player[] players;

    private Set<Domino> free_dominoes;
    private Domino center = null;

    private int set_type;

    /**
     * A class that operates the dominoes without creating duplicates
     * @param set_type The amount of different digits on the dominoes (biggest digit - 1)
     * @param sides The number of sides
     * @param players The number of players
     */
    public DominoOperator(int set_type, int sides, int players) {
        this.set_type = set_type;
        dominoes = new HashMap<DominoType, Domino>();
        this.sides = new Side[sides];
        this.players =  new Player[players];
        free_dominoes = new HashSet<Domino>();
        Domino temp;
        for (int a = 0; a < set_type; a++) {
            for (int b = 0; b < set_type; b++) {
                if (a <= b) {
                    temp = new Domino(a, b);
                    dominoes.put(new DominoType(a, b), temp);
                    free_dominoes.add(temp);
                }
            }
        }

        for (int i = 0; i < sides; i++) {
            this.sides[i] = new Side();
        }
        for (int i = 0; i < players; i++) {
            this.players[i] = new Player();
        }
    }

    /**
     * Returns the Domino of the specified type
     * @param domino The type of the Domino (e.g. 5-6 is a <code> new DominoType(5, 6) </code>)
     * @return The specified domino
     */
    public Domino getDomino(DominoType domino) {
        return dominoes.get(domino);
    }

    /** Returns the Player at the specified index */
    public Player getPlayer(int index) {
        return players[index];
    }

    /** Returns the Side at the specified index */
    public Side getSide(int index) {
        return sides[index];
    }

    public boolean isAvailable(DominoType domino) {
        return free_dominoes.contains(getDomino(domino));
    }

    /**
     * Places a domino, if possible
     * @param player The player that has the domino
     * @param side The side to place it on
     * @param index The index of the domino in the player's inventory
     */
    public void placeDomino(int player, int side, int index) {
        Domino temp = players[player].getDomino(index);
        if (sides[side].addDomino(temp)) {
            players[player].removeDomino(index);
        }
    }

    /**
     * Places a domino without any checks. Useful for loading games.
     * @param player The player that has the domino
     * @param side The side to place it on
     * @param index The index of the domino in the player's inventory
     */
    public void blindPlace(int player, int side, int index) {
        Domino temp = players[player].getDomino(index);
        sides[side].blindAdd(temp);
        players[player].removeDomino(index);
    }

    /** Gives the specified domino to the player, if possible */
    public void giveDomino(int player, DominoType domino) {
        if (isAvailable(domino)) {
            players[player].addDomino(getDomino(domino));
            free_dominoes.remove(getDomino(domino));
        }
    }

    /** Gives the specified domino to the player */
    public void blindGive(int player, DominoType domino) {
        players[player].blindAdd(getDomino(domino));
        free_dominoes.remove(getDomino(domino));
    }

    /** Find the owner of the specified domino (null if nothing) */
    public DominoHolder getOwner(DominoType domino) {
        Domino dmn = getDomino(domino);
        if (free_dominoes.contains(dmn)) {
            return null;
        } else {
            for (Player player : players) {
                if (player.hasDomino(dmn)) {
                    return player;
                }
            }
            for (Side side : sides) {
                if (side.hasDomino(dmn)) {
                    return side;
                }
            }
        }
        return null;
    }

    /**
     * Sets the center domino to a selected one, taking it from a player that owns it, if possible
     * @param domino The domino that should be placed at the center
     * @param end_map A list of ints, each one will be the end of a respective side
     * @throws IllegalArgumentException if the end map has a different size than the number of sides OR
     * the owner of the domino is not a player
     */
    public void setCenter(DominoType domino, int[] end_map) throws IllegalArgumentException {
        DominoHolder owner = getOwner(domino);
        Domino dmn = getDomino(domino);
        if (end_map.length != sides.length) {
            throw new IllegalArgumentException("The end map should be the exact same length as the number of sides");
        }
        if (owner != null && owner instanceof Player) {
            owner.removeDomino(dmn);
            center = dmn;
            for (int i = 0; i < sides.length; i++) {
                sides[i].start_from_end = end_map[i];
            }
            if (dmn.a == dmn.b) {
                dmn.placement = Placement.DOUBLE;
            } else {
                dmn.placement = Placement.NORMAL;
            }
        } else {
            throw new IllegalArgumentException("The domino should be owned by a player");
        }
    }

    /**
     * Sets the center domino to a specified one
     * @param domino The domino to be placed at the center
     * @param end_map A list of ints, each one will be the end of a respective side
     * @throws IllegalArgumentException if the end map has a different size than the number of sides
     */
    public void blindSetCenter(DominoType domino, int[] end_map) throws IllegalArgumentException {
        DominoHolder owner = getOwner(domino);
        Domino dmn = getDomino(domino);
        if (end_map.length != sides.length) {
            throw new IllegalArgumentException("The end map should be the exact same length as the number of sides");
        }
        if (owner != null) {
            owner.removeDomino(dmn);
            center = dmn;
            for (int i = 0; i < sides.length; i++) {
                sides[i].start_from_end = end_map[i];
            }
            if (dmn.a == dmn.b) {
                dmn.placement = Placement.DOUBLE;
            } else {
                dmn.placement = Placement.NORMAL;
            }
        }
    }

    /** Resets the game */
    public void reset() {
        center = null;
        for (Side side : sides) {
            side.clear();
            side.start_from_end = 1;
        }
        for (Player player : players) {
            player.clear();
        }
        free_dominoes.addAll(dominoes.values());
    }

    public boolean giveRandom(int player, int count) {
        if (count < free_dominoes.size()) {
            return false;
        } else {
            for (int i = 0; i < count; i++) {
                getPlayer(player).addDomino((Domino) free_dominoes.toArray()[Dominoes.RANDOM.nextInt(free_dominoes.size())]);
            }
            return true;
        }
    }
}
