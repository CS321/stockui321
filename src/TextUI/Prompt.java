/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TextUI;

import Structure.Table;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author treybriggs
 */
public class Prompt {

    static final String PS = "> ";
    static final InputStreamReader converter = new InputStreamReader(System.in);
    static final BufferedReader in = new BufferedReader(converter);

    public static String menuPrompt(Table<String> options) {

        for (List<String> o : options) {
            System.out.println(o.get(0) + ") " + o.get(1));
        }

        System.out.print(PS);

        String input = null;
        try {
            while (options.search((input = in.readLine()), 0) == -1) {
                System.out.println("Invalid option");
                for (List<String> o : options) {
                    System.out.println(o.get(0) + ") " + o.get(1));
                }
                System.out.print(PS);
            }
        } catch (IOException ex) {
        }

        return input;

    }

    public static String stringPrompt(String message, InputValidator v) {
        String input = null;

        try {
            System.out.println(message);
            System.out.print(PS);
            while (!v.validate((input = in.readLine()))) {
                System.out.println("Invalid option");
                System.out.println(message);
                System.out.print(PS);
            }
        } catch (IOException ex) {
        }

        return input;
    }

    public static int integerPrompt(String message, InputValidator v) {
        int r;

        String input = null;

        try {
            System.out.println(message);
            System.out.print(PS);
            while (!v.validate((input = in.readLine()))) {
                System.out.println("Invalid number");
                System.out.println(message);
                System.out.print(PS);
            }
        } catch (IOException ex) {
        }

        r = Integer.parseInt((String) input);


        return r;
    }

    public static double doublePrompt(String message, InputValidator v) {
        double r;
        String input = null;

        try {
            System.out.println(message);
            System.out.print(PS);
            while (!v.validate((input = in.readLine()))) {
                System.out.println("Invalid number");
                System.out.println(message);
                System.out.print(PS);
            }
        } catch (IOException ex) {
        }

        r = Double.parseDouble((String) input);


        return r;
    }
//    public static void initializeNoUsers() {
//        Table<String> options = new Table<String>();
//        
//        options.addRow(Arrays.asList("1", "Create a User"));
//        options.addRow(Arrays.asList("e", "exit"));
//        
//        System.out.print(PS);
//        InputStreamReader converter = new InputStreamReader(System.in);
//        BufferedReader in = new BufferedReader(converter);
//        try {
//            in.readLine();
//        } catch (IOException ex) {
//            Logger.getLogger(Prompt.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
