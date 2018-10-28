package client.gui.swing;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ChadGameBoard extends JPanel implements MouseListener, MouseMotionListener {

  private static final String DEFAULT_GAME_BOARD =
      "rdCreDrcCkdDreErcDrdErcEreCRiHRjIRhHKiIRjJRhIRiJRhJRjH";

  private int squareSize = 60;
  private final int NUMBER_OF_SQUARES_PER_ROW = 12;
  private JLayeredPane layeredPane;
  private JPanel chessBoard;
  private JLabel movingChessPiece;
  private int xAdjustment;
  private int yAdjustment;
  private Container pieceStartSquare;
  private client.game.Point moveFromSquare;
  private client.game.Point moveToPoint;
  private HashSet<JPanel> validPieceMoves = new HashSet<>();

  public ChadGameBoard(String piecesLocations){
    this();
    setBoardPieces(piecesLocations);
  }

  public ChadGameBoard(){
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

      piecesLocations = piecesLocations.substring(3, piecesLocations.length());
    }

    this.revalidate();

  }

  /**
   * Converts client.game.Point's into an index for the grid layout
   * @param location the location in string from
   * @return the index of the location
   */
  private int convertGamePointToIndex(String location) {
    client.game.Point pieceLocation = new client.game.Point(location);
    return pieceLocation.getArrayCol() + pieceLocation.getArrayRow()*12;
  }

  /**
   * Converts pixel x,y into a java.awt.Point
   * @param colX the x pixel to col
   * @param rowY the y pixel to row
   * @return the point used in game logic
   */
  public java.awt.Point findArrayIndex(int colX, int rowY){
    return new java.awt.Point(colX/squareSize, rowY/squareSize);
  }


  /**
   * Clears the board of all pieces
   */
  private void clearBoardOfPieces() {
    // Each square
    for(Component gameSquare: chessBoard.getComponents()){
      // Find the Piece in the square
      for(Component label: ((JPanel) gameSquare).getComponents()){
        // Remove if piece found
        if("Piece".equals(label.getName())){
          ((JPanel) gameSquare).remove(label);
        }
      }
    }
  }



  public void mousePressed(MouseEvent e){
    movingChessPiece = null;
    moveFromSquare = null;


    Component c =  chessBoard.findComponentAt(e.getX(), e.getY());

    java.awt.Point arrayIndex = findArrayIndex(e.getY(), e.getX());
    moveFromSquare = new client.game.Point(arrayIndex.x, arrayIndex.y);

    System.out.println("Array location: " + arrayIndex);
    System.out.println("Game Point location: " + moveFromSquare);

    // Did not click on a piece
    if (c instanceof JPanel){
      return;
    }

    setValidMoves("dCeDcCdDeEcDdEcEeC", true);


    pieceStartSquare = c.getParent();

    java.awt.Point parentLocation = c.getParent().getLocation();
    xAdjustment = parentLocation.x - e.getX();
    yAdjustment = parentLocation.y - e.getY();
    movingChessPiece = (JLabel)c;
    movingChessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
    movingChessPiece.setSize(movingChessPiece.getWidth(), movingChessPiece.getHeight());
    layeredPane.add(movingChessPiece, JLayeredPane.DRAG_LAYER);

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
    moveToPoint = new client.game.Point(arrayIndex.x, arrayIndex.y);

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

    moveToSquare.remove(c); //only for none working
    moveToSquare.add(movingChessPiece);

    movingChessPiece.setVisible(true);
  }

  private void resetMove() {
    pieceStartSquare.add(movingChessPiece);
    movingChessPiece.setVisible(true);
  }

  private void setValidMoves(String validMoves, boolean state) {

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

      validMoves = validMoves.substring(2, validMoves.length());

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




  public void mouseClicked(MouseEvent e) {

  }
  public void mouseMoved(MouseEvent e) {
  }
  public void mouseEntered(MouseEvent e){

  }
  public void mouseExited(MouseEvent e) {

  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.getContentPane().add(new ChadGameBoard(DEFAULT_GAME_BOARD));
    frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
    frame.pack();
    frame.setResizable(false);
    frame.setLocationRelativeTo( null );
    frame.setVisible(true);
  }



}
