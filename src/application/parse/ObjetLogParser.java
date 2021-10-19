package application.parse;

//method for reading objet log file to add additional data to existing PrintJobs (username, project)

import application.jobs.PrintJob;
import application.jobs.ProjectManager;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

public class ObjetLogParser {
    private Scanner scanner;
    private ProjectManager projects;
    private Set<String> projectNames;


    // initialize instance by creating Scanner to read file
    public ObjetLogParser(String fileName, ProjectManager projects) throws IOException {
        this.projectNames = new HashSet<>();
        this.projects = projects;
        this.scanner = new Scanner(Paths.get(fileName));
    }


    private void moveToDate(LocalDate readFromDate) {
        if (readFromDate == null) {
            return;
        }
        readFromDate = readFromDate.minusDays(4);

        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            if (!row.equals("")) {
                String[] parts = row.split(" ");
                try {
                    LocalDate date = DateTimeParser.getDateFromString(parts[0]);
                    if (date.isAfter(readFromDate)) {
                        break;
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void readData(LocalDate readFromDate) {

        moveToDate(readFromDate);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.contains("following tray is going to be packed")) {
                continue;
            }
            addInfoToExistingJob();

        }
    }

    private void addInfoToExistingJob() {
        //get relevant rows from log file
        scanner.nextLine();
        scanner.nextLine();
        String pathRow = scanner.nextLine();
        scanner.nextLine();
        String userAndJobRow = scanner.nextLine();

        //search pathRow for project name
        String projectName = projects.searchStringForProjectName(pathRow);

        try {

            //get job file name from end of path string
            String[] pathParts = pathRow.split("\\\\");
            String jobName = pathParts[pathParts.length - 1];

            //strip file extension from name
            jobName = jobName.split(".objtf")[0];
            addProjectNameGuess(pathParts[3].split(" ")[0]);

            //get jobID and username from project row
            String[] userAndJobParts = userAndJobRow.split("ID# ")[1].split(" ");
            String jobID = userAndJobParts[0];
            String user = userAndJobParts[3].split(",")[0];
            PrintJob job = projects.findExistingJobByNameWithoutProject(jobID);
            if (job != null) {
                projects.addJobToProject(projectName, job);
                job.updateFromLog(jobID, user, projectName, jobName);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {

        }

    }

    public void addProjectNameGuess(String guess) {
        projectNames.add(guess);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Project Name Guesses:\n");
        for (String name : projectNames) {

                output.append(name).append("\n");
            }
        output.append("---------");
        return output.toString();
    }

}
