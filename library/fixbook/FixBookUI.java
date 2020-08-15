package library.fixbook;
import java.util.Scanner;


public class fixbookUi {

	public static enum uiState { INITIALISED, READY, FIXING, COMPLETED };

	private fixbookControl CoNtRoL;
	private Scanner InPuT;
	private uiState state;

	
	public fixbookUi(fixbookControl CoNtRoL) {
		this.CoNtRoL = CoNtRoL;
		InPuT = new Scanner(System.in);
		state = uiState.INITIALISED;
		CoNtRoL.SetUi(this);
	}


	public void setState(uiState stateSet) {
		this.state = stateSet;
	}

	
	public void RuN() {
		OuTpUt("Fix Book Use Case UI\n");
		
		while (true) {
			
			switch (state) {
			
			case READY:
				String BoOk_EnTrY_StRiNg = iNpUt("Scan Book (<enter> completes): ");
				if (BoOk_EnTrY_StRiNg.length() == 0) 
					CoNtRoL.SCannING_COMplete();
				
				else {
					try {
						int BoOk_Id = Integer.valueOf(BoOk_EnTrY_StRiNg).intValue();
						CoNtRoL.BoOk_ScAnNeD(BoOk_Id);
					}
					catch (NumberFormatException e) {
						OuTpUt("Invalid bookId");
					}
				}
				break;	
				
			case FIXING:
				String AnS = iNpUt("Fix Book? (Y/N) : ");
				boolean FiX = false;
				if (AnS.toUpperCase().equals("Y")) 
					FiX = true;
				
				CoNtRoL.FiX_BoOk(FiX);
				break;
								
			case COMPLETED:
				OuTpUt("Fixing process complete");
				return;
			
			default:
				OuTpUt("Unhandled state");
				throw new RuntimeException("fixbookUi : unhandled state :" + state);			
			
			}		
		}
		
	}

	
	private String iNpUt(String prompt) {
		System.out.print(prompt);
		return InPuT.nextLine();
	}	
		
		
	private void OuTpUt(Object object) {
		System.out.println(object);
	}
	

	public void dIsPlAy(Object object) {
		OuTpUt(object);
	}
	
	
}
