package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

import client.presenter.network.messages.*;


public class Query {
	private final String driver = "com.mysql.jdbc.Driver";
	private final String theURL = "jdbc:mysql://129.82.45.59:3306/warewolves";
	private final String user = "bcgood";
	private final String pass = "830271534";
	private static final String startingBoard = "rcCrcDrcErdCkdDrdEreCreDreERhHRhIRhJRiHKiIRiJRjHRjIRjJ";
	
	//TODO: Move, end game
	
	/**
	 * 
	 * @param username The player's nickname who is attempting to log in
	 * @param hashedPass The hash of the password attempt
	 * @return True if successful login, false if failed due to nickname or password failure
	 */
	public LoginResponse loginCheck(Login msg) {
		LoginResponse ret = new LoginResponse(false, "undefined");
		try	{//Connect to DB 
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {//Check if username and hashedPass match
					String query = "SELECT * FROM users where email = '"+ msg.email +"' AND hashedPass = '"+msg.passwordAttempt+"';";       
					ResultSet rs = st.executeQuery(query);
					try{//check if rs is empty
						if(rs.first())
							ret = new LoginResponse(true, rs.getString("nickname"));
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * Checks if email is registered in the system
	 * @param email String of the email read from a register request message
	 * @return True if email is taken, false if it is not taken.
	 */
	private boolean emailCheck(String email) {
		boolean ret = true;
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {
					String query = "SELECT * FROM users WHERE email = '"+email+"';";       
					ResultSet rs = st.executeQuery(query);
					try{//Check if rs is empty
						ret = rs.first();
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * Checks if nickname is registered in the system
	 * @param nickname String of the nickname read from a register request message
	 * @return true if nickname is taken, false if it is not taken.
	 */
	private boolean nicknameCheck(String nickname) {
		boolean ret = true;
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {
					String query = "SELECT * FROM users WHERE nickname = '"+nickname+"';";       
					ResultSet rs = st.executeQuery(query);
					try{//Check if rs is empty
						ret = rs.first();
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * Insert query for registering a user
	 * @param msg Register message that has passed checks
	 */
	private void registerInsert(Register msg) {
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {
					String query = "INSERT INTO users (nickname, email, hashedPass) VALUES ('"+msg.nickname+"', '"+msg.email+"', '"+msg.password+"');";       
					st.executeQuery(query);
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Handle registration of users, checks if email or nickname are taken and responds on status of registration.
	 * @param msg A Register message with all the appropriate fields filled
	 * @return A RegisterResponse with the fields set based on success or failure of registering
	 */
	public RegisterResponse register(Register msg) {
		RegisterResponse ret;
		if(emailCheck(msg.email))//Email taken
			ret = new RegisterResponse(false, false);
		else if(nicknameCheck(msg.nickname))//Nickname taken
			ret = new RegisterResponse(false, true);
		else {
			ret = new RegisterResponse(true, false);
			registerInsert(msg);
		}
		return ret;
	}
	
	/**
	 * Check is all fields of an unregister message match an actual user
	 * @param msg the Unregister message
	 * @return true if the nickname, email, AND hashedPass match an actual user; false if otherwise
	 */
	private boolean unregisterCheck(Unregister msg) {
		boolean ret = false;
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {
					String query = "SELECT * FROM users WHERE nickname = '"+msg.nickname+"' AND email = '"+msg.email+"' AND hashedPass = '"+msg.password+"';";       
					ResultSet rs = st.executeQuery(query);
					try{//Check if rs is empty
						ret = rs.first();
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * Unregisters a User if all fields of a users profile match their request.
	 * @param msg Unregister message with fields passed from the server;
	 */
	public void unregister(Unregister msg) {
		if(unregisterCheck(msg)) {
			try	{//Connect to DB 
				Class.forName(driver); 
				Connection conn = DriverManager.getConnection(theURL, user, pass);	
				try{
					Statement st = conn.createStatement();
					try {
						String query = "DELETE FROM users WHERE nickname = '"+msg.nickname+"';";       
						st.executeQuery(query);
					} finally { st.close(); }
				} finally { conn.close(); }
			} catch (Exception e) {
				System.err.printf("Exception: ");
				System.err.println(e.getMessage());
			}
		}	
	}
	
	/**
	 * Gather Game
	 * @param gameID Game ID from the GameRequest message
	 * @return GameInfo message to send back to client how requested it.
	 */
	public GameInfo getGame(int gameID) {
		GameInfo ret = new GameInfo(gameID, "", false);
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {
					String query = "SELECT * FROM activeGames WHERE gameID = "+gameID+";";       
					ResultSet rs = st.executeQuery(query);
					try{//Check if rs is empty
						if(rs.first())
							ret = new GameInfo(gameID, rs.getString("board"), rs.getBoolean("turn"));
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * Adds Invite into the Invites table after checking that sender and recipient nicknames exist
	 * @param inv InviteRequest message passed from the server
	 */
	public void addInvite(InviteRequest inv) {
		if(nicknameCheck(inv.sender) && nicknameCheck(inv.recipient)) {//Check sender/recipient nicknames exist
			try	{//Connect to DB 
				Class.forName(driver); 
				Connection conn = DriverManager.getConnection(theURL, user, pass);	
				try{
					Statement st = conn.createStatement();
					try {
						String query = "INSERT INTO invites (sender, recipient, sendDate) VALUES ('"+inv.sender+"', '"+inv.recipient+"', CURDATE());";       
						st.executeQuery(query);
					} finally { st.close(); }
				} finally { conn.close(); }
			} catch (Exception e) {
				System.err.printf("Exception: ");
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Updates an invites status based on the inviteID
	 * @param inv An InviteResponse message passed from the server
	 */
	public void updateInvite(InviteResponse inv) {
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {
					String query = "UPDATE invites SET status = "+inv.status+" WHERE inviteID="+inv.inviteID+";";       
					st.executeQuery(query);
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		if(inv.status)
			makeNewGame(inv.inviteID);
	}
	
	/**
	 * Creates new game after an invite is accepted
	 * @param inviteID the Accepted invite's ID
	 */
	private void makeNewGame(int inviteID) {
		String white = "", black = "";
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {//Grab nicknames from invite record
					String query = "SELECT * FROM invites WHERE inviteID = "+inviteID+";";       
					ResultSet rs = st.executeQuery(query);
					try{//check if rs is empty
						if(rs.first()) {
							white = rs.getString("sender");
							black = rs.getString("recipient");
						}
					} finally { rs.close(); }
					query = "INSERT INTO activeGames (whitePlayer, blackPlayer, board, turn, startDate, ended) VALUES"+
							"('"+white+"', '"+black+"', '"+startingBoard+"', false, CURDATE(), false);";
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Fetches all invites without a response sent to or by the given player's nickname.
	 * @param nickname Nickname of player's inbox to recieve
	 * @return The given nickname's inbox in an InboxResponse.
	 */
	public InboxResponse getInbox(String nickname) {
		InboxResponse ret = new InboxResponse(new int[] {0}, new String[] {"-1"}, new String[] {"-1"},new String[] {"-1"});
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {//Check if username and hashedPass match
					String query = "SELECT * FROM invites WHERE (sender = '"+ nickname +"' OR recipient = '"+nickname+"') AND status IS NULL;";       
					ResultSet rs = st.executeQuery(query);
					try{//check if rs is empty
						if(rs.first()) {
							ArrayList<Integer> ids = new ArrayList<Integer>();
							ArrayList<String> senders = new ArrayList<String>();
							ArrayList<String> recipients = new ArrayList<String>();
							ArrayList<String> sendDates = new ArrayList<String>();
							ids.add(rs.getInt("inviteID"));
							senders.add(rs.getString("sender"));
							recipients.add(rs.getString("recipients"));
							sendDates.add(rs.getString("sendDate"));
							while(rs.next()) {
								ids.add(rs.getInt("inviteID"));
								senders.add(rs.getString("sender"));
								recipients.add(rs.getString("recipients"));
								sendDates.add(rs.getString("sendDate"));
							}
							String[] Senders = new String[senders.size()];
							String[] Recipients = new String[recipients.size()];
							String[] SendDates = new String[sendDates.size()];
							int[] IDs = new int[ids.size()];
							for(int i=0;i<IDs.length;i++)
								IDs[i]=ids.get(i).intValue();
							ret = new InboxResponse(IDs, senders.toArray(Senders), recipients.toArray(Recipients), sendDates.toArray(SendDates));
						}
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * Checks what color a player is in a given game
	 * @param gameID ID of game
	 * @param nickname nickanem for player check
	 * @return true if player is white, false if player is black
	 */
	private boolean whiteOrBlack(int gameID, String nickname) {
		boolean ret = false;
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {
					String query = "SELECT * FROM activeGames WHERE gameID = "+gameID+";";       
					ResultSet rs = st.executeQuery(query);
					try{//Check if rs is empty
						rs.first();
						String white = rs.getString("whitePlayer");
						if(nickname.equalsIgnoreCase(white))
							ret = true;
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * Handle resign message
	 * @param msg Resign from player in message form
	 */
	public void resign(Resign msg) {
		if(whiteOrBlack(msg.gameID, msg.nickname)) {//white resigned
			try	{//Connect to DB 
				Class.forName(driver); 
				Connection conn = DriverManager.getConnection(theURL, user, pass);	
				try{
					Statement st = conn.createStatement();
					try {
						String query = "UPDATE activeGames SET ended=true, winner=true, whiteSeeResults=true WHERE gameID="+msg.gameID+";";       
						st.executeQuery(query);
					} finally { st.close(); }
				} finally { conn.close(); }
			} catch (Exception e) {
				System.err.printf("Exception: ");
				System.err.println(e.getMessage());
			}
		}
		else {//black resigned
			try	{//Connect to DB 
				Class.forName(driver); 
				Connection conn = DriverManager.getConnection(theURL, user, pass);	
				try{
					Statement st = conn.createStatement();
					try {
						String query = "UPDATE activeGames SET ended=true, winner=false, blackSeeResults=true WHERE gameID="+msg.gameID+";";       
						st.executeQuery(query);
					} finally { st.close(); }
				} finally { conn.close(); }
			} catch (Exception e) {
				System.err.printf("Exception: ");
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Fetches info about a profile from given nickname
	 * @param nickname Nickname of the player to fetch profile information about
	 * @return ProfileResponse message holding profile data about player with passed nickname.
	 */
	public ProfileResponse getProfile(String nickname) {
		ProfileResponse ret = new ProfileResponse(new String[] {"-1"}, new String[] {"-1"}, new String[] {"-1"}, new String[] {"-1"}, new boolean[] {false});
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {//Check if username and hashedPass match
					String query = "SELECT * FROM pastGames WHERE (whitePlayer = '"+ nickname +"' OR blackPlayer = '"+nickname+"');";       
					ResultSet rs = st.executeQuery(query);
					try{//check if rs is empty
						if(rs.first()) {
							ArrayList<String> whitePlayers = new ArrayList<String>();
							ArrayList<String> blackPlayers = new ArrayList<String>();
							ArrayList<String> startDates = new ArrayList<String>();
							ArrayList<String> endDates = new ArrayList<String>();
							ArrayList<Boolean> results = new ArrayList<Boolean>();
							whitePlayers.add(rs.getString("whitePlayer"));
							blackPlayers.add(rs.getString("blackPlayer"));
							startDates.add(rs.getString("startDate"));
							endDates.add(rs.getString("endDate"));
							results.add(rs.getBoolean("winner"));
							while(rs.next()) {
								whitePlayers.add(rs.getString("whitePlayer"));
								blackPlayers.add(rs.getString("blackPlayer"));
								startDates.add(rs.getString("startDate"));
								endDates.add(rs.getString("endDate"));
								results.add(rs.getBoolean("winner"));
							}
							String[] whitePlayer = new String[whitePlayers.size()];
							String[] blackPlayer = new String[blackPlayers.size()];
							String[] startDate = new String[startDates.size()];
							String[] endDate = new String[endDates.size()];
							boolean[] result = new boolean[results.size()];
							for(int i=0;i<result.length;i++)
								result[i]=results.get(i).booleanValue();
							ret = new ProfileResponse(whitePlayers.toArray(whitePlayer), blackPlayers.toArray(blackPlayer), startDates.toArray(startDate),
									endDates.toArray(endDate), result);
						}
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * Fetches nicknames of all registered players.
	 * @return A Players message holding the nicknames of all registered players
	 */
	public Players getPlayers() {
		Players ret = new Players(new String[] {"-1"});
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {
					String query = "SELECT nickname FROM users;";       
					ResultSet rs = st.executeQuery(query);
					try{
						ArrayList<String> nicknames = new ArrayList<String>();
						while(rs.next())
							nicknames.add(rs.getString("nickname"));
						String[] names = new String[nicknames.size()];
						ret = new Players(names);
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		return ret;
	}
	
	//TODO: add ended?
	
	public ActiveGameResponse getActiveGames(String nickname) {
		ActiveGameResponse ret = new ActiveGameResponse(new int[] {-1}, new String[] {"-1"}, new String[] {"-1"}, new String[] {"-1"}, new boolean[] {false}, new boolean[] {false});
		ArrayList<Integer> gameIDs = new ArrayList<Integer>();
		ArrayList<String> boards = new ArrayList<String>();
		ArrayList<String> opponents = new ArrayList<String>();
		ArrayList<String> startDates = new ArrayList<String>();
		ArrayList<Boolean> turns = new ArrayList<Boolean>();
		ArrayList<Boolean> colors = new ArrayList<Boolean>();
		try	{//Connect to DB 
			Class.forName(driver); 
			Connection conn = DriverManager.getConnection(theURL, user, pass);	
			try{
				Statement st = conn.createStatement();
				try {//Check if username and hashedPass match
					String query = "SELECT * FROM activeGames WHERE whitePlayer = '"+ nickname +"';";       
					ResultSet rs = st.executeQuery(query);
					try{//check if rs is empty
						if(rs.first()) {
							gameIDs.add(rs.getInt("gameID"));
							boards.add(rs.getString("board"));
							opponents.add(rs.getString("blackPlayer"));
							startDates.add(rs.getString("startDate"));
							turns.add(rs.getBoolean("turn"));
							while(rs.next()) {
								gameIDs.add(rs.getInt("gameID"));
								boards.add(rs.getString("board"));
								opponents.add(rs.getString("blackPlayer"));
								startDates.add(rs.getString("startDate"));
								turns.add(rs.getBoolean("turn"));
								colors.add(false);
							}
						}
					} finally { rs.close(); }
					query = "SELECT * FROM activeGames WHERE blackPlayer = '"+ nickname +"';";
					rs = st.executeQuery(query);
					try{//check if rs is empty
						if(rs.first()) {
							gameIDs.add(rs.getInt("gameID"));
							boards.add(rs.getString("board"));
							opponents.add(rs.getString("whitePlayer"));
							startDates.add(rs.getString("startDate"));
							turns.add(rs.getBoolean("turn"));
							while(rs.next()) {
								gameIDs.add(rs.getInt("gameID"));
								boards.add(rs.getString("board"));
								opponents.add(rs.getString("blackPlayer"));
								startDates.add(rs.getString("startDate"));
								turns.add(rs.getBoolean("turn"));
								colors.add(true);
							}
						}
					} finally { rs.close(); }
				} finally { st.close(); }
			} finally { conn.close(); }
		} catch (Exception e) {
			System.err.printf("Exception: ");
			System.err.println(e.getMessage());
		}
		int[] ids = new int[gameIDs.size()];
		String[] board = new String[boards.size()];
		String[] opponent = new String[opponents.size()];
		String[] dates = new String[startDates.size()];
		boolean[] turn = new boolean[turns.size()];
		boolean[] color = new boolean[colors.size()];
		for(int i=0;i<ids.length;i++) {
			ids[i] = gameIDs.get(i).intValue();
			turn[i] = turns.get(i).booleanValue();
			color[i] = colors.get(i).booleanValue();
		}
		ret = new ActiveGameResponse(ids, boards.toArray(board), opponents.toArray(opponent), startDates.toArray(dates), turn, color);
		return ret;
	}
	
	
	public static void main(String[] args) {
		Query q = new Query();
		Login test = new Login("testUser@example.com", "123");
		q.loginCheck(test);
	}
	
}
