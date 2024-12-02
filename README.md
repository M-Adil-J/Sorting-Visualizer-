Project Overview
 
This project is a Java-based GUI application that visualizes the sorting process for different algorithms. It supports two modes:  
1. Single Algorithm Mode: Visualizes one algorithm sorting an array.  
2. Race Mode: Visualizes multiple algorithms sorting the same array side-by-side, simulating a "race."  

Features  
- Algorithms Supported: Bubble Sort, Insertion Sort, Selection Sort, Merge Sort, Quick Sort.  
- Array Options:  
  - User-defined array.  
  - Randomly generated array with customizable size.  
- Leaderboard (Race Mode): Displays sorting times for each algorithm in order of completion.  
- Visualization: Bars represent array elements, with real-time updates during sorting.  


How to Run  

1. Setup Environment  
   - Install Java JDK 
   - Use an IDE like IntelliJ IDEA or Eclipse, or simply a text editor and terminal.  

2. Compile the Program  
   - Save the provided code as SortingVisualizer.java.   
 

3. Run the Program 
   - Execute the compiled file:   

4. Choose Input Options
   - Array Input: Enter comma-separated values or let the program generate a random array.  
   - Mode Selection:  
   - Select either Single Algorithm or Race Mode.  

5. Follow Prompts  
   - For Single Algorithm mode, choose an algorithm to visualize.  
   - For Race Mode, watch the sorting "race" and view the leaderboard.  

Notes 
- Ensure input arrays contain valid integers when entering manually.  
- For larger arrays or slower algorithms (like Bubble Sort), the process may take longer.  
- The visualization speed is controlled by a `Thread.sleep(200)` delay, which can be adjusted in the code for faster/slower animations.  

Enjoy visualizing sorting in action! 🎉
