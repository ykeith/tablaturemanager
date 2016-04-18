import java.io.File;

/**
 * Class which handle files listing
 *
 * @author Yves Keith
 */
public class FilesLister {
    /**
     * List .gp* and .gtp files (recursively)
     *
     * @param _path path to analyze
     * @param _pilePath result list
     * @return Pile<String>
     */
    Pile<String> gpLister(String _path, Pile<String> _pilePath){
        File path = new File(_path);
        // Regular expressions for .gp* and gtp
        String regExGP = "(.*[.]{1}g{1}p{1}[1-9]{1}$)";
        String regExGPT = "(.*[.]gtp$)";
        String dir;

        for(int i=0;i<path.list().length;i++){
            File dirTest = new File(_path + File.separator + path.list()[i]);
            if (dirTest.isDirectory()) {
                // Rebuild directory
                dir = _path + File.separator + path.list()[i];
                gpLister(dir, _pilePath);
            }
            else if(path.list()[i].matches(regExGP) || path.list()[i].matches(regExGPT)) {
                // Rebuild path
                String fileGp = path.toString() + File.separator + path.list()[i];
                // Add to collection
                _pilePath.pile(fileGp);
            }
        }
        return _pilePath;
    }

    /**
     * Test if file exist
     *
     * @param _pathFileToFound filename to check
     * @return boolean
     */
    boolean lookingForFile(String _pathFileToFound){
        File file = new File(_pathFileToFound);
        return file.exists();
    }
}
