package com.company.coursework.Models;

import com.company.coursework.VirusApplication;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Cell {
    public Cell(int _x, int _y) throws FileNotFoundException {
        x = _x;
        y = _y;
        Image image = new Image(new FileInputStream("src/image/Zerik.jpg"));
        imageView = new ImageView(image);
        imageView.setX(_x);
        imageView.setY(_y);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imageView.setPreserveRatio(true);
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    clickAction();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                event.consume();
            }
        });
    }

    private void clickAction() throws FileNotFoundException {
        if (VirusApplication.getInstance().moveStatus) {
            setCrossMark();
        } else {
            setZeroMark();
        }
        VirusApplication.getInstance().addClickCount();
    }

    public void setCrossMark() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/image/crossMark.png"));
        imageView.setImage(image);
    }

    public void setZeroMark() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/image/circle.png"));
        imageView.setImage(image);
    }

    public ImageView getImageView() {
        return imageView;
    }

    protected int x, y;
    protected ImageView imageView;
}
