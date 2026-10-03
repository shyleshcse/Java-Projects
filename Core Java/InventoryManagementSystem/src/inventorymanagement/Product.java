package inventorymanagement;

public class Product {
    private int productId;
    private String productName;
    private Category category;
    private double price;
    private int stock;

    public Product(int productId, String productName, Category category, double price, int stock) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Category getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setProductName(String productName) {
        if (productName == null || productName.isBlank())
            throw new IllegalArgumentException("Product name cannot be empty.");
        this.productName = productName;
    }

    public void setCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("Category cannot be null.");
        this.category = category;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public void addStock(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        stock += quantity;
    }

    public void sellProduct(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        if (stock == 0) throw new IllegalStateException("Product is out of stock.");
        if (quantity > stock) throw new IllegalStateException("Not enough stock available.");
        stock -= quantity;
    }

    @Override
    public String toString() {
        return "ID: " + productId + " | Product: " + productName +
                " | Category: " + category + " | Price: Rs." + String.format("%.2f", price) +
                " | Stock: " + stock;
    }

    public String toFileString() {
        return productId + "|" + productName + "|" + category + "|" + price + "|" + stock;
    }

    public static Product fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 5) throw new IllegalArgumentException("Invalid product data.");
        return new Product(Integer.parseInt(parts[0]), parts[1], Category.valueOf(parts[2]),
                Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
    }
}
