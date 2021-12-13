
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Get {
    public static HashMap get_char_num(String file) throws IOException {
        FileReader fr = new FileReader(file);
        char[] buf = new char[1];
        HashMap M = new HashMap<Character, Integer>();
        while (fr.read(buf)!=-1){
            if (M.get(buf[0]) == null){
                M.put(buf[0],1);
            }
            else {
                M.put(buf[0],(int)M.get(buf[0])+1);
            }

        }
        return M;
    }
}
