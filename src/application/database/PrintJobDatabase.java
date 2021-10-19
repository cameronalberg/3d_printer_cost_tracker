package application.database;

import application.jobs.PrintJob;
import application.jobs.Project;
import application.jobs.ProjectManager;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class PrintJobDatabase {
    static final String path = "data/";
    private Connection database;
    private LocalDate creationDate;
    private LocalDate lastUpdated;
    private final String fileName;

    public PrintJobDatabase(String fileName) {
        this.fileName = fileName;
        this.lastUpdated = null;

    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    private void getDatabaseDateInfo() throws SQLException {
        Statement statement = database.createStatement();
        String getCreationDate = "SELECT dateCreated FROM dates";
        ResultSet result = statement.executeQuery(getCreationDate);
        while (result.next()) {
            this.creationDate = result.getDate("dateCreated").toLocalDate();
        }

        String getLastUpdated = "SELECT dateUpdated FROM dates ORDER BY dateCreated DESC";
        result = statement.executeQuery(getLastUpdated);
        while (result.next()) {
            this.lastUpdated = result.getDate("dateUpdated").toLocalDate();
        }

    }

    public boolean load() {
        boolean connected = false;
        if (databaseExists(fileName)) {
            connect();
            connected = true;
        }
        return connected;
    }

    private static boolean databaseExists(String fileName) {
        File file = new File(addPath(fileName));
        return file.exists();
    }

    public boolean createNewDatabase() {
        String filePath = addPath(fileName);
        if (databaseExists(filePath)) {
            System.out.println("Database already exists! Skipping.");
            return false;
        }

        String url = "jdbc:sqlite:" + filePath;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement statement = conn.createStatement();
                String addProjectTable = "CREATE TABLE IF NOT EXISTS projects (\n"
                        + "             name text PRIMARY KEY)";

                String addJobTable = "CREATE TABLE IF NOT EXISTS jobs (\n"
                        + "             jobID BIGINT PRIMARY KEY, \n"
                        + "             trayID BIGINT, \n"
                        + "             jobName text, \n"
                        + "             projectName text, \n"
                        + "             userName text, \n"
                        + "             modelConsumption DOUBLE, \n"
                        + "             supportConsumption DOUBLE, \n"
                        + "             date DATE, \n"
                        + "             duration TIME)";

                String addDateInfo = "CREATE TABLE IF NOT EXISTS dates (\n"
                        + "             dateCreated DATE NOT NULL,\n"
                        + "             dateUpdated DATE)";

                statement.execute(addProjectTable);
                statement.execute(addJobTable);
                statement.execute(addDateInfo);

                java.util.Date utilDate = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                String addDates = "INSERT INTO dates(dateCreated, dateUpdated) VALUES(?,?)";
                PreparedStatement pstmt = conn.prepareStatement(addDates);
                pstmt.setDate(1, sqlDate);
                pstmt.setDate(2, null);
                pstmt.executeUpdate();
                System.out.println("A new database " + fileName + " has been created.");

                this.lastUpdated = null;
                this.creationDate = sqlDate.toLocalDate();

                connect();
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static String addPath(String fileName) {
        if (fileName.contains(path)) {
            return fileName;
        }
        return path + fileName;
    }

    private Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + addPath(fileName);
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            this.database = conn;
            getDatabaseDateInfo();
            System.out.println("Connection to database " + fileName +" has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean close()  {
        try {
            this.database.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public void addData(ProjectManager projects) {
        for (String projectName : projects.getProjectNames()) {
            String sql;

            try {
                sql = "INSERT INTO projects (name) VALUES (?)";
                PreparedStatement pstmt = database.prepareStatement(sql);
                pstmt.setString(1, projectName);
                pstmt.executeUpdate();
            } catch (SQLException e) {
//                System.out.println(e.getMessage());
            }
            Project currProject = projects.getProjectByName(projectName);
            for (PrintJob job : currProject.getJobs()) {
                try {
                    sql = "INSERT INTO jobs (jobID, trayID, jobName, projectName, userName, " +
                            "modelConsumption, supportConsumption, date, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = database.prepareStatement(sql);

                    java.sql.Date sqlDate;

                    if (job.getDate() != null) {
                        sqlDate = java.sql.Date.valueOf(job.getDate().toLocalDate());
                    } else {
                        sqlDate = null;
                    }

                    java.sql.Time sqlTime = new java.sql.Time(job.getBuildTime().toMillis());

                    pstmt.setInt(1, Integer.parseInt(job.getJobID()));
                    pstmt.setInt(2, Integer.parseInt(job.getTrayID()));
                    pstmt.setString(3, job.getJobName());
                    pstmt.setString(4, job.getProjectName());
                    pstmt.setString(5, job.getUserName());
                    pstmt.setDouble(6, job.getModelConsumption());
                    pstmt.setDouble(7, job.getSupportConsumption());
                    pstmt.setDate(8, sqlDate);
                    pstmt.setTime(9, sqlTime);
                    pstmt.executeUpdate();

                }catch (SQLException e) {
//                    System.out.println(e.getMessage());
                }
            }

        }
    }
}