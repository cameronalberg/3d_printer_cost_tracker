package application.jobs;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ProjectManager {
    //map of project names (strings) and their associated project
    private HashMap<String, Project> projects;
    private Project emptyProject;

    public ProjectManager() {
        this.projects = new HashMap<>();
        this.emptyProject = new Project("None");
        this.projects.put("None", this.emptyProject);
    }
    public ProjectManager(String fileName) {

        List<String> projectList = readProjectsFromFile(fileName);
        addMultiple(projectList);
    }

    public ProjectManager(List<String> projectList) {
        this();
        if (!projectList.isEmpty()) {
            addMultiple(projectList);
        }
    }

    private void addMultiple(List<String> projectList) {
        for (String name : projectList) {
            add(new Project(name));
        }
    }

    private List<String> readProjectsFromFile(String fileName) {
        List<String> names = new ArrayList<>();
        try {
            Files.lines(Paths.get(fileName)).forEach(line -> {
               String name = line.trim();
               names.add(name);

            });
        } catch (Exception e) {
            return names;
        }
        return names;

    }

    public void add(Project project) {
        this.projects.putIfAbsent(project.getName(), project);
    }

    public boolean projectExists(Project project) {
        return (this.projects.getOrDefault(project.getName(), null) != null);
    }

    public Set<String> getProjectNames() {
        return this.projects.keySet();
    }

    public Project getProjectByName(String projectName) {
        return this.projects.getOrDefault(projectName, null);
    }

    public String searchStringForProjectName(String string) {
        for (String existingProject : this.projects.keySet()) {
            if (string.contains(existingProject)) {
                return existingProject;
            }
        }
        return null;
    }

    public void addJobToProject(String projectName, PrintJob job) {
        // check to see if project exists
        Project project = projects.getOrDefault(projectName, null);

        //if project does not exist, add to empty project category
        if(project == null) {
            addJobWithoutProject(job);
        } else {
            project.addJobToProject(job);
            emptyProject.removeJob(job);
        }
    }

    public void addJobWithoutProject(PrintJob job) {
        emptyProject.addJobToProject(job);
    }

    public PrintJob findExistingJobByNameWithoutProject(String jobID) {
        return emptyProject.findJobByName(jobID);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Projects:\n");
        for (String name : this.projects.keySet()) {
            if (!name.equals("None")) {
                output.append(name).append(": ").append(this.projects.get(name).getJobCount()).append(" linked jobs\n");
            }
        }
        output.append("Unlinked Jobs: ").append(emptyProject.getJobCount());
        output.append(emptyProject.getJobs());
        return output.toString();
    }
}
