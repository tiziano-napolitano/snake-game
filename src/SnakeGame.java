import javax.swing.*;

public class SnakeGame extends JFrame {
    private static SnakeGame singleton=null;

    private SnakeGame(){ initBoard();}

    public static SnakeGame getInstance(){
        if(singleton==null)
            singleton=new SnakeGame();
        return singleton;
    }

    private void initBoard(){
        getContentPane().add(new BoardPanel());
        setTitle("SnAkE GaMe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> getInstance().setVisible(true));
    }
}
