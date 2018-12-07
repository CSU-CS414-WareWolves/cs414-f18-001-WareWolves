package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels.chadgame;

import edu.colostate.cs.cs414.warewolves.chad.client.gui.ChadGameDriver;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.MovePieceMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewValidMoves;
import edu.colostate.cs.cs414.warewolves.chad.client.Point;
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
  private Point moveFromSquare;
  /**
   * Our client.game.Point for the square the piece being moved goes to
   */
  private Point moveToPoint;

  /**
   * All the valid moves for the piece
   */
  private HashSet<JPanel> validPieceMoves = new HashSet<>();

  /**
   * The driver for the game
   */
  private ChadGameDriver appDriver;

  /**
   * Sets up a game with given piece positions
   * @param piecesLocations the piece locations
   */
  public ChadGameBoard(ChadGameDriver swingChadDriver, String piecesLocations){
    this(swingChadDriver);
    setBoardPieces(piecesLocations);
  }

  /**
   * Sets up an empty board
   * call setBoardPieces(String piecesLocations) to add pieces
   */
  public ChadGameBoard(ChadGameDriver swingChadDriver){

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

    chessBoard.revalidate();
    this.repaint();

  }

  /**
   * Converts client.game.Point's into an index for the grid layout
   * @param location the location in string from
   * @return the index of the location
   */
  private int convertGamePointToIndex(String location) {
    Point pieceLocation = new Point(location);
    return (144 - ((12 - pieceLocation.getArrayCol())  + pieceLocation.getArrayRow()* 12));
  }

  /**
   * Converts pixel x,y into a java.awt.Point
   * @param colX the x pixel to col
   * @param rowY the y pixel to row
   * @return the point used in game logic
   */
  private java.awt.Point findArrayIndex(int colX, int rowY){
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
  }

  /**
   * When the user press the mouse button, see if they selected a piece and get the pieces valid move
   * @param e info about the mouse location
   */
  public void mousePressed(MouseEvent e){

    if(e.getButton() != 1) {return;}

    // Debug Mouse location
    System.out.println("x:" + e.getX() +" y:" +e.getY());
    // Clear previous moves info
    movingChessPiece = null;
    moveFromSquare = null;

    // Convert location to our index method
    Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
    java.awt.Point arrayIndex = findArrayIndex(e.getY(), e.getX());
    moveFromSquare = new Point(arrayIndex.x, arrayIndex.y);
    // Debug piece location
    System.out.println("Array location: " + arrayIndex);
    System.out.println("Game Point location: " + moveFromSquare);

    // Did not click on a piece
    if (c instanceof JPanel){
      System.out.println("NO PIECE");
      return;
    }
    // The square the piece started out
    movingPieceStartSquare = c.getParent();

    // Offsets for moving the piece with the mouse
    java.awt.Point parentLocation = c.getParent().getLocation();
    xAdjustment = parentLocation.x - e.getX();
    yAdjustment = parentLocation.y - e.getY();

    // Attach the piece to mouse
    movingChessPiece = (JLabel)c;
    movingChessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
    movingChessPiece.setSize(movingChessPiece.getWidth(), movingChessPiece.getHeight());
    // Move the piece the drag layer for visibility
    layeredPane.add(movingChessPiece, JLayeredPane.DRAG_LAYER);
    // Ask for valid moves for the piece from the Driver
    appDriver.handleViewMessage(new ViewValidMoves(moveFromSquare));
  }

  /**
   * Moves the pieces Label to the mouses location
   * @param me info about the mouse location
   */
  public void mouseDragged(MouseEvent me) {
    if (movingChessPiece == null) return;
    movingChessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
  }

  /**
   * Finds were the piece dropped and sent the move to the Driver if it was in the list
   * of valid moves
   * @param e info about the mouse location
   */
  public void mouseReleased(MouseEvent e) {
    // Not moving a piece
    if(movingChessPiece == null){return;}
    if(e.getButton() != 1) {return;}

    movingChessPiece.setVisible(false); // stop sudden jumps in the piece location

    setAllValidMoves(false);

    // Convert location to our index method
    java.awt.Point arrayIndex = findArrayIndex(e.getY(), e.getX());
    moveToPoint = new Point(arrayIndex.x, arrayIndex.y);
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

    // If the player tried to make an invalid move
    if(!validPieceMoves.contains(moveToSquare)){
      resetMove();
      return;
    }

    //Logic for getting move
    validPieceMoves.clear();

    // Send the move to the Diver
    appDriver.handleViewMessage(new MovePieceMessage(moveFromSquare, moveToPoint));


    // Remove the icon from the drag layer
    layeredPane.remove(movingChessPiece);

  }

  /**
   * Moves a piece back to its starting square
   */
  private void resetMove() {
    movingPieceStartSquare.add(movingChessPiece);
    movingChessPiece.setVisible(true);
  }

  public void setValidMoves(String validMoves, boolean state) {
    // Check if validMoves is in the correct format
    if (validMoves.length() % 2 != 0) {
      throw new IllegalArgumentException("ChadGameBoard::setValidMoves: " + validMoves +
          " is not a multiple of 2");
    }

    validPieceMoves.clear(); // remove all previous pieces
    while (validMoves.length() > 0) {
      String validMoveInfo = validMoves.substring(0, 2); // break the string into the board locations
      int squareIndex = convertGamePointToIndex(validMoveInfo);

      JPanel square = (JPanel) chessBoard.getComponent(squareIndex);
      setValidMove(square, state);
      validPieceMoves.add(square); // save valid move squares for logic checks in mouseRelease()
      validMoves = validMoves.substring(2); // remove the current squares info

    }
  }

  /**
   * Sets all the valid move Jlabels to visible or not visible
   * @param state visible or not visible
   */
  public void setAllValidMoves(boolean state) {
    for(Component gameSquare: chessBoard.getComponents()){
      setValidMove(gameSquare, state);
    }
  }

  /**
   * Sets a squares valid move to visible or not visible
   * @param gameSquare the square to modify the valid moves visibility
   * @param state visible or not visible
   */
  private void setValidMove(Component gameSquare, boolean state) {
    if(!(gameSquare instanceof JPanel)){
      throw new IllegalArgumentException("Trying to convert " + gameSquare.getClass() + " to JPanel");
    }
    // Find the validMove JLabel
    for(Component labels: ((JPanel) gameSquare).getComponents()){
      if("ValidMove".equals(labels.getName())){
        labels.setVisible(state);
      }
    }
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
