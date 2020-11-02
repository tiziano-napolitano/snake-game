import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;
import java.util.Arrays;
import java.util.Random;

public class BoardPanel extends JPanel implements ActionListener {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int ELEMENT = 25;
    static final int UNITS = (WIDTH*HEIGHT)/ELEMENT; //all dots of board
    static final int DELAY = 75;
    final int[] x = new int[UNITS];
    final int[] y = new int[UNITS];
    int bodyParts = 3;
    int applesEaten;
    int apple_X;
    int apple_Y;
    char direction = 'R';
    boolean running = false;
    boolean pause = false;
    Timer timer;
    Random random;
    Image body;
    Image head;
    Image apple;
    Image tail;
    Image map;
    JButton jButton;

    public BoardPanel(){
        initPanelGame();
    }

    private void initPanelGame(){
        random = new Random();
        addKeyListener(new KeyboardAdapter());
        setFocusable(true);
        images();
        Dimension size = new Dimension(map.getWidth(null), map.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        startGame();
    }

    private void images(){
        body = new ImageIcon("src/img/body.png").getImage();
        head = new ImageIcon("src/img/head_right.png").getImage();
        apple = new ImageIcon("src/img/apple.png").getImage();
        map = new ImageIcon("src/img/map.png").getImage();
        tail = new ImageIcon("src/img/tail_right.png").getImage();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(map, 0, 0, null);
        draw(g);
    }

    public void draw(Graphics g){
        /*
        Griglia

        for(int i=0;i<HEIGHT/ELEMENT;i++){
            g.drawLine(i*ELEMENT,0,i*ELEMENT,HEIGHT);
            g.drawLine(0,i*ELEMENT,WIDTH,i*ELEMENT);
        }*/

        if(running){
            g.drawImage(apple,apple_X,apple_Y,this);
            for(int i=0;i<bodyParts-1;i++){
                if(i==0){
                    g.drawImage(head,x[i],y[i],this);
                } else {
                    g.drawImage(body,x[i],y[i],this);
                }
            }
            tailmove();
            g.drawImage(tail,x[bodyParts-1],y[bodyParts-1],this);
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String msg="Score: ";
            g.drawString(msg+applesEaten,(WIDTH - metrics.stringWidth(msg+applesEaten))/2, g.getFont().getSize());
        }else{
            gameOver(g);
        }

    }

    public void newApple(){
        apple_X = random.nextInt(WIDTH/ELEMENT)*ELEMENT;
        apple_Y = random.nextInt(HEIGHT/ELEMENT)*ELEMENT;
    }

    public void move(){
        for(int i= bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U' -> {
                y[0] = y[0] - ELEMENT;
                head = new ImageIcon("src/img/head_top.png").getImage();
            }
            case 'D' -> {
                y[0] = y[0] + ELEMENT;
                head = new ImageIcon("src/img/head_down.png").getImage();
            }
            case 'L' -> {
                x[0] = x[0] - ELEMENT;
                head = new ImageIcon("src/img/head_left.png").getImage();
            }
            case 'R' -> {
                x[0] = x[0] + ELEMENT;
                head = new ImageIcon("src/img/head_right.png").getImage();
            }
        }
    }

    public void tailmove(){
        if(x[bodyParts-1]==x[bodyParts-2]){
            if(y[bodyParts-2]==y[bodyParts-1]+ELEMENT) tail = new ImageIcon("src/img/tail_down.png").getImage();
            else if(y[bodyParts-2]==y[bodyParts-1]-ELEMENT) tail = new ImageIcon("src/img/tail_top.png").getImage();
        }else if(y[bodyParts-1]==y[bodyParts-2]){
            if(x[bodyParts-2]==x[bodyParts-1]+ELEMENT) tail = new ImageIcon("src/img/tail_right.png").getImage();
            else if(x[bodyParts-2]==x[bodyParts-1]-ELEMENT) tail = new ImageIcon("src/img/tail_left.png").getImage();
        }
    }

    public void checkApple(){
        if((x[0] == apple_X) && (y[0] == apple_Y)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        //check for collision head with body
        for(int i=bodyParts;i>0;i--){
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        //check head collides left bord
        if(x[0]<0){
            running=false;
        }

        //check head collides right bord
        if(x[0]>=WIDTH){
            running=false;
        }

        //check head collides top bord
        if(y[0]<0){
            running=false;
        }

        //check head collides down bord
        if(y[0]>=HEIGHT){
            running=false;
        }

        if(!running){
            timer.stop();
        }
    }

    private void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String msg="Score: ";
        g.drawString(msg+applesEaten,(WIDTH - metrics.stringWidth(msg+applesEaten))/2, g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Helvetica",Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        String msg1="GaMe OvEr";
        g.drawString(msg1,(WIDTH - metrics1.stringWidth(msg1))/2, HEIGHT/2);

        jButton = new JButton("START");
        jButton.setForeground(Color.white);
        jButton.setBackground(Color.red);
        jButton.setFont(new Font("ariel",Font.BOLD,40));
        jButton.setBounds(200,340,200,40);
        jButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    applesEaten = 0;
                    bodyParts = 3;
                    direction='R';
                    Arrays.fill(x,0);
                    Arrays.fill(y,0);
                    timer.start();
                    running=true;
                    jButton.setVisible(false);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        add(jButton);
        jButton.requestFocus();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    private class KeyboardAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            switch (e.getKeyCode()){
                case KeyEvent.VK_SPACE:
                    JTextField stop=new JTextField("PAUSE!");
                    stop.setFont(new Font("arial",Font.BOLD,50));
                    stop.setBounds( 200,240,200,60);
                    stop.setBackground(Color.red);
                    stop.setForeground(Color.white);
                    if(!pause && running){
                        getRootPane().add(stop);
                        stop.setVisible(true);
                        pause=true;
                        timer.stop();
                    }else if(pause && running){
                        getRootPane().remove(stop);
                        stop.setVisible(false);
                        pause=false;
                        timer.restart();
                    }
                    break;
                case KeyEvent.VK_A:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if(!running && !pause) {

                    }
            }
        }
    }
}
