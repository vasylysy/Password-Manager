package src;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

import java.util.Base64;
import java.util.Random;
import java.util.stream.Collectors;


public class Main extends Application {
    Stage window;
    Scene logowanie, rejestracja, przypomnij, logged, dodawan, wyswietlanie;

    String login = null;
    String[][] dane;
    TextArea textArea = new TextArea("");

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        //Scene1

        Label loginLbl = new Label();
        loginLbl.setText("Login:");

        TextField loginTxt = new TextField();
        loginTxt.setPrefSize(120,30);
        loginTxt.setMaxSize(120,30);
        loginTxt.setMinSize(120,30);

        Label hasloLbl = new Label();
        hasloLbl.setText("Hasło:");

        PasswordField hasloTxt = new PasswordField();
        hasloTxt.setPrefSize(120,30);
        hasloTxt.setMaxSize(120,30);
        hasloTxt.setMinSize(120,30);

        Hyperlink przypomnijLnk = new Hyperlink();
        przypomnijLnk.setText("Przypomnij hasło");
        przypomnijLnk.setOnAction(e -> window.setScene(przypomnij));

        Button zalogujBtn = new Button();
        zalogujBtn.setText("Zaloguj się");

        zalogujBtn.setOnAction(event -> {
            if(dbLogowanie(loginTxt.getText(), encrypt(hasloTxt.getText()))) {
                login = loginTxt.getText();
                dane = createTable(countRows(getId(login)), getId(login));
                loginTxt.setText("");
                hasloTxt.setText("");
                window.setScene(logged);
            }
        });

        Button zarejestrujBtn = new Button();
        zarejestrujBtn.setText("Zarejestruj się");
        zarejestrujBtn.setOnAction(e -> window.setScene(rejestracja));


        VBox layout1 = new VBox(5);
        layout1.setAlignment(Pos.CENTER);
        layout1.getChildren().addAll(loginLbl, loginTxt, hasloLbl, hasloTxt, przypomnijLnk, zalogujBtn, zarejestrujBtn);
        logowanie = new Scene(layout1, 600, 400);

        //rejestracja

        Label loginLblR = new Label();
        loginLblR.setText("Nowy login:");


        TextField loginTxtR = new TextField();
        loginTxtR.setPrefSize(120,30);
        loginTxtR.setMaxSize(120,30);
        loginTxtR.setMinSize(120,30);

        Label hasloLblR = new Label();
        hasloLblR.setText("Nowe hasło:");

        PasswordField hasloTxtR = new PasswordField();
        hasloTxtR.setPrefSize(120,30);
        hasloTxtR.setMaxSize(120,30);
        hasloTxtR.setMinSize(120,30);

        Label powtHasloLblR = new Label();
        powtHasloLblR.setText("Powtórz hasło:");

        PasswordField powtHasloTxtR = new PasswordField();
        powtHasloTxtR.setPrefSize(120,30);
        powtHasloTxtR.setMaxSize(120,30);
        powtHasloTxtR.setMinSize(120,30);

        TextField generujHl = new TextField();
        generujHl.setPrefSize(120,30);
        generujHl.setMaxSize(120,30);
        generujHl.setMinSize(120,30);

        Button generujH = new Button();
        generujH.setText("Generuj hasło");
        generujH.setOnAction(event -> {
            String pass = new Random().ints(10, 33, 122).mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
            generujHl.setText(pass);
        });

        TextField nazwiskoTxtR = new TextField();
        nazwiskoTxtR.setPrefSize(120,30);
        nazwiskoTxtR.setMaxSize(120,30);
        nazwiskoTxtR.setMinSize(120,30);

        Label nazwiskoLblR = new Label();
        nazwiskoLblR.setText("Nazwisko panienskie:");

        Button zarejestrujBtnR = new Button();
        zarejestrujBtnR.setText("Zarejestruj się");
        zarejestrujBtnR.setOnAction(event -> {
            if (loginTxtR.getText().length() > 0 && nazwiskoTxtR.getText().length() > 0 && hasloTxtR.getText().length() > 5 && hasloTxtR.getText().compareTo(powtHasloTxtR.getText()) == 0) {
                dbRejestracja(loginTxtR.getText(), encrypt(hasloTxtR.getText()), encrypt(nazwiskoTxtR.getText()));
                loginTxtR.setText("");
                hasloTxtR.setText("");
                powtHasloTxtR.setText("");
                nazwiskoTxtR.setText("");
                window.setScene(logowanie);
            }
        });

