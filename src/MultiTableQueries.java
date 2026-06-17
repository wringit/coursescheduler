/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
/**
 *
 * @author ranji
 */
public class MultiTableQueries {
    private static Connection connection;
    private static PreparedStatement getAllClassDescriptions;
    private static PreparedStatement getScheduledStudentsByClass;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static ResultSet resultSet;
    public static ArrayList<ClassDescription> getAllClassDescriptions(String semester) {
       connection = DBConnection.getConnection();
        ArrayList<ClassDescription> descriptions = new ArrayList<>();
        try
        {
            getAllClassDescriptions = connection.prepareStatement("select CLASS.COURSECODE, DESCRIPTION, SEATS from CLASS, COURSE where SEMESTER = ? and CLASS.COURSECODE = COURSE.COURSECODE order by CLASS.COURSECODE");
            getAllClassDescriptions.setString(1, semester);
            getAllClassDescriptions.setString(2, semester);
            resultSet = getAllClassDescriptions.executeQuery();
            
            while(resultSet.next())
            {
                ClassDescription description = new ClassDescription(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(1));
                descriptions.add(description);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return descriptions;
    }
    
   public static ArrayList<StudentEntry> getScheduledStudentsByClass(String semester, String courseCode) {
       ArrayList<StudentEntry> entries = new ArrayList<StudentEntry>();
        connection = DBConnection.getConnection();
        try {
            getScheduledStudentsByClass = connection.prepareStatement("select STUDENT.STUDENTID, FIRSTNAME, LASTNAME from SCHEDULE, STUDENT where SEMESTER = ? and COURSECODE = ? and STATUS='S' and STUDENT.STUDENTID = SCHEDULE.STUDENTID");
            getScheduledStudentsByClass.setString(1, semester);
            getScheduledStudentsByClass.setString(2, courseCode);
            resultSet = getScheduledStudentsByClass.executeQuery();
            while (resultSet.next()) {
                StudentEntry entry = new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
                entries.add(entry);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return entries;
    }
    
    public static ArrayList<StudentEntry> getWaitlistedStudentsByClass(String semester, String courseCode) {
       ArrayList<StudentEntry> entries = new ArrayList<StudentEntry>();
        connection = DBConnection.getConnection();
        try {
            getWaitlistedStudentsByClass = connection.prepareStatement("select STUDENT.STUDENTID, FIRSTNAME, LASTNAME from SCHEDULE, STUDENT where SEMESTER = ? and COURSECODE = ? and STATUS='W' and STUDENT.STUDENTID = SCHEDULE.STUDENTID");
            getWaitlistedStudentsByClass.setString(1, semester);
            getWaitlistedStudentsByClass.setString(2, courseCode);
            resultSet = getWaitlistedStudentsByClass.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + "on waitlist");
                StudentEntry entry = new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
                entries.add(entry);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return entries;
    }
}
