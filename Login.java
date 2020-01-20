import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login{
    private Window w;
    private String userNameString;
    private String passWordString;
    private static JLabel welcome;
    private static JTextField userName;
    private static JPasswordField passWord;
    private Database db = new Database("jdbc:sqlite:database/login.db");
    private boolean loginGood = false;

    public Login(){
        db.createNewDatabase();
        String sql = "CREATE TABLE IF NOT EXISTS credentials(\n"
                + "	username text PRIMARY KEY,\n"
                + " password text NOT NULL,\n"
                + "	capacity real\n"
                + ");";

        db.executeSQL(sql);
        LoginGUI();

    }

    public void LoginGUI(){
        w = new Window("Login",600,300,false);
        w.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        userName = new JTextField();
        passWord = new JPasswordField();
        passWord.setEchoChar('*');
        JButton loginEnter = new JButton("Enter");
        JButton createNewUser = new JButton("Create New User");
        createNewUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getInfoFromGui();
                enterNewUser();
            }
        });
        loginEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            getInfoFromGui();
            loginGood = LoginChecker(userNameString,passWordString);
            if(loginGood){
                goodLogin();
            }
            else {
                badLogin();
            }
            }
        });
        JLabel userNamePrompt = new JLabel("Username: ");
        JLabel passWordPrompt = new JLabel("Password: ");
        welcome = new JLabel("Welcome, please enter your credentials");
        JBox b =
                JBox.vbox(
                        JBox.vglue(),
                        JBox.hbox( JBox.hglue() , welcome , JBox.hglue() ),
                        JBox.vglue(),
                        JBox.hbox(JBox.hglue(),userNamePrompt,userName,JBox.hglue(),passWordPrompt,passWord,JBox.hglue()),
                        JBox.vglue(),
                        JBox.hbox(JBox.hglue(), loginEnter, JBox.hglue() ),
                        JBox.vglue(),
                        JBox.hbox(JBox.hglue(), createNewUser,JBox.hglue()),
                        JBox.vglue()
                        );
        b.setSize(userName, 80,20);
        b.setSize(passWord, 80,20);
        b.setSize(loginEnter,150,50);
        b.setSize(createNewUser, 150,50);
        w.add(b);
        w.setVisible(true);
    }

    private void getInfoFromGui(){
        this.userNameString=userName.getText();
        this.passWordString=passWord.getText();
        userName.setText("");
        passWord.setText("");
    }

    private boolean LoginChecker(String user, String pass){
        String sql = "SELECT * FROM credentials where UPPER(username) LIKE UPPER('"+user+"')";

        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:database/login.db");
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    String passFromDb = rs.getString("password");
                    if (passFromDb.equals(pass)) {
                        return true;
                    }
                    else {
                        return false;
                    }

            }

        catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }

    }

    private void enterNewUser(){
            String sql = "INSERT INTO credentials (username, password)\n"
                    + "VALUES ('" + this.userNameString + "','" + this.passWordString + "');";
            db.executeSQL(sql);

    }

    private void goodLogin(){
        w.dispose();
        FunctionsWindow fw = new FunctionsWindow(userNameString);
    }

    private void badLogin(){
        welcome.setText("Sorry the Username or Password was Incorrect, Please try again");
    }


}
