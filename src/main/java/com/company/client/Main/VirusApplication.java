package com.company.client.Main;

import com.company.client.Client;
import com.company.client.Models.Cell;
import com.company.share.CellKind;
import com.company.client.StartWindow.StartMenu;
import com.company.share.PackageObj;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class VirusApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(VirusApplication.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Virus War");
        instance = this;
        mainController = fxmlLoader.getController();
        initialization();
        stage.setScene(scene);
        stage.show();
    }


    private void initialization() {
        map = new HashMap<>();
        initGame();
    }

    public Cell getCell(int x, int y) {
        return map.get(y * 100 + x);
    }

    public void setUncheckedCellMap() {
        for (var entry : map.entrySet()) {
            entry.getValue().checked = false;
        }
    }



    public void initGame() {
        try {
            StartMenu.showMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finishGameFlag = false;
        cellCount = 100;
        skipMoveKind = CellKind.cell;
        wonCell = CellKind.cell;
        clickCount = 0;
        crossCellCount = 0;
        zeroCellCount = 0;
        moveStatus = CellKind.crossMark;
        mainController.init();
        map.clear();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Cell cell;
                try {
                    cell = new Cell(x * 70, y * 70);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                map.put(y * 100 + x, cell);
                mainController.getCellPain().getChildren().add(cell.getImageView());
            }
        }
    }

    public void skipMove() throws IOException {
        if (moveStatus == CellKind.crossMark) {
            if (skipMoveKind == CellKind.zeroMark) finishGameFlag = true;
            skipMoveKind = CellKind.crossMark;
            moveStatus = CellKind.zeroMark;
        } else {
            if (skipMoveKind == CellKind.crossMark) finishGameFlag = true;
            skipMoveKind = CellKind.zeroMark;
            moveStatus = CellKind.crossMark;
        }
        if (finishGameFlag) {
            finishGame();
        }
        if (Client.getInstance() != null && !Client.getInstance().closeFlag) mainController.getSkipMoveButton().setDisable(clientKind != moveStatus);
        mainController.switchPlayerMove();
    }

    public void finishGame() {
        mainController.finishGame();
    }

    public String getClickCountStr() {
        return Integer.toString(clickCount);
    }

    public void checkFinish() {
        if (clickCount > 6) {
            if (zeroCellCount == 0) {
                wonCell = CellKind.crossMark;
                finishGameFlag = true;
            } else if (crossCellCount == 0) {
                wonCell = CellKind.zeroMark;
                finishGameFlag = true;
            }
        }
        if (cellCount == 0) {
            wonCell = CellKind.cell;
            finishGameFlag = true;
        }
    }

    public void addClickCount(int x, int y) throws IOException, ClassNotFoundException {
        mainController.getSkipMoveButton().setDisable(true);
        clickCount++;
        String logStr = Character.toString((char) ('a' + x)) + (10 - y);
        if (clickCount % 3 == 0) logStr = logStr + " ";
        else logStr = logStr + "-";
        skipMoveKind = CellKind.cell;
        mainController.addLogText(logStr);
        checkFinish();
        if (finishGameFlag) {
            finishGame();
        } else {
            mainController.setClickCountTextField(Integer.toString(3 - clickCount % 3));
            if (clickCount % 3 == 0) {
                if (moveStatus == CellKind.crossMark) moveStatus = CellKind.zeroMark;
                else moveStatus = CellKind.crossMark;
                if (Client.getInstance() != null && !Client.getInstance().closeFlag) mainController.getSkipMoveButton().setDisable(clientKind != moveStatus);
                mainController.switchPlayerMove();


            }
        }
    }

    private boolean checkAround(int x, int y, CellKind cellKind) {
        boolean status = false;
        if ((x + 1 <= 9) && (y - 1 >= 0) && getCell(x + 1, y - 1).cellKind == cellKind) {
            status = true;
        } else if ((x + 1 <= 9) && getCell(x + 1, y).cellKind == cellKind) {
            status = true;
        } else if ((x + 1 <= 9) && (y + 1 <= 9) && getCell(x + 1, y + 1).cellKind == cellKind) {
            status = true;
        } else if ((y - 1 >= 0) && getCell(x, y - 1).cellKind == cellKind) {
            status = true;
        } else if ((y + 1 <= 9) && getCell(x, y + 1).cellKind == cellKind) {
            status = true;
        } else if ((x - 1 >= 0) && (y - 1 >= 0) && getCell(x - 1, y - 1).cellKind == cellKind) {
            status = true;
        } else if ((x - 1 >= 0) && getCell(x - 1, y).cellKind == cellKind) {
            status = true;
        } else if ((x - 1 >= 0) && (y + 1 <= 9) && getCell(x - 1, y + 1).cellKind == cellKind) {
            status = true;
        }
        return status;
    }

    private boolean checkTmp(int _x, int y, CellKind cellKind1, CellKind cellKind2) {
        int _y;
        if (y - 1 >= 0) { // up
//            System.out.print("u");
            _y = y - 1;
            if (check(_x, cellKind1, cellKind2, _y)) return true;
        }
        if (y + 1 <= 9) { // down
//            System.out.print("d");
            _y = y + 1;
            if (check(_x, cellKind1, cellKind2, _y)) return true;
        }
//        System.out.print("m");
        _y = y; // middle
        return check(_x, cellKind1, cellKind2, _y);
    }

    private boolean check(int _x, CellKind cellKind1, CellKind cellKind2, int _y) {
        Cell tmpCell;
        CellKind tmp;
        tmpCell = getCell(_x, _y);
        if (!tmpCell.checked) {
            tmpCell.checked = true;
            tmp = tmpCell.cellKind;
            return tmp == cellKind1 || tmp == cellKind2 && recurseCheckStep(_x, _y, cellKind1, cellKind2);
        }
        return false;
    }

    private boolean recurseCheckStep(int x, int y, CellKind cellKind1, CellKind cellKind2) {
        int _x;
        if (x + 1 <= 9) { // right side
//            System.out.print("r");
            _x = x + 1;
            if (checkTmp(_x, y, cellKind1, cellKind2)) return true;
//            System.out.println();
        }
        if (x - 1 >= 0) { // left side
//            System.out.print("l");
            _x = x - 1;
            if (checkTmp(_x, y, cellKind1, cellKind2)) return true;
//            System.out.println();
        }
//        System.out.print("m");
        return checkTmp(x, y, cellKind1, cellKind2); // middle side
    }

    private boolean isAvailableByChainCell(int x, int y, CellKind cellKind) {
        CellKind tmp1, tmp2;
        if (cellKind == CellKind.crossMark) {
            tmp1 = CellKind.crossMark;
            tmp2 = CellKind.filledZeroMark;
        } else {
            tmp1 = CellKind.zeroMark;
            tmp2 = CellKind.filledCrossMark;
        }
        return recurseCheckStep(x, y, tmp1, tmp2);
    }

    public boolean isAvailableCell(int x, int y, CellKind cellKind) {
        boolean status = false;
        if (Client.getInstance() != null && !Client.getInstance().closeFlag && clientKind != moveStatus) {
            System.out.println("Not your move");
            return false;
        }
        if (!finishGameFlag) {
            if (cellKind == CellKind.cell && (clickCount == 0 || clickCount == 3)) {
                if ((x == 0 && y == 9 && moveStatus == CellKind.crossMark) || (x == 9 && y == 0 && moveStatus == CellKind.zeroMark))
                    return true;
            }

            setUncheckedCellMap();
            if (moveStatus == CellKind.crossMark && (checkAround(x, y, CellKind.crossMark) || isAvailableByChainCell(x, y, CellKind.crossMark))) {
                status = true;
            } else if (moveStatus == CellKind.zeroMark && (checkAround(x, y, CellKind.zeroMark) || isAvailableByChainCell(x, y, CellKind.zeroMark))) {
                status = true;
            }
        }
        return status;
    }

    public static void main(String[] args) throws IOException {
        launch();
        if (Client.getInstance() != null) Client.getInstance().quit();
    }

    public static VirusApplication getInstance() {
        VirusApplication localInstance = instance;
        if (localInstance == null) {
            synchronized (VirusApplication.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new VirusApplication();
                }
            }
        }
        return localInstance;
    }

    public void getClick(int x, int y) throws IOException, ClassNotFoundException {
        Cell cell = VirusApplication.getInstance().getCell(x, y);
        cell.clickAction(x, y);
    }

    private static volatile VirusApplication instance;
    public int clickCount;
    public int cellCount;
    public boolean finishGameFlag;
    public CellKind wonCell;
    private CellKind skipMoveKind;
    public int crossCellCount;
    public int zeroCellCount;
    public CellKind moveStatus;
    public MainController mainController;
    public HashMap<Integer, Cell> map;
    public CellKind clientKind;
    public boolean initFlag = false;
}

