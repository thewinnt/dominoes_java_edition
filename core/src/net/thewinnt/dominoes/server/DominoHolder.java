package net.thewinnt.dominoes.server;

public interface DominoHolder {
    public Domino getDomino(int index);

    public void setDomino(int index, Domino domino);

    public boolean addDomino(Domino domino);
    public void blindAdd(Domino domino);

    public void removeDomino(int index);
    public void removeDomino(Domino domino);

    public int length();

    public boolean hasDomino(Domino domino);

    public void clear();
}