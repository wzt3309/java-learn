package core.clone;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CloneObjectTest {

    @Test
    public void givenObject_whenClone_thenGetANewObject() throws CloneNotSupportedException {
        Person p1 = new Person(1);
        Person p2 = (Person) p1.clone();

        assertThat(p1.getId()).isEqualTo(p2.getId());
        assertThat(p1.hashCode()).isNotEqualTo(p2.hashCode());
    }

    @Test
    public void givenObjectWithReferenceField_whenClone_thenShallowCopy() throws CloneNotSupportedException {
        Person p1 = new Person(1);
        p1.setStudent(new Student("test1"));
        Person p2 = (Person) p1.clone();

        assertThat(p1.getId()).isEqualTo(p2.getId());
        // the same student in p1 and p2
        // so it is the shallow copy
        assertThat(p1.getStudent().hashCode()).isEqualTo(p2.getStudent().hashCode());
    }

    @Test
    public void givenObjectWithReferenceField_whenClone_thenDeepCopy() throws CloneNotSupportedException {
        DeepPerson p1 = new DeepPerson(1);
        p1.setStudent(new Student("test1"));
        DeepPerson p2 = (DeepPerson) p1.clone();

        assertThat(p1.getId()).isEqualTo(p2.getId());
        assertThat(p1.getStudent().hashCode()).isNotEqualTo(p2.getStudent().hashCode());
    }

}