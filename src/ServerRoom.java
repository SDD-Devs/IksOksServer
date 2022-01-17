//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.concurrent.ThreadLocalRandom;

public class ServerRoom {
    public static final int RAND_MIN = 10000;
    public static final int RAND_MAX = 99999;
    Player p1;
    Player p2;
    String roomStatus;
    int roomID;
    int nextTurn = 0;

    public ServerRoom(Player pl1) {
        this.p1 = pl1;
        this.roomID = ThreadLocalRandom.current().nextInt(10000, 100000);
        this.p1.room = this.roomID;
    }

    public void insertSecondPlayer(Player p) {
        this.p2 = p;
        this.p2.room = this.roomID;
    }

    public int getPlayerCount() {
        int count = 0;
        if (this.p1 != null) {
            ++count;
        }

        if (this.p2 != null) {
            ++count;
        }

        return count;
    }

    public String getRoomStatus() {
        int pCount = this.getPlayerCount();
        if (pCount == 0) {
            this.roomStatus = "empty";
        }

        if (pCount == 1) {
            this.roomStatus = "Waiting for P2";
        }

        if (pCount == 2) {
            this.roomStatus = "full";
        }

        return this.roomStatus;
    }

    public int getRoomID() {
        return this.roomID;
    }

    public void removePlayer(Player p) {
        if (p == this.p1) {
            this.p1 = null;
            System.out.print("Removed p1");
        } else if (p == this.p2) {
            this.p2 = null;
            System.out.print("Removed p2");
        }

        if (this.p1 == null && this.p2 == null) {
            ServerManager.ServerRoomList.remove(this.roomID);
        }

        ServerManager.printnTheList();
    }
}
