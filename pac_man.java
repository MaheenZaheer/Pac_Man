import javax.swing.JFrame;
public class pac_man {
    public static void main(String[] args){
        int rowCount=21;
        int columnCount=19;
        int tileSize=32;
        int BoardWidth=columnCount*tileSize;
        int BoardHeight=rowCount*tileSize;

        JFrame frame = new JFrame("Pac Man");//title
        frame.setSize(BoardWidth,BoardHeight);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); //at centre
        frame.setResizable(false);// so user cannot expand the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when user click on x
        Map pacmanGame = new Map();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus(); //imp
        frame.setVisible(true);
    }

}


