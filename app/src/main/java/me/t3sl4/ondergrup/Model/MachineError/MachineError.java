package me.t3sl4.ondergrup.Model.MachineError;

import android.os.Parcel;
import android.os.Parcelable;

public class MachineError implements Parcelable {

    private String machineID;
    private String errorCode;
    private String errorMessage;
    private String errorDate;

    public MachineError(String machineID, String errorCode, String errorMessage, String errorDate) {
        this.machineID = machineID;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDate = errorDate;
    }

    protected MachineError(Parcel in) {
        machineID = in.readString();
        errorCode = in.readString();
        errorMessage = in.readString();
        errorDate = in.readString();
    }

    public static final Creator<MachineError> CREATOR = new Creator<MachineError>() {
        @Override
        public MachineError createFromParcel(Parcel in) {
            return new MachineError(in);
        }

        @Override
        public MachineError[] newArray(int size) {
            return new MachineError[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(machineID);
        dest.writeString(errorCode);
        dest.writeString(errorMessage);
        dest.writeString(errorDate);
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(String errorDate) {
        this.errorDate = errorDate;
    }
}
