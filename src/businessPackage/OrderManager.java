package businessPackage;

import dataAccessPackage.db.OrderDBAccess;
import dataAccessPackage.interfaces.OrderDataAccess;
import exceptionPackage.OrderException;
import modelPackage.Order;
import modelPackage.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class OrderManager {

    private final OrderDataAccess orderDAO;

    public OrderManager() {
        orderDAO = new OrderDBAccess();
    }

    public ArrayList<Order> getAllOrders() throws OrderException {
        return orderDAO.getAllOrders();
    }

    public ArrayList<Order> getOrdersByTable(Integer idTable) throws OrderException {
        validateId(idTable, "La table sélectionnée est invalide.");

        return orderDAO.getOrdersByTable(idTable);
    }

    public BigDecimal getTotalAmountByTable(Integer idTable) throws OrderException {
        validateId(idTable, "La table sélectionnée est invalide.");

        return orderDAO.getTotalAmountByTable(idTable);
    }

    public void validateOrder(Order order, Map<Product, Integer> cart) throws OrderException {
        validateOrderHeader(order);
        validateCart(cart);

        orderDAO.addOrderWithLines(order, cart);
    }

    private void validateOrderHeader(Order order) throws OrderException {
        if (order == null) {
            throw new OrderException("La commande est invalide.");
        }

        if (order.getGuestCount() == null || order.getGuestCount() <= 0) {
            throw new OrderException("Le nombre de personnes doit être supérieur à 0.");
        }

        if (order.getOrderDate() == null) {
            throw new OrderException("La date de commande est obligatoire.");
        }

        if (order.getTakeAway() == null) {
            throw new OrderException("Le type de commande est obligatoire.");
        }

        if (order.getIsTakeAway()) {
            validateTakeAwayOrder(order);
        } else {
            validateOnSiteOrder(order);
        }
    }

    private void validateTakeAwayOrder(Order order) throws OrderException {
        if (order.getNameCustomer() == null || order.getNameCustomer().isBlank()) {
            throw new OrderException("Le nom du client est obligatoire pour une commande à emporter.");
        }

        if (order.getPickUpTime() == null) {
            throw new OrderException("L'heure de retrait est obligatoire pour une commande à emporter.");
        }

        if (order.getTable() != null) {
            throw new OrderException("Une commande à emporter ne peut pas être liée à une table.");
        }
    }

    private void validateOnSiteOrder(Order order) throws OrderException {
        if (order.getTable() == null) {
            throw new OrderException("Une commande sur place doit être liée à une table.");
        }

        if (order.getNameCustomer() != null && !order.getNameCustomer().isBlank()) {
            throw new OrderException("Une commande sur place ne doit pas contenir de nom client.");
        }

        if (order.getTelCustomer() != null && !order.getTelCustomer().isBlank()) {
            throw new OrderException("Une commande sur place ne doit pas contenir de numéro de téléphone.");
        }

        if (order.getPickUpTime() != null) {
            throw new OrderException("Une commande sur place ne doit pas contenir d'heure de retrait.");
        }
    }

    private void validateCart(Map<Product, Integer> cart) throws OrderException {
        if (cart == null || cart.isEmpty()) {
            throw new OrderException("La commande doit contenir au moins un produit.");
        }

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            if (product == null) {
                throw new OrderException("Un produit de la commande est invalide.");
            }

            if (product.getProductLabel() == null || product.getProductLabel().isBlank()) {
                throw new OrderException("Un produit de la commande est invalide.");
            }

            if (quantity == null || quantity <= 0) {
                throw new OrderException("La quantité d'un produit doit être supérieure à 0.");
            }
        }
    }

    private void validateId(Integer id, String message) throws OrderException {
        if (id == null || id <= 0) {
            throw new OrderException(message);
        }
    }

    public void deleteOrder(Integer idOrder) throws OrderException {
        validateId(idOrder, "La commande sélectionnée est invalide.");

        orderDAO.deleteOrder(idOrder);
    }
}