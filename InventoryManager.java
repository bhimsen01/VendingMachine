import java.io.*;
import java.util.*;

public class InventoryManager {
    private final List<Product> products = new ArrayList<>();
    private static final String INVENTORY_FILE = "inventory.txt";

    public void loadInventory() {
        products.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                products.add(new Product(parts[0], Double.parseDouble(parts[1]), Integer.parseInt(parts[2])));
            }
        } catch (IOException e) {
            System.out.println("Inventory file not found. Starting with empty inventory.");
        }
    }

    public void saveInventory() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {
            for (Product p : products) {
                bw.write(p.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    public void displayItems() {
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }
    }

    public Product getProductByNumber(int num) {
        if (num <= 0 || num > products.size()) return null;
        return products.get(num - 1);
    }

    public List<Product> getProducts() {
        return products;
    }
}
