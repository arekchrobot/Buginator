package pl.ark.chr.buginator.commons.util;

import java.util.Objects;

public class Pair<X, Y> {

    public final X _1;
    public final Y _2;

    public Pair(X x, Y y) {
        this._1 = x;
        this._2 = y;
    }

    @Override
    public String toString() {
        return "(" + _1 + "," + _2 + ")";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Pair)) {
            return false;
        }

        Pair<X, Y> that = (Pair<X, Y>) o;

        return Objects.equals(this._1, that._1) && Objects.equals(this._2, that._2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2);
    }
}
