package client.presenter.controller;

/**
 * These are the types of message that the View can send and receive
 */
public enum ViewMessageType {
  REGISTER,
  LOGIN,
  UNREGISTER,
  LOGOUT,
  SHOW_VALID_MOVES,
  MENU,
  SELECT_GAME,
  MOVE_PIECE,
  REGISTER_RESPONSE,
  LOGIN_RESPONSE,
  UNREGISTER_RESPONSE,
  LOGOUT_RESPONSE,
  SHOW_VALID_MOVES_RESPONSE,
  MENU_RESPONSE,
  SELECT_GAME_RESPONSE,
  MOVE_PIECE_RESPONSE;
}