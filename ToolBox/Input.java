package ToolBox;

import java.util.*;

public class Input {

    private static final Scanner scanner = new Scanner(System.in);

    public static int getNum() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.print("It's gotta be a number buddy: ");
                scanner.next();
            }
        }
    }
}