package com.foxminded;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


public class DataGeneration {

    final SqlConnection sqlConnection = new SqlConnection();
    final Randomizer randomizer = new Randomizer();
    static final int NUMBER_OF_STUDENTS = 200;

    public void generateData() throws SQLException, ClassNotFoundException, IOException {
        generateDatabase();
        generateTable();
        fillTable();
    }

    private void generateDatabase() throws SQLException, IOException, ClassNotFoundException {
        Properties user = sqlConnection.getProperties("postgres");
        sqlConnection.sqlRequestByFile(user, "createDB");
    }

    private void generateTable() throws SQLException, IOException, ClassNotFoundException {
        Properties user = sqlConnection.getProperties("developer");
        sqlConnection.sqlRequestByFile(user, "CreateTable");
    }

    private void fillTable() throws SQLException {
        int maxGroupsNumber = 10;
        for (int i = 0; i < NUMBER_OF_STUDENTS; i++) {
            int groupSize = randomizer.randomGroupSize(true, 10, 30);
            if (maxGroupsNumber >=0){
                String groupName = randomizer.randomGroupName();
                sqlConnection.sqlExecute("INSERT INTO \"group\" (group_name, size)" + " VALUES (\'" + groupName + "\'" + ", " + "\'"+ groupSize + "\');");
                for (int j = 0; j < groupSize ; j++) {
                    sqlConnection.sqlExecute(" INSERT INTO students (first_name, last_name, group_name) VALUES (\'" + randomizer.randomFirstName() +
                    "\' , \'" + randomizer.randomLastName() + "\', \'" + groupName + "\');");
                }
                i += groupSize - 1;
                maxGroupsNumber--;
            }
            else {
                sqlConnection.sqlExecute("INSERT INTO students (first_name, last_name) VALUES (\'" + randomizer.randomFirstName() +
                        "\' , \'" + randomizer.randomLastName() + "\');");
            }
        }
        for (int i = 0; i <10 ; i++) {
            sqlConnection.sqlExecute("INSERT INTO courses ( course_name) VALUES(\'" + randomizer.getCours(i) + "\');");
        }
        for (int i = 1; i <= NUMBER_OF_STUDENTS; i++) {
            Set<String> courses = new HashSet<>();
            for (int j = randomizer.randomInt(3); courses.size() < j;) {
                String course = randomizer.getCours(randomizer.randomInt(9));
                courses.add(course);
                sqlConnection.sqlExecute("INSERT INTO student_courses ( student_id, course_id) VALUES (\'" + i + "\' ,\'" + randomizer.randomInt(9) + "\');");
            }
        }
    }
}
