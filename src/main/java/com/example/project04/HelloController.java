package com.example.project04;

import javafx.beans.InvalidationListener;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    DragController dragController;
    @FXML
    private MenuItem cmSquare,cmCircle,cmTriangle;
    @FXML
    public ColorPicker colorPicker;
    @FXML
    ToggleButton button1,button2,button3,button4,button5,button6;
    @FXML
    private ListView<String> myListView;
    @FXML
    private GridPane buttonPane;
    @FXML
    public RadioButton checkBox;
    @FXML
    public  Pane myPane;


    public static int id = 0;
    public static ObservableList<Shape>list = FXCollections.observableArrayList();
    public  ObservableList<DragController> observe= FXCollections.observableArrayList();


    String currentShape;
    @FXML
    public void rect(ActionEvent e) {
        System.out.println("rec");
    }

    @FXML
    BorderPane borderPane;

    public void buttonListener(ToggleButton button){
    button.selectedProperty().addListener((obs, oldV, newV) -> button.setText(newV ?"S":"")
    );}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        observe.addListener((ListChangeListener<DragController>) change -> {});
        list.addListener((InvalidationListener) observable -> System.out.println("List invalidaded"));


        ToggleButton[] buttons = {button1,button2,button3,button4,button5,button6};
        cmTriangle.setOnAction(a -> button1.setText(button1.getText().isEmpty() ? "S" : ""));
        cmSquare.setOnAction(a -> button2.setText(button2.getText().isEmpty() ? "S" : ""));
        cmCircle.setOnAction(a -> button3.setText(button3.getText().isEmpty() ? "S" : ""));

        for(ToggleButton s : buttons) buttonListener(s);
        myListView.getSelectionModel().selectedItemProperty().addListener((
                observableValue, s, t1) -> currentShape = myListView.getSelectionModel().getSelectedItem());
    }




    boolean doubleClick(MouseEvent event){return event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2;}

    /**
     * LOTS TO DO!
     * @param shape
     */
    public void drawOnPane(Shape shape){
        id++;
        if(colorPicker.getValue().getOpacity() <= 0.2) {
            shape.setStroke(Color.RED);
            myListView.getItems().add(String.valueOf("Layer-"+id+"- [Empty][Stroke:RED] " + shape));
        }

        else
            myListView.getItems().add(String.valueOf("Layer-"+id+"- [Filled]"+shape));

        shape.setFill(colorPicker.getValue());
        myPane.getChildren().add(shape);
        list.add(shape);
        this.dragController = new DragController(shape,true);

    }
    @FXML
    void drawShape(MouseEvent event) {
        if((button1.getText().equals("S")) && doubleClick(event)) {
            Polygon triangle = new Polygon();
            triangle.getPoints().setAll(event.getX(),event.getY(),event.getX()+15,event.getY()-20,event.getX()+30,event.getY());
            drawOnPane(triangle);
        }
        if(button2.getText().equals("S") && doubleClick(event)) {
            Rectangle rectangle = new Rectangle(50,50,50,50);
            Rectangle rectangle2 = new Rectangle(100,100,50,50);
            Shape shape = Shape.union(rectangle,rectangle2);
            shape.setLayoutX(event.getX()-100);
            shape.setLayoutY(event.getY()-100);
            drawOnPane(shape);
        }

        if(button3.getText().equals("S") && doubleClick(event)){
            Circle circle0 = new Circle(event.getX(),event.getY(),30);

            drawOnPane(circle0);
            event.consume();
        }
        if(button4.getText().equals("S")&& doubleClick(event)){
            SVGPath svgPath = new SVGPath();
            String path = "M 25 20 Q 0 35 50 85 Q 100 35 75 20 Q 50 15 50 40 Q 50 15 25 20  ";
            svgPath.setContent(path);
            svgPath.setLayoutX(event.getX()-50);
            svgPath.setLayoutY(event.getY()-50);
            drawOnPane(svgPath);
            event.consume();
        }
        if(button5.getText().equals("S") && doubleClick(event)){
            Line line = new Line(0, 40, 150, 40);
            Shape rect = new Rectangle(150, 150);
            Shape combined = Shape.union(rect,line);
            combined.setLayoutX(event.getX()-75);
            combined.setLayoutY(event.getY()-75);

            drawOnPane(combined);
        }
        if((button6.getText().equals("S"))&& doubleClick(event)){
            Arc arc = new Arc(event.getX()+60,event.getY()+60,80,100,100,60);
            arc.setType(ArcType.CHORD);
            arc.setFill(Color.RED);
            drawOnPane(arc);
        }
    }

}