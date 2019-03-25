package core.clone;

public class DeepPerson extends Person {

    public DeepPerson(int id) {
        super(id);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        DeepPerson d = (DeepPerson) super.clone();
        d.setStudent((Student) d.getStudent().clone());
        return d;
    }
}
