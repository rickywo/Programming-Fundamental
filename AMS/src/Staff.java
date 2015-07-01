public class Staff {
   private String eNo;
   private String name;
   private String position;
   public Staff(String eNo, String name, String position)
   {
	   this.eNo = eNo;
	   this.name = name;
	   this.position = position;   
   }
   public String getName()
   {
	   return name;
   }
   public String getENo()
   {
	   return eNo;
   }

	public String toString() {
		return "eNo: " + eNo + " name: " + name + " position: " + position;
	}
}
