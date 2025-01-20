
/**
 *
 * @author Álvaro Martín Martin.
 */

/*
ATENCIÓN: La variable index empieza en el array en 1, no en 0. EN ESTA IMPLEMENTACIÓN, LUEGO JAVA EN LOS ARRAYLIST LO HACE EMPEZANDO EN 0
          Es decir, si queremos añadir un elemento (value = 8) y le pasamos el index = 2, la lista  [1, 6, 3, 2]
          quedará de la siguiente manera: [1, 8, 6, 3, 2]

 */
public class FloatArrayList implements FloatList {

    private final Float[] vector;
    private final int MAX;
    private int pos; //indice en el que se colocará el siguiente elemento, está vacío

    public FloatArrayList(int tamaño) {
        this.MAX = tamaño;
        this.vector = new Float[MAX];
        this.pos = 0;
    }

    private void comprobarLimites(int index) {
        if ((index < 0) && (index > MAX)) {
            throw new RuntimeException("The index is out of limits");
        }

    }

    private boolean isFull() {
        return (pos == MAX);
    }

    @Override
    public int size() {
        return this.pos;
    }

    @Override
    public boolean isempty() {
        return (pos == 0);
    }

    @Override
    public void add(Float value) { //Añadir por el final
        if (this.isFull()) {
            throw new RuntimeException("The list is full");
        }

        vector[pos] = value;
        pos++;
    }

    public void addPrincipio(Float value) { //Método adicional. Inserción por cabecera.
        if (this.isFull()) {
            throw new RuntimeException("The list is full");
        }

        for (int i = pos - 1; i >= 0; i--) {
            vector[i + 1] = vector[i];
        }
        vector[0] = value;
    }

    @Override
    public void add(int index, Float value) {   //Recordar que index == posicion natural del array +1

        this.comprobarLimites(index);
        if (this.isFull()) {
            throw new RuntimeException("The list is full");
        }

        for (int i = pos - 1; i >= index - 1; i--) {
            vector[i + 1] = vector[i];
        }
        vector[index - 1] = value;
        pos++;
    }

    @Override
    public Float remove() { //Eliminar por cabecera
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }
        Float value = vector[0];
        for (int i = 0; i <= pos; i++) {
            vector[i] = vector[i + 1];
        }
        pos--;
        return value;
    }

    public Float removePorElFinal() { //Eliminar por el final
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }
        pos--;
        return vector[pos];
    }

    @Override
    public Float remove(int index) {

        this.comprobarLimites(index);
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }
        Float value = vector[index - 1];
        for (int i = index - 1; i >= pos; i++) {
            vector[i] = vector[i + 1];
        }
        pos--;

        return value;
    }

    @Override
    public Float get() { //Getear el primer elemento del array
        return vector[0];
    }

    @Override
    public Float get(int index) {
        return vector[index - 1];
    }

    @Override
    public int search(Float value) { //Devolverá 0 si no lo encuentra. Si lo encuentra devuelve el índice del array donde se encuentra
        if (this.isempty()) {
            return 0;
        }

        int i = 0;
        while ((i <= pos) && (!value.equals(vector[i]))) {
            i++;
        }
        if (value.equals(vector[i])) {
            return i + 1; //Sumo 1 porque el indice se está tratando como que es 1 numero más que la posicion natural del array
        }
        return 0;
    }

    @Override
    public boolean contains(Float value) {
        return (this.search(value) != 0);
    }

}
