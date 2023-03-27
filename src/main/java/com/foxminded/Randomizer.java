package com.foxminded;

import java.util.*;

public class Randomizer {

    String[] firstName = {"Michael" ,"James" ,"Matthew" ,"Nicholas " ,"Christopher" ,"Joseph" ,"Zachary" ,"Joshua" ,"Andrew" ,"William" ,
            "Emily Hannah" ,"Kaitlyn" ,"Sarah" ,"Madison" ,"Brianna" ,"Kaylee" ,"Hailey" ,"Alexis", "Elizabeth"};
    String[] secondName = {"Smith" ,"Johnson" ,"Williams" ,"Brown" ,"Jones" ,"Garcia" ,"Miller" ,"Davis" ,"Rodriguez" ,"Martinez" ,"Hernandez" ,
            "Lopez" ,"Gonzalez" ,"Wilson" ,"Anderson" ,"Thomas" ,"Taylor" ,"Moore" ,"Jackson" ,"Martin"};

    Random random = new Random();

    public String randomGroupName(){
        int length = 2;
        String randomChar = random.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        String result = randomChar + "-" + random.nextInt(90);
        return result;
    }

    public String randomFirstName(){
        return firstName[random.nextInt(19)];
    }

    public String randomLastName(){
        return secondName[random.nextInt(19)];
    }

    public int randomGroupSize(boolean zeroRange, int minRange, int maxRange){
       int result = random.nextInt((maxRange + 1) - minRange) + minRange;
        if (result+ 15 > maxRange+minRange){
            return 0;
        }
        return result;
    }

    public int randomGroupSize (int minRange, int maxRange){
        return random.nextInt(maxRange - minRange) + minRange;
    }

    public String getCours (int iterator){
        String[] courses = {"mathematics", "biology", "geography", "chemistry", "art", "music", "physics", "geometry", "history", "technology"};
        return courses[iterator];
    }

    public int randomInt(int max){
        return random.nextInt(max) + 1;
    }
}
