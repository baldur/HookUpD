import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReLocator {
    public static void cp(String fileToCopy, String copyToFile) throws IOException {
        System.out.println(fileToCopy);  
        System.out.println(copyToFile);  
        File inputFile = new File(fileToCopy);
        File outputFile = new File(copyToFile);

        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        int c;

        while ((c = in.read()) != -1)
            out.write(c);

        in.close();
        out.close();
    }

    public static ArrayList listFiles(File location, ArrayList filesCollection) {
        File[] files = location.listFiles(new MediaFileFilter());
        for(File file : files) {
            if(file.isDirectory()){
                ReLocator.listFiles(file, filesCollection);
            } else {
                filesCollection.add(file);
                //System.out.println(file);
            }
        }
        return filesCollection;
    }
}

