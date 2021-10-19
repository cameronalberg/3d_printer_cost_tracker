package application.jobs;

import java.util.List;

public class Project {
    private final String name;
    private final JobList jobs;

    public Project(String name) {
        this.name = name;
        this.jobs = new JobList();
    }

    public String getName() {
        return this.name;
    }

    public void addJobToProject(PrintJob job) {
        jobs.add(job);
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

    public List<PrintJob> getJobs() {
        return jobs.getJobs();
    }
}
