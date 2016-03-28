import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class GeneticAlgorithm {

     /* Genetic Algorithm parameters */
     private static final double crossoverRate = 0.75;
     private static final double mutationRate = 0.01;
     private static final boolean elitism = false;
     private static int shift = 0;

     /* Population evolution method
      * Evolve a given population
      */
     public static Population evolvePop(Population pop) {
         Population newPopulation = new Population(pop.size(), false);
         /*
          * Use elitism
          */
         if(elitism){
        	 shift=1;
        	 newPopulation.saveIndividual(0, pop.getFittest());
         }
         /*
          *  Create a new population using the offsprings of previous population
          */
         for (int i = shift; i < pop.size(); i++) {
        	 Individual indiv1 = rouletteSelection(pop,false);
             Individual indiv2 = rouletteSelection(pop,false);
             //Individual indiv1 = tournamentSelection(pop,2);
             //Individual indiv2 = tournamentSelection(pop,2);
             Individual newIndiv = onePointCrossover(indiv1, indiv2);
             newPopulation.saveIndividual(i, newIndiv);
         }

         /*
          *  Mutate population
          */
         for (int i = shift; i < newPopulation.size(); i++) {
             mutate(newPopulation.getIndividual(i));
         }

         return newPopulation;
     }

     /* Crossover methods
      * Uniform crossover
      */
     private static Individual uniformCrossover(Individual indiv1, Individual indiv2) {
         Individual newIndiv = IndividualFactory.calveIndividual("binary");
         // Loop through genes
         for (int i = 0; i < indiv1.size(); i++) {
             // Crossover
             if (Math.random() <= crossoverRate) {
                 newIndiv.setGene(i, indiv1.getGene(i));
             } else {
                 newIndiv.setGene(i, indiv2.getGene(i));
             }
         }
         return newIndiv;
     }
     /*
      * One-point crossover
      */
     private static Individual onePointCrossover(Individual indiv1, Individual indiv2){
    	 Individual newIndiv = IndividualFactory.calveIndividual("binary");
    	 
    	 // make child's bytes equal to his father's bytes
    	 for(int i=0;i<newIndiv.size();i++){
    		 newIndiv.setGene(i, indiv1.getGene(i));
    	 }    	 
    	 if(Math.random() <= crossoverRate){
	    	 // generate a random point
	    	 int point = (int) (50*Math.random());
	    	 
	    	 // swap genes
	    	 for(int i=0;i<=point;i++){
	    		 newIndiv.setGene(i, indiv2.getGene(i));
	    	 }
    	 }
    	 return newIndiv;
     }
     /*
      * Two-point crossover
      */
     private static Individual twoPointCrossover(Individual indiv1, Individual indiv2){
    	 Individual newIndiv = IndividualFactory.calveIndividual("binary");
    	 
    	 // make child's bytes equal to his father's bytes
    	 for(int i=0;i<newIndiv.size();i++){
    		 newIndiv.setGene(i, indiv1.getGene(i));
    	 }
    	 
    	 if(Math.random() <= crossoverRate){
	    	 // generate random two points
	    	 int point1 = (int) (50*Math.random()); int point2 = (int) (50*Math.random());
	    	 
	    	 // assure that points are not the same and point2 > point1
			 if(point1 > point2){
				 int temp = point1;
				 point1 = point2;
				 point2 = temp;
			 }
			 // swap genes
	    	 for(int j=point1;j<=point2;j++){
	    		 newIndiv.setGene(j, indiv2.getGene(j));
	    	 }
    	 }
    	 return newIndiv;
     }
     
     /* Mutation method
      * Mutates a given individual
      */
     private static void mutate(Individual indiv) {
         // Loop through genes
         for (int i = 0; i < indiv.size(); i++) {
             if (Math.random() <= mutationRate) {
                 // Create random gene
                 byte gene = (byte) Math.round(Math.random());
                 indiv.setGene(i, gene);
             }
         }
     }

     /* Selection methods
      * Tournament selection
      */
     private static Individual tournamentSelection(Population pop, int selectionPopSize) {
    	 /*
    	  * The higher the selectionPopSize, the higher the selection pressure
    	  */
    	 
    	// Create a tournament population
         Population tournament = new Population(selectionPopSize, false);
         // For each place in the tournament get a random individual
         for (int i = 0; i < selectionPopSize; i++) {
             int randomId = (int) (Math.random() * pop.size());
             tournament.saveIndividual(i, pop.getIndividual(randomId));
         }
         // Get the fittest
         Individual fittest = tournament.getFittest();
         return fittest;
     }
     
     /*
      *  Roulette wheel selection with/without Linear Normalization
      */
     private static Individual rouletteSelection(Population pop, boolean normalize){
    	 HashMap<Double,Individual> unsortedWheel = new HashMap<Double,Individual>();
    	 HashMap<Double,Individual> sortedWheel = new HashMap<Double,Individual>();
    	 ArrayList<Double> temp = new ArrayList<Double>();
    	 double fitnessSum = 0; double newfitness=0;
    	 int min = 0; int max=20;
    	 Individual selectedIndiv = null;
    	 
    	 // calculate fitness sum
    	 for(int i=0;i<pop.individuals.length;i++){
    		 fitnessSum += pop.individuals[i].getFitness();
    	 }
    	 
    	 // define each wheel slice associated to its corresponding individual
    	 for(int j=0;j<pop.individuals.length;j++){
    		 unsortedWheel.put(pop.individuals[j].getFitness()/fitnessSum,pop.individuals[j]);
    		 temp.add(pop.individuals[j].getFitness()/fitnessSum);
    	 }
    	 
    	 // sort temp by ascending order
    	 Collections.sort(temp);

    	 // sort wheel by ascending order
    	 for(int j=0;j<pop.individuals.length;j++){
    		 if(normalize){
        		 newfitness=min+(j)*(max-min)/(pop.individuals.length-1);
        		 sortedWheel.put( newfitness , unsortedWheel.get(temp.get(j)) );
        		 //System.out.println("Temp before: "+temp.get(j)+" Number: "+j);
        		 temp.add(j, newfitness);
        		 //System.out.println("Temp after: "+temp.get(j)+" Number: "+j);
    		 }else{
    			 sortedWheel.put( temp.get(j) , unsortedWheel.get(temp.get(j)) );
    		 }
    	 }
    	 
   	 	 // get a random number from 0 to the biggest slice value 	
    	 double rand = Math.random()*temp.get(pop.individuals.length-1);
    	 //System.out.println("Biggest fitness: "+temp.get(pop.individuals.length-1));
    	 // iterate through sortedWheel in search for the slice where the random number is
    	 for(Map.Entry<Double, Individual> entry : sortedWheel.entrySet()){
    		 if(rand <= entry.getKey()){
    			 //System.out.println("Selected fitness before: "+entry.getKey());
    			 selectedIndiv = entry.getValue();
    			 //System.out.println("Selected fitness after: "+selectedIndiv.getFitness());
    		 }
    	 }

		//System.out.println("Selected fitness after: "+selectedIndiv.getFitness());
    	 return selectedIndiv;
     }
}
