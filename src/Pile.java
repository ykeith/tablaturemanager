import java.util.ArrayList;

/**
 * Implementation of classic Pile class
 *
 * @author Yves Keith
 */
public class Pile<E> {

    private ArrayList<E> elements;

    /**
     * Constructor
     */
    public Pile() {
        elements = new ArrayList<E>();
    }

    /**
     * Get size of the pile
     *
     * @return int
     */
    public int getSize() {
        return elements.size();
    }

    /**
     * Get the object at the index in the list
     *
     * @param _i index
     * @return E
     */
    public E get(int _i) {
        if (_i <= getSize()) {
            return elements.get(_i);
        } else return null;
    }

    /**
     * Pile an element
     *
     * @param _n element to add
     */
    public void pile(E _n) {
        elements.add(_n);
    }

    /**
     * Return last element
     *
     * @return E
     */
    public E unstack() {
        if (!elements.isEmpty()) {
            return elements.remove(elements.size() - 1);
        } else return null;
    }

    /**
     * Remove an element
     *
     * @param _i index
     */
    public void remove(int _i) {
        elements.remove(_i);
    }

    /**
     * Sort the collection to avoid duplication
     */
    public void sort() {
        int nbCorrespond = 0;
        for (int i = getSize(); i > 0; i--) {
            for (int j = getSize(); j > 0; j--) {
                if (i < getSize() && j < getSize()) {
                    if (get(i).equals(get(j))) {
                        nbCorrespond++;
                        // More than one occurrence => remove
                        if (nbCorrespond > 1) {
                            remove(j);
                        }
                    }
                }
            }
            nbCorrespond = 0;
        }
    }

    /**
     * Retunr the collection in String Format
     *
     * @return String
     */
    public String toString() {
        String str = "";
        for (int i = 0; i < getSize(); i++) {
            str += get(i).toString();
        }
        return str;
    }
}
