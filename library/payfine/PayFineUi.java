package library.payfine;
import java.util.Scanner;


public class PayFineUi {


	public static enum UiState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };

	private PayFineControl CoNtRoL;
	private Scanner input;
	private UiState state;

	
	public PayFineUi(PayFineControl control) {
		this.CoNtRoL = control;
		input = new Scanner(System.in);
		state = UiState.INITIALISED;
		control.setUi(this);
	}
	
	
	public void setState(UiState state) {
		this.state = state;
	}


	public void Run() {
		output("Pay Fine Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case READY:
				String Mem_Str = input("Swipe member card (press <enter> to cancel): ");
				if (Mem_Str.length() == 0) {
					CoNtRoL.Cancel();
					break;
				}
				try {
					int memberId = Integer.valueOf(Mem_Str).intValue();
					CoNtRoL.CardSwiped(memberId);
				}
				catch (NumberFormatException e) {
					output("Invalid memberId");
				}
				break;
				
			case PAYING:
				double AmouNT = 0;
				String Amt_Str = input("Enter amount (<Enter> cancels) : ");
				if (Amt_Str.length() == 0) {
					CoNtRoL.Cancel();
					break;
				}
				try {
					AmouNT = Double.valueOf(Amt_Str).doubleValue();
				}
				catch (NumberFormatException e) {}
				if (AmouNT <= 0) {
					output("Amount must be positive");
					break;
				}
				CoNtRoL.PayFine(AmouNT);
				break;
								
			case CANCELLED:
				output("Pay Fine process cancelled");
				return;
			
			case COMPLETED:
				output("Pay Fine process complete");
				return;
			
			default:
				output("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + state);			
			
			}		
		}		
	}

	
	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}	
		
		
	private void output(Object object) {
		System.out.println(object);
	}	
			

	public void display(Object object) {
		output(object);
	}


}
