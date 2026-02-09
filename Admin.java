import java.io.*;
import java.util.*;

public class Admin {
    private static final String PIN_FILE = "pin.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private String encryptedPIN;

    public void loadPIN() {
        File file = new File(PIN_FILE);
        if (!file.exists()) {
            encryptedPIN = Utils.encrypt("1234");
            savePIN();
            System.out.println("No PIN found. Default PIN (1234) created.");
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                encryptedPIN = br.readLine();
                if (encryptedPIN == null || encryptedPIN.isEmpty()) {
                    encryptedPIN = Utils.encrypt("1234");
                    savePIN();
                    System.out.println("Empty PIN file. Resetting to default PIN (1234).");
                }
            } catch (IOException e) {
                System.out.println("Error loading PIN file.");
                e.printStackTrace();
            }
        }
    }

    public void savePIN() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PIN_FILE))) {
            bw.write(encryptedPIN);
        } catch (IOException e) {
            System.out.println("Error saving PIN.");
            e.printStackTrace();
        }
    }

    public void adminMode(InventoryManager inventoryManager, SalesManager salesManager) {
        System.out.print("Enter Admin PIN: ");
        String input = scanner.nextLine();
        String encryptedInput = Utils.encrypt(input);

        if (!encryptedInput.equals(encryptedPIN)) {
            System.out.println("Incorrect PIN.");
            return;
        }

        while (true) {
            System.out.println("\n--------------------------------");
            System.out.println("|          ADMIN PANEL         |");
            System.out.println("--------------------------------");
            System.out.println("|1. View Inventory             |");
            System.out.println("|2. Restock Items              |");
            System.out.println("|3. Change Item Prices         |");
            System.out.println("|4. View Sales Report          |");
            System.out.println("|5. Change Admin PIN           |");
            System.out.println("|6. Shutdown Admin Mode        |");
            System.out.println("--------------------------------");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\n--------------------------------");
                    System.out.println("          Inventory            ");
                    System.out.println("--------------------------------");
                    inventoryManager.displayItems();
                    System.out.println("--------------------------------");
                    break;
                case "2":
                    restockItems(inventoryManager);
                    inventoryManager.saveInventory();
                    break;
                case "3":
                    changePrices(inventoryManager);
                    inventoryManager.saveInventory();
                    break;
                case "4":
                    salesManager.displaySalesReport();
                    break;
                case "5":
                    changePIN();
                    break;
                case "6":
                    System.out.println("Shutting down vending machine...");
                    System.exit(0);
                case "7":
                    return;
                default:
                    System.out.println("\nInvalid option!");
            }
        }
    }

    private void restockItems(InventoryManager manager) {
        List<Product> products = manager.getProducts();
        for (Product product : products) {
            try {
                System.out.print("Add stock to " + product.getName() + " (current: " + product.getQuantity() + "): ");
                int addQty = Integer.parseInt(scanner.nextLine());
                if (addQty >= 0) {
                    product.setQuantity(product.getQuantity() + addQty);
                } else {
                    System.out.println("Quantity cannot be negative.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Skipping...");
            }
        }
    }

    private void changePrices(InventoryManager manager) {
        List<Product> products = manager.getProducts();
        for (Product product : products) {
            try {
                System.out.print("Set new price for " + product.getName() + " (current: $" + product.getPrice() + "): ");
                double newPrice = Double.parseDouble(scanner.nextLine());
                if (newPrice >= 0) {
                    product.setPrice(newPrice);
                } else {
                    System.out.println("Price cannot be negative.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Skipping...");
            }
        }
    }

    private void changePIN() {
        System.out.print("Enter new PIN: ");
        String newPIN = scanner.nextLine();
        encryptedPIN = Utils.encrypt(newPIN);
        savePIN();
        System.out.println("PIN updated successfully.");
    }
}
