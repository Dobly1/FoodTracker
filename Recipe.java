import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
public class Recipe {


        private ArrayList<String> ingredients;
        private ArrayList<Integer> amount;
        private int dataBaseAmount;
        private int[] lowest;

        private Database db;
        private String userName;
        private JTable recipeTable;
        //JTextField
        private JTextField recipeField;
        private JTextField rrName;
        private JTextField recipeName;
        private JTextField recipeIngredient;
        private JTextField recipeAmount;
        private JTextField mrName;
        private JTextField mrAmount;
        private JTextField chmName;
        //JLabel
        private JLabel chmAmount;


    public Recipe(String user){ //Constructor requiring user logged in, so that connection can be established to specific database
            this.userName=user;
            db = new Database("jdbc:sqlite:database/"+user+".db"); //Create new instance of database to execute SQL
        }

    public JBox RecipeGUI(){
                try { //Must use try/catch block in order to account for SQL exceptions thrown by the TableModel
                    //Set up necessary JButtons
                    JButton addNewRecipe = new JButton("Add Recipe");
                    JButton removeRecipe = new JButton("Remove Recipe");
                    JButton clearSearch = new JButton("Clear Search");
                    JButton searchEnter = new JButton("Enter");
                    JButton makeRecipe = new JButton("Make Recipe");
                    JButton checkHowMuch = new JButton("Able to Make");
                    //Add functionality in order to make Buttons useful
                    addNewRecipe.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        addRecipeGUI();
                        }
                    });
                    removeRecipe.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        removeRecipeGUI();
                        }
                    });
                    clearSearch.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        clearSearchTable();
                        }
                    });
                    searchEnter.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        searchForRecipe();
                        }
                    });
                    makeRecipe.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        makeRecipeGUI();
                        }
                    });
                    checkHowMuch.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        checkHowMuchGUI();
                        }
                    });
                    //JLabels
                    JLabel recipeNameLabel = new JLabel("Recipe");
                    JLabel recipeIngredientLabel = new JLabel("Ingredient");
                    JLabel recipeAmountLabel = new JLabel("Amount");
                    //JTextFields
                    recipeField = new JTextField("");
                    //JTable
                    recipeTable = new JTable(myModel("SELECT recipe,ingredient,amount FROM recipes"));
                    JScrollPane recipeScrollPane = new JScrollPane(recipeTable);
                    //Create JBox with proper placement of all the objects
                    JBox b = JBox.vbox(
                            JBox.vglue(),
                            JBox.hbox(JBox.hglue(),addNewRecipe,JBox.hglue(),removeRecipe,JBox.hglue(),clearSearch,JBox.hglue(),makeRecipe,JBox.hglue(),checkHowMuch,JBox.hglue()),
                            JBox.vglue(),
                            JBox.hbox(JBox.hglue(),recipeField,searchEnter,JBox.hglue()),
                            JBox.hbox(JBox.hglue(),recipeNameLabel,JBox.hglue(),recipeIngredientLabel,JBox.hglue(),recipeAmountLabel,JBox.hglue()),
                            JBox.hbox(JBox.hglue(),recipeScrollPane,JBox.hglue()),
                            JBox.vglue()
                    );
                    b.setSize(addNewRecipe,250,100);
                    b.setSize(removeRecipe,250,100); //Setting appropriate sizes
                    b.setSize(clearSearch,250,100);
                    b.setSize(makeRecipe,250,100);
                    b.setSize(checkHowMuch,250,100);
                    b.setSize(recipeField,1320,40);
                    b.setSize(searchEnter,200,40);
                    b.setSize(recipeScrollPane,1520,780);
                    return b;

                } catch (Exception e) {

                }
            return null;
        }

    private AbstractTableModel myModel(String sql) throws Exception{
        Connection con = DriverManager.getConnection("jdbc:sqlite:database/"+userName+".db"); //Forms new connection to database
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql); //Generates result set based off the input to fill the table
        FillTable model = new FillTable(rs); //Creates AbstractTableModel based off the ResultSet
        return model;
    }

    private void clearSearchTable(){
        try{
            AbstractTableModel tmRefresh = myModel("SELECT recipe,ingredient,amount FROM recipes");
            recipeTable.setModel(tmRefresh); //Generates new
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void searchForRecipe(){
        try {
            String getSearchInfo = recipeField.getText();
            recipeField.setText("");
            String sql = "SELECT * FROM recipes WHERE UPPER(recipe) LIKE UPPER('" + getSearchInfo+"')";
            AbstractTableModel tmRefresh = myModel(sql);
            recipeTable.setModel(tmRefresh);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    private void addRecipeGUI(){
        Window addGUI = new Window("Add Recipe",600,300,false);
        addGUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        recipeName = new JTextField("");
        recipeIngredient = new JTextField("");
        recipeAmount = new JTextField("");

        JLabel arName = new JLabel("Recipe");
        JLabel arIngred = new JLabel("Ingredient");
        JLabel arAmount = new JLabel("Amount");
        JButton addEnter = new JButton("Enter");
        JButton finish = new JButton("Finish");
        finish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGUI.dispose();
            }
        });
        addEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRecipeToDatabase();
            }
        });
        JBox b = JBox.vbox(
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),arName,recipeName,JBox.hglue(),arIngred,recipeIngredient,JBox.hglue(),arAmount,recipeAmount,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),addEnter,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),finish,JBox.hglue()),
                JBox.vglue()
        );
        b.setSize(recipeName,120,30);
        b.setSize(recipeIngredient,120,30);
        b.setSize(recipeAmount,120,30);
        b.setSize(addEnter,120,70);
        addGUI.add(b);
        addGUI.setVisible(true);
    }

    private void addRecipeToDatabase(){
        try {
            String getName = recipeName.getText();
            String getIngred = recipeIngredient.getText();
            String getAmount = recipeAmount.getText();
            recipeIngredient.setText("");
            recipeAmount.setText("");
            String sql = "INSERT INTO recipes(recipe,ingredient,amount) VALUES('" + getName + "','" + getIngred + "', " + getAmount + ");";
            db.executeSQL(sql);
            clearSearchTable();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    private void removeRecipeGUI(){
        Window rrGUI = new Window("Remove Recipe",600,300,false);
        rrGUI.setDefaultCloseOperation(2);

        //JLabels
        JLabel nameLabel = new JLabel("Ingredient Name:");

        //JTextField
        rrName = new JTextField("");

        //JButton
        JButton enter = new JButton("Enter");

        //Action Listeners
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                removeRecipe();
                rrGUI.dispose();
            }
        });

        JBox b = JBox.vbox(
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),nameLabel,rrName,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),enter,JBox.hglue()),
                JBox.vglue()
        );

        b.setSize(rrName,150,20);
        b.setSize(enter,80,40);
        rrGUI.add(b);
        rrGUI.setVisible(true);
    }

    private void removeRecipe(){
        String rrNameString = rrName.getText();
        String sql = "DELETE FROM recipes WHERE UPPER(recipe) LIKE upper('"+rrNameString+"');";
        db.executeSQL(sql);
        clearSearchTable();
    }


    private void makeRecipeGUI(){
        Window mrGUI = new Window("Make Recipe",600,300,false);
        mrGUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //JButtons
        JButton enter = new JButton("Enter");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mrGUI.dispose();
                makeRecipeEval();
            }
        });

        //Setting up the Text Fields
        mrName = new JTextField("");
        mrAmount = new JTextField("");
        //Setting up JLabels
        JLabel nameLabel = new JLabel("Recipe Name:");
        JLabel amountLabel = new JLabel("Amount:");
        JLabel warning = new JLabel("Please enter Recipe name exactly as it is seen on the recipe table");
        JLabel warning2 = new JLabel("Aswell to see the new amount of ingredients hit 'Clear Search' on the ingredients tab");

        JBox b = JBox.vbox(
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),nameLabel,mrName,JBox.hglue(),amountLabel,mrAmount,JBox.hglue()),
                JBox.hbox(JBox.hglue(),enter,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),warning,JBox.hglue()),
                JBox.hbox(JBox.hglue(),warning2,JBox.hglue()),
                JBox.vglue()
        );
        b.setSize(mrName,120,30);
        b.setSize(mrAmount,120,30);
        mrGUI.add(b);
        mrGUI.setVisible(true);
    }

    private void makeRecipeEval(){
        String recipeName = mrName.getText();
        int recipeAmount = Integer.parseInt(mrAmount.getText());
        String sql = "SELECT * FROM recipes WHERE UPPER(recipe) LIKE UPPER('"+recipeName+"')";
        int i = 0;
        ingredients = new ArrayList<String>(0);
        amount = new ArrayList<Integer>(0);

        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:database/"+userName+".db");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql))
        {
            while(rs.next()){
            String ingredName = rs.getString("ingredient");
            ingredients.add(ingredName);
            Integer amountOfIngredients = rs.getInt("amount");
            amount.add(amountOfIngredients);
            }
        } catch(Exception e){ }
        for(int o=0;o<ingredients.size();o++){
            String sql2 = "SELECT * FROM ingredients WHERE UPPER(name) LIKE upper('" + ingredients.get(o) + "')";
            try(
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:database/"+userName+".db");
                    PreparedStatement stmt = conn.prepareStatement(sql2);
                    ResultSet rs = stmt.executeQuery()) {
                rs.next();
                dataBaseAmount = rs.getInt("stock");
            }
            catch(Exception e) {}

            dataBaseAmount -= amount.get(o)*recipeAmount;

            String sql3 = "UPDATE ingredients SET stock ="+dataBaseAmount+" WHERE upper(name) LIKE ('"+ingredients.get(o)+"')";
            db.executeSQL(sql3);
            clearSearchTable();
        }

    }


    private void checkHowMuchGUI(){
        Window chmGUI = new Window("Check How Much",600,300,false);
        chmGUI.setDefaultCloseOperation(2);

        //JLabels
        JLabel nameLabel = new JLabel("Recipe Name:");
        chmAmount = new JLabel("Enter Recipe Name and Press Enter");
        //JTextField
        chmName = new JTextField("");

        //JButton
        JButton enter = new JButton("Enter");
        JButton finish = new JButton("Finish");

        //Action Listeners
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                checkHowMuchEval();

            }
        });
        finish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chmName.setText("Enter Recipe Name and Press Enter");
                chmGUI.dispose();
            }
        });
        JBox b = JBox.vbox(
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),nameLabel,chmName,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),enter,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),chmAmount,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),finish,JBox.hglue()),
                JBox.vglue()
        );

        b.setSize(chmName,150,20);
        b.setSize(enter,80,40);
        chmGUI.add(b);
        chmGUI.setVisible(true);
    }

    private void checkHowMuchEval(){
        String recipeName = chmName.getText();
        String sql = "SELECT * FROM recipes WHERE UPPER(recipe) LIKE UPPER('"+recipeName+"')";
        int i = 0;
        ingredients = new ArrayList<String>(0);
        amount = new ArrayList<Integer>(0);

        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:database/"+userName+".db");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql))
        {
            while(rs.next()){
                String ingredName = rs.getString("ingredient");
                ingredients.add(ingredName);
                Integer amountOfIngredients = rs.getInt("amount");
                amount.add(amountOfIngredients);
            }
        } catch(Exception e){ }
        lowest = new int[ingredients.size()];
        for(int o=0;o<ingredients.size();o++){
            String sql2 = "SELECT * FROM ingredients WHERE UPPER(name) LIKE upper('" + ingredients.get(o) + "')";
            try(
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:database/"+userName+".db");
                    PreparedStatement stmt = conn.prepareStatement(sql2);
                    ResultSet rs = stmt.executeQuery()) {
                rs.next();
                dataBaseAmount = rs.getInt("stock");
            }
            catch(Exception e) {}

            lowest[o] = dataBaseAmount / amount.get(o);

        }
        while(true) //Uses while loop so it will run for as long as it needs to
        {
            int timesChecked = 0;
            for(int o=0; o<lowest.length-1;o++){ //Checks elements from the first to second last of the array

                if(lowest[o]>lowest[o+1]) //Checks if the current element is greater than the one to it's right
                {
                    int temp = lowest[o]; //Swaps their values in order to put the greater one on the right
                    lowest[o] = lowest[(o)+1];
                    lowest[o+1] = temp;
                    timesChecked++; //Keeps track of whether or not the if loops was needed to perform swaps

                }
            }
            if(timesChecked==0)
            {
                break; //If no swaps were needed then it exits
            }
        }
        chmAmount.setText("You have enough ingredients to make this recipe "+lowest[0]+" times");


    }
}
