package server;

import common.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private Integer port;
    private List<ConnectedClient> clients;

    public Server(Integer port) {
        this.port = port;
        this.clients = new ArrayList<ConnectedClient>();
        Thread threadConnection = new Thread(new Connection(this));
        threadConnection.start();
    }

    public void addClient(ConnectedClient newClient) {
        this.clients.add(newClient);
        Message m = new Message(String.valueOf(newClient.getId()), newClient.getId()+" vient de se connecter");
        broadcastMessage(m, newClient.getId());
    }

    public void broadcastMessage(Message m, Integer id) {
        for (ConnectedClient client : clients) {
            if (!client.getId().equals(id)) {
                try {
                    client.sendMessage(m);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void disconnectClient(ConnectedClient client) throws IOException {
        client.closeClient();
        this.clients.remove(client);
        Message m = new Message(String.valueOf(client.getId()), client.getId()+" vient de se déconnecter");
        broadcastMessage(m, client.getId());
    }

    public Integer getPort() {
        return port;
    }
}
