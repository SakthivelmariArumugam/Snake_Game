import java.awt.*;
import java.awt.event.*;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener,KeyListener{
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityy!=1)
        {
            velocityx=0;
            velocityy=-1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityy!=-1){
            velocityx=0;
            velocityy=1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityx!=1)
        {
            velocityx=-1;
            velocityy=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityx!=-1)
        {
            velocityx=1;
            velocityy=0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private class Tile{
        int x;
        int y;
        Tile(int x,int y)
        {
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHight;

    int tileSize=25;

    Tile snakeHead;

    ArrayList<Tile> snakeBody;

    Tile food;

    Random random;

    Timer gameLoop;

    int velocityx;
    int velocityy;

    boolean gameOver=false;

    SnakeGame(int boardWidth,int boardHight)
    {
        this.boardWidth=boardWidth;
        this.boardHight=boardHight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeBody=new ArrayList<Tile>();
        snakeHead=new Tile(5,5);

        food=new Tile(10,10);

        random=new Random();
        placeFood();

        velocityx=0;
        velocityy=0;


        gameLoop=new Timer(100,this);
        gameLoop.start();

    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
        for(int i=0;i<boardWidth/tileSize;i++)
        {
            g.drawLine(i*tileSize,0,i*tileSize,boardHight);
            g.drawLine(0,i*tileSize,boardWidth,i*tileSize);
        }
        g.setColor(Color.red);
        g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);
        g.fill3DRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize,true);
        g.setColor(Color.green);
        g.fillRect(snakeHead.x*tileSize,snakeHead.y*tileSize,tileSize,tileSize);
        g.fill3DRect(snakeHead.x*tileSize,snakeHead.y*tileSize,tileSize,tileSize,true);
        for(int i=0;i<snakeBody.size();i++)
        {
            Tile snakePart=snakeBody.get(i);
            g.fill3DRect(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize,true);
        }
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver)
        {
            g.setColor(Color.red);
            g.drawString("Game Over:"+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
        else
        {
            g.drawString("Score:"+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
    }
    public boolean collision(Tile tile1,Tile tile2)
    {
        return tile1.x==tile2.x && tile1.y==tile2.y;
    }
    public void placeFood()
    {
        food.x=random.nextInt(boardWidth/tileSize);
        food.y=random.nextInt(boardHight/tileSize);
    }
    public void move()
    {
        if (collision(snakeHead,food)){
            snakeBody.add(new Tile(food.x,food.y));
            placeFood();
        }
        for(int i=snakeBody.size()-1;i>=0;i--)
        {
            Tile snakePart=snakeBody.get(i);
            if(i==0)
            {
                snakePart.x=snakeHead.x;
                snakePart.y=snakeHead.y;
            }
            else
            {
                Tile prevSankePart=snakeBody.get(i-1);
                snakePart.x=prevSankePart.x;
                snakePart.y=prevSankePart.y;
            }
        }
        snakeHead.x +=velocityx;
        snakeHead.y +=velocityy;
        for(int i=0;i<snakeBody.size();i++)
        {
            Tile snakePart=snakeBody.get(i);
            if(collision(snakeHead,snakePart)){
                gameOver=true;
            }
        }
        if(snakeHead.x*tileSize<0 || snakeHead.x*tileSize>boardWidth || snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHight){
            gameOver=true;
        }
    }
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
        if(gameOver)
        {
            gameLoop.stop();
        }
    }


}