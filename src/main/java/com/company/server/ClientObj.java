package com.company.server;

import com.company.share.CellKind;
import com.company.share.PackageObj;

import java.util.LinkedList;

public class ClientObj {
    ClientObj(String _name) {
        name = _name;
        packageObjLinkedList = new LinkedList<>();
    }

    public ClientStatus clientStatus = ClientStatus.finding;
    public String partner = null;
    public CellKind cellKind = CellKind.cell;
    public String name;
    public PackageObj packageObj;
    public LinkedList<PackageObj> packageObjLinkedList;
//    public boolean sendFlag = false;
    public boolean partnerReadyFlag = false;
}
