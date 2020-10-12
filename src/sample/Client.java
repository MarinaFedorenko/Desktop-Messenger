package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import static java.awt.Color.GREEN;

public class Client extends Application {
    //sample.fxml
    @FXML
    private Pane chat1;
    @FXML
    private Label lbl_user1;
    @FXML
    private Label lbl_message1;
    @FXML
    private Pane chat2;
    @FXML
    private Label lbl_user2;
    @FXML
    private Label lbl_message2;
    @FXML
    private Pane chat3;
    @FXML
    private Label lbl_user3;
    @FXML
    private Label lbl_message3;
    @FXML
    private Pane chat4;
    @FXML
    private Label lbl_user4;
    @FXML
    private Label lbl_message4;
    @FXML
    private Pane chat5;
    @FXML
    private Label lbl_user5;
    @FXML
    private Label lbl_message5;
    @FXML
    private Pane chat6;
    @FXML
    private Label lbl_user6;
    @FXML
    private Label lbl_message6;
    @FXML
    private Pane chat7;
    @FXML
    private Label lbl_user7;
    @FXML
    private Label lbl_message7;

    @FXML
    private Button btnSearch;

    @FXML
    private TextField search_field;
    @FXML
    private Button btn_send;
    @FXML
    private TextField sendMessage_field;
    @FXML
    private Label lbl_receiver;
    @FXML
    private Label lbl_sender;
    @FXML
    private MenuButton btn_more;
    @FXML
    private Pane dialog_field;
    @FXML
    private MenuItem btn_clearHistory;
    @FXML
    private MenuItem btn_deleteChat;
    @FXML
    private Label lbl_online;
    @FXML
    private ScrollPane scroll_field;
    //sample.fxml

    private double xOffset = 0;
    private double yOffset = 0;

    private static String ip = "127.0.0.1";
    private static int port = 8000;

    private String sender = "";
    //private String receiver = "";

    public static String CREATE = "create";
    public static String LOGIN = "login";
    public static String FIND = "find";
    public static String SEND_MESSAGE = "send_message";
    public static String GET_MESSAGE = "get_message";
    public static String TRUE = "true";
    public static String FALSE = "false";

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");//04.07.2001
    DateFormat timeFormat = new SimpleDateFormat("h:mm a");//12:08 PM
    private Vector<String> history1;
    private Vector<String> history2;
    private Vector<String> history3;
    private Vector<String> history4;
    private Vector<String> history5;
    private Vector<String> history6;
    private Vector<String> history7;


    protected String toServer;
    protected String username;
    private int me = 0;//счётчик нажатый на lbl_sender
    protected Boolean online;
    protected Button butt;
    //protected String newMessage;//мне пришло новое смс от кого-то

    public Client() {
        this.toServer = "";
        this.username = "";
        this.online = false;
        this.history1 = new Vector<>();
        this.history2 = new Vector<>();
        this.history3 = new Vector<>();
        this.history4 = new Vector<>();
        this.history5 = new Vector<>();
        this.history6 = new Vector<>();
        this.history7 = new Vector<>();

    }

    public void setUsername(String username) {
        this.username = username;
        this.sender = username;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    void initialize() {
        //Это всё поток JavaFX
        lbl_sender.setOnMouseClicked(event -> {
            me++;
            if (me == 1) {
                lbl_sender.setText(this.username);
                lbl_sender.setStyle("-fx-border-color: #F0E68C; -fx-border-radius: 5");

            } else {
                lbl_sender.setText("");
                lbl_sender.setStyle("-fx-border-color: #454545");
                me = 0;
            }
        });

        btn_send.setOnAction(event -> {
//            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");//04.07.2001
//            DateFormat timeFormat = new SimpleDateFormat("h:mm a");//12:08 PM
            Date data = new Date();
            String time = timeFormat.format(data);
            String date = dateFormat.format(data);

            String message = sendMessage_field.getText();//в строку записываю сообщение
            String receiver = lbl_receiver.getText();
            if (!receiver.equals("") && !message.trim().equals("")) {//если есть получатель и сообщение не пустое
                butt = btn_send;
                toServer = SEND_MESSAGE + "#" + sender + "#" + receiver + "#" + message + "#" + time + "#" + date;
                System.out.println("Сообщение: " + toServer);
                try {
                    Connection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                sendMessage_field.setStyle("-fx-border-color: red");
            }//иначе тектовое поле станет красным

        });

        btn_clearHistory.setOnAction(event -> {
            dialog_field.getChildren().clear();
            int temp = comparison(lbl_receiver.getText());
            switch (temp) {
                case 1: {
                    lbl_message1.setText("");
                    break;
                }
                case 2: {
                    lbl_message2.setText("");
                    break;
                }
                case 3: {
                    lbl_message3.setText("");
                    break;
                }
                case 4: {
                    lbl_message4.setText("");
                    break;
                }
                case 5: {
                    lbl_message5.setText("");
                    break;
                }
                case 6: {
                    lbl_message6.setText("");
                    break;
                }
                case 7: {
                    lbl_message7.setText("");
                    break;
                }
                default:
                    break;
            }
        });
        btn_deleteChat.setOnAction(event -> {
            int temp = comparison(lbl_receiver.getText());
            switch (temp) {
                case 1: {
                    clearChatAway(lbl_user1, lbl_message1);
                    break;
                }
                case 2: {
                    clearChatAway(lbl_user2, lbl_message2);
                    break;
                }
                case 3: {
                    clearChatAway(lbl_user3, lbl_message3);
                    break;
                }
                case 4: {
                    clearChatAway(lbl_user4, lbl_message4);
                    break;
                }
                case 5: {
                    clearChatAway(lbl_user5, lbl_message5);
                    break;
                }
                case 6: {
                    clearChatAway(lbl_user6, lbl_message6);
                    break;
                }
                case 7: {
                    clearChatAway(lbl_user7, lbl_message7);
                    break;
                }
                default:
                    break;
            }
        });
        btnSearch.setOnAction(event -> {
            String friendUserName = search_field.getText().trim();
            if (friendUserName.equals("")) {
                System.out.println("Поле поиска пустое!");
                search_field.setStyle("-fx-border-color: red");
                search_field.setPromptText("Write friend's username...");
            } else {
                search_field.clear();
                search_field.setStyle("");
                search_field.setPromptText("Search...");
                butt = btnSearch;
                toServer = FIND + "#" + friendUserName;
                try {
                    Connection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });
//        dialog_field.setOnMouseClicked(event -> {
//            int temp = comparison(lbl_receiver.getText());
//            getHistory(temp);
//        });

        chat1.setOnMouseClicked(event -> {
            chatsValidate(lbl_user1);
        });
        chat2.setOnMouseClicked(event -> {
            chatsValidate(lbl_user2);
        });
        chat3.setOnMousePressed(event -> {
            chatsValidate(lbl_user3);
        });
        chat4.setOnMousePressed(event -> {
            chatsValidate(lbl_user4);
        });
        chat5.setOnMousePressed(event -> {
            chatsValidate(lbl_user5);
        });
        chat6.setOnMousePressed(event -> {
            chatsValidate(lbl_user6);
        });
        chat7.setOnMousePressed(event -> {
            chatsValidate(lbl_user7);
        });

    }

    private void clearChatAway(Label label1, Label label2) {
        label1.setText("");
        label2.setText("");
        lbl_receiver.setText("");
        dialog_field.getChildren().clear();
        btn_more.setVisible(false);
        btn_send.setVisible(false);
    }

    private void chatsValidate(Label label) {
        if (label.getText().equals("")) {//если выбираю пустой чат -> всё очищаю
            lbl_receiver.setText("");
            btn_more.setVisible(false);
            btn_send.setVisible(false);
            dialog_field.getChildren().clear();

        } else if (lbl_receiver.getText().equals(label.getText())) {// если выбираю уже открытый чат
            btn_more.setVisible(true);
            btn_send.setVisible(true);
            toServer = GET_MESSAGE + "#" + username;//получить новый смс
            try {
                Connection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!label.getText().equals("") && !label.equals(lbl_receiver.getText())) {//если я хочу перейти на другой чат
            lbl_receiver.setText(label.getText());
            int temp = comparison(label.getText());
            getHistory(temp);
            btn_more.setVisible(true);
            btn_send.setVisible(true);
//
//            toServer = GET_MESSAGE + "#" + username;//получить новый смс
//            try {
//                Connection();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }

    private int comparison(String string) {
        if (string.equals("")) return -1;
        else if (string.equals(lbl_user1.getText()))
            return 1;
        else if (string.equals(lbl_user2.getText()))
            return 2;
        else if (string.equals(lbl_user3.getText()))
            return 3;
        else if (string.equals(lbl_user4.getText()))
            return 4;
        else if (string.equals(lbl_user5.getText()))
            return 5;
        else if (string.equals(lbl_user6.getText()))
            return 6;
        else if (string.equals(lbl_user7.getText()))
            return 7;
        else return 0;

    }

    public void addNewFriend(String friendUsername, String lastMessage, Boolean b) {//здесь lastMessage уже собран: L/R#message#time
        if (b) {
            search_field.setText("");
            int temp = comparison(friendUsername);//from user3 temp = 3
            switch (temp) {
                case 0: {// если нет чата с этим человеком, то я создам его и внесу туда всю инфу
                    // find clean chat box -> create new chat
                    getHistory(temp);
                    int templ = 1;
                    switch (templ) {
                        case 1:
                            if (lbl_user1.getText().equals("")) {
                                lbl_user1.setText(friendUsername);
                                chatsValidate(lbl_user1);
//                                lbl_message1.setText(lastMessage);
//                                lbl_message1.setTextFill(Color.GREEN);
                                break;
                            } else templ++;
                        case 2:
                            if (lbl_user2.getText().equals("")) {
                                lbl_user2.setText(friendUsername);
                                chatsValidate(lbl_user2);
//                                lbl_message2.setText(lastMessage);
//                                lbl_message2.setTextFill(Color.GREEN);
                                getHistory(temp);
                                break;
                            } else templ++;
                        case 3:
                            if (lbl_user3.getText().equals("")) {
                                lbl_user3.setText(friendUsername);
                                chatsValidate(lbl_user3);
//                                lbl_message3.setText(lastMessage);
//                                lbl_message3.setTextFill(Color.GREEN);
                                setHistory(temp, lastMessage);// lastmessage: L/R#message#time
                                break;
                            } else templ++;
                        case 4:
                            if (lbl_user4.getText().equals("")) {
                                lbl_user4.setText(friendUsername);
                                chatsValidate(lbl_user4);
//                                lbl_message4.setText(lastMessage);
//                                lbl_message4.setTextFill(Color.GREEN);
                                break;
                            } else templ++;
                        case 5:
                            if (lbl_user5.getText().equals("")) {
                                lbl_user5.setText(friendUsername);
                                chatsValidate(lbl_user5);
//                                lbl_message5.setText(lastMessage);
//                                lbl_message5.setTextFill(Color.GREEN);
                                break;
                            } else templ++;
                        case 6:
                            if (lbl_user6.getText().equals("")) {
                                lbl_user6.setText(friendUsername);
                                chatsValidate(lbl_user6);
//                                lbl_message6.setText(lastMessage);
//                                lbl_message6.setTextFill(Color.GREEN);
                                break;
                            } else templ++;
                        case 7:
                            if (lbl_user7.getText().equals("")) {
                                lbl_user7.setText(friendUsername);
                                chatsValidate(lbl_user7);
//                                lbl_message7.setText(lastMessage);
//                                lbl_message7.setTextFill(Color.GREEN);
                                break;
                            } else templ++;
                        default: {//все чаты заняты, нет места чтобы добавить новый
//                    chat = chat7;
//                    chat7 = chat6;
//                    chat6 = chat5;
//                    chat5 = chat4;
//                    chat4 = chat3;
//                    chat3 = chat2;
//                    chat2 = chat1;
//                    chat1 = chat;
//                    lbl_user1.setText(friendUserName);
                            if (lastMessage.equals("")) {// при создании чата я добавляю в эту ф-цию lastMessage="";
                                search_field.setStyle("-fx-border-color: red; ");
                                search_field.setPromptText("Delete one of the chats");
                            } else {// кто-то отправил мне смс, но у меня нет свободного места для чата с этим человеком
                                search_field.setPromptText("You have new message.");
                            }
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    chatsValidate(lbl_user1);
                    break;
                }
                case 2: {
                    chatsValidate(lbl_user2);
                    break;
                }
                case 3: {
                    chatsValidate(lbl_user3);
                    break;
                }
                case 4: {
                    chatsValidate(lbl_user4);
                    break;
                }
                case 5: {
                    chatsValidate(lbl_user5);
                    break;
                }
                case 6: {
                    chatsValidate(lbl_user6);
                    break;
                }
                case 7: {
                    chatsValidate(lbl_user7);
                    break;
                }
            }
            search_field.setStyle("");
            search_field.setPromptText("Search");
        } else {
            search_field.setStyle("-fx-border-color: red; ");
            search_field.setPromptText("No such user");
        }
    }

    private void sendingMessage(Boolean b) {
        if (b) {
            Date data = new Date();
            String time = timeFormat.format(data);
            int temp = comparison(lbl_receiver.getText());
            Text newMessage = new Text(sendMessage_field.getText());
            sendMessage_field.clear();//clear the Textfield
            newMessage.setFont(Font.font("Serif", 18));
            newMessage.setFill(Color.WHITE);

            Text newTime = new Text(time);
            newTime.setFont(Font.font("Serif", 10));
            newTime.setFill(Color.WHITE);

            // R#message#time
            String text = "R#"+sendMessage_field.getText()+"#"+time;
            setHistory(temp, sendMessage_field.getText());

//        Label newMessage = new Label();
//        newMessage.setStyle("-fx-border-color: #F0E68C; -fx-border-radius: 5; -fx-background-color: #FFFFFF");
//        newMessage.setText(sendMessage_field.getText());
            HBox hbox = new HBox();
            hbox.getChildren().add(newMessage);// set Message
            hbox.setAlignment(Pos.BASELINE_RIGHT);
            dialog_field.getChildren().add(hbox);
            HBox hhbox = new HBox();
            hhbox.getChildren().add(newTime);// set time of message
            hhbox.setAlignment(Pos.BASELINE_RIGHT);
            dialog_field.getChildren().add(hhbox);
            scroll_field.setContent(dialog_field);

            //setHistory(temp, newMessage.getText(), newTime.getText(), "R#");
            //setNewMessage(temp, newMessage.getText(), Color.WHITE);

            switch (temp) {
                case 1: {
                    lbl_message1.setText(newMessage.getText());
                    lbl_message1.setTextFill(Color.WHITE);
                    break;
                }
                case 2: {
                    lbl_message2.setText(newMessage.getText());
                    lbl_message2.setTextFill(Color.WHITE);
                    break;
                }
                case 3: {
                    lbl_message3.setText(newMessage.getText());
                    lbl_message3.setTextFill(Color.WHITE);
                    break;
                }
                case 4: {
                    lbl_message4.setText(newMessage.getText());
                    lbl_message4.setTextFill(Color.WHITE);
                    break;
                }
                case 5: {
                    lbl_message5.setText(newMessage.getText());
                    lbl_message5.setTextFill(Color.WHITE);
                    break;
                }
                case 6: {
                    lbl_message6.setText(newMessage.getText());
                    lbl_message6.setTextFill(Color.WHITE);
                    break;
                }
                case 7: {
                    lbl_message7.setText(newMessage.getText());
                    lbl_message7.setTextFill(Color.WHITE);
                    break;
                }
                default:
                    break;
            }
        } else {
            sendMessage_field.setStyle("-fx-border-color: red");
        }
    }

    private void getHistory2(Vector<String> historyy, Label label){
        if (!historyy.isEmpty()) {
            StringTokenizer history;
            String side;

            for (int i = 0; i < historyy.size(); i++) {
                history = new StringTokenizer(historyy.get(i), "#");
                side = history.nextToken();
                Text message = new Text(history.nextToken());
                message.setFont(Font.font("Serif", 18));
                message.setFill(Color.WHITE);
                Text time = new Text(history.nextToken());
                time.setFont(Font.font("Serif", 10));
                time.setFill(Color.WHITE);
                HBox hbox = new HBox();
                HBox hhbox = new HBox();
                hbox.getChildren().add(message);// set text of message
                hhbox.getChildren().add(time);// set time of message
                if (side.equals("R")) {
                    hbox.setAlignment(Pos.BASELINE_RIGHT);
                    hhbox.setAlignment(Pos.BASELINE_RIGHT);
                    dialog_field.getChildren().add(hbox);
                    dialog_field.getChildren().add(hhbox);
                    scroll_field.setContent(dialog_field);
                    label.setText(message.getText());
                    label.setTextFill(Color.WHITE);

                } else if (side.equals("L")) {
                    hbox.setAlignment(Pos.BASELINE_LEFT);
                    hhbox.setAlignment(Pos.BASELINE_LEFT);
                    dialog_field.getChildren().add(hbox);
                    dialog_field.getChildren().add(hhbox);
                    scroll_field.setContent(dialog_field);
                    label.setText(message.getText());
                    label.setTextFill(Color.GREEN);

                }
            }
        }
    }

    private void getHistory(int temp) {
        switch (temp) {
            case 1: {
                dialog_field.getChildren().clear();
                getHistory2(history1, lbl_message1);//выведет все соообщения с history1
                break;
            }//L#message#time  OR R#message#time
            case 2: {
                dialog_field.getChildren().clear();
                getHistory2(history2, lbl_message2);//выведет все соообщения с history2
                break;
            }
            case 3: {
                dialog_field.getChildren().clear();
                getHistory2(history3, lbl_message3);//выведет все соообщения с history3
                break;
            }
            case 4: {
                dialog_field.getChildren().clear();
                getHistory2(history4, lbl_message4);//выведет все соообщения с history4
                break;
            }
            case 5: {
                dialog_field.getChildren().clear();
                getHistory2(history5, lbl_message5);
                break;
            }
            case 6: {
                dialog_field.getChildren().clear();
                getHistory2(history6, lbl_message6);
                break;
            }
            case 7: {
                dialog_field.getChildren().clear();
                getHistory2(history7, lbl_message7);
                break;
            }
            default:
                break;
        }
    }


    private Boolean setHistory(int temp, String lastMessage) {
        if (!lastMessage.equals("")) {
            switch (temp) {
                case 1: {
                    if (alreadyContains(history1, lastMessage)) return true;
                    else history1.add(lastMessage);
                    return false;
                }//L#message#time  OR R#message#time
                case 2: {
                    if (alreadyContains(history2, lastMessage)) return true;
                    else history2.add(lastMessage);
                    return false;
                }//позже смогу выводить историю при смене чата и знать на какой стороне выводить конкретное смс
                case 3: {
                    if (alreadyContains(history3, lastMessage)) return true;
                    else history3.add(lastMessage);
                    return false;
                }
                case 4: {
                    if (alreadyContains(history4, lastMessage)) return true;
                    else history4.add(lastMessage);
                    return false;
                }
                case 5: {
                    if (alreadyContains(history5, lastMessage)) return true;
                    else history5.add(lastMessage);
                    return false;
                }
                case 6: {
                    if (alreadyContains(history6, lastMessage)) return true;
                    else history6.add(lastMessage);
                    return false;
                }
                case 7: {
                    if (alreadyContains(history7, lastMessage)) return true;
                    else history7.add(lastMessage);
                    return false;
                }
                default:
                    return false;
            }
        }
        return false;
    }

    private Boolean alreadyContains(Vector vector, String lastMessage) {
        if (!vector.isEmpty()) {
            for (int i = 0; i < vector.size(); i++)
                if (vector.get(i).equals(lastMessage)) return true;//уже содержит такое смс
        }
        return false;// ещё не содержит такое смс
    }


    private void gettingMessage(String sender, String receiver, String text, String time) {
        int temp;
        boolean b = true;
        if (receiver.equals(username)){//мне отправили смс
            temp = comparison(sender);// узнаю в каком чате у меня этот человек
            b = setHistory(temp, "L#" + text + "#" + time);//true - if already exist, false - if not exist yet
            if (lbl_receiver.getText().equals(sender) && !b){
                //если у меня открыт чат с отправителем и в истории нет его смс
                //показываю это смс
//                Text newMessage = new Text(text);
//                newMessage.setFont(Font.font("Serif", 18));
//                newMessage.setFill(Color.WHITE);
//
//                Text newTime = new Text(time);
//                newTime.setFont(Font.font("Serif", 10));
//                newTime.setFill(Color.WHITE);
//
//                HBox hbox = new HBox();
//                hbox.getChildren().add(newMessage);// set Message
//                hbox.setAlignment(Pos.BASELINE_LEFT);
//                dialog_field.getChildren().add(hbox);
//                HBox hhbox = new HBox();
//                hhbox.getChildren().add(newTime);// set time of message
//                hhbox.setAlignment(Pos.BASELINE_LEFT);
//                dialog_field.getChildren().add(hhbox);
//                scroll_field.setContent(dialog_field);
//                setNewMessage(temp, text, Color.GREEN);
                getHistory(temp);
            }
            else if(temp>0){//если есть чат с человеком, но он не открыт
                setNewMessage(temp, text, Color.GREEN);
            }
            else if (temp == 0){
//                String lastMessage = "L#"+text+"#"+time;
//                addNewFriend(sender, lastMessage, true);//создаю чат либо открываю уже существующий с этим отправителем
            }
        }
        else if (sender.equals(username)){//если я отправила смс
            temp = comparison(receiver);// узнаю в каком чате у меня этот человек
            b = setHistory(temp, "R#" + text + "#" + time);//true - if already exist, false - if not exist yet
                if(lbl_receiver.getText().equals(receiver) && !b){
                    //если у меня открыт чат с отправителем и в истории нет моё смс
                    //показываю это смс
//                    Text newMessage = new Text(text);
//                    newMessage.setFont(Font.font("Serif", 18));
//                    newMessage.setFill(Color.WHITE);
//
//                    Text newTime = new Text(time);
//                    newTime.setFont(Font.font("Serif", 10));
//                    newTime.setFill(Color.WHITE);
//
//                    HBox hbox = new HBox();
//                    hbox.getChildren().add(newMessage);// set Message
//                    hbox.setAlignment(Pos.BASELINE_RIGHT);
//                    dialog_field.getChildren().add(hbox);
//                    HBox hhbox = new HBox();
//                    hhbox.getChildren().add(newTime);// set time of message
//                    hhbox.setAlignment(Pos.BASELINE_RIGHT);
//                    dialog_field.getChildren().add(hhbox);
//                    scroll_field.setContent(dialog_field);
                    getHistory(temp);
                    setNewMessage(temp, text, Color.WHITE);
                }
                else if(temp>0){//если есть чат с человеком, но он не открыт
                    setNewMessage(temp, text, Color.WHITE);
                }
                else if (temp == 0){
//                String lastMessage = "L#"+text+"#"+time;
//                addNewFriend(sender, lastMessage, true);//создаю чат либо открываю уже существующий с этим отправителем
                }

        }

            }

            private void setNewMessage(int temp, String message, Color color){
                switch (temp) {// последнее смс будет видно в панели чата, оно от собеседника - текст будет зелёным
                    case 1: {  lbl_message1.setText(message);  lbl_message1.setTextFill(color);break; }
                    case 2: {  lbl_message2.setText(message); lbl_message2.setTextFill(color); break; }
                    case 3: {  lbl_message3.setText(message);  lbl_message3.setTextFill(color);break; }
                    case 4: {  lbl_message4.setText(message);  lbl_message4.setTextFill(color);break; }
                    case 5: {  lbl_message5.setText(message);  lbl_message5.setTextFill(color);break; }
                    case 6: {  lbl_message6.setText(message);  lbl_message6.setTextFill(color);break; }
                    case 7: {  lbl_message7.setText(message);  lbl_message7.setTextFill(color);break; }
                    default: break; }

            }


    @FXML
    void exit(ActionEvent event) throws IOException {
        online = false;
        toServer = "exit"+"#"+username;
        System.out.println("Нажала на выход и отправила:" + toServer);
        Connection();
        Platform.exit();
        System.exit(0);
    }


    @Override
    public void start(Stage primaryStage)  {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
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
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void Connection() throws IOException{
        Socket socket = new Socket (ip, port);
        DataInputStream reader = new DataInputStream(socket.getInputStream());
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
//        new Thread(()->{
//            try{
              //  while(true){
                if(toServer!="") {
                    String output = toServer;
                    toServer = ("");
                    writer.writeUTF(output);// send String
                    System.out.println("Отправила серверу: " + output);
                    StringTokenizer stringOut = new StringTokenizer(output, "#");
                    String noNeed = stringOut.nextToken();



                    String input = reader.readUTF(); // получаю ответ от сервера
                    System.out.println("Получила от сервера: " + input);
                    StringTokenizer stringIn = new StringTokenizer(input, "#");
                    String keyword = stringIn.nextToken();
                    input = input.replace(keyword + "#", "");//удаляю из строки ключевое слово
                        if(keyword.equals("exit")&& input.equals(TRUE)){
                        System.out.println("Пока");
                        socket.close();
                        //break;
                    }
                    translator(keyword, stringOut, input);
                }

                //}
               // }

                reader.close();
                writer.close();
//            }
//            catch(Exception e){e.printStackTrace();}
      //  }).start();


    }

    private void translator(String keyword, StringTokenizer str, String input){
        //это всё поток Connection
        if (keyword.equals(CREATE) && input.equals(TRUE)) {
            online = true;
            //username = str.nextToken();
            //createNewUser(button);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //button.getScene().getWindow().hide();
                    openNewScene("sample.fxml", username, butt);
                }
            });

        }
        else if(keyword.equals(CREATE) && input.equals(FALSE)){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    butt.setStyle("-fx-border-color: red; -fx-border-width: 3");
                    butt.setText("username");
                }
            });
        }
        else if(keyword.equals(LOGIN) && input.equals(TRUE)){
            online = true;
           // username = str.nextToken();
            //loginUser(button);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //button.getScene().getWindow().hide();
                    openNewScene("sample.fxml", username, butt);
                }
            });
        }
        else if (keyword.equals(LOGIN) && input.equals(FALSE)){
            butt.setStyle("-fx-border-color: red; -fx-border-width: 3");
            butt.setText("not exist");
        }
        else if(keyword.equals(SEND_MESSAGE) && input.equals(TRUE)){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    sendingMessage(true);
                }
            });
        }
        else if (keyword.equals(SEND_MESSAGE) && input.equals(FALSE)){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    sendingMessage(false);
                }
            });
        }
        else if(keyword.equals(FIND)&& input.equals(TRUE)){
            String friendUsername = str.nextToken();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addNewFriend(friendUsername, "",true);
                }
            });
        }
        else if (keyword.equals(FIND) && input.equals(FALSE)){
            System.out.println("такого друга нет");
            String friendUsername = str.nextToken();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addNewFriend(friendUsername, "",false);
                }
            });
        }
        else if (keyword.equals(GET_MESSAGE) && input.equals(FALSE)){}
        else if(keyword.equals(GET_MESSAGE)){
            StringTokenizer s = new StringTokenizer(input, "#");//я же могу получить несколько смс одновременно
            int counter = Integer.parseInt(s.nextToken());
            System.out.println("counter = "+ counter);
            while(counter!=0){//поэтому буду считывать строку до её конца
                String sender = s.nextToken();
                String receiver = s.nextToken();
                String text = s.nextToken();
                String time = s.nextToken();
                System.out.println("Проверяю цикл получения смс: "+sender+"///"+receiver+"///"+text+"///"+time);
                System.out.println();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Отправила в gettingMessage строки: " +sender+"#"+receiver + "#"+ text+ "#"+ time );
                        gettingMessage(sender, receiver, text, time);
                    }
                });
                counter--;

            }

        }


    }


    private void openNewScene(String window, String string, Button button){
        //((Node)(event.getSource())).getScene().getWindow().hide();
        button.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();

        Client controller = loader.getController();
        controller.setUsername(string);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initStyle(StageStyle.UNDECORATED);


        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        //move around here
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });


        stage.setScene(scene);
        stage.show();


    }



}


