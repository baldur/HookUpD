import java.io.File;

public class MediaFileFilter implements java.io.FileFilter {
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String name = f.getName().toLowerCase();
        return name.endsWith("mp4") || name.endsWith("jpg");
    }
}

