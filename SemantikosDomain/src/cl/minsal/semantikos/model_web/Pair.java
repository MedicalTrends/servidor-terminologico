package cl.minsal.semantikos.model_web;

/**
 * Created by root on 29-08-16.
 */
public class Pair<L,R> {

    private L first;

    private R second;

    public Pair(L left, R right) {
        this.first = left;
        this.second = right;
    }

    public L getFirst() { return first; }
    public R getSecond() { return second; }

    public void setSecond(R second) {
        this.second = second;
    }

    public void setFirst(L first) {
        this.first = first;
    }

    @Override
    public int hashCode() { return first.hashCode() ^ second.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return this.first.equals(pairo.getFirst()) &&
                this.second.equals(pairo.getSecond());
    }

}
