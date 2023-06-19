package com.amsidh.udemy;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonObjectTest {

  @Test
  public void createJsonObject() {
    JsonObject jsonObject = new JsonObject().put("id", 1).put("name", "Amsidh").put("isMarried", true);
    String encode = jsonObject.encode();
    Assertions.assertEquals("{\"id\":1,\"name\":\"Amsidh\",\"isMarried\":true}", encode);
    JsonObject jsonObjectFromString = new JsonObject(encode);
    Assertions.assertEquals(jsonObject, jsonObjectFromString);
  }

  @Test
  public void createJsonObjectFromMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", 1);
    map.put("name", "Amsidh");
    map.put("isMarried", true);
    JsonObject jsonObject = JsonObject.mapFrom(map);
    Assertions.assertEquals(map.get("id"), jsonObject.getInteger("id"));
    Assertions.assertEquals(map.get("name"), jsonObject.getString("name"));
    Assertions.assertEquals(map.get("isMarried"), jsonObject.getBoolean("isMarried"));
  }

  @Test
  public void createJsonObjectFromCustomPojo() {
    Employee employee = new Employee(1, "Amsidh Lokhande", true);
    JsonObject employeeJsonObject = JsonObject.mapFrom(employee);
    Assertions.assertEquals(employeeJsonObject.getInteger("id"), employee.getId());
    Assertions.assertEquals(employeeJsonObject.getString("name"), employee.getName());
    Assertions.assertEquals(true, employeeJsonObject.getBoolean("married"));

    Employee mappedTo = employeeJsonObject.mapTo(Employee.class);
    Assertions.assertEquals(employee.getId(), mappedTo.getId());
    Assertions.assertEquals(employee.getName(), mappedTo.getName());
    Assertions.assertEquals(employee.getMarried(), mappedTo.getMarried());
  }

  @Test
  public void createJsonArrayFromArray() {
    JsonArray jsonArray = new JsonArray().add(
        new Employee(1, "Amsidh", true))
      .add(new Employee(2, "Adithi", false))
      .add(new Employee(3, "Aditya", false))
      .add(new Employee(4, "Anjali", true));
    Assertions.assertEquals("[{\"id\":1,\"name\":\"Amsidh\",\"married\":true},{\"id\":2,\"name\":\"Adithi\",\"married\":false},{\"id\":3,\"name\":\"Aditya\",\"married\":false},{\"id\":4,\"name\":\"Anjali\",\"married\":true}]", jsonArray.encode());
    List<Employee> list = jsonArray.getList();
    Assertions.assertEquals(1, list.get(0).getId());
  }
}


class Employee {
  private Integer id;
  private String name;
  private Boolean isMarried;

  public Employee() {
  }

  public Employee(Integer id, String name, Boolean isMarried) {
    this.id = id;
    this.name = name;
    this.isMarried = isMarried;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getMarried() {
    return isMarried;
  }

  public void setMarried(Boolean married) {
    isMarried = married;
  }
}
