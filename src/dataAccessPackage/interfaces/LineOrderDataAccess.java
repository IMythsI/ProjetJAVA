package dataAccessPackage.interfaces;

import exceptionPackage.LineOrderException;
import modelPackage.LineOrder;

import java.util.ArrayList;

public interface LineOrderDataAccess {

    ArrayList<LineOrder> getLineOrdersByTable(Integer idTable)
            throws LineOrderException;

    ArrayList<LineOrder> getLineOrdersByOrder(Integer idOrder)
            throws LineOrderException;
}