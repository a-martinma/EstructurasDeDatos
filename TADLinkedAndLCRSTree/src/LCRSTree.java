import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import material.Position;

/**
 *
 * @author Álvaro Martín Martín.
 * @param <E>
 */
public class LCRSTree<E> implements NAryTree<E> {

    private class LCRSnode<T> implements Position<T> {

        private T elem;
        private LCRSnode<T> padre;
        private LCRSnode<T> primerHijo;
        private LCRSnode<T> siguienteHermano;

        public LCRSnode(T elem, LCRSnode<T> padre, LCRSnode<T> primerHijo, LCRSnode<T> siguienteHermano) {
            this.elem = elem;
            this.padre = padre;
            this.primerHijo = primerHijo;
            this.siguienteHermano = siguienteHermano;
        }

        @Override
        public T getElement() {
            return this.elem;
        }

        public LCRSnode<T> getPadre() {
            return padre;
        }

        public LCRSnode<T> getPrimerHijo() {
            return primerHijo;
        }

        public LCRSnode<T> getSiguienteHermano() {
            return siguienteHermano;
        }

        public void setElem(T elem) {
            this.elem = elem;
        }

        public void setPadre(LCRSnode<T> padre) {
            this.padre = padre;
        }

        public void setPrimerHijo(LCRSnode<T> primerHijo) {
            this.primerHijo = primerHijo;
        }

        public void setSiguienteHermano(LCRSnode<T> siguienteHermano) {
            this.siguienteHermano = siguienteHermano;
        }

    }

    private LCRSnode<E> root = null;

    private LCRSnode<E> checkPosition(Position<E> pos) {
        if ((pos == null) || !(pos instanceof LCRSnode)) {
            throw new RuntimeException("The position is invalid");
        }
        return (LCRSnode<E>) pos;
    }

    private LCRSTree<E> checkTree(NAryTree<E> tree) {
        if ((tree == null) || !(tree instanceof LCRSTree)) {
            throw new RuntimeException("The tree is invalid");
        }
        return (LCRSTree<E>) tree;
    }

    @Override
    public Position<E> addRoot(E e) {
        if (!this.isEmpty()) {
            throw new RuntimeException("El árbol ya tiene raíz porque no está vacío");
        }
        this.root = new LCRSnode(e, null, null, null);
        return this.root;
    }

    @Override
    public Position<E> add(E element, Position<E> p) { //Añadir un nodo como hijo del position que se pasa, se debería añadir como último hijo

        LCRSnode<E> padre = checkPosition(p);
        LCRSnode<E> newNode = new LCRSnode(element, padre, null, null);

        if (padre.getPrimerHijo() == null) { //El padre no tiene hijos
            padre.setPrimerHijo(newNode);
        } else { //El padre tiene hijos, recorrerlos todos hasta llegar al ultimo y meter el nodo ahí al final
            LCRSnode<E> aux = padre.getPrimerHijo();
            while (aux.getSiguienteHermano() != null) {
                aux = aux.getSiguienteHermano(); //Saldrá del while y aux apunta al último hijo
            }
            aux.setSiguienteHermano(newNode);
        }
        return newNode;
    }

    @Override
    public Position<E> add(E element, Position<E> p, int n) { //El índice empieza en 0
        LCRSnode<E> padre = checkPosition(p);
        LinkedList<Position<E>> childrenList = (LinkedList<Position<E>>) this.children(p);

        if ((n < 0) || (n > childrenList.size())) {
            throw new RuntimeException("No se puede introducir un hijo en esa posición.");
        }
        if (childrenList.size() == n) //En este caso, se estaría añadiendo por el final, por lo tanto se ejecuta el método de arriba
        {
            return this.add(element, p);
        } else {
            LCRSnode<E> aux = padre.getPrimerHijo();
            LCRSnode<E> auxAnt = null;
            int i = 0;
            while (i < n) {
                i++;
                auxAnt = aux;
                aux = aux.getSiguienteHermano();
            } //Sale del while y aux está apuntando al nodo que será el siguiente hermano del nuevo nodo
            LCRSnode<E> newNodo = new LCRSnode(element, padre, null, aux);
            if (auxAnt != null) //Es posible que n=0 y por lo tanto, auxAnt sea = null, por lo tanto solo se hará el set del siguiente hermano si n!=0
            {
                auxAnt.setSiguienteHermano(newNodo);
            }

            return newNodo;
        }

    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        LCRSnode<E> node1 = checkPosition(p1);
        LCRSnode<E> node2 = checkPosition(p2);
        E aux = node1.getElement();
        node1.setElem(node2.getElement());
        node2.setElem(aux);
    }

