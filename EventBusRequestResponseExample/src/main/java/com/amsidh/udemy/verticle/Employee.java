package com.amsidh.udemy.verticle;


import java.io.Serializable;
import java.util.Objects;

public class Employee implements Serializable {
  private Integer empId;
  private String name;

  public Employee() {
  }

  public Employee(Integer empId, String name) {
    this.empId = empId;
    this.name = name;
  }

  public Integer getEmpId() {
    return empId;
  }

  public void setEmpId(Integer empId) {
    this.empId = empId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee employee = (Employee) o;
    return Objects.equals(empId, employee.empId) && Objects.equals(name, employee.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(empId, name);
  }

  @Override
  public String toString() {
    return "Employee{" +
      "empId=" + empId +
      ", name='" + name + '\'' +
      '}';
  }
}
