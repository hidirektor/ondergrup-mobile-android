package me.t3sl4.ondergrup.Model.MachineMaintenance;

import android.os.Parcel;
import android.os.Parcelable;

public class Maintenance implements Parcelable {

    private String maintenanceID;
    private String machineID;
    private String technician;
    private String maintenanceDate;
    private String kontrol11;
    private String kontrol12;
    private String kontrol13;
    private String kontrol14;
    private String kontrol21;
    private String kontrol22;
    private String kontrol23;
    private String kontrol24;
    private String kontrol31;
    private String kontrol32;
    private String kontrol33;
    private String kontrol34;
    private String kontrol35;
    private String kontrol36;
    private String kontrol41;
    private String kontrol42;
    private String kontrol43;
    private String kontrol44;
    private String kontrol45;
    private String kontrol46;
    private String kontrol51;
    private String kontrol52;
    private String kontrol53;
    private String kontrol54;
    private String kontrol55;
    private String kontrol56;
    private String kontrol61;
    private String kontrol62;
    private String kontrol63;
    private String kontrol71;
    private String kontrol72;
    private String kontrol81;
    private String kontrol82;
    private String kontrol83;
    private String kontrol91;
    private String kontrol92;
    private String kontrol93;
    private String kontrol94;
    private String kontrol95;
    private String kontrol96;
    private String kontrol97;
    private String kontrol98;
    private String kontrol99;
    private String kontrol910;

    public Maintenance(String maintenanceID, String machineID, String technician, String maintenanceDate) {
        this.maintenanceID = maintenanceID;
        this.machineID = machineID;
        this.technician = technician;
        this.maintenanceDate = maintenanceDate;
    }

    protected Maintenance(Parcel in) {
        maintenanceID = in.readString();
        machineID = in.readString();
        technician = in.readString();
        maintenanceDate = in.readString();
        kontrol11 = in.readString();
        kontrol12 = in.readString();
        kontrol13 = in.readString();
        kontrol14 = in.readString();
        kontrol21 = in.readString();
        kontrol22 = in.readString();
        kontrol23 = in.readString();
        kontrol24 = in.readString();
        kontrol31 = in.readString();
        kontrol32 = in.readString();
        kontrol33 = in.readString();
        kontrol34 = in.readString();
        kontrol35 = in.readString();
        kontrol36 = in.readString();
        kontrol41 = in.readString();
        kontrol42 = in.readString();
        kontrol43 = in.readString();
        kontrol44 = in.readString();
        kontrol45 = in.readString();
        kontrol46 = in.readString();
        kontrol51 = in.readString();
        kontrol52 = in.readString();
        kontrol53 = in.readString();
        kontrol54 = in.readString();
        kontrol55 = in.readString();
        kontrol56 = in.readString();
        kontrol61 = in.readString();
        kontrol62 = in.readString();
        kontrol63 = in.readString();
        kontrol71 = in.readString();
        kontrol72 = in.readString();
        kontrol81 = in.readString();
        kontrol82 = in.readString();
        kontrol83 = in.readString();
        kontrol91 = in.readString();
        kontrol92 = in.readString();
        kontrol93 = in.readString();
        kontrol94 = in.readString();
        kontrol95 = in.readString();
        kontrol96 = in.readString();
        kontrol97 = in.readString();
        kontrol98 = in.readString();
        kontrol99 = in.readString();
        kontrol910 = in.readString();
    }

