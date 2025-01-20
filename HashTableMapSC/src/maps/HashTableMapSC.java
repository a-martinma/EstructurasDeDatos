package maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author Álvaro Martín Martín.
 * @param <K> The key
 * @param <V> The stored value
 */
public class HashTableMapSC<K, V> implements Map<K, V> {

    private LinkedList<Entry<K, V>>[] array; //Array que en cada posicion guarda una lista de entradas clave-valor
    private int tamaño; //Tamaño máximo de entradas en el array
    private int size; //Numero de entradas que hay en el array
    
    //Variables necesarias para la funcion hashValue(). Encargada de transformar el código hash a un numero entero en el rango de posiciones válidas(tamaño del array). Necesarias para MAD.
    private int primo; 
    private int a, b; 
    

    private class HashEntry<T, U> implements Entry<T, U> {

        private T clave;
        private U valor;

        public HashEntry(T k, U v) {
            this.clave = k;
            this.valor = v;
        }

        @Override
        public U getValue() {
            return this.valor;
        }

        @Override
        public T getKey() {
            return this.clave;
        }

        public U setValue(U val) {
            return this.valor = val;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 83 * hash + Objects.hashCode(this.clave);
            hash = 83 * hash + Objects.hashCode(this.valor);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final HashEntry<?, ?> other = (HashEntry<?, ?>) obj;
            if (!Objects.equals(this.clave, other.clave)) {
                return false;
            }
            return Objects.equals(this.valor, other.valor);
        }

        @Override
        public String toString() {
            return "HashEntry{" + "clave=" + clave + ", valor=" + valor + '}';
        }
    }

    private class HashTableMapIterator<T, U> implements Iterator<Entry<T, U>> {
        
        private ArrayList<HashEntry<T, U>>[] current;
        private int numElems;
        private Queue<HashEntry<T, U>> cola;
        

        //Ejercicio 2.2
        public HashTableMapIterator(ArrayList<HashEntry<T, U>>[] map, int numElems) {
            this.current = map;
            this.numElems = numElems;
            cola = new LinkedList();
            
            for(ArrayList<HashEntry<T, U>> posicion : this.current){ //Se añade a la cola todas las entradas del mapa
                if (posicion != null)
                    for(HashEntry<T, U> entry: posicion)
                        cola.add(entry);
            }
        }

