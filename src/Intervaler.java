import java.util.Timer;
import javax.swing.*;

class Intervaler {
    public static void main(String[] args) {

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("I am running");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new DeviceSpy(), 1000, 1000);
        f.pack();
        f.setVisible(true);
    }
}

