//import java.swing.*; lsa hshof hn7tagha wla la 
import javax.swing.JFrame;

public class Test {

    public static void main(String[] args) throws Exception {
       int tileSize = 32 ;
       int rows = 16; 
       int columns = 16;
       int boardWidth = tileSize * columns; //al window bta3na hyb2a 512px
       int boardHeight = tileSize * rows; // m3mlna4 = 512px 3la tol 34an lw 3yzen n8er b3deen

       JFrame frame = new JFrame("FCI's Space Invaders");
       frame.setSize(boardWidth, boardHeight);
       frame.setVisible(true);
       frame.setLocationRelativeTo(null);
       frame.setResizable(false); // momken n8erha btw
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //7:00 on our video

        //instance of the JPanel file
        SpaceInvaders spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders); //add the panel to our frame الحاجات دي سهله بس عشان محدش يتلغبط
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);
     

    }
}
