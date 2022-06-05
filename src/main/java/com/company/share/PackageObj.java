package com.company.share;

import java.io.Serializable;

public class PackageObj implements Serializable {
    public int x, y;
    public boolean skipFlag;

    public PackageObj(int _x, int _y, boolean _skipFlag) {
        x = _x;
        y = _y;
        skipFlag = _skipFlag;
    }

    public PackageObj() {

    }
}
