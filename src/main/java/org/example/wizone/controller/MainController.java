package org.example.wizone.controller;
import com.convertapi.client.ConversionResult;
import com.convertapi.client.Param;
import io.github.palexdev.materialfx.controls.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;
import org.example.wizone.app.CADFileLoader;
import org.example.wizone.coordinates.Line;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.example.wizone.coordinates.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.util.Duration;



public class MainController implements Initializable {

    //Walls
    //Add
    @FXML
    private MFXButton AddWalls;
    private boolean wallMode = false;
    private Point2D wallStartPoint = null;
    private List<Wall> walls = new ArrayList<>();
    private Map<String, Double> wallThicknessMap = new HashMap<>();
    private Map<String, Color> wallColorMap = new HashMap<>();
    //Delete
    @FXML
    private MFXButton DeleteWalls;
    private boolean wallDeletionMode = false;
    //Select
    @FXML
    private MFXButton selectModeButton;
    private boolean selectionMode = false;
    private List<Path> selectedWalls = new ArrayList<Path>();

    //Access Points
    //Add
    @FXML
    private MFXButton APButton;
    private boolean apPlacementMode = false;
    private Map<Pane, List<AccessPoint>> apLocations = new HashMap<>();
    private Map<String, Map<String, Integer>> apPrices = new HashMap<>();
    @FXML
    private MFXComboBox<String> techCB;
    @FXML
    private MFXComboBox<String> brandCB;
    @FXML
    private MFXComboBox<String> modelCB;
    private Map<String, List<String>> brandToModels = new HashMap<>();
    @FXML
    private MFXTextField apName;
    @FXML
    private MFXTextField FirstTF;
    @FXML
    private MFXTextField SecondTF;
    public int APCount=1;

    //Delete
    @FXML
    private MFXButton deleteAP;
    private boolean apDeletionMode = false;
    //Edit
    @FXML
    private MFXButton editAP;
    private boolean apEditMode = false;
    private AccessPoint selectedAP = null;
    private ImageView selectedAPImageView = null;
    @FXML
    private MFXButton confirmEdit;
    //Wiring
    @FXML
    private MFXButton wiringModeButton;
    private boolean wiringMode = false;
    private Point2D wiringStartPoint = null;
    private List<Wire> wires = new ArrayList<>();


    //Other
    @FXML
    private MFXRectangleToggleNode saveButton;
    @FXML
    private MFXButton coverageInfo;
    @FXML
    private MFXButton saveSnapshot;
    @FXML
    private MFXToggleButton showOriginal;
    @FXML
    private Label welcomeLabel;
    private String username;
    @FXML
    private Button LogoutButton;
    @FXML
    private ImageView ButtonGroup;
    @FXML
    private Button ZoomInBG;
    @FXML
    private Button ZoomOutBG;
    private double zoomFactor = 1.0;
    private static final double ZOOM_INCREMENT = 0.1;
    private static final double MIN_ZOOM = 0.9;
    private static final double MAX_ZOOM = 2.0;
    private Timeline zoomTimeline;
    @FXML
    private Button PanBG;
    private boolean panningMode = false;




    //File
    @FXML
    private MFXRectangleToggleNode OpenFile;
    @FXML
    private MFXComboBox<String> FileComboBox;
    private CADFileLoader cadFileLoader = new CADFileLoader();

    //Display
    @FXML
    private TabPane planTabPane;
    @FXML
    private Pane centerPane;
    private Stage stage;
    private Pane svgPane;

    //Right pane
    @FXML
    private StackPane rightPane;
    //Wall pane
    @FXML
    private Pane wallSettingsPane;
    @FXML
    private MFXComboBox<String> wallMaterialCB;
    //AP Pane
    @FXML
    private Pane apSettingsPane;
    @FXML
    private Pane editPropertiesPane;
    @FXML
    private MFXCheckbox editCheckBox;
    //Wiring pane
    @FXML
    private Pane wiringSettingsPane;
    @FXML
    private MFXComboBox<String> techTypeCB;
    @FXML
    private MFXComboBox<String> colorCB;
    @FXML
    private MFXComboBox<String> thicknessCB;


    private static final double ClickOffset_X = 84.80;
    private static final double ClickOffset_Y= 36.80;


    //Initialization and populating
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        populateFields();
        FileComboBox.getItems().clear();
        loadSavedPlanNames();
        rightPane.getChildren().clear();
        OpenFile.setOnAction(event -> handleFileOpen());
        //coverageInfo.setOnAction(event -> printSelectedWalls());

        editCheckBox.setSelected(false);
        editPropertiesPane.setVisible(false);
        editCheckBox.setOnAction(event -> {
            editPropertiesPane.setVisible(editCheckBox.isSelected());
        });

        FileComboBox.setOnAction(event -> {
            String selectedFile = FileComboBox.getSelectionModel().getSelectedItem();
            if (selectedFile != null) {
                handleSavedPlanOpen(selectedFile);
            }
        });

        APButton.setOnAction(event -> {
            if (apPlacementMode)
            {
                apPlacementMode = false;
                System.out.println("AP placement mode deactivated");
            }
            else
            {
                toggleAll();
                apPlacementMode=true;
                System.out.println("AP placement mode active");
                showPane(apSettingsPane);
                initializePaneEventHandlers();
            }
        });

        deleteAP.setOnAction(event -> {
            if (apDeletionMode)
            {
                apDeletionMode = false;
                System.out.println("AP deletion mode deactivated");
            }
            else
            {
                toggleAll();
                apDeletionMode=true;
                System.out.println("AP deletion mode active");
                showPane(apSettingsPane);
                initializePaneEventHandlers();
            }
        });

        editAP.setOnAction(event -> {
            if (apEditMode)
            {
                apEditMode=false;
                System.out.println("AP edit mode deactivated");
            }
            else
            {
            toggleAll();
            apEditMode=true;
            System.out.println("AP edit mode active");
            showPane(apSettingsPane);
            initializePaneEventHandlers();
            }
        });

        AddWalls.setOnAction(event -> {
            if (wallMode)
            {
                wallMode=false;
                printWalls();
                System.out.println("Wall placement mode deactivated");
            }
            else
            {
                toggleAll();
                wallMode=true;
                System.out.println("Wall placement mode active");
                showPane(wallSettingsPane);
                initializePaneEventHandlers();
            }
        });

        DeleteWalls.setOnAction(event -> {
            if (wallDeletionMode)
            {
                wallDeletionMode=false;
                System.out.println("Wall deletion mode deactivated");
            }
            else
            {
                toggleAll();
                wallDeletionMode=true;
                System.out.println("Wall deletion mode active");
                showPane(wallSettingsPane);
                initializePaneEventHandlers();
            }
        });

        selectModeButton.setOnAction(event -> {
            if (selectionMode)
            {
                selectionMode = false;
                System.out.println("Wall selection mode deactivated");
            }
            else {
                toggleAll();
                selectionMode = true;
                System.out.println("Wall selection mode active");
                showPane(wallSettingsPane);
                initializePaneEventHandlers();
            }
            });

        wiringModeButton.setOnAction(event -> {
            if (wiringMode)
            {
                wiringMode = false;
                printWires();
                System.out.println("Wiring mode deactivated");
            }
            else {
                toggleAll();
                wiringMode = true;
                System.out.println("Wiring mode active");
                showPane(wiringSettingsPane);
                initializePaneEventHandlers();
            }
        });

        saveSnapshot.setOnAction(event -> {
            savePlanAsImage();
        });

        brandCB.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                modelCB.getItems().clear();
                modelCB.getItems().addAll(brandToModels.getOrDefault(newValue, Collections.emptyList()));
            }
        });

        confirmEdit.setOnAction(event -> {
            if(apEditMode)
            saveChanges();
        });

        try{
            loadAPPricesFromFile();
        }
        catch (Exception e){
            System.out.println("Error loading AP prices: " + e.getMessage());
        }

        coverageInfo.setOnAction(event -> {
            calculateTotalCost();
        });

        showOriginal.setOnAction(event -> {
            boolean showOriginalValue = showOriginal.isSelected();

            if (showOriginalValue) {
                showOriginalSVG();
            } else {
                showEverything();
            }
        });

        username = RegistrationController.registeredFirstName + " " + RegistrationController.registeredLastName;
        System.out.println("Welcome " + username);
        welcomeLabel.setText(username);

        LogoutButton.setOnAction(event -> {
           handleLogout(event);
        });

        ZoomInBG.setOnAction(event -> handleZoomIn());

        ZoomOutBG.setOnAction(event -> handleZoomOut());

        PanBG.setOnAction(event -> {
            if (panningMode) {
                panningMode = false;
                System.out.println("Panning mode deactivated");
            } else {
                panningMode = true;
                System.out.println("Panning mode activated");
                handlePan();
            }
        });

        saveButton.setOnAction(event -> {
            generateResultsFile();
        });

    }

    private void populateFields() {
        //Wiring Tech Type Combo Box
        techTypeCB.getItems().addAll("Cat5e", "Cat5e", "Cat6", "Cat6a", "Cat7","Cat8");
        techTypeCB.setValue("Cat5e");
        //Wiring Color Combo Box
        colorCB.getItems().addAll("Blue", "Green", "Red", "Yellow", "Black", "White");
        colorCB.setValue("Blue");
        //Wiring Thickness Combo Box
        thicknessCB.getItems().addAll("1", "2", "3", "4", "5", "6");
        thicknessCB.setValue("3");
        //Wall Material Combo Box
        wallMaterialCB.getItems().addAll("Brick", "Concrete","Glass", "Drywall", "Wood","Stone", "Metal", "Plaster", "Steel", "Aluminum", "Cement", "Marble");
        wallMaterialCB.setValue("Concrete");
        // Wall thickness and color maps
        wallThicknessMap.put("Brick", 4.0);
        wallThicknessMap.put("Concrete", 3.5);
        wallThicknessMap.put("Glass", 1.0);
        wallThicknessMap.put("Drywall", 2.0);
        wallThicknessMap.put("Wood", 2.5);
        wallThicknessMap.put("Stone", 3.0);
        wallThicknessMap.put("Metal", 1.5);
        wallThicknessMap.put("Plaster", 2.0);
        wallThicknessMap.put("Steel", 1.5);
        wallThicknessMap.put("Aluminum", 1.0);
        wallThicknessMap.put("Cement", 3.0);
        wallThicknessMap.put("Marble", 2.5);

        wallColorMap.put("Brick", Color.FIREBRICK);
        wallColorMap.put("Concrete", Color.DIMGRAY);
        wallColorMap.put("Glass", Color.LIGHTBLUE);
        wallColorMap.put("Drywall", Color.GREENYELLOW);
        wallColorMap.put("Wood", Color.SADDLEBROWN);
        wallColorMap.put("Stone", Color.DARKGRAY);
        wallColorMap.put("Metal", Color.SILVER);
        wallColorMap.put("Plaster", Color.LIGHTGRAY);
        wallColorMap.put("Steel", Color.DARKGRAY);
        wallColorMap.put("Aluminum", Color.LIGHTGRAY);
        wallColorMap.put("Cement", Color.GRAY);
        wallColorMap.put("Marble", Color.BLUEVIOLET);
        techCB.getItems().addAll("Wi-Fi", "Ethernet", "Cellular");
        techCB.setValue("Wi-Fi");

        // Brand Combo Box
        brandCB.getItems().addAll("Cisco", "Ubiquiti", "Aruba", "TP-Link", "Netgear", "D-Link", "MikroTik", "Fortinet", "Juniper", "Huawei");
        brandCB.setValue("Cisco");

        // Populate brandToModels map
        brandToModels.put("Cisco", Arrays.asList("1140i", "1240i", "3700i", "4800i", "Catalyst 9800", "Meraki AP"));
        brandToModels.put("Ubiquiti", Arrays.asList("UniFi AP-AC Lite", "UniFi AP-AC Pro", "UniFi AP-AC HD", "UniFi AP-AC LR", "UniFi AP-AC Mesh"));
        brandToModels.put("Aruba", Arrays.asList("Instant On AP11", "Instant On AP22", "Instant On AP32", "Aruba 300 Series", "Aruba 700 Series"));
        brandToModels.put("TP-Link", Arrays.asList("Archer C80", "Archer AX6000", "Deco X60", "Omada EAP245", "Omada EAP660"));
        brandToModels.put("Netgear", Arrays.asList("Orbi WiFi 6E", "Nighthawk AX8", "Orbi Satellite", "AXE11000", "AXE8"));
        brandToModels.put("D-Link", Arrays.asList("DAP-1660", "DAP-1860", "DIR-882", "DIR-890L", "DIR-869"));
        brandToModels.put("MikroTik", Arrays.asList("hAP ac", "cAP ac", "RB4011", "RB750Gr3", "RB951Ui-2HnD"));
        brandToModels.put("Fortinet", Arrays.asList("FortiAP-221E", "FortiAP-321E", "FortiAP-101D", "FortiAP-330D", "FortiAP-430D"));
        brandToModels.put("Juniper", Arrays.asList("Mist Access Point 3", "Mist Access Point 4", "Mist Access Point 5", "Mist Access Point 6", "Mist Access Point 7"));
        brandToModels.put("Huawei", Arrays.asList("AP7010DN", "AP7030DN", "AP7060DN", "AP7130DN", "AP7230DN"));

    }

    private void showPane(Pane paneToShow) {
        rightPane.getChildren().clear();
        rightPane.getChildren().add(paneToShow);
    }

    public void toggleAll() {

        if (wallMode) {
            System.out.println("Add wall mode deactivated");
        }
        if (wallDeletionMode)
            System.out.println("Delete wall mode deactivated");
        if (selectionMode)
            System.out.println("Selection mode deactivated");
        if (apPlacementMode)
            System.out.println("AP placement mode deactivated");
        if (apDeletionMode)
            System.out.println("AP deletion mode  deactivated");
        if (apEditMode)
            System.out.println("AP edit mode deactivated");
        if (wiringMode) {
            System.out.println("Wiring mode deactivated");

        }
        wallMode=false;
        wallDeletionMode=false;
        wallStartPoint=null;
        selectionMode=false;
        apPlacementMode=false;
        apDeletionMode=false;
        apEditMode=false;
        wiringMode=false;
        wiringStartPoint=null;
        selectedAP = null;
        if (selectedAPImageView != null) {
            selectedAPImageView.setEffect(null);
            selectedAPImageView = null;
        }


    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void initializePaneEventHandlers() {
        for (Tab tab : planTabPane.getTabs()) {
            Node content = tab.getContent();
            if (content instanceof AnchorPane) {
                AnchorPane anchorPane = (AnchorPane) content;
                anchorPane.setOnMouseClicked(event -> {
                    if (apPlacementMode) {
                        placeAPIcon(anchorPane, event);

                    }
                    if (apEditMode) {
                        handleEditAP(anchorPane, event);
                    } else {
                        selectedAP = null;
                        if (selectedAPImageView != null) {
                            selectedAPImageView.setEffect(null);
                            selectedAPImageView = null;
                        }
                    }
                    if (apDeletionMode) {
                        deleteAPIcon(anchorPane, event);
                    }
                    if (wiringMode) {
                        handleWiring(anchorPane, event);
                    }
                    if (wallMode) {
                        handleWallDrawing(anchorPane, event);
                    }
                    if (selectionMode) {
                        handleLineSelection(anchorPane, event);
                    }
                    if(wallDeletionMode){
                        handleWallDeletion(anchorPane, event);
                    }
                });
            }
        }
    }

    private void loadSavedPlanNames() {
        try {

            java.nio.file.Path savedPlansPath = java.nio.file.Paths.get("src", "main", "java", "org", "example", "wizone", "savedPlans");


            java.nio.file.Files.list(savedPlansPath).forEach(filePath -> {

                String fileNameWithoutExtension = filePath.getFileName().toString().replaceFirst("[.][^.]+$", "");

                FileComboBox.getItems().add(fileNameWithoutExtension);
            });
        } catch (IOException e) {
            System.err.println("Error loading saved plan names: " + e.getMessage());
        }
    }

    void handleLogout(ActionEvent event) {
        try {

            username = null;
            welcomeLabel.setText("Welcome");


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/wizone/Stepper.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            RegistrationController registrationController = loader.getController();
            registrationController.setStage(stage);


            stage.setScene(scene);
            stage.setTitle("WiZone");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addButtonGroup() {
        ImageView buttonGroup = ButtonGroup;
        Button zoomInBG = ZoomInBG;
        Button zoomOutBG = ZoomOutBG;
        Button panBG = PanBG;
    centerPane.getChildren().addAll(buttonGroup, zoomInBG, zoomOutBG,panBG);
    }



    //Zooming
    private void handleZoomIn() {
        if (zoomFactor < MAX_ZOOM) {
            zoomFactor += ZOOM_INCREMENT;
            zoom(zoomFactor);
        }
    }

    private void handleZoomOut() {
        if (zoomFactor > MIN_ZOOM) {
            zoomFactor -= ZOOM_INCREMENT;
            zoom(zoomFactor);
        }
    }

    private void zoom(double newZoomFactor) {
        zoomTimeline = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                svgPane.setScaleX(newZoomFactor);
                svgPane.setScaleY(newZoomFactor);

                for (Pane pane : apLocations.keySet()) {
                    for (Node node : pane.getChildren()) {
                        if (node instanceof ImageView || node instanceof javafx.scene.shape.Path) {
                            node.setScaleX(newZoomFactor);
                            node.setScaleY(newZoomFactor);
                        }
                    }
                }

                updateElementPositions();
                centerZoomedContent(svgPane);
            }
        }));
        zoomTimeline.play();
    }

    private void centerZoomedContent(Pane contentPane) {

        Bounds contentBounds = contentPane.getBoundsInLocal();


        double centerX = contentBounds.getMinX() + contentBounds.getWidth() / 2;
        double centerY = contentBounds.getMinY() + contentBounds.getHeight() / 2;

        double translateX = contentPane.getLayoutX() - centerX;
        double translateY = contentPane.getLayoutY() - centerY;


        contentPane.setTranslateX(translateX);
        contentPane.setTranslateY(translateY);
    }

    private void updateElementPositions() {

        for (Pane pane : apLocations.keySet()) {
            for (AccessPoint ap : apLocations.get(pane)) {

                for (Node node : pane.getChildren()) {
                    if (node instanceof ImageView) {
                        ImageView imageView = (ImageView) node;
                        if (Math.abs(imageView.getLayoutX() + ClickOffset_X - ap.x) < 5 &&
                                Math.abs(imageView.getLayoutY() + ClickOffset_Y - ap.y) < 5) {
                            imageView.setLayoutX((ap.x - ClickOffset_X) * zoomFactor);
                            imageView.setLayoutY((ap.y - ClickOffset_Y) * zoomFactor);
                        }
                    }
                }
            }
        }
        // Walls
        for (Wall wall : walls) {
            for (Node node : centerPane.getChildren()) {
                if (node instanceof javafx.scene.shape.Line) {
                    javafx.scene.shape.Line line = (javafx.scene.shape.Line) node;
                    if (checkLineMatchesWall(line, wall)) {
                        line.setStartX(wall.line.start.x * zoomFactor);
                        line.setStartY(wall.line.start.y * zoomFactor);
                        line.setEndX(wall.line.end.x * zoomFactor);
                        line.setEndY(wall.line.end.y * zoomFactor);
                    }
                }
            }
        }  // Walls
        for (Wall wall : walls) {

            for (Node node : centerPane.getChildren()) {
                if (node instanceof javafx.scene.shape.Line) {
                    javafx.scene.shape.Line line = (javafx.scene.shape.Line) node;
                    if (checkLineMatchesWall(line, wall)) {
                        line.setStartX(wall.line.start.x * zoomFactor);
                        line.setStartY(wall.line.start.y * zoomFactor);
                        line.setEndX(wall.line.end.x * zoomFactor);
                        line.setEndY(wall.line.end.y * zoomFactor);
                    }
                }
            }
        }

        // Wires
        for (Wire wire : wires) {

            for (Node node : centerPane.getChildren()) {
                if (node instanceof javafx.scene.shape.Line) {
                    javafx.scene.shape.Line line = (javafx.scene.shape.Line) node;
                    if (checkLineMatchesWire(line, wire)) {
                        line.setStartX(wire.line.start.x * zoomFactor);
                        line.setStartY(wire.line.start.y * zoomFactor);
                        line.setEndX(wire.line.end.x * zoomFactor);
                        line.setEndY(wire.line.end.y * zoomFactor);
                    }
                }
            }
        }
    }

    private void handlePan() {
        final double[] initialOffset = {svgPane.getLayoutX(), svgPane.getLayoutY()};

        svgPane.setOnMousePressed(event -> {
            if (panningMode) {
                double initialOffsetX = initialOffset[0];
                double initialOffsetY = initialOffset[1];

                event.consume();
            }
        });

        svgPane.setOnMouseDragged(event -> {
            if (panningMode) {

                double deltaX = event.getX() - event.getSceneX() + initialOffset[0];
                double deltaY = event.getY() - event.getSceneY() + initialOffset[1];

                Bounds svgBounds = svgPane.getBoundsInLocal();


                double newLayoutX = Math.max(0, Math.min(stage.getWidth() - svgBounds.getWidth(), deltaX));
                double newLayoutY = Math.max(0, Math.min(stage.getHeight() - svgBounds.getHeight(), deltaY));


                svgPane.setLayoutX(newLayoutX);
                svgPane.setLayoutY(newLayoutY);
            }
        });

    }


    //Shapes and coordinates
    private String extractCoordinates(javafx.scene.shape.Path path) {
        StringBuilder builder = new StringBuilder();
        ObservableList<PathElement> elements = path.getElements();
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) instanceof MoveTo) {
                MoveTo moveTo = (MoveTo) elements.get(i);
                builder.append("(").append(path.getStrokeWidth()).append(", (")
                        .append(moveTo.getX()).append(",").append(moveTo.getY()).append(")");
            } else if (elements.get(i) instanceof LineTo) {
                LineTo lineTo = (LineTo) elements.get(i);
                builder.append(" -> (").append(lineTo.getX()).append(",").append(lineTo.getY()).append("))");
            }
        }
        return builder.toString();
    }

    private void convertToJavaFXShapes(List<Group> groups, Pane targetPane)  {

        targetPane.getChildren().clear();
        addButtonGroup();


        if (svgPane == null) {
            svgPane = new Pane();
            svgPane.setPrefSize(800, 600);
            svgPane.setLayoutX(54);
            svgPane.setLayoutY(42);
        } else {

            svgPane.getChildren().clear();
        }


        for (Group group : groups) {
            for (Line line : group.lines) {
                Path path = new Path();


                path.getElements().add(new MoveTo(line.start.x, line.start.y));


                path.getElements().add(new LineTo(line.end.x, line.end.y));

                svgPane.getChildren().add(path);
            }
        }


        if (!targetPane.getChildren().contains(svgPane)) {
            targetPane.getChildren().add(svgPane);
        }
    }

    private void parseSVGCoordinates(String svgPath) throws IOException {
        FileInputStream fis = new FileInputStream(svgPath);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        StringBuilder sb = new StringBuilder();
        String content;
        while ((content = reader.readLine()) != null) {
            sb.append(content);
        }
        String svgContent = sb.toString();

        Document doc = Jsoup.parse(svgContent, "", org.jsoup.parser.Parser.xmlParser());
        Elements paths = doc.select("path");
        List<Group> groups = new ArrayList<>();
        for (Element path : paths) {
            String d = path.attr("d");
            List<Point> points = parsePathCoordinates(d);
            Group group = new Group();
            for (int i = 0; i < points.size() - 1; i++) {
                Point start = points.get(i);
                Point end = points.get(i + 1);
                Line line = new Line(start, end);
                group.addLine(line);
            }
            groups.add(group);
        }

        Tab currentTab = planTabPane.getSelectionModel().getSelectedItem();

        Node tabContent = currentTab.getContent();

        if (tabContent instanceof Pane) {
            Pane tabPane = (Pane) tabContent;

            Platform.runLater(() -> {
                try {
                    convertToJavaFXShapes(groups, tabPane);
                } catch (Exception e) {
                    System.err.println("Error converting SVG coordinates to JavaFX shapes: " + e.getMessage());
                }
            });
        } else {
            System.err.println("Error: Tab content is not a Pane.");
        }

        displayGroups(groups);
    }

    private List<Point> parsePathCoordinates(String d) {
        List<Point> points = new ArrayList<>();
        String[] segments = d.split("[MLZ]");
        for (String segment : segments) {
            if (!segment.trim().isEmpty()) {
                String[] xyPairs = segment.trim().split("\\s*,\\s*");
                for (String xyPair : xyPairs) {
                    String[] coords = xyPair.trim().split("\\s+");
                    try {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        points.add(new Point(x, y));
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error parsing coordinates: " + xyPair);
                    }
                }
            }
        }
        return points;
    }

    //Loading
    private void handleFileOpen() {
        Stage currentStage = (Stage) OpenFile.getScene().getWindow();

        if (currentStage == null) {
            System.err.println("Stage is null!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select DWG File");
        Window window = currentStage.getScene().getWindow();
        if (window != null) {
            FileChooser.ExtensionFilter dwgFilter = new FileChooser.ExtensionFilter("DWG files (*.dwg)", "*.dwg");
            fileChooser.getExtensionFilters().add(dwgFilter);
            File selectedFile = fileChooser.showOpenDialog(window);
            if (selectedFile != null) {
                try {
                    String apiSecret = System.getenv("CONVERTAPI_SECRET");
                    if (apiSecret == null) {
                        System.err.println("Error: CONVERTAPI_SECRET environment variable not set!");
                        return;
                    }
                    Config.setDefaultSecret(apiSecret);


                    java.nio.file.Path selectedFilePath = selectedFile.toPath();
                    String fileNameWithoutExtension = selectedFilePath.getFileName().toString().replaceFirst("[.][^.]+$", "");

                    cadFileLoader.loadCADFile(selectedFile);

                    CompletableFuture<ConversionResult> svgResult = ConvertApi.convert(
                            "dwg", "svg",
                            new Param("file", selectedFilePath),
                            new Param("ImageHeight", "599"),
                            new Param("ImageWidth", "799"),
                            new Param("ColorSpace", "monochrome")
                    );

                    svgResult.thenAccept(svgConversionResult -> {
                        try {

                            java.nio.file.Path savedPlansPath = java.nio.file.Paths.get("src", "main", "java", "org", "example", "wizone", "savedPlans");

                            java.nio.file.Files.createDirectories(savedPlansPath);

                            java.nio.file.Path svgFilePath = savedPlansPath.resolve(fileNameWithoutExtension + ".svg");
                            svgConversionResult.saveFile(svgFilePath).get();
                            System.out.println("Conversion to SVG successful!");


                            cadFileLoader.loadSVGFile(svgFilePath.toFile());


                            parseSVGCoordinates(svgFilePath.toString());
                        } catch (Exception e) {
                            System.err.println("Error saving SVG: " + e.getMessage());
                        }
                    });

                } catch (Exception e) {
                    System.err.println("Conversion failed: " + e.getMessage());
                }
            } else {
                System.out.println("No file selected.");
            }
        }
    }

    private void handleSavedPlanOpen(String selectedFile) {
        try {
            java.nio.file.Path savedPlansPath = java.nio.file.Paths.get("src", "main", "java", "org", "example", "wizone", "savedPlans");
            java.nio.file.Path svgFilePath = savedPlansPath.resolve(selectedFile + ".svg");


            cadFileLoader.loadSVGFile(svgFilePath.toFile());


            walls.clear();
            wires.clear();
            apLocations.clear();


            parseSVGCoordinates(svgFilePath.toString());
        } catch (IOException e) {
            System.err.println("Error loading saved plan: " + e.getMessage());
        }
    }


    //Show original
    private void showOriginalSVG() {

        if (svgPane != null) {
            svgPane.setVisible(true);
        }


        hideAccessPoints();
        hideWallsAndWires();
    }

    private void showEverything() {
        showAccessPoints();
        showWallsAndWires();
    }

    private void hideAccessPoints() {
        for (Pane pane : apLocations.keySet()) {
            for (Node node : pane.getChildren()) {
                if (node instanceof ImageView) {
                    node.setOpacity(0);
                }
            }
        }
    }

    private void showAccessPoints() {
        for (Pane pane : apLocations.keySet()) {
            for (Node node : pane.getChildren()) {
                if (node instanceof ImageView) {
                    node.setOpacity(1);
                }
            }
        }
    }

    private void hideWallsAndWires() {
        // Hide walls
        for (Wall wall : walls) {

            for (Node node : centerPane.getChildren()) {
                if (node instanceof javafx.scene.shape.Line) {
                    javafx.scene.shape.Line line = (javafx.scene.shape.Line) node;
                    if (checkLineMatchesWall(line, wall)) {
                        line.setOpacity(0);
                        break;
                    }
                }
            }
        }

        // Hide wires
        for (Wire wire : wires) {

            for (Node node : centerPane.getChildren()) {
                if (node instanceof javafx.scene.shape.Line) {
                    javafx.scene.shape.Line line = (javafx.scene.shape.Line) node;
                    if (checkLineMatchesWire(line, wire)) {
                        line.setOpacity(0);
                        break;
                    }
                }
            }
        }
    }

    private void showWallsAndWires() {
        // Show walls
        for (Wall wall : walls) {
            for (Node node : centerPane.getChildren()) {
                if (node instanceof javafx.scene.shape.Line) {
                    javafx.scene.shape.Line line = (javafx.scene.shape.Line) node;
                    if (checkLineMatchesWall(line, wall)) {
                        line.setOpacity(1);
                        break;
                    }
                }
            }
        }

        // Show wires
        for (Wire wire : wires) {

            for (Node node : centerPane.getChildren()) {
                if (node instanceof javafx.scene.shape.Line) {
                    javafx.scene.shape.Line line = (javafx.scene.shape.Line) node;
                    if (checkLineMatchesWire(line, wire)) {
                        line.setOpacity(1);
                        break;
                    }
                }
            }
        }
    }

    private boolean checkLineMatchesWall(javafx.scene.shape.Line line, Wall wall) {
        return (line.getStartX() == wall.line.start.x && line.getStartY() == wall.line.start.y &&
                line.getEndX() == wall.line.end.x && line.getEndY() == wall.line.end.y);
    }

    private boolean checkLineMatchesWire(javafx.scene.shape.Line line, Wire wire) {
        return (line.getStartX() == wire.line.start.x && line.getStartY() == wire.line.start.y &&
                line.getEndX() == wire.line.end.x && line.getEndY() == wire.line.end.y);
    }




    //Access point
    public static class AccessPoint {
        public double x;
        public double y;
        public int tabNumber;
        public String brand;
        public String model;
        public String name;
        public int transmitPower24GHz;
        public int transmitPower5GHz;

        public AccessPoint(double x, double y, int tabNumber, String brand, String model, String name, int transmitPower24GHz, int transmitPower5GHz) {
            this.x = x;
            this.y = y;
            this.tabNumber = tabNumber;
            this.brand = brand;
            this.model = model;
            this.name = name;
            this.transmitPower24GHz = transmitPower24GHz;
            this.transmitPower5GHz = transmitPower5GHz;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + "), " + tabNumber + ", " + brand + ", " + model + ", " + name + ", " + transmitPower24GHz + ", " + transmitPower5GHz;
        }

    }

    private void loadAPPricesFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String brand = parts[0].trim();
                    String model = parts[1].trim();
                    int price = Integer.parseInt(parts[2].trim());

                    apPrices.computeIfAbsent(brand, k -> new HashMap<>()).put(model, price);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading AP prices from file: " + e.getMessage());
        }
    }

    private void loadAPPricesFromFile() {
        String filePath = "F:\\Uni\\Bachelor\\Intellij\\WiZone\\src\\main\\java\\org\\example\\wizone\\AccessPoint\\costOfAP.txt";
        loadAPPricesFromFile(filePath);
    }

    private int calculateTotalCost() {
        int totalCost = 0;
        for (Pane pane : apLocations.keySet()) {
            for (AccessPoint ap : apLocations.get(pane)) {
                int price = apPrices.getOrDefault(ap.brand, Collections.emptyMap()).getOrDefault(ap.model, 0);
                totalCost += price;
            }
        }
        System.out.println("Total cost of Access Points: " + totalCost);
        return totalCost;
    }



    private void deleteAPIcon(Pane pane, MouseEvent event) {
        // Get the clicked node
        Node clickedNode = event.getPickResult().getIntersectedNode();


        if (clickedNode instanceof ImageView) {
            pane.getChildren().remove(clickedNode);


            List<AccessPoint> apList = apLocations.get(pane);
            if (apList != null) {
                double adjustedX = clickedNode.getLayoutX();
                double adjustedY = clickedNode.getLayoutY();


                Iterator<AccessPoint> iterator = apList.iterator();
                while (iterator.hasNext()) {
                    AccessPoint ap = iterator.next();
                    if (ap.x == adjustedX - 58 && ap.y == adjustedY - 50) {
                        System.out.println("Deleted Access Point: " + ap);

                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    private void placeAPIcon(Pane pane, MouseEvent event) {
        Image image = new Image(getClass().getResource("/org/example/wizone/assets/apIcon.png").toExternalForm());

        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        double x = event.getX();
        double y = event.getY();

        Point2D pointInParent = pane.localToParent(x, y);

        double adjustedX = pointInParent.getX() + 4;
        double adjustedY = pointInParent.getY() + 28;

        imageView.setLayoutX(adjustedX-18);
        imageView.setLayoutY(adjustedY-40);


        pane.getChildren().add(imageView);


        double realX = adjustedX - 58;
        double realY = adjustedY - 70;

        int tabIndex = planTabPane.getSelectionModel().getSelectedIndex();


        String selectedTech = techCB.getSelectionModel().getSelectedItem();
        selectedTech = (selectedTech == null) ? "Wi-Fi" : selectedTech;

        String selectedBrand = brandCB.getSelectionModel().getSelectedItem();
        selectedBrand = (selectedBrand == null) ? "Cisco" : selectedBrand;

        String selectedModel = modelCB.getSelectionModel().getSelectedItem();
        if (selectedModel == null) {
            List<String> modelsForBrand = brandToModels.getOrDefault(selectedBrand, Collections.emptyList());
            if (!modelsForBrand.isEmpty()) {
                selectedModel = modelsForBrand.getFirst();
                modelCB.setValue(selectedModel);
            } else {
                selectedModel = "1140i";
            }
        }

        String apNameValue = apName.getText();
        String name = (apNameValue.isEmpty()) ? ("Access Point " + APCount) : apNameValue;
        int transmitPower24GHz = (FirstTF.getText().isEmpty()) ? 6 : Integer.parseInt(FirstTF.getText());
        int transmitPower5GHz = (SecondTF.getText().isEmpty()) ? 10 : Integer.parseInt(SecondTF.getText());


        AccessPoint ap = new AccessPoint(realX, realY, tabIndex + 1, selectedBrand, selectedModel, name, transmitPower24GHz, transmitPower5GHz);
        apLocations.computeIfAbsent(pane, k -> new ArrayList<>()).add(ap);
        APCount++;

        for (Map.Entry<Pane, List<AccessPoint>> entry : apLocations.entrySet()) {
            if (entry.getKey().equals(pane)) {
                List<AccessPoint> points = entry.getValue();
                for (AccessPoint point : points) {
                    System.out.println(point);
                }
            }
        }

        apPlacementMode = false;
    }

    private void handleEditAP(Pane pane, MouseEvent event) {
        if (apEditMode) {
            Node clickedNode = event.getPickResult().getIntersectedNode();

            if (clickedNode instanceof ImageView) {

                if (selectedAPImageView != null) {
                    selectedAPImageView.setEffect(null);
                }

                selectedAPImageView = (ImageView) clickedNode;


                List<AccessPoint> apList = apLocations.get(pane);
                if (apList != null) {
                    selectedAP = null;

                    double clickedX = event.getX();
                    double clickedY = event.getY();


                    for (AccessPoint ap : apList) {
                        double apCenterX = ap.x + (clickedNode.getLayoutX() / 2);
                        double apCenterY = ap.y + (clickedNode.getLayoutY() / 2);
                        double distance = Math.sqrt(Math.pow(clickedX - apCenterX, 2) + Math.pow(clickedY - apCenterY, 2));

                        if (selectedAP == null || distance < Math.sqrt(Math.pow(clickedX - selectedAP.x, 2) + Math.pow(clickedY - selectedAP.y, 2))) {
                            selectedAP = ap;
                        }
                    }

                    if (selectedAP != null) {
                        selectedAPImageView.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLUEVIOLET, 15, 0.5, 0, 0));
                        brandCB.setValue(selectedAP.brand);
                        modelCB.setValue(selectedAP.model);
                        apName.setText(selectedAP.name);
                        FirstTF.setText(String.valueOf(selectedAP.transmitPower24GHz));
                        SecondTF.setText(String.valueOf(selectedAP.transmitPower5GHz));
                    }
                }
            }
        }
    }


    //Walls

    public static class Wall {
            public double thickness;
            public Color color;
            public Line line;

            public Wall(double thickness, Color color, Line line) {
                this.thickness = thickness;
                this.color = color;
                this.line = line;
            }

            @Override
            public String toString() {
                return "(" + thickness + ", (" + line.start.x + "," + line.start.y + ") -> (" + line.end.x + "," + line.end.y + "))";
            }

        }

    private void printWalls() {
        System.out.println("Walls:");
        for (Wall wall : walls) {
            System.out.println(wall);
        }
    }

    private void handleWallDrawing(Pane pane, MouseEvent event) {
        if (wallStartPoint == null) {
            wallStartPoint = new Point2D(event.getX(), event.getY());
            System.out.println("Wall start point: " + wallStartPoint);
        } else {
            Point2D wallEndPoint = new Point2D(event.getX(), event.getY());
            System.out.println("Wall end point: " + wallEndPoint);

            String selectedMaterial = wallMaterialCB.getValue();
            if (selectedMaterial != null) {

                double thickness = wallThicknessMap.getOrDefault(selectedMaterial, 3.5);
                Color color = wallColorMap.getOrDefault(selectedMaterial, Color.DIMGRAY);


                Wall wall = new Wall(thickness, color,
                        new Line(new Point(wallStartPoint.getX(), wallStartPoint.getY()),
                                new Point(wallEndPoint.getX(), wallEndPoint.getY())));
                walls.add(wall);


                javafx.scene.shape.Line line = new javafx.scene.shape.Line(
                        wallStartPoint.getX(), wallStartPoint.getY(),
                        wallEndPoint.getX(), wallEndPoint.getY());
                line.setStrokeWidth(thickness);
                line.setStroke(color);
                pane.getChildren().add(line);

                wallStartPoint = wallEndPoint;
            } else {
                System.out.println("No material selected. Using default settings.");
            }
        }
    }

    private void handleWallDeletion(AnchorPane anchorPane, MouseEvent event) {
            Node clickedNode = event.getPickResult().getIntersectedNode();
            if (clickedNode instanceof Path) {

                Pane parent = (Pane) clickedNode.getParent();
                if (parent != null) {
                    parent.getChildren().remove(clickedNode);
                }

            }
        }

    private void handleLineSelection(AnchorPane anchorPane, MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode instanceof Path) {
            Path clickedPath = (Path) clickedNode;

            clickedPath.setStroke(Color.BLUEVIOLET);
            selectedWalls.add(clickedPath);
        }
}


    private void printSelectedWalls() {
        System.out.println("Walls:");
        for (javafx.scene.shape.Path path : selectedWalls) {
            String coordinates = extractCoordinates(path);
            System.out.println(coordinates);
        }
    }


    private void displayGroups(List<Group> groups) {
        System.out.println("Parsing done");
        System.out.println("Extracting coordinates");
        int lineCount = 1;
        for (Group group : groups) {
            for (Line line : group.lines) {
                System.out.println("Line " + lineCount++ + ": " + line);
            }
        }
        System.out.println("Group: " + groups);
    }


    //Saving
    private void savePlanAsImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Plan as Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"),
                new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg")
        );
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try {
                SnapshotParameters params = new SnapshotParameters();
                WritableImage image = centerPane.snapshot(params, null);

                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);


                ImageIO.write(bufferedImage, selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1), selectedFile);

                System.out.println("Plan saved successfully to: " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error saving plan as image: " + e.getMessage());
            }
        }
    }

    public void saveChanges() {
        if (selectedAP != null) {
            selectedAP.brand = brandCB.getValue();
            selectedAP.model = modelCB.getValue();
            selectedAP.name = apName.getText();
            selectedAP.transmitPower24GHz = Integer.parseInt(FirstTF.getText());
            selectedAP.transmitPower5GHz = Integer.parseInt(SecondTF.getText());


            System.out.println("Updated Access Point: " + selectedAP);


            System.out.println("Changes saved successfully!");

            if (selectedAPImageView != null) {
                selectedAPImageView.setEffect(null);
                selectedAPImageView = null;
            }


            apEditMode = false;
            System.out.println("AP edit mode deactivated");


            for (Pane pane : apLocations.keySet()) {
                for (Node node : pane.getChildren()) {
                    if (node instanceof ImageView) {
                        ImageView imageView = (ImageView) node;
                        if (Math.abs(imageView.getLayoutX() + ClickOffset_X - selectedAP.x) < 5 &&
                                Math.abs(imageView.getLayoutY() + ClickOffset_Y - selectedAP.y) < 5) {
                        }
                    }
                }
            }
        } else {
            System.out.println("Please select an Access Point to edit.");
        }
    }

    private List<Group> parseSVGCoordinatesRes(String svgPath) {
        List<Group> groups = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(svgPath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String content;
            while ((content = reader.readLine()) != null) {
                sb.append(content);
            }
            String svgContent = sb.toString();

            Document doc = Jsoup.parse(svgContent, "", org.jsoup.parser.Parser.xmlParser());
            Elements paths = doc.select("path");

            for (Element path : paths) {
                String d = path.attr("d");
                List<Point> points = extractCoordinatesRes(d);
                Group group = new Group();
                for (int i = 0; i < points.size() - 1; i++) {
                    Point start = points.get(i);
                    Point end = points.get(i + 1);
                    Line line = new Line(start, end);
                    group.addLine(line);
                }
                groups.add(group);
            }

            return groups;
        } catch (IOException e) {
            System.err.println("Error extracting SVG coordinates: " + e.getMessage());
            return groups;
        }
    }

    private List<Point> extractCoordinatesRes(String d) {
        List<Point> points = new ArrayList<>();
        String[] segments = d.split("[MLZ]");
        for (String segment : segments) {
            if (!segment.trim().isEmpty()) {
                String[] xyPairs = segment.trim().split("\\s*,\\s*");
                for (String xyPair : xyPairs) {
                    String[] coords = xyPair.trim().split("\\s+");
                    try {
                        double x = Double.parseDouble(coords[0]);
                        double y = Double.parseDouble(coords[1]);
                        points.add(new Point(x, y));
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error parsing coordinates: " + xyPair);
                    }
                }
            }
        }
        return points;
    }

    private void generateResultsFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save WiZone Plan Results");
        fileChooser.setInitialFileName("wizone_plan_results.txt");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt")
        );

        Stage stage = (Stage) saveButton.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try (PrintWriter writer = new PrintWriter(selectedFile)) {
                writer.println("## WiZone Plan Results");
                writer.println();
                writer.println("### Walls");
                writer.println();

                for (Wall wall : walls) {
                    writer.println("- Thickness: " + wall.thickness + " units");
                    writer.println("- Color: " + wall.color);
                    writer.println("- Line: (" + wall.line.start.x + ", " + wall.line.start.y + ") -> (" + wall.line.end.x + ", " + wall.line.end.y + ")");
                    writer.println();
                }

                writer.println("### Access Points");
                writer.println();

                int apCount = 1;
                for (Pane pane : apLocations.keySet()) {
                    for (AccessPoint ap : apLocations.get(pane)) {
                        writer.println("Access Point " + apCount++);
                        writer.println("- Brand: " + ap.brand);
                        writer.println("- Model: " + ap.model);
                        writer.println("- Name: " + ap.name);
                        writer.println("- Transmit Power (2.4GHz): " + ap.transmitPower24GHz);
                        writer.println("- Transmit Power (5GHz): " + ap.transmitPower5GHz);
                        writer.println("- Position: (" + ap.x + ", " + ap.y + ")");
                        writer.println("- Price: " + apPrices.getOrDefault(ap.brand, Collections.emptyMap()).getOrDefault(ap.model, 0));
                        writer.println();
                    }
                }

                if (!selectedWalls.isEmpty()) {
                    writer.println("Selected Walls:");

                    for (int i = 0; i < selectedWalls.size(); i++) {
                        Path selectedPath = selectedWalls.get(i);
                        writer.println("Wall " + (i + 1) + ": " + extractCoordinates(selectedPath));
                    }
                }

                writer.println("### Wires");
                writer.println();

                for (Wire wire : wires) {
                    writer.println("- Technology: " + wire.technology);
                    writer.println("- Color: " + wire.color);
                    writer.println("- Thickness: " + wire.thickness + " units");
                    writer.println("- Line: (" + wire.line.start.x + ", " + wire.line.start.y + ") -> (" + wire.line.end.x + ", " + wire.line.end.y + ")");
                    writer.println();
                }

                writer.println("### SVG Coordinates");
                writer.println();

                List<Group> groups = parseSVGCoordinatesRes(cadFileLoader.getSVGFilePath());

                int lineCount = 1;
                for (Group group : groups) {
                    for (Line line : group.lines) {
                        writer.println("Line " + lineCount++ + ": [(" + line.start.x + ", " + line.start.y + ") -> (" + line.end.x + ", " + line.end.y + ")]");
                    }
                }

                writer.println();
                writer.println("Total Cost of Access Points: " + calculateTotalCost());
                writer.println();

            } catch (IOException e) {
                System.err.println("Error saving results file: " + e.getMessage());
            }
        }
    }


    //Wires
    public static class Wire {
        public String technology;
        public String color;
        public int thickness;
        public Line line;

        public Wire(String technology, String color, int thickness, Line line) {
            this.technology = technology;
            this.color = color;
            this.thickness = thickness;
            this.line = line;
        }

        @Override
        public String toString() {
            return "(" + technology + ", " + color + ", " + thickness + ", (" + line.start.x + "," + line.start.y + ") -> (" + line.end.x + "," + line.end.y + "))";
        }
    }

    private void printWires() {
        System.out.println("Wires:");
        for (Wire wire : wires) {
            System.out.println(wire);
        }
    }

    private void handleWiring(Pane pane, MouseEvent event) {
        if (wiringStartPoint == null) {
            wiringStartPoint = new Point2D(event.getX(), event.getY());
            System.out.println("Wiring start point: " + wiringStartPoint);
        } else {
            Point2D wiringEndPoint = new Point2D(event.getX(), event.getY());
            System.out.println("Wiring end point: " + wiringEndPoint);


            String selectedTech = techTypeCB.getSelectionModel().getSelectedItem();
            selectedTech = (selectedTech == null) ? "Cat5e" : selectedTech;
            String selectedColor = colorCB.getSelectionModel().getSelectedItem();
            selectedColor = (selectedColor == null) ? "Blue" : selectedColor;
            int selectedThickness = thicknessCB.getSelectionModel().getSelectedIndex() + 1;
            selectedThickness = (selectedThickness == 0) ? 3 : selectedThickness;
            Wire wire = new Wire(selectedTech, selectedColor, selectedThickness,
                    new Line(new Point(wiringStartPoint.getX(), wiringStartPoint.getY()),
                            new Point(wiringEndPoint.getX(), wiringEndPoint.getY())));
            wires.add(wire);


            javafx.scene.shape.Line line = new javafx.scene.shape.Line(
                    wiringStartPoint.getX(), wiringStartPoint.getY(),
                    wiringEndPoint.getX(), wiringEndPoint.getY());
            line.setStrokeWidth(selectedThickness);
            line.setStroke(Color.web(getColorFromName(selectedColor)));
            pane.getChildren().add(line);

            wiringStartPoint = wiringEndPoint;

        }
    }

    private String getColorFromName(String colorName) {
        switch (colorName) {
            case "Blue":
                return "#0000FF";
            case "Green":
                return "#008000";
            case "Red":
                return "#FF0000";
            case "Yellow":
                return "#FFFF00";
            case "Black":
                return "#000000";
            case "White":
                return "#FFFFFF";
            default:
                return "#0000FF";
        }
    }




    //Backup
//    private void displayImage(String imagePath) {
//        File imageFile = new File(imagePath);
//        try {
//            URL imageUrl = imageFile.toURI().toURL();
//            javafx.scene.image.Image image = new javafx.scene.image.Image(imageUrl.toExternalForm());
//

//            Tab activeTab = planTabPane.getSelectionModel().getSelectedItem();
//
//
//            ImageView activeImageView = null;
//            if (activeTab.getContent() instanceof Parent contentParent) {
//                for (Node child : contentParent.getChildrenUnmodifiable()) {
//                    if (child instanceof ImageView) {
//                        activeImageView = (ImageView) child;
//                        activeImageView.setImage(image);
//                        break;
//                    }
//                }
//            } else {
//                System.err.println("Tab content is not a Parent node.");
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
////    }
//    private void printForDesmos(List<Group> groups) {
//        for (Group group : groups) {
//            for (Line line : group.lines) {
//                System.out.println(line.start.x + "," + line.start.y);
//                System.out.println(line.end.x + "," + line.end.y);
//            }
//        }
//    }
    //    private void printSelectedWalls() {
//        System.out.println("Walls:");
//        for (javafx.scene.shape.Path path : selectedWalls) {
//            String coordinates = extractCoordinates(path);
//            System.out.println(coordinates);
//        }
//    }

}