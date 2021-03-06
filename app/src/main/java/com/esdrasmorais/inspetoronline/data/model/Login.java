package com.esdrasmorais.inspetoronline.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class Login extends Default {
//    private Long id;
    private String displayName;
    private Employee employee;
    private Long phone;

    public Login() {

    }

    public Login(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }

    //public Long getId() {
//        return id;
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
