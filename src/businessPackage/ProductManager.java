package businessPackage;

import dataAccessPackage.db.ProductDBAccess;
import dataAccessPackage.interfaces.ProductDataAccess;
import exceptionPackage.ProductException;
import modelPackage.Product;

import java.util.ArrayList;

public class ProductManager {

    private final ProductDataAccess productDAO;

    public ProductManager() {
        productDAO = new ProductDBAccess();
    }

    public ArrayList<Product> getAllProducts() throws ProductException {
        return productDAO.getAllProducts();
    }

    public void addProduct(Product product) throws ProductException {
        validateProduct(product);
        productDAO.addProduct(product);
    }

    public void updateProduct(Product oldProduct, Product newProduct) throws ProductException {
        if (oldProduct == null) {
            throw new ProductException("Le produit à modifier est invalide.");
        }

        validateProduct(newProduct);
        productDAO.updateProduct(oldProduct, newProduct);
    }

    public void deleteProduct(Product product) throws ProductException {
        if (product == null || product.getProductLabel() == null || product.getProductLabel().isBlank()) {
            throw new ProductException("Le produit à supprimer est invalide.");
        }

        productDAO.deleteProduct(product);
    }

    private void validateProduct(Product product) throws ProductException {
        if (product == null) {
            throw new ProductException("Le produit est invalide.");
        }

        if (product.getProductLabel() == null || product.getProductLabel().isBlank()) {
            throw new ProductException("Le nom du produit est obligatoire.");
        }

        if (product.getPrice() == null || product.getPrice().doubleValue() < 0) {
            throw new ProductException("Le prix du produit doit être positif.");
        }

        if (product.getProductType() == null || product.getProductType().getTypeLabel() == null
                || product.getProductType().getTypeLabel().isBlank()) {
            throw new ProductException("Le type du produit est obligatoire.");
        }
    }
}