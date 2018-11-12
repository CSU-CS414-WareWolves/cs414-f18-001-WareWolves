package client.presenter.network.messages;

import java.util.Arrays;

public class ProfileResponse extends NetworkMessage {

	public final String[] whitePlayers;
	public final String[] blackPlayers;
	public final String[] startDates;
	public final String[] endDates;
	public final boolean[] results;// T=black win, F=white win,
	
	/**
	 * Constructor for Server response
	 * @param whitePlayers List of white players of games the requested nickname has played in 
	 * @param blackPlayers List of black players of games the requested nickname has played in
	 * @param startDates List of start dates of games the requested nickname has played in
	 * @param endDates List of end dates of games the requested nickname has played in
	 * @param results Results of games the requested nickname has played in
	 */
	public ProfileResponse(String[] whitePlayers, String[] blackPlayers, String[] startDates,
			String[] endDates, boolean[] results) {
		super(NET_MESSAGE_TYPE.PROFILE_RESPONSE);
		this.whitePlayers = Arrays.copyOf(whitePlayers, whitePlayers.length);
		this.blackPlayers = Arrays.copyOf(blackPlayers, blackPlayers.length);
		this.startDates = Arrays.copyOf(startDates, startDates.length);
		this.endDates = Arrays.copyOf(endDates, endDates.length);
		this.results = Arrays.copyOf(results, results.length);
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for presenter
	 * @param data String representation of the message
	 */
	public ProfileResponse(String data) {
		super(NET_MESSAGE_TYPE.PROFILE_RESPONSE);
		String[] recs = data.split("#");
		this.whitePlayers = new String[recs.length];
		this.blackPlayers = new String[recs.length];
		this.startDates = new String[recs.length];
		this.endDates = new String[recs.length];
		this.results = new boolean[recs.length];
		for(int i=0;i<recs.length;i++) {
			String[] splt = recs[i].split(":");
			if(i==0) {
				whitePlayers[i]=splt[1];
				blackPlayers[i]=splt[2];
				startDates[i]=splt[3];
				endDates[i]=splt[4];
				results[i]=Boolean.parseBoolean(splt[5]);
			}
			else {
				whitePlayers[i]=splt[0];
				blackPlayers[i]=splt[1];
				startDates[i]=splt[2];
				endDates[i]=splt[3];
				results[i]=Boolean.parseBoolean(splt[4]);
			}
		}
		length = this.getDataString().getBytes().length;
	}
	
	@Override
	public String getDataString() {
		String data = type.typeCode+":";
		for(int i=0;i<whitePlayers.length;i++) {
			if(i == whitePlayers.length-1)//last entry, don't append #
				data+=whitePlayers[i]+":"+blackPlayers[i]+":"+startDates[i]+":"+endDates[i]+":"+results[i];
			else
				data+=whitePlayers[i]+":"+blackPlayers[i]+":"+startDates[i]+":"+endDates[i]+":"+results[i]+"#";
		}
		return data;
	}

}
