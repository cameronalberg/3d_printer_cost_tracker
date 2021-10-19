package application.jobs;

import java.util.ArrayList;

public class Project {
    private String name;
    private boolean active = true;
    private JobList jobs;

    public Project(String name) {
        this.name = name;
        this.jobs = new JobList();
        this.active = true;
    }

    public String getName() {
        return this.name;
    }

    public void addJobToProject(PrintJob job) {
        jobs.add(job);
    }

    public PrintJob findJob(PrintJob job) {
        return jobs.findJob(job);
    }

    public void removeJob(PrintJob job) {
        jobs.remove(job);
    }

    public PrintJob findJobByName(String jobID) {
        return jobs.findJobByName(jobID);
    }

    public int getJobCount() {
        return jobs.jobCount();
    }

    public String getJobs() {
        return jobs.toString();
    }
}
