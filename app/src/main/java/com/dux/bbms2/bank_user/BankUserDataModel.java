package com.dux.bbms2.bank_user;

public class BankUserDataModel {

    private String name,gender,foundedYear,addrress,bloodGroup,mobile,email,uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddrress() {
        return addrress;
    }

    public void setAddrress(String addrress) {
        this.addrress = addrress;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(String foundedYear) {
        this.foundedYear = foundedYear;
    }

    @Override
    public String toString() {
        return "BankUserDataModel{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", foundedYear='" + foundedYear + '\'' +
                ", addrress='" + addrress + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }


}
