import java.util.HashMap;
import java.util.Map;

public class Product { //Open-closed principle, класс можно рассширить в наследнике, добавляя новую функциональность.
    private final String name;
    private final String brand;
    private final int price;

    private final Map<Integer, Integer> estimations = new HashMap<>();
    private int rating;

    public Product(String name, String brand, int price, int rating) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.rating = rating;
        estimations.put(rating, 1);
    }

    public void addEstimation(int estimation) {
        int people = 0;
        if (estimations.containsKey(estimation)) {
            estimations.put(estimation, estimations.get(estimation) + 1);
        } else estimations.put(estimation, 1);
        rating = 0;
        for (int est : estimations.keySet()) {
            people += estimations.get(est);
            rating += (est * estimations.get(est));
        }
        rating = (int) Math.round(rating / (double) people);
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public int getPrice() {
        return price;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31
                + brand.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return name.equals(product.getName())
                && brand.equals(product.getBrand());
    }

    @Override
    public String toString() {
        return String.format("%-18s|%-18s|%3d р.|%5d    ", name, brand, price, rating);
    }
}
