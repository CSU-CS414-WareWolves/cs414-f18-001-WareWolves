package client.presenter.network.messages;

//Abstract class for messages, shared functionality
public abstract class NetworkMessage {
	public static final NET_MESSAGE_TYPE type = null;
	public int length; 
	public abstract String getDataString();
}
