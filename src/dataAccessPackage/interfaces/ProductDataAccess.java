package dataAccessPackage.interfaces;

import exceptionPackage.ProductException;
import modelPackage.Product;

import java.util.ArrayList;

public interface ProductDataAccess {
    ArrayList<Product> getAllProducts() throws ProductException;
}