import java.io.*;
import java.util.List;
import java.util.Iterator;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *@comment Classe de gestion de fichier xml
 */
public class Xml {
    private static String path;//Le chemin fichier de notre xml
    private File fileXml;//Le fichier xml
    private static Element racine; //Cr�ation de la racine du fichier xml pour la lecture et �criture
    private static org.jdom2.Document document;//On cr�e un nouveau Document JDOM bas� sur la racine que l'on vient de cr�er pour la lecture et �criture
    
    /**
     *@comment Constructeur de la classe Xml
     */
    public Xml(){ //Constructeur pour cr�ation d'un nouveau xml
        racine = new Element("FICHIERS");//Cr�ation de la racine du fichier xml
        document = new Document(racine);//Cr�ation d'un nouveau Document JDOM bas� sur la racine que l'on vient de cr�er
    }
    
    /**
     *@param String
     *@comment Permet la r�cup�ration du fichier xml en prenant
     * en param�tre le chemin du fichier
     */
    public void setFile(String _filePath){
        path = _filePath;
        fileXml = new File(path);
    }
    
    /**
     *@comment permet le parsing et la r�cup�ration de l'�l�ment racine du fichier xml
     */
     static void readRoot() throws Exception { //throws Exception pour la gestion d'une �ventuelle erreur
        SAXBuilder sxb = new SAXBuilder();//On cr�e une instance de SAXBuilder
        document = sxb.build(new File(path));//Ajoute le fichier xml pars� � notre document JDOM
        racine = document.getRootElement(); //R�cup�re l'�l�ment racine du document JDOM
    }

    /**
    *@param String
    *@return void
    *@comment Cette m�thode ajoute la balise r�pertoire au xml
    * �l�ment enfant de l'�l�ment racine,
    * On y rajoute comme valeur le chemin du r�pertoire pass� en param�tre
    */
     public void insertDir(String _directoryName){
            Element directory = new Element("REPERTOIRE");//Cr�ation d'un nouvel �l�ment REPERTOIRE
            directory.setText(_directoryName);//Ajout du texte
            racine.addContent(directory);//Rajout de l'�l�ment � la racine
     }
     
    /**
    *@param String
    *@return void
    *@comment Cette m�thode ajoute une noeud "MUSIQUE" et ses enfants � notre xml
    * avec comme valeur pour l'�l�ment CHEMIN la valeur du param�tre
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
     *@comment m�thode de sauvegarde du fichier xml
     */
    public void save(){
        try{
            //On utilise ici un affichage classique avec getPrettyFormat()
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            //Il suffit simplement de cr�er une instance de FileOutputStream
            //avec en argument le nom du fichier pour effectuer la s�rialisation.
            sortie.output(document, new FileOutputStream(path));
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
    }
    
    /**
     *@param String
     *@return Pile<String>
     *@comment Retourne toutes les valeurs que prend l'�l�ment pass� en param�tre dans le xml
     */
    public Pile<String> readXml (String _element){
        List listMusique = racine.getChildren("MUSIQUE");//On cr�e une List contenant tous les noeuds "MUSIQUE" de l'Element racine
        Iterator i = listMusique.iterator();//Cr�ation d'un Iterator sur notre liste
        Pile <String>arrayChemin = new Pile();//Cr�ation d'un collection de String
        while(i.hasNext()){//Boucle sur l'iterator
            Element courant = (Element)i.next();//On recr�e l'Element courant � chaque tour de boucle afin de pouvoir utiliser les m�thodes propres aux Element //(element) => cast ou transtypage
            //On empile dans notre collection le texte coresspondant au noeud i (du while) de l'element pass� en parametre monter en majuscule (comme les noeud dans le xml)
            arrayChemin.empiler(courant.getChild(_element.toUpperCase()).getText());//toUpperCase car le xml est construit en majuscule
        }
        return arrayChemin;
    }
    
    /**
     *@param String, String
     *@return Element
     *@comment Cette m�thode filtre le xml � la recherche de l'�l�ment dont le nom est �gal � la valeur contenu dans _researchElement
     * et correspondant � une balise dans un noeud dont l'�l�ment CHEMIN � pour valeur la valeur contenu dans _pathFile
     * enfin elle renvoit l'�l�ment en question sous la forme d'un Element
     */
     static Element elementFilter(String _pathFile, String _researchElement){   
        List listMusique = racine.getChildren("MUSIQUE");
        Iterator i = listMusique.iterator();
        Pile <Element>resultElements = new Pile();
        Element elm = racine;//Cr�ation d'un �l�ment qui � la valeur de racine
        while(i.hasNext()){
            Element current = (Element)i.next();
            //Si le contenu de la balise "CHEMIN" de notre noeud "MUSIQUE" courant est �gale � celui pass� en param�tre
            if(current.getChildText("CHEMIN").compareTo(_pathFile) == 0){
                resultElements.empiler(current.getChild(_researchElement.toUpperCase())); //S�lectionne l'�l�ment de la balise voulue en param�tre
            }
        }
        //R�cup�ration de l'�l�ment dans la collection d'�l�ment obtenue
        for(int j=0; j<resultElements.getSize(); j++){
            elm = resultElements.get(j);
        }
        return elm;
    }

    /**
     *@param String, String, String
     *@comment On fait une modification sur un Element
     * Prend en param�tre l'identifian de l'�l�ment MUSIQUE
     * c'est � dire la valeur que contienr sa balise REPERTOIRE
     * Le nom de l'�l�ment enfant recherch�
     * Et la valeur que doit prendre cet �l�ment
     */
    static void modifElement(String _pathFile, String _researchElement, String _modif){
        Element element = elementFilter(_pathFile, _researchElement);//On appel notre m�thode de recherche d'�l�ment
        element.setText(_modif);//On modifie le texte de l'�l�ment trouv�
    }

     /**
      *@param String, String
      *@return String
      *@comment Permet de r�cup�rer la valeur d'une balise pr�cise
      * M�me fonctionnement que la m�thode modifElement
      * mais retourne la valeur de l'�l�ment au lieu de la modifier
      */
    static String readElement(String _pathFile, String _researchElement){
        Element element = elementFilter(_pathFile, _researchElement);
        return element.getText();
    }

     /**
      *@param String, String
      *@return Pile<String>
      *@comment Permet de r�cup�rer une pile des balises correspondantes � celles recherch�es
      */
     static Pile<String> pathFilter(String _researchElement, String _valueElement){     
        List listMusique = racine.getChildren("MUSIQUE");
        Iterator i = listMusique.iterator();
        Pile <String>resultElements = new Pile();
        while(i.hasNext()){
            Element current = (Element)i.next();
            //Si le contenu de la balise recherch�e est �gale � la valeur recherch�e
            if(current.getChildText(_researchElement).compareTo(_valueElement) == 0){
                resultElements.empiler(current.getChildText("CHEMIN").toString()); //S�lectionne le texte de la balise voulue en param�tre et l'ajoute � la collection
            }
        }
        return resultElements;
    }
      /**
      *@return Pile<String>
      *@comment Pour r�cup�rer les valeurs de toutes les balises CHEMIN
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
      *@comment Cette m�thode prend en param�tre la valeur que doit contenir la balise <CHEMIN> du noeud xml � supprimer
      */
     public void deleteNode(String _pathFile){
        Element element = elementFilter(_pathFile, "CHEMIN");//On recherhe l'�l�ment � supprimer
        Element parentElement = element.getParentElement();//On s�l�ctionne le noeud � supprimer
        parentElement.getParent().removeContent(parentElement);//On supprime l'�l�ment � partir de l'�l�ment parent et donc l'int�gralit� du noeud
     }

     /**
      *@return String
      *@comment Cette m�thode retourne le contenu de la balise r�pertoire
      */
     public String readDirGp() throws Exception{
        readRoot();//Lis la racine du xml et nous la mets � disposition
//        System.out.println("racine.getChildText(REPERTOIRE)"+racine.getChildText("REPERTOIRE"));
        return racine.getChildText("REPERTOIRE");
     }
    
    /*
     *@comment m�thode d'affichage du xml
     * Essentiellement utilis�e pour le d�buget la mise en place de l'application
     */
    static void show(){
        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(document, System.out);
        }
        catch (IOException e){}
    }
}
