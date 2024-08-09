package me.t3sl4.ondergrup.Model.MachineVersion;

import android.os.Parcel;
import android.os.Parcelable;

public class Version implements Parcelable {

    private String versionTitle;
    private String versionDesc;
    private String versionCode;
    private String versionID;
    private String releaseDate;

    public Version(String versionTitle, String versionDesc, String versionCode, String versionID, String releaseDate) {
        this.versionTitle = versionTitle;
        this.versionDesc = versionDesc;
        this.versionCode = versionCode;
        this.versionID = versionID;
        this.releaseDate = releaseDate;
    }

    public String getVersionTitle() {
        return versionTitle;
    }

    public void setVersionTitle(String versionTitle) {
        this.versionTitle = versionTitle;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(versionTitle);
        dest.writeString(versionDesc);
        dest.writeString(versionCode);
        dest.writeString(versionID);
        dest.writeString(releaseDate);
    }

    public static final Creator<Version> CREATOR = new Creator<Version>() {
        @Override
        public Version createFromParcel(Parcel in) {
            return new Version(in);
        }

        @Override
        public Version[] newArray(int size) {
            return new Version[size];
        }
    };

    protected Version(Parcel in) {
        versionTitle = in.readString();
        versionDesc = in.readString();
        versionCode = in.readString();
        versionID = in.readString();
        releaseDate = in.readString();
    }
}
