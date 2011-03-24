import java.util.Timer;

class Intervaler {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new DeviceSpy(), 1000, 1000);
    }
}

