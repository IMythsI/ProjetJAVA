package businessPackage;

import dataAccessPackage.db.ProductDBAccess;
import dataAccessPackage.interfaces.ProductDataAccess;
import exceptionPackage.ProductException;
import modelPackage.Product;

import java.util.ArrayList;

public class ProductManager {
    private ProductDataAccess productDAO;

    public ProductManager() {
        productDAO = new ProductDBAccess();
    }

    public ArrayList<Product> getAllProducts() throws ProductException {
        return productDAO.getAllProducts();
    }
}