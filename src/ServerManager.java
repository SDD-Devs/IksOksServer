//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ServerManager {
    public static ArrayList<Player> AllPlayers = new ArrayList();
    public static NavigableMap<Integer, ServerRoom> ServerRoomList = new TreeMap();

    public ServerManager() {
    }

    public static int assignSocketWithRoom(Player player) {
        ServerRoom sRoom = new ServerRoom(player);
        player.room = sRoom.roomID;
        int roomid = sRoom.roomID;
        AllPlayers.add(player);
        ServerRoomList.put(sRoom.roomID, sRoom);
        printnTheList();
        return roomid;
    }

    public static void printnTheList() {
        Iterator var1 = ServerRoomList.values().iterator();

        while(var1.hasNext()) {
            ServerRoom sRoom = (ServerRoom)var1.next();
            System.out.println("=============Room" + sRoom.roomID + "=============");
            if (sRoom.p1 != null) {
                System.out.println("Player 1 add: " + sRoom.p1.socket.getInetAddress());
                System.out.println("Player 1 roomID: " + sRoom.p1.getRoom());
            }

            if (sRoom.p2 != null) {
                System.out.println("Player 2 add: " + sRoom.p2.socket.getInetAddress());
                System.out.println("Player 2 roomID: " + sRoom.p2.getRoom());
            }

            System.out.println("Room Status: " + sRoom.getRoomStatus());
            System.out.println("Room List Size: " + ServerRoomList.size());
            System.out.println("Next player's turn: " + sRoom.nextTurn);
            System.out.println();
        }

    }

    public static void sendData(Player callingPlayer, int room, String cmd) {
        if (ServerRoomList.containsKey(room)) {
            ServerRoom tmpRoom = (ServerRoom)ServerRoomList.get(room);
            if (tmpRoom.getPlayerCount() != 2) {
                System.out.println("Room is empty or missing the 2nd player.");
            } else {
                Player receivingPlayer = null;
                if (tmpRoom.p1.id == callingPlayer.id) {
                    receivingPlayer = tmpRoom.p2;
                    System.out.println("Player 1 same as the calling player");
                }

                if (tmpRoom.p2.id == callingPlayer.id) {
                    receivingPlayer = tmpRoom.p1;
                    System.out.println("Player 2 same as the calling player");
                }

                try {
                    DataOutputStream out = new DataOutputStream(receivingPlayer.socket.getOutputStream());
                    out.writeUTF("SE-" + cmd);
                } catch (IOException var6) {
                    System.out.println("[EXCEPTION] while getting 2nd play output stream.");
                }

            }
        } else {
            System.out.println("Invalid room number");
        }
    }

    public static boolean joinRoomIfExists(int roomID, Player p) {
        if (ServerRoomList.containsKey(roomID)) {
            ((ServerRoom)ServerRoomList.get(roomID)).insertSecondPlayer(p);
            printnTheList();
            return true;
        } else {
            return false;
        }
    }
}
