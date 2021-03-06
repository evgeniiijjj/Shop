import java.util.Scanner;

public class UserInterfaceImpl implements UserInterface {

    private final Scanner scanner = new Scanner(System.in);
    private Shop shop;

    @Override
    public UserInterface setShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    @Override
    public void startCommunication() {
        int ordinalNum = 0;
        int step = 0;
        int res = 0;
        messageSelector(0);
        shop.printProductsInWarehouse();
        step++;
        while (true) {
            try {
                messageSelector(step);
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("Q")) {
                    return;
                }
                switch (step) {
                    case 1 -> {
                        res = Integer.parseInt(input);
                        if (res < 1 || res > 2) throw new NumberFormatException();
                        if (res > 1) shop.printProductsInWarehouse();
                        step += res;
                    }
                    case 2 -> {
                        shop.printProductsInWarehouse(input);
                        if (shop.getProductsNumInPrintedList() == 0) {
                            step = 1;
                            break;
                        }
                        step++;
                    }
                    case 3 -> {
                        res = Integer.parseInt(input);
                        ordinalNum = res;
                        if (res < 1 || res > shop.getProductsNumInPrintedList()) throw new NumberFormatException();
                        shop.printProductInWarehouse(ordinalNum);
                        step++;
                    }
                    case 4 -> {
                        res = Integer.parseInt(input);
                        if (res < 1 || res > shop.getProductRemainingNumInWarehouse(ordinalNum))
                            throw new NumberFormatException();
                        shop.addProductsInShoppingBasket(ordinalNum, res);
                        step += 11;
                    }
                    case 6 -> {
                        if (input.equalsIgnoreCase("Y")) {
                            step = 1;
                            break;
                        }
                        step++;
                    }
                    case 7 -> {
                        if (input.equalsIgnoreCase("Y")) {
                            shop.paymentForShoppingBasket();
                            messageSelector(++step);
                            step += 3;
                            break;
                        }
                        step += 2;
                    }
                    case 9 -> {
                        if (input.equalsIgnoreCase("Y")) {
                            shop.returnProductsFromShoppingBasket();
                            messageSelector(++step);
                            return;
                        }
                    }
                    case 11 -> {
                        if (input.equalsIgnoreCase("Y")) {
                            System.out.println(shop.getTracking(0).getStatusMessage());
                            return;
                        }
                    }
                    case 15 -> {
                        res = Integer.parseInt(input);
                        if (res < 1 || res > 10) throw new NumberFormatException();
                        shop.addProductEstimation(ordinalNum, res);
                        step++;
                        messageSelector(step);
                        step = 5;
                        messageSelector(step);
                        shop.printProductsInShoppingBasket();
                        step++;
                    }
                }
            } catch (NumberFormatException e) {
                messageSelector(-1);
            }
        }
    }

    static void messageSelector(int selector) {
        switch (selector) {
            case 0 -> {
                System.out.println("Добро пожаловать в наш магазин!");
                System.out.println("Нажмите Q чтобы выйти из магазина.");
                System.out.println("Ознакомтесь со список продуктов:");
            }
            case 1 -> {
                System.out.println("Выберите пункт меню:");
                System.out.println("1. Фильтровать список товара по ключевым словам, ценам, производителям;");
                System.out.println("2. Выбор товара из списка по номеру товара.");
            }
            case 2 -> System.out.println("Введите образец для поиска");
            case 3 -> System.out.println("Выберите товар из списка");
            case 4 -> System.out.println("Введите количество товара");
            case 5 -> System.out.println("Ваш список товаров:");
            case 6 -> System.out.println("Продолжить выбор товаров? Для подтверждения ведите Y");
            case 7 -> System.out.println("Чтобы оплатить заказ и закончить введите Y");
            case 8 -> System.out.println("Заказ оплачен, ожидайте доставки");
            case 9 -> System.out.println("Хотите отменить заказ? Введите Y для подтверждения");
            case 10 -> System.out.println("Заказ отменен");
            case 11 -> System.out.println("Для получения трекинга заказа введите Y");
            case 15 -> System.out.println("Пожалуйста оцените качество товара от 1 до 10");
            case 16 -> System.out.println("Спасибо за оценку, приятных дальнейший покупок");
            default -> System.out.println("Введены некорректные данные, повторите ввод");
        }
    }
}
