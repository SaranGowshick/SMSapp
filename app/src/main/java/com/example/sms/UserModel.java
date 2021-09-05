package com.example.sms;

public class UserModel {
    private String id;
    private String name;
    private String contact;
    private String message;
    private String status;
    //    String id,name,contact,message,status;
    public UserModel(String id, String name, String contact, String message, String status) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.message = message;
        this.status = status;
        }
        public String getId () {
            return id;
        }
        public String getName () {
            return name;
        }
        public String getContact() {
            return contact;
        }
        public String getMessage() {
            return message;
        }
        public String getStatus(){
            return status;
        }
    }
