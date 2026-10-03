package inventorymanagement;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private final List<BillItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0)
            throw new IllegalArgumentException("Invalid bill item.");
        items.add(new BillItem(product.getProductName(), quantity, product.getPrice(),
                product.getPrice() * quantity));
    }

    public double calculateTotal() {
        double total = 0;
        for (BillItem item : items) total += item.total;
        return total;
    }

    public void printBill() {
        System.out.println("\n======================================");
        System.out.println("                BILL");
        System.out.println("======================================");
        for (BillItem item : items) {
            System.out.printf("%-20s Qty: %-3d Price: Rs.%.2f Total: Rs.%.2f%n",
                    item.name, item.quantity, item.price, item.total);
        }
        System.out.println("--------------------------------------");
        System.out.printf("Bill Total: Rs.%.2f%n", calculateTotal());
        System.out.println("======================================");
    }

    private static class BillItem {
        String name; int quantity; double price; double total;
        BillItem(String name, int quantity, double price, double total) {
            this.name = name; this.quantity = quantity; this.price = price; this.total = total;
        }
    }
}
