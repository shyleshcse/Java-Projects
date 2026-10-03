package inventorymanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
    private final Map<Integer, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        if (product.getProductId() <= 0) throw new IllegalArgumentException("Product ID must be positive.");
        if (products.containsKey(product.getProductId()))
            throw new IllegalArgumentException("Product ID already exists.");
        products.put(product.getProductId(), product);
    }

    public Product searchProduct(int id) { return products.get(id); }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public void updatePrice(int id, double price) {
        Product product = getProductOrThrow(id);
        product.setPrice(price);
    }

    public void addStock(int id, int quantity) {
        getProductOrThrow(id).addStock(quantity);
    }

    public void sellProduct(int id, int quantity) {
        getProductOrThrow(id).sellProduct(quantity);
    }

    public List<Product> getLowStockProducts() {
        List<Product> result = new ArrayList<>();
        for (Product product : products.values())
            if (product.getStock() < 5) result.add(product);
        return result;
    }

    public void deleteProduct(int id) {
        if (products.remove(id) == null) throw new IllegalArgumentException("Product not found.");
    }

    public Bill createBill(int id, int quantity) {
        Product product = getProductOrThrow(id);
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        product.sellProduct(quantity);
        Bill bill = new Bill();
        bill.addItem(product, quantity);
        return bill;
    }

    public void loadProducts(List<Product> loaded) {
        products.clear();
        for (Product product : loaded) addProduct(product);
    }

    private Product getProductOrThrow(int id) {
        Product product = products.get(id);
        if (product == null) throw new IllegalArgumentException("Product not found.");
        return product;
    }
}
