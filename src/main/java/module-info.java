module org.example.wizone {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires MaterialFX;
    requires jdk.jconsole;
    requires aspose.cad;
    requires org.apache.pdfbox;
    requires jdk.jsobject;
    requires org.slf4j;
    requires commons.math3;
    requires scenic.view;
    requires javafx.swing;
    requires convertapi;
    requires batik.parser;
    requires org.apache.commons.io;
    requires org.jsoup;


    opens org.example.wizone to javafx.fxml;
    exports org.example.wizone;
    exports org.example.wizone.controller;
    opens org.example.wizone.controller to javafx.fxml;
}