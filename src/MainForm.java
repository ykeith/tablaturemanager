import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.io.File;

/**
 * @author yk
 */
public class MainForm extends javax.swing.JFrame {
    private String gpDir;    //Le r�pertoire des fichiers guitar pro
    private String pathXml;  //Le chemin vers le xml : tmdata.xml
    private String pathHtml; //Le chemin vers la page html d'aide
    private Xml xmlFile = new Xml();        //Cr�ation d'un nouveau objet de notre classe xml

    /**
     * Constructeur
     */
    public MainForm() {
        initComponents();
        String tmDir = ""; //Le chemin vers le noeud courant de l'application pas de . parce que sinon le point reste est empeche lancement de fichier
        File absolutPath = new File(tmDir); //On cr�er un nouveau fichier pour rechercher le chemin absolu
        tmDir = absolutPath.getAbsolutePath(); //On recupere le chemin absolu
        pathXml = tmDir + File.separator + "TMdata.xml"; //Notre fichier xml doit se trouver dans le m�me r�pertoire que notre application
        pathHtml = tmDir + File.separator + "help.html"; //Fichier d'aide de tablatures manager
        xmlFile.setFile(pathXml);
//        System.out.println("tmDir=" + tmDir);//DEBUG
//        System.out.println("pathXml=" + pathXml);//DEBUG
        jComboBox2.setVisible(false);//TODO
        jLabel2.setVisible(false);//TODO
    }

    /**
     * @comment initialisation
     * On regarde si nous avons d�j� un xml de donn�es
     * Si oui, on regarde quel est le r�pertoire � lister et on fait la mise � jour du xml puis des comboBox
     * Sinon On demande d'abord � l'utilisateur de choisir un r�pertoire et on cr�e le xml
     */
    public void init() throws Exception {
        FilesLister filesList = new FilesLister();
        if (!filesList.lookingForFile(pathXml)) { // Si le xml qui contient les donn�es n'existe pas
            showFileChooser();
        } else {
            gpDir = xmlFile.readDirGp();//R�cup�ration du chemin du r�pertoire � lister, dans le xml
//            System.out.println("gpDir="+gpDir);//DEBUG
            xmlUpdate();//mise � jour du xml
            xmlFile.save();//sauvegarde du xml
            combo1Update();//mise � jour de la jcombobox1
            comboUpdate();//mise � jours des autres combobox
            show();//d�voile la frame principale � l'utilisateur
        }
    }

    /**
     * @comment Configure et affiche la jframe de choix de r�pertoire
     */
    public void showFileChooser() {
        jFrame3.setSize(600, 400);
        jFrame3.setResizable(false);
        jFrame3.setVisible(true);
    }

    /**
     * @comment On recherche tous les path contenu dans le xml qu'on insert dans une collection
     * On filtre les fichiers .gp et .gtp dans le r�pertoire qu'on insert dans une collection
     * Puis on fait la comparaison des deux collections
     * S'il y a des nouveaux fichiers on les ins�res dans le xml
     * A l'inverse si certains fichiers ne sont plus l� on les supprimes
     */
    public void xmlUpdate() {
        FilesLister filesList = new FilesLister();  //construction d'un objet qui permet de lister les tablatures
        Pile<String> allPath = xmlFile.getAllPath();//On recupere tous les chemins des fichiers
        Pile<String> newPath = new Pile<String>();  //Pour pouvoir appeler la m�thode readNewPath
        Pile<String> newAllPath = filesList.gpLister(gpDir, newPath);

        if (newAllPath.getSize() > allPath.getSize()) { //Si le tableau de nouveau est plus grand
            for (int i = 0; i < newAllPath.getSize(); i++) {
                int verif = 0;
                for (int j = 0; j < allPath.getSize(); j++) {
                    if (newAllPath.get(i).compareTo(allPath.get(j)) == 0) {
                        verif++;
                    }
                }
                if (verif == 0) {
//                     System.out.println("Une update � faire : ajout d'un nouveau noeud dans le xml");//DEBUG
                    xmlFile.insertNode(newAllPath.get(i));
                }
            }
        } else {
            for (int i = 0; i < allPath.getSize(); i++) {
                int verif = 0;
                for (int j = 0; j < newAllPath.getSize(); j++) {
                    if (newAllPath.get(j).compareTo(allPath.get(i)) == 0) {
                        verif++;
                    }
                }
                if (verif == 0) {
//                    System.out.println("Une update � faire :  suppression d'un noeud dans le xml"); //DEBUG
                    xmlFile.deleteNode(allPath.get(i)); //On supprime le noeud en question
                }
            }
        }
    }

