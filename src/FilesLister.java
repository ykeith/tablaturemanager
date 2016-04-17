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
    *@comment Permet de lister les fichiers .gp* et .gtp d'un r�pertoire et de ses sous-r�pertoire
    * Renvoit les chemins des fichiers sous la forme d'une Pile de String
    * N�cessite de placer en param�tre le nom du r�pertoire � lister
    * Car la m�thode peut s'appeler elle m�me (pour sous-r�pertoire)
    * Et une Pile de String qui soit vide
    * Car sinon on n'efface la Pile � renvoyer lors d'un appel r�cursif de la m�thode(la m�thode appelle cette m�me m�thode)
    */
   public Pile<String> gpLister(String _path, Pile<String> _pilePath){
        File path = new File(_path);
        String re = "(.*[.]{1}g{1}p{1}[1-9]{1}$)";//Expression r�guli�re
        String re2 = "(.*[.]gtp$)";
        String dir;
        
        for(int i=0;i<path.list().length;i++){
            //Reconstruction d'un objet fichier avec le chemin complet pour pouvoir tester s'il s'agit d'un r�pertoire
            File dirTest = new File(_path + File.separator + path.list()[i]);
            if (dirTest.isDirectory()) {//S'il s'agit d'un r�pertoire
                dir = _path + "/" + path.list()[i]; //Reconstruction du chemin du r�pertoire
                gpLister(dir, _pilePath); //Rappel la m�thode Read, pour lister le sous-r�pertoire
            }
            else if(path.list()[i].matches(re) || path.list()[i].matches(re2)) {//D'un fichier contenant une ou l'autre expression reguliere
                String fileGp = path.toString() + File.separator + path.list()[i]; //Reconstruction du chemin
                _pilePath.empiler(fileGp);//On ajoute le chemin du fichier � la collection
            }
        } 
        return _pilePath;
    }

   /**
    *@author Keith Yves
    *@date 11/03/2008
    *@param String
    *@return boolean
    *@comment Test l'existence d'un fichier dont le chemin est donn� en param�tre
    */ 
    public boolean lookingForFile(String _pathFileToFound){
        File file = new File(_pathFileToFound);//On construit un objet fichier � partir du chemin donn�
        return file.exists();//On test son existence
    }
}
