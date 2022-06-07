package com.company.share;

import java.io.Serializable;

public class PackageObj implements Serializable {
    public int x, y;
    public boolean skipFlag;
    public boolean winFlag;
    public CellKind winKind;

    public PackageObj(int _x, int _y, boolean _skipFlag, boolean _winFlag, CellKind _winKind) {
        x = _x;
        y = _y;
        skipFlag = _skipFlag;
        winFlag = _winFlag;
        winKind = _winKind;
    }

    public PackageObj() {

    }
}
