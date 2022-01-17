//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class MultiThreadServer implements Runnable {
    Socket csocket;
    DataOutputStream out;
    DataInputStream in;
    public static volatile boolean SingleThreadRunning = true;
    Player thisPlayer;

    public MultiThreadServer(Player p) {
        this.csocket = p.socket;
        this.thisPlayer = p;

        try {
            this.out = new DataOutputStream(this.csocket.getOutputStream());
            this.in = new DataInputStream(this.csocket.getInputStream());
        } catch (IOException var3) {
            System.out.println("[EXCEPTION] Error while setting up i/o streams.");
        }

    }

    public void run() {
        String received = "-1";
        String[] cmdArr = null;

        while(SingleThreadRunning) {
            try {
                received = this.in.readUTF();
                System.out.println("Received: " + received);
                cmdArr = received.split("-");
            } catch (Exception var13) {
                System.out.println("[EXCEPTION] connection problem");
                SingleThreadRunning = false;
            }

            ServerManager.printnTheList();
            String var3;
            ServerRoom sRoom;
            DataOutputStream p1OutStream;
            DataOutputStream p2OutStream;
            switch((var3 = cmdArr[0]).hashCode()) {
                case 2159:
                    if (var3.equals("CR")) {
                        int roomID = ServerManager.assignSocketWithRoom(this.thisPlayer);
                        System.out.print("CR command - creating new room.");

                        try {
                            this.out.writeUTF("RCREATED-" + roomID);
                        } catch (IOException var12) {
                            System.out.println("[EXCEPTION] error while sending feedback for create room.");
                        }
                    }
                    break;
                case 2376:
                    if (var3.equals("JR")) {
                        if (!ServerManager.joinRoomIfExists(Integer.parseInt(cmdArr[1]), this.thisPlayer)) {
                            System.out.println("The room [" + Integer.parseInt(cmdArr[1]) + "] has not been created or full.");
                        } else {
                            System.out.print("JR command - joining a room.");

                            try {
                                this.out.writeUTF("RJOINED-" + cmdArr[1]);
                                sRoom = (ServerRoom)ServerManager.ServerRoomList.get(Integer.parseInt(cmdArr[1]));
                                p1OutStream = new DataOutputStream(sRoom.p1.socket.getOutputStream());
                                p2OutStream = new DataOutputStream(sRoom.p2.socket.getOutputStream());
                                boolean coinFlip = false;
                                if (ThreadLocalRandom.current().nextInt(0, 100) > 50) {
                                    coinFlip = true;
                                }

                                if (!coinFlip) {
                                    p1OutStream.writeUTF("GO");
                                    p2OutStream.writeUTF("WAIT");
                                    sRoom.nextTurn = 2;
                                } else {
                                    p2OutStream.writeUTF("GO");
                                    p1OutStream.writeUTF("WAIT");
                                    sRoom.nextTurn = 1;
                                }
                            } catch (IOException var11) {
                                System.out.println("[EXCEPTION] error while sending feedback for join room.");
                            }
                        }
                    }
                    break;
                case 2642:
                    if (var3.equals("SE")) {
                        ServerManager.sendData(this.thisPlayer, Integer.parseInt(cmdArr[1]), cmdArr[2]);
                        System.out.println("SE command - transfering message to other player.");

                        try {
                            sRoom = (ServerRoom)ServerManager.ServerRoomList.get(Integer.parseInt(cmdArr[1]));
                            p1OutStream = new DataOutputStream(sRoom.p1.socket.getOutputStream());
                            p2OutStream = new DataOutputStream(sRoom.p2.socket.getOutputStream());
                            if (sRoom.nextTurn == 1) {
                                p1OutStream.writeUTF("GO");
                                p2OutStream.writeUTF("WAIT");
                                sRoom.nextTurn = 2;
                            } else {
                                p2OutStream.writeUTF("GO");
                                p1OutStream.writeUTF("WAIT");
                                sRoom.nextTurn = 1;
                            }
                        } catch (IOException var10) {
                            System.out.println("[EXCEPTION] while choosing turns.");
                        }
                    }
                    break;
                case 2142494:
                    if (var3.equals("EXIT")) {
                        sRoom = (ServerRoom)ServerManager.ServerRoomList.get(Integer.parseInt(cmdArr[1]));
                        sRoom.removePlayer(this.thisPlayer);
                    }
            }
        }

        try {
            this.csocket.close();
        } catch (IOException var9) {
            System.out.println("[EXCEPTION] Unable to close the connected socket.");
        }

    }
}
