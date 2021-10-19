package application.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobList {
    private HashMap<String, PrintJob> jobs;

    public JobList() {
        this.jobs = new HashMap<>();
    }

    public void add(PrintJob job) {
        jobs.putIfAbsent(job.getJobID(), job);
    }

    public void remove(PrintJob job) {
        jobs.remove(job.getJobID());
    }

    public PrintJob findJob(PrintJob job) {
        return jobs.getOrDefault(job.getJobID(), null);
    }

    public PrintJob findJobByName(String jobID) {
        return jobs.getOrDefault(jobID, null);
    }

    public int jobCount() {
        return jobs.size();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (PrintJob job : jobs.values()) {
            output.append(job).append("\n\n");
        }
        return output.toString();
    }

    public List<PrintJob> getJobs() {
        return new ArrayList<>(jobs.values());
    }
}
