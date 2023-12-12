package server;

import common.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectedClient implements Runnable{
    private static Integer idCounter = 0;
    private Integer id;
    private Server server;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ConnectedClient(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.id = idCounter++;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Nouvelle connexion, id = " + id);
    }

    @Override
    public void run() {
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean isActive = true;
        while (isActive) {
            try {
                Message message = (Message) in.readObject();
                message.setSender(String.valueOf(id));
                if (message.getContent() == null) {
                    isActive = false;
                    server.disconnectClient(this);
                    throw new IOException("Client " + id + " déconnecté");
                } else {
                    server.broadcastMessage(message, id);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(Message message) throws IOException {
        this.out.writeObject(message);
        this.out.flush();
    }

    public void closeClient() throws IOException {
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    public Integer getId() {
        return id;
    }
}
