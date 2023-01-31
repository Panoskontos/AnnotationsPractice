package org.example;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {


    @SuppressWarnings("unused")
    Cat mycat = new Cat("stella");

    if(mycat.getClass().isAnnotationPresent(ClassChanger.class)){
        System.out.println("This class will change");
    } else {
        System.out.println("THis class wont change");
    }

    for (Method method : mycat.getClass().getDeclaredMethods()){
        if(method.isAnnotationPresent(VeryImportant.class)){
            VeryImportant veryImportant = method.getAnnotation(VeryImportant.class);
            for(int i=0; i<veryImportant.times();i++){
                method.invoke(mycat);
                System.out.println(veryImportant.language());
            }

        }
    }



    }
}