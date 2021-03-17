package Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
   public String generateStringFromJSON(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
