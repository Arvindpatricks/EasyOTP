package com.hackadroid.arvind.easyotp;

/**
 * Created by arvind on 14/03/16.
 */
public class Person {
    //name and address string
    private String name;
    private String emailid;
    private String otp;
    private boolean autoSend;

    public Person() {
      /*Blank default constructor essential for Firebase*/
    }
    //Getters and setters
    public String getName() {
        return name;
    }

    public boolean getAutoSend(){return autoSend;}

    public void setAutoSend(boolean autoSend){this.autoSend=autoSend;}
    public void setName(String name) {
        this.name = name;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getOtp(){
        return otp;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public void setOtp(String otp){
        this.otp=otp;
    }
}
