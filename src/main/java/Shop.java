import java.util.*;

public class Shop { //Open-closed principle, класс можно рассширить в наследнике, добавляя новую функциональность.
                    // Single-responsibility principle, класс включает в себя только функционал работы с товарами и не содержит посторонней функциональности,
                    // за которую отвечают, например, поставщик или служба доставки.
    private final List<ProductSupplier> suppliersList = new ArrayList<>();
    private final List<ProductDeliverer> deliverersList = new ArrayList<>();
    private final Map<Integer, ProductDeliverer> trackingList = new HashMap<>();
    private final List<Product> productRange = new ArrayList<>();
    private final List<Map<Product, Integer>> orderHistory = new ArrayList<>();
    private final Map<Product, Integer> warehouse = new HashMap<>();
    private final Map<Integer, Integer> mapOrdinalNumToIndex = new HashMap<>();
    private final Map<Product, Integer> shoppingBasket = new HashMap<>();

    public Shop addSupplier(ProductSupplier supplier) {
        suppliersList.add(supplier);
        return this;
    }

    public Shop addDeliverer(ProductDeliverer deliverer) {
        deliverersList.add(deliverer);
        return this;
    }

    public void addProduct(Product product, int num) {
        if (!warehouse.containsKey(product)) {
            warehouse.put(product, num);
            productRange.add(product);
        } else {
            warehouse.put(product, warehouse.get(product) + num);
        }
    }

    public void fillInAssortment() {
        for (ProductSupplier supplier : suppliersList) {
            supplier.putInProductRange(this);
        }
    }

    public void addProductEstimation(int ordinalNum, int estimation) {
        productRange.get(mapOrdinalNumToIndex.get(ordinalNum)).addEstimation(estimation);
    }

    public void addProductsInShoppingBasket(int ordinalNum, int num) {
        int index = mapOrdinalNumToIndex.get(ordinalNum);
        Product product = productRange.get(index);
        if (!shoppingBasket.containsKey(product)) {
            shoppingBasket.put(product, num);
        } else {
            shoppingBasket.put(product, shoppingBasket.get(product) + num);
        }
    }

    public void returnProductsFromShoppingBasket() {
        shoppingBasket.clear();
    }

    public void paymentForShoppingBasket() {
        Map<Product, Integer> order = new HashMap<>();
        for (Product product : shoppingBasket.keySet()) {
            warehouse.put(product, warehouse.get(product) - shoppingBasket.get(product));
            order.put(product, shoppingBasket.get(product));
        }
        for (ProductDeliverer deliverer : deliverersList) {
            if (deliverer.takeOrder(order)) {
                orderHistory.add(order);
                trackingList.put(orderHistory.size() - 1, deliverer);
                break;
            }
        }
    }

    public Tracking getTracking(int orderIndex) {
        return trackingList.get(orderIndex).getOrderTracking();
    }

    public int getProductsNumInProductRange() {
        return productRange.size();
    }

    public int getProductsNumInPrintedList() {
        return mapOrdinalNumToIndex.size();
    }

    public int getProductRemainingNumInWarehouse(int ordinalNum) {
        int index = mapOrdinalNumToIndex.get(ordinalNum);
        Product product = productRange.get(index);
        return warehouse.get(product) - shoppingBasket.getOrDefault(product, 0);
    }

    public void printProductsInWarehouse() {
        productRange.sort(Comparator.comparingInt(Product::getRating).reversed());
        print(0, null, null, false);
    }

    public void printProductsInWarehouse(String filterSample) {
        productRange.sort(Comparator.comparingInt(Product::getRating).reversed());
        print(0, null, filterSample, false);
    }

    public void printProductsInShoppingBasket() {
        print(0, shoppingBasket, null, false);
    }

    public void printProductInWarehouse(Integer ordinalNum) {
        print(ordinalNum, null, null, false);
    }

    public void printProductInWarehouseLastList() {
        print(0, null, null, true);
    }

    public void printProductInShoppingBasket(Integer ordinalNum) {
        print(ordinalNum, shoppingBasket, null, false);
    }

    private void print(int ordinalNum, Map<Product, Integer> productsNumber, String filterSample, boolean lastList) {
        boolean printOrdinalNum = ordinalNum > 0;
        boolean filter = false;
        boolean calculateRemainingProducts = false;
        if (productsNumber == null) {
            productsNumber = warehouse;
            calculateRemainingProducts = true;
        }
        if (!lastList && !printOrdinalNum) mapOrdinalNumToIndex.clear();
        int price = 0;
        if (filterSample != null) {
            filterSample = filterSample.toLowerCase();
            filter = true;
            try {
                price = Integer.parseInt(filterSample);
            } catch (NumberFormatException ignored) {}
        }
        System.out.println("  пп  |   наименование   |       бренд      | цена | рейтинг | кол-во ");
        System.out.println("-----------------------------------------------------------------------");
        int element = mapOrdinalNumToIndex.getOrDefault(ordinalNum, 0);
        for (int index = element; index < (printOrdinalNum ? element + 1 : productRange.size()); index++) {
            Product product = productRange.get(index);
            if (warehouse.get(product) < 1) continue;
            if (filter) {
                if (price > 0) {
                    if (product.getPrice() != price) continue;
                } else {
                    if (!product.getName().toLowerCase().contains(filterSample) && !product.getBrand().toLowerCase().contains(filterSample)) continue;
                }
            }
            if (lastList) {
                if (!mapOrdinalNumToIndex.containsValue(index)) continue;
            }
            if (!productsNumber.containsKey(product)) continue;
            if (!lastList && !printOrdinalNum) {
                ordinalNum++;
                mapOrdinalNumToIndex.put(ordinalNum, index);
            }
            System.out.printf("%3d.  |%2s|%5d\n", ordinalNum, product, productsNumber.get(product) - (calculateRemainingProducts ? shoppingBasket.getOrDefault(product, 0) : 0));
        }
        if (mapOrdinalNumToIndex.size() == 0) System.out.println("ПУСТО");
        System.out.println("-----------------------------------------------------------------------");
    }
}
