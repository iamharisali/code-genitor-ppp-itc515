package library.borrowbook;
import java.util.Scanner;


public class BorrowBookUI {
	
	public static enum UiState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };

	private BorrowBookControl CoNtRoL;
	private Scanner InPuT;
	private UiState StaTe;

	
	public BorrowBookUI(BorrowBookControl control) {
		this.CoNtRoL = control;
		InPuT = new Scanner(System.in);
		StaTe = UiState.INITIALISED;
		control.setUi(this);
	}

	
	private String iNpUT(String PrOmPt) {
		System.out.print(PrOmPt);
		return InPuT.nextLine();
	}	
		
		
	private void OuTpUt(Object ObJeCt) {
		System.out.println(ObJeCt);
	}
	
			
	public void setState(UiState StAtE) {
		this.StaTe = StAtE;
	}

	
	public void run() {
		OuTpUt("Borrow Book Use Case UI\n");
		
		while (true) {
			
			switch (StaTe) {			
			
			case CANCELLED:
				OuTpUt("Borrowing Cancelled");
				return;

				
			case READY:
				String MEM_STR = iNpUT("Swipe member card (press <enter> to cancel): ");
				if (MEM_STR.length() == 0) {
					CoNtRoL.cancel();
					break;
				}
				try {
					int MeMbEr_Id = Integer.valueOf(MEM_STR).intValue();
					CoNtRoL.swiped(MeMbEr_Id);
				}
				catch (NumberFormatException e) {
					OuTpUt("Invalid Member Id");
				}
				break;

				
			case RESTRICTED:
				iNpUT("Press <any key> to cancel");
				CoNtRoL.cancel();
				break;
			
				
			case SCANNING:
				String BoOk_StRiNg_InPuT = iNpUT("Scan Book (<enter> completes): ");
				if (BoOk_StRiNg_InPuT.length() == 0) {
					CoNtRoL.complete();
					break;
				}
				try {
					int BiD = Integer.valueOf(BoOk_StRiNg_InPuT).intValue();
					CoNtRoL.scanned(BiD);
					
				} catch (NumberFormatException e) {
					OuTpUt("Invalid Book Id");
				} 
				break;
					
				
			case FINALISING:
				String AnS = iNpUT("Commit loans? (Y/N): ");
				if (AnS.toUpperCase().equals("N")) {
					CoNtRoL.cancel();
					
				} else {
					CoNtRoL.commitLoans();
					iNpUT("Press <any key> to complete ");
				}
				break;
				
				
			case COMPLETED:
				OuTpUt("Borrowing Completed");
				return;
	
				
			default:
				OuTpUt("Unhandled state");
				throw new RuntimeException("BorrowBookUI : unhandled state :" + StaTe);			
			}
		}		
	}


	public void display(Object object) {
		OuTpUt(object);		
	}


}
