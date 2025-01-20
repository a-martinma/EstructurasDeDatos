
/**
 *
 * @author Álvaro Martín Martín.
 */

/*
ATENCIÓN: La variable index empieza en la lista en 1, no en 0. LUEGO JAVA EN LOS LINKEDLIST LO HACE EMPEZANDO EN 0
          Es decir, si queremos añadir un elemento (value = 8) y le pasamos el index = 2, la lista  [1, 6, 3, 2]
          quedará de la siguiente manera: [1, 8, 6, 3, 2]

 */
public class FloatLinkedList implements FloatList {

    public class FloatNode {

        private Float elem;
        private FloatNode next;

        public FloatNode(Float elem, FloatNode next) {
            this.elem = elem;
            this.next = next;
        }

        public Float getElem() {
            return elem;
        }

        public FloatNode getNext() {
            return next;
        }

        public void setElem(Float elem) {
            this.elem = elem;
        }

        public void setNext(FloatNode next) {
            this.next = next;
        }

    }

    private FloatNode head;
    private int size;

    public FloatLinkedList() {
        this.head = null;
        this.size = 0;
    }

    public FloatNode forward(int index) { //Método auxiliar para obtener el nodo anterior dado un índice
        if (index > 0 && index <= this.size + 1) { // + 1 porque si hay 4 elementos, lo puedes meter en la posicion 5 como máximo
            FloatNode ant = null;
            FloatNode actual = this.head;
            for (int i = 1; i < index; i++) {
                ant = actual;
                actual = actual.getNext();
            }
            return ant;
        } else {
            throw new RuntimeException("The index is out of limits");
        }

    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isempty() {
        return (size == 0);
    }

    @Override
    public void add(Float value) { //Añadir por cabecera
        FloatNode newNode = new FloatNode(value, head);
        this.head = newNode;
        this.size++;
    }

    @Override
    public void add(int index, Float value) { //Añadir en una posicion dada (index)
        FloatNode ant = this.forward(index);
        if (ant == null) {
            FloatNode newNode = new FloatNode(value, head); //estas 2 lineas se podrían haber sustituido por add(value)
            this.head = newNode;
        } else {
            FloatNode newNode = new FloatNode(value, ant.getNext());
            ant.setNext(newNode);  // ant = newNode  también valdría creo pero da una aviso de que la variable nunca se usa
        }
        size++;
    }

    @Override
    public Float remove() { //Eliminar el primer elemento. Hay que devolver el valor del nodo
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }

        Float value = this.head.getElem();
        this.head = this.head.getNext();
        size--;

        return value;

    }

    @Override
    public Float remove(int index) {
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }

        FloatNode ant = this.forward(index);
        if (ant == null) {
            return this.remove();
        } else {
            FloatNode actual = ant.getNext();
            ant.setNext(actual.getNext());  //ant = actual.getNext(); habia puesto pero me daba un aviso de que no se usa la variable
            size--;

            return actual.getElem();
        }

    }

    @Override
    public Float get() {
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }

        return this.head.getElem();

    }

    @Override
    public Float get(int index) {
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }

        FloatNode ant = this.forward(index);
        if (ant == null) {
            return this.head.getElem();
        } else {
            return ant.getNext().getElem();
        }

    }

    @Override
    public int search(Float value) { //Te pasan un valor y tienes que devolver el indice. Se devolverá 0 cuando el elemento no esté en la lista
        if (this.isempty()) {
            return 0;
        }

        FloatNode buscador = this.head;
        int count = 1;
        while ((count < this.size) && (!buscador.getElem().equals(value))) {
            count++;
            buscador = buscador.getNext();
        }
        if ((buscador.getElem().equals(value)) || count < this.size) {
            return count;
        }

        return 0;

    }

    @Override
    public boolean contains(Float value) {
        return (search(value) != 0);
    }

}
