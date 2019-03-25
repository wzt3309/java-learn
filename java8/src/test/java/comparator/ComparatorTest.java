package comparator;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

public class ComparatorTest {
    private Employee[] employees;
    private Employee[] employeesArrayWithNulls;
    private Employee[] sortedEmployeesByName;
    private Employee[] sortedEmployeesByNameDesc;
    private Employee[] sortedEmployeesByAge;
    private Employee[] sortedEmployeesByMobile;
    private Employee[] sortedEmployeesBySalary;
    private Employee[] sortedEmployeesArray_WithNullsFirst;
    private Employee[] sortedEmployeesArray_WithNullsLast;
    private Employee[] sortedEmployeesByNameAge;
    private Employee[] someMoreEmployees;
    private Employee[] sortedEmployeesByAgeName;

    @Before
    public void initData() {
        employees = new Employee[] { new Employee("John", 25, 3000, 9922001), new Employee("Ace", 22, 2000, 5924001), new Employee("Keith", 35, 4000, 3924401) };
        employeesArrayWithNulls = new Employee[] { new Employee("John", 25, 3000, 9922001), null, new Employee("Ace", 22, 2000, 5924001), null, new Employee("Keith", 35, 4000, 3924401) };

        sortedEmployeesByName = new Employee[] { new Employee("Ace", 22, 2000, 5924001), new Employee("John", 25, 3000, 9922001), new Employee("Keith", 35, 4000, 3924401) };
        sortedEmployeesByNameDesc = new Employee[] { new Employee("Keith", 35, 4000, 3924401), new Employee("John", 25, 3000, 9922001), new Employee("Ace", 22, 2000, 5924001) };

        sortedEmployeesByAge = new Employee[] { new Employee("Ace", 22, 2000, 5924001), new Employee("John", 25, 3000, 9922001), new Employee("Keith", 35, 4000, 3924401) };

        sortedEmployeesByMobile = new Employee[] { new Employee("Keith", 35, 4000, 3924401), new Employee("Ace", 22, 2000, 5924001), new Employee("John", 25, 3000, 9922001), };

        sortedEmployeesBySalary = new Employee[] { new Employee("Ace", 22, 2000, 5924001), new Employee("John", 25, 3000, 9922001), new Employee("Keith", 35, 4000, 3924401), };

        sortedEmployeesArray_WithNullsFirst = new Employee[] { null, null, new Employee("Ace", 22, 2000, 5924001), new Employee("John", 25, 3000, 9922001), new Employee("Keith", 35, 4000, 3924401) };
        sortedEmployeesArray_WithNullsLast = new Employee[] { new Employee("Ace", 22, 2000, 5924001), new Employee("John", 25, 3000, 9922001), new Employee("Keith", 35, 4000, 3924401), null, null };

        someMoreEmployees = new Employee[] { new Employee("Jake", 25, 3000, 9922001), new Employee("Jake", 22, 2000, 5924001), new Employee("Ace", 22, 3000, 6423001), new Employee("Keith", 35, 4000, 3924401) };

        sortedEmployeesByAgeName = new Employee[] { new Employee("Ace", 22, 3000, 6423001), new Employee("Jake", 22, 2000, 5924001), new Employee("Jake", 25, 3000, 9922001), new Employee("Keith", 35, 4000, 3924401) };
        sortedEmployeesByNameAge = new Employee[] { new Employee("Ace", 22, 3000, 6423001), new Employee("Jake", 22, 2000, 5924001), new Employee("Jake", 25, 3000, 9922001), new Employee("Keith", 35, 4000, 3924401) };
    }

    @Test
    public void whenComparing_thenSortedByName() {
        // extract key from element given by employees and then compare the key
        // the key must implements Comparable
        Arrays.sort(employees, Comparator.comparing(Employee::getName));
        assertThat(sortedEmployeesByName).isEqualTo(employees);
    }

    @Test
    public void whenComparingWithoutComparator_thenSortedByNameDesc() {
        Comparator<Employee> employeeComparator = Comparator.comparing(Employee::getName,
                // or using Comparator.reverseOrder();
                (s1, s2) -> s2.compareTo(s1)
        );
        Arrays.sort(employees, employeeComparator);
        assertThat(sortedEmployeesByNameDesc).isEqualTo(employees);
    }

    @Test
    public void whenComparingInt_thenSortedByAge() {
        Comparator<Employee> employeeComparator = Comparator.comparingInt(Employee::getAge);
        Arrays.sort(employees, employeeComparator);
        assertThat(sortedEmployeesByAge).isEqualTo(employees);
    }

    @Test
    public void whenComparingInt_thenSortedBySalary() {
        Comparator<Employee> employeeComparator = Comparator.comparingDouble(Employee::getSalary);
        Arrays.sort(employees, employeeComparator);
        assertThat(sortedEmployeesBySalary).isEqualTo(employees);
    }

    @Test
    public void whenComparingInt_thenSortedByMobile() {
        Comparator<Employee> employeeComparator = Comparator.comparingLong(Employee::getMobile);
        Arrays.sort(employees, employeeComparator);
        assertThat(sortedEmployeesByMobile).isEqualTo(employees);
    }

    @Test
    public void whenNaturalOrder_thenSortedByName() {
        // compare object that is instance of Comparable
        // this will using the element itself compareTo() method
        Comparator<Employee> employeeComparator = Comparator.naturalOrder();
        Arrays.sort(employees, employeeComparator);
        assertThat(sortedEmployeesByName).isEqualTo(employees);
    }

    @Test
    public void whenReverseOrder_thenSortedByNameDesc() {
        // natural reverse order
        Comparator<Employee> employeeComparator = Comparator.reverseOrder();
        Arrays.sort(employees, employeeComparator);
        assertThat(sortedEmployeesByNameDesc).isEqualTo(employees);
    }

    @Test
    public void whenNullsFirst_thenSortedByNameWithNullsFirst() {
        Comparator<Employee> employeeComparator = Comparator.comparing(Employee::getName);
        Comparator<Employee> employeeComparator_withNullsFirst = Comparator.nullsFirst(employeeComparator);
        Arrays.sort(employeesArrayWithNulls, employeeComparator_withNullsFirst);
        assertThat(sortedEmployeesArray_WithNullsFirst).isEqualTo(employeesArrayWithNulls);

    }

    @Test
    public void whenNullsFirst_thenSortedByNameWithNullsLast() {
        Comparator<Employee> employeeComparator = Comparator.comparing(Employee::getName);
        Comparator<Employee> employeeComparator_withNullsLast = Comparator.nullsLast(employeeComparator);
        Arrays.sort(employeesArrayWithNulls, employeeComparator_withNullsLast);
        assertThat(sortedEmployeesArray_WithNullsLast).isEqualTo(employeesArrayWithNulls);
    }

    @Test
    public void whenThenComparing_thenSortByNameAge() {
        Comparator<Employee> employeeComparator = Comparator.comparing(Employee::getName)
                                                            .thenComparingInt(Employee::getAge);
        Arrays.sort(someMoreEmployees, employeeComparator);
        assertThat(sortedEmployeesByNameAge).isEqualTo(someMoreEmployees);
    }

    @Test
    public void whenThenComparing_thenSortByAgeName() {
        Comparator<Employee> employeeComparator = Comparator.comparingInt(Employee::getAge)
                                                            .thenComparing(Employee::getName);
        Arrays.sort(someMoreEmployees, employeeComparator);
        assertThat(sortedEmployeesByAgeName).isEqualTo(someMoreEmployees);
    }

}