        private void goToNextElement() {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public boolean hasNext() {
            return(!cola.isEmpty());
        }

        @Override
        public Entry<T, U> next() {
            return cola.remove();
        }

        @Override
        public void remove() { 
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private class HashTableMapKeyIterator<T, U> implements Iterator<T> {
        
        private HashTableMapIterator<T, U> current;
        private Queue<T> cola;

        public HashTableMapKeyIterator(HashTableMapIterator<T, U> it) {
            this.current=it;
            cola = new LinkedList();
            while(current.hasNext()){ //Utilizo el iterador de antes para añadir todas las claves  de todas las entradas a la cola
                Entry<T, U> entrada = current.next();
                cola.add(entrada.getKey());
            }
        }

        @Override
        public T next() {
            return cola.remove();
        }

        @Override
        public boolean hasNext() {
            return (!cola.isEmpty());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private class HashTableMapValueIterator<T, U> implements Iterator<U> {
        
        private HashTableMapIterator<T, U> current;
        private Queue<U> cola;

        public HashTableMapValueIterator(HashTableMapIterator<T, U> it) {
            this.current=it;
            cola = new LinkedList();
            while(current.hasNext()){ //Utilizo el iterador de antes para añadir todas las claves  de todas las entradas a la cola
                Entry<T, U> entrada = current.next();
                cola.add(entrada.getValue());
            }
        }

        @Override
        public U next() {
            return cola.remove();
        }

        @Override
        public boolean hasNext() {
            return (!cola.isEmpty());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not implemented.");
        }
    }

    private int calcularPosicion(K key) { //Se supone que la clave simepre es un numero ya que en las trasparencias, los ejemplos son así
        /*int keyNum = (int) key; ASI NO
        return (keyNum % this.tamaño);
        */
        int hash = key.hashCode();
        return (hash % this.tamaño);
    }

    /*private Entry<K, V> checkEntry(HashEntry<K, V> hashEntry) { Lo comento porque no lo uso
        if (hashEntry == null || !(hashEntry instanceof Entry)) {
            throw new RuntimeException("Entrada no válida");
        }
        return (Entry<K, V>) hashEntry;
    }*/

    /**
     * Creates a hash table
     */
    public HashTableMapSC() {
        this.tamaño = 20;
        this.size = 0;
        this.array = new LinkedList[tamaño];
        this.primo = 29383; //Por ejemplo
    }

    /**
     * Creates a hash table.
     *
     * @param cap initial capacity
     */
    public HashTableMapSC(int cap) {
        this.tamaño = cap;
        this.size = 0;
        this.array = new LinkedList[cap];
        this.primo = 29383; //Por ejemplo
    }

    /**
     * Creates a hash table with the given prime factor and capacity.
     *
     * @param p prime number
     * @param cap initial capacity
     */
    public HashTableMapSC(int p, int cap) {
        this.tamaño = cap;
        this.size = 0;
        this.array = new LinkedList[cap];
        if (p < cap) {
            throw new RuntimeException("El numero primo debe ser mayor que la capacidad");
        }
        this.primo = p;
    }

    /**
     * Hash function applying MAD method to default hash code.
     *
     * @param key Key
     * @return
     */
    protected int hashValue(K key) { //Se encarga de transformar el código hash a un numero entero en el rango de posiciones válidas(tamaño del array)
        int hash = key.hashCode();
        Random random = new Random();
        a = random.nextInt(this.primo);//Genera numero aleatorio entre 0 y primo-1
        b = random.nextInt(this.primo);

        return (((hash * a + b) % primo) % this.tamaño); //MAD --> hc(y) = ((ay+b) mod p) mod N

    }

    /**
     * Returns the number of entries in the hash table.
     *
     * @return the size
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Returns whether or not the table is empty.
     *
     * @return true if the size is 0
     */
    @Override
    public boolean isEmpty() {
        return (this.size == 0);
    }

    /**
     * Returns the value associated with a key.
     *
     * @param key
     * @return value
     */
    @Override
    public V get(K key) throws IllegalStateException {
        for (Entry<K, V> entry : this.entries()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        throw new IllegalStateException();

    }

    /**
     * Put a key-value pair in the map, replacing previous one if it exists.
     *
     * @param key
     * @param value
     * @return value
     */
    @Override
    public V put(K key, V value) throws IllegalStateException {
        LinkedList<K> listaKeys = (LinkedList<K>) this.keys();
        if(listaKeys.contains(key))
            throw new IllegalStateException("No se puede insertar una entrada cuya clave ya exista en el mapa");
        
        int posicion = this.calcularPosicion(key); //Posicion del array donde se va a insertar
        V valorDevuelto = null;

        if (this.array[posicion] == null) {// Si en la posicion del array no hay lista, no habrá ningun elemento insertado. Habrá que crear la lista e insertar el elemento
            this.array[posicion] = new LinkedList<>(); //Se crea la lista

        } else { //La posicion ya tiene lista, comprobar si la clave está en la lista
            for (Entry<K, V> entry : this.array[posicion]) //Recorrer todas las entradas de la lista
            {
                if (entry.getKey().equals(key)) { //Si hay alguna entrada con la misma clave...
                    valorDevuelto = entry.getValue();
                    this.array[posicion].remove(entry); //Eliminar la entrada de la lista
                }
            }
        }
        this.array[posicion].add(new HashEntry(key, value)); //Se añade la entrada a la lista
        this.size++;

        return valorDevuelto; //Devolverá null si se ha insertado una entrada sin la necesidad de sustituirla por otra
    }

    /**
     * Removes the key-value pair with a specified key.
     *
     * @param key
     * @return
     */
    @Override
    public V remove(K key) throws IllegalStateException {

        LinkedList<K> listaKeys = (LinkedList<K>) this.keys();

        if (listaKeys.contains(key)) {
            int posicion = this.calcularPosicion(key); //Posicion del array donde se encuentra la entry
            for (Entry<K, V> entry : array[posicion]) {
                if (entry.getKey().equals(key)) {
                    V valorDevuelto = entry.getValue();
                    array[posicion].remove(entry);
                    this.size--;
                    return valorDevuelto;
                }
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        throw new UnsupportedOperationException("Not yet implemented");
        ////return new HashTableMapIterator(this.array,this.tamaño); pero no lo pilla por los tipos, se deberían cambiar para que se acepte
    }

    /**
     * Returns an iterable object containing all of the keys.
     *
     * @return
     */
    @Override
    public Iterable<K> keys() {
        LinkedList<K> listaClaves = new LinkedList();
        for (Entry<K, V> entry : this.entries()) {
            listaClaves.add(entry.getKey());
        }

        return listaClaves;
    }

    /**
     * Returns an iterable object containing all of the values.
     *
     * @return
     */
    @Override
    public Iterable<V> values() {
        LinkedList<V> listaValores = new LinkedList();
        for (Entry<K, V> entry : this.entries()) {
            listaValores.add(entry.getValue());
        }

        return listaValores;
    }

    /**
     * Returns an iterable object containing all of the entries.
     *
     * @return
     */
    @Override
    public Iterable<Entry<K, V>> entries() {
        LinkedList<Entry<K, V>> listaEntradas = new LinkedList();
        for (int i = 0; i <this.tamaño; i++) { //Recorro todas las posiciones del array
            if (array[i] != null) { //Si hay lista en la posicion del array, se añaden todos los elementos de la lista a la lista de entradas
                for (Entry<K, V> entry : array[i]) {
                    listaEntradas.add(entry);
                }
            }
        }
        return listaEntradas;
    }

    /**
     * Determines whether a key is valid.
     *
     * @param k Key
     */
    protected void checkKey(K k) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Increase/reduce the size of the hash table and rehashes all the entries.
     *
     * @param newCap
     */
    protected void rehash(int newCap) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
