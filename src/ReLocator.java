import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ReLocator {

    public String fileName;
    public String targetLocation;
    public String md5sum;

    public ReLocator(String name, String target) {
        fileName = name;
        targetLocation = target;
    }

    public void cp() throws IOException {
        System.out.println(fileName);  
        System.out.println(targetLocation);  
        File inputFile = new File(fileName);
        File outputFile = new File(targetLocation + inputFile.getName());

        FileInputStream fis = new FileInputStream( inputFile );
        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex( fis );
        System.out.println(md5);

        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        int c;

        while ((c = in.read()) != -1) {
            out.write(c);
        }

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
            }
        }
        return filesCollection;
    }
}