    /**
     * @comment mise � jour de la comboBox1
     * On gere combo1 � part car on peut vouloir garder son selected (pour l'option : "modifier")
     */
    private void combo1Update() {
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Sélectionnez");
        Pile<String> collChemin = xmlFile.readXml("CHEMIN");
        for (int i = 0; i < collChemin.getSize(); i++) {
            jComboBox1.addItem(collChemin.get(i));
        }
    }

    /**
     * @comment mise � jour des jcomboBox deux � huit
     */
    private void comboUpdate() {
        Pile<String> fichier = xmlFile.readXml("FICHIER");
        fichier.sort();
        jComboBox2.removeAllItems();
        jComboBox2.addItem("Sélectionnez");
        for (int i = 0; i < fichier.getSize(); i++) {
            jComboBox2.addItem(xmlFile.readXml("FICHIER").get(i));
        }

        //On r�cup�re la collection obtenu en resultat pour pouvoir la manipuler plus facilement sans avoir � rappeler la m�thode readXml
        Pile<String> collecStr = xmlFile.readXml("NOM");
//        System.out.println("Avant" + collecStr);//DEBUG
        collecStr.sort(); //Fait le tri de la collection, �vite les doublon
//        System.out.println("Apres" + collecStr);//DEBUG
        jComboBox3.removeAllItems();//Suppresion de tous les objets contenu dans la jcombobox
        jComboBox3.addItem("Sélectionnez");
        for (int i = 0; i < collecStr.getSize(); i++) { //Boucle sur les r�sultat du xml
            String str = collecStr.get(i).toString();
            if (collecStr.get(i).compareTo("") != 0) { //On refuse les cha�nes vides
                jComboBox3.addItem(collecStr.get(i));//Ajout
            }
        }

        Pile<String> auteur = xmlFile.readXml("AUTEUR");
        auteur.sort();
        jComboBox4.removeAllItems();
        jComboBox4.addItem("Sélectionnez");
        for (int i = 0; i < auteur.getSize(); i++) {
            if (auteur.get(i).compareTo("") != 0) {
                jComboBox4.addItem(auteur.get(i));
            }
        }

        Pile<String> album = xmlFile.readXml("ALBUM");
        album.sort();
        jComboBox5.removeAllItems();
        jComboBox5.addItem("Sélectionnez");
        for (int i = 0; i < album.getSize(); i++) {
            if (album.get(i).compareTo("") != 0) {
                jComboBox5.addItem(album.get(i));
            }
        }

        Pile<String> difficulte = xmlFile.readXml("DIFFICULTE");
        difficulte.sort();
        jComboBox6.removeAllItems();
        jComboBox6.addItem("Sélectionnez");
        for (int i = 0; i < difficulte.getSize(); i++) {
            if (difficulte.get(i).compareTo("") != 0) {
                jComboBox6.addItem(difficulte.get(i));
            }
        }

        Pile<String> maitrise = xmlFile.readXml("MAITRISE");
        maitrise.sort();
        jComboBox7.removeAllItems();
        jComboBox7.addItem("Sélectionnez");
        for (int i = 0; i < maitrise.getSize(); i++) {
            if (maitrise.get(i).compareTo("") != 0) {
                jComboBox7.addItem(maitrise.get(i));
            }
        }

        Pile<String> commentaire = xmlFile.readXml("COMMENTAIRE");
        commentaire.sort();
        jComboBox8.removeAllItems();
        jComboBox8.addItem("Sélectionnez");
        for (int i = 0; i < commentaire.getSize(); i++) {
            if (commentaire.get(i).compareTo("") != 0) {
                jComboBox8.addItem(commentaire.get(i));
            }
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jFrame1 = new javax.swing.JFrame();
        jLabel9 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jFrame2 = new javax.swing.JFrame();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jFrame3 = new javax.swing.JFrame();
        jFileChooser1 = new javax.swing.JFileChooser();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jComboBox8 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();

        jFrame1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jFrame1.setResizable(false);
        jLabel9.setText("Nom");

        jTextField1.setText("jTextField1");
        jTextField1.setMaximumSize(new java.awt.Dimension(6, 20));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel10.setText("Artiste");

        jButton3.setText("Valider");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Annuler");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField2.setText("jTextField2");

        jLabel11.setText("Album");

        jTextField3.setText("jTextField3");

        jLabel12.setText("Difficulte");

        jTextField4.setText("jTextField4");

        jLabel13.setText("Maitrise");

        jTextField5.setText("jTextField5");

        jLabel14.setText("Commentaire");

        jTextField6.setText("jTextField6");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jFrame1Layout = new org.jdesktop.layout.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
                jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jFrame1Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jLabel11)
                                        .add(jLabel12)
                                        .add(jLabel13)
                                        .add(jLabel14)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jFrame1Layout.createSequentialGroup()
                                                .add(jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel10)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel9)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jButton3)
                                                .add(9, 9, 9)
                                                .add(jButton4)))
                                .addContainerGap())
        );
        jFrame1Layout.setVerticalGroup(
                jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jFrame1Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel9)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel10)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel11)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel12)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel13)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTextField5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel14)
                                .add(jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jFrame1Layout.createSequentialGroup()
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                        .add(jButton3)
                                                        .add(jButton4))
                                                .addContainerGap())
                                        .add(jFrame1Layout.createSequentialGroup()
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jTextField6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())))
        );
        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel15.setText("Fichier");

        jLabel16.setText("jLabel15");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel17.setText("Nom");

        jLabel18.setText("jLabel15");

        jLabel19.setText("Artiste");

        jLabel20.setText("jLabel15");

        jLabel21.setText("Album");

        jLabel22.setText("jLabel15");

        jLabel23.setText("Difficulte");

        jLabel24.setText("jLabel15");

        jLabel25.setText("Maitrise");

        jLabel26.setText("jLabel15");

        jLabel27.setText("Commentaire");

        jLabel28.setText("jlabel28");

        jButton5.setText("Valider");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Annuler");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jFrame2Layout = new org.jdesktop.layout.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
                jFrame2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jFrame2Layout.createSequentialGroup()
                                .addContainerGap(251, Short.MAX_VALUE)
                                .add(jButton5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton6)
                                .addContainerGap())
                        .add(jFrame2Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel27)
                                .addContainerGap(332, Short.MAX_VALUE))
                        .add(jFrame2Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jFrame2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jLabel28, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                                        .add(jLabel15)
                                        .add(jLabel16)
                                        .add(jLabel17)
                                        .add(jLabel19)
                                        .add(jLabel20)
                                        .add(jLabel21)
                                        .add(jLabel22)
                                        .add(jLabel24)
                                        .add(jLabel23)
                                        .add(jLabel25)
                                        .add(jLabel26)
                                        .add(jLabel18))
                                .addContainerGap())
        );
        jFrame2Layout.setVerticalGroup(
                jFrame2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jFrame2Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel15)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel16)
                                .add(16, 16, 16)
                                .add(jLabel17)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel18)
                                .add(14, 14, 14)
                                .add(jLabel19)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel20)
                                .add(16, 16, 16)
                                .add(jLabel21)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel22)
                                .add(16, 16, 16)
                                .add(jLabel23)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel24)
                                .add(17, 17, 17)
                                .add(jLabel25)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel26)
                                .add(16, 16, 16)
                                .add(jLabel27)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel28)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                                .add(jFrame2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jButton6)
                                        .add(jButton5))
                                .addContainerGap())
        );
        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jFrame3Layout = new org.jdesktop.layout.GroupLayout(jFrame3.getContentPane());
        jFrame3.getContentPane().setLayout(jFrame3Layout);
        jFrame3Layout.setHorizontalGroup(
                jFrame3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jFrame3Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jFileChooser1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jFrame3Layout.setVerticalGroup(
                jFrame3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jFrame3Layout.createSequentialGroup()
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jFileChooser1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"S\u00e9lectionnez"}));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setLabel("Valider");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setLabel("Quitter");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"S\u00e9lectionnez"}));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"S\u00e9lectionnez"}));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"S\u00e9lectionnez"}));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"S\u00e9lectionnez"}));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"S\u00e9lectionnez"}));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"S\u00e9lectionnez"}));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"S\u00e9lectionnez"}));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        jLabel1.setText("Fichier");

        jLabel2.setText("Nom du Fichier");

        jLabel3.setText("Nom");

        jLabel4.setText("Artiste");

        jLabel5.setText("Album");

        jLabel6.setText("Difficulte");

        jLabel7.setText("Maitrise");

        jLabel8.setText("Commentaire");

        jMenu1.setText("Menu");
        jMenuItem1.setLabel("Modifier");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Afficher");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Voir le xml");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Aide");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("Quitter");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Outils");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem6.setText("Mettre \u00e0 jour le xml");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });

        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("Remise \u00e0 z\u00e9ro de la recherche");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });

        jMenu2.add(jMenuItem7);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .add(jButton1)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(jButton2))
                                                .add(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .add(jComboBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .add(jComboBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .add(jComboBox5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .add(jComboBox6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .add(jComboBox7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                        .add(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jLabel1))
                                        .add(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jLabel2))
                                        .add(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jLabel3))
                                        .add(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jLabel4))
                                        .add(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jLabel5))
                                        .add(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jLabel6))
                                        .add(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(jComboBox8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                        .add(jLabel7)
                                                        .add(jLabel8))))
                                .addContainerGap(403, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(107, 107, 107)
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel2)
                                .add(6, 6, 6)
                                .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel3)
                                .add(6, 6, 6)
                                .add(jComboBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel4)
                                .add(6, 6, 6)
                                .add(jComboBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel5)
                                .add(6, 6, 6)
                                .add(jComboBox5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel6)
                                .add(6, 6, 6)
                                .add(jComboBox6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel7)
                                .add(6, 6, 6)
                                .add(jComboBox7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel8)
                                .add(6, 6, 6)
                                .add(jComboBox8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 47, Short.MAX_VALUE)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jButton2)
                                        .add(jButton1))
                                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        if (evt.getActionCommand().equals("ApproveSelection")) {                //Si l'utilisateur appuie sur le bouton ok
            gpDir = jFileChooser1.getSelectedFile().getAbsolutePath();  //R�cup�re le r�pertoire des fichiers gp donn� par l'utilisateur
//            System.out.println("gpDir=" + gpDir);//DEBUG
            xmlFile.insertDir(gpDir);//On cr�er l'�l�ment r�pertoire dans le xml avec en valeur le choix de l'utilisateur
            jFrame3.dispose();                                          //On se s�pare de jfram3 qui contint le filechooser
            setSize(900, 700);
            Pile<String> listPath = new Pile<String>();
            FilesLister filesList = new FilesLister();
            listPath = filesList.gpLister(gpDir, listPath);             //on lit le repertoire pour lister les tablatures
            for (int i = 0; i < listPath.getSize(); i++) {
                xmlFile.insertNode(listPath.get(i));
            }
            xmlFile.save();                                             //On enregistre les modifications
            combo1Update();
            comboUpdate();                                              //On genere les autres comboBox
            show();                                                     //D�voile la jframe � l'utilisateur
        }
        if (evt.getActionCommand().equals("CancelSelection")) {                 //S'il appuie sur le bouton cancel
            System.exit(0); //Quitte l'application
        }
    }//GEN-LAST:event_jFileChooser1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        combo1Update();
        comboUpdate(); //On appel la mise � jour des combos
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jFrame2.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jButton1ActionPerformed(evt); //On appel la m�thode du jButton1
        jFrame2.dispose();//On retire la jFrame2
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        xmlUpdate(); //On mets le xml � jour
        xmlFile.save();
        combo1Update();
        comboUpdate();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        //Si on a autre chose que la String S�lectionnez de s�lectionn� dans la combobox1
        if (jComboBox1.getSelectedItem().toString().compareTo("Sélectionnez") != 0) {
            jLabel16.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "CHEMIN"));
            jLabel18.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "NOM"));
            jLabel20.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "AUTEUR"));
            jLabel22.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "ALBUM"));
            jLabel24.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "DIFFICULTE"));
            jLabel26.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "MAITRISE"));
            jLabel28.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "COMMENTAIRE"));
            jFrame2.setSize(700, 500); //On modifie la taille du form pour quelle soit confortable
            jFrame2.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        System.out.println("passage par ouverture xml=" + pathXml);
        try {
            File file = new File(pathXml);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (Exception e) {
            System.out.println("erreur d'execution " + e.toString());
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {
            File file = new File(pathHtml);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (Exception e) {
            System.out.println("erreur d'execution " + e.toString());
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        System.exit(0); //Quitte l'application
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jFrame1.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        xmlFile.modifElement(jComboBox1.getSelectedItem().toString(), "NOM", jTextField1.getText());
        xmlFile.modifElement(jComboBox1.getSelectedItem().toString(), "AUTEUR", jTextField2.getText());
        xmlFile.modifElement(jComboBox1.getSelectedItem().toString(), "ALBUM", jTextField3.getText());
        xmlFile.modifElement(jComboBox1.getSelectedItem().toString(), "DIFFICULTE", jTextField4.getText());
        xmlFile.modifElement(jComboBox1.getSelectedItem().toString(), "MAITRISE", jTextField5.getText());
        xmlFile.modifElement(jComboBox1.getSelectedItem().toString(), "COMMENTAIRE", jTextField6.getText());
        xmlFile.save();
        comboUpdate();//On regenere les comboBox car il y a eu changement dans le xml, mais pas le combobox1 puisqu'il n'y a pas de nouvelle chanson
        jFrame1.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // jTextField1.setText(jComboBox3.getSelectedItem().toString());
        if (jComboBox1.getSelectedItem().toString().compareTo("Sélectionnez") != 0) { //Si on a autre chose que la String S�lectionnez de s�lectionn� dans la combobox
            jTextField1.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "NOM"));
            jTextField2.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "AUTEUR"));
            jTextField3.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "ALBUM"));
            jTextField4.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "DIFFICULTE"));
            jTextField5.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "MAITRISE"));
            jTextField6.setText(xmlFile.readElement(jComboBox1.getSelectedItem().toString(), "COMMENTAIRE"));
            jFrame1.setSize(400, 400);
            jFrame1.setVisible(true); //REnd visible notre frame pour les modifications
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            File file = new File(jComboBox1.getSelectedItem().toString());
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);

        } catch (Exception e) {
            System.out.println("erreur d'execution " + e.toString());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        if (jComboBox8.getItemCount() > 0) {
            if (jComboBox8.getSelectedItem().toString() != "Sélectionnez") {
                jComboBox1.removeAllItems();
                Pile<String> list = xmlFile.pathFilter("COMMENTAIRE", jComboBox8.getSelectedItem().toString());
                for (int i = 0; i < list.getSize(); i++) {
                    jComboBox1.addItem(list.get(i));
                }
            }
        }
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        if (jComboBox7.getItemCount() > 0) {
            if (jComboBox7.getSelectedItem().toString() != "Sélectionnez") {
                jComboBox1.removeAllItems();
                Pile<String> list = xmlFile.pathFilter("MAITRISE", jComboBox7.getSelectedItem().toString());
                for (int i = 0; i < list.getSize(); i++) {
                    jComboBox1.addItem(list.get(i));
                }
            }
        }
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        if (jComboBox6.getItemCount() > 0) {
            if (jComboBox6.getSelectedItem().toString() != "Sélectionnez") {
                jComboBox1.removeAllItems();
                Pile<String> list = xmlFile.pathFilter("DIFFICULTE", jComboBox6.getSelectedItem().toString());
                for (int i = 0; i < list.getSize(); i++) {
                    jComboBox1.addItem(list.get(i));
                }
            }
        }
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        if (jComboBox5.getItemCount() > 0) {
            if (jComboBox5.getSelectedItem().toString() != "Sélectionnez") {
                jComboBox1.removeAllItems();
                Pile<String> list = xmlFile.pathFilter("ALBUM", jComboBox5.getSelectedItem().toString());
                for (int i = 0; i < list.getSize(); i++) {
                    jComboBox1.addItem(list.get(i));
                }
            }
        }
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        if (jComboBox4.getItemCount() > 0) {
            if (jComboBox4.getSelectedItem().toString() != "Sélectionnez") {
                jComboBox1.removeAllItems();
                Pile<String> list = xmlFile.pathFilter("AUTEUR", jComboBox4.getSelectedItem().toString());
                for (int i = 0; i < list.getSize(); i++) {
                    jComboBox1.addItem(list.get(i));
                }
            }
        }
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        if (jComboBox3.getItemCount() > 0) { //Si on a des items dans la combo
            if (jComboBox3.getSelectedItem().toString() != "Sélectionnez") { //Si ce n'est pas "S�lectionnez" qui est s�lectionn�
                jComboBox1.removeAllItems(); //On supprime tous les �l�ments dans la combo1
                Pile<String> list = xmlFile.pathFilter("NOM", jComboBox3.getSelectedItem().toString()); //On appel la m�thode qui r�cup�re tous les CHEMIN qui ont en enfant une balise (1er param�tre) � la valeur du 2nd param�tre
                for (int i = 0; i < list.getSize(); i++) {
                    jComboBox1.addItem(list.get(i));
                }
            }
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0); //Quitte l'application
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox <String> jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JFrame jFrame3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables

}