        VBox layout2 = new VBox(5);
        layout2.setAlignment(Pos.CENTER);
        layout2.getChildren().addAll(loginLblR, loginTxtR, hasloLblR, hasloTxtR, powtHasloLblR, powtHasloTxtR, nazwiskoLblR, nazwiskoTxtR, zarejestrujBtnR, generujHl, generujH);
        rejestracja = new Scene(layout2, 600, 400);


        //przypomnij

        Label przypomn1L = new Label();
        przypomn1L.setText("Nazwisko panienskie:");

        TextField przypomn1T = new TextField();
        przypomn1T.setPrefSize(120,30);
        przypomn1T.setMaxSize(120,30);
        przypomn1T.setMinSize(120,30);

        Label przypomn2L = new Label();
        przypomn2L.setText("Twoje hasło to:");

        TextField przypomn2T = new TextField();
        przypomn2T.setPrefSize(120,30);
        przypomn2T.setMaxSize(120,30);
        przypomn2T.setMinSize(120,30);

        Button sprawdz = new Button();
        sprawdz.setText("Przypomnij");
        sprawdz.setOnAction(event -> przypomn2T.setText(dbPrzypomnienie(encrypt(przypomn1T.getText()))));

        Button backLogin = new Button();
        backLogin.setText("Wróc do logowania");
        backLogin.setOnAction(e -> {
            przypomn1T.setText("");
            przypomn2T.setText("");
            window.setScene(logowanie);
        });

        VBox layout3 = new VBox(5);
        layout3.setAlignment(Pos.CENTER);
        layout3.getChildren().addAll(przypomn1L, przypomn1T, przypomn2L, przypomn2T, sprawdz, backLogin);
        przypomnij = new Scene(layout3, 600, 400);


        //logged

        Button wylogujse = new Button();
        wylogujse.setText("Wyloguj się");
        wylogujse.setOnAction(e -> window.setScene(logowanie));

        Button dodawanie = new Button();
        dodawanie.setText("Dodaj");
        dodawanie.setOnAction(e -> window.setScene(dodawan));

        Button wyswietl = new Button();
        wyswietl.setText("Wyświetl");
        wyswietl.setOnAction(e ->{
            for(int i = 1; i < countRows(getId(login))+1; i++) {
                textArea.appendText("\n"+i+"   ");
                textArea.appendText(dane[i-1][0]+"  --  "+dane[i-1][1]+"  --  "+dane[i-1][2]);
            }
            window.setScene(wyswietlanie);
        });

        VBox layout4 = new VBox(5);
        layout4.getChildren().addAll(wylogujse, dodawanie, wyswietl);
        layout4.setAlignment(Pos.TOP_RIGHT);
        layout4.setPadding(new Insets(20));
        logged = new Scene(layout4, 600, 400);


        //dodawanie

        Label doczegoL = new Label();
        doczegoL.setText("Do czego:");

        TextField doczego = new TextField();
        doczego.setPrefSize(120,30);
        doczego.setMaxSize(120,30);
        doczego.setMinSize(120,30);

        Label imieL = new Label();
        imieL.setText("Imie użytkownika:");

        TextField imie = new TextField();
        imie.setPrefSize(120,30);
        imie.setMaxSize(120,30);
        imie.setMinSize(120,30);

        Label hasLoL = new Label();
        hasLoL.setText("Hasło:");

        PasswordField hasLo = new PasswordField();
        hasLo.setPrefSize(120,30);
        hasLo.setMaxSize(120,30);
        hasLo.setMinSize(120,30);

        Label powthasLoL = new Label();
        powthasLoL.setText("Powtórz hasło:");

        PasswordField powthasLo = new PasswordField();
        powthasLo.setPrefSize(120,30);
        powthasLo.setMaxSize(120,30);
        powthasLo.setMinSize(120,30);

        Button dodaj = new Button();
        dodaj.setText("Dodaj");
        dodaj.setOnAction(event -> {
            if (hasLo.getText().compareTo(powthasLo.getText()) == 0 && hasLo.getText().length()>0) {
                int id = getId(login);
                pushData(id, doczego.getText(), imie.getText(), encrypt(hasLo.getText()));
                dane = createTable(countRows(getId(login)), getId(login));
                textArea.setText("");
            }
            doczego.setText("");
            imie.setText("");
            hasLo.setText("");
            powthasLo.setText("");
        });

        Button wroc = new Button();
        wroc.setText("Wróć");
        wroc.setOnAction(e -> {
            window.setScene(logged);
            textArea.setText(" ");
        });

