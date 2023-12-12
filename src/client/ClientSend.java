package client;

import common.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientSend implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    public ClientSend(Client client) {
        try {
            this.socket = new Socket(client.getAddress(), client.getPort());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Votre message >> ");
            String m = sc.nextLine();
            Message message = new Message("client", m);
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
