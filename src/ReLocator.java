import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;

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

        if(fileAlreadyCopied(md5)) {
            return;
        }

        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        int c;

        while ((c = in.read()) != -1) {
            out.write(c);
        }

        storeMD5s(md5);
        in.close();
        out.close();
    }

    private Boolean fileAlreadyCopied(String md5) {
        ArrayList md5s = readMD5s();
        int index = md5s.indexOf(md5);
        if(index == -1) {
            return false;
        } else {
            return true;
        }
    }

    private ArrayList readMD5s() {
        ArrayList md5s = null;
        try {
            String xml = "filesCopied.xml";
            XMLDecoder d = new XMLDecoder(
                               new BufferedInputStream(
                                   new FileInputStream( xml )));
            Object result = d.readObject();
            md5s = (ArrayList)result;
            d.close();
        } catch(Exception e) {
            // an error occurs when there is now filesCopied.xml file
            // then it's best just to write an empty array list and get on
            // with it
            md5s = new ArrayList();
            writeMD5(md5s);
        }
        return md5s;
    }

    public void storeMD5s(String md5) {
        ArrayList md5s = readMD5s();
        md5s.add(md5);
        writeMD5(md5s);
    }
    
    private void writeMD5(ArrayList md5s) {
        try {
            String xml = "filesCopied.xml";
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
                                     new FileOutputStream( xml )));
            e.writeObject(md5s);
            e.close();
        } catch(Exception e) {
        }
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

