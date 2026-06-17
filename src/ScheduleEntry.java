/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Timestamp;
import java.util.Calendar;
/**
 *
 * @author ranji
 */
public class ScheduleEntry {
    private String semester;
    private String courseCode;
    private String studentID;
    private String status;
    private Timestamp timestamp;
    // Timestamp??
    
    public ScheduleEntry (String semester, String courseCode, String studentID, String status, Timestamp timestamp) {
        this.semester = semester;
        this.courseCode = courseCode;
        this.studentID = studentID;
        this.status = status;
        this.timestamp = timestamp;
    }
    
    public ScheduleEntry (String semester, String courseCode, String studentID) {
        this.semester = semester;
        this.courseCode = courseCode;
        this.studentID = studentID;
        //this.status = status;
        if (ScheduleQueries.getScheduledStudentCount(semester, courseCode) >= ClassQueries.getClassSeats(semester, courseCode)) {
            status = "W";
        } else {
            status = "S";
        }
        this.timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }
    
    public String getSemester () {
        return semester;
    }
    
    public String getCourseCode () {
        return courseCode;
    }
    
    public String getStatus () {
        return status; 
    }
    
    public String getStudentID () {
        return studentID;
    }
    
    public Timestamp getTimestamp () {
        return timestamp;
    }
}
