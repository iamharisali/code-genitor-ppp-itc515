
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
	private Date loanDate;
	
	private Map<Integer, Book> catalog;
	private Map<Integer, Member> members;
	private Map<Integer, Loan> loans;
	private Map<Integer, Loan> currentLoans;
	private Map<Integer, Book> damagedBooks;
	

	private Library() {
		catalog = new HashMap<>();
		members = new HashMap<>();
		loans = new HashMap<>();
		currentLoans = new HashMap<>();
		damagedBooks = new HashMap<>();
		bookId = 1;
		memberId = 1;		
		loanId = 1;		
	}

	
	public static synchronized Library getInstance() {		
		if (Self == null) {
			Path path = Paths.get(LibraryFile);			
			if (Files.exists(path)) {	
				try (ObjectInputStream libraryFile = new ObjectInputStream(new FileInputStream(LibraryFile));) {
			    
					Self = (Library) libraryFile.readObject();
					Calendar.getInstance().setDate(Self.loanDate);
					libraryFile.close();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else Self = new Library();
		}
		return Self;
	}

	
	public static synchronized void save() {
		if (Self != null) {
			Self.loanDate = Calendar.getInstance().getDate();
			try (ObjectOutputStream libraryFile = new ObjectOutputStream(new FileOutputStream(LibraryFile));) {
				libraryFile.writeObject(Self);
				libraryFile.flush();
				libraryFile.close();	
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int getBookId() {
		return bookId;
	}
	
	
	public int getMemberId() {
		return memberId;
	}
	
	
	private int getNextBookId() {
		return bookId++;
	}

	
	private int getNextMemberId() {
		return memberId++;
	}

	
	private int getNextLoanId() {
		return loanId++;
	}

	
	public List<Member> listMembers() {		
		return new ArrayList<Member>(members.values()); 
	}


	public List<Book> listBooks() {		
		return new ArrayList<Book>(catalog.values()); 
	}


	public List<Loan> listCurrentLoans() {
		return new ArrayList<Loan>(currentLoans.values());
	}


	public Member addMember(String lastName, String firstName, String email, int phoneNo) {		
		Member member = new Member(lastName, firstName, email, phoneNo, getNextMemberId());
		members.put(member.getId(), member);		
		return member;
	}

	
	public Book addBook(String a, String t, String c) {		
		Book b = new Book(a, t, c, getNextBookId());
		catalog.put(b.getId(), b);		
		return b;
	}

	
	public Member getMember(int memberId) {
		if (members.containsKey(memberId)) 
			return members.get(memberId);
		return null;
	}

	
	public Book getBook(int bookId) {
		if (catalog.containsKey(bookId)) 
			return catalog.get(bookId);		
		return null;
	}

	
	public int getLoanLimit() {
		return LoanLimit;
	}

	
	public boolean canMemberBorrow(Member member) {		
		if (member.getNumberOfCurrentLoans() == LoanLimit ) 
			return false;
				
		if (member.finesOwed() >= MaxFinesOwed) 
			return false;
				
		for (Loan loan : member.getLoans()) 
			if (loan.isOverDue()) 
				return false;
			
		return true;
	}

	
	public int getNumberOfLoansRemainingForMember(Member member) {		
		return LoanLimit - member.getNumberOfCurrentLoans();
	}

	
	public Loan issueLoan(Book book, Member member) {
		Date dueDate = Calendar.getInstance().getDueDate(LoanPeriod);
		Loan loan = new Loan(getNextLoanId(), book, member, dueDate);
		member.takeOutLoan(loan);
		book.borrow();
		loans.put(loan.getId(), loan);
		currentLoans.put(book.getId(), loan);
		return loan;
	}
	
	
	public Loan getLoanByBookId(int bookId) {
		if (currentLoans.containsKey(bookId)) 
			return currentLoans.get(bookId);
		
		return null;
	}

	
	public double calculateOverdueFines(Loan loan) {
		if (loan.isOverDue()) {
			long daysOverDue = Calendar.getInstance().getDaysDifference(loan.getDueDate());
			double fine = daysOverDue * FinePerDay;
			return fine;
		}
		return 0.0;		
	}


	public void dischargeLoans(Loan currentLoan, boolean isDamaged) {
		Member member = currentLoan.getMember();
		Book book  = currentLoan.getBook();
		
		double overDueFIne = calculateOverdueFines(currentLoan);
		member.addFine(overDueFIne);	
		
		member.disChargeLoan(currentLoan);
		book.returnState(isDamaged);
		if (isDamaged) {
			member.addFine(DamageFee);
			damagedBooks.put(book.getId(), book);
		}
		currentLoan.discharge();
		currentLoans.remove(book.getId());
	}


	public void checkCurrentLoans() {
		for (Loan loan : currentLoans.values()) 
			loan.checkOverDue();
				
	}


	public void repairBook(Book currentBook) {
		if (damagedBooks.containsKey(currentBook.getId())) {
			currentBook.repair();
			damagedBooks.remove(currentBook.getId());
		}
		else 
			throw new RuntimeException("Library: repairBook: book is not damaged");
		
		
	}
	
	
}

