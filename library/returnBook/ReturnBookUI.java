package library.returnBook;
import java.util.Scanner;


public class ReturnBookUI {

	public static enum UiState { INITIALISED, READY, INSPECTING, COMPLETED };

	private ReturnBookControl control;
	private Scanner input;
	private UiState state;

	
	public ReturnBookUI(ReturnBookControl control) {
		this.control = control;
		input = new Scanner(System.in);
		state = UiState.INITIALISED;
		control.setUi(this);
	}


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
				String AnS = input("Is book damaged? (Y/N): ");
				boolean isDamaged = false;
				if (AnS.toUpperCase().equals("Y")) {					
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
	
	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}		
		
	private void outPut(Object objeCt) {
		System.out.println(objeCt);
	}	
			
	public void display(Object object) {
		outPut(object);
	}
	
	public void setState(UiState state) {
		this.state = state;
	}	
}