    public static final Creator<Maintenance> CREATOR = new Creator<Maintenance>() {
        @Override
        public Maintenance createFromParcel(Parcel in) {
            return new Maintenance(in);
        }

        @Override
        public Maintenance[] newArray(int size) {
            return new Maintenance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maintenanceID);
        dest.writeString(machineID);
        dest.writeString(technician);
        dest.writeString(maintenanceDate);
        dest.writeString(kontrol11);
        dest.writeString(kontrol12);
        dest.writeString(kontrol13);
        dest.writeString(kontrol14);
        dest.writeString(kontrol21);
        dest.writeString(kontrol22);
        dest.writeString(kontrol23);
        dest.writeString(kontrol24);
        dest.writeString(kontrol31);
        dest.writeString(kontrol32);
        dest.writeString(kontrol33);
        dest.writeString(kontrol34);
        dest.writeString(kontrol35);
        dest.writeString(kontrol36);
        dest.writeString(kontrol41);
        dest.writeString(kontrol42);
        dest.writeString(kontrol43);
        dest.writeString(kontrol44);
        dest.writeString(kontrol45);
        dest.writeString(kontrol46);
        dest.writeString(kontrol51);
        dest.writeString(kontrol52);
        dest.writeString(kontrol53);
        dest.writeString(kontrol54);
        dest.writeString(kontrol55);
        dest.writeString(kontrol56);
        dest.writeString(kontrol61);
        dest.writeString(kontrol62);
        dest.writeString(kontrol63);
        dest.writeString(kontrol71);
        dest.writeString(kontrol72);
        dest.writeString(kontrol81);
        dest.writeString(kontrol82);
        dest.writeString(kontrol83);
        dest.writeString(kontrol91);
        dest.writeString(kontrol92);
        dest.writeString(kontrol93);
        dest.writeString(kontrol94);
        dest.writeString(kontrol95);
        dest.writeString(kontrol96);
        dest.writeString(kontrol97);
        dest.writeString(kontrol98);
        dest.writeString(kontrol99);
        dest.writeString(kontrol910);
    }

    public String getMaintenanceID() {
        return maintenanceID;
    }

    public void setMaintenanceID(String maintenanceID) {
        this.maintenanceID = maintenanceID;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getKontrol11() {
        return kontrol11;
    }

    public void setKontrol11(String kontrol11) {
        this.kontrol11 = kontrol11;
    }

    public String getKontrol12() {
        return kontrol12;
    }

    public void setKontrol12(String kontrol12) {
        this.kontrol12 = kontrol12;
    }

    public String getKontrol13() {
        return kontrol13;
    }

    public void setKontrol13(String kontrol13) {
        this.kontrol13 = kontrol13;
    }

    public String getKontrol14() {
        return kontrol14;
    }

    public void setKontrol14(String kontrol14) {
        this.kontrol14 = kontrol14;
    }

    public String getKontrol21() {
        return kontrol21;
    }

    public void setKontrol21(String kontrol21) {
        this.kontrol21 = kontrol21;
    }

    public String getKontrol22() {
        return kontrol22;
    }

    public void setKontrol22(String kontrol22) {
        this.kontrol22 = kontrol22;
    }

    public String getKontrol23() {
        return kontrol23;
    }

    public void setKontrol23(String kontrol23) {
        this.kontrol23 = kontrol23;
    }

    public String getKontrol24() {
        return kontrol24;
    }

    public void setKontrol24(String kontrol24) {
        this.kontrol24 = kontrol24;
    }

    public String getKontrol31() {
        return kontrol31;
    }

    public void setKontrol31(String kontrol31) {
        this.kontrol31 = kontrol31;
    }

    public String getKontrol32() {
        return kontrol32;
    }

    public void setKontrol32(String kontrol32) {
        this.kontrol32 = kontrol32;
    }

    public String getKontrol33() {
        return kontrol33;
    }

    public void setKontrol33(String kontrol33) {
        this.kontrol33 = kontrol33;
    }

    public String getKontrol34() {
        return kontrol34;
    }

    public void setKontrol34(String kontrol34) {
        this.kontrol34 = kontrol34;
    }

    public String getKontrol35() {
        return kontrol35;
    }

    public void setKontrol35(String kontrol35) {
        this.kontrol35 = kontrol35;
    }

    public String getKontrol36() {
        return kontrol36;
    }

    public void setKontrol36(String kontrol36) {
        this.kontrol36 = kontrol36;
    }

    public String getKontrol41() {
        return kontrol41;
    }

    public void setKontrol41(String kontrol41) {
        this.kontrol41 = kontrol41;
    }

    public String getKontrol42() {
        return kontrol42;
    }

    public void setKontrol42(String kontrol42) {
        this.kontrol42 = kontrol42;
    }

    public String getKontrol43() {
        return kontrol43;
    }

    public void setKontrol43(String kontrol43) {
        this.kontrol43 = kontrol43;
    }

    public String getKontrol44() {
        return kontrol44;
    }

    public void setKontrol44(String kontrol44) {
        this.kontrol44 = kontrol44;
    }

    public String getKontrol45() {
        return kontrol45;
    }

    public void setKontrol45(String kontrol45) {
        this.kontrol45 = kontrol45;
    }

    public String getKontrol46() {
        return kontrol46;
    }

    public void setKontrol46(String kontrol46) {
        this.kontrol46 = kontrol46;
    }

    public String getKontrol51() {
        return kontrol51;
    }

    public void setKontrol51(String kontrol51) {
        this.kontrol51 = kontrol51;
    }

    public String getKontrol52() {
        return kontrol52;
    }

    public void setKontrol52(String kontrol52) {
        this.kontrol52 = kontrol52;
    }

    public String getKontrol53() {
        return kontrol53;
    }

    public void setKontrol53(String kontrol53) {
        this.kontrol53 = kontrol53;
    }

    public String getKontrol54() {
        return kontrol54;
    }

    public void setKontrol54(String kontrol54) {
        this.kontrol54 = kontrol54;
    }

    public String getKontrol55() {
        return kontrol55;
    }

    public void setKontrol55(String kontrol55) {
        this.kontrol55 = kontrol55;
    }

    public String getKontrol56() {
        return kontrol56;
    }

    public void setKontrol56(String kontrol56) {
        this.kontrol56 = kontrol56;
    }

    public String getKontrol61() {
        return kontrol61;
    }

    public void setKontrol61(String kontrol61) {
        this.kontrol61 = kontrol61;
    }

    public String getKontrol62() {
        return kontrol62;
    }

    public void setKontrol62(String kontrol62) {
        this.kontrol62 = kontrol62;
    }

    public String getKontrol63() {
        return kontrol63;
    }

    public void setKontrol63(String kontrol63) {
        this.kontrol63 = kontrol63;
    }

    public String getKontrol71() {
        return kontrol71;
    }

    public void setKontrol71(String kontrol71) {
        this.kontrol71 = kontrol71;
    }

    public String getKontrol72() {
        return kontrol72;
    }

    public void setKontrol72(String kontrol72) {
        this.kontrol72 = kontrol72;
    }

    public String getKontrol81() {
        return kontrol81;
    }

    public void setKontrol81(String kontrol81) {
        this.kontrol81 = kontrol81;
    }

    public String getKontrol82() {
        return kontrol82;
    }

    public void setKontrol82(String kontrol82) {
        this.kontrol82 = kontrol82;
    }

    public String getKontrol83() {
        return kontrol83;
    }

    public void setKontrol83(String kontrol83) {
        this.kontrol83 = kontrol83;
    }

    public String getKontrol91() {
        return kontrol91;
    }

    public void setKontrol91(String kontrol91) {
        this.kontrol91 = kontrol91;
    }

    public String getKontrol92() {
        return kontrol92;
    }

    public void setKontrol92(String kontrol92) {
        this.kontrol92 = kontrol92;
    }

    public String getKontrol93() {
        return kontrol93;
    }

    public void setKontrol93(String kontrol93) {
        this.kontrol93 = kontrol93;
    }

    public String getKontrol94() {
        return kontrol94;
    }

    public void setKontrol94(String kontrol94) {
        this.kontrol94 = kontrol94;
    }

    public String getKontrol95() {
        return kontrol95;
    }

    public void setKontrol95(String kontrol95) {
        this.kontrol95 = kontrol95;
    }

    public String getKontrol96() {
        return kontrol96;
    }

    public void setKontrol96(String kontrol96) {
        this.kontrol96 = kontrol96;
    }

    public String getKontrol97() {
        return kontrol97;
    }

    public void setKontrol97(String kontrol97) {
        this.kontrol97 = kontrol97;
    }

    public String getKontrol98() {
        return kontrol98;
    }

    public void setKontrol98(String kontrol98) {
        this.kontrol98 = kontrol98;
    }

    public String getKontrol99() {
        return kontrol99;
    }

    public void setKontrol99(String kontrol99) {
        this.kontrol99 = kontrol99;
    }

    public String getKontrol910() {
        return kontrol910;
    }

    public void setKontrol910(String kontrol910) {
        this.kontrol910 = kontrol910;
    }
}
