package application.jobs;

import java.time.Duration;
import java.time.LocalDateTime;

public class PrintJob {
    private final String jobID;
    private final String trayID;
    private double modelConsumption;
    private double supportConsumption;
    private Duration buildDuration;
    private String jobName;
    private String userName;
    private String projectName;
    private LocalDateTime startTime;

    public PrintJob(String jobID, String trayID, double modelConsumption,
                    double supportConsumption, Duration buildDuration, LocalDateTime startTime) {
        this.jobID = jobID;
        this.trayID = trayID;
        this.modelConsumption = modelConsumption;
        this.supportConsumption = supportConsumption;
        this.buildDuration = buildDuration;
        this.startTime = startTime;
    }

    public double TotalCost(){
        int year = 2019; //placeholder
        PrintJobCost cost = new PrintJobCost(year);
        return cost.getJobCost(modelConsumption, supportConsumption, buildDuration);
    }

    @Override
    public String toString() {
        return "Job ID: " + jobID + "\n" + "Tray ID: " + trayID + "\n" + "Job Name: " + jobName + "\n"
                + "User: " + userName + "\n" + "Project: " + projectName + "\n"
                + "Start Time: " + startTime + "\n" + "Model (g): " + modelConsumption + "\n"
                + "Support (g): " + supportConsumption + "\n" + "Duration (min): " + buildDuration.toMinutes();
    }

    public String getJobID() {
        return jobID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void updateFromLog(String jobID, String userName, String projectName, String jobName) {

        this.setUserName(userName);
        this.setProjectName(projectName);
        this.setJobName(jobName);

    }

    public String getTrayID() {
        return this.trayID;
    }

    public String getJobName() {
        return this.jobName;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public double getModelConsumption() {
        return this.modelConsumption;
    }

    public double getSupportConsumption() {
        return this.supportConsumption;
    }

    public LocalDateTime getDate() {
        return this.startTime;
    }

    public Duration getBuildTime() {
        return this.buildDuration;
    }


}
