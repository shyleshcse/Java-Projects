package inventorymanagement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryFileService {
    private final String filePath;

    public InventoryFileService(String filePath) { this.filePath = filePath; }

    public void saveProducts(List<Product> products) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Product product : products) {
                writer.write(product.toFileString());
                writer.newLine();
            }
        }
    }

    public List<Product> loadProducts() throws IOException {
        List<Product> products = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return products;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    try { products.add(Product.fromFileString(line)); }
                    catch (RuntimeException e) { System.out.println("Skipping invalid record: " + line); }
                }
            }
        }
        return products;
    }
}
