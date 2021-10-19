package application;

import application.parse.ObjetEndJobParser;
import application.parse.ObjetLogParser;
import application.jobs.ProjectManager;
import application.database.PrintJobDatabase;

import java.io.IOException;

public class CostTracker {

    public static void main(String[] args) {
        String filename = "data/testdata.csv";
        String logFile = "data/log.txt";
        String databaseName = "testdatabase.db";
        String backupProjectList = "data/projects.txt";

        //create new database connection instance
        PrintJobDatabase databaseConnection = new PrintJobDatabase(databaseName);

        //load database
        if (!databaseConnection.load()) {
            System.out.println("Database not found. Creating new database.");
            databaseConnection.createNewDatabase();
        }

        System.out.println("Database was created on " + databaseConnection.getCreationDate());
        System.out.println("Database was last updated on "+ databaseConnection.getLastUpdated());

        ProjectManager projects = new ProjectManager(databaseConnection.getProjects());

        //Read log files, and only read in data from the date that the database was last updated
        try {
            ObjetEndJobParser finder = new ObjetEndJobParser(filename, projects);
            finder.readData(databaseConnection.getLastUpdated());

        } catch (IOException e) {
            System.out.println(filename + "not found");
        }

        try {
            ObjetLogParser logParser = new ObjetLogParser(logFile, projects);
            logParser.readData(databaseConnection.getLastUpdated());
        } catch (IOException e) {
            System.out.println(logFile + "not found");
        }

        //add parsed Project and Job Data to database
        databaseConnection.addData(projects);


        //close connection to database when finished
        databaseConnection.close();

    }
}
