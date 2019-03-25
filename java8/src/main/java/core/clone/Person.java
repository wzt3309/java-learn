package core.clone;

import java.util.Objects;

// must implement Cloneable or throws CloneNotSupportedException
public class Person implements Cloneable {
    private int id;
    private Student student;

    public Person(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    /* if want to clone in other package it should be public see "protected visibility" */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
