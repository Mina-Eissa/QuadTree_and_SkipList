class Pair<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements Comparable<Pair<T1, T2>> {
    T1 first;
    T2 second;

    Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public Pair() {

    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair<T1, T2> p = (Pair<T1, T2>) o;
            return p.first.equals(first) && p.second.equals(second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return first.hashCode() * 31 + second.hashCode();
    }

    @Override
    public int compareTo(Pair<T1, T2> o) {
        if (first.compareTo(o.first) == 0)
            return second.compareTo(o.second);
        return first.compareTo(o.first);
    }
}