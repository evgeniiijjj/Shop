import java.util.Map;

public interface ProductDeliverer { //реализован Dependency Inversion Principle, а так же Interface Segregation Principle
    boolean takeOrder(Map<Product, Integer> order);
    Tracking getOrderTracking();
}
