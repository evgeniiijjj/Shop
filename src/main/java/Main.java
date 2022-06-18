import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Shop shop = new Shop();
        shop.addSupplier(shop1 -> {
                shop1.addProduct(new Product("хлеб", "Деревенский", 20, 10), 100);
                shop1.addProduct(new Product("молоко", "Веселый молочник", 45, 9), 70);
            }).addSupplier(shop1 -> {
                shop1.addProduct(new Product("сок яблочный", "Фруктовый сад", 70, 5), 30);
                shop1.addProduct(new Product("сок томатный", "Фруктовый сад", 60, 4), 35);
                shop1.addProduct(new Product("селедка", "Морепродукты", 90, 3), 40);
            }).addSupplier(shop1 -> {
                shop1.addProduct(new Product("шоколад молочный", "Аленка", 85, 7), 40);
                shop1.addProduct(new Product("печенье овсянное", "Веселый кондитер", 110, 8), 40);
                shop1.addProduct(new Product("мороженное", "Домик в деревне", 45, 9), 70);
            }).fillInAssortment();
        shop.addDeliverer(new ProductDeliverer() {
            @Override
            public boolean takeOrder(Map<Product, Integer> order) {
                return true;
            }

            @Override
            public Tracking getOrderTracking() {
                return new Tracking() {
                    @Override
                    public String getStatusMessage() {
                        return "Заказ доставляется, ожидайте";
                    }
                };
            }
        });
        new UserInterfaceImpl().setShop(shop).startCommunication();
    }
}
