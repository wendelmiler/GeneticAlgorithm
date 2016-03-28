
public class BinaryIndividual implements Individual {

     static int defaultGeneLength = 50;
     private byte[] genes = new byte[defaultGeneLength];
     private double fitness = 0;
     
     @Override
     public String getIndividualType(){
    	 return "Binary Individual";
     }

     // Create a random individual
     @Override
     public void generateIndividual() {
         for (int i = 0; i < size(); i++) {
             byte gene = (byte) Math.round(Math.random()); 
        	 //byte gene = Byte.parseByte("1");
             genes[i] = gene;
         }
     }

     /* Getters and setters */
     public static void setDefaultGeneLength(int length) {
         defaultGeneLength = length;
     }
     
     @Override
     public byte getGene(int index) {
         return genes[index];
     }
     
     @Override
     public void setGene(int index, byte value) {
         genes[index] = value;
         fitness = 0;
     }

     @Override
     public int size() {
         return genes.length;
     }
     
     @Override
     public double getFitness() {
         if (fitness == 0) {
             fitness = calcFitness();
         }
         return fitness;
     }
     
     @Override
     public void setFitness(double newfitness){
    	 this.fitness=newfitness;
     }

     @Override
     public String toString() {
         String geneString = "";
         for (int i = 0; i < size(); i++) {
             geneString += getGene(i);
         }
         return geneString;
     }
     /*
      * Calculate individuals' fitness for F6(x,y) function
      */
     private double calcFitness(){
         double x_i = 0;
         double y_i = 0;
         
         // convert x binary to x decimal
         String s = toString();
         String xb = s.substring(0,25);
         String yb = s.substring(25,50);
         double x = Integer.parseInt(xb,2);
         double y = Integer.parseInt(yb,2);
         
         x = x*(200/(Math.pow(2,25)-1))-100;        
         y = y*(200/(Math.pow(2,25)-1))-100;
         fitness = 0.5-(Math.pow(Math.sin(Math.hypot(x,y)),2)-0.5)/Math.pow(1+0.001*(Math.pow(x,2)+Math.pow(y,2)),2);
         
         return fitness;
     }
 }