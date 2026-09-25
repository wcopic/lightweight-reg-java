package tramite;

import java.util.ArrayList;
import java.util.List;

public class ListaDoble<T> { //expedientes
    private Nodo<T> head, tail, current;

    public void add(T x) {
        Nodo<T> n = new Nodo<>(x);
        if (head == null) {
            head = tail = current = n;
        } else {
            tail.setSiguiente(n);
            n.setAnterior(tail);
            tail = n;
        }
    }

    public T next() {
        if (current != null && current.getSiguiente() != null) {
            current = current.getSiguiente();
        }
        return current != null ? current.getDato() : null;
    }

    public T prev() {
        if (current != null && current.getAnterior() != null) {
            current = current.getAnterior();
        }
        return current != null ? current.getDato() : null;
    }

    public T current() {
        return current != null ? current.getDato() : null;
    }

    public List<T> toList() {
        List<T> lista = new ArrayList<>();
        Nodo<T> actual = head;

        while (actual != null) {
            lista.add(actual.getDato());
            actual = actual.getSiguiente();
        }

        return lista;
    }
}

