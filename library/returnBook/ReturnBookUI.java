package library.returnBook;
import java.util.Scanner;

///////////////////////////////////////////////////////////////////////////////
//                   
// Subject:          Professional Programming Practice 
// Author:           DON MEERIYAGALLA
// Email:            lakshansm90@gmail.com
// Lecturer's Name:  Recep Ulusoy 
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////

public class ReturnBookUI {

	public static enum UiState { INITIALISED, READY, INSPECTING, COMPLETED };

	private ReturnBookControl control;
	private Scanner input;
	private UiState state;
	
    /**
     *
     * @param control
     */
    public ReturnBookUI(ReturnBookControl control) {
		this.control = control;
		input = new Scanner(System.in);
		state = UiState.INITIALISED;
		control.setUi(this);
	}

    /**
     *
     */
    public void run() {		
		outPut("Return Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {			
                            case INITIALISED:
                                    break;
                            case READY:
                                    String bookInputString = input("Scan Book (<enter> completes): ");
                                    if (bookInputString.length() == 0) {
                                            control.scanningComplete();
                                    }
                                    else {
                                            try {
                                                    int bookId = Integer.valueOf(bookInputString).intValue();
                                                    control.bookScaned(bookId);
                                            }
                                            catch (NumberFormatException e) {
                                                    outPut("Invalid bookId");
                                            }					
                                    }
                                    break;
                            case INSPECTING:
                                    String answer = input("Is book damaged? (Y/N): ");
                                    boolean isDamaged = false;
                                    if (answer.toUpperCase().equals("Y")) {					
                                            isDamaged = true;
                                    }
                                    control.disChargeLoan(isDamaged);
                            case COMPLETED:
                                    outPut("Return processing complete");
                                    return;
                            default:
                                    outPut("Unhandled state");
                                    throw new RuntimeException("ReturnBookUI : unhandled state :" + state);			
			}
		}
	}
    
    /**
     *
     * @param prompt
     */	
	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}
        
    /**
     *
     * @param object
     */		
	private void outPut(Object objeCt) {
		System.out.println(objeCt);
	}	
			
    /**
     *
     * @param object
     */
    public void display(Object object) {
		outPut(object);
	}
	
    /**
     *
     * @param state
     */
    public void setState(UiState state) {
		this.state = state;
	}	
}
