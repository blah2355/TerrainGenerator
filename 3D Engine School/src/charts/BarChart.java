package charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 

public class BarChart extends ApplicationFrame{
	
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static double[] runtimes;
	private static long[] operations;
	
	public BarChart( String applicationTitle , String chartTitle ) {
	      super( applicationTitle );        
	      JFreeChart barChart = ChartFactory.createBarChart(
	         chartTitle,           
	         "Algorithm",            
	         "Score",            
	         createDataset(),          
	         PlotOrientation.VERTICAL,           
	         true, true, false);
	         
	      ChartPanel chartPanel = new ChartPanel( barChart );        
	      chartPanel.setPreferredSize(new java.awt.Dimension( 1280 , 720 ) );        
	      setContentPane( chartPanel ); 
	      
	   }
	   
	   private CategoryDataset createDataset( ) {
	      final String fractal = "FRACTAL";        
	      final String perlin = "PERLIN";        
	      final String value = "VALUE"; 
	      final String simplex = "SIMPLEX";
	      final String random = "RANDOM NOISE";
	      final String time = "Runtime";        
	      final String ops = "Operations";             
	      final DefaultCategoryDataset dataset = 
	      new DefaultCategoryDataset( );  

	      dataset.addValue( runtimes[0] , value , time );        
	      dataset.addValue( operations[0] , value , ops );        
	      
	      dataset.addValue( runtimes[1] , fractal , time );        
	      dataset.addValue( operations[1] , fractal , ops );
	     
	      dataset.addValue( runtimes[2] , perlin , time );        
	      dataset.addValue( operations[2] , perlin , ops ); 
	      
	      dataset.addValue(runtimes[3], simplex, time);
	      dataset.addValue(operations[3], simplex, ops);

	      dataset.addValue(runtimes[4], random, time);
	      dataset.addValue(operations[4], random, ops);

	      return dataset; 
	   }
	   
	   public static void createChart(double[] rts, long[] ops) {
		  runtimes = rts;
		  operations = ops;
		  
	      BarChart chart = new BarChart("RunTime Data", 
	         "Runtime and Operations Performed");
	      chart.pack( );        
	      RefineryUtilities.centerFrameOnScreen( chart );        
	      chart.setVisible( true ); 
	   }
	}
