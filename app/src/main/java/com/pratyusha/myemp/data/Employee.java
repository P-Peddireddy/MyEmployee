package com.pratyusha.myemp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Comparator;

public class Employee  {
    // creating variables for our different fields.
    @Exclude
    private String id;
    private String strFirstName;
    private String strLastName;

    private String strDepartment;

    public String getStrDepartment() {
        return strDepartment;
    }

    public void setStrDepartment(String strDepartment) {
        this.strDepartment = strDepartment;
    }

    public String getStrBasePoint() {
        return strBasePoint;
    }

    public void setStrBasePoint(String strBasePoint) {
        this.strBasePoint = strBasePoint;
    }

    private String strBasePoint;

    public Employee() {
    }

    // creating getter and setter methods.
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }
public static Comparator<Employee> idasc= new Comparator<Employee>() {
    @Override
    public int compare(Employee employee, Employee t1) {

        Double d1 = Double.parseDouble(employee.getStrBasePoint());
        Double d2 = Double.parseDouble(t1.getStrBasePoint());


        return Double.compare(d2,d1);
    }
};

}
