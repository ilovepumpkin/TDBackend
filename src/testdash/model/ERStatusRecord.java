package testdash.model;

public class ERStatusRecord {
	private long unattemptedPoints;
	private long attemptedPoints;
	private long blockedPoints;
	private long failedPoints;
	private long completedPoints;
	private long deferredPoints;
	private long permFailPoints;
	
	public ERStatusRecord(long attemptedPoints, long blockedPoints,
			long completedPoints, long deferredPoints, long failedPoints,
			long permFailPoints, long unattemptedPoints) {
		super();
		this.attemptedPoints = attemptedPoints;
		this.blockedPoints = blockedPoints;
		this.completedPoints = completedPoints;
		this.deferredPoints = deferredPoints;
		this.failedPoints = failedPoints;
		this.permFailPoints = permFailPoints;
		this.unattemptedPoints = unattemptedPoints;
	}
	
	public long getUnattemptedPoints() {
		return unattemptedPoints;
	}
	public long getAttemptedPoints() {
		return attemptedPoints;
	}
	public long getBlockedPoints() {
		return blockedPoints;
	}
	public long getFailedPoints() {
		return failedPoints;
	}
	public long getCompletedPoints() {
		return completedPoints;
	}
	public long getDeferredPoints() {
		return deferredPoints;
	}
	public long getPermFailPoints() {
		return permFailPoints;
	}
	
	
	
}
