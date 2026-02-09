public class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void decrementQuantity() {
        if (quantity > 0) quantity--;
    }

    public String toFileString() {
        return name + "," + price + "," + quantity;
    }

    @Override
    public String toString() {
        return name + " - $" + price + " [" + quantity + " left]";
    }
}
