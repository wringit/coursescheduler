/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author ranji
 */
public class StudentQueries {
    private static Connection connection;
    private static PreparedStatement addStudents;
    private static PreparedStatement getAllStudents;
    private static PreparedStatement getStudent;
    private static PreparedStatement dropStudent;
    private static ResultSet resultSet;
    
    public static void addStudents(StudentEntry student) {
        connection = DBConnection.getConnection();
        try
        {
            addStudents = connection.prepareStatement("insert into STUDENT (STUDENTID, FIRSTNAME, LASTNAME) values (?, ?, ?)");
            addStudents.setString(1, student.getStudentID()); // question marks get replaced by this, indexing starting at 1
            addStudents.setString(2, student.getFirstName());
            addStudents.setString(3, student.getLastName());
            addStudents.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<StudentEntry> getAllStudents () {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> studentsList = new ArrayList<>();
        try
        {
            getAllStudents = connection.prepareStatement("select STUDENTID, FIRSTNAME, LASTNAME from STUDENT order by LASTNAME");
            resultSet = getAllStudents.executeQuery();
            
            while(resultSet.next())
            {
                StudentEntry student = new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
                studentsList.add(student);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return studentsList;
    }
    
    public static StudentEntry getStudent(String studentID) {
        connection = DBConnection.getConnection();
        StudentEntry student = null;
        try
        {
            getStudent = connection.prepareStatement("select STUDENTID, FIRSTNAME, LASTNAME from STUDENT where STUDENTID = ?");
            getStudent.setString(1, studentID);
            resultSet = getStudent.executeQuery();
            resultSet.next();
            student = new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return student;
    }
    
    public static void dropStudent(String studentID) {
        connection = DBConnection.getConnection();
        
        try {
//            ArrayList<SemesterEntry> semesters = SemesterQueries.getSemesterList();
//            for (SemesterEntry semester: semesters) {
//                ArrayList<ScheduleEntry> scheduleEntries = ScheduleQueries.getScheduleByStudent(studentID, semester.toString());
//                for (ScheduleEntry entry: scheduleEntries) {
//                    ScheduleQueries.dropStudentScheduleByCourse(semester.toString(), studentID, entry.getCourseCode());
//                }
//            }
            dropStudent = connection.prepareStatement("DELETE from STUDENT where STUDENTID = ?");
            dropStudent.setString(1, studentID);
            dropStudent.executeUpdate();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

}
