import java.util.Scanner;

public class TestHugeInteger {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first huge integer (up to 40 digits): ");
        String inputA = scanner.nextLine();

        System.out.print("Enter second huge integer (up to 40 digits): ");
        String inputB = scanner.nextLine();

        HugeInteger a = new HugeInteger();
        HugeInteger b = new HugeInteger();
        
        a.parse(inputA);
        b.parse(inputB);

        System.out.println("a + b = " + a.add(b));
        System.out.println("a - b = " + a.subtract(b));
        System.out.println("b - a = " + b.subtract(a));
        System.out.println("a == b? " + a.isEqualTo(b));
        System.out.println("a != b? " + a.isNotEqualTo(b));
        System.out.println("a >  b? " + a.isGreaterThan(b));
        System.out.println("a <  b? " + a.isLessThan(b));
        System.out.println("a >= b? " + a.isGreaterThanOrEqualTo(b));
        System.out.println("a <= b? " + a.isLessThanOrEqualTo(b));
        System.out.println("a is zero? " + a.isZero());
        System.out.println("b is zero? " + b.isZero());

        scanner.close();
    }
}
