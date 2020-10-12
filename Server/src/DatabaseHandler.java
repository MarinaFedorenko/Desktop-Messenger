import java.sql.*;
import java.util.StringTokenizer;

public class DatabaseHandler extends Configs{
    public int id;
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException{
        // строка подключения к бд
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort+ "/" + dbName+ "?verifyServerCertificate=false"+
                "&useSSL=false" +
                "&requireSSL=false"+
                "&useLegacyDatetimeCode=false"+
                "&amp" +
                "&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");


        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    //метод для записи пользователя в бд
    public Boolean createUser(String str){
        int result = 0;
        StringTokenizer string = new StringTokenizer(str, "#");
        String username = string.nextToken();
        String firstname = string.nextToken();
        String lastname = string.nextToken();
        String password = string.nextToken();
        String location = string.nextToken();
        String gender = string.nextToken();
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +Const.USER_USERNAME+","+
                Const.USER_FIRSTNAME + "," + Const.USER_LASTNAME + "," + Const.USER_PASSWORD + ","+
                Const.USER_LOCATION + "," + Const.USER_GENDER + ")" +
                "VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, username);
            prSt.setString(2, firstname);
            prSt.setString(3, lastname);
            prSt.setString(4, password);
            prSt.setString(5, location);
            prSt.setString(6, gender);
            result = prSt.executeUpdate();//записать в бд
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(result!=0) return true;
        else return false;

    }

    public Boolean createMessage(String str){
        int result = 0;
        StringTokenizer string = new StringTokenizer(str, "#");
        String sender = string.nextToken();
        String receiver = string.nextToken();
        String text = string.nextToken();
        String time = string.nextToken();
        String date = string.nextToken();

        String insert = "INSERT INTO " + Const.MESSAGE_TABLE + "(" +
                Const.MESSAGE_SENDER + "," + Const.MESSAGE_RECEIVER + "," +
                Const.MESSAGE_TEXT + "," + Const.MESSAGE_TIME + ","+
                Const.MESSAGE_DATE + ")" +
                "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, sender);
            prSt.setString(2, receiver);
            prSt.setString(3, text);
            prSt.setString(4, time);
            prSt.setString(5, date);
            result =  prSt.executeUpdate();//записать в бд
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(result!=0) return true;
        else return false;

    }


    //считать все данные пользователя
    public ResultSet getUser(String str){

        StringTokenizer string = new StringTokenizer(str, "#");
        String username = string.nextToken();
        String password = string.nextToken();

        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USER_USERNAME + "=? AND " + Const.USER_PASSWORD + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, username);
            prSt.setString(2, password);

            resSet = prSt.executeQuery();//взять результативные строки с бд
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet findUser(String username){


        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USER_USERNAME + "=?  ";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, username);

            resSet = prSt.executeQuery();//взять результативные строки с бд
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet checkNewMessage(String str) throws SQLException, ClassNotFoundException {//по индексу сравнить, если


//        String select1 = "SELECT * FROM " + Const.MESSAGE_TABLE + " ORDER BY " + Const.MESSAGE_ID+ " DESC LIMIT 0, 1";
//        PreparedStatement prStt = getDbConnection().prepareStatement(select1);
//        ResultSet result = prStt.executeQuery();//взять результативные строки с бд
//       result.next();
//        this.id = result.getInt("message_id");
//        System.out.println("Max ID = "+ id);

        String receiver = str;// строки где я получатель
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.MESSAGE_TABLE + " WHERE " +
                Const.MESSAGE_RECEIVER + "=? OR " + Const.MESSAGE_SENDER+ "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, receiver);
            prSt.setString(2, receiver);
            //prSt.setInt(2, id);

            resSet = prSt.executeQuery();//взять результативные строки с бд
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

}
