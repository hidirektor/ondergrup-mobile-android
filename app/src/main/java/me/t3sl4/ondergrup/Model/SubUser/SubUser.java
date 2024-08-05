package me.t3sl4.ondergrup.Model.SubUser;

import android.os.Parcel;
import android.os.Parcelable;

public class SubUser implements Parcelable {

    private String subUserID;
    private String role;
    private String userName;
    private String eMail;
    private String nameSurname;
    private String phone;
    private String companyName;
    private String owner;
    private String isActive;

    public SubUser(String subUserID, String role, String userName, String eMail, String nameSurname, String phone, String companyName, String owner, String isActive) {
        this.subUserID = subUserID;
        this.role = role;
        this.userName = userName;
        this.eMail = eMail;
        this.nameSurname = nameSurname;
        this.phone = phone;
        this.companyName = companyName;
        this.owner = owner;
        this.isActive = isActive;
    }

    protected SubUser(Parcel in) {
        subUserID = in.readString();
        role = in.readString();
        userName = in.readString();
        eMail = in.readString();
        nameSurname = in.readString();
        phone = in.readString();
        companyName = in.readString();
        owner = in.readString();
        isActive = in.readString();
    }

    public String getSubUserID() {
        return subUserID;
    }

    public void setSubUserID(String subUserID) {
        this.subUserID = subUserID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public static final Creator<SubUser> CREATOR = new Creator<SubUser>() {
        @Override
        public SubUser createFromParcel(Parcel in) {
            return new SubUser(in);
        }

        @Override
        public SubUser[] newArray(int size) {
            return new SubUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subUserID);
        dest.writeString(role);
        dest.writeString(userName);
        dest.writeString(eMail);
        dest.writeString(nameSurname);
        dest.writeString(phone);
        dest.writeString(companyName);
        dest.writeString(owner);
        dest.writeString(isActive);
    }
}
