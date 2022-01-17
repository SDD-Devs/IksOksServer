//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.net.Socket;

class Player {
    Socket socket;
    int id;
    int room;

    public Player(Socket s) {
        this.socket = s;
    }

    public Player(Socket s, int identification) {
        this.socket = s;
        this.id = identification;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom() {
        return this.room;
    }

    public void setRoom(int room) {
        this.room = room;
    }
}
