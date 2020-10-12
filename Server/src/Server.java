import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;
import javafx.fxml.FXML;
import javafx.stage.StageStyle;


public class Server extends Application {
    @FXML
    private Button btn_finish;


    @FXML
    public static ScrollPane scroll_field;

    @FXML
    public static VBox dialog_field;


    @FXML
    private Label lbl_username;

    @FXML
    private Label lbl_ip;

    @FXML
    private Label lbl_port;

    static Vector<String> onlineUsers = new Vector<>();
    static Vector<String> onlineIP = new Vector<>();
    static  Vector<Integer> onlinePort = new Vector<>();
    public static String CREATE = "create";
    public static String LOGIN = "login";
    public static String TRUE = "true";
    public static String FALSE = "false";
    public static String FIND = "find";
    public static String EXIT = "exit";
    public static String SEND_MESSAGE = "send_message";
    public static String GET_MESSAGE = "get_message";
    private static boolean isWorking = true;
    private static String username="";
    private static  String ip="";
    private static int port = 0;

    private double xOffset = 0;
    private double yOffset = 0;


    @FXML
    void initialize() {
        btn_finish.setOnAction(event->{
            if(onlineUsers.isEmpty()){// если нет подключенных пользователей
                isWorking = false;
                Platform.exit();
                System.exit(0);

            }

        });

    }

