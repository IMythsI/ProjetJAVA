package dataAccessPackage.interfaces;

import exceptionPackage.ProductException;
import modelPackage.Product;

import java.util.ArrayList;

public interface ProductDataAccess {

    ArrayList<Product> getAllProducts() throws ProductException;

    void addProduct(Product product) throws ProductException;

    void updateProduct(Product oldProduct, Product newProduct) throws ProductException;

    void deleteProduct(Product product) throws ProductException;
}