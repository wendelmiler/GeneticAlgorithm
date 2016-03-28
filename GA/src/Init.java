import org.jfree.ui.RefineryUtilities;

import ga.awt.chart.XYLineChart_AWT;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection; 


public class Init {

     public static void main(String[] args) {
    	 final XYSeriesCollection dataset = new XYSeriesCollection( );
    	 double[] bestIndividualsSum = new double[100];
         // Create an initial population
         Population population = new Population(100, true); // 100 individuals   
         Population initialPop = population;
         Individual bestInitialInd = initialPop.getFittest();
         
    	 for(int k=0;k<3;k++){
	    	 Individual[] bestIndividuals = new Individual[100];	    	 
	    	 population = initialPop;

	         
	         // Evolve our population until we reach an optimum solution
	         int generationCount = 0;
	
	         while(generationCount < 100){	        	 
	             System.out.println("Generation: " + generationCount + " Fittest: " + population.getFittest().getFitness() + " Genes:  " + population.getFittest().toString());
	             population = GeneticAlgorithm.evolvePop(population);
	             bestIndividuals[generationCount] = population.getFittest();
	             bestIndividualsSum[generationCount] += population.getFittest().getFitness();
	             generationCount++;
	         }
	         System.out.print("Best solution found: ");
	         System.out.println(population.getFittest().getFitness());
	         System.out.print("Genes: ");
	         System.out.println(population.getFittest());
	     
	         // Parse from byte to real
	         String b1 = population.getFittest().toString();
	         b1 = b1.substring(0,25);
	         System.out.print("Binary X: "+b1);
	         double b = Integer.parseInt(b1,2);
	         b=b*(200/(Math.pow(2,25)-1))-100;
	         System.out.println(" Real X: "+b);
	         
	         String b2 = population.getFittest().toString();
	         b2 = b2.substring(25,50);
	         System.out.print("Binary Y: "+b2);
	         double c = Integer.parseInt(b2,2);
	         c=c*(200/(Math.pow(2,25)-1))-100;
	         System.out.println(" Real Y: "+c);
	         
	         // create dataset to plot
	         final XYSeries bestInd = new XYSeries( "Melhores Individuos da Execucao "+(k+1) );
	         int counter=0;
	         bestInd.add(counter,bestInitialInd.getFitness());
	         for(Individual ind : bestIndividuals){
	        	 counter++;
	        	 bestInd.add(counter,ind.getFitness());
	         }
	         dataset.addSeries( bestInd );
	         if(k==2){                   
		         final XYSeries bestIndMean = new XYSeries( "Media ");
		         int meanCount=0;
		         bestIndMean.add(meanCount,bestInitialInd.getFitness());
		         for(double d : bestIndividualsSum){
		        	 meanCount++;
		        	 bestIndMean.add(meanCount,d/3);
		        	 //System.out.println("Pop "+counter+": "+ind.getFitness());
		         }
		         dataset.addSeries( bestIndMean );
		         XYLineChart_AWT chart = new XYLineChart_AWT("Melhor Individuo de cada Geracao", "Fitness", dataset );
		         chart.pack( );          
		         RefineryUtilities.centerFrameOnScreen( chart );          
		         chart.setVisible( true ); 
	         }

    	 } // end for

     }

 }