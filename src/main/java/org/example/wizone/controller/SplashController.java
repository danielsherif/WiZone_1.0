package org.example.wizone.controller;

import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SplashController {

    @FXML
    private MFXProgressBar determinateBar;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            loadNextPage();
        }));
        timeline.play();
    }

    private void loadNextPage() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/wizone/Stepper.fxml"));
            System.out.println("FXML loaded: " + loader.getLocation());
            Parent root = loader.load();
            Scene stepperScene = new Scene(root);


            RegistrationController stepperController = loader.getController();

            stepperController.setStage(stage);

            stage.setScene(stepperScene);
            stage.setTitle("WiZone");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}