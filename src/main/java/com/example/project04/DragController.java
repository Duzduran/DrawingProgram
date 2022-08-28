package com.example.project04;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import java.util.function.Consumer;

public class DragController {

    // @FXML Pane
    private  Node target;
    private double anchorX;
    private double anchorY;
    private double mouseOffsetFromNodeZeroX;
    private double mouseOffsetFromNodeZeroY;
    private EventHandler<MouseEvent> setAnchor;
    private EventHandler<MouseEvent> updatePositionOnDrag;
    private EventHandler<MouseEvent> commitPositionOnRelease;
    private final int ACTIVE = 1;
    private final int INACTIVE = 0;
    private int cycleStatus = INACTIVE;
    private BooleanProperty isDraggable;
    public DragController(Node target, boolean isDraggable) {
        this.target = target;
        createHandlers();
        createDraggableProperty();
        this.isDraggable.set(isDraggable);
    }
    public DragController(){

    }
    private void createHandlers() {
        SimpleDoubleProperty x = new SimpleDoubleProperty();
        SimpleDoubleProperty y = new SimpleDoubleProperty();

        setAnchor = event -> {
            target.layoutXProperty().unbind();
            target.layoutYProperty().unbind();
            if (event.isPrimaryButtonDown()) {
                cycleStatus = ACTIVE;
                anchorX = event.getSceneX()-154;
                anchorY = event.getSceneY()-86;
                mouseOffsetFromNodeZeroX = event.getX();
                mouseOffsetFromNodeZeroY = event.getY();
            }
            if (event.isSecondaryButtonDown()) {
                cycleStatus = INACTIVE;
                target.setTranslateX(0);
                target.setTranslateY(0);
            }
        };
        updatePositionOnDrag = event -> {
            if (cycleStatus != INACTIVE ) {
                target.setCursor(Cursor.CLOSED_HAND);
                target.setTranslateX(event.getSceneX() - anchorX - 154);
                target.setTranslateY(event.getSceneY() - anchorY - 86);
                x.set(event.getSceneX()-154);
                y.set(event.getSceneY() - 86);
            }
        };
        commitPositionOnRelease = event -> {
            if (cycleStatus != INACTIVE ) {
                target.setCursor(Cursor.OPEN_HAND);

                if((event.getSceneY() >95 && event.getSceneY()<610) && (event.getSceneX()>190&& event.getSceneX() <950)) {
                    target.setLayoutX(event.getSceneX() - mouseOffsetFromNodeZeroX - 162);
                    target.setLayoutY(event.getSceneY() - mouseOffsetFromNodeZeroY - 100);
                }
                //clear changes from TranslateX and TranslateY
                target.setTranslateX(0);
                target.setTranslateY(0);
                checkShapeIntersection(HelloController.list);


            }
        };
    }

    public void createDraggableProperty() {
        isDraggable = new SimpleBooleanProperty();
        isDraggable.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                target.addEventFilter(MouseEvent.MOUSE_PRESSED, setAnchor);
                target.addEventFilter(MouseEvent.MOUSE_DRAGGED, updatePositionOnDrag);
                target.addEventFilter(MouseEvent.MOUSE_RELEASED, commitPositionOnRelease);
            } else {
                target.removeEventFilter(MouseEvent.MOUSE_PRESSED, setAnchor);
                target.removeEventFilter(MouseEvent.MOUSE_DRAGGED, updatePositionOnDrag);
                target.removeEventFilter(MouseEvent.MOUSE_RELEASED, commitPositionOnRelease);
            }
        });
    }
    public void checkShapeIntersection(ObservableList<Shape> shapes) {
        shapes.forEach(shape1 -> {
            if (target != shape1) {
                Shape intersect = Shape.intersect((Shape) target,  shape1);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    target.layoutXProperty().bind(shape1.layoutXProperty());
                    target.layoutYProperty().bind(shape1.layoutYProperty());
                }
            }
        });
        }
    }