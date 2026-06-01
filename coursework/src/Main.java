import exception.ParkException;
import factory.AttractionFactory;
import memento.ParkMemento;
import model.*;
import observer.StatisticsCollector;
import storage.AmusementParkStorage;

import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        AmusementParkStorage storage = AmusementParkStorage.getInstance();
        StatisticsCollector statsCollector = new StatisticsCollector();
        storage.getEventManager().subscribe(statsCollector);

        ParkMemento savedState = null;

        storage.addAttraction(AttractionFactory.createAttraction("Круті Віражі", AttractionType.EXTREME));
        storage.addAttraction(AttractionFactory.createAttraction("Дитяча Казка", AttractionType.CHILDREN));
        storage.addAttraction(AttractionFactory.createAttraction("Колесо Огляду 360", AttractionType.FAMILY));

        System.out.println("=== СИСТЕМА МОНІТОРИНГУ РОБОТИ РОЗВАЖАЛЬНОГО ПАРКУ ===");
        System.out.println("Розробник: Малаховський Олександр\n");

        while (true) {
            System.out.println("\nМеню управління:");
            System.out.println("1. Список атракціонів, їх статус та кількість відвідувачів");
            System.out.println("2. Продати Квиток / Абонемент");
            System.out.println("3. Симулювати вхід відвідувача за Квитком/Абонементом");
            System.out.println("4. Змінити статус технічного обслуговування (Графік ТО)");
            System.out.println("5. Переглянути статистику подій");
            System.out.println("6. Зберегти поточний стан (Memento)");
            System.out.println("7. Відновити стан із збереження");
            System.out.println("0. Вихід");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.println("\n--- Моніторинг об'єктів парку ---");
                        var list = storage.getAttractions();
                        for (int i = 0; i < list.size(); i++) {
                            Attraction a = list.get(i);
                            System.out.printf("%d. %s [%s] | Відвідувачів: %d | Доступний: %s\n",
                                    i + 1, a.getName(), a.getType(), a.getVisitorCount(), 
                                    a.isUnderMaintenance() ? "НІ (На тех. обслуговуванні)" : "ТАК");
                            System.out.println("   Обмеження: Вік " + a.getMinAge() + "+, Зріст від " + a.getMinHeight() + " см.");
                            System.out.println("   Опис: " + a.getDescription());
                        }
                    }
                    case "2" -> {
                        System.out.println("\nТип атракціону для квитка:");
                        System.out.println("1. EXTREME\n2. CHILDREN\n3. FAMILY");
                        System.out.print("Вибір: ");
                        int typeChoise = Integer.parseInt(scanner.nextLine());
                        AttractionType targetType = switch (typeChoise) {
                            case 1 -> AttractionType.EXTREME;
                            case 2 -> AttractionType.CHILDREN;
                            case 3 -> AttractionType.FAMILY;
                            default -> throw new ParkException("Невірний тип!");
                        };

                        System.out.print("Купити як: 1 - Одноразовий квиток, 2 - Абонемент (10 поїздок): ");
                        int passType = Integer.parseInt(scanner.nextLine());
                        boolean isSub = (passType == 2);

                        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
                        Pass newPass = new Pass(uniqueId, targetType, isSub);
                        storage.registerPassSale(newPass);

                        System.out.println("Успішно оформлено! ID перепустки: " + uniqueId);
                    }
                    case "3" -> {
                        System.out.print("Введіть ID вашого квитка/абонемента: ");
                        String passId = scanner.nextLine();
                        Pass foundPass = storage.getSoldPasses().stream()
                                .filter(p -> p.getId().equalsIgnoreCase(passId))
                                .findFirst()
                                .orElseThrow(() -> new ParkException("Квиток з таким ID не знайдено!"));

                        System.out.println("\nОберіть атракціон для входу:");
                        var list = storage.getAttractions();
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println((i + 1) + ". " + list.get(i).getName() + " [" + list.get(i).getType() + "]");
                        }
                        System.out.print("Номер: ");
                        int index = Integer.parseInt(scanner.nextLine()) - 1;
                        if (index < 0 || index >= list.size()) throw new ParkException("Невірний номер!");
                        
                        Attraction selected = list.get(index);

                        if (foundPass.getAllowedType() != selected.getType()) {
                            storage.getEventManager().notify("ACCESS_DENIED", "Квиток ID " + passId + " призначений для категорії " + foundPass.getAllowedType() + ", а не " + selected.getType());
                            throw new ParkException("Помилка: Цей квиток не підходить для даного типу атракціону!");
                        }

                        if (foundPass.getUsesLeft() <= 0) {
                            throw new ParkException("Помилка: На цьому квитку/абонементі закінчилися поїздки!");
                        }

                        System.out.print("Введіть вік відвідувача: ");
                        int age = Integer.parseInt(scanner.nextLine());
                        System.out.print("Введіть зріст відвідувача (в см): ");
                        double height = Double.parseDouble(scanner.nextLine());

                        if (selected.isUnderMaintenance()) {
                            storage.getEventManager().notify("ACCESS_DENIED", "Атракціон '" + selected.getName() + "' закритий на тех. обслуговування.");
                            throw new ParkException("Доступ закрито: об'єкт знаходиться на технічному обслуговуванні.");
                        }

                        if (selected.canAccess(age, height)) {
                            foundPass.use();
                            selected.incrementVisitors();
                            storage.getEventManager().notify("VISIT_REGISTERED", "Відвідувач пройшов на '" + selected.getName() + "'. Залишилося поїздок: " + foundPass.getUsesLeft());
                            System.out.println("Прохід дозволено! Приємного відпочинку. Залишилося поїздок: " + foundPass.getUsesLeft());
                        } else {
                            storage.getEventManager().notify("ACCESS_DENIED", "Відвідувач не підійшов під обмеження атракціону '" + selected.getName() + "'");
                            System.out.println("Доступ відхилено: невідповідність обмеженням щодо зросту або віку.");
                        }
                    }
                    case "4" -> {
                        System.out.print("Введіть номер атракціону для зміни статусу ТО: ");
                        int index = Integer.parseInt(scanner.nextLine()) - 1;
                        if (index < 0 || index >= storage.getAttractions().size()) {
                            throw new ParkException("Невірний номер!");
                        }
                        Attraction selected = storage.getAttractions().get(index);
                        boolean nextStatus = !selected.isUnderMaintenance();
                        selected.setUnderMaintenance(nextStatus);

                        String msg = nextStatus ? "Відправлено на тех. обслуговування." : "Успішно пройшов ТО та повернений в графік роботи.";
                        storage.getEventManager().notify("MAINTENANCE_LOG", "Атракціон '" + selected.getName() + "': " + msg);
                        System.out.println("Статус успішно оновлено.");
                    }
                    case "5" -> statsCollector.printStatistics();
                    case "6" -> {
                        savedState = storage.save();
                        System.out.println("Стан системи успішно збережено!");
                    }
                    case "7" -> {
                        if (savedState == null) throw new ParkException("Збереження відсутні!");
                        storage.restore(savedState);
                    }
                    case "0" -> {
                        System.out.println("Завершення сесії робочого дня.");
                        return;
                    }
                    default -> System.out.println("Невірний пункт меню.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Помилка введення: очікувалося числове значення.");
            } catch (ParkException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}