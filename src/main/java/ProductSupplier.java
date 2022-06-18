import java.util.List;

public interface ProductSupplier { //реализован Dependency Inversion Principle, а так же Interface Segregation Principle
    void putInProductRange(Shop shop);
}
