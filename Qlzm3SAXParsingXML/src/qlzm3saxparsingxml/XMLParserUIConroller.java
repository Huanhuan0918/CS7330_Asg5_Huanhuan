/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qlzm3saxparsingxml;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;

/**
 *
 * @author leiqi
 */

public class XMLParserUIConroller implements Initializable {

    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(textArea.getScene().getWindow());

        textArea.clear();
        if (file != null) {
            try {
                XMLNode node = XMLParser.load(file);
                
                display(node);
                
            } catch (Exception ex) {
                displayExceptionAlert("Exception parsing XML file.", ex);
            }
        }
    }

    private void display(XMLNode staff) {        
        for (Entry<String, ArrayList<XMLNode>> detail : staff.properties.entrySet()) {
            ArrayList<XMLNode> detailList = detail.getValue();
            for (XMLNode subDetail : detailList) {
                String attributeString = " ";
                if(!subDetail.attributes.isEmpty()){
                    attributeString = attributeString + "(";
                    for (Entry<String, String> attributes : subDetail.attributes.entrySet()){
                        attributeString = attributeString + " @" + attributes.getKey() + ": " + attributes.getValue() + " ";
                    }
                    attributeString = attributeString + ") : ";
                }
                textArea.appendText(subDetail.getSpace() + subDetail.name + ": " + attributeString + subDetail.content + "\n");
                if(!subDetail.properties.isEmpty()){
                    display(subDetail);                   
                }
            }           
        }
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        displayAboutAlert();
    }

    private void displayAboutAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Java SAX parsing XML DOM example.");
        alert.setContentText("This application was developed by Huanhuan Xia for CS7330 in University of Missouri-Columbia.");

        TextArea textArea = new TextArea("This example illustrates how to parse XML using Java SAX.");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    private void displayExceptionAlert(String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception!");
        alert.setContentText(message);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
