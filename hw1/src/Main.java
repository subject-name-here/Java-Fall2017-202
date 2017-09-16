import ru.spbau.mit.java.paradov.HashTable;

/** Класс, созданный для того, чтобы протестировать HashTable. */
public class Main {
    public static void main(String[] args) {
        HashTable h = new HashTable();
        h.put("42", "Java");
        if (h.contains("42"))
            System.out.println("true");
        System.out.println(h.get("42"));

        System.out.println(h.put("42", "Drop Java"));

        if (!h.contains("43"))
            System.out.println("true");

        System.out.println(h.get("42"));

        System.out.println(h.put("44", "kek"));
        System.out.println(h.get("44"));
        System.out.println(h.size());

        System.out.println(h.remove("42"));
        if (!h.contains("42"))
            System.out.println("true");
        else
            System.out.println("false");

        System.out.println(h.size());
        h.clear();
        System.out.println(h.size());
    }
}