    public static void main(String[] args) {launch(args);}

public void Connection (Button button) throws IOException {
        ServerSocket server = new ServerSocket(8000);
        System.out.println("Сервер запущен...");
        // ServerSocket имеет маскимум соединений по умолчанию 50
            Socket socket;
            //username = "";
            //поменяла местами try and while
                while(true) {

                    if(!isWorking) break; // закрываю сервер
                    socket = server.accept();

                    System.out.println("Client is connected: " + socket);
                    DataInputStream reader = new DataInputStream(socket.getInputStream());
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());


                        Socket finalSocket = socket;
                        DatabaseHandler dbhandler = new DatabaseHandler();

                    Socket finalSocket1 = socket;
                    Thread request = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String input = "";
                                    //while(true){
                                    //if(reader!=null) {
                                        input = reader.readUTF();
                                        StringTokenizer string = new StringTokenizer(input, "#");
                                        String temp = string.nextToken();//запоминаю ключевое слово
                                        input = input.replace(temp + "#", "");//удаляю из строки ключевое слово

                                        if (temp.equals(CREATE)) {
                                            String username = string.nextToken();
                                            ResultSet result = dbhandler.findUser(username);// есть ли в бд user с таким же именем
                                            int counter = 0;
                                            while (true) {
                                                try {
                                                    if (!result.next()) break;
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
                                                counter++;
                                            }
                                            if (counter >= 1) {// есть, значит нужно другое имя придумать
                                                writer.writeUTF(CREATE+"#"+FALSE);
                                            }
                                            else{
                                                Boolean ok = dbhandler.createUser(input);//создаю в бд новый кортеж
                                                if(ok){
                                                    writer.writeUTF(CREATE+"#"+TRUE);
                                                    Server.username = username;//временно запоминаю username пользователя
                                                    Server.onlineUsers.add(username);
                                                    Server.onlineIP.add(finalSocket1.getInetAddress().toString());
                                                    Server.onlinePort.add(finalSocket1.getPort());
                                                }
                                                else writer.writeUTF(CREATE+"#"+FALSE);
                                            }
                                        }
                                        else if (temp.equals(LOGIN)) {
                                            String username = string.nextToken();
                                            ResultSet result = dbhandler.getUser(input);
                                            int counter = 0;
                                            while (true) {
                                                try {
                                                    if (!result.next()) break;
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
                                                counter++;
                                            }
                                            if (counter >= 1) {
                                                System.out.println("Client username logined: " + username);
                                                if(Server.alreadyExist(username)){ System.out.println("Такой пользователь уже онлайн!!!");
                                                    writer.writeUTF(LOGIN+"#"+FALSE); // если такой user уже онлайн
                                                }
                                                else {
                                                    writer.writeUTF(LOGIN+"#"+TRUE);
                                                    Server.username = username;
                                                    Server.onlineUsers.add(username);
                                                    Server.onlineIP.add(finalSocket1.getInetAddress().toString());
                                                    Server.onlinePort.add(finalSocket1.getPort());
                                                }
                                            }
                                            else {
                                                writer.writeUTF(LOGIN+"#"+FALSE);// если такой user не существует
                                                System.out.println("Client username not logined: " + username);
                                            }
                                        }

                                        else if (temp.equals(EXIT)) {
                                            writer.writeUTF(EXIT+"#"+TRUE);
                                            finalSocket.close();
                                            System.out.println("Клиент должен stop ");
                                            String username = input;
                                            for (int i = 0; i < Server.onlineUsers.size(); i++) {
                                                if (Server.onlineUsers.get(i).equals(username)) {
                                                    Server.onlineUsers.remove(i);
                                                    Server.onlineIP.remove(i);
                                                    Server.onlinePort.remove(i);
//                                                    int finalI = i;
//                                                    Platform.runLater(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            // в серверном приложениии удалим лейбл оотв этому пользователю
//                                                            dialog_field.getChildren().remove(finalI);
//                                                        }
//                                                    });
                                                    System.out.println("Пока");
                                                    break;
                                                }
                                            }
                                        }
                                        else if (temp.equals(SEND_MESSAGE)) {
                                          // synchronized (dbhandler){// запрос в бд может делать только один объект одновременно,
                                            Boolean ok = dbhandler.createMessage(input);
                                            if (ok) {
                                                String myUsername = string.nextToken(); // send_message(ключевое слово)#sender(я)#receiver#text#time#date
                                                ResultSet result = dbhandler.checkNewMessage(myUsername);// где я отправитель и я получатель

                                                writer.writeUTF(SEND_MESSAGE + "#" + TRUE);
                                            } else {
                                                writer.writeUTF(SEND_MESSAGE + "#" + FALSE);
                                            }
                                          // }
                                        }
                                        // вектор string(история сообщений, ult date = сегодня)<- set (every count of resultset(concat with "#"))
                                          //  writer.writeChars(строка, размер вектора/\ все/\эл-ты/\вектора)
                                        else if (temp.equals(FIND)) {
                                            //synchronized (dbhandler) {
                                                ResultSet result = dbhandler.findUser(input);
                                                int counter = 0;
                                                while (true) {
                                                    try {
                                                        if (!result.next()) break;
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    counter++;
                                                }
                                                if (counter >= 1) {
                                                    writer.writeUTF(FIND + "#" + TRUE);
                                                } else {
                                                    writer.writeUTF(FIND + "#" + FALSE);
                                                }
                                            //}
                                        }
                                        else if(temp.equals(GET_MESSAGE)) {
                                           // synchronized (dbhandler) {
                                            System.out.println("Ты хочешь получить смс; Ты : "+ input);
                                                ResultSet result = dbhandler.checkNewMessage(input);//input = my username(I am receiver)
                                                int counter = 0;
                                                String s = temp;
                                                String ss ="";
                                                while (true) {
                                                    try {
                                                        if (!result.next()) break;
                                                        ss += "#";
                                                        ss += result.getString("sender");
                                                        ss+="#";
                                                        ss+= result.getString("receiver");
                                                        ss += "#";
                                                        ss += result.getString("text");
                                                        ss += "#";
                                                        ss += result.getString("time");

                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    counter++;
                                                }
                                                s+="#"+ Integer.toString(counter);
                                                s+=ss;
                                                    if (counter >= 1) {
                                                        writer.writeUTF(s);
                                                    } else writer.writeUTF(FALSE);
                                           // }
                                        }


//                                    Label newUser = new Label();
////                                    Platform.runLater(new Runnable() {
////                                        @Override
////                                        public void run() {
//                                            if(Server.username!="" && !onlineUsers.isEmpty()){
//                                                //String window = button.getScene().getWindow().toString();
//                                                FXMLLoader loader = new FXMLLoader();
//                                                loader.setLocation(getClass().getResource("server.fxml"));
//                                                loader.setRoot(this);
//                                                Server controller = loader.getController();
//                                                for(int i=0; i<onlineUsers.size(); i++){
//                                                    if(onlineUsers.get(i).equals(Server.username)){//если есть в списке создаю новый label
//                                                        String uname = onlineUsers.get(i);
//                                                        Server.username = "";
//                                                        String ip = Server.onlineIP.get(i);
//                                                        String port = Server.onlinePort.get(i).toString();
//                                                       // Label newUser = new Label();
//                                                        newUser.setText(uname);
//                                                        newUser.setStyle("-fx-border-color: #F0E68C; -fx-border-radius: 5; -fx-background-color: #FFFFFF");
//                                                        System.out.println("Создала label with username :"+ uname);
//                                                        dialog_field.getChildren().add(newUser);
//                                                        scroll_field.setContent(dialog_field);
////                                                        newUser.setOnMouseClicked(event -> {
//                                                            lbl_username.setText(uname);
//                                                            lbl_ip.setText(ip);
//                                                            lbl_port.setText(port);
////                                                        });
//
//
//                                                    }
//
//                                                }
//
//                                            }
//
////                                        }
//                                    });



//                                    }
//                                    else{
//                                        System.out.println("Вводной поток пустой((((");
//                                    }



                                    // }
                                }catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    writer.close();
                                    reader.close();
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        request.start();







        }
}



    public static Boolean alreadyExist(String username){
    for(int i=0; i<onlineUsers.size(); i++){
        if(onlineUsers.get(i).equals(username)){
            return true;
        }
    }
    return false;
}

    @Override
    public void start(Stage primaryStage) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("server.fxml"));
                Scene scene = new Scene(root);
                primaryStage.initStyle(StageStyle.TRANSPARENT);
                primaryStage.setScene(scene);
                primaryStage.initStyle(StageStyle.UNDECORATED);
                scene.setFill(Color.TRANSPARENT);

                root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
                });
                root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        primaryStage.setX(event.getScreenX() - xOffset);
                        primaryStage.setY(event.getScreenY() - yOffset);
                    }
                });

                //создать поток ожидающий ок == фолс чтобы закрыть это окно
                //или платформ.экзит
                primaryStage.setScene(scene);
                primaryStage.show();
                new Thread(()->{
                    try {
                        Connection(btn_finish);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();


            } catch(IOException e){
                e.printStackTrace();
            }



    }


}


