import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    //method for the position of the aliens
    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;
        boolean alive = true;  //used for aliens
        boolean used = false;//used for aliens

        Block(int x, int y, int width, int height, Image img){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    //same size as before 
    int tileSize = 32;
    int rows = 16;
    int columns = 16; 
    int boardWidth = tileSize * columns;
    int boardHeight = tileSize * rows;

    //creating variables for images
    Image shipImg;  
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentImg;
    Image alienYellowImg;
    ArrayList<Image> alienImgArray;

     //ship
    int shipWidth = tileSize*2;//64px
    int shipHeight = tileSize; //32px
    int shipX = tileSize*columns/2 -tileSize; //to make it start from the middle on X axis
    int shipY = boardHeight - tileSize*2; //to start 2 tiles above the bottom
    int shioVelocityX = tileSize;

    Block ship;//for the drawing step
     //alines
     ArrayList<Block> alienArray;
     int alienWidth = tileSize*2;
     int alienHeight = tileSize;
     int alienX = tileSize;
     int alienY = tileSize;


     int alienRows = 2;
     int alienColums = 3 ;
     int alienCount = 0; // number of alines to defeat
     int alienVelocityX = 1; //alien moving speed

     // bullets
     ArrayList<Block> bulletArray = new ArrayList<>();
     int bulletWidth = tileSize/8;
     int bulletHeight = tileSize/2;
     int bulletVelocityY = -2 ;// bullet moving speed


    Timer gameLoop;
    int score = 0;
    boolean gameOver = false;
    String[] difficultyLevels = {"Easy", "Medium", "Hard"};
    String selectedDifficulty = "Medium";
    boolean showMenu = true;

    JButton easyButton, mediumButton, hardButton;
    JLabel titleLabel;

    //constructor declaration
    SpaceInvaders() {
        //to make sure that its a 512px window;
        setPreferredSize(new Dimension(boardWidth, boardHeight)); 
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);
        bulletArray =  new ArrayList<>();


        //load images
        shipImg = new ImageIcon(getClass().getResource("./ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("./alien.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("./alien-cyan.png")).getImage();
        alienMagentImg = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage();

        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentImg);
        alienImgArray.add(alienYellowImg);

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
        alienArray = new ArrayList<Block>();
        bulletArray = new ArrayList<Block>();



        //game timer
        gameLoop =new Timer(1000/60, this);//1000/60=16.7
        createAliens();
        gameLoop.start();
        createMenuComponents();
        showMenu = true;
    }
    //defining the paint component
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    //making draw function
    public void draw(Graphics g){
        if (!showMenu) {
            //ship draw
            g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

            // aliens
            for (int i = 0; i < alienArray.size(); i++) {
                Block alien = alienArray.get(i);
                if (alien.alive) {
                    g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null); // Use alien.img instead of alienImg
                }
            }

            // bullets
            g.setColor(Color.white);
            for (int i = 0; i < bulletArray.size(); i++) {
                Block bullet = bulletArray.get(i);
                if (!bullet.used) {
                    g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
                }
            }

            //score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            if (gameOver) {
                g.drawString("Game Over :" + String.valueOf(score), 10, 35);
            } else {
                g.drawString("Score :" + String.valueOf(score), 10, 35);
            }
        }
    }

    public void move(){
        //aliens
        for(int i = 0;i < alienArray.size();i++){
            Block alien = alienArray.get(i);
            if(alien.alive){
                alien.x += alienVelocityX;


                // alien move when touchs borders
                if(alien.x + alien.width >= boardWidth || alien.x <= 0){
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX*2;

                    // alien move down one row
                    for(int j = 0; j <alienArray.size(); j++){
                        alienArray.get(j).y += alienHeight;
                    }

                }

                if (alien.y >= ship.y){
                    gameOver = true;
                    
            }
        }


     //bullets
     for (int k = 0; k < bulletArray.size(); k++) {
        Block bullet = bulletArray.get(k);
        bullet.y += bulletVelocityY;
        // bullet collision with aliens
        for (int j = 0; j < alienArray.size(); j++) {
            Block currentAlien = alienArray.get(j);
            if (detectCollision(bullet, currentAlien) && currentAlien.alive && !bullet.used) {
                bullet.used = true;
                currentAlien.alive = false;
                alienCount--;
                score += 10;
            }
        }
    }
    
    //clean up bullets
    for (int k = 0; k < bulletArray.size(); k++) {
        Block bullet = bulletArray.get(k);
        if (bullet.used || bullet.y < 0) {
            bulletArray.remove(k);
            k--; // adjust index after removal
        }
    }
    //next level
    if (alienCount == 0) {
        bulletVelocityY=-2;
        alienColums = Math.min(alienColums + 1, columns / 2 - 2);
        alienRows = Math.min(alienRows + 1, rows - 6);
        bulletArray.clear();
        alienArray.clear();
        createAliens();
    }
}
    }


 



     public void createAliens(){
        Random random = new Random();
         for( int r = 0; r < alienRows; r++){

            for(int c = 0; c < alienColums; c++){

                 int randomImgIndex = random.nextInt(alienImgArray.size());
                Block alien = new Block(
                    alienX + c*alienWidth,
                    alienY + r*alienHeight,
                    alienWidth,
                    alienHeight,
                    alienImgArray.get(randomImgIndex)
                    ); 
                    alienArray.add(alien);

            }

        } 
        alienCount = alienArray.size();
    } 

    public boolean detectCollision(Block b1, Block b2) {
        return b1.x < b2.x + b2.width &&
               b1.x + b1.width > b2.x &&
               b1.y < b2.y + b2.height &&
               b1.y + b2.height > b2.y;
    }

    public void createMenuComponents() {
        setLayout(null);

        titleLabel = new JLabel("FCI Space Invaders");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.white);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, tileSize, boardWidth, 2 * tileSize);
        add(titleLabel);

        easyButton = createButton("Easy", 5 * tileSize, boardWidth, 2 * tileSize);
        mediumButton = createButton("Medium", 8 * tileSize, boardWidth, 2 * tileSize);
        hardButton = createButton("Hard", 11 * tileSize, boardWidth, 2 * tileSize);

        easyButton.addActionListener(e -> setDifficulty("Easy"));
        mediumButton.addActionListener(e -> setDifficulty("Medium"));
        hardButton.addActionListener(e -> setDifficulty("Hard"));

        add(easyButton);
        add(mediumButton);
        add(hardButton);
    }

    private JButton createButton(String text, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(width / 2 - 100, y, 200, height); // Increase button width
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setForeground(Color.white);
        button.setFont(new Font("Arial", Font.BOLD, 35));
        return button;
    }

    public void showMenuComponents() {
        titleLabel.setVisible(true);
        easyButton.setVisible(true);
        mediumButton.setVisible(true);
        hardButton.setVisible(true);
    }

    public void hideMenuComponents() {
        titleLabel.setVisible(false);
        easyButton.setVisible(false);
        mediumButton.setVisible(false);
        hardButton.setVisible(false);
    }

    public void setDifficulty(String difficulty) {
        switch (difficulty) {
            case "Easy":
                alienVelocityX = 1;
                bulletVelocityY = -2;
                break;
            case "Medium":
                alienVelocityX = 2; // Increase speed for Medium
                bulletVelocityY = -2;
                break;
            case "Hard":
                alienVelocityX = 5; // Increase speed for Hard
                bulletVelocityY = -2;
                break;
        }
        hideMenuComponents();
        startGame();
    }

    public void startGame() {
        showMenu = false;
        gameOver = false;
        ship.x = shipX;
        alienArray.clear();
        bulletArray.clear();
        score = 0;
        alienRows = 2; // Reset alien rows
        alienColums = 3; // Reset alien columns
        createAliens();
        gameLoop.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!showMenu) {
            move();
            repaint();
        }
        if (gameOver) {
            gameLoop.stop();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            showMenu = true;
            showMenuComponents();
            gameOver = false;
            repaint(); // Ensure the game components are hidden
        } else if (showMenu) {
            showMenuComponents();
        } else if (!showMenu) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shioVelocityX >= 0) {
                ship.x -= shioVelocityX; // move left one tile
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shioVelocityX <= boardWidth) {
                ship.x += shioVelocityX; // move right one tile
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                Block bullet = new Block(ship.x + shipWidth * 15 / 32, ship.y, bulletWidth, bulletHeight, null);
                bulletArray.add(bullet);
            }
        }
        if (showMenu && e.getKeyCode() == KeyEvent.VK_ENTER) {
            showMenu = false;
            gameOver = false;
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienRows = 2; // Reset alien rows
            alienColums = 3; // Reset alien columns
            createAliens();
            gameLoop.start();
        }
    }
}

