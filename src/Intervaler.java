import java.util.Timer;
import javax.swing.*;
import java.awt.Dimension;

class Intervaler {
    public static void main(String[] args) {

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("I am running");
        f.setLocation(200, 200);
        Dimension minSize = new Dimension(600,200);
        f.setMinimumSize(minSize);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new DeviceSpy(f), 1000, 1000);
        f.pack();
        f.setVisible(true);
    }
}

