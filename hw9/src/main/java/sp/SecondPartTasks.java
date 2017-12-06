package sp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().flatMap(path -> {
            try {
                return Files.lines(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }})
                .filter(line -> line.contains(sequence))
                .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        int numberOfShots = 2000000;
        Random random = new Random();
        return Stream.generate(() -> {
            Double x = random.nextDouble() - 0.5;
            Double y = random.nextDouble() - 0.5;
            Double r = 0.5;

            return x * x + y * y - r * r;
        })
                .limit(numberOfShots)
                .filter(p -> p <= 1e-5)
                .count() / (double) numberOfShots;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream().max(Comparator.comparing(
                    e -> e.getValue().stream()
                        .collect(Collectors.joining()).length()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.summingInt(Map.Entry::getValue)));
    }
}
