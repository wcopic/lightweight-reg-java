package tramite;

import java.util.ArrayList;
import java.util.List;

public class ListaSimple<T> {
    private Nodo<T> head;

    public void add(T x) {
        Nodo<T> n = new Nodo<>(x);
        if (head == null) {
            head = n;
        } else {
            Nodo<T> t = head;
            while (t.getSiguiente() != null) {
                t = t.getSiguiente();
            }
            t.setSiguiente(n);
        }
    }

    public String toListString() {
        StringBuilder sb = new StringBuilder();
        Nodo<T> t = head;
        while (t != null) {
            sb.append(t.getDato().toString()).append("\n");
            t = t.getSiguiente();
        }
        return sb.toString();
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
