import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static final int NUM_THREADS = 1000;
    public static final int ROUTE_LENGTH = 100;
    public static final String COMMANDS = "RLRFR";

    public static void main(String[] args) {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute(COMMANDS, ROUTE_LENGTH);
                int rCount = countR(route);
                updateSizeToFreq(rCount);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printResults();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countR(String route) {
        int rCount = 0;
        for (char c : route.toCharArray()) {
            if (c == 'R') {
                rCount++;
            }
        }
        return rCount;
    }

    public static void updateSizeToFreq(int rCount) {
        synchronized (sizeToFreq) {
            sizeToFreq.put(rCount, sizeToFreq.getOrDefault(rCount, 0) + 1);
        }
    }

    public static void printResults() {
        int mostFrequent = 0;
        int mostFrequentCount = 0;

        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int count = entry.getKey();
            int frequency = entry.getValue();

            if (frequency > mostFrequentCount) {
                mostFrequent = count;
                mostFrequentCount = frequency;
            }
        }

        System.out.println("\nСамое частое количество повторений: " + mostFrequent + " (встретилось " + mostFrequentCount + " раз)");

        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int count = entry.getKey();
            int frequency = entry.getValue();

            if (count != mostFrequent) {
                System.out.println("- " + count + " (" + frequency + " раз)");
            }
        }
    }
}