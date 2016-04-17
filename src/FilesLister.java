
package tablaturesmanager;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import java.io.*;
import java.util.*;

/**
 *@author Keith Yves
 *@comment Classe qui permet le listage de fichier
 */
public class FilesLister {
        
    /** Constructeur de la classe FilesLister */
    public FilesLister() {            
    }
    
   /**
    *@param String, Pile<String>
    *@return Pile<String>
    *@comment Permet de lister les fichiers .gp* et .gtp d'un répertoire et de ses sous-répertoire
    * Renvoit les chemins des fichiers sous la forme d'une Pile de String
    * Nécessite de placer en paramétre le nom du répertoire à lister
    * Car la méthode peut s'appeler elle même (pour sous-répertoire)
    * Et une Pile de String qui soit vide
    * Car sinon on n'efface la Pile à renvoyer lors d'un appel récursif de la méthode(la méthode appelle cette même méthode)
    */
   public Pile<String> gpLister(String _path, Pile<String> _pilePath){
        File path = new File(_path);
        String re = "(.*[.]{1}g{1}p{1}[1-9]{1}$)";//Expression réguliére
        String re2 = "(.*[.]gtp$)";
        String dir;
        
        for(int i=0;i<path.list().length;i++){
            //Reconstruction d'un objet fichier avec le chemin complet pour pouvoir tester s'il s'agit d'un répertoire
            File dirTest = new File(_path + "/" + path.list()[i]);
            if (dirTest.isDirectory()) {//S'il s'agit d'un répertoire
                dir = _path + "/" + path.list()[i]; //Reconstruction du chemin du répertoire
                gpLister(dir, _pilePath); //Rappel la méthode Read, pour lister le sous-répertoire
            }
            else if(path.list()[i].matches(re) || path.list()[i].matches(re2)) {//D'un fichier contenant une ou l'autre expression reguliere
                String fileGp = path.toString() + "\\" + path.list()[i]; //Reconstruction du chemin
                _pilePath.empiler(fileGp);//On ajoute le chemin du fichier à la collection
            }
        } 
        return _pilePath;
    }

   /**
    *@author Keith Yves
    *@date 11/03/2008
    *@param String
    *@return boolean
    *@comment Test l'existence d'un fichier dont le chemin est donné en paramètre
    */ 
    public boolean lookingForFile(String _pathFileToFound){
        File file = new File(_pathFileToFound);//On construit un objet fichier à partir du chemin donné
        return file.exists();//On test son existence
    }
}
