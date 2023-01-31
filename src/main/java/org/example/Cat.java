package org.example;

@ClassChanger
public class Cat {
    private String name;

    public Cat(String name) {
        this.name = name;
        System.out.println("Cat creted");
    }

    public String getName() {
        return name;
    }

    @VeryImportant(times = 3, language = "firebase")
    public void meow(){
        System.out.println("Meeow");
    }


    @VeryImportant(times = 1, language = "h2")
    public void quaks(){
        System.out.println("quaks");
    }
    public void setName(String name) {
        this.name = name;
    }
}