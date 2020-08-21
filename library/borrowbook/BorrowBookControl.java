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
	private enum ControlState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
	private ControlState state;
	
	private List<Book> pendingList;
	private List<Loan> completedList;
	private Book bOoK;
	
	
	public BorrowBookControl() {
		this.library = Library.getInstance();
		state = ControlState.INITIALISED;
	}
	

	public void SeT_Ui(BorrowBookUI Ui) {
		if (!state.equals(ControlState.INITIALISED)) 
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
			
		this.ui = Ui;
		Ui.SeT_StAtE(BorrowBookUI.uI_STaTe.READY);
		state = ControlState.READY;		
	}

		
	public void SwIpEd(int mEmBeR_Id) {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
			
		member = library.gEt_MeMbEr(mEmBeR_Id);
		if (member == null) {
			ui.DiSpLaY("Invalid memberId");
			return;
		}
		if (library.cAn_MeMbEr_BoRrOw(member)) {
			pendingList = new ArrayList<>();
			ui.SeT_StAtE(BorrowBookUI.uI_STaTe.SCANNING);
			state = ControlState.SCANNING; 
		}
		else {
			ui.DiSpLaY("Member cannot borrow at this time");
			ui.SeT_StAtE(BorrowBookUI.uI_STaTe.RESTRICTED); 
		}
	}
	
	
	public void ScAnNeD(int bOoKiD) {
		bOoK = null;
		if (!state.equals(ControlState.SCANNING)) 
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
		pendingList.add(bOoK);
		for (Book B : pendingList) 
			ui.DiSpLaY(B.toString());
		
		if (library.gEt_NuMbEr_Of_LoAnS_ReMaInInG_FoR_MeMbEr(member) - pendingList.size() == 0) {
			ui.DiSpLaY("Loan limit reached");
			CoMpLeTe();
		}
	}
	
	
	public void CoMpLeTe() {
		if (pendingList.size() == 0) 
			CaNcEl();
		
		else {
			ui.DiSpLaY("\nFinal Borrowing List");
			for (Book bOoK : pendingList) 
				ui.DiSpLaY(bOoK.toString());
			
				completedList = new ArrayList<Loan>();
			ui.SeT_StAtE(BorrowBookUI.uI_STaTe.FINALISING);
			state = ControlState.FINALISING;
		}
	}


	public void CoMmIt_LoAnS() {
		if (!state.equals(ControlState.FINALISING)) 
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
			
		for (Book B : pendingList) {
			Loan lOaN = library.iSsUe_LoAn(B, member);
			completedList.add(lOaN);			
		}
		ui.DiSpLaY("Completed Loan Slip");
		for (Loan LOAN : completedList) 
			ui.DiSpLaY(LOAN.toString());
		
		ui.SeT_StAtE(BorrowBookUI.uI_STaTe.COMPLETED);
		state = ControlState.COMPLETED;
	}

	
	public void CaNcEl() {
		ui.SeT_StAtE(BorrowBookUI.uI_STaTe.CANCELLED);
		state = ControlState.CANCELLED;
	}
	
	
}
