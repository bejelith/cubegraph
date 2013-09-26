package it.telecom.forecast.holtwinters;

import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Observation;
import net.sourceforge.openforecast.models.DoubleExponentialSmoothingModel;

public class DoubleExponentialSmoothingTest
{
    /**
     * The amount of error in the forecast values where the forecast is
     * considered "correct" by the test.
     */
    private static double TOLERANCE = 0.005;
    
    /**
     * The amount of error/tolerance in the Mean Squared Error where the
     * forecast is still considered "correct" by the test.
     */
    private static double MSE_TOLERANCE = 0.1;
    
    private double alpha = 0.3;
    private double gamma = 0.3;
    
    public DoubleExponentialSmoothingTest( String name, double alpha, double gamma){
    	this.alpha = alpha;
    	this.gamma = gamma;
    }

    
    /**
     * A somewhat more realistic test where the results are known (and were
     * calculated independently of the model). Validates that the
     * DoubleExponentialSmoothingModel returns the expected set of results
     * for the given inputs.
     */
    public DataSet testDoubleExponentialSmoothing(double[] observations)
    {        
        DataSet observedData = new DataSet();
        DataPoint dp;
        
      
        
        for ( int t=0; t < observations.length; t++ ){
            dp = new Observation( observations[t] );
            dp.setIndependentValue( "t",  t+1 );
            observedData.add( dp );
        }
        
        ForecastingModel model
            = 
  //          new DoubleExponentialSmoothingModel( 0.3623, 1.0 );
        new DoubleExponentialSmoothingModel( alpha, gamma );
        
        // Initialize the model
        model.init( observedData );
        
       /* assertEquals( "Checking the accuracy of the model",
                      3.36563849, model.getMSE(), MSE_TOLERANCE );*/
        
        // Create a data set for forecasting
        DataSet fcValues = new DataSet();
        
        for ( int t=1; t<= observations.length + 1; t ++ )
        {
            dp = new Observation( 0.0 );
            dp.setIndependentValue( "t", t );
            fcValues.add( dp );
        }
    
        // Get forecast values
        DataSet results = model.forecast( fcValues );

        return results;
    }
}
// Local Variables:
// tab-width: 4
// End:
