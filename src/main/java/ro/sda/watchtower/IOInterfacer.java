package ro.sda.watchtower;

import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Clasă utilitară pentru interfațarea cu utilizatorul
 */
public class IOInterfacer {
    /**
     * Cere un int de la utilizator care respectă o anumită condiție
     * @param prompt Cerința de afișat utilizatorului
     * @param condition Condiția ce trebuie îndeplinită de input
     * @param errorString Mesajul de eroare afișat dacă utilizatorul a introdus input greșit
     * @return Valoarea corectă aleasă
     */
    public static int requestInt(String prompt, Predicate<Integer> condition, String errorString) {
        int decision = -1;
        IO.println(prompt);

        Scanner scanner = new Scanner(System.in);
        while (decision == -1) {
            IO.print("> ");
            String line = scanner.nextLine();
            try {
                decision = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                IO.println("Not a number");
                continue;
            }
            if (!condition.test(decision)) {
                IO.println(errorString);
                decision = -1;
            }
            IO.println();
        }
        return decision;
    }
    /**
     * Cere un string de la utilizator care respectă o anumită condiție
     * @param prompt Cerința de afișat utilizatorului
     * @param condition Condiția ce trebuie îndeplinită de input
     * @param errorString Mesajul de eroare afișat dacă utilizatorul a introdus input greșit
     * @return Valoarea corectă aleasă
     */
    public static String requestString(String prompt, Predicate<String> condition, String errorString){
        IO.println(prompt);
        boolean ack = false;
        Scanner s = new Scanner(System.in);
        String str = "";
        while (!ack){
            IO.print("> ");
            str = s.nextLine();
            ack = condition.test(str);
            if(!ack)
                IO.println(errorString);
            IO.println();
        }
        return str;
    }
}
