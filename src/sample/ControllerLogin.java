package sample;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ControllerLogin extends Client{
    private double xOffset = 0;
    private double yOffset = 0;
    private static String LOGIN = "login";



        @FXML
        private Button btnCreate;
        @FXML
        private TextField userName_field;
        @FXML
        private TextField password_field;
        @FXML
        private Button btnLogin;
        @FXML
        private Button btnExit;
        private String userName;
        private String passWord;


    @FXML
    void initialize(){
        btnCreate.setOnAction(event->{
            //((Node)(event.getSource())).getScene().getWindow().hide();
            openNewScene("create.fxml", btnCreate);
        });

        btnLogin.setOnAction(event ->{
            userName = userName_field.getText().trim();
            passWord = password_field.getText().trim();
            if(!userName.equals("") && !passWord.equals("")){
                String output = LOGIN + "#"+userName+"#"+ passWord;
                butt = btnLogin;
                toServer = output;
                setUsername(userName);
                System.out.println("Данные в строку");
                try {
                    Connection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (userName.equals(""))
                userName_field.setStyle("-fx-border-color: red");
                else
                userName_field.setStyle("-fx-border-color: green");
                if (passWord.equals(""))
                    password_field.setStyle("-fx-border-color: red");
                else
                    password_field.setStyle("-fx-border-color: green");
            }

        });
        btnExit.setOnAction(event ->{
           //toServer = "exit"; по идее я даже ещё не подключалась к серверу, поэтому нет смысла
            //Connection(btnExit);сообщать ему что я выхожу
            Platform.exit();
            System.exit(0);
        });

    }

//    public void loginUser(Button button) {
//            if (ok){
//               // openNewScene("sample.fxml", btnLogin);
//                 openNewScene("sample.fxml", button);
//                ok = false;
//            }
//            else{
//                userName_field.setStyle("-fx-border-color: red");
//                password_field.setStyle("-fx-border-color: red");
//            }
//    }


    public void openNewScene(String window, Button button){
            button.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(window));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        Parent root = loader.getRoot();
//        if(window.equals("sample.fxml")){
//            Controller controller = loader.getController();
//            controller.setMyUsername(string);// я отправляю свой username
//        }

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
