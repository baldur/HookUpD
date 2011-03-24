import java.util.TimerTask;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeviceSpy extends TimerTask {
    public void run() {
        // this will be in a settings file
        Properties prop = new Properties();
        File[] volumes = null;
        List<String> drives = null;
        String destinationFolder = null;
        try {
            // the configuration file name
            String fileName = "app.config";            
            InputStream is = new FileInputStream(fileName);
            // load the properties file
            prop.load(is);
            String[] knownDrives = prop.getProperty("app.usb_devices").split(",");
            destinationFolder = prop.getProperty("app.destination");
            String mountPoint = prop.getProperty("app.mountPoint");
            drives = Arrays.asList(knownDrives);
            volumes = new File(mountPoint).listFiles();  
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (File volume : volumes) {
            if(drives.indexOf(volume.toString()) > -1) {
                ArrayList relocationCandidates = ReLocator.listFiles(volume, new ArrayList(100));
                Iterator iterator = relocationCandidates.iterator();
                while(iterator.hasNext()) {
                    try {
                        Object ob = iterator.next();
                        File file = new File(ob.toString());
                        ReLocator.cp(file.toString(), destinationFolder+file.getName());
                    }catch(java.io.IOException exp){ 
                        exp.printStackTrace();
                    } 

                }
            }
        }
    }
}

