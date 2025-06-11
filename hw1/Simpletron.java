import java.util.Scanner;

public class Simpletron {
    public static final int MEMORY_SIZE = 100;
    public static final int SENTINEL = -99999;

    static int[] memory = new int[MEMORY_SIZE];
    static int accumulator = 0;
    static int instructionCounter = 0;
    static int instructionRegister = 0;
    static int operationCode = 0;
    static int operand = 0;

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("*** Welcome to Simpletron! ***");
        System.out.println("*** Please enter your program one instruction ***");
        System.out.println("*** (or data word) at a time. I will display ***");
        System.out.println("*** the location number and a question mark (?) ***");
        System.out.println("*** You then type the word for that location. ***");
        System.out.println("*** Type -99999 to stop entering your program. ***\n");

        // Input
        for (int i = 0; i < MEMORY_SIZE; i++) {
            System.out.printf("%02d ? ", i);
            int value = input.nextInt();
            if (value == SENTINEL) break;
            if (value < -9999 || value > 9999) {
                System.out.println("*** Invalid instruction. Please enter again. ***");
                i--;
            } else {
                memory[i] = value;
            }
        }

        System.out.println("\n*** Program loading completed ***");
        System.out.println("*** Program execution begins ***\n");

        // Execution
        while (true) {
            instructionRegister = memory[instructionCounter];
            operationCode = instructionRegister / 100;
            operand = instructionRegister % 100;

            switch (operationCode) {
                case 10:
                    System.out.print("Enter an integer: ");
                    memory[operand] = input.nextInt();
                    break;
                case 11:
                    System.out.println("Output: " + memory[operand]);
                    break;
                case 20:
                    accumulator = memory[operand];
                    break;
                case 21:
                    memory[operand] = accumulator;
                    break;
                case 30:
                    accumulator += memory[operand];
                    if (accumulator < -9999 || accumulator > 9999) {
                        System.out.println("*** Fatal Error: Accumulator overflow ***");
                        dump();
                        return;
                    }
                    break;
                case 31:
                    accumulator -= memory[operand];
                    if (accumulator < -9999 || accumulator > 9999) {
                        System.out.println("*** Fatal Error: Accumulator overflow ***");
                        dump();
                        return;
                    }
                    break;
                case 32:
                    if (memory[operand] == 0) {
                        System.out.println("*** Fatal Error: Division by zero ***");
                        dump();
                        return;
                    }
                    accumulator /= memory[operand];
                    break;
                case 33:
                    accumulator *= memory[operand];
                    if (accumulator < -9999 || accumulator > 9999) {
                        System.out.println("*** Fatal Error: Accumulator overflow ***");
                        dump();
                        return;
                    }
                    break;
                case 40:
                    instructionCounter = operand;
                    continue;
                case 41:
                    if (accumulator < 0) {
                        instructionCounter = operand;
                        continue;
                    }
                    break;
                case 42:
                    if (accumulator == 0) {
                        instructionCounter = operand;
                        continue;
                    }
                    break;
                case 43:
                    System.out.println("*** Simpletron execution terminated ***");
                    dump();
                    return;
                default:
                    System.out.println("*** Fatal Error: Invalid operation code ***");
                    dump();
                    return;
            }
            instructionCounter++;
        }
    }

    public static void dump() {
        System.out.println("\nREGISTERS:");
        System.out.printf("accumulator         %+05d\n", accumulator);
        System.out.printf("instructionCounter     %02d\n", instructionCounter);
        System.out.printf("instructionRegister %+05d\n", instructionRegister);
        System.out.printf("operationCode          %02d\n", operationCode);
        System.out.printf("operand                %02d\n", operand);

        System.out.println("\nMEMORY:");
        for (int i = 0; i < 10; i++) {
            System.out.printf("%6d", i);
        }
        for (int i = 0; i < MEMORY_SIZE; i++) {
            if (i % 10 == 0) System.out.printf("\n%2d", i);
            System.out.printf(" %+05d", memory[i]);
        }
        System.out.println();
    }
}
