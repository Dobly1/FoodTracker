import javax.swing.*;

public class Window extends JFrame {

    public Window(String name, int x,int y, Boolean resizable){
        this.setTitle(name);
        this.setSize(x,y);
        this.setResizable(resizable);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

    }


}
