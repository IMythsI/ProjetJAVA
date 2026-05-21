package dataAccessPackage.interfaces;

import exceptionPackage.*;
import modelPackage.LineOrder;

import java.util.ArrayList;

public interface LineOrderDataAccess {
    ArrayList<LineOrder> getLineOrdersByTable(Integer idTable) throws LineOrderException;
}