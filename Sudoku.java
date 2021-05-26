/*
Joshua Xiao
May 25, 2021

This program offers three services (modes).

1. Soduko Validator
    
    The Validator takes in a Sudoku grid and the program outputs if the solution is valid or not. A Sudoku is valid if all integers in each row, column, and
    sub-square is unique and in a specific range. If the solution is not valid, the program highlights in red and outputs the first matching numbers found in
    a row, column, or sub-square as it traverses through the Sudoku. If the grid is fully filled in and valid, the programs outputs it is a valid solution. 
    If the grid is partially filled, and has potential to be a valid Sudoku, the program will output this.

2. Soduko Solver

    The Solver takes in a Sudoku grid and outputs all valid solutions, or a single valid solution, depending on what the user wants. The more clues
    the user gives, the faster the runtime. For instance, for a 9x9, it will take more time to solve a Sudoku that has 17 clues, compared to one that
    has 30 clues. The Solver can output a single solution for any Sudoku smaller than a 16x16 in a few seconds. The empty cells that are solved will
     be highlighted in green.

3. Soduko Generator

    The Generator asks for the dimensions of a Sudoku (equal to the maximum value allowed in a cell), and randomly generates a Sudoku grid with a single
    valid solution. The user can then attempt to solve the Sudoku, or give up and see the solution. Similar to the Sudoku Solver, the empty cells that are solved will
    be highlighted in green.

It is not recommended to use the Soduku Solver and Generator for grids larger than 16x16, as a simple back-tracking method is used to solve the
Sudokus. A faster algorithm would be Donald Knuth's Dancing Links technique, but this is too advanced.
*/

import java.util.*;

class Sudoku {

    static final String RESET = "\u001B[0m";                // RESET WHITE
    static final String BORDER_COLOR = "\033[1;33m";        // BRIGHT YELLOW
    static final String CYAN_BOLD = "\033[1;36m";    // CYAN
    static final String GREEN_BOLD = "\033[1;32m";   // GREEN
    static final String RED_BOLD = "\033[1;31m";     // RED
    static final String PURPLE_BOLD = "\033[1;35m";  // PURPLE

    static Scanner scan;
    static int counter;
    static int[][] grid;
    static int[][] col;
    static int[][] row;
    static int[][] square;
    static ArrayList<Integer> empty;
    static ArrayList<Integer> wrong;

    /*
    The main method takes care of the main menu and handles which mode the user wants.
    */
    public static void main(String arg[]) {

        clearScreen();

        scan = new Scanner(System.in);
        
        int mode;

        while(true) {

            mode = menu();
            clearScreen();

            if (mode == 1) {
                inputDimensions();
                System.out.println();
                inputSudoku();
                clearScreen();
                boolean valid = validateGrid();
                printMatrix(grid);
                if (valid) {
                    if (empty.isEmpty()) {
                        System.out.println("Your solution is valid ! Horray !");
                    }
                    else {
                        System.out.println("Your solution is valid so far, but there are empty cells !");
                    }
                }
            }
            else if (mode == 2) {
                inputDimensions();
                System.out.println();
                inputSudoku();
                validateGrid();
                clearScreen();
                System.out.println("Your Sudoku:\n");
                printMatrix(grid);
                solveGrid();
            }
            else if (mode == 3) {
                inputDimensions();
                generateGrid();
            }
            else if (mode == 4) {
                break;
            }

            System.out.print("\nPress [ENTER] to continue ...");
            scan.nextLine();

            empty = new ArrayList<Integer>();
            wrong = new ArrayList<Integer>();
        }

        scan.close();
        System.out.println(RED_BOLD + "[Program Terminated]\n" + RESET);
    }

    /*
    Returns true if the argument is a perfect square. Else, false;
    */
    public static boolean isPerfectSquare(int n) {
        return Math.pow((int) Math.sqrt(n), 2) == n;
    }

    /*
    Returns the number of digits in a positive integer.
    */
    public static int numberOfDigits(int n) {
        return (int) Math.ceil(Math.log10(n + 1));
    }

    /*
    Clear the console.
    */
    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }   
 
    /*
    Outputs a horizontal array of dashes (-) of length equal to the argument.
    */
    public static void printHorizontalOuterBorder(int n) {
        System.out.print(BORDER_COLOR);
        for (int i = 0; i < n; ++i) {
            System.out.print("-");
        }
        System.out.println(RESET);
    }

    /*
    Outputs a vertical bar (|).
    */
    public static void printVerticalBorder() {
        System.out.print(BORDER_COLOR + "| " + RESET);
    }

    /*
    Outputs a horizontal array of spaces ( ) of length equal to the argument.
    */
    public static void printSpaces(int n) {
        for (int i = 0; i < n; ++i) {
            System.out.print(" ");
        }
    }

    /*
    Returns true if an integer 'n' exists in list. Else, false.
    */
    public static boolean contains(ArrayList<Integer> list, int n) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == n) return true;
        }
        return false;
    }

    /*
    Each cell in the Sudoku is given a unique number, starting from 0.
    Returns the row number from the cell's unique number.
    */
    public static int getRowNum(int n) {
        return (n / grid.length) % grid.length;
    }

    /*
    Each cell in the Sudoku is given a unique number, starting from 0.
    Returns the column number from the cell's unique number.
    */
    public static int getColNum(int n) {
        return n % grid.length;
    }

    /*
    Each cell in the Sudoku is given a unique number, starting from 0.
    Each sub-square is also given a unique number, starting from 0.
    Returns the sub-square number from the cell's unique number.
    */
    public static int getSubSquareNum(int n) {
        int base = (int) Math.sqrt(grid.length);
        return base * (getRowNum(n) / base) + getColNum(n) / base;
    }

    /*
    Asks user to input the dimensions of the Sudoku, equal to the maximum allowed value of a cell.
    */
    public static void inputDimensions() {
        int maxN;
        String inp;

        while (true) {
            System.out.print("Enter the maximum value allowed in the Sudoku (a positive perfect square): ");
            inp = scan.nextLine();
            try {
                maxN = Integer.parseInt(inp.trim());
                if (maxN < 1 || !isPerfectSquare(maxN)) throw new Exception();
                break;
            } catch (NumberFormatException e) {
                System.out.println(RED_BOLD + "Invalid formatted integer." + RESET);
            } catch (Exception e) {
                System.out.println(RED_BOLD + "Number entered is not a positive perfect square." + RESET);
            }
        }

        grid = new int[maxN][maxN];
        col = new int[maxN][maxN];
        row = new int[maxN][maxN];
        square = new int[maxN][maxN];
    }

    /*
    Asks user to input the cells of the Sudoku as it apperas in the grid (reading left to right, top to bottom).
    */
    public static void inputSudoku() {
        System.out.println("Enter cells of Sodoku, as it appears in the grid, row by row (top to bottom), cell by cell (left to right)");
        System.out.println("Leave 0 for empty cell. Press [ENTER] after each cell.");

        String inp;

        for (int r = 0; r < grid.length; ++r) {
            for (int c = 0; c < grid[r].length; ++c) {
                while (true) {
                    inp = scan.nextLine();
                    try {
                        grid[r][c] = Integer.parseInt(inp.trim());
                        if (grid[r][c] < 0 || grid[r][c] > grid.length) throw new Exception();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println(RED_BOLD + "Invalid input for integer variable." + RESET);
                        System.out.println("Re-enter value of cell in row " + (r + 1) + ", column " + (c + 1));
                    } catch (Exception e) {
                        System.out.println(RED_BOLD + grid[r][c] + " is not in the range [1, " + grid.length + "]" + RESET);
                        System.out.println("Renter value of cell in row " + (r + 1) + ", column " + (c + 1));
                    }
                }
            }
        }
    }

    /*
    Outputs the Sudoku grid/matrix, formatted with borders seperating each sub-square.
    */
    public static void printMatrix(int[][] matrix) {
        int length = matrix.length * (1 + numberOfDigits(matrix.length)) + 2 * (int)Math.sqrt(matrix.length) + 1;

        for (int r = 0; r < matrix.length; ++r) {

            if (r % Math.sqrt(matrix.length) == 0) printHorizontalOuterBorder(length);

            for (int c = 0; c < matrix[r].length; ++c) {
                int pos = matrix.length * r + c;

                if (c % Math.sqrt(matrix.length) == 0) {
                    printVerticalBorder();
                }
                if (matrix[r][c] == 0) {
                    printSpaces(numberOfDigits(matrix.length));
                }
                else {
                    printSpaces( numberOfDigits(matrix.length) - numberOfDigits(matrix[r][c]) );
                    if (contains(wrong, pos)) {
                        System.out.print(RED_BOLD);
                    }
                    else if (contains(empty, pos) && wrong.isEmpty() || empty.isEmpty() && wrong.isEmpty()) {
                        System.out.print(GREEN_BOLD);
                    }
                    System.out.print(matrix[r][c]);
                    System.out.print(RESET);
                }
                System.out.print(" ");
            }
            printVerticalBorder();
            System.out.println();
        }

        printHorizontalOuterBorder(length);
    }

    /*
    Returns true if the Sudoku grid is valid (each number in row, column, sub-square is unique). Else, false;
    */
    public static boolean validateGrid() {

        for (int[] arr : col) Arrays.fill(arr, -1);
        for (int[] arr : row) Arrays.fill(arr, -1);
        for (int[] arr : square) Arrays.fill(arr, -1);

        empty = new ArrayList<Integer>();
        wrong = new ArrayList<Integer>();

        for (int r = 0; r < grid.length; ++r) {
            for (int c = 0; c < grid[r].length; ++c) {

                int pos = grid.length * r + c;

                if (grid[r][c] == 0) {
                    empty.add(pos);
                    continue;
                }
               
                int num = grid[r][c] - 1;
                int squareNum = getSubSquareNum(pos);

                if (row[r][num] >= 0) {
                    int c1 = row[r][num] % grid.length + 1;
                    System.out.println("Matching " + grid[r][c] + " found in row " + (r + 1) + ", columns " + c1 + " and " + (c + 1));
                    wrong.add(row[r][num]);
                    wrong.add(pos);
                    return false;
                }
                if (col[c][num] >= 0) {
                    int r1 = (col[c][num] / grid.length) % grid.length + 1;
                    System.out.println("Matching " + grid[r][c] + " found in column " + (c + 1) + ", rows " + r1 + " and " + (r + 1));
                    wrong.add(col[c][num]);
                    wrong.add(pos);
                    return false;
                }
                if (square[squareNum][num] >= 0) {
                    int c1 = square[squareNum][num] % grid.length + 1;
                    int r1 = (square[squareNum][num] / grid.length) % grid.length + 1;
                    System.out.println("Matching " + grid[r][c] + " found in sub-square: row " + r1 + " column " + c1 + ", row " + (r + 1) + " column " + (c + 1));
                    wrong.add(square[squareNum][num]);
                    wrong.add(pos);
                    return false;
                }

                row[r][num] = col[c][num] = square[squareNum][num] = pos;
            }
        }
        return true;
    }

    /*
    Solves the Sudoku grid, using a simple back-tracking method, that runs through all possibilities for each empty cell.
    If all the empty cells can be filled, a valid solution is found. Not efficient for large Sudokus (larger than 9x9).
    */
    public static void solveGrid() {

        if (!validateGrid()) {
            System.out.println();
            System.out.println("Not a valid Sudoku. Zero solutions!");
            return;
        }

        String inp;
        int choice;

        while (true) {
            System.out.println();
            System.out.print("Do you want all solutions or a single valid solution (1 - all, 2 - single): ");
            inp = scan.nextLine();
            try {
                choice = Integer.parseInt(inp.trim());
                if (choice != 1 && choice != 2) throw new Exception();
                break;
            } catch (NumberFormatException e) {
                System.out.println(RED_BOLD + "Invalid input for integer variable." + RESET);
            } catch (Exception e) {
                System.out.println(RED_BOLD + "Number entered is not 1 or 2." + RESET);
            }
        }

        --choice;

        System.out.println();
        System.out.println("Finding solutions ...\n");

        counter = 0;
        int possibilities = backTrack(0, choice);

        if (possibilities > 0 && choice == 1) {
            printMatrix(grid);
        }
        if (possibilities == 0) {
            System.out.println("No valid solutions exist !");
        }
        if (choice == 0) {
            System.out.println("There " + (possibilities == 1 ? "is " : "are ") + possibilities + " solution(s) for the given Sudoku!");
        }
    }

    /*
    Returns the number of solutions for a given Sudoku, depending on how many solutions the user wants.
    This method is also used in generateGrid() to randomly generate a unique Sudoku grid.
    */
    public static int backTrack(int n, int limit) {
        if (n == empty.size()) {
            if (limit == 0) printMatrix(grid);
            ++counter;
            return 1;
        }

        int ret = 0;
        int c = getColNum(empty.get(n));
        int r = getRowNum(empty.get(n));
        int squareNum = getSubSquareNum(empty.get(n));

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < grid.length; ++i) {
            list.add(i);
        }

        while (!list.isEmpty()) {
            int idx = (int) (Math.random() * list.size());
            int num = list.get(idx);
            list.remove(idx);

            if (row[r][num] >= 0 || col[c][num] >= 0 || square[squareNum][num] >= 0) continue;

            grid[r][c] = num + 1;
            row[r][num] = col[c][num] = square[squareNum][num] = grid.length * r + c;

            ret += backTrack(n + 1, limit);

            if (limit > 0 && counter >= limit) {
                if (limit == 2) {
                    grid[r][c] = 0;
                    row[r][num] = col[c][num] = square[squareNum][num] = -1;
                }
                return limit;
            }

            grid[r][c] = 0;
            row[r][num] = col[c][num] = square[squareNum][num] = -1;
        }

        return ret;
    }

    /*
    Generates a random Sudoku grid with a unique solution, and with the desired dimensions of the user.
    The user can then attempt to solve the Sudoku, and check their answer. If the user's answer is incorrect,
    they can keep trying, or give up and see the solution.
    */
    public static void generateGrid() {
        validateGrid();
        counter = 0;
        backTrack(0, 1);

        int[][] solution = new int[grid.length][grid.length];

        for (int r = 0; r < grid.length; ++r) {
            for (int c = 0; c < grid[r].length; ++c) {
                solution[r][c] = grid[r][c];
            }
            System.out.println();
        }

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < Math.pow(grid.length, 2); ++i) {
            list.add(i);
        }

        empty = new ArrayList<Integer>();

        while (!list.isEmpty()) {
            int idx = (int) (Math.random() * list.size());
            int pos = list.get(idx);
            list.remove(idx);

            int c = getColNum(pos);
            int r = getRowNum(pos);
            int squareNum = getSubSquareNum(pos);
            empty.add(pos);

            System.out.println("Generating ... Trying to remove: " + "row " + r + ", " + "column " + c);
            
            int num = grid[r][c] - 1;
            row[r][num] = col[c][num] = square[squareNum][num] = -1;
            grid[r][c] = 0;
            counter = 0;

            if (backTrack(0, 2) > 1) {
                empty.remove(empty.size() - 1);
                grid[r][c] = num + 1;
                row[r][num] = col[c][num] = square[squareNum][num] = pos;
                break;
            }
        }

        clearScreen();

        Collections.sort(empty);

        int[][] unfilled = new int[grid.length][grid.length];

        for (int r = 0; r < grid.length; ++r) {
            for (int c = 0; c < grid.length; ++c) {
                unfilled[r][c] = grid[r][c];
            }
        }

        clearScreen();

        boolean found = false;

        while (true) {
            System.out.println("Generated Soduku: ");
            printMatrix(unfilled);
            String inp;
            int choice;
            
            while (true) {
                System.out.println();
                System.out.print("Attempt to solve the Sudoku or see the solution (1 - attempt, 2 - give up): "); 
                inp = scan.nextLine();
                try {
                    choice = Integer.parseInt(inp.trim());
                    if (choice != 1 && choice != 2) throw new Exception();
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(RED_BOLD + "Invalid input for integer variable." + RESET);
                } catch (Exception e) {
                    System.out.println(RED_BOLD + "Number entered is not 1 or 2." + RESET);
                }
            }

            System.out.println();

            if (choice == 1) {
                System.out.println("Enter values of the " + empty.size() + " empty cells in the order as they appear");
                System.out.println("Top to bottom row, left to right column. Press [ENTER] after each cell: \n");

                if (empty.isEmpty()) System.exit(0);

                for (int pos : empty) {
                    int c = getColNum(pos);
                    int r = getRowNum(pos);

                    while (true) {
                        inp = scan.nextLine();
                        try {
                            grid[r][c] = Integer.parseInt(inp.trim());
                            if (grid[r][c] < 1 || grid[r][c] > grid.length) throw new Exception();
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println(RED_BOLD + "Invalid input for integer variable." + RESET);
                            System.out.println("Re-enter value for cell in row " + (r + 1) + " column " + (c + 1));
                        } catch (Exception e) {
                            System.out.println(RED_BOLD + "The number entered is not in the range [1, " + grid.length + "]." + RESET);
                            System.out.println("Re-enter value for cell in row " + (r + 1) + " column " + (c + 1));
                        }
                    }
                }

                clearScreen();
                System.out.println("Your solution: \n");

                if (validateGrid()) {
                    found = true;
                    break;
                }
                else {
                    printMatrix(grid);
                    System.out.println(RED_BOLD + "\nWrong answer :(\n" + RESET);
                    for (int r = 0; r < grid.length; ++r) {
                        for (int c = 0; c < grid[r].length; ++c) {
                            grid[r][c] = unfilled[r][c];
                        }
                    }
                    validateGrid();
                }
            }
            else {
                break;
            }
        }

        printMatrix(solution);

        if (found) System.out.println(GREEN_BOLD + "\nCorrect answer :) Horray !\n" + RESET);
    }

    /*
    Ouputs the menu, including the ASCII art and the list of modes the user can choose from.
    Returns the mode number the user inputs.
    */
    public static int menu() {
        clearScreen();
        System.out.println(BORDER_COLOR);
        System.out.println("          _____                    _____                    _____                   _______                   _____                    _____          w");
        System.out.println("         /\\    \\                  /\\    \\                  /\\    \\                 /::\\    \\                 /\\    \\                  /\\    \\         ");
        System.out.println("        /::\\    \\                /::\\____\\                /::\\    \\               /::::\\    \\               /::\\____\\                /::\\____\\        ");
        System.out.println("       /::::\\    \\              /:::/    /               /::::\\    \\             /::::::\\    \\             /:::/    /               /:::/    /        ");
        System.out.println("      /::::::\\    \\            /:::/    /               /::::::\\    \\           /::::::::\\    \\           /:::/    /               /:::/    /        ");
        System.out.println("     /:::/\\:::\\    \\          /:::/    /               /:::/\\:::\\    \\         /:::/~~\\:::\\    \\         /:::/    /               /:::/    /         ");
        System.out.println("    /:::/__\\:::\\    \\        /:::/    /               /:::/  \\:::\\    \\       /:::/    \\:::\\    \\       /:::/____/               /:::/    /           ");
        System.out.println("    \\:::\\   \\:::\\    \\      /:::/    /               /:::/    \\:::\\    \\     /:::/    / \\:::\\    \\     /::::\\    \\              /:::/    /            ");
        System.out.println("  ___\\:::\\   \\:::\\    \\    /:::/    /      _____    /:::/    / \\:::\\    \\   /:::/____/   \\:::\\____\\   /::::::\\____\\________    /:::/    /      _____  ");
        System.out.println(" /\\   \\:::\\   \\:::\\    \\  /:::/____/      /\\    \\  /:::/    /   \\:::\\ ___\\ |:::|    |     |:::|    | /:::/\\:::::::::::\\    \\  /:::/____/      /\\    \\ ");
        System.out.println("/::\\   \\:::\\   \\:::\\____\\|:::|    /      /::\\____\\/:::/____/     \\:::|    ||:::|____|     |:::|    |/:::/  |:::________\\____\\|:::|    /      /::\\____\\");
        System.out.println("\\:::\\   \\:::\\   \\::/    /|:::|____\\     /:::/    /\\:::\\    \\     /:::|____| \\:::\\    \\   /:::/    / \\::/   |::|   |          |:::|____\\     /:::/    /");
        System.out.println(" \\:::\\   \\:::\\   \\/____/  \\:::\\    \\   /:::/    /  \\:::\\    \\   /:::/    /   \\:::\\    \\ /:::/    /   \\/____|::|   |           \\:::\\    \\   /:::/    / ");
        System.out.println("  \\:::\\   \\:::\\    \\       \\:::\\    \\ /:::/    /    \\:::\\    \\ /:::/    /     \\:::\\    /:::/    /          |::|   |            \\:::\\    \\ /:::/    /  ");
        System.out.println("   \\:::\\   \\:::\\____\\       \\:::\\    /:::/    /      \\:::\\    /:::/    /       \\:::\\__/:::/    /           |::|   |             \\:::\\    /:::/    /   ");
        System.out.println("    \\:::\\  /:::/    /        \\:::\\__/:::/    /        \\:::\\  /:::/    /         \\::::::::/    /            |::|   |              \\:::\\__/:::/    /    ");
        System.out.println("     \\:::\\/:::/    /          \\::::::::/    /          \\:::\\/:::/    /           \\::::::/    /             |::|   |               \\::::::::/    /     ");
        System.out.println("      \\::::::/    /            \\::::::/    /            \\::::::/    /             \\::::/    /              |::|   |                \\::::::/    /      ");
        System.out.println("       \\::::/    /              \\::::/    /              \\::::/    /               \\::/____/               \\::|   |                 \\::::/    /       ");
        System.out.println("        \\::/    /                \\::/____/                \\::/____/                 ~~                      \\:|   |                  \\::/____/        ");
        System.out.println("         \\/____/                  ~~                       ~~                                                \\|___|                   ~~              ");
        System.out.println(RESET);

        for (int i = 0; i < 3; ++i) System.out.println();

        printSpaces(65);
        System.out.print(CYAN_BOLD);
        System.out.println("      __   __   ___"); 
        printSpaces(65);
        System.out.println("|\\/| /  \\ |  \\ |__ ");
        printSpaces(65);
        System.out.println("|  | \\__/ |__/ |___ ");
        System.out.print(RESET);

        for (int i = 0; i < 3; ++i) System.out.println();

        printSpaces(45);
        System.out.println(GREEN_BOLD + '1' + RESET + " - Sudoku Validator              Check if your solution is valid");
        printSpaces(45);
        System.out.println(GREEN_BOLD + '2' + RESET + " - Sudoku Solver                 Let the computer solve your Sudoku ");
        printSpaces(45);
        System.out.println(GREEN_BOLD + '3' + RESET + " - Sudoku Genrator               Generate a single unique Sudoku");
        printSpaces(45);
        System.out.println(GREEN_BOLD + '4' + RESET + " - QUIT                          QUIT PROGRAM");
        System.out.println();
        printSpaces(45);
        System.out.println(PURPLE_BOLD + "Note" + RESET + ": any Sudoku larger than 16x16 is highly unrecommended due to larger runtime");
        System.out.println();

        int mode;
        String inp; 

        while(true) {
            printSpaces(45);
            System.out.print("Enter mode number: ");
            inp = scan.nextLine();
            try {
                mode = Integer.parseInt(inp.trim());
                if (mode < 1 || mode > 4) throw new Exception();
                break;
            } catch (NumberFormatException e) {
                printSpaces(45);
                System.out.println(RED_BOLD + "The input is not an integer." + RESET);
            } catch (Exception e) {
                printSpaces(45);
                System.out.println(RED_BOLD + "The integer entered is not between 1 and 4." + RESET);
            } finally {
                System.out.println();
            }
        }
        
        return mode;
    }
}