        VBox layout5 = new VBox(5);
        layout5.getChildren().addAll(doczegoL, doczego, imieL, imie, hasLoL, hasLo, powthasLoL, powthasLo, dodaj, wroc);
        layout5.setAlignment(Pos.TOP_RIGHT);
        layout5.setPadding(new Insets(20));
        dodawan = new Scene(layout5, 600, 400);

        //wyswietlanie

        Button wrocc = new Button();
        wrocc.setText("Wróć");
        wrocc.setOnAction(e -> {
            textArea.setText(" ");
            window.setScene(logged);
        });

        textArea.setEditable(false);

        VBox layout6 = new VBox(5);
        layout6.getChildren().addAll(textArea, wrocc);
        layout6.setAlignment(Pos.TOP_RIGHT);
        layout6.setPadding(new Insets(20));
        wyswietlanie = new Scene(layout6, 600, 400);

        //render

        window.setScene(logowanie);
        window.setTitle("PassKeeper");
        window.show();
    }




    public static void main(String[] args) {
        launch(args);
    }


    public static boolean dbLogowanie(String login, String password){
        Connection c;
        Statement stmt;
        boolean bool = false;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "1234");

            System.out.println("Opened database successfully");

            stmt = c.createStatement();


            ResultSet rs = stmt.executeQuery( "SELECT LOGIN, PASSWORD FROM USERS_DATA;" );
            while (rs.next()){
                String s1 = rs.getString("login");
                s1 = s1.replaceAll(" ", "");
                String s2 = rs.getString("password");
                s2 = s2.replaceAll(" ", "");
                if (login.compareTo(s1) == 0 && password.compareTo(s2) == 0) {
                    bool = true;
                    break;
                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return bool;
    }

    private void dbRejestracja(String login, String password, String answer){
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "1234");

            System.out.println("Opened database successfully");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO USERS_DATA (LOGIN,PASSWORD,ANSWER) "
                    + "VALUES ( '" + login +"','"+  password +"','"+ answer+ "');";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");

    }

    public static String dbPrzypomnienie(String answer){
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "1234");

            System.out.println("Opened database successfully");

            stmt = c.createStatement();


            ResultSet rs = stmt.executeQuery( "SELECT  PASSWORD, ANSWER FROM USERS_DATA;" );
            while (rs.next()){
                String s1 = rs.getString("answer");
                s1 = s1.replaceAll(" ", "");
                String s2 = rs.getString("password");
                s2 = s2.replaceAll(" ", "");
                if (answer.compareTo(s1) == 0) {
                    System.out.println("password found");
                    return decrypt(s2);
                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return "DOES NOT EXIST";
    }

    public static int getId(String login){
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "1234");

            System.out.println("Opened database successfully");

            stmt = c.createStatement();


            ResultSet rs = stmt.executeQuery( "SELECT  ID, LOGIN FROM USERS_DATA;" );
            while (rs.next()){
                String s = rs.getString("login");
                s = s.replaceAll(" ", "");
                int id = rs.getInt("id");
                if (login.compareTo(s) == 0) {
                    System.out.println("id found");
                    return id;
                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return 0;
    }

    private int countRows(int id){
        int k = 0;
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "1234");

            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery( "SELECT  ID FROM ACC_DATA;" );
            while (rs.next()){
                int n = rs.getInt("id");
                if (id == n)
                    k++;
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        System.out.println("Operation done successfully");
        return k;
    }

    private void pushData(int id, String hint, String login, String password){
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "1234");

            System.out.println("Opened database successfully");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO ACC_DATA (ID, HINT, LOGIN, PASSWORD) "
                    + "VALUES ( '" + id +"','"+  hint +"','"+ login + "','"+  password +"');";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");

    }


    private String[][] createTable(int rows, int id){
        String[][] dane = new String[rows][3];
        int k = 0;
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            "postgres", "1234");

            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery( "SELECT  ID, HINT, LOGIN, PASSWORD FROM ACC_DATA;" );
            while (rs.next()){
                int n = rs.getInt("id");
                String hint = rs.getString("hint");
                hint = hint.replaceAll(" ", "");
                String login = rs.getString("login");
                login = login.replaceAll(" ", "");
                String password = rs.getString("password");
                password = password.replaceAll(" ", "");
                if (id == n){
                    dane[k][0] = hint;
                    dane[k][1] = login;
                    dane[k][2] = decrypt(password);
                    k++;
                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        System.out.println("Operation done successfully");

        return dane;
    }


    private static final String secretKey = "go!go!Na`Vi!";

    private static final String salt = "pepperAS";

    public static String encrypt(String strToEncrypt) {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

}