    @Override
    public E replace(Position<E> p, E e) {
        LCRSnode<E> node = checkPosition(p);
        E valorantes = node.getElement();
        node.setElem(e);
        return valorantes;
    }

    @Override
    public NAryTree<E> subTree(Position<E> v) { // El nodo que enganches para hacer el subárbol será la futura raíz, dejará de tener hermanos (si los tiene)
        LCRSnode<E> nuevoPadre = checkPosition(v);
        LCRSTree<E> nuevoArbol = new LCRSTree();
        if (this.isEmpty()) {
            throw new RuntimeException("El árbol está vacío");
        }

        if (nuevoPadre.getPadre() == null)//El position que nos pasan es la raiz del árbol
        {
            nuevoArbol.root = this.root;
        } else if (nuevoPadre.getPadre().getPrimerHijo().equals(nuevoPadre)) {//Si el nuevoPadre es el primerHijo...
            nuevoPadre.getPadre().setPrimerHijo(nuevoPadre.getSiguienteHermano());// Que el padre del nuevoPadre apunte al siguiente hermano del nuevoPadre
            nuevoPadre.setPadre(null); //Quito el puntero del primerHijo al padre
            nuevoPadre.setSiguienteHermano(null);//Quito el puntero del nuevoPadre a su siguiente hermano
            nuevoArbol.root = nuevoPadre;
        } else { //Si el nuevoPadre no es el primerHijo... 
            LCRSnode<E> aux = nuevoPadre.getPadre().getPrimerHijo();//El aux que apunte al primero de los hermanos
            while (aux.siguienteHermano != nuevoPadre) {
                aux = aux.getSiguienteHermano(); //Saldrá del while apuntando al anterior hermano
            }
            aux.setSiguienteHermano(nuevoPadre.getSiguienteHermano());//Que el anterior hermano apunte al siguiente hermano
            nuevoPadre.setSiguienteHermano(null);//Que el nuevoPadre no tenga hemanos
            nuevoPadre.setPadre(null);
            nuevoArbol.root = nuevoPadre;
        }
        return nuevoArbol;
    } //No se hasta que punto es necesario modificar las referencias de los punteros. He intentado dejar el árbol lo más limpio posible de punteros

    @Override
    public void remove(Position<E> p) {//Objetivo: Eliminar todas las referencias del nodo a borrar
        //Si el nodo que se quiere eliminar tiene hijos, no se podrá acceder a ellos en el árbol. Es una movida implementarlo perfecto.
        LCRSnode<E> nodo = checkPosition(p);
        if (this.isEmpty()) {
            throw new RuntimeException("El árbol está vacío");
        }
        if (nodo.getPadre() == null)//Si el nodo que nos pasan es la raíz
        {
            throw new RuntimeException("No se puede eliminar la raíz del árbol");
        }
        if (nodo.getPadre().getPrimerHijo().equals(nodo)) { //Si el nuevoPadre es el primerHijo...
            nodo.getPadre().setPrimerHijo(nodo.getSiguienteHermano());// Que el padre del nodo apunte al siguiente hermano del nodo
            nodo.setPadre(null); //Quito el puntero del primerHijo al padre
            nodo.setSiguienteHermano(null);//Quito el puntero del nodo a su siguiente hermano
        } else { //Si el nodo no es el primerHijo...
            LCRSnode<E> aux = nodo.getPadre().getPrimerHijo();//El aux que apunte al primero de los hermanos
            while (aux.siguienteHermano != nodo) {
                aux = aux.getSiguienteHermano(); //Saldrá del while apuntando al anterior hermano
            }
            aux.setSiguienteHermano(nodo.getSiguienteHermano());//Que el anterior hermano apunte al siguiente hermano
            nodo.setSiguienteHermano(null);//Que el nodo no tenga hemanos
            nodo.setPadre(null);
        }

    }

