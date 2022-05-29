package com.company.coursework.Models;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class Cell {
    public Cell(int _x, int _y, String path) throws FileNotFoundException {
        x = _x;
        y = _y;
        Image image = new Image(new FileInputStream(path));
        imageView = new ImageView(image);
        imageView.setX(_x);
        imageView.setY(_y);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imageView.setPreserveRatio(true);

    }

    public ImageView getImageView() {
        return imageView;
    }

    protected int x, y;
    protected ImageView imageView;
}
