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
import java.util.Calendar;
/**
 *
 * @author ranji
 */
public class ClassQueries {
    private static Connection connection;
    private static PreparedStatement addClass;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getAllClasses;
    private static PreparedStatement getClassSeats;
    private static PreparedStatement dropClass;
    private static ResultSet resultSet;
    public static void addClass(ClassEntry classEntry) {
        connection = DBConnection.getConnection();
        try
        {
            addClass = connection.prepareStatement("insert into CLASS (SEMESTER, COURSECODE, SEATS) values (?, ?, ?)");
            addClass.setString(1, classEntry.getSemester()); // question marks get replaced by this, indexing starting at 1
            addClass.setString(2, classEntry.getCourseCode());
            addClass.setInt(3, classEntry.getSeats());
            addClass.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<String> getAllCourseCodes(String semester) {
        connection = DBConnection.getConnection();
        ArrayList<String> codesList = new ArrayList<String>();
        try
        {
            getAllCourseCodes = connection.prepareStatement("select COURSECODE from CLASS where SEMESTER = ? order by COURSECODE");
            getAllCourseCodes.setString(1,semester);
            resultSet = getAllCourseCodes.executeQuery();
            while(resultSet.next())
            {
                String code = new String(resultSet.getString(1));
                codesList.add(code);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return codesList;
    }
    
    
    public static int getClassSeats(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        int seats = -1;
        try {
           getClassSeats = connection.prepareStatement("select SEATS from CLASS where SEMESTER = ? and COURSECODE = ?");
           getClassSeats.setString(1,semester);
           getClassSeats.setString(2,courseCode);
           resultSet = getClassSeats.executeQuery();
           resultSet.next();
           seats = resultSet.getInt(1);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return seats;
    }
    
    public static void dropClass(String Semester, String courseCode) {
        connection = DBConnection.getConnection();
        try {
            dropClass = connection.prepareStatement("DELETE from CLASS where SEMESTER = ? and COURSECODE = ?");
            dropClass.setString(1, Semester);
            dropClass.setString(2, courseCode);
            dropClass.executeUpdate();
            dropClass = connection.prepareStatement("DELETE from SCHEDULE where SEMESTER = ? and COURSECODE = ?");
            dropClass.setString(1, Semester);
            dropClass.setString(2, courseCode);
            dropClass.executeUpdate();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
