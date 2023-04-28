import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
public class GameOverScreen extends Actor {

    // custom colors
    private static final Color BLANK = new Color(0, 0, 0, 0);
    private static final Color TAN = new Color(120, 111, 102);


    public GameOverScreen(int score) {

        // game over image
        GreenfootImage gameOverBg = new GreenfootImage("images/2048_gameover.png");


        // put score on image and draw it
        GreenfootImage finalScore = new GreenfootImage("Score: " + score, 50, TAN, BLANK);
        gameOverBg.drawImage(finalScore, 0, 450);
        setImage(gameOverBg);

    }
}
