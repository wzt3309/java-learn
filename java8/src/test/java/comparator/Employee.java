package comparator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Employee implements Comparable<Employee> {
    String name;
    int age;
    double salary;
    long mobile;

    @Override
    public int compareTo(Employee o) {
        if (o == null) {
            return 1;
        }
        return name.compareTo(o.name);
    }

    // cannot be the key for Comparable
    public Map getNotComparable() {
        return null;
    }
}
