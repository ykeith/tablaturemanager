import java.awt.List;
import java.util.ArrayList;

/**
 *@author Keith Yves
 *@comment Cette classe permet d'empiler et d�piler
 * un objet de fa�on g�n�rique <E> (exemple : Mouton, Moto, int, String, ...)
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
     *@return un objet du m�me type que notre ArrayList(E)
     *@comment Accesseur pour obtenir l'objet contenu � l'index pass� en param�tre
     */
    public E get(int _i){
        if ( _i <= getSize() ){
            return elements.get(_i);
        }
        else return null;
    }

    /**
     *@param int
     *@comment Empile � la derni�re place de la pile notre �l�ment pass� en param�tre
     */
    public void empiler(E _n){
        elements.add(_n);
    }

    /**
     *@return int
     *@comment Depile le dernier �l�ment de la pile
     */
    public E depiler(){
            if(!elements.isEmpty()){ //Si la pile n'est pas vide
                    return elements.remove(elements.size()-1); //On retourne le dernier �l�ment
            }
            else return null; //On retourne un �l�ment improbable
    }

    /**
     *@param int
     *@comment Supprime l'�l�ment � l'index donn�
     */
    public void remove(int _index){
        elements.remove(_index);
    }

    /*
     *@author yk
     *@date 12/03/2008
     *@comment Cette m�thode tri la collection et y retire les doublons
     */
    public void sort(){
        // Pour chaque objet dans la collection on v�rifie qu'aucun autre objet de la collection ne lui ai �gale plus d'une fois
        int nbCorrespond = 0;
        for (int i=getSize(); i>0; i--){ //Boucle sur la collection
            for ( int j=getSize(); j>0; j--){ //Nouvelle boucle sur la collection
                if(i < getSize() && j < getSize()){
                    if( get(i).equals(get(j))){//Si deux objet sont �gaux
                            nbCorrespond++; //On rajoute une correpondance au compteur
                        if(nbCorrespond > 1){
                            remove(j); //Plus d'une correspondance on supprime
                        }
                    }
                }
            }
        nbCorrespond = 0;//R�initialise notre compteur
        }
    }
    
    /*
     *@comment m�thode pour copier une collection pour ne pas avoir le m�me pointeur
     * entre la collection copi�e et celle obtenue
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
     *@comment Pour �ventuellement effectuer les debugging
     */
    public String toString(){
        String str = "";
        for(int i=0;i<getSize();i++){
            str += get(i).toString();
        }
        return str;
    }
}
