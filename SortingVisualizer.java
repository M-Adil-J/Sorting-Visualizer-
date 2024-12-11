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

    
            if (!isRaceMode) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, "Sorting took: " + (sortDuration / 1_000_000) + " ms", "Sorting Complete", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                });
            } else {
                repaint();
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
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    
    g.setColor(Color.WHITE);
    g.setFont(new Font("SansSerif", Font.BOLD, 16)); 
    FontMetrics titleMetrics = g.getFontMetrics();
    String title = "Algorithm: " + algorithm;
    int titleWidth = titleMetrics.stringWidth(title);
    g.drawString(title, (getWidth() - titleWidth) / 2, 20); 

    int barWidth = getWidth() / array.length; 
    int maxHeight = getHeight(); 
    int maxValue = Arrays.stream(array).max().orElse(1); 

    
    int fontSize = Math.max(10, barWidth / 3);
    g.setFont(new Font("SansSerif", Font.BOLD, fontSize));
    FontMetrics metrics = g.getFontMetrics();

    for (int i = 0; i < array.length; i++) {
        int barHeight = (int) (((double) array[i] / maxValue) * (maxHeight * 0.8)) + 10;

        
        if (i == currentIndex) {
            g.setColor(Color.DARK_GRAY);
        } else {
            g.setColor(Color.GRAY);
        }

        
        g.fillRect(i * barWidth, maxHeight - barHeight, barWidth, barHeight);

        
        g.setColor(Color.BLACK);
        g.drawRect(i * barWidth, maxHeight - barHeight, barWidth, barHeight);

        
        if (barWidth < 20) continue;

        
        g.setColor(Color.BLACK);

        
        String number = String.valueOf(array[i]);
        int textWidth = metrics.stringWidth(number);
        int textHeight = metrics.getHeight();

        int textX = i * barWidth + (barWidth - textWidth) / 2;
        int textY = maxHeight - barHeight + (barHeight + textHeight) / 2;

        
        g.drawString(number, textX, textY);
    }
}

public static void main(String[] args) {
    runProgram();
}

private static void runProgram() {
    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Sorting Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        String[] options = {"Input Your Own Array", "Generate Random Array"};
        int option = JOptionPane.showOptionDialog(frame, "Would you like to input your own array or generate a random one?",
                "Array Input Option", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        int[] array;
        if (option == 0) {
            String input = JOptionPane.showInputDialog(frame, "Enter your array (comma separated values):");
            array = Arrays.stream(input.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } else {
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
        
            
            new Thread(() -> {
                panel.startSorting(frame, false);
        
                
                while (panel.getSortDuration() == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
        
                
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, "Sorting took: " + (panel.getSortDuration() / 1_000_000) + " ms", "Sorting Complete", JOptionPane.INFORMATION_MESSAGE);
        
                    
                    askToRestart(frame);
                });
            }).start();
        } else if ("Race Mode".equals(selectedMode)) {
            frame.setLayout(new GridLayout(1, 5));
            String[] algorithms = {"Bubble Sort", "Insertion Sort", "Selection Sort", "Merge Sort", "Quick Sort"};
            List<SortingVisualizer> panels = new ArrayList<>();
            for (String algo : algorithms) {
                SortingVisualizer panel = new SortingVisualizer(algo, array.clone());
                panels.add(panel);
                frame.add(panel);
                panel.startSorting(frame, true);
            }
        
            new Thread(() -> {
                for (SortingVisualizer panel : panels) {
                    while (panel.getSortDuration() == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        
                panels.sort(Comparator.comparingLong(SortingVisualizer::getSortDuration));
                StringBuilder leaderboard = new StringBuilder("Leaderboard:\n");
                for (int i = 0; i < panels.size(); i++) {
                    leaderboard.append((i + 1)).append(". ").append(panels.get(i).algorithm)
                            .append(" - ").append(panels.get(i).getSortDuration() / 1_000_000).append(" ms\n");
                }
        
                
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, leaderboard.toString(), "Race Results", JOptionPane.INFORMATION_MESSAGE);
        
                    
                    askToRestart(frame);
                });
            }).start();
        }
        

        frame.setVisible(true);
    });
}

private static void askToRestart(JFrame frame) {
    int response = JOptionPane.showConfirmDialog(frame, "Would you like to run the program again?", "Restart Program", JOptionPane.YES_NO_OPTION);
    if (response == JOptionPane.YES_OPTION) {
        frame.dispose();
        runProgram();
    } else {
        frame.dispose();
    }
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
