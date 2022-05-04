package net.thewinnt.dominoes.server;

import java.util.ArrayList;

public class Player implements DominoHolder {
    private ArrayList<Domino> inventory = new ArrayList<Domino>();
    private int score = 0;
    private int victories = 0;
    private String name = "";
    public int[] tags = null;

    public Player() {}
    public Player(ArrayList<Domino> inventory) {
        this.inventory = inventory;
    }

    public Player(String name) {
        this.name = name;
    }

    public Player(ArrayList<Domino> inventory, int score, int victories, String name) {
        this.inventory = inventory;
        this.score = score;
        this.victories = victories;
        this.name = name;
    }

    public Player(ArrayList<Domino> inventory, int score, int victories, String name, int[] tags) {
        this.inventory = inventory;
        this.score = score;
        this.victories = victories;
        this.name = name;
        this.tags = tags;
    }

    @Override
    public Domino getDomino(int index) {
        return inventory.get(index);
    }

    @Override
    public void setDomino(int index, Domino domino) {
        inventory.set(index, domino);
        
    }

    @Override
    public boolean addDomino(Domino domino) {
        inventory.add(domino);
        return true;
    }

    @Override
    public void blindAdd(Domino domino) {
        addDomino(domino);
        
    }

    @Override
    public void removeDomino(int index) {
        inventory.remove(index);
        
    }

    @Override
    public void removeDomino(Domino domino) {
        inventory.remove(domino);
        
    }

    @Override
    public int length() {
        return inventory.size();
    }

    @Override
    public boolean hasDomino(Domino domino) {
        return inventory.contains(domino);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int new_score) {
        score = new_score;
    }

    public int getVictories() {
        return victories;
    }

    public void victory() {
        victories++;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String new_name) {
        name = new_name;
    }
}
