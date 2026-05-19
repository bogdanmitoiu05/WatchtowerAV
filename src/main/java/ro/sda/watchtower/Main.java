package ro.sda.watchtower;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    /**
     * Funcție ce printează meniul unui program la comanda help sau menu sau la prima rulare
     */
    private static void printHeader(){
        IO.println(String.format("Current directory: %s", Paths.get("")
                .toAbsolutePath()));
        IO.println("Selectati o optiune:");
        IO.println("1. Vizualizati baza de definitii");
        IO.println("2. Adaugati in baza de definitii");
        IO.println("3. Stergeti din baza de definitii");
        IO.println("4. Scanati un fisier");
        IO.println("5. Afisati structura RBTree");
        IO.println("6. Afisati informatii despre o amenintare");
        IO.println("7. Inchideti");
    }

    /**
     * Funcție de gestiune a meniului. Inițiază o buclă care parsează valoarea introdusă de utilizator. În caz de eroare, utilizatorul este
     * ghidat spre a alege o opțiune corectă
     * @return Opțiunea corectă aleasă
     */
    private static int showMenu(){
        int decision = -1;
        printHeader();

        Scanner scanner = new Scanner(System.in);
        while(decision == -1){
            IO.print("> ");
            String line = scanner.nextLine();
            if(line.equals("menu") || line.equals("help")){
                printHeader();
                continue;
            }
            try {
                decision = Integer.parseInt(line);
            }
            catch (NumberFormatException e){
                IO.println("Not a number or command");
                continue;
            }
            if(decision < 1 || decision > 7){
                IO.println("Invalid option");
                decision = -1;
            }
        }
        return decision;
    }



    static void main() {
        IO.println("WatchtowerAV - proiect SDA 2026 - Mitoiu Bogdan-Petru");
        WatchtowerAVEngine engine = new WatchtowerAVEngine();
        int decision = -1;
        while(decision != 7){
            decision = showMenu();
            switch (decision){
                case 1:
                    var matches = engine.getDefinitions();
                    for(var match: matches){
                        IO.println(String.format("%d. - %s",match.id(), Arrays.toString(match.bytes())));
                    }
                    break;
                case 2:

                    String virFile = IOInterfacer.requestString("Type the file name of the virus",(s)-> Files.exists(Path.of(s)), "File does not exist");
                    String descFile = IOInterfacer.requestString("Type the file name of the description",(s)-> Files.exists(Path.of(s)), "File does not exist");

                    try{
                        engine.add(virFile,descFile);
                    } catch (FileAlreadyExistsException e) {
                        IO.println("Error: file already exists");
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    int virId = IOInterfacer.requestInt("Enter the id of the threat you want to delete", (i)-> 0<=i && i<engine.getDefCount(),"Index out of range");
                    engine.remove(virId);
                    break;
                case 4:
                    String toScan = IOInterfacer.requestString("Type the file name to scan",(s)-> Files.exists(Path.of(s)), "File does not exist");
                    var results = engine.scan(toScan);
                    if(results.isEmpty()){
                        IO.println("No threats found");
                    }
                    else {
                        for (var result : results) {
                            IO.println(result);
                        }
                    }
                    break;
                case 5:
                    engine.printDescDb();
                    break;
                case 6:
                    int curVirId = IOInterfacer.requestInt("Enter the id of the threat you want to inspect", (i)-> 0<=i && i<engine.getDefCount(),"Index out of range");
                    IO.println(engine.getInfo(curVirId));
                    break;
                case 7:
                    break;
                default:
                    IO.println("Invalid option");
                    break;
            }
        }
    }
}
