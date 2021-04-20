import java.util.Scanner;
import static java.lang.Integer.valueOf;

public class Reactor {
    
    Boolean enabled = true;
    Integer temp = 20;
    Integer targTemp = 80;

    public static void main(String[] args) throws InterruptedException {
        final Reactor reactor = new Reactor();
        final boolean enabled = true;

        Runnable uran = new Runnable() {
            public void run() {
                while (reactor.enabled) {
                    synchronized (reactor) {
                        reactor.temp++;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Runnable observ = new Runnable() {
            public void run() {
                while (enabled || reactor.temp > -1) {
                    System.out.println("TEMP:" + reactor.temp);
                    if(reactor.temp>=100){
                        System.out.println("EXPLOSION");
                        System.exit(0);
                    } else if(reactor.temp<=0){
                        System.out.println("STOPPED");
                        System.exit(0);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Runnable absorb = new Runnable() {
            public void run() {
                while (reactor.enabled || reactor.temp > 0) {
                    synchronized (reactor) {
                        if (reactor.temp > reactor.targTemp) {
//                            System.out.println("ABSORB: " + reactor.temp);
                            int diff = Math.abs(reactor.targTemp - reactor.temp);
                            if (diff < 10) reactor.temp -= 2;
                            else if (diff > 10 && diff < 20) reactor.temp -= 3;
                            else if (diff > 20) reactor.temp -= 4;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(uran).start();
        new Thread(absorb).start();
        new Thread(observ).start();

        while (reactor.enabled) {
            Scanner s = new Scanner(System.in);
            if (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.equals("stop")) reactor.enabled = false;
                else reactor.targTemp = valueOf(line);
            }
            Thread.sleep(100);
        }
    }

}
