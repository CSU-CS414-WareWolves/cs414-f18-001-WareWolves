package edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages;

/**
 * Abstract class for net messages' shared functionality
 * @author bcgdwn
 *
 */
public abstract class NetworkMessage {
	
	/**
	 * Message type
	 */
	public final NET_MESSAGE_TYPE type;
	/**
	 * Length of data String
	 */
	public int length; 
	/**
	 * Puts the message into a String representation that can be sent down a wire or used in a constructor
	 * @return String form of message
	 */
	public abstract String getDataString();
	
	/**
	 * Super constructor for use by concrete classes
	 * @param Type sets the type field
	 */
	public NetworkMessage(NET_MESSAGE_TYPE Type) {
		type = Type;
	}
	
}
