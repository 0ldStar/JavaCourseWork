package com.company.coursework.Models;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;

public class Zerik extends Cell {
    public Zerik(int _x, int _y) throws FileNotFoundException {
        super(_x, _y, "src/image/circle.png");
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("Tile pressed ");
                event.consume();
            }
        });
    }

}
