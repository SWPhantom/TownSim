//Test file to be used in a command-line way.

import java.util.*;
import java.util.regex.MatchResult;
import java.io.*;
public class PopulationGenerator {
    public static void main(String[] args) {
        /*Fix this for BlueJ
        if (args.length != 1) {
            System.err.println("usage: java PopulationGenerator " + "/file/location");
            System.exit(0);
        }
        */

        //DECLARATIONS:
        //String filepath = args[0];
        String filepath = "./SourceFiles/Input.txt";
        
        File file = new File(filepath);
        int population=0;
        int families=0;
        ArrayList<String> classification = new ArrayList<String>();
        ArrayList<Integer> classificationNumber = new ArrayList<Integer>();
        UI ui = new UI();

        //A. This goes through a file specified by the command-line argument
        //and checks what the lines say. 
        try{
            Scanner s = new Scanner(file);
            while (s.hasNext()){
                String inputToken = s.next();
                if(inputToken.equals("Population:")){population=s.nextInt();}else
                if(inputToken.equals("Families:")){families=s.nextInt();}else
                if(inputToken.equals("Classification:")){
                    classification.add(s.next());
                    classificationNumber.add(s.nextInt());
                }
                //Switch statements for strings implemented in Java SE7. Save code for later.
                /*
                switch (inputToken){
                case "Population:": population=s.nextInt(); break;
                case "Families:": families=s.nextInt(); break;
                case "Classification:": classification.add(s.next());
                classificationNumber.add(s.nextInt()); break;
                default: break;
                }*/ 
            }

            //B. this section generates the people with the specified arguments.
            ui.generatePeople(families, population);
            for (int i=0; i<classification.size(); i++){
                ui.famGen.setMassClassification(classificationNumber.get(i), classification.get(i));
            }

            //C. Printing of the finished population. 
            //Both a text file and a command-line output
            //are created.
            String finalOutput = ui.output();
            try{
                // Create file 
                FileWriter fstream = new FileWriter("./SourceFiles/output.txt");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(finalOutput);
                //Close the output stream
                out.close();
            }catch (Exception e){//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }

            System.out.println(finalOutput);

            //System.out.println(ui.debugOutput());
            //System.out.println(ui.outputDatabase());
            s.close();
        }catch(FileNotFoundException x){
            x.printStackTrace();
        }catch (IOException x){
            x.printStackTrace();
        }
    }
}
