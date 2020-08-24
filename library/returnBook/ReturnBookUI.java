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
		control.sEt_uI(this);
	}


	public void run() {		
		outPut("Return Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case INITIALISED:
				break;
				
			case READY:
				String bookInputString = input("Scan Book (<enter> completes): ");
				if (bookInputString.length() == 0) 
					control.sCaNnInG_cOmPlEtE();
				
				else {
					try {
						int Book_Id = Integer.valueOf(bookInputString).intValue();
						control.bOoK_sCaNnEd(Book_Id);
					}
					catch (NumberFormatException e) {
						outPut("Invalid bookId");
					}					
				}
				break;								
				
			case INSPECTING:
				String AnS = input("Is book damaged? (Y/N): ");
				boolean Is_DAmAgEd = false;
				if (AnS.toUpperCase().equals("Y")) 					
					Is_DAmAgEd = true;
				
				control.dIsChArGe_lOaN(Is_DAmAgEd);
			
			case COMPLETED:
				outPut("Return processing complete");
				return;
			
			default:
				outPut("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + state);			
			}
		}
	}

	
	private String input(String PrOmPt) {
		System.out.print(PrOmPt);
		return input.nextLine();
	}	
		
		
	private void outPut(Object ObJeCt) {
		System.out.println(ObJeCt);
	}
	
			
	public void display(Object object) {
		outPut(object);
	}
	
	public void setState(UiState state) {
		this.state = state;
	}

	
}
