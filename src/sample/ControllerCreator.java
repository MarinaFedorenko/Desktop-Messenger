package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderStroke;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ControllerCreator extends Client{
    private double xOffset = 0;
    private double yOffset = 0;
    public static String CREATE = "create";


    @FXML
    private TextField userName_field;
    @FXML
    private TextField password_field;
    @FXML
    private Button btnDone;
    @FXML
    private Button btnBack;
    @FXML
    private TextField firstName_field;
    @FXML
    private TextField lastName_field;
    @FXML
    private TextField location_field;
    @FXML
    private CheckBox checkBoxMale;
    @FXML
    private CheckBox checkBoxFemale;




    @FXML
    void initialize(){

       btnDone.setOnAction(event->{
           //считываю инфу с полей для заполнения данных нового пользователя
           String username = userName_field.getText().trim();
           String firstname = firstName_field.getText().trim();
           String lastname = lastName_field.getText().trim();
           String password = password_field.getText().trim();
           String location =  location_field.getText().trim();
           String gender = "";
           Boolean valid = false;
           if(checkBoxMale.isSelected() && checkBoxFemale.isSelected())
               valid = false;
           else if (checkBoxFemale.isSelected()){
               gender = "Female";
               valid = true;
           }
           else if (checkBoxMale.isSelected()){
               gender = "Male";
                valid = true;
           }
           else {//ни то ни то не отмечено
               valid = false;
           }
           if(!firstname.equals("") && !lastname.equals("") && !location.equals("") &&
                   !username.equals("") && !password.equals("") && valid.equals(true)){

               toServer = CREATE+"#"+ username+"#"+firstname +"#"+ lastname + "#" +
                       password+"#"+location+"#"+gender;
               setUsername(username);
               butt = btnDone;
               try {
                   Connection();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
           else {
               if (firstname.equals(""))
                   firstName_field.setStyle("-fx-border-color: red");
               else
                   firstName_field.setStyle("-fx-border-color: green");
               if (lastname.equals(""))
                   lastName_field.setStyle("-fx-border-color: red");
               else
                   lastName_field.setStyle("-fx-border-color: green");
               if (location.equals(""))
                   location_field.setStyle("-fx-border-color: red");
               else
                   location_field.setStyle("-fx-border-color: green");
               if (username.equals(""))
                   userName_field.setStyle("-fx-border-color: red");
               else
                   userName_field.setStyle("-fx-border-color: green");
               if (password.equals(""))
                   password_field.setStyle("-fx-border-color: red");
               else
                   password_field.setStyle("-fx-border-color: green");
               if (!checkBoxMale.isSelected() && !checkBoxFemale.isSelected() ||
                       checkBoxMale.isSelected() && checkBoxFemale.isSelected()){
                   checkBoxFemale.setStyle("-fx-border-color: red");
                   checkBoxMale.setStyle("-fx-border-color: red");
               }
               else {
                   checkBoxFemale.setStyle("-fx-border-color: green");
                   checkBoxMale.setStyle("-fx-border-color: green");
               }
           }

       });

       btnBack.setOnAction(event -> {
           openNewScene("login.fxml", btnBack);
       });
    }

    public void usernameAlreadyExist() {
        userName_field.setStyle("-fx-border-color: red");
        userName_field.setPromptText("Username already exist");
    }



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
