package inventorymanagement;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventoryService service = new InventoryService();
    private static final InventoryFileService fileService =
            new InventoryFileService("data/products.txt");

    public static void main(String[] args) {
        loadAtStartup();
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> addProduct();
                    case 2 -> displayAll();
                    case 3 -> searchProduct();
                    case 4 -> updatePrice();
                    case 5 -> addStock();
                    case 6 -> sellProduct();
                    case 7 -> lowStock();
                    case 8 -> generateBill();
                    case 9 -> deleteProduct();
                    case 10 -> saveProducts();
                    case 11 -> loadProducts();
                    case 0 -> {
                        saveProducts();
                        System.out.println("Thank you. Program closed.");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("======================================");
        System.out.println("       INVENTORY MANAGEMENT SYSTEM");
        System.out.println("======================================");
        System.out.println("1. Add Product");
        System.out.println("2. Show All Products");
        System.out.println("3. Search Product by ID");
        System.out.println("4. Update Product Price");
        System.out.println("5. Add Stock");
        System.out.println("6. Sell Product");
        System.out.println("7. Show Low Stock Products");
        System.out.println("8. Calculate Bill");
        System.out.println("9. Delete Product");
        System.out.println("10. Save Products to File");
        System.out.println("11. Load Products from File");
        System.out.println("0. Exit");
        System.out.println("======================================");
    }

    private static void addProduct() {
        int id = readInt("Enter product ID: ");
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        Category category = readCategory();
        double price = readDouble("Enter price: ");
        int stock = readInt("Enter stock quantity: ");

        if (price < 0 || stock < 0) throw new IllegalArgumentException("Price/stock cannot be negative.");

        service.addProduct(new Product(id, name, category, price, stock));
        System.out.println("Product added successfully.");
    }

    private static void displayAll() {
        List<Product> products = service.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (Product product : products) System.out.println(product);
    }

    private static void searchProduct() {
        Product product = service.searchProduct(readInt("Enter product ID: "));
        System.out.println(product == null ? "Product not found." : product);
    }

    private static void updatePrice() {
        service.updatePrice(readInt("Enter product ID: "), readDouble("Enter new price: "));
        System.out.println("Price updated successfully.");
    }

    private static void addStock() {
        service.addStock(readInt("Enter product ID: "), readInt("Enter quantity to add: "));
        System.out.println("Stock added successfully.");
    }

    private static void sellProduct() {
        service.sellProduct(readInt("Enter product ID: "), readInt("Enter quantity to sell: "));
        System.out.println("Product sold successfully.");
    }

    private static void lowStock() {
        List<Product> products = service.getLowStockProducts();
        if (products.isEmpty()) System.out.println("No products have stock below 5.");
        else for (Product product : products) System.out.println(product);
    }

    private static void generateBill() {
        Bill bill = service.createBill(readInt("Enter product ID: "), readInt("Enter quantity: "));
        bill.printBill();
    }

    private static void deleteProduct() {
        service.deleteProduct(readInt("Enter product ID: "));
        System.out.println("Product deleted successfully.");
    }

    private static void saveProducts() throws IOException {
        fileService.saveProducts(service.getAllProducts());
        System.out.println("Product details saved successfully.");
    }

    private static void loadProducts() throws IOException {
        List<Product> products = fileService.loadProducts();
        service.loadProducts(products);
        System.out.println(products.size() + " product(s) loaded successfully.");
    }

    private static void loadAtStartup() {
        try {
            List<Product> products = fileService.loadProducts();
            service.loadProducts(products);
            if (!products.isEmpty()) System.out.println(products.size() + " product(s) loaded from file.");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not load product data: " + e.getMessage());
        }
    }

    private static Category readCategory() {
        while (true) {
            System.out.println("1. LAPTOP");
            System.out.println("2. MOBILE");
            System.out.println("3. ACCESSORY");
            System.out.println("4. HOME_APPLIANCE");
            int choice = readInt("Enter category: ");
            switch (choice) {
                case 1: return Category.LAPTOP;
                case 2: return Category.MOBILE;
                case 3: return Category.ACCESSORY;
                case 4: return Category.HOME_APPLIANCE;
                default: System.out.println("Invalid category.");
            }
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid integer."); }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);
            try { return Double.parseDouble(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
        }
    }
}
