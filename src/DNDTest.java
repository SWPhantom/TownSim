//Tester class that gives the user ability to choose what to do with
//the Human creation toolset.

import java.util.*;
public class DNDTest{
	public static void main(String [] args){
		//DECLARATIONS:
		Scanner key = new Scanner(System.in);
		UI ui = new UI();

		//RunTime
		//TEMP THINGS:
		boolean finish = false;
		while(!finish){
			System.out.println("Choose what to do!");
			System.out.println("1 - Choose name modules.");
			System.out.println("2 - Generate Families.");
			System.out.println("3 - Output to Terminal and File.");
			System.out.println("4 - Quit");
			int input = key.nextInt();
			if(input == 1){
				ui.chooseNameList();
			}
			if(input == 2){
				ui.generatePeople();
			}
			if(input == 3){
				System.out.println(ui.output());
			}
			if(input == 4){
				System.out.println("EVERYTHING IS OVER.\n\n");
				finish = true;
			}

		}
	}

}
