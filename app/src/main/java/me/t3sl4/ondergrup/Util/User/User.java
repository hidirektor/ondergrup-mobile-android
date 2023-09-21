package me.t3sl4.ondergrup.Util.User;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String role;
    private String userName;
    private String eMail;
    private String nameSurname;
    private String phoneNumber;
    private String profilePhotoPath;
    private String companyName;
    private String createdAt;

    public User(String role, String userName, String eMail, String nameSurname, String phoneNumber, String companyName, String createdAt) {
        this.role = role;
        this.userName = userName;
        this.eMail = eMail;
        this.nameSurname = nameSurname;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.createdAt = createdAt;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(role);
        dest.writeString(userName);
        dest.writeString(eMail);
        dest.writeString(nameSurname);
        dest.writeString(phoneNumber);
        dest.writeString(profilePhotoPath);
        dest.writeString(companyName);
        dest.writeString(createdAt);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Parcelable'den geri çevirme işlemi
    protected User(Parcel in) {
        role = in.readString();
        userName = in.readString();
        eMail = in.readString();
        nameSurname = in.readString();
        phoneNumber = in.readString();
        profilePhotoPath = in.readString();
        companyName = in.readString();
        createdAt = in.readString();
    }
}
