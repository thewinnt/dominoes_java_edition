package net.thewinnt.dominoes.server;

import java.util.ArrayList;

import net.thewinnt.dominoes.server.Domino.Placement;

public class Side implements DominoHolder, Cloneable {
    ArrayList<Domino> contents = new ArrayList<Domino>();
    boolean is_blocked = false;
    int start_from_end = -1;

    public Side() {}

    public Side(ArrayList<Domino> contents) {
        this.contents = contents;
    }

    public Side(int start_from_end) {
        this.start_from_end = start_from_end;
    }

    public Side(ArrayList<Domino> contents, int start_from_end) {
        this.contents = contents;
        this.start_from_end = start_from_end;
    }

    public Side(ArrayList<Domino> contents, int start_from_end, boolean is_blocked) {
        this.contents = contents;
        this.start_from_end = start_from_end;
        this.is_blocked = is_blocked;
    }

    public boolean addDomino(Domino domino) {
        if (is_blocked) {
            return false;
        } else {
            Placement compatibility;
            if (start_from_end == -1) {
                compatibility = contents.get(contents.size() - 1).compatibilityType(domino);
            } else {
                compatibility = new Domino(start_from_end, start_from_end).compatibilityType(domino);
                start_from_end = -1;
            }
            if (compatibility != null) {
                domino.placement = compatibility;
                contents.add(domino);
                return true;
            } else {
                return false;
            }
        } 
    }

    public void blindAdd(Domino domino) {
        contents.add(domino);
        if (domino.placement == null) {
            domino.placement = Placement.NORMAL;
        }
    }

    public Placement compatibilityType(Domino domino) {
        if (is_blocked) {
            return null;
        } else if (start_from_end != -1) {
            return contents.get(contents.size() - 1).compatibilityType(domino);
        } else {
            return new Domino(start_from_end, start_from_end).compatibilityType(domino);
        }
    }

    public void setBlock(boolean block) {
        is_blocked = block;
    }

    @Override
    public Domino getDomino(int index) {
        return contents.get(index);
    }

    @Override
    public void setDomino(int index, Domino domino) {
        contents.set(index, domino);
        
    }

    @Override
    public void removeDomino(int index) {
        contents.remove(index);
    }

    @Override
    public void removeDomino(Domino domino) {
        contents.remove(domino);
    }

    @Override
    public int length() {
        return contents.size();
    }

    @Override
    public boolean hasDomino(Domino domino) {
        return contents.contains(domino);
    }

    @Override
    public void clear() {
        contents.clear();
    }

    @Override
    public Side clone() {
        ArrayList<Domino> new_contents = new ArrayList<Domino>(contents.size());
        for (int i = 0; i < contents.size(); i++) {
            new_contents.set(i, contents.get(i).clone());
        }
        return new Side(new_contents, start_from_end, is_blocked);
    }
}
