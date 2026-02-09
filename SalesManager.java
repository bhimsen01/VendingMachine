import java.io.*;
import java.util.*;

public class SalesManager {
    private final List<String> sales = new ArrayList<>();
    private static final String SALES_FILE = "sales.txt";

    public void recordSale(String itemName, double price) {
        String entry = "Sold: " + itemName + " for $" + price + " at " + new Date();
        sales.add(entry);
    }

    public void saveSales() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SALES_FILE, true))) {
            for (String sale : sales) {
                bw.write(sale);
                bw.newLine();
            }
            sales.clear(); // Clear after saving
        } catch (IOException e) {
            System.out.println("Error saving sales data.");
            e.printStackTrace();
        }
    }

    public void displaySalesReport() {
        System.out.println("\n--------------------------------------------------------------------");
        System.out.println("                             SALES REPORT                             ");
        System.out.println("--------------------------------------------------------------------");

        double totalRevenue = 0;
        int totalItemsSold = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);

                // Extract price from the line (e.g., "Sold: Chips for $2.5 at Mon Apr 15 10:00:00")
                try {
                    int dollarIndex = line.indexOf(" for $");
                    if (dollarIndex != -1) {
                        String pricePart = line.substring(dollarIndex + 6);
                        String[] splitPrice = pricePart.split(" ");
                        double price = Double.parseDouble(splitPrice[0]);
                        totalRevenue += price;
                        totalItemsSold++;
                    }
                } catch (Exception e) {
                    System.out.println("Failed to parse sale line: " + line);
                }
            }

            // Print summary
            System.out.println("----------------------------- SUMMARY ------------------------------");
            System.out.printf("Total Items Sold: %d\n", totalItemsSold);
            System.out.printf("Total Revenue: $%.2f\n", totalRevenue);
            System.out.println("--------------------------------------------------------------------");

        } catch (IOException e) {
            System.out.println("No sales data available.");
        }
    }
}
