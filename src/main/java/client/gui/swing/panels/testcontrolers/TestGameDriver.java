package client.gui.swing.panels.testcontrolers;

import client.game.Game;
import client.gui.ChadGameDriver;
import client.gui.swing.info.ActiveGameInfo;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.MovePieceResponse;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.RegisterResponseMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import client.presenter.controller.messages.ViewValidMovesResponse;
import client.presenter.network.messages.ActiveGameResponse;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.LoginResponse;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.Players;
import client.presenter.network.messages.ProfileResponse;

public class TestGameDriver implements ChadGameDriver {

  private ChadGameDriver gui;
  private Game game;
  private boolean playerColor;
  private ActiveGameInfo currentGame;

  @Override
  public void handleViewMessage(ViewMessage message) {
    switch (message.messageType) {

      case REGISTER:
        RegisterMessage registerMessage = (RegisterMessage) message;
        System.out.println(registerMessage.messageType);
        if(registerMessage.email.equals("bad")){
          gui.handleViewMessage(new RegisterResponseMessage(false, new String[] {"The email has already registered account"}));
        } else if(registerMessage.nickname.equals("bad")){
          gui.handleViewMessage(new RegisterResponseMessage(false, new String[] {"The nickname has already been taken"}));
        } else {
          gui.handleViewMessage(new RegisterResponseMessage(true, new String[] {"testUser"})); // Need to save nickname or login
          gui.handleNetMessage(new Players("19:testUser:testUser2:testUser3"));
        }

        break;
      case LOGIN:
        LoginMessage login = (LoginMessage) message;
        System.out.println(login.messageType);
        if(login.email.equals("bad")){
          gui.handleViewMessage(new LoginResponseMessage(false, "testUser"));
        } else {
          gui.handleViewMessage(new LoginResponseMessage(true, "testUser"));
          gui.handleNetMessage(new Players("19:testUser:testUser2:testUser3"));
        }
        break;
      case UNREGISTER:
        break;
      case SHOW_VALID_MOVES:
        // if the game is over no valid moves
        if(game.gameover()){return;}
        if(game.getTurn() != playerColor){return;}
        // Find the valid moves
        ViewValidMoves validMovesMessage = (ViewValidMoves) message;
        String validMoves = game.validMoves(validMovesMessage.location.toString());
        // Tell GUI to what moves to show
        gui.handleViewMessage(new ViewValidMovesResponse(new String[] {validMoves}));
        break;
      case MENU:
        handleMenuMessage((MenuMessage) message);
        break;
      case MOVE_PIECE:
        MovePieceMessage moves = (MovePieceMessage) message;
        if(game.getTurn() != playerColor){
          gui.handleViewMessage(new MovePieceResponse(false, game.getBoard()));
          return;}
        game.move(moves.fromLocation.toString(), moves.toLocation.toString());
        gui.handleViewMessage(new MovePieceResponse(true, game.getBoard()));

        break;
      case REGISTER_RESPONSE:
        break;
      case LOGIN_RESPONSE:
        break;
      case UNREGISTER_RESPONSE:
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        break;
      case MENU_RESPONSE:
        break;
      case MOVE_PIECE_RESPONSE:
        break;
    }
  }

  private void handleMenuMessage(MenuMessage message) {

    switch (message.menuType){

      case LOGOUT:
        break;
      case PLAYER_STATS:
        System.out.println(message.menuType + " " + message.information[0]);
        gui.handleNetMessage(new ProfileResponse("18:testUser2:testUser:01-01-18:01-01-18:true#testUser:testUser2:02-14-18:02-14-18:false" ));
        break;
      case ACTIVE_GAMES:
        System.out.println(message.menuType);
        gui.handleNetMessage( new ActiveGameResponse("10:123:RiIrdDKjJkeEQaAqlL:testUser:01-01-18:true:false:false#1234:RiIrdDKjJkeEQaAqlL:testUser2:02-14-18:false:false:false"));
        break;
      case INVITES:
        if(message.information.length == 0){
          System.out.println(message.menuType + " inbox");
        } else {
          System.out.println(message.menuType + " invite");
        }
        gui.handleNetMessage(new InboxResponse("24:123:Mac:testUser:01-01-18#1234:testUser:Charlie:02-14-18"));
        break;
      case SELECT_GAME:
        currentGame = new ActiveGameInfo(message.information);
        game =  new Game(currentGame.getGameBoard(), currentGame.getTurn());
        playerColor = currentGame.getColor();
        gui.handleViewMessage(new MovePieceResponse(true, game.getBoard()));
        break;
      case SEND_INVITE:
        System.out.println(message.menuType);
        gui.handleNetMessage(new InboxResponse("16:123:Mac:testUser:01-01-18#1234:testUser:Mac:02-14-18"));
        break;
      case RESIGN:
        System.out.println(message.menuType);
        gui.handleNetMessage( new ActiveGameResponse("10:123:RiIrdDKjJkeEQaAqlL:testUser:01-01-18:true:false:false#1234:RiIrdDKjJkeEQaAqlL:testUser2:02-14-18:false:false:false"));

        break;
    }

  }

  @Override
  public void handleNetMessage(NetworkMessage message) {

  }

  @Override
  public void createAndShowGUI() {

  }

  public void setGui(ChadGameDriver gui) {
    this.gui = gui;
  }
}
