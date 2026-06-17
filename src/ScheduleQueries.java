/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
/**
 *
 * @author ranji
 */
public class ScheduleQueries {
    private static Connection connection;
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement updateScheduleEntry;
    private static ResultSet resultSet;
    public static void addScheduleEntry(ScheduleEntry entry) {
        connection = DBConnection.getConnection();
        try
        {
            addScheduleEntry = connection.prepareStatement("insert into SCHEDULE (SEMESTER, COURSECODE, STUDENTID, STATUS, TIMESTAMP) values (?, ?, ?, ?, ?)");
            addScheduleEntry.setString(1, entry.getSemester()); // question marks get replaced by this, indexing starting at 1
            addScheduleEntry.setString(2, entry.getCourseCode());
            addScheduleEntry.setString(3, ""+entry.getStudentID());
            addScheduleEntry.setString(4, entry.getStatus());
            addScheduleEntry.setTimestamp(5, entry.getTimestamp());
            addScheduleEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String student) {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedule = new ArrayList<>();
        try
        {
            getScheduleByStudent = connection.prepareStatement("select SEMESTER, COURSECODE, STUDENTID, STATUS, TIMESTAMP from SCHEDULE where SEMESTER = ? and STUDENTID = ?");
            getScheduleByStudent.setString(1, semester);
            getScheduleByStudent.setString(2, student);
            resultSet = getScheduleByStudent.executeQuery();
            
            while(resultSet.next())
            {
                ScheduleEntry entry = new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getTimestamp(5));
                schedule.add(entry);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return schedule;
    }
    
    public static int getScheduledStudentCount (String currentSemester, String courseCode) {
        
        // select count(studentID) from schedule where semester = ? and courseCode = ? and status = 'Scheduled'
        connection = DBConnection.getConnection();
        int count = -1;
        try
        {
            getScheduledStudentCount = connection.prepareStatement("select count(studentID) from schedule where semester = ? and courseCode = ? and status = 'S'");
            getScheduledStudentCount.setString(1, currentSemester);
            getScheduledStudentCount.setString(2, courseCode);
            resultSet = getScheduledStudentCount.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return count;
    }
    
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByClass(String semester, String courseCode) {
        ArrayList<ScheduleEntry> entries = new ArrayList<ScheduleEntry>();
        
        connection = DBConnection.getConnection();
        try {
            getWaitlistedStudentsByClass = connection.prepareStatement("select STUDENT.STUDENTID, FIRSTNAME, LASTNAME from SCHEDULE, STUDENT where SEMESTER = ? and COURSECODE = ? order by SCHEDULE.TIMESTAMP ASC");
            getWaitlistedStudentsByClass.setString(1, semester);
            getWaitlistedStudentsByClass.setString(2, courseCode);
            resultSet = getWaitlistedStudentsByClass.executeQuery();
            while (resultSet.next()) {
                ScheduleEntry entry = new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
                entries.add(entry);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return entries;
    }
    
    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode) {
        connection = DBConnection.getConnection();
        
        try {
            dropStudentScheduleByCourse = connection.prepareStatement("DELETE from SCHEDULE where SEMESTER = ? and STUDENTID = ? and COURSECODE = ?");
            dropStudentScheduleByCourse.setString(1, semester);
            dropStudentScheduleByCourse.setString(2, studentID);
            dropStudentScheduleByCourse.setString(3, courseCode);
            dropStudentScheduleByCourse.executeUpdate();
            
            ArrayList<ScheduleEntry> entries = getWaitlistedStudentsByClass(semester, courseCode);
            updateScheduleEntry(entries.get(0));
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    
    public static void dropScheduleByCourse(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        
        try {
            dropStudentScheduleByCourse = connection.prepareStatement("DELETE from SCHEDULE where SEMESTER = ? and COURSECODE = ?");
            dropStudentScheduleByCourse.setString(1, semester);
            dropStudentScheduleByCourse.setString(2, courseCode);
            dropStudentScheduleByCourse.executeUpdate();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static void updateScheduleEntry(ScheduleEntry entry) {
        connection = DBConnection.getConnection();

        try {
           updateScheduleEntry = connection.prepareStatement("update SCHEDULE set STATUS = ? where SEMESTER = ? and COURSECODE = ? and STUDENTID = ?");
           int currentStudents = getScheduledStudentCount(entry.getSemester(), entry.getCourseCode());
           int maxStudents = ClassQueries.getClassSeats(entry.getSemester(), entry.getCourseCode());
           String updatedStatus = currentStudents<maxStudents?"S":"W";
           updateScheduleEntry.setString(1, updatedStatus);
           updateScheduleEntry.setString(2, entry.getSemester());
           updateScheduleEntry.setString(3, entry.getCourseCode());
           updateScheduleEntry.setString(4, entry.getStudentID());
           updateScheduleEntry.executeUpdate();
           
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
