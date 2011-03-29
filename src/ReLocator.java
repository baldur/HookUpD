import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;

public class ReLocator {

    public String fileName;
    public String targetLocation;
    public String md5sum;
    private String basePath = System.getProperty("user.home") + "/Library/Application Support/HookUpD/";
    private String filesCopiedXml = basePath + "filesCopied.xml";

    public ReLocator(String name, String target) {
        fileName = name;
        targetLocation = target;
    }

    static final int BUFF_SIZE = 100000;
    static final byte[] buffer = new byte[BUFF_SIZE];

    public void cp() throws IOException {
        File inputFile = new File(fileName);
        InputStream fis = null;
        OutputStream out = null; 
        String md5 = null;

        try {
            fis = new FileInputStream(fileName);
            //important a new fis needs to be used since I can't figure out how to revind the file in java
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex( new FileInputStream(fileName) );
            if(fileAlreadyCopied(md5)) {
                return;
            }
            out = new FileOutputStream(targetLocation + inputFile.getName());
            while (true) {
                synchronized (buffer) {
                    int amountRead = fis.read(buffer);
                    if (amountRead == -1) {
                        break;
                    }
                    out.write(buffer, 0, amountRead); 
                }
            } 
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (out != null) {
                out.close();
            }
            // document that the file has been copied
            storeMD5s(md5);
        }
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
            new File(basePath).mkdir();
            XMLDecoder d = new XMLDecoder(
                               new BufferedInputStream(
                                   new FileInputStream( filesCopiedXml )));
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
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
                                     new FileOutputStream( filesCopiedXml )));
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

