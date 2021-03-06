package library.entities;
import java.util.Date;
import java.util.concurrent.TimeUnit;

///////////////////////////////////////////////////////////////////////////////
//                   
// Subject:          Professional Programming Practice 
// Author:           DON MEERIYAGALLA
// Email:            lakshansm90@gmail.com
// Lecturer's Name:  Recep Ulusoy 
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////

public class Calendar {
	
	private static Calendar self;
	private static java.util.Calendar calendar;
	
	
	private Calendar() {
		calendar = java.util.Calendar.getInstance();
	}
	
	public static Calendar getInstance() {
		if (self == null) {
			self = new Calendar();
		}
		return self;
	}
	
    /**
     *
     * @param days
     */
    public void incrementDate(int days) {
		calendar.add(java.util.Calendar.DATE, days);		
	}
	
    /**
     *
     * @param date
     */
    public synchronized void setDate(Date date) {
		try {
			calendar.setTime(date);
	        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        calendar.set(java.util.Calendar.MINUTE, 0);  
	        calendar.set(java.util.Calendar.SECOND, 0);  
	        calendar.set(java.util.Calendar.MILLISECOND, 0);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	public synchronized Date getDate() {
		try {
	        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);  
	        calendar.set(java.util.Calendar.MINUTE, 0);  
	        calendar.set(java.util.Calendar.SECOND, 0);  
	        calendar.set(java.util.Calendar.MILLISECOND, 0);
			return calendar.getTime();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

    /**
     *
     * @param loanPeriod
     * @return
     */
    public synchronized Date getDueDate(int loanPeriod) {
		Date now = getDate();
		calendar.add(java.util.Calendar.DATE, loanPeriod);
		Date dUeDaTe = calendar.getTime();
		calendar.setTime(now);
		return dUeDaTe;
	}
	
    /**
     *
     * @param targetDate
     * @return
     */
    public synchronized long getDaysDifference(Date targetDate) {
		
		long diffMillis = getDate().getTime() - targetDate.getTime();
	    long diffDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
	    return diffDays;
	}

}
