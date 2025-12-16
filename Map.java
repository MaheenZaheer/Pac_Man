import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;


public class Map extends JPanel implements ActionListener, KeyListener {
    public void move(){
        pacman.x +=pacman.velocityX;
        pacman.y +=pacman.velocityY;
        //check wall collision
        for (Block wall : walls){
            if (collision(pacman,wall)){
                pacman.x -=pacman.velocityX;
                pacman.y -=pacman.velocityY;
                break;
            }

        }
        //check ghost collisions
        for (Block ghost : ghosts) {
            if (collision(ghost,pacman)){
                livess-=1;
                if (livess==0){
                    gameOver = true;
                    return;
                }
                resetPositions();
            }
            if (ghost.y==tileSize*9 && ghost.direction!= 'U' && ghost.direction != 'D'){
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls){
                if (collision(ghost,wall) || ghost.x<=0 || ghost.x + ghost.width >= BoardWidth){
                    ghost.x -=ghost.velocityX;
                    ghost.y -=ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    break;
                }
            }
        }
        // check food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)){
                foodEaten = food;
                score +=10;
            }
        }
        foods.remove(foodEaten);


    }
    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&   // missing!
                a.y + a.height > b.y;

    }
    public void resetPositions(){
        pacman.reset();
        pacman.velocityX=0;
        pacman.velocityY=0;
        for (Block ghost : ghosts){
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { //all the letters except arrow keys
        System.out.println();
    }

    @Override
    public void keyPressed(KeyEvent e) { //all the keys include arrow keys

    }

    @Override
    public void keyReleased(KeyEvent e) { //when we hold on a key
//        System.out.println("keyEvent: " + e.getKeyCode());
    if (gameOver){
        loadMap();
        resetPositions();
        livess= 3;
        score=0;
        gameOver=false;
        gameLoop.start();

    }
     if (e.getKeyCode()==KeyEvent.VK_UP){
         pacman.updateDirection(('U'));
     }
      else if (e.getKeyCode()==KeyEvent.VK_LEFT){
            pacman.updateDirection(('L'));
        }
       else if (e.getKeyCode()==KeyEvent.VK_DOWN){
            pacman.updateDirection(('D'));
        }
       else if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            pacman.updateDirection(('R'));
        }

       if (pacman.direction=='U'){
           pacman.image = pacmanUp;
       }
       else if (pacman.direction=='D'){
            pacman.image = pacmanDown;
        }
        else if (pacman.direction=='L'){
            pacman.image = pacmanLeft;
        }
        else if (pacman.direction=='R'){
            pacman.image = pacmanRight;
        }


    }

    class Block{
        int x;
        int y;
        int height;
        int width;
        Image image;

        int startx;
        int starty;


        char direction= 'U'; // U L D R
        int velocityX=0;
        int velocityY=0;

        public Block(Image image, int x, int y, int height, int width) {
            this.image = image;
            this.height = height;
            this.width = width;
            this.x = x;
            this.y = y;
            this.startx=x;
            this.starty=y;

        }
        void updateDirection(char direction) {
            char preDirection = this . direction;
            this.direction=direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this. velocityY;
            for (Block wall:walls) {
                if (collision(this,wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction=preDirection;
                    updateVelocity();
                }
            }

        }
        void updateVelocity(){
            if (this.direction =='U'){
                this.velocityX=0;
                this.velocityY=-tileSize/4;
            } else if (this.direction=='D')  {
                this.velocityX=0;
                this.velocityY=tileSize/4;
            } else if (this.direction=='L') {
                this.velocityX=-tileSize/4;
                this.velocityY=0;
                
            } else if (this.direction=='R') {
                this.velocityX=tileSize/4;
                this.velocityY=0;

            }
        }
        void reset() {
            this.x =this.startx;
            this.y = this.starty;
        }
    }
    private int rowCount=21;
    private int columnCount=19;
    private int tileSize=32;
    private int BoardWidth=columnCount*tileSize;
    private int BoardHeight=rowCount*tileSize;
    private Image wallImage;
    private Image blueGhost;
    private Image pinkGhost;
    private Image orangeGhost;
    private Image redGhost;

    private Image pacmanUp;
    private Image pacmanDown;
    private Image pacmanLeft;
    private Image pacmanRight;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char [] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int livess = 3;
    boolean gameOver = false;


//x = wall, O = skip, P = pacman, " " = food
 // Ghost: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };






    Map(){ //const
        setPreferredSize(new Dimension(BoardWidth,BoardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this); //imp
        setFocusable(true); //imp
        requestFocusInWindow();
        //load Images
        wallImage = new ImageIcon(getClass().getResource("wall.png")).getImage();
        blueGhost = new ImageIcon(getClass().getResource("blueGhost.png")).getImage();
        orangeGhost = new ImageIcon(getClass().getResource("orangeGhost.png")).getImage();
        pinkGhost = new ImageIcon(getClass().getResource("pinkGhost.png")).getImage();
        redGhost = new ImageIcon(getClass().getResource("redGhost.png")).getImage();

        pacmanUp = new ImageIcon(getClass().getResource("pacmanUp.png")).getImage();
        pacmanDown = new ImageIcon(getClass().getResource("pacmanDown.png")).getImage();
        pacmanLeft = new ImageIcon(getClass().getResource("pacmanLeft.png")).getImage();
        pacmanRight = new ImageIcon(getClass().getResource("pacmanRight.png")).getImage();

        loadMap();
        for (Block ghost: ghosts){
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);

        }
        gameLoop= new Timer(50,this); //how long it takes to start timer, millisec gone between frames
        //20fps (1000/50)
        gameLoop.start();

    }
    public void loadMap(){
        walls= new HashSet<Block>();
        foods= new HashSet<Block>();
        ghosts= new HashSet<Block>();
        for (int r =0; r< rowCount; r++){
            for (int c = 0; c<columnCount; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x= c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X'){ //block wall
                    Block wall = new Block(wallImage,x,y,tileSize,tileSize);
                   walls.add(wall);
                } else if (tileMapChar=='b') { //blue ghost
                  Block ghost = new Block(blueGhost,x,y,tileSize,tileSize);
                    ghosts.add(ghost);

                }else if (tileMapChar=='o') { // orange ghost
                    Block ghost = new Block(orangeGhost,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }else if (tileMapChar=='p') { // pink ghost
                    Block ghost = new Block(pinkGhost,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }else if (tileMapChar=='r') { // red ghost
                    Block ghost = new Block(redGhost,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar=='P') { // pacman
                    pacman= new Block(pacmanRight,x,y,tileSize,tileSize);
                } else if (tileMapChar==' ') { // food
                    Block food = new Block(null,x+14,y+14,4,4);
                    foods.add(food);


                }
            }
        }

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
    g.drawImage(pacman.image,pacman.x,pacman.y,pacman.width,pacman.height,null);

    for (Block ghost: ghosts){//ghosts
        g.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);
    }

    for (Block wall : walls){ // wall
        g.drawImage(wall.image,wall.x,wall.y,wall.width,wall.height,null);

    }
    g.setColor(Color.WHITE);
    for (Block food : foods){ //food
        g.fillRect(food.x,food.y,food.width,food.height);

    }
    g.setFont(new Font("Arial", Font.PLAIN, 20));
    if (gameOver){
        g.drawString ("Game Over1; " + String.valueOf(score), tileSize/2, tileSize/2);
    }
    else {
        g.drawString("x" + String.valueOf(livess) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
    }



    }


}
