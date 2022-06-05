package com.company.client.Models;

import com.company.client.Client;
import com.company.client.Main.VirusApplication;
import com.company.share.CellKind;
import com.company.share.PackageObj;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Cell {
    public Cell(int _x, int _y) throws FileNotFoundException {
        x = _x;
        y = _y;
        cellKind = CellKind.cell;
        Image image = new Image(new FileInputStream("src/image/cell.png"));
        imageView = new ImageView(image);
        imageView.setX(_x + 1);
        imageView.setY(_y + 1);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imageView.setPreserveRatio(true);
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                if (VirusApplication.getInstance().isAvailableCell(x / 70, y / 70, cellKind))
                    clickAction(x / 70, y / 70);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            event.consume();
        });
    }

    public void clickAction(int _x, int _y) throws IOException, ClassNotFoundException {
        boolean validClick = false;
        switch (cellKind) {
            case crossMark -> {
                if (VirusApplication.getInstance().moveStatus == CellKind.zeroMark) {
                    setFilledCrossMark();
                    validClick = true;
                }
            }
            case zeroMark -> {
                if (VirusApplication.getInstance().moveStatus == CellKind.crossMark) {
                    setFilledZeroMark();
                    validClick = true;
                }
            }
            case cell -> {
                if (VirusApplication.getInstance().moveStatus == CellKind.crossMark) {
                    setCrossMark();
                } else {
                    setZeroMark();
                }
                VirusApplication.getInstance().cellCount--;
                validClick = true;
            }
            default -> System.out.println("INVALID");
        }
        if (validClick) {
            System.out.println("Clicked");
            if (Client.getInstance() != null && !Client.getInstance().closeFlag && VirusApplication.getInstance().moveStatus == VirusApplication.getInstance().clientKind)
                Client.getInstance().sendMove(new PackageObj(_x, _y, false));
            else System.out.println("Error");
            VirusApplication.getInstance().addClickCount(_x, _y);
        }
    }

    public void setCrossMark() throws FileNotFoundException {
        VirusApplication.getInstance().crossCellCount++;
        Image image = new Image(new FileInputStream("src/image/crossMark.png"));
        imageView.setImage(image);
        cellKind = CellKind.crossMark;
    }

    public void setFilledCrossMark() throws FileNotFoundException {
        VirusApplication.getInstance().crossCellCount--;
        Image image = new Image(new FileInputStream("src/image/filledCrossMark.png"));
        imageView.setImage(image);
        cellKind = CellKind.filledCrossMark;
    }

    public void setZeroMark() throws FileNotFoundException {
        VirusApplication.getInstance().zeroCellCount++;
        Image image = new Image(new FileInputStream("src/image/circle.png"));
        imageView.setImage(image);
        cellKind = CellKind.zeroMark;
    }

    public void setFilledZeroMark() throws FileNotFoundException {
        VirusApplication.getInstance().zeroCellCount--;
        Image image = new Image(new FileInputStream("src/image/filledCircle.png"));
        imageView.setImage(image);
        cellKind = CellKind.filledZeroMark;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean checked = false;
    private final int x, y;
    private final ImageView imageView;
    public CellKind cellKind;
}
