
package tablaturesmanager;

import java.io.*;
import javax.management.AttributeChangeNotification;
import org.jdom.*;//utilisation de la librairie jdom pour gérer le xml
import org.jdom.output.*;
import org.jdom.input.*;
import org.jdom.filter.*;
import java.util.List;
import java.util.Iterator;

/**
 *@comment Classe de gestion de fichier xml
 */
public class Xml {
    private static String path;//Le chemin fichier de notre xml
    private File fileXml;//Le fichier xml
    private static Element racine; //Création de la racine du fichier xml pour la lecture et écriture
    private static org.jdom.Document document;//On crée un nouveau Document JDOM basé sur la racine que l'on vient de créer pour la lecture et écriture
    
    /**
     *@comment Constructeur de la classe Xml
     */
    public Xml(){ //Constructeur pour création d'un nouveau xml
        racine = new Element("FICHIERS");//Création de la racine du fichier xml
        document = new Document(racine);//Création d'un nouveau Document JDOM basé sur la racine que l'on vient de créer
    }
    
    /**
     *@param String
     *@comment Permet la récupération du fichier xml en prenant
     * en paramètre le chemin du fichier
     */
    public void setFile(String _filePath){
        path = _filePath;
        fileXml = new File(path);
    }
    
    /**
     *@comment permet le parsing et la récupèration de l'élément racine du fichier xml
     */
     static void readRoot() throws Exception { //throws Exception pour la gestion d'une éventuelle erreur
        SAXBuilder sxb = new SAXBuilder();//On crée une instance de SAXBuilder
        document = sxb.build(new File(path));//Ajoute le fichier xml parsé à notre document JDOM
        racine = document.getRootElement(); //Récupére l'élément racine du document JDOM
    }

    /**
    *@param String
    *@return void
    *@comment Cette méthode ajoute la balise répertoire au xml
    * élément enfant de l'élément racine,
    * On y rajoute comme valeur le chemin du répertoire passé en paramètre
    */
     public void insertDir(String _directoryName){
            Element directory = new Element("REPERTOIRE");//Création d'un nouvel élément REPERTOIRE
            directory.setText(_directoryName);//Ajout du texte
            racine.addContent(directory);//Rajout de l'élément à la racine
     }
     
    /**
    *@param String
    *@return void
    *@comment Cette méthode ajoute une noeud "MUSIQUE" et ses enfants à notre xml
    * avec comme valeur pour l'élément CHEMIN la valeur du paramètre
    */
    public void insertNode(String _pathFile){
        Element musique = new Element("MUSIQUE");
        racine.addContent(musique);
        Element chemin = new Element("CHEMIN");
        chemin.setText(_pathFile);
        musique.addContent(chemin);
        Element fichier = new Element("FICHIER");
        musique.addContent(fichier);
        Element nom = new Element("NOM");
        musique.addContent(nom);
        Element auteur = new Element("AUTEUR");
        musique.addContent(auteur);
        Element album = new Element("ALBUM");
        musique.addContent(album);
        Element difficulte = new Element("DIFFICULTE");
        musique.addContent(difficulte);
        Element maitrise = new Element("MAITRISE");
        musique.addContent(maitrise);
        Element commentaire = new Element("COMMENTAIRE");
        musique.addContent(commentaire);
    }
    
    /**
     *@comment méthode de sauvegarde du fichier xml
     */
    static void save(){
        try{
            //On utilise ici un affichage classique avec getPrettyFormat()
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            //Il suffit simplement de créer une instance de FileOutputStream
            //avec en argument le nom du fichier pour effectuer la sérialisation.
            sortie.output(document, new FileOutputStream(path));
        }
        catch (java.io.IOException e){}
    }
    
    /**
     *@param String
     *@return Pile<String>
     *@comment Retourne toutes les valeurs que prend l'élément passé en paramétre dans le xml
     */
    public Pile<String> readXml (String _element){
        List listMusique = racine.getChildren("MUSIQUE");//On crée une List contenant tous les noeuds "MUSIQUE" de l'Element racine
        Iterator i = listMusique.iterator();//Création d'un Iterator sur notre liste
        Pile <String>arrayChemin = new Pile();//Création d'un collection de String
        while(i.hasNext()){//Boucle sur l'iterator
            Element courant = (Element)i.next();//On recrée l'Element courant à chaque tour de boucle afin de pouvoir utiliser les méthodes propres aux Element //(element) => cast ou transtypage
            //On empile dans notre collection le texte coresspondant au noeud i (du while) de l'element passé en parametre monter en majuscule (comme les noeud dans le xml)
            arrayChemin.empiler(courant.getChild(_element.toUpperCase()).getText());//toUpperCase car le xml est construit en majuscule
        }
        return arrayChemin;
    }
    
    /**
     *@param String, String
     *@return Element
     *@comment Cette méthode filtre le xml à la recherche de l'élément dont le nom est égal à la valeur contenu dans _researchElement
     * et correspondant à une balise dans un noeud dont l'élément CHEMIN à pour valeur la valeur contenu dans _pathFile
     * enfin elle renvoit l'élément en question sous la forme d'un Element
     */
     static Element elementFilter(String _pathFile, String _researchElement){   
        List listMusique = racine.getChildren("MUSIQUE");
        Iterator i = listMusique.iterator();
        Pile <Element>resultElements = new Pile();
        Element elm = racine;//Création d'un élément qui à la valeur de racine
        while(i.hasNext()){
            Element current = (Element)i.next();
            //Si le contenu de la balise "CHEMIN" de notre noeud "MUSIQUE" courant est égale à celui passé en paramétre
            if(current.getChildText("CHEMIN").compareTo(_pathFile) == 0){
                resultElements.empiler(current.getChild(_researchElement.toUpperCase())); //Sélectionne l'élément de la balise voulue en paramétre
            }
        }
        //Récupération de l'élément dans la collection d'élément obtenue
        for(int j=0; j<resultElements.getSize(); j++){
            elm = resultElements.get(j);
        }
        return elm;
    }

    /**
     *@param String, String, String
     *@comment On fait une modification sur un Element
     * Prend en paramètre l'identifian de l'élément MUSIQUE
     * c'est à dire la valeur que contienr sa balise REPERTOIRE
     * Le nom de l'élément enfant recherché
     * Et la valeur que doit prendre cet élément
     */
    static void modifElement(String _pathFile, String _researchElement, String _modif){
        Element element = elementFilter(_pathFile, _researchElement);//On appel notre méthode de recherche d'élément
        element.setText(_modif);//On modifie le texte de l'élément trouvé
    }

     /**
      *@param String, String
      *@return String
      *@comment Permet de récupérer la valeur d'une balise précise
      * Même fonctionnement que la méthode modifElement
      * mais retourne la valeur de l'élément au lieu de la modifier
      */
    static String readElement(String _pathFile, String _researchElement){
        Element element = elementFilter(_pathFile, _researchElement);
        return element.getText();
    }

     /**
      *@param String, String
      *@return Pile<String>
      *@comment Permet de récupérer une pile des balises correspondantes à celles recherchées
      */
     static Pile<String> pathFilter(String _researchElement, String _valueElement){     
        List listMusique = racine.getChildren("MUSIQUE");
        Iterator i = listMusique.iterator();
        Pile <String>resultElements = new Pile();
        while(i.hasNext()){
            Element current = (Element)i.next();
            //Si le contenu de la balise recherchée est égale à la valeur recherchée
            if(current.getChildText(_researchElement).compareTo(_valueElement) == 0){
                resultElements.empiler(current.getChildText("CHEMIN").toString()); //Sélectionne le texte de la balise voulue en paramétre et l'ajoute à la collection
            }
        }
        return resultElements;
    }
      /**
      *@return Pile<String>
      *@comment Pour récupérer les valeurs de toutes les balises CHEMIN
      * Retourne une collection de chemin de fichiers
      */
     public Pile<String> getAllPath(){
        Pile<String> allPath = new Pile<String>();
        List listMusique = racine.getChildren("MUSIQUE");
        Iterator i = listMusique.iterator();
        while(i.hasNext()){
            Element current = (Element)i.next();
            String currentPath = current.getChildText("CHEMIN");
            allPath.empiler(currentPath);
        }
         return allPath;
     }
     
     /**
      *@param String
      *@comment Cette méthode prend en paramètre la valeur que doit contenir la balise <CHEMIN> du noeud xml à supprimer
      */
     public void deleteNode(String _pathFile){
        Element element = elementFilter(_pathFile, "CHEMIN");//On recherhe l'élément à supprimer
        Element parentElement = element.getParentElement();//On séléctionne le noeud à supprimer
        parentElement.getParent().removeContent(parentElement);//On supprime l'élément à partir de l'élément parent et donc l'intégralité du noeud
     }

     /**
      *@return String
      *@comment Cette méthode retourne le contenu de la balise répertoire
      */
     public String readDirGp() throws Exception{
        readRoot();//Lis la racine du xml et nous la mets à disposition
//        System.out.println("racine.getChildText(REPERTOIRE)"+racine.getChildText("REPERTOIRE"));
        return racine.getChildText("REPERTOIRE");
     }
    
    /*
     *@comment méthode d'affichage du xml
     * Essentiellement utilisée pour le débuget la mise en place de l'application
     */
    static void show(){
        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(document, System.out);
        }
        catch (java.io.IOException e){}
    }
}
