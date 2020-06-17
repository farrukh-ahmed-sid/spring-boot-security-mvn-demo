package com.example.demo.security;

public enum Permissions {

    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write");

    private String permission;

    Permissions(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}
