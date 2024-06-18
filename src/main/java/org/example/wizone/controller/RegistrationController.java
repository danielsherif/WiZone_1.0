package org.example.wizone.controller;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.MFXStepper.MFXStepperEvent;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.animation.PauseTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.example.wizone.model.User;
import org.example.wizone.model.UserStorage;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    private final MFXTextField registrationEmail;
    private final MFXTextField loginEmail;
    private final MFXPasswordField registrationPasswordField;
    private final MFXPasswordField loginPassword;
    private final MFXTextField firstName;
    private final MFXTextField lastName;
    private final MFXComboBox<String> genderCombo;
    private final MFXCheckbox checkbox;
    private MFXTextField genderLabel2;
    @FXML
    private MFXStepper RegistrationStepper, LoginStepper;
    @FXML
    private StackPane registrationPane, loginPane;
    @FXML
    private MFXButton registrationBtn, loginBtn;

    private BooleanProperty registrationError = new SimpleBooleanProperty(false);
    private BooleanProperty loginError = new SimpleBooleanProperty(false);

    public static String registeredUsername;
    public static String registeredFirstName;
    public static String registeredLastName;

    private Stage stage;

    public RegistrationController() {
        registrationEmail = new MFXTextField();
        loginEmail = new MFXTextField();
        registrationPasswordField = new MFXPasswordField();
        loginPassword = new MFXPasswordField();
        firstName = new MFXTextField();
        lastName = new MFXTextField();
        genderCombo = new MFXComboBox<>();
        checkbox = new MFXCheckbox("Confirm Data?");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        registrationEmail.setPromptText("Email...");
        registrationEmail.getValidator().constraint("The email must be at least 6 characters long", registrationEmail.textProperty().length().greaterThanOrEqualTo(6));
        registrationEmail.setLeadingIcon(new MFXIconWrapper("fas-user", 16, Color.web("#4D4D4D"), 24));

        registrationPasswordField.getValidator().constraint("The password must be at least 8 characters long", registrationPasswordField.textProperty().length().greaterThanOrEqualTo(8));
        registrationPasswordField.setPromptText("Password...");

        firstName.setPromptText("First Name...");
        firstName.getValidator().constraint("Please enter first name", firstName.textProperty().isNotEmpty());

        lastName.setPromptText("Last Name...");
        lastName.getValidator().constraint("Please enter last name", lastName.textProperty().isNotEmpty());

        genderCombo.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));


        List<MFXStepperToggle> registrationSteps = createSteps();
        RegistrationStepper.getStepperToggles().addAll(registrationSteps);

        List<MFXStepperToggle> loginSteps = createLoginSteps();
        LoginStepper.getStepperToggles().addAll(loginSteps);


        registrationBtn.setVisible(false);
        loginPane.setVisible(false);

        RegistrationStepper.addEventHandler(MFXStepperEvent.LAST_NEXT_EVENT, event -> handleRegistration());
        LoginStepper.addEventHandler(MFXStepperEvent.LAST_NEXT_EVENT, event -> handleLogin());
    }


    private void handleRegistration() {
        if (UserStorage.doesUserExist(registrationEmail.getText())) {

            registrationError.set(true);
            registrationEmail.getValidator().constraint("Username already exists", registrationError);
            registrationEmail.getValidator().validate();
            return;
        }


        if (registrationEmail.getValidator().validate().isEmpty() && registrationPasswordField.getValidator().validate().isEmpty() &&
                firstName.getValidator().validate().isEmpty() && lastName.getValidator().validate().isEmpty()) {
            String passwordHash = hashPassword(registrationPasswordField.getText());
            User user = new User(registrationEmail.getText(), passwordHash, firstName.getText(), lastName.getText());
            UserStorage.saveUser(user);
            RegistrationController.registeredUsername = user.getUsername();
            RegistrationController.registeredFirstName = user.getFirstName();
            RegistrationController.registeredLastName = user.getLastName();


            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> loadMainScreen());
            delay.play();
        } else {
            registrationError.set(true);
            registrationEmail.getValidator().validate();
            registrationPasswordField.getValidator().validate();
        }
    }

    private void handleLogin() {
        List<User> users = UserStorage.loadUsers();
        User loggedInUser = null;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(loginEmail.getText())) {
                loggedInUser = user;
                break;
            }
        }

        boolean isEmailValid = loginEmail.getValidator().validate().isEmpty();
        boolean isPasswordValid = loginPassword.getValidator().validate().isEmpty();


        boolean isAuthenticated = isEmailValid && isPasswordValid && loggedInUser != null &&
                loggedInUser.getPasswordHash().equals(hashPassword(loginPassword.getText()));

        if (isAuthenticated) {

            System.out.println("Login successful!");
            loginError.set(false);

            MFXStepperToggle lastToggle = LoginStepper.getStepperToggles().get(LoginStepper.getStepperToggles().size() - 1);
            lastToggle.setIcon(new MFXFontIcon("fas-check", 16, Color.web("#85CB33")));

            Label successLabel = new Label("Successful login, redirecting...");
            successLabel.getStyleClass().add("success-label");
            successLabel.setAlignment(Pos.CENTER);
            loginPane.getChildren().add(successLabel);
            registeredFirstName = loggedInUser.getFirstName();
            registeredLastName = loggedInUser.getLastName();


            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                loadMainScreen();
                loginPane.getChildren().remove(successLabel);
            });
            delay.play();
        } else {

            loginError.set(true);

            loginEmail.getValidator().constraint("Invalid username or password", loginError);
            loginPassword.getValidator().constraint("Invalid username or password", loginError);


            loginEmail.getValidator().validate();
            loginPassword.getValidator().validate();
        }
    }



    private void loadMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/wizone/MainScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);


            MainController mainScreenController = loader.getController();
            mainScreenController.setStage(stage);

            Platform.runLater(() -> {
                stage.setScene(scene);
                stage.setTitle("WiZone");
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<MFXStepperToggle> createSteps() {
        MFXStepperToggle step1 = new MFXStepperToggle("Step 1", new MFXFontIcon("fas-lock", 16, Color.web("#f1c40f")));
        VBox step1Box = new VBox(20, wrapNodeForValidation(registrationEmail), wrapNodeForValidation(registrationPasswordField));
        step1Box.setAlignment(Pos.CENTER);
        step1.setContent(step1Box);
        step1.getValidator().dependsOn(registrationEmail.getValidator()).dependsOn(registrationPasswordField.getValidator());

        MFXStepperToggle step2 = new MFXStepperToggle("Step 2", new MFXFontIcon("fas-user", 16, Color.web("#49a6d7")));
        VBox step2Box = new VBox(20, firstName, lastName, genderCombo);
        step2Box.setAlignment(Pos.CENTER);
        step2.setContent(step2Box);
        step2.getValidator().dependsOn(firstName.getValidator()).dependsOn(lastName.getValidator()).dependsOn(genderCombo.getValidator());

        MFXStepperToggle step3 = new MFXStepperToggle("Step 3", new MFXFontIcon("fas-check", 16, Color.web("#85CB33")));
        Node step3Grid = createGrid();
        step3.setContent(step3Grid);
        step3.getValidator().constraint("Data must be confirmed", checkbox.selectedProperty());

        return List.of(step1, step2, step3);
    }

    private List<MFXStepperToggle> createLoginSteps() {
        MFXStepperToggle loginStep1 = new MFXStepperToggle("Login Step1", new MFXFontIcon("fas-lock", 16, Color.web("#f1c40f")));
        VBox loginStepBox1 = new VBox(20, wrapNodeForValidation(loginEmail), wrapNodeForValidation(loginPassword));
        loginStepBox1.setAlignment(Pos.CENTER);
        loginStep1.setContent(loginStepBox1);
        loginStep1.getValidator().dependsOn(loginEmail.getValidator()).dependsOn(loginPassword.getValidator());

        return List.of(loginStep1);
    }

    private <T extends Node & Validated> Node wrapNodeForValidation(T node) {
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setManaged(false);
        RegistrationStepper.addEventHandler(MFXStepperEvent.VALIDATION_FAILED_EVENT, event -> {
            MFXValidator validator = node.getValidator();
            List<Constraint> validate = validator.validate();
            if (!validate.isEmpty()) {
                errorLabel.setText(validate.get(0).getMessage());
            }
        });
        RegistrationStepper.addEventHandler(MFXStepperEvent.NEXT_EVENT, event -> errorLabel.setText(""));
        VBox wrap = new VBox(9, node, errorLabel) {
            @Override
            protected void layoutChildren() {
                super.layoutChildren();

                double x = node.getBoundsInParent().getMinX();
                double y = node.getBoundsInParent().getMaxY() + getSpacing();
                double width = getWidth();
                double height = errorLabel.prefHeight(-1);
                errorLabel.resizeRelocate(x, y, width, height);
            }

            @Override
            protected double computePrefHeight(double width) {
                return super.computePrefHeight(width) + errorLabel.getHeight() + getSpacing();
            }
        };
        wrap.setAlignment(Pos.CENTER);
        return wrap;
    }

    private Node createGrid() {
        MFXTextField emailLabel1 = createLabel("Email: ");
        MFXTextField usernameLabel2 = createLabel("");
        usernameLabel2.textProperty().bind(registrationEmail.textProperty());

        MFXTextField firstNameLabel1 = createLabel("First Name: ");
        MFXTextField firstNameLabel2 = createLabel("");
        firstNameLabel2.textProperty().bind(firstName.textProperty());

        MFXTextField lastNameLabel1 = createLabel("Last Name: ");
        MFXTextField lastNameLabel2 = createLabel("");
        lastNameLabel2.textProperty().bind(lastName.textProperty());

        MFXTextField genderLabel1 = createLabel("Gender: ");
        genderLabel2 = createLabel("");
        genderLabel2.textProperty().bind(Bindings.createStringBinding(
                () -> genderCombo.getValue() != null ? genderCombo.getValue() : "Can't Say",
                genderCombo.valueProperty()
        ));

        emailLabel1.getStyleClass().add("header-label");
        firstNameLabel1.getStyleClass().add("header-label");
        lastNameLabel1.getStyleClass().add("header-label");
        genderLabel1.getStyleClass().add("header-label");

        MFXTextField completedLabel = MFXTextField.asLabel("Completed!");
        completedLabel.getStyleClass().add("completed-label");
        completedLabel.setAlignment(Pos.CENTER);
        Label completedRedirectLabel = new Label("Redirecting...");
        completedLabel.getStyleClass().add("completed-label");

        HBox b1 = new HBox(emailLabel1, usernameLabel2);
        HBox b2 = new HBox(firstNameLabel1, firstNameLabel2);
        HBox b3 = new HBox(lastNameLabel1, lastNameLabel2);
        HBox b4 = new HBox(genderLabel1, genderLabel2);

        b1.setMaxWidth(Region.USE_PREF_SIZE);
        b2.setMaxWidth(Region.USE_PREF_SIZE);
        b3.setMaxWidth(Region.USE_PREF_SIZE);
        b4.setMaxWidth(Region.USE_PREF_SIZE);

        VBox box = new VBox(10, b1, b2, b3, b4, checkbox);
        box.setAlignment(Pos.CENTER);
        StackPane.setAlignment(box, Pos.CENTER);

        RegistrationStepper.setOnLastNext(event -> {
            if (registrationEmail.getValidator().validate().isEmpty() &&
                    registrationPasswordField.getValidator().validate().isEmpty() &&
                    firstName.getValidator().validate().isEmpty() &&
                    lastName.getValidator().validate().isEmpty() &&
                    genderCombo.getValidator().validate().isEmpty()) {
                box.getChildren().setAll(completedLabel, completedRedirectLabel);
                RegistrationStepper.setMouseTransparent(true);
                getRegistrationInputs();
            } else {
                registrationError.set(true);
                registrationEmail.getValidator().validate();
                registrationPasswordField.getValidator().validate();
                firstName.getValidator().validate();
                lastName.getValidator().validate();
                genderCombo.getValidator().validate();
            }
        });
        RegistrationStepper.setOnBeforePrevious(event -> {
            if (RegistrationStepper.isLastToggle()) {
                checkbox.setSelected(false);
                box.getChildren().setAll(b1, b2, b3, b4, checkbox);
            }
        });


        return box;
    }



    private MFXTextField createLabel(String text) {
        MFXTextField label = MFXTextField.asLabel(text);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPrefWidth(200);
        label.setMinWidth(Region.USE_PREF_SIZE);
        label.setMaxWidth(Region.USE_PREF_SIZE);
        return label;
    }

    public void openRegistrationPane() {
        registrationPane.setVisible(true);
        registrationBtn.setVisible(false);
        loginPane.setVisible(false);
        loginBtn.setVisible(true);
    }

    public void openLoginPane() {
        loginPane.setVisible(true);
        loginBtn.setVisible(false);
        registrationPane.setVisible(false);
        registrationBtn.setVisible(true);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void getRegistrationInputs() {

        System.out.println("User created successfully ");
        System.out.println("Username: " + registrationEmail.getText()+ "  First Name: " + firstName.getText() + "  Last Name: " + lastName.getText() + "  Gender: "+ genderLabel2.getText());
//        System.out.println(registrationEmail.getText());
       // System.out.println(registrationPasswordField.getText());
//        System.out.println(firstName.getText());
//        System.out.println(lastName.getText());
//        System.out.println(genderLabel2.getText());
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public void getLoginInputs() {
        System.out.println(loginEmail.getText());
        System.out.println(loginPassword.getText());
    }
}