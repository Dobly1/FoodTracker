import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FunctionsWindow{

    private Database db;
    private String user;
    private int x=0;
    private int y=0;

    public FunctionsWindow(String userFromLogin){
        user = userFromLogin;
        start();
    }

    private void start(){
        prepareDatabases();
        GUI();
    }

    private void GUI(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        x = width;
        int height = (int) screenSize.getHeight();
        y = height;
        Window w = new Window("Food Tracker", width,height,true);


        System.out.println(user);
        Ingredient ing = new Ingredient(user);
        Recipe rec = new Recipe(user);
        JTabbedPane JTP = new JTabbedPane();
        JBox ingredientsBox = ing.ingredientGUI();
        JBox recipeBox = rec.RecipeGUI();
        JTP.addTab("Ingredients",ingredientsBox);
        JTP.addTab("Recipes",recipeBox);
        w.add(JTP);

        w.setVisible(true);

    }

    private void prepareDatabases(){
        db = new Database("jdbc:sqlite:database/"+user+".db");
        db.createNewDatabase();
        String ingredientTable = "CREATE TABLE IF NOT EXISTS ingredients(\n"
                            +"name text PRIMARY KEY,\n"
                            +"stock integer UNSIGNED CHECK (stock > 0) NOT NULL,\n"
                            +"unit text NOT NULL,\n"
                            +"capacity real\n"
                            +");";
        String recipeTable = "CREATE TABLE IF NOT EXISTS recipes(\n"
                            +"id integer PRIMARY KEY,\n"
                            +"recipe text NOT NULL,\n"
                            +"ingredient text NOT NULL,\n"
                            +"amount integer NOT NULL,\n"
                            +"capacity real\n"
                            +");";
        db.executeSQL(ingredientTable);
        db.executeSQL(recipeTable);
    }

}
