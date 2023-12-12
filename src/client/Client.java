package client;

import common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private String address;
    private Integer port;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client(String address, Integer port) throws IOException {
        this.address = address;
        this.port = port;
        this.socket = new Socket(address, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        Thread clientSend = new Thread(new ClientSend(this));
        Thread clientReceive = new Thread(new ClientReceive(this, socket));
        clientSend.start();
        clientReceive.start();
    }

    public void disconnectServer() throws IOException {
        this.in.close();
        this.out.close();
        this.socket.close();
        System.exit(0);
    }

    public void messageRecieved(Message message) {
        System.out.println(message);
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }
}
