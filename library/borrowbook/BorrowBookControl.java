package library.borrowbook;
import java.util.ArrayList;
import java.util.List;

import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;
import library.entities.Member;

public class BorrowBookControl {
	
	private BorrowBookUI ui;
	
	private Library library;
	private Member member;
	private enum CONTROL_STATE { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private CONTROL_STATE sTaTe;
	
	private List<Book> pEnDiNg_LiSt;
	private List<Loan> cOmPlEtEd_LiSt;
	private Book bOoK;
	
	
	public BorrowBookControl() {
		this.library = Library.getInstance();
		sTaTe = CONTROL_STATE.INITIALISED;
	}
	

	public void SeT_Ui(BorrowBookUI Ui) {
		if (!sTaTe.equals(CONTROL_STATE.INITIALISED)) 
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
			
		this.ui = Ui;
		Ui.SeT_StAtE(BorrowBookUI.ui_STaTe.READY);
		sTaTe = CONTROL_STATE.READY;		
	}

		
	public void SwIpEd(int mEmBeR_Id) {
		if (!sTaTe.equals(CONTROL_STATE.READY)) 
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
			
		member = library.gEt_MeMbEr(mEmBeR_Id);
		if (member == null) {
			ui.DiSpLaY("Invalid memberId");
			return;
		}
		if (library.cAn_MeMbEr_BoRrOw(member)) {
			pEnDiNg_LiSt = new ArrayList<>();
			ui.SeT_StAtE(BorrowBookUI.ui_STaTe.SCANNING);
			sTaTe = CONTROL_STATE.SCANNING; 
		}
		else {
			ui.DiSpLaY("Member cannot borrow at this time");
			ui.SeT_StAtE(BorrowBookUI.ui_STaTe.RESTRICTED); 
		}
	}
	
	
	public void ScAnNeD(int bOoKiD) {
		bOoK = null;
		if (!sTaTe.equals(CONTROL_STATE.SCANNING)) 
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
			
		bOoK = library.gEt_BoOk(bOoKiD);
		if (bOoK == null) {
			ui.DiSpLaY("Invalid bookId");
			return;
		}
		if (!bOoK.iS_AvAiLaBlE()) {
			ui.DiSpLaY("Book cannot be borrowed");
			return;
		}
		pEnDiNg_LiSt.add(bOoK);
		for (Book B : pEnDiNg_LiSt) 
			ui.DiSpLaY(B.toString());
		
		if (library.gEt_NuMbEr_Of_LoAnS_ReMaInInG_FoR_MeMbEr(member) - pEnDiNg_LiSt.size() == 0) {
			ui.DiSpLaY("Loan limit reached");
			CoMpLeTe();
		}
	}
	
	
	public void CoMpLeTe() {
		if (pEnDiNg_LiSt.size() == 0) 
			CaNcEl();
		
		else {
			ui.DiSpLaY("\nFinal Borrowing List");
			for (Book bOoK : pEnDiNg_LiSt) 
				ui.DiSpLaY(bOoK.toString());
			
			cOmPlEtEd_LiSt = new ArrayList<Loan>();
			ui.SeT_StAtE(BorrowBookUI.ui_STaTe.FINALISING);
			sTaTe = CONTROL_STATE.FINALISING;
		}
	}


	public void CoMmIt_LoAnS() {
		if (!sTaTe.equals(CONTROL_STATE.FINALISING)) 
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
			
		for (Book B : pEnDiNg_LiSt) {
			Loan lOaN = library.iSsUe_LoAn(B, member);
			cOmPlEtEd_LiSt.add(lOaN);			
		}
		ui.DiSpLaY("Completed Loan Slip");
		for (Loan LOAN : cOmPlEtEd_LiSt) 
			ui.DiSpLaY(LOAN.toString());
		
		ui.SeT_StAtE(BorrowBookUI.ui_STaTe.COMPLETED);
		sTaTe = CONTROL_STATE.COMPLETED;
	}

	
	public void CaNcEl() {
		ui.SeT_StAtE(BorrowBookUI.ui_STaTe.CANCELLED);
		sTaTe = CONTROL_STATE.CANCELLED;
	}
	
	
}
