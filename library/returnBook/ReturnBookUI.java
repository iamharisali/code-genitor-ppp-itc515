package library.returnBook;

import java.util.Scanner;

public class ReturnBookUI {

	public static enum UiState {
		INITIALISED, READY, INSPECTING, COMPLETED
	};

<<<<<<< HEAD
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
=======
	private rETURN_bOOK_cONTROL CoNtRoL;
	private Scanner iNpUt;
	private uI_sTaTe StATe;

	
	public ReturnBookUI(rETURN_bOOK_cONTROL cOnTrOL) {
		this.CoNtRoL = cOnTrOL;
		iNpUt = new Scanner(System.in);
		StATe = uI_sTaTe.INITIALISED;
		cOnTrOL.sEt_uI(this);
>>>>>>> 5155ad24cb3208f29c49e00eddc2d7bfab2fa288
	}

	/**
	 *
	 */
	public void run() {
		outPut("Return Book Use Case UI\n");

		while (true) {
<<<<<<< HEAD

			switch (state) {
=======
			
			switch (StATe) {
			
>>>>>>> 5155ad24cb3208f29c49e00eddc2d7bfab2fa288
			case INITIALISED:
				break;
			case READY:
<<<<<<< HEAD
				String bookInputString = input("Scan Book (<enter> completes): ");
				if (bookInputString.length() == 0) {
					control.scanningComplete();
				} else {
					try {
						int bookId = Integer.valueOf(bookInputString).intValue();
						control.bookScaned(bookId);
					} catch (NumberFormatException e) {
						outPut("Invalid bookId");
=======
				String BoOk_InPuT_StRiNg = iNpUt("Scan Book (<enter> completes): ");
				if (BoOk_InPuT_StRiNg.length() == 0) 
					CoNtRoL.sCaNnInG_cOmPlEtE();
				
				else {
					try {
						int Book_Id = Integer.valueOf(BoOk_InPuT_StRiNg).intValue();
						CoNtRoL.bOoK_sCaNnEd(Book_Id);
>>>>>>> 5155ad24cb3208f29c49e00eddc2d7bfab2fa288
					}
				}
				break;
			case INSPECTING:
<<<<<<< HEAD
				String answer = input("Is book damaged? (Y/N): ");
				boolean isDamaged = false;
				if (answer.toUpperCase().equals("Y")) {
					isDamaged = true;
				}
				control.disChargeLoan(isDamaged);
=======
				String AnS = iNpUt("Is book damaged? (Y/N): ");
				boolean Is_DAmAgEd = false;
				if (AnS.toUpperCase().equals("Y")) 					
					Is_DAmAgEd = true;
				
				CoNtRoL.dIsChArGe_lOaN(Is_DAmAgEd);
			
>>>>>>> 5155ad24cb3208f29c49e00eddc2d7bfab2fa288
			case COMPLETED:
				outPut("Return processing complete");
				return;
			default:
<<<<<<< HEAD
				outPut("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + state);
=======
				oUtPuT("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + StATe);			
>>>>>>> 5155ad24cb3208f29c49e00eddc2d7bfab2fa288
			}
		}
	}

<<<<<<< HEAD
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
=======
	
	private String iNpUt(String PrOmPt) {
		System.out.print(PrOmPt);
		return iNpUt.nextLine();
	}	
		
		
	private void oUtPuT(Object ObJeCt) {
		System.out.println(ObJeCt);
	}
	
			
	public void display(Object object) {
		oUtPuT(object);
	}
	
	public void sEt_sTaTe(uI_sTaTe state) {
		this.StATe = state;
>>>>>>> 5155ad24cb3208f29c49e00eddc2d7bfab2fa288
	}

	/**
	 *
	 * @param state
	 */
	public void setState(UiState state) {
		this.state = state;
	}
}
