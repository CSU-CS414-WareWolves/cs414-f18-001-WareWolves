package edu.colostate.cs.cs414.warewolves.chad;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.ChadPresenter;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.ai.AiDriver;
import edu.colostate.cs.cs414.warewolves.chad.server.ChadServer;

import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

public class Launcher {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        switch (args[0]){
            case "interface":
                ChadPresenter app = new ChadPresenter(args[2], args[3], args[1]);
                app.start();
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
