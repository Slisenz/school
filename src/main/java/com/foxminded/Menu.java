package com.foxminded;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Menu {

   private final SqlConnection sqlConnection = new SqlConnection();
   private final Randomizer randomizer = new Randomizer();
   private final Scanner scanner = new Scanner(System.in);

    public void showMenu(Properties developer) throws SQLException {
        System.out.println("""
                a. Find all groups with less or equals student count

                b. Find all students related to course with given name

                c. Add new student

                d. Delete student by STUDENT_ID

                e. Add a student to the course (from a list)

                f. Remove the student from one of his or her courses""");
        String input = scanner.nextLine();
        if (input.equals("a")){
            System.out.println("please insert size group:");
            findGroups(developer ,scanner.nextInt());
        }
        if (input.equals("b")){
            System.out.println("please insert name of course");
            findStudentsByCourse(developer ,scanner.nextLine());
        }
        if (input.equals("c")){
            System.out.println("please insert first name and lat name of student");
            addNewStudent(developer ,scanner.nextLine(), scanner.nextLine());
            setCourse(developer);
        }
        if (input.equals("d")){
            System.out.println("please insert student id");
            deleteStudent(developer ,scanner.nextLine());
        }
        if (input.equals("e")){
            System.out.println("please select course");
            setCourse(developer);
        }
        if (input.equals("f")){
            System.out.println("please insert student id");
            deleteCourse(developer ,scanner.nextInt());
        }
    }

    private String findGroups(Properties developer , int input) throws SQLException {
        ResultSet resultSet =  sqlConnection.sqlTableExecute(developer ,"SELECT group_name, size  FROM \"group\" WHERE size <= " + input +";");
        outputResult(resultSet);
    return resultSet.toString();
    }

    private Boolean findStudentsByCourse(Properties developer , String input) throws SQLException {
        ResultSet resultSet =  sqlConnection.sqlTableExecute(developer ,
                "SELECT first_name , last_name " +
                        "FROM students " +
                        "WHERE student_id IN " +
                        "(SELECT student_id FROM student_courses WHERE course_id IN " +
                        "(SELECT course_id FROM courses WHERE course_name = \'" + input + "\'));");
        outputResult(resultSet);
        return true;

    }

    private Boolean addNewStudent (Properties developer ,String firstName, String lastName) throws SQLException{
        String groupName ;
        System.out.println("insert group name pleas");
        groupName = scanner.nextLine();
        ResultSet resultSet;
        if (groupName.equals("")){
            resultSet = sqlConnection.sqlTableExecute(developer ,"INSERT INTO students (first_name, last_name)  VALUES ( \'" + firstName + "\',\'" + lastName + "\') RETURNING students.first_name, students.last_name;");
        }
        else {
            resultSet = sqlConnection.sqlTableExecute(developer ,"INSERT INTO students( first_name, last_name, group_name) VALUES ( \'" + firstName +"\',\'" + lastName + "\',\'" + groupName + "\') RETURNING students.first_name, students.last_name, students.group_name;");
        }
        outputResult(resultSet);
        return true;
    }

    private Boolean deleteStudent(Properties developer ,String input) throws SQLException {
        ResultSet resultSet;
        resultSet = sqlConnection.sqlTableExecute(developer ,"DELETE FROM students WHERE student_id = \'" + input +"\' RETURNING students.first_name, students.last_name;" );
        outputResult(resultSet);
        return true;
    }
    
    private boolean setCourse(Properties developer)throws SQLException {
        System.out.println("please select course");
        for (int i = 0; i < 10; i++) {
            System.out.println( i+1 + ": " + randomizer.getCours(i));
        }
        int courseId = scanner.nextInt();
        ResultSet resultSet;
        scanner.nextLine();
        System.out.println("insert student id");
        String studentID = scanner.nextLine();
        resultSet = sqlConnection.sqlTableExecute(developer ,"INSERT INTO student_courses (student_id, course_id) VALUES (\'" + studentID + "\',\'" + courseId + "\') RETURNING student_courses.course_id;");
        System.out.println("you added course to student");
        outputResult(resultSet);
        return true;
    }

    private boolean deleteCourse(Properties developer ,int studentId) throws SQLException {
        ResultSet resultSet;
        System.out.println("Please select wanted course");
        resultSet = sqlConnection.sqlTableExecute(developer ,"SELECT course_name, course_id FROM courses WHERE course_id in (SELECT course_id FROM student_courses WHERE student_id = \'" + studentId + "\');");
        outputResult(resultSet);
        System.out.println("please select course to delete");
        int courseId = scanner.nextInt();
        ResultSet secondResult = sqlConnection.sqlTableExecute(developer ,"DELETE FROM student_courses WHERE student_id = \'"+ studentId + "\' AND course_id = \'" + courseId + "\' RETURNING student_courses.course_id , student_courses.student_id;");
        outputResult(secondResult);
        return true;
    }

    private void outputResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnsNumber = resultSetMetaData.getColumnCount();
        while(resultSet.next()){
            for (int i = 1; i <= columnsNumber ; i++) {
                String columnValue = resultSet.getString(i);
                System.out.print(resultSetMetaData.getColumnName(i) + ": " + columnValue + " ");
                if (i+1 > columnsNumber){
                    continue;
                }
                columnValue = resultSet.getString(++i);
                System.out.println(" " + resultSetMetaData.getColumnName(i) + ": " + columnValue);
            }
        }
    }
}