    @Override
    public void attach(Position<E> p, NAryTree<E> t) { //Añadir la raiz del arbol como ultimo hijo del nodo donde se quiera atachear
        if (this.isEmpty()) {
            throw new RuntimeException("No se puede añadir un subárbol a ningun nodo porque el árbol está vacío");
        }
        LCRSnode<E> nodo = checkPosition(p);
        LCRSTree<E> tree = checkTree(t);
        if (nodo.getPrimerHijo() == null) {
            nodo.setPrimerHijo((LCRSnode<E>) tree.root()); //Necesario castear a LCRSnode<E> ya que root() devuelve un Position
        } else {
            LCRSnode<E> aux = nodo.getPrimerHijo();
            while (aux.getSiguienteHermano() != null) {
                aux = aux.getSiguienteHermano();//Saldrá del while apuntando al último hermano
            }
            aux.setSiguienteHermano((LCRSnode<E>) tree.root());
        }
        tree.root.setPadre(nodo);//No olvidarse de que hay que apuntar al padre (en este caso "nodo")
    }

    @Override
    public boolean isEmpty() {
        return (this.root == null);
    }

    @Override
    public Position<E> root() {
        return this.root;
    }

    @Override
    public Position<E> parent(Position<E> v) {
        LCRSnode<E> node = checkPosition(v);
        return node.getPadre();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        LCRSnode<E> padre = checkPosition(v);
        if (padre.getPrimerHijo() == null) {
            throw new RuntimeException("El nodo elegido no tiene hijos");
        }
        LinkedList<Position<E>> listaHijos = new LinkedList();
        LCRSnode<E> aux = padre.getPrimerHijo();
        listaHijos.add(aux); //Esto hay que hacerlo así porque si el padre solo tuviera un hijo, podría haber problemas al meterlo dentro del while
        while (aux.getSiguienteHermano() != null) {
            aux = aux.getSiguienteHermano();
            listaHijos.add(aux);
        }
        return listaHijos;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        return (!this.isLeaf(v));
    }

    @Override
    public boolean isLeaf(Position<E> v) {
        LCRSnode<E> node = checkPosition(v);
        return (node.getPrimerHijo() == null);
    }

    @Override
    public boolean isRoot(Position<E> v) {
        LCRSnode<E> node = checkPosition(v);
        return (node.getPadre() == null);
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new LCRSTreeIterator(this);
    }

    public int size() {
        int cuenta = 0;
        Iterator<Position<E>> iterator = this.iterator();
        while (iterator.hasNext()) {
            cuenta++;
            iterator.next();
        }
        return cuenta;

    }

    public class LCRSTreeIterator<E> implements Iterator<Position<E>> {

        LCRSTree<E> current;
        Queue<Position<E>> cola = new LinkedList();

        public LCRSTreeIterator(LCRSTree<E> tree) {
            this.current = tree;
            if (current.isEmpty()) {
                throw new RuntimeException("El árbol está vacío");
            }
            cola.add(tree.root());
        }

        @Override
        public boolean hasNext() {
            return (!cola.isEmpty());
        }

        @Override
        public Position<E> next() {
            Position<E> pos = cola.remove();
            LCRSnode<E> nodo = (LCRSnode<E>) pos; //Para trabajar con el nodo es necesario castearlo a un formato tipo nodo, con el position no se puede hacer nada
            if (nodo.getPrimerHijo() != null) {
                cola.add(nodo.getPrimerHijo());
            }
            if (nodo.getSiguienteHermano() != null) {
                cola.add(nodo.getSiguienteHermano());
            }

            return pos;
        }

    }

}
