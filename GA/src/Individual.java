
public interface Individual {
	double fitness = 0;
	public String getIndividualType();
	public void generateIndividual();
	public double getFitness();
	public void setFitness(double newfitness);
	public int size();
	public void setGene(int index, byte value);
	public byte getGene(int index);
	
}
