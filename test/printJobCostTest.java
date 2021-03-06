import application.jobs.PrintJobCost;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class printJobCostTest {

    @Test
    public void initializeToCurrentYear() throws Exception {
        PrintJobCost cost = new PrintJobCost(2021);
        assertEquals(2021, cost.getYear());
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructorRejectsZeroYear() throws Exception {
        PrintJobCost cost = new PrintJobCost(0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructorRejectsNegativeYear() throws Exception {
        PrintJobCost cost = new PrintJobCost(-2021);
    }

    @Test
    public void initializeToFutureYear() throws Exception {
        PrintJobCost cost = new PrintJobCost(3021);
        assertEquals(3021, cost.getYear());
    }

    @Test
    public void hourlyRateDefaultsToThree() throws Exception {
        PrintJobCost cost = new PrintJobCost(3021);
        assertEquals(3, cost.getHourlyRate(), 0.001);
    }

    @Test
    public void hourlyRateMatchesMaps() throws Exception {
        PrintJobCost cost = new PrintJobCost(2019);
        assertEquals(5, cost.getHourlyRate(), 0.001);
    }

    @Test (expected = IllegalArgumentException.class)
    public void jobCostRejectsNegativeModelValues() throws Exception{
        PrintJobCost cost = new PrintJobCost(2019);
        Duration buildDuration = Duration.ofMinutes(30);
        cost.getJobCost(-1, 20, buildDuration);
    }

    @Test (expected = IllegalArgumentException.class)
    public void jobCostRejectsNegativeSupportValues() throws Exception{
        PrintJobCost cost = new PrintJobCost(2019);
        Duration buildDuration = Duration.ofMinutes(30);
        cost.getJobCost(20, -1, buildDuration);
    }

    @Test (expected = IllegalArgumentException.class)
    public void jobCostRejectsNegativeDurationValues() throws Exception{
        PrintJobCost cost = new PrintJobCost(2019);
        Duration buildDuration = Duration.ofMinutes(-30);
        cost.getJobCost(20, 20, buildDuration);
    }

    @Test (expected = IllegalArgumentException.class)
    public void jobCostRejectsAllNegativeValues() throws Exception{
        PrintJobCost cost = new PrintJobCost(2019);
        Duration buildDuration = Duration.ofMinutes(-30);
        cost.getJobCost(-20, -20, buildDuration);
    }

    @Test
    public void jobCostReturnsZeroWhenGivenZeroValues() throws Exception{
        PrintJobCost cost = new PrintJobCost(2019);
        Duration buildDuration = Duration.ofSeconds(0);
        assertEquals(0, cost.getJobCost(0, 0, buildDuration), 0.001);
    }

    @Test
    public void jobCostCalculatesCorrectValues() throws Exception{
        PrintJobCost cost = new PrintJobCost(2019);
        Duration buildDuration = Duration.ofSeconds(0);
        assertEquals(.47, cost.getJobCost(1, 1, buildDuration), 0.01);
    }

    @Test
    public void jobCostCalculatesCorrectValues2() throws Exception{
        PrintJobCost cost = new PrintJobCost(2020);
        Duration buildDuration = Duration.ofMinutes(15);
        assertEquals(1.22, cost.getJobCost(1, 1, buildDuration), 0.01);
    }

    @Test
    public void jobCostCalculatesCorrectValues3() throws Exception{
        PrintJobCost cost = new PrintJobCost(2019);
        Duration buildDuration = Duration.ofMinutes(315);
        assertEquals(952.35, cost.getJobCost(2500, 720, buildDuration), 0.01);
    }

    @Test
    public void jobCostCalculatesCorrectValues4() throws Exception{
        PrintJobCost cost = new PrintJobCost(2020);
        Duration buildDuration = Duration.ofMinutes(315);
        assertEquals(941.85, cost.getJobCost(2500, 720, buildDuration), 0.01);
    }


}