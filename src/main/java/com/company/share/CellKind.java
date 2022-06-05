package com.company.share;

import java.io.Serializable;

public enum CellKind implements Serializable {
    crossMark,
    filledCrossMark,
    zeroMark,
    filledZeroMark,
    cell;

    public String toString() {
        String name = null;
        switch (this) {
            case crossMark -> name = "crossMark";
            case filledCrossMark -> name = "filledCrossMark";
            case zeroMark -> name = "zeroMark";
            case filledZeroMark -> name = "filledZeroMark";
            case cell -> name = "cell";
        }
        return name;
    }
}
