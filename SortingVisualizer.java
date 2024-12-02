import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SortingVisualizer extends JPanel {
    private int[] array;
    private String algorithm;
    private int currentIndex = -1;
    private long sortDuration = 0;

    public SortingVisualizer(String algorithm, int[] array) {
        this.algorithm = algorithm;
        this.array = array;
        setBackground(Color.BLACK);
    }

    public void startSorting(JFrame frame, boolean isRaceMode) {
        new Thread(() -> {
            long startTime = System.nanoTime();
            sort();
            sortDuration = System.nanoTime() - startTime;

            // Show sorting time in Single Algorithm mode
            if (!isRaceMode) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, "Sorting took: " + (sortDuration / 1_000_000) + " ms", "Sorting Complete", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose(); // Close the frame after sorting
                });
            } else {
                repaint(); // For race mode, just repaint after sorting
            }
        }).start();
    }

    public long getSortDuration() {
        return sortDuration;
    }

    private void sort() {
        switch (algorithm) {
            case "Bubble Sort":
                bubbleSort();
                break;
            case "Insertion Sort":
                insertionSort();
                break;
            case "Selection Sort":
                selectionSort();
                break;
            case "Merge Sort":
                mergeSort(array, 0, array.length - 1);
                break;
            case "Quick Sort":
                quickSort(array, 0, array.length - 1);
                break;
        }
        currentIndex = -1;
        repaint();
    }

    private void bubbleSort() {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                currentIndex = j;
                if (array[j] > array[j + 1]) {
                    swap(j, j + 1);
                }
                repaint();
                sleep();
            }
        }
    }

    private void insertionSort() {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                currentIndex = j;
                array[j + 1] = array[j];
                j--;
                repaint();
                sleep();
            }
            currentIndex = j + 1;
            array[j + 1] = key;
            repaint();
            sleep();
        }
        currentIndex = -1;
    }

    private void selectionSort() {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                currentIndex = j;
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
                repaint();
                sleep();
            }
            swap(i, minIndex);
        }
    }

    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);
        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            currentIndex = k;
            if (leftArray[i] <= rightArray[j]) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
            repaint();
            sleep();
        }
        while (i < n1) {
            currentIndex = k;
            array[k++] = leftArray[i++];
            repaint();
            sleep();
        }
        while (j < n2) {
            currentIndex = k;
            array[k++] = rightArray[j++];
            repaint();
            sleep();
        }
    }

    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            currentIndex = j;
            if (array[j] < pivot) {
                i++;
                swap(i, j);
            }
            repaint();
            sleep();
        }
        swap(i + 1, high);
        return i + 1;
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    int barWidth = getWidth() / array.length;
    int maxHeight = getHeight();
    int maxValue = Arrays.stream(array).max().orElse(1); // Maximum value in the array
    
    for (int i = 0; i < array.length; i++) {
        // Calculate bar height proportional to the max value
        int barHeight = (int) (((double) array[i] / maxValue) * (maxHeight * 0.8)) + 10;
        
        // Set colors
        if (i == currentIndex) {
            g.setColor(Color.WHITE); // Current item being compared
        } else {
            g.setColor(Color.PINK); // Default bar color
        }
        
        // Draw the bar
        g.fillRect(i * barWidth, maxHeight - barHeight, barWidth, barHeight);
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sorting Visualizer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 600);

            // Ask user if they want to input their own array or generate it randomly
            String[] options = {"Input Your Own Array", "Generate Random Array"};
            int option = JOptionPane.showOptionDialog(frame, "Would you like to input your own array or generate a random one?",
                    "Array Input Option", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            int[] array;
            if (option == 0) {  // User wants to input their own array
                String input = JOptionPane.showInputDialog(frame, "Enter your array (comma separated values):");
                array = Arrays.stream(input.split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
            } else {  // Generate random array
                String sizeInput = JOptionPane.showInputDialog(frame, "Enter Array Size:", "50");
                int arraySize = Integer.parseInt(sizeInput);
                array = generateRandomArray(arraySize);
            }

            String[] modes = {"Single Algorithm", "Race Mode"};
            String selectedMode = (String) JOptionPane.showInputDialog(frame, "Choose Mode:", "Mode Selection", JOptionPane.QUESTION_MESSAGE, null, modes, modes[0]);

            if ("Single Algorithm".equals(selectedMode)) {
                String[] algorithms = {"Bubble Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort"};
                String algorithm = (String) JOptionPane.showInputDialog(frame, "Choose an Algorithm:", "Algorithm Selection", JOptionPane.QUESTION_MESSAGE, null, algorithms, algorithms[0]);
                SortingVisualizer panel = new SortingVisualizer(algorithm, array);
                frame.add(panel);
                panel.startSorting(frame, false);  // Pass false for single algorithm mode
            } else if ("Race Mode".equals(selectedMode)) {
                frame.setLayout(new GridLayout(1, 5));
                String[] algorithms = {"Bubble Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort"};
                List<SortingVisualizer> panels = new ArrayList<>();
                for (String algo : algorithms) {
                    SortingVisualizer panel = new SortingVisualizer(algo, array.clone());
                    panels.add(panel);
                    frame.add(panel);
                    panel.startSorting(frame, true);  // Pass true for race mode
                }

                new Thread(() -> {
                    // Wait for all algorithms to finish sorting
                    for (SortingVisualizer panel : panels) {
                        while (panel.getSortDuration() == 0) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }

                    // Sort the panels by duration and display leaderboard
                    panels.sort(Comparator.comparingLong(SortingVisualizer::getSortDuration));
                    StringBuilder leaderboard = new StringBuilder("Leaderboard:\n");
                    for (int i = 0; i < panels.size(); i++) {
                        leaderboard.append((i + 1)).append(". ").append(panels.get(i).algorithm)
                                .append(" - ").append(panels.get(i).getSortDuration() / 1_000_000).append(" ms\n");
                    }
                    JOptionPane.showMessageDialog(frame, leaderboard.toString(), "Race Results", JOptionPane.INFORMATION_MESSAGE);
                }).start();
            }

            frame.setVisible(true);
        });
    }

    private static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100) + 1;
        }
        return array;
    }
}
