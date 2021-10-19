package application;

import application.files.ObjetEndJobParser;
import application.files.ObjetLogParser;
import application.jobs.ProjectManager;

import java.io.IOException;
import java.nio.file.Paths;

public class CostTracker {

    public static void main(String[] args) {
        String filename = "data/testdata.csv";
        String logFile = "data/log.txt";
        System.out.println(Paths.get(filename));
        ProjectManager projects = new ProjectManager("data/projects.txt");

        try {
            ObjetEndJobParser finder = new ObjetEndJobParser(filename, projects);

        } catch (IOException e) {
            System.out.println(filename + "not found");
        }

        try {
            ObjetLogParser logParser = new ObjetLogParser(logFile, projects);
            System.out.println(logParser);
        } catch (IOException e) {
            System.out.println(logFile + "not found");
        }

        System.out.println(projects);

    }
}
