/*
 * Main.java
 *
 * Created on 7 décembre 2007, 15:02
 *
 */

package tablaturesmanager;

import com.sun.naming.internal.ResourceManager;
import java.awt.Frame;
import javax.swing.JFrame;


/**
 *
 *@author Keith Yves
 *@comment Classe principale
 */

public class Main {
    /** Constructeur de la classe Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        MainForm t = new MainForm();//Création de l'objet de type MainForm, le form principal de l'application
        t.init();//Initialisation de la form (remplissage combobox, etc)
    } 
}
