
public class IndividualFactory {
	
	public static Individual calveIndividual(String type){
		
		if(type.equalsIgnoreCase("Binary")){
			return new BinaryIndividual();
		}
		
		return null;
	}
}
