import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;


public class TestSPR {
	
	public static void testData(Database db){
		//String query="Codestream='Connections 2.5' & SPRState='Open' & TestPhase='FVT' & Project='Ventura Install'";
		String query="Codestream='Connections 2.5' & SPRState='Open' & TestPhase='FVT' & Project='Install'";
		DocumentCollection bugs = null;
		
		try {
			bugs = db.search(query);
			System.out.println(">>bug count:"+bugs.getCount());
			Document bug = null;
			for(bug = bugs.getFirstDocument(); bug != null; bug = bugs.getNextDocument()){
				String severity=bug.getItemValueString("Severity");
				String sprID=bug.getItemValueString("SPRID");
				String briefDesc=bug.getItemValueString("BriefDescription");
				System.out.println(severity);
				System.out.println(sprID);
				System.out.println(briefDesc);
			}
		} catch (NotesException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public static int getSPRNumByOwner (Database db, String owner) {
		
		int count = 0;
		DocumentCollection bugs = null;
		Document bug = null;
		
		
		
		try {
			bugs = db.search("@Contains(Codestream; \"Connections 2.5\")");
		} catch (NotesException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		bugs = bugs.FTSearch("@Contains(Project; \"Ventura Install\")");
//	
//		bugs = bugs.search("@Contains(TestPhase; \"FVT\")");

//		DocumentCollection bugs = db.search("@Contains(Severity; \"1\")");
//		DocumentCollection bugs = db.search("@Contains(SPRState; \"Open\")");
//		DocumentCollection bugs = db.search("@Contains(SPRState; \"Pending\")");
//		DocumentCollection bugs = db.search("@Contains(SPRSubState; \"Fixed\")");
//		DocumentCollection bugs = db.search("@Contains(\"QA Assign\"; \"Bo Song\")");
//		DocumentCollection bugs = db.search("@Contains(\"Dev_Assign\"; \"Hao H Zhang\")");
//		DocumentCollection bugs = db.search("@Contains(ProductGroup; \"Connections\")");

//		DocumentCollection bugs = db.search("@Contains(CS_CodeBase; \"Connections\")");
		
//		int count = 0;
//		
//		Document bug = null;
//		for(bug = bugs.getFirstDocument(); bug != null; bug = bugs.getNextDocument()){
//			
//			String project = null;
//			String testPhase = null;
//			String severity = null;
//			
//			project = bug.getItemValueString("Project");
//			testPhase = bug.getItemValueString("TestPhase");
//			severity = bug.getItemValueString("Severity");
//			System.out.println("project = " + project + ", testPhase = " + testPhase + ", severity = " + severity);
//			
//			if ("Ventura Install".equalsIgnoreCase(project)) {
//				if ("FVT".equalsIgnoreCase(testPhase)) {
//					if("1".equalsIgnoreCase(severity)) {
//					
//						count++;
//						System.out.println("current_count = " + count);
//					}
//				}
//			}
//			
//		}
//		System.out.println("final_count = " + count);
		
		String prexStr = "@Contains(\"Dev_Assign\"; \"";
		String postStr = "\")";
		String searchExp =  prexStr + owner + postStr;
		
		try {
			
			bugs = db.search(searchExp);
			
			for(bug = bugs.getFirstDocument(); bug != null; bug = bugs.getNextDocument()){
				
				String codeStream = null;
								
				codeStream = bug.getItemValueString("Codestream");
				
				System.out.println("codeStream = " + codeStream + ", owner = " + owner);
				

				
			}
			System.out.println("final_count = " + count);
		} catch (NotesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
		
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		Session session;
		try {
			session = NotesFactory.createSession("9.33.9.232:63148", "xieling@cn.ibm.com", "emoxuan2");
			Database db = session.getDatabase(null, "l_dir\\lcspr.nsf");
			System.out.println(">>>"+db);
			
			
//			DocumentCollection bugs = db.search("@Contains(SPRState; \"Open\")");
//			System.out.println(bugs.getCount());
			testData(db);
			
		} catch (NotesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
