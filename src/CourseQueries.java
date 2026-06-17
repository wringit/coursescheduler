import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ranji
 */
public class CourseQueries {
    private static ResultSet resultSet;
    private static Connection connection;
    private static PreparedStatement addCourse;
    private static PreparedStatement getDescription;
    private static PreparedStatement getAllCourseCodes;
    public static void addCourse(CourseEntry course) {
        connection = DBConnection.getConnection();
        try {
            addCourse = connection.prepareStatement("insert into COURSE (COURSECODE, DESCRIPTION) values (?, ?)");
            addCourse.setString(1, course.getCourseCode());
            addCourse.setString(2, course.getDescription());
            addCourse.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
    
    public static ArrayList<String> getAllCourseCodes() {
        connection = DBConnection.getConnection();
        ArrayList<String> codesList = new ArrayList<String>();
        try {
            getAllCourseCodes = connection.prepareStatement("select COURSECODE from COURSE order by COURSECODE");
            resultSet = getAllCourseCodes.executeQuery();
            while (resultSet.next()) {
                String code = new String(resultSet.getString(1));
                System.out.println(code);
                codesList.add(code);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return codesList;
    }
    public static String getDescription(String courseCode) {
        connection = DBConnection.getConnection();
        String description = "";
        try {
            getDescription = connection.prepareStatement("select DESCRIPTION from COURSE where COURSECODE = ?");
            getDescription.setString(1, courseCode);
            resultSet = getDescription.executeQuery();
            resultSet.next();
            description = resultSet.getString(1);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return description;
    }
}
