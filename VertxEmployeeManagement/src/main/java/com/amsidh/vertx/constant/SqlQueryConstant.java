package com.amsidh.vertx.constant;

import lombok.Value;

@Value
public final class SqlQueryConstant {

  private SqlQueryConstant() {
  }
  public static final String SQL_SELECT_EMPLOYEE_BY_ID = "select * from employee where id=#{employeeId}";
  public static final String DELETE_EMPLOYEE_QUERY = "delete from employee where id=#{employeeId}";
  public static final String SELECT_FROM_EMPLOYEE = "select * from employee";
  public static final String SAVE_EMPLOYEE_QUERY = "insert into employee (name, emailId, salary) values (#{employeeName}, #{emailId}, #{salary})";
  public static final String SELECT_EMPLOYEE_BY_ID = "select * from employee where id=#{employeeId}";
  public static final String UPDATE_EMPLOYEE_QUERY = "update employee set name=#{employeeName}, emailId=#{emailId}, salary=#{salary} where id=#{employeeId}";


}
