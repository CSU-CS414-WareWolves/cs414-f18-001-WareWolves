package client.gui.swing;

import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.ViewValidMoves;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ChadGameBoard extends JPanel implements MouseListener, MouseMotionListener {

  /**
   * Default positions for the start of the game
   */
  private static final String DEFAULT_GAME_BOARD =
      "rdCreDRiHRjIrcCkdDreERhHKiIRjJrcDrdERhIRiJrcERhJreCRjH";

  /**
   * Size of the squares in pixels
   */
  private int squareSize = 60;
  /**
   * Number of square per row
   */
  private final int NUMBER_OF_SQUARES_PER_ROW = 12;

  /**
   * Panel used for dragging pieces around
   */
  private JLayeredPane layeredPane;
  /**
   * Grid layout of 12*12 squares
   */
  private JPanel chessBoard;
  /**
   * The icon of piece being moved
   */
  private JLabel movingChessPiece;
  /**
   * The x offset for moving the piece by the mouse
   */
  private int xAdjustment;
  /**
   * The y offset for moving the piece by the mouse
   */
  private int yAdjustment;

  /**
   * The square the piece being move is from
   * This is used to reset the piece for invalid moves
   */
  private Container movingPieceStartSquare;

  /**
   * Our client.game.Point for the square the piece being moved
   */
  private client.Point moveFromSquare;
  /**
   * Our client.game.Point for the square the piece being moved goes to
   */
  private client.Point moveToPoint;

  /**
   * All the valid moves for the piece
   */
  private HashSet<JPanel> validPieceMoves = new HashSet<>();

  /**
   * The driver for the game
   */
  private SwingChadDriver appDriver;

  /**
   * Sets up a game with given piece positions
   * @param piecesLocations the piece locations
   */
  public ChadGameBoard(SwingChadDriver swingChadDriver, String piecesLocations){
    this(swingChadDriver);
    setBoardPieces(piecesLocations);
  }

  /**
   * Sets up an empty board
   * call setBoardPieces(String piecesLocations) to add pieces
   */
  public ChadGameBoard(SwingChadDriver swingChadDriver){

    appDriver = swingChadDriver;

    Dimension boardSize = new Dimension(
        NUMBER_OF_SQUARES_PER_ROW * squareSize,
        NUMBER_OF_SQUARES_PER_ROW * squareSize);

    //  Use a Layered Pane for this this application
    layeredPane = new JLayeredPane();
    this.add(layeredPane);
    layeredPane.setPreferredSize(boardSize);

    //Add a chess board to the Layered Pane

    chessBoard = new JPanel();
    layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
    chessBoard.addMouseListener(this);
    chessBoard.addMouseMotionListener(this);
    chessBoard.setLayout( new GridLayout(12, 12) );
    chessBoard.setPreferredSize( boardSize );
    chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

    for (int i = 0; i < 144; i++) {
      JPanel square = GameSquareFactory.getInstance().createdBoardSquare(i, squareSize);
      chessBoard.add(square);
    }

  }


  /**
   * Removes all the prevous peices and sets up the new pieces
   * @param piecesLocations the locations of all the pieces
   */
  public void setBoardPieces(String piecesLocations) {

    // Check if locations is valid
    if(piecesLocations.length() % 3 != 0){
      throw new IllegalArgumentException("ChadGameBoard::setBoardPieces: " + piecesLocations +
          " is not a multiple of 3");
    }

    clearBoardOfPieces();

    // Break the string into 3 char chunks and process info
    while(piecesLocations.length() > 0) {
      String pieceInfo = piecesLocations.substring(0,3);

      JLabel pieceLabel = ChessPieceFactory.getInstance().getPiece( pieceInfo.charAt(0));
      int pieceIndex = convertGamePointToIndex(pieceInfo.substring(1));

      JPanel square = (JPanel)chessBoard.getComponent(pieceIndex);
      square.add(pieceLabel);

      piecesLocations = piecesLocations.substring(3);
    }

    this.revalidate();

  }

  /**
   * Converts client.game.Point's into an index for the grid layout
   * @param location the location in string from
   * @return the index of the location
   */
  private int convertGamePointToIndex(String location) {
    client.Point pieceLocation = new client.Point(location);
    return (144 - ((12 - pieceLocation.getArrayCol())  + pieceLocation.getArrayRow()* 12));
  }

  /**
   * Converts pixel x,y into a java.awt.Point
   * @param colX the x pixel to col
   * @param rowY the y pixel to row
   * @return the point used in game logic
   */
  public java.awt.Point findArrayIndex(int colX, int rowY){
    return new java.awt.Point(rowY/squareSize, Math.abs(colX/squareSize - 11));
  }


  /**
   * Clears the board of all pieces
   */
  private void clearBoardOfPieces() {
    // Each square
    for(Component gameSquare: chessBoard.getComponents()){
      // Find the piece in the square
      for(Component label: ((JPanel) gameSquare).getComponents()){
        // Remove if piece found
        if("Piece".equals(label.getName())){
          ((JPanel) gameSquare).remove(label);
        }
      }
    }
    this.repaint();
  }



  public void mousePressed(MouseEvent e){
    movingChessPiece = null;
    moveFromSquare = null;


    Component c =  chessBoard.findComponentAt(e.getX(), e.getY());

    java.awt.Point arrayIndex = findArrayIndex(e.getY(), e.getX());
    moveFromSquare = new client.Point(arrayIndex.x, arrayIndex.y);

    System.out.println("Array location: " + arrayIndex);
    System.out.println("Game Point location: " + moveFromSquare);

    // Did not click on a piece
    if (c instanceof JPanel){
      return;
    }



    movingPieceStartSquare = c.getParent();

    java.awt.Point parentLocation = c.getParent().getLocation();
    xAdjustment = parentLocation.x - e.getX();
    yAdjustment = parentLocation.y - e.getY();
    movingChessPiece = (JLabel)c;
    movingChessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
    movingChessPiece.setSize(movingChessPiece.getWidth(), movingChessPiece.getHeight());
    layeredPane.add(movingChessPiece, JLayeredPane.DRAG_LAYER);

    appDriver.handleViewMessage(new ViewValidMoves(moveFromSquare));

  }




  //Move the chess piece around

  public void mouseDragged(MouseEvent me) {
    if (movingChessPiece == null) return;
    movingChessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
  }

  //Drop the chess piece back onto the chess board

  public void mouseReleased(MouseEvent e) {
    // Not moving a piece
    if(movingChessPiece == null){return;}

    movingChessPiece.setVisible(false);


    setAllValidMoves(false);

    java.awt.Point arrayIndex = findArrayIndex(e.getY(), e.getX());
    moveToPoint = new client.Point(arrayIndex.x, arrayIndex.y);

    Component c =  chessBoard.findComponentAt(e.getX(), e.getY());

    // If player moved piece out of bounds
    if(c == null){
      resetMove();
      return;
    }

    Container moveToSquare;
    // If moving on to another piece
    if (c instanceof JLabel){
      moveToSquare = c.getParent();
    }
    else {
      moveToSquare = (Container)c;
    }

    if(!validPieceMoves.contains(moveToSquare)){
      resetMove();
      return;
    }

    //Logic for getting move

    appDriver.handleViewMessage(new MovePieceMessage(moveFromSquare, moveToPoint));

    /*
    moveToSquare.remove(c); //only for none working
    moveToSquare.add(movingChessPiece);
    */

    // Remove the icon from the drag layer
    layeredPane.remove(movingChessPiece);

  }

  private void resetMove() {
    movingPieceStartSquare.add(movingChessPiece);
    movingChessPiece.setVisible(true);
  }

  public void setValidMoves(String validMoves, boolean state) {

    if (validMoves.length() % 2 != 0) {
      throw new IllegalArgumentException("ChadGameBoard::setValidMoves: " + validMoves +
          " is not a multiple of 2");
    }

    validPieceMoves.clear();

    while (validMoves.length() > 0) {
      String validMoveInfo = validMoves.substring(0, 2);
      int squareIndex = convertGamePointToIndex(validMoveInfo);

      JPanel square = (JPanel) chessBoard.getComponent(squareIndex);
      setValidMove(square, state);
      validPieceMoves.add(square);

      validMoves = validMoves.substring(2);

    }
  }

  private void setAllValidMoves(boolean state) {
    for(Component gameSquare: chessBoard.getComponents()){
      setValidMove(gameSquare, state);
    }
  }

  private void setValidMove(Component gameSquare, boolean state) {
    if(!(gameSquare instanceof JPanel)){
      throw new IllegalArgumentException("Trying to convert " + gameSquare.getClass() + " to JPanel");
    }

    for(Component labels: ((JPanel) gameSquare).getComponents()){
      if("ValidMove".equals(labels.getName())){
        labels.setVisible(state);
      }
    }
  }

  public void displayGameOverMessage(String message){

  }


  // MouseListener, MouseMotionListener methods implemented but not used

  public void mouseClicked(MouseEvent e) {
    // Only care about press and hold
  }
  public void mouseMoved(MouseEvent e) {
    // Only care about dragging

  }
  public void mouseEntered(MouseEvent e){
    // Do nothing
  }
  public void mouseExited(MouseEvent e) {
    // Do nothing
  }





}
