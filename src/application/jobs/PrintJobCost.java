package application.jobs;

import java.time.Duration;
import java.util.HashMap;
public class PrintJobCost {
    //set unchanging variables
    private static final double TAX_RATE = 10.25;
    private static final double MODEL_COST_PER_GRAM = 0.33075;
    private static final double SUPPORT_COST_PER_GRAM = 0.1378125;
    private static final int YEARLY_MAINTENANCE_COST = 3000;

    //set static map for hourly rates of each year
    private static final HashMap<Integer, Double> hourlyRates = new HashMap<>() {{
        put(2018, 6.0);
        put(2019, 5.0);
        put(2020, 3.0);
        put(2021, 3.0);
    }};

    //each instance is given the year of the job that is being evaluated
    private int year;

    public PrintJobCost(int currentYear){
        if (currentYear > 0) {
            this.year = currentYear;
        } else {
            throw new IllegalArgumentException("invalid year");
        }
    }

    public int getYear() {
        return this.year;
    }

    public double getHourlyRate() {
        //set hourlyRate to default value of 3, unless year is found in map
        double hourlyRate = 3;
        if (hourlyRates.containsKey(year)) {
            hourlyRate = hourlyRates.get(year);
        }
        return hourlyRate;
    }

    public double getJobCost(double modelConsumption, double supportConsumption, Duration buildDuration){
        if (modelConsumption < 0 || supportConsumption < 0 || buildDuration.isNegative()) {
            throw new IllegalArgumentException("all parameters must be positive");
        }
        //calculate total cost using material cost, build time, and hourly rate (dependent on year)
        double materialCost = modelConsumption * MODEL_COST_PER_GRAM + supportConsumption * SUPPORT_COST_PER_GRAM;
        double durationInHours = (double) buildDuration.toMinutes() / 60.0;
        double buildCost = this.getHourlyRate() * durationInHours;

        double totalCost = materialCost + buildCost;

        return Math.round(totalCost * 100.0) / 100.0;
    }



}
