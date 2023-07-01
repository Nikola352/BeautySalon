package ui.manager.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import entity.Cosmetologist;
import entity.Employee;
import entity.Gender;
import entity.Manager;
import entity.Receptionist;

public class EmployeeTableModel extends AbstractTableModel {
    private ArrayList<Employee> employees;
    private String[] columnNames = {"Ime i prezime", "Uloga", "Korisničko ime", "Lozinka", "Pol", "Telefon", "Adresa", "Radni staž", "Bonus", "Plata"};

    public EmployeeTableModel(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(employees.isEmpty() || row >= employees.size())
            return null;
        Employee employee = employees.get(row);
        switch (column) {
            case 0:
                return employee.getName() + " " + employee.getLastname();
            case 1:
                if(employee instanceof Manager)
                    return "Menadžer";
                else if(employee instanceof Cosmetologist)
                    return "Kozmetičar";
                else if(employee instanceof Receptionist)
                    return "Recepcioner";
                else
                    return null;
            case 2:
                return employee.getUsername();
            case 3:
                return employee.getPassword();
            case 4:
                if(employee.getGender() == Gender.MALE)
                    return "Muški";
                else if(employee.getGender() == Gender.FEMALE)
                    return "Ženski";
                else 
                    return null;
            case 5:
                return employee.getPhoneNum();
            case 6:
                return employee.getAddress();
            case 7:
                return employee.getYearsOfExperience();
            case 8:
                return employee.getBonus();
            case 9:
                return employee.getSalary();
            default:
                return null;
        }
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
        fireTableDataChanged();
    }

    public Employee getEmployee(int row) {
        return employees.get(row);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        fireTableDataChanged();
    }

    public void removeEmployee(int row) {
        employees.remove(row);
        fireTableDataChanged();
    }
}
