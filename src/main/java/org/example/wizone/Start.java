package org.example.wizone;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.example.wizone.controller.SplashController;

import java.io.IOException;

public class Start extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.setProperty("prism.order", "sw");

        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.DEFAULT)
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/org/example/wizone/Splash.fxml"));
        if (splashLoader.getLocation() == null) {
            throw new IOException("FXML file not found at the specified location: /org/example/wizone/Splash.fxml");
        }
        Parent splashRoot = splashLoader.load();
        Scene splashScene = new Scene(splashRoot);


        primaryStage.setScene(splashScene);
        primaryStage.setTitle("WiZone");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/wizone/assets/emblem-only2.png")));
        primaryStage.show();


        SplashController splashController = splashLoader.getController();
        splashController.setStage(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}
