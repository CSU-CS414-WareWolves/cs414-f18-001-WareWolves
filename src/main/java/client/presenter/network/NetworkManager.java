package client.presenter.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import client.presenter.ChadPresenter;
import client.presenter.network.messages.NetworkMessage;
import java.util.Observable;
import java.util.Observer;

public class NetworkManager implements Observer {
	private Socket sock;
	private Sender send;
	private RecieveThread recv;
	private ChadPresenter presenter;
	
	/**
	 * @constructor
	 * @param addr IP address of server
	 * @param port Port that server is listening on
	 * @throws IOException 
	 */
	public NetworkManager(InetAddress addr, int port, ChadPresenter presenter) throws IOException {
		sock = new Socket(addr, port);
		send = new Sender(sock);
		recv = new RecieveThread(sock, this);
		this.presenter = presenter;
	}
	
	/**
	 * Starts RecieveThread
	 */
	public void startThread() {
		recv.run();
	}
	
	/**
	 * 
	 * @param msg The Message object to send
	 * @return True if send successful, false if send failed
	 */
	public boolean sendMessage(NetworkMessage msg) {
		return send.sendToServer(msg);
	}

  /**
   *
   * @param msg The Message object to send
   */
	protected void sendToPresenter(NetworkMessage msg) {
		presenter.handleNetMessage(msg);
	}


	@Override
	public void update(Observable o, Object arg) {
    NetworkMessage message = (NetworkMessage) arg;
    presenter.handleNetMessage(message);

  }
}
