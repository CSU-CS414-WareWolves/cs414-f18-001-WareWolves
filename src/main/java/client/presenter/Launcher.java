package client.presenter;

import client.presenter.ai.AiDriver;
import server.ChadServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class Launcher {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        switch (args[0]){
            case "interface":
                ChadPresenter app = new ChadPresenter(args[2], args[3], args[1]);
                app.start(args[2]);
                break;
            case "AI":
                AiDriver driver = new AiDriver(InetAddress.getByName(args[1]), Integer.parseInt(args[2]));
                break;
            case "server":
                int port = Integer.parseInt(args[1]);
                ChadServer server = new ChadServer(port);
                server.start();
        }
    }
}
