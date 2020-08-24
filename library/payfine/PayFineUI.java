package library.payfine;
import java.util.Scanner;


public class PayFineUi {


	public static enum UiState { INITIALISED, READY, PAYING, COMPLETED, CANCELLED };

	private pAY_fINE_cONTROL CoNtRoL;
	private Scanner input;
	private UiState state;

	
	public PayFineUi(pAY_fINE_cONTROL control) {
		this.CoNtRoL = control;
		input = new Scanner(System.in);
		state = UiState.INITIALISED;
		control.SeT_uI(this);
	}
	
	
	public void SetState(UiState state) {
		this.state = state;
	}


	public void Run() {
		output("Pay Fine Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case READY:
				String Mem_Str = input("Swipe member card (press <enter> to cancel): ");
				if (Mem_Str.length() == 0) {
					CoNtRoL.CaNcEl();
					break;
				}
				try {
					int Member_ID = Integer.valueOf(Mem_Str).intValue();
					CoNtRoL.CaRd_sWiPeD(Member_ID);
				}
				catch (NumberFormatException e) {
					output("Invalid memberId");
				}
				break;
				
			case PAYING:
				double AmouNT = 0;
				String Amt_Str = input("Enter amount (<Enter> cancels) : ");
				if (Amt_Str.length() == 0) {
					CoNtRoL.CaNcEl();
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
				CoNtRoL.PaY_FiNe(AmouNT);
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
			

	public void DiSplAY(Object object) {
		output(object);
	}


}
