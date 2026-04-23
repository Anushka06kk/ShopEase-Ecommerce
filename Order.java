package ecommerce;

/**
 * Order - Plain Java Object representing a row in the orders table.
 */
public class Order {

    private int orderId;
    private int userId;
    private int totalAmount;

    public Order(int orderId, int userId, int totalAmount) {
        this.orderId     = orderId;
        this.userId      = userId;
        this.totalAmount = totalAmount;
    }

    public int getOrderId()     { return orderId;     }
    public int getUserId()      { return userId;      }
    public int getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "Order #" + orderId + " | Total: ₹" + totalAmount;
    }
}

