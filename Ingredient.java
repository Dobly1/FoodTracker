import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ingredient {

    private String userName;
    private int stock;
    private String unit;
    private String user;
    private Database db;
    private JTextField ingredientField;
    private int dataBaseAmount;

    //More Text Fields
    private JTextField ingredName;
    private JTextField ingredStock;
    private JTextField ingredUnit;
    private JTextField csNameField;
    private JTextField csAmount;
    private JTextField riName;

    private JTable ingredientsTable;

    //Radio Buttons
    private JRadioButton csAdd;
    private JRadioButton csRemove;


    public Ingredient(String user){
        this.userName=user;
        db = new Database("jdbc:sqlite:database/"+user+".db");
    }

    public JBox ingredientGUI (){
        try {
            String sql = "INSERT INTO ingredients (name, stock,unit)\n"
                    + "VALUES('DefaultIngredient',1,'Default');";
            db.executeSQL(sql);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            try {

                //JButtons
                JButton searchEnter = new JButton("Enter");
                JButton addIngred = new JButton("Add Ingredient");
                JButton removeIngred = new JButton("Remove Ingredient");
                JButton changeStock = new JButton("Change Stock");
                JButton clearSearch = new JButton("Clear Search");
                //Corresponding ActionListeners
                searchEnter.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    searchForIngredient();
                    }
                });
                addIngred.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addIngredientGUI();
                    }
                });
                removeIngred.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeIngredientGUI();
                    }
                });
                changeStock.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changeStockGUI();
                    }
                });
                clearSearch.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    clearSearchTable();
                    }
                });

                //JTextFields
                ingredientField = new JTextField("");
                //Labels
                JLabel nameLabel = new JLabel("Name");
                JLabel stockLabel = new JLabel("Stock");
                JLabel unitsLabel = new JLabel("Units");
                //Table
                ingredientsTable = new JTable(myModel("SELECT name,stock,unit FROM ingredients"));
                JScrollPane ingredientScrollPane = new JScrollPane(ingredientsTable);
                //JBox
                JBox b = JBox.vbox(
                        JBox.vglue(),
                        JBox.hbox(JBox.hglue(),addIngred,JBox.hglue(),changeStock,JBox.hglue(),removeIngred,JBox.hglue(),clearSearch,JBox.hglue()),
                        JBox.vglue(),
                        JBox.hbox(JBox.hglue(),ingredientField,searchEnter,JBox.hglue()),
                        JBox.hbox(JBox.hglue(),nameLabel,JBox.hglue(),stockLabel,JBox.hglue(),unitsLabel,JBox.hglue()),
                        JBox.hbox(JBox.hglue(),ingredientScrollPane,JBox.hglue()),
                        JBox.vglue()
                        );
                //Sizing
                b.setSize(ingredientScrollPane,1520,780);
                b.setSize(ingredientField,1320,40);
                b.setSize(searchEnter,200,40);
                b.setSize(addIngred, 250,100);
                b.setSize(removeIngred,250,100);
                b.setSize(changeStock,250,100);
                b.setSize(clearSearch,250,100);
                b.setVisible(true);
                return b;
            } catch (Exception e) {
                System.out.println("EXCEPTION TRIGGERED");
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    private AbstractTableModel myModel(String sql) throws Exception{
        Connection con = DriverManager.getConnection("jdbc:sqlite:database/"+userName+".db");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        FillTable model = new FillTable(rs);
        return model;
    }

    private void addIngredientToDatabase(){
        try {
            String getName = ingredName.getText();
            String getStock = ingredStock.getText();
            String getUnit = ingredUnit.getText();
            String sql = "INSERT INTO ingredients(name,stock,unit) VALUES('" + getName + "'," + getStock + ", '" + getUnit + "');";
            db.executeSQL(sql);
            AbstractTableModel tmRefresh = myModel("SELECT name,stock,unit FROM ingredients");
            ingredientsTable.setModel(tmRefresh);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void clearSearchTable(){
        try{
            AbstractTableModel tmRefresh = myModel("SELECT name,stock,unit FROM ingredients");
            ingredientsTable.setModel(tmRefresh);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void addIngredientGUI(){
        Window addGUI = new Window("Add Ingredient",600,300,false);
        addGUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        ingredName = new JTextField("");
        ingredStock = new JTextField("");
        ingredUnit = new JTextField("");

        JLabel name = new JLabel("Name");
        JLabel stock = new JLabel("Stock");
        JLabel unit = new JLabel("Unit");
        JButton addEnter = new JButton("Finish");
        addEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             addGUI.dispose();
             addIngredientToDatabase();
            }
        });
        JBox b = JBox.vbox(
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),name,ingredName,JBox.hglue(),stock,ingredStock,JBox.hglue(),unit,ingredUnit,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),addEnter,JBox.hglue()),
                JBox.vglue()
            );
        b.setSize(ingredName,120,30);
        b.setSize(ingredStock,120,30);
        b.setSize(ingredUnit,120,30);
        b.setSize(addEnter,120,70);
        addGUI.add(b);
        addGUI.setVisible(true);
    }

    private void searchForIngredient(){
        try {
            String getSearchInfo = ingredientField.getText();
            ingredientField.setText("");
            String sql = "SELECT * FROM ingredients WHERE UPPER(name) LIKE UPPER('" + getSearchInfo+"')";
            AbstractTableModel tmRefresh = myModel(sql);
            ingredientsTable.setModel(tmRefresh);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    private void removeIngredient(){
        String riNameString = riName.getText();
        String sql = "DELETE FROM ingredients WHERE UPPER(name) LIKE upper('"+riNameString+"');";
        db.executeSQL(sql);
        clearSearchTable();
    }

    private void removeIngredientGUI(){
        Window riGUI = new Window("Remove Ingredient",600,300,false);
        riGUI.setDefaultCloseOperation(2);

        //JLabels
        JLabel nameLabel = new JLabel("Ingredient Name:");

        //JTextField
        riName = new JTextField("");

        //JButton
        JButton enter = new JButton("Enter");

        //Action Listeners
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                removeIngredient();
                riGUI.dispose();
            }
        });

        JBox b = JBox.vbox(
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),nameLabel,riName,JBox.hglue()),
                JBox.vglue(),
                JBox.hbox(JBox.hglue(),enter,JBox.hglue()),
                JBox.vglue()
        );

        b.setSize(riName,150,20);
        b.setSize(enter,80,40);
        riGUI.add(b);
        riGUI.setVisible(true);

    }

    private void changeStockEvaluator(){
        if(csAdd.isSelected()){
            addStock();
        }
        if(csRemove.isSelected()){
            removeStock();
        }
    }

    private void addStock(){
        try {
            String csNameString = csNameField.getText();
            String csAmountString = csAmount.getText();
            int csAmountInt = Integer.parseInt(csAmountString);
            String sql = "SELECT * FROM ingredients WHERE UPPER(name) LIKE upper('" + csNameString + "')";
            try(
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database/"+userName+".db");
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
                rs.next();
                dataBaseAmount = rs.getInt("stock");
            }
            catch(Exception e) {}

            dataBaseAmount += csAmountInt;
            System.out.println(dataBaseAmount);
            String sql2 = "UPDATE ingredients SET stock ="+dataBaseAmount+" WHERE upper(name) LIKE ('"+csNameString+"')";
            db.executeSQL(sql2);
            clearSearchTable();
        }
        catch(Exception e){
        }
    }

    private void removeStock(){
        try {
            String csNameString = csNameField.getText();
            String csAmountString = csAmount.getText();
            int csAmountInt = Integer.parseInt(csAmountString);
            String sql = "SELECT * FROM ingredients WHERE UPPER(name) LIKE upper('" + csNameString + "')";
            try(
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:database/"+userName+".db");
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {
                rs.next();
                dataBaseAmount = rs.getInt("stock");
            }
            catch(Exception e) {}

            dataBaseAmount -= csAmountInt;
            System.out.println(dataBaseAmount);
            String sql2 = "UPDATE ingredients SET stock ="+dataBaseAmount+" WHERE upper(name) LIKE ('"+csNameString+"')";
            db.executeSQL(sql2);
            clearSearchTable();
        }
        catch(Exception e){
        }
    }

    private void changeStockGUI(){
        Window csGUI = new Window("Change Stock",600,300,false);
        csGUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //JButtons
        JButton enter = new JButton("Enter");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                csGUI.dispose();
                changeStockEvaluator();
            }
        });
        //Radio Buttons
        csAdd= new JRadioButton("Add");
        csRemove = new JRadioButton("Remove");
        //Grouping Buttons Together so only 1 can be selected
        ButtonGroup group1 = new ButtonGroup();
        group1.add(csAdd);
        group1.add(csRemove);
        //Setting up the Text Fields
        csNameField = new JTextField("");
        csAmount = new JTextField("");
        //Setting up JLabels
        JLabel nameLabel = new JLabel("Ingredient Name:");
        JLabel amountLabel = new JLabel("Amount:");

        JBox b = JBox.vbox(
                            JBox.vglue(),
                            csAdd,
                            JBox.hbox(JBox.hglue(),nameLabel,csNameField,JBox.hglue(),amountLabel,csAmount,JBox.hglue()),
                            csRemove,
                            JBox.hbox(JBox.hglue(),enter,JBox.hglue()),
                            JBox.vglue()
                          );
        b.setSize(csNameField,120,30);
        b.setSize(csAmount,120,30);
        csGUI.add(b);
        csGUI.setVisible(true);
    }

}
