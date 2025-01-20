package material;

/**
 *
 * @author Álvaro Martín Martín
 */
public class Pair<E,F> {
    private E first;
    private F second;

    public Pair(E first, F second) {
        this.first = first;
        this.second = second;
    }

    public E getKey() {
        return first;
    }

    public F getValue() {
        return second;
    }

    public void setFirst(E first) {
        this.first = first;
    }

    public void setSecond(F second) {
        this.second = second;
    }
    
    
    
}
