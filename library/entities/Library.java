
package library.entities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Library implements Serializable {
	
	private static final String LibraryFile = "library.obj";
	private static final int LoanLimit = 2;
	private static final int LoanPeriod = 2;
	private static final double FinePerDay = 1.0;
	private static final double MaxFinesOwed = 1.0;
	private static final double DamageFee = 2.0;
	
	private static Library Self;
	private int bookId;
	private int memberId;
	private int loanId;
	private Date lOaN_DaTe;
	
	private Map<Integer, Book> CaTaLoG;
	private Map<Integer, Member> MeMbErS;
	private Map<Integer, Loan> LoAnS;
	private Map<Integer, Loan> CuRrEnT_LoAnS;
	private Map<Integer, Book> DaMaGeD_BoOkS;
	

	private Library() {
		CaTaLoG = new HashMap<>();
		MeMbErS = new HashMap<>();
		LoAnS = new HashMap<>();
		CuRrEnT_LoAnS = new HashMap<>();
		DaMaGeD_BoOkS = new HashMap<>();
		bookId = 1;
		memberId = 1;		
		loanId = 1;		
	}

	
	public static synchronized Library getInstance() {		
		if (Self == null) {
			Path PATH = Paths.get(LibraryFile);			
			if (Files.exists(PATH)) {	
				try (ObjectInputStream LiBrArY_FiLe = new ObjectInputStream(new FileInputStream(LibraryFile));) {
			    
					Self = (Library) LiBrArY_FiLe.readObject();
					Calendar.getInstance().setDate(Self.lOaN_DaTe);
					LiBrArY_FiLe.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else Self = new Library();
		}
		return Self;
	}

	
	public static synchronized void SaVe() {
		if (Self != null) {
			Self.lOaN_DaTe = Calendar.getInstance().getDate();
			try (ObjectOutputStream LiBrArY_fIlE = new ObjectOutputStream(new FileOutputStream(LibraryFile));) {
				LiBrArY_fIlE.writeObject(Self);
				LiBrArY_fIlE.flush();
				LiBrArY_fIlE.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int gEt_BoOkId() {
		return bookId;
	}
	
	
	public int gEt_MeMbEr_Id() {
		return memberId;
	}
	
	
	private int gEt_NeXt_BoOk_Id() {
		return bookId++;
	}

	
	private int gEt_NeXt_MeMbEr_Id() {
		return memberId++;
	}

	
	private int gEt_NeXt_LoAn_Id() {
		return loanId++;
	}

	
	public List<Member> lIsT_MeMbErS() {		
		return new ArrayList<Member>(MeMbErS.values()); 
	}


	public List<Book> lIsT_BoOkS() {		
		return new ArrayList<Book>(CaTaLoG.values()); 
	}


	public List<Loan> lISt_CuRrEnT_LoAnS() {
		return new ArrayList<Loan>(CuRrEnT_LoAnS.values());
	}


	public Member aDd_MeMbEr(String lastName, String firstName, String email, int phoneNo) {		
		Member member = new Member(lastName, firstName, email, phoneNo, gEt_NeXt_MeMbEr_Id());
		MeMbErS.put(member.getId(), member);		
		return member;
	}

	
	public Book aDd_BoOk(String a, String t, String c) {		
		Book b = new Book(a, t, c, gEt_NeXt_BoOk_Id());
		CaTaLoG.put(b.getId(), b);		
		return b;
	}

	
	public Member gEt_MeMbEr(int memberId) {
		if (MeMbErS.containsKey(memberId)) 
			return MeMbErS.get(memberId);
		return null;
	}

	
	public Book getBook(int bookId) {
		if (CaTaLoG.containsKey(bookId)) 
			return CaTaLoG.get(bookId);		
		return null;
	}

	
	public int gEt_LoAn_LiMiT() {
		return LoanLimit;
	}

	
	public boolean cAn_MeMbEr_BoRrOw(Member member) {		
		if (member.getNumberOfCurrentLoans() == LoanLimit ) 
			return false;
				
		if (member.finesOwed() >= MaxFinesOwed) 
			return false;
				
		for (Loan loan : member.getLoans()) 
			if (loan.Is_OvEr_DuE()) 
				return false;
			
		return true;
	}

	
	public int gEt_NuMbEr_Of_LoAnS_ReMaInInG_FoR_MeMbEr(Member MeMbEr) {		
		return LoanLimit - MeMbEr.getNumberOfCurrentLoans();
	}

	
	public Loan iSsUe_LoAn(Book book, Member member) {
		Date dueDate = Calendar.getInstance().getDueDate(LoanPeriod);
		Loan loan = new Loan(gEt_NeXt_LoAn_Id(), book, member, dueDate);
		member.takeOutLoan(loan);
		book.BoRrOw();
		LoAnS.put(loan.GeT_Id(), loan);
		CuRrEnT_LoAnS.put(book.getId(), loan);
		return loan;
	}
	
	
	public Loan GeT_LoAn_By_BoOkId(int bookId) {
		if (CuRrEnT_LoAnS.containsKey(bookId)) 
			return CuRrEnT_LoAnS.get(bookId);
		
		return null;
	}

	
	public double CaLcUlAtE_OvEr_DuE_FiNe(Loan LoAn) {
		if (LoAn.Is_OvEr_DuE()) {
			long DaYs_OvEr_DuE = Calendar.getInstance().getDaysDifference(LoAn.GeT_DuE_DaTe());
			double fInE = DaYs_OvEr_DuE * FinePerDay;
			return fInE;
		}
		return 0.0;		
	}


	public void DiScHaRgE_LoAn(Loan cUrReNt_LoAn, boolean iS_dAmAgEd) {
		Member mEmBeR = cUrReNt_LoAn.GeT_MeMbEr();
		Book bOoK  = cUrReNt_LoAn.GeT_BoOk();
		
		double oVeR_DuE_FiNe = CaLcUlAtE_OvEr_DuE_FiNe(cUrReNt_LoAn);
		mEmBeR.addFine(oVeR_DuE_FiNe);	
		
		mEmBeR.disChargeLoan(cUrReNt_LoAn);
		bOoK.ReTuRn(iS_dAmAgEd);
		if (iS_dAmAgEd) {
			mEmBeR.addFine(DamageFee);
			DaMaGeD_BoOkS.put(bOoK.getId(), bOoK);
		}
		cUrReNt_LoAn.DiScHaRgE();
		CuRrEnT_LoAnS.remove(bOoK.getId());
	}


	public void cHeCk_CuRrEnT_LoAnS() {
		for (Loan lOaN : CuRrEnT_LoAnS.values()) 
			lOaN.cHeCk_OvEr_DuE();
				
	}


	public void repairBook(Book cUrReNt_BoOk) {
		if (DaMaGeD_BoOkS.containsKey(cUrReNt_BoOk.getId())) {
			cUrReNt_BoOk.RePaIr();
			DaMaGeD_BoOkS.remove(cUrReNt_BoOk.getId());
		}
		else 
			throw new RuntimeException("Library: repairBook: book is not damaged");
		
		
	}
	
	
}

