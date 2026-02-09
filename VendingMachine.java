import java.util.*;

public class VendingMachine {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventoryManager inventoryManager = new InventoryManager();
    private static final SalesManager salesManager = new SalesManager();
    private static final Admin admin = new Admin();
    private static final Membership membership = new Membership();

    public static void main(String[] args) {
        inventoryManager.loadInventory();
        admin.loadPIN();
        membership.loadMemberPIN();

        while (true) {
            System.out.println("\n================================");
            System.out.println("|     VENDING MACHINE MENU     |");
            System.out.println("================================");
            System.out.println("| 1. Buy Item                  |");
            System.out.println("| 2. Admin Mode                |");
            System.out.println("| 3. Exit                      |");
            System.out.println("================================");
            System.out.print("Choose Option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleBuy();
                    break;
                case "2":
                    admin.adminMode(inventoryManager, salesManager);
                    break;
                case "3":
                    inventoryManager.saveInventory();
                    salesManager.saveSales();
                    System.out.println("\nThank you for purchase. Please visit again.");
                    return;
                default:
                    System.out.println("\nInvalid option! Try again.");
            }
        }
    }

    private static void handleBuy() {
        System.out.println("\n--------------------------------");
        System.out.println("          BUY MODE          ");
        System.out.println("--------------------------------");

        List<Product> cart = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        while (true) {
            inventoryManager.displayItems();
            System.out.println("--------------------------------");
            System.out.print("Enter product number to add to cart (or 0 to checkout): ");
            int productNumber = Integer.parseInt(scanner.nextLine());

            if (productNumber == 0) break;

            Product product = inventoryManager.getProductByNumber(productNumber);
            if (product == null) {
                System.out.println("\nInvalid product number!");
                System.out.println("\n--------------------------------");
                continue;
            }

            System.out.print("Enter quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());

            if (product.getQuantity() < qty || qty <= 0) {
                System.out.println("\nInvalid quantity. Available: " + product.getQuantity());
                System.out.println("\n--------------------------------");
                continue;
            }

            cart.add(product);
            quantities.add(qty);
            System.out.println();
            System.out.println(product.getName() + " x" + qty + " added to cart.");
            System.out.println("\n--------------------------------");
        }

        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Returning to main menu.");
            return;
        }

        boolean validMember = false;
        System.out.print("\nAre you a member? (yes/no): ");
        String isMember = scanner.nextLine();
        
        if (isMember.equalsIgnoreCase("yes")) {
            int attempts = 0;
        
            while (attempts < 5) {
                System.out.print("Enter your member PIN (or 0 to skip): ");
                String pinInput = scanner.nextLine();
        
                if (pinInput.equals("0")) {
                    System.out.println("\nSkipping membership. No discount will be applied.");
                    break;
                }
        
                if (membership.verifyMember(pinInput)) {
                    System.out.println("\nMembership verified. Discount applied.");
                    validMember = true;
                    break;
                } else {
                    attempts++;
                    if (attempts < 5) {
                        System.out.println("\nInvalid PIN. Try again (" + (5 - attempts) + " attempts left).");
                    } else {
                        System.out.println("\nToo many failed attempts. Skipping membership...");
                    }
                }
            }
        }
        

        double total = 0.0;
        for (int i = 0; i < cart.size(); i++) {
            total += cart.get(i).getPrice() * quantities.get(i);
        }

        if (validMember) {
            total *= 0.9;
        }

        System.out.printf("\nTotal amount to pay: $%.2f\n", total);
        System.out.print("Insert money: $");
        double money = Double.parseDouble(scanner.nextLine());

        if (money < total) {
            System.out.println("\nInsufficient amount. Purchase cancelled.");
            return;
        }

        double change = money - total;
        System.out.printf("\nPurchase successful! Your change: $%.2f\n", change);
        salesManager.saveSales();
        inventoryManager.saveInventory();


        for (int i = 0; i < cart.size(); i++) {
            Product p = cart.get(i);
            int qty = quantities.get(i);
            for (int j = 0; j < qty; j++) {
                p.decrementQuantity();
                double finalPrice = validMember ? p.getPrice() * 0.9 : p.getPrice();
                salesManager.recordSale(p.getName(), finalPrice);
            }
        }

        System.out.print("Would you like a receipt? (yes/no): ");
String wantsReceipt = scanner.nextLine();

if (wantsReceipt.equalsIgnoreCase("yes")) {
    System.out.println("\n============== RECEIPT ==============");
    System.out.printf("%-15s %-10s %-10s\n", "Item", "Qty", "Price");

    double subtotal = 0.0;
    for (int i = 0; i < cart.size(); i++) {
        Product p = cart.get(i);
        int qty = quantities.get(i);
        double itemTotal = p.getPrice() * qty;
        subtotal += itemTotal;
        System.out.printf("%-15s %-10d $%-9.2f\n", p.getName(), qty, p.getPrice());
    }

    System.out.println("-------------------------------------");
    System.out.printf("%-30s $%.2f\n", "Subtotal:", subtotal);

    if (validMember) {
        double discount = subtotal * 0.10;
        System.out.printf("%-29s -$%.2f\n", "Membership Discount (10%):", discount);
    }

    System.out.printf("%-30s $%.2f\n", "Total Paid:", money);
    System.out.printf("%-30s $%.2f\n", "Change Returned:", change);
    System.out.println("====================================");
}

    }
}
