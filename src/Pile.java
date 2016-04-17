
package tablaturesmanager;
import java.awt.List;
import java.util.ArrayList;

/**
 *@author Keith Yves
 *@comment Cette classe permet d'empiler et dépiler
 * un objet de façon générique <E> (exemple : Mouton, Moto, int, String, ...)
 * et en plus en pouvant modifier la taille du tableau (ArrayList)
 *@version 1.0 - 30/01/2008
 */
public class Pile<E> {
    private ArrayList<E> elements;//notre ArrayList

    /**
     *@comment Constructeur de la classe Pile
     */
    public Pile(){
        elements = new ArrayList<E>();
    }

    /**
     *@return int
     *@comment Accesseur pour obtenir la taille du tableau
     */
    public int getSize(){
            return elements.size();
    }
        
    /**
     *@param int (l'index voulu)
     *@return un objet du même type que notre ArrayList(E)
     *@comment Accesseur pour obtenir l'objet contenu à l'index passé en paramétre
     */
    public E get(int _i){
        if ( _i <= getSize() ){
            return elements.get(_i);
        }
        else return null;
    }

    /**
     *@param int
     *@comment Empile à la derniére place de la pile notre élément passé en paramétre
     */
    public void empiler(E _n){
        elements.add(_n);
    }

    /**
     *@return int
     *@comment Depile le dernier élément de la pile
     */
    public E depiler(){
            if(!elements.isEmpty()){ //Si la pile n'est pas vide
                    return elements.remove(elements.size()-1); //On retourne le dernier élément
            }
            else return null; //On retourne un élément improbable
    }

    /**
     *@param int
     *@comment Supprime l'élément à l'index donné
     */
    public void remove(int _index){
        elements.remove(_index);
    }

    /*
     *@author yk
     *@date 12/03/2008
     *@comment Cette méthode tri la collection et y retire les doublons
     */
    public void sort(){
        // Pour chaque objet dans la collection on vérifie qu'aucun autre objet de la collection ne lui ai égale plus d'une fois
        int nbCorrespond = 0;
        for (int i=getSize(); i>0; i--){ //Boucle sur la collection
            for ( int j=getSize(); j>0; j--){ //Nouvelle boucle sur la collection
                if(i < getSize() && j < getSize()){
                    if( get(i).equals(get(j))){//Si deux objet sont égaux
                            nbCorrespond++; //On rajoute une correpondance au compteur
                        if(nbCorrespond > 1){
                            remove(j); //Plus d'une correspondance on supprime
                        }
                    }
                }
            }
        nbCorrespond = 0;//Réinitialise notre compteur
        }
    }
    
    /*
     *@comment méthode pour copier une collection pour ne pas avoir le même pointeur
     * entre la collection copiée et celle obtenue
     */
    private Pile<E> copyCollection(){
        Pile<E> clone = new Pile<E>();
        for(int i=0; i<getSize(); i++){
            clone.empiler(get(i));
        }
        return clone;
    }
    
    /*
     *@author yk
     *@date 12/03/2008
     *@comment Pour éventuellement effectuer les debugging
     */
    public String toString(){
        String str = "";
        for(int i=0;i<getSize();i++){
            str += get(i).toString();
        }
        return str;
    }
}
