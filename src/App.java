//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
    public static ServerSocket ss;
    public static volatile int count = 0;

    public App() {
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, This is server.!");
        System.out.println("Waiting for the connection...");
        ss = new ServerSocket(5999);

        while(true) {
            Socket sock = ss.accept();
            Player player = new Player(sock, count);
            System.out.println("Connected to:" + sock.getInetAddress());
            (new Thread(new MultiThreadServer(player))).start();
            ++count;
        }
    }
}
