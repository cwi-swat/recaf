package recaf.core;

import java.util.ArrayList;

import recaf.core.functional.SD;

public class SwitchContext<R> {
	public int caseNumber;
	public boolean caseFound;
	public boolean defaultFound;
	public boolean breakFound;
	public boolean fallThrough;
	public ArrayList<SD<R>> recordCases;
	
	public SwitchContext() {
		recordCases = new ArrayList<>();
	}
}