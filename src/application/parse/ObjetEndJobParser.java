package application.parse;


import application.jobs.PrintJob;
import application.jobs.ProjectManager;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

//Class for building PrintJob instances from ObjectEndJobDataCollection.csv file
public class ObjetEndJobParser {
    private Scanner scanner;
    private ProjectManager projects;

    // initialize instance by creating Scanner to read file, and then read data;
    public ObjetEndJobParser(String fileName, ProjectManager projects) throws IOException {
        scanner = new Scanner(Paths.get(fileName));
        this.projects = projects;
    }

    private void moveToDate(LocalDate readFromDate) {
        if (readFromDate == null) {
            return;
        }
        readFromDate = readFromDate.minusDays(4); //subtract several days to ensure nothing is skipped
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            try {
                LocalDate date = DateTimeParser.getDateFromString(row.split(",")[3]);
                if (date.isAfter(readFromDate)) {
                    break;
                }
            } catch (Exception ignored) {

            }

        }
    }

    // read all rows in file
    public void readData(LocalDate readFromDate) {

        moveToDate(readFromDate);

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            //split rows on commas
            String[] row = line.split(",");


            try {
                //try to read first column as valid integer
                int jobID = Integer.parseInt(row[0]);

                //if retrieved value is greater than 0 and number of columns in row is 44, a valid entry exists
                if (jobID > 0 && row.length == 44) {

                    //read all job info at row
                    PrintJob job = getJobInfo(row);

                    //Debugging step
//                    if (job != null) {
//                        System.out.println(job);
//                    }
            }
            }   catch (NumberFormatException ignored) {

            }



        }
    }

    //Method for reading rows from ObjectEndJobDataCollection.csv file
    private PrintJob getJobInfo(String[] entry) {
        String jobID = entry[0];
        String trayID = entry[2];
        LocalDateTime startTime = DateTimeParser.getDateTimeFromString(entry[3], entry[4]);
        double modelConsumption = Double.parseDouble(entry[22]) + Double.parseDouble(entry[23]) + Double.parseDouble(entry[24]);
        double supportConsumption = Double.parseDouble(entry[25]);
        int buildTimeInSeconds = Integer.parseInt(entry[13]) + Integer.parseInt(entry[14]) + Integer.parseInt(entry[15]);

        if (buildTimeInSeconds < 600) {
            return null;
        }
        Duration buildDuration = Duration.ofSeconds(buildTimeInSeconds);
        PrintJob job = new PrintJob(jobID, trayID, modelConsumption, supportConsumption, buildDuration, startTime);
        projects.addJobWithoutProject(job);
        return job;
    }

}
