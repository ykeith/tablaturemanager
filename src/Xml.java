import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Handle XML file
 */
public class Xml {

    /**
     * Path of the XML file
     */
    private String path;
    private Element root;
    private Document document;
    private File fileXml;

    public static final String ROOT_TAG = "files";
    public static final String DIRECTORY_TAG = "directory";
    public static final String MUSIC_TAG = "music";
    public static final String PATH_TAG = "path";
    public static final String FILE_TAG = "file";
    public static final String NAME_TAG = "name";
    public static final String AUTHOR_TAG = "author";
    public static final String ALBUM_TAG = "album";
    public static final String DIFFICULTY_TAG = "difficulty";
    public static final String MASTERY_TAG = "mastery";
    public static final String COMMENT_TAG = "comment";

    /**
     * Constructor
     */
    public Xml() {
        root = new Element(ROOT_TAG);
        document = new Document(root);
    }

    /**
     * Read the root
     *
     * @throws Exception
     */
    private void readRoot() throws Exception {
        SAXBuilder sxb = new SAXBuilder();
        document = sxb.build(new File(path));
        root = document.getRootElement();
    }

    /**
     * Get an element from a music node
     *
     * @param _pathNode string content of the path node (acting like an index for music node)
     * @param _tagName  tagName of the researched element
     * @return Element
     */
    private Element getElement(String _pathNode, String _tagName) throws Exception {
        List listMusique = root.getChildren(MUSIC_TAG);
        Iterator i = listMusique.iterator();
        while (i.hasNext()) {
            Element current = (Element) i.next();
            // Found by index (path node)
            if (current.getChildText(PATH_TAG).compareTo(_pathNode) == 0) {
                // Get the element which have the good tag name
                return current.getChild(_tagName.toLowerCase());
            }
        }
        throw new Exception("An error occurred while trying to get access to a node");
    }

    /**
     * Set an element value
     *
     * @param _pathNode string content of the path node (acting like an index for music node)
     * @param _tagName  tagName of the element to by modify
     * @param _value    value to set
     */
    public void setElementValue(String _pathNode, String _tagName, String _value) {
        try {
            Element element = getElement(_pathNode, _tagName);
            element.setText(_value);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Get the value of an element
     *
     * @param _pathNode string content of the path node (acting like an index for music node)
     * @param _tagName  tagName of the element to by modify
     * @return String
     */
    public String readElement(String _pathNode, String _tagName) {
        String value = null;
        try {
            Element element = getElement(_pathNode, _tagName);
            value = element.getText();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return value;
    }

    /**
     * Find the path (acting like an index) of every nodes that contain a specific node with a specific value
     *
     * @param _tagName tag name to search
     * @param _value   value to search
     * @return Pile<String>
     */
    public Pile<String> findPath(String _tagName, String _value) {
        List musicList = root.getChildren(MUSIC_TAG);
        Iterator i = musicList.iterator();
        Pile<String> pathList = new Pile();
        while (i.hasNext()) {
            Element current = (Element) i.next();
            // If current contain the specific node with the specific value
            if (current.getChildText(_tagName).compareTo(_value) == 0) {
                pathList.pile(current.getChildText(PATH_TAG));
            }
        }
        return pathList;
    }

    /**
     * Show the XML
     */
    public void show() {
        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(document, System.out);
        } catch (IOException e) {
            System.out.println("An error occurred while displaying the XML");
        }
    }

    /**
     * Set the File
     *
     * @param _filePath path of the XML file
     */
    public void setFile(String _filePath) {
        path = _filePath;
        fileXml = new File(path);
    }

    /**
     * Add directory tag in the XML with value
     *
     * @param _directoryName Path of the directory (which should contain gp files)
     */
    public void addDirectory(String _directoryName) {
        Element directory = new Element(DIRECTORY_TAG);
        directory.setText(_directoryName);
        root.addContent(directory);
    }

    /**
     * Add a node to the xml
     *
     * @param _path content of the path node (acting like an index for music node)
     */
    public void addNode(String _path) {
        Element music = new Element(MUSIC_TAG);
        root.addContent(music);
        music.addContent(new Element(PATH_TAG).setText(_path));
        music.addContent(new Element(FILE_TAG));
        music.addContent(new Element(NAME_TAG));
        music.addContent(new Element(AUTHOR_TAG));
        music.addContent(new Element(ALBUM_TAG));
        music.addContent(new Element(DIFFICULTY_TAG));
        music.addContent(new Element(MASTERY_TAG));
        music.addContent(new Element(COMMENT_TAG));
    }

    /**
     * Save the XML
     */
    public void save() {
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            xmlOutputter.output(document, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Return each value contain in node with the given tag name
     *
     * @param _tagName Tag name to found
     * @return Pile<String>
     */
    public Pile<String> find(String _tagName) {
        List musicList = root.getChildren(MUSIC_TAG);
        Iterator i = musicList.iterator();
        Pile<String> result = new Pile();
        while (i.hasNext()) {
            Element current = (Element) i.next();
            result.pile(current.getChild(_tagName.toLowerCase()).getText());
        }
        return result;
    }

    /**
     * Return each value contain in path node
     *
     * @return Pile<String>
     */
    public Pile<String> getAllPath() {
        return find(PATH_TAG);
    }

    /**
     * Delete a node
     *
     * @param _path content of the path node (acting like an index for music node)
     */
    public void deleteNode(String _path) {
        try {
            Element element = getElement(_path, PATH_TAG);
            Element parentElement = element.getParentElement();
            // Move another time to the parent in order to delete our node (can't delete directly on the node)
            parentElement.getParent().removeContent(parentElement);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Get value of directory node
     *
     * @return String
     * @throws Exception
     */
    public String getDirectory() throws Exception {
        readRoot();
        return root.getChildText(DIRECTORY_TAG);
    }

    /**
     * Update the XML regarding a new listing of the directory
     * If there is news files, add them to the XML
     * If some files are gone, remove them from the XML
     */
    public void update() {
        try {
            FilesLister filesList = new FilesLister();
            Pile<String> allPath = getAllPath();
            Pile<String> newPath = new Pile<String>();
            Pile<String> newAllPath = filesList.gpLister(getDirectory(), newPath);

            if (newAllPath.getSize() > allPath.getSize()) {
                for (int i = 0; i < newAllPath.getSize(); i++) {
                    int verif = 0;
                    for (int j = 0; j < allPath.getSize(); j++) {
                        if (newAllPath.get(i).compareTo(allPath.get(j)) == 0) {
                            verif++;
                        }
                    }
                    if (verif == 0) {
                        addNode(newAllPath.get(i));
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
                        deleteNode(allPath.get(i));
                    }
                }
            }
            save();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Load data to the XML
     * Used for initialization
     */
    public void load() {
        // Initialize file
        save();
        try {
            Pile<String> listPath = new Pile<String>();
            FilesLister filesList = new FilesLister();
            listPath = filesList.gpLister(getDirectory(), listPath);

            for (int i = 0; i < listPath.getSize(); i++) {
                addNode(listPath.get(i));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        save();
    }
}