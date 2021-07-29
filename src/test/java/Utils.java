import java.io.FileWriter;
import java.io.IOException;
public class Utils {

    public String writeToFile(String title, String paragraph) {
        try {
            FileWriter textFile = new FileWriter("searchResults.txt");

            textFile.append(title + " : \n" + paragraph);

            textFile.close();
            return null;
        } catch (IOException e) {
            return e.getMessage();
        }

    }
}
