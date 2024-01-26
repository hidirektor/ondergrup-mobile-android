package me.t3sl4.ondergrup.Model.Machine;

import android.os.Parcel;
import android.os.Parcelable;

public class Machine implements Parcelable {

    private String ownerUser;
    private String lastUpdate;
    private String machineType;
    private String machineID;
    private String devirmeYuruyusSecim;
    private String calismaSekli;
    private String emniyetCercevesi;
    private String yavaslamaLimit;
    private String altLimit;
    private String kapiTablaAcKonum;
    private String basincSalteri;
    private String kapiSecimleri;
    private String kapiAcTipi;
    private String kapi1Tip;
    private String kapi1AcSure;
    private String kapi2Tip;
    private String kapi2AcSure;
    private String kapitablaTip;
    private String kapiTablaAcSure;
    private String yukariYavasLimit;
    private String devirmeYukariIleriLimit;
    private String devirmeAsagiGeriLimit;
    private String devirmeSilindirTipi;
    private String platformSilindirTipi;
    private String yukariValfTmr;
    private String asagiValfTmr;
    private String devirmeYukariIleriTmr;
    private String devirmeAsagiGeriTmr;
    private String makineCalismaTmr;
    private String buzzer;
    private String demoMode;
    private String calismaSayisi1;
    private String calismaSayisi10;
    private String calismaSayisi100;
    private String calismaSayisi1000;
    private String calismaSayisi10000;
    private String dilSecim;
    private String eepromData37;
    private String eepromData38;
    private String eepromData39;
    private String eepromData40;
    private String eepromData41;
    private String eepromData42;
    private String eepromData43;
    private String eepromData44;
    private String eepromData45;
    private String eepromData46;
    private String eepromData47;
    private String lcdBacklightSure;

    public Machine(String ownerUser, String lastUpdate, String machineType, String machineID, String devirmeYuruyusSecim, String calismaSekli, String emniyetCercevesi,
                   String yavaslamaLimit, String altLimit, String kapiTablaAcKonum, String basincSalteri, String kapiSecimleri,
                   String kapiAcTipi, String kapi1Tip, String kapi1AcSure, String kapi2Tip, String kapi2AcSure, String kapitablaTip,
                   String kapiTablaAcSure, String yukariYavasLimit, String devirmeYukariIleriLimit, String devirmeAsagiGeriLimit,
                   String devirmeSilindirTipi, String platformSilindirTipi, String yukariValfTmr, String asagiValfTmr,
                   String devirmeYukariIleriTmr, String devirmeAsagiGeriTmr, String makineCalismaTmr, String buzzer, String demoMode,
                   String calismaSayisi1, String calismaSayisi10, String calismaSayisi100, String calismaSayisi1000, String calismaSayisi10000,
                   String dilSecim, String eepromData37, String eepromData38, String eepromData39, String eepromData40, String eepromData41,
                   String eepromData42, String eepromData43, String eepromData44, String eepromData45, String eepromData46, String eepromData47,
                   String lcdBacklightSure) {
        this.ownerUser = ownerUser;
        this.lastUpdate = lastUpdate;
        this.machineType = machineType;
        this.machineID = machineID;
        this.devirmeYuruyusSecim = devirmeYuruyusSecim;
        this.calismaSekli = calismaSekli;
        this.emniyetCercevesi = emniyetCercevesi;
        this.yavaslamaLimit = yavaslamaLimit;
        this.altLimit = altLimit;
        this.kapiTablaAcKonum = kapiTablaAcKonum;
        this.basincSalteri = basincSalteri;
        this.kapiSecimleri = kapiSecimleri;
        this.kapiAcTipi = kapiAcTipi;
        this.kapi1Tip = kapi1Tip;
        this.kapi1AcSure = kapi1AcSure;
        this.kapi2Tip = kapi2Tip;
        this.kapi2AcSure = kapi2AcSure;
        this.kapitablaTip = kapitablaTip;
        this.kapiTablaAcSure = kapiTablaAcSure;
        this.yukariYavasLimit = yukariYavasLimit;
        this.devirmeYukariIleriLimit = devirmeYukariIleriLimit;
        this.devirmeAsagiGeriLimit = devirmeAsagiGeriLimit;
        this.devirmeSilindirTipi = devirmeSilindirTipi;
        this.platformSilindirTipi = platformSilindirTipi;
        this.yukariValfTmr = yukariValfTmr;
        this.asagiValfTmr = asagiValfTmr;
        this.devirmeYukariIleriTmr = devirmeYukariIleriTmr;
        this.devirmeAsagiGeriTmr = devirmeAsagiGeriTmr;
        this.makineCalismaTmr = makineCalismaTmr;
        this.buzzer = buzzer;
        this.demoMode = demoMode;
        this.calismaSayisi1 = calismaSayisi1;
        this.calismaSayisi10 = calismaSayisi10;
        this.calismaSayisi100 = calismaSayisi100;
        this.calismaSayisi1000 = calismaSayisi1000;
        this.calismaSayisi10000 = calismaSayisi10000;
        this.dilSecim = dilSecim;
        this.eepromData37 = eepromData37;
        this.eepromData38 = eepromData38;
        this.eepromData39 = eepromData39;
        this.eepromData40 = eepromData40;
        this.eepromData41 = eepromData41;
        this.eepromData42 = eepromData42;
        this.eepromData43 = eepromData43;
        this.eepromData44 = eepromData44;
        this.eepromData45 = eepromData45;
        this.eepromData46 = eepromData46;
        this.eepromData47 = eepromData47;
        this.lcdBacklightSure = lcdBacklightSure;
    }

    protected Machine(Parcel in) {
        ownerUser = in.readString();
        lastUpdate = in.readString();
        machineType = in.readString();
        machineID = in.readString();
        devirmeYuruyusSecim = in.readString();
        calismaSekli = in.readString();
        emniyetCercevesi = in.readString();
        yavaslamaLimit = in.readString();
        altLimit = in.readString();
        kapiTablaAcKonum = in.readString();
        basincSalteri = in.readString();
        kapiSecimleri = in.readString();
        kapiAcTipi = in.readString();
        kapi1Tip = in.readString();
        kapi1AcSure = in.readString();
        kapi2Tip = in.readString();
        kapi2AcSure = in.readString();
        kapitablaTip = in.readString();
        kapiTablaAcSure = in.readString();
        yukariYavasLimit = in.readString();
        devirmeYukariIleriLimit = in.readString();
        devirmeAsagiGeriLimit = in.readString();
        devirmeSilindirTipi = in.readString();
        platformSilindirTipi = in.readString();
        yukariValfTmr = in.readString();
        asagiValfTmr = in.readString();
        devirmeYukariIleriTmr = in.readString();
        devirmeAsagiGeriTmr = in.readString();
        makineCalismaTmr = in.readString();
        buzzer = in.readString();
        demoMode = in.readString();
        calismaSayisi1 = in.readString();
        calismaSayisi10 = in.readString();
        calismaSayisi100 = in.readString();
        calismaSayisi1000 = in.readString();
        calismaSayisi10000 = in.readString();
        dilSecim = in.readString();
        eepromData37 = in.readString();
        eepromData38 = in.readString();
        eepromData39 = in.readString();
        eepromData40 = in.readString();
        eepromData41 = in.readString();
        eepromData42 = in.readString();
        eepromData43 = in.readString();
        eepromData44 = in.readString();
        eepromData45 = in.readString();
        eepromData46 = in.readString();
        eepromData47 = in.readString();
        lcdBacklightSure = in.readString();
    }

    public static final Creator<Machine> CREATOR = new Creator<Machine>() {
        @Override
        public Machine createFromParcel(Parcel in) {
            return new Machine(in);
        }

        @Override
        public Machine[] newArray(int size) {
            return new Machine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ownerUser);
        dest.writeString(lastUpdate);
        dest.writeString(machineType);
        dest.writeString(machineID);
        dest.writeString(devirmeYuruyusSecim);
        dest.writeString(calismaSekli);
        dest.writeString(emniyetCercevesi);
        dest.writeString(yavaslamaLimit);
        dest.writeString(altLimit);
        dest.writeString(kapiTablaAcKonum);
        dest.writeString(basincSalteri);
        dest.writeString(kapiSecimleri);
        dest.writeString(kapiAcTipi);
        dest.writeString(kapi1Tip);
        dest.writeString(kapi1AcSure);
        dest.writeString(kapi2Tip);
        dest.writeString(kapi2AcSure);
        dest.writeString(kapitablaTip);
        dest.writeString(kapiTablaAcSure);
        dest.writeString(yukariYavasLimit);
        dest.writeString(devirmeYukariIleriLimit);
        dest.writeString(devirmeAsagiGeriLimit);
        dest.writeString(devirmeSilindirTipi);
        dest.writeString(platformSilindirTipi);
        dest.writeString(yukariValfTmr);
        dest.writeString(asagiValfTmr);
        dest.writeString(devirmeYukariIleriTmr);
        dest.writeString(devirmeAsagiGeriTmr);
        dest.writeString(makineCalismaTmr);
        dest.writeString(buzzer);
        dest.writeString(demoMode);
        dest.writeString(calismaSayisi1);
        dest.writeString(calismaSayisi10);
        dest.writeString(calismaSayisi100);
        dest.writeString(calismaSayisi1000);
        dest.writeString(calismaSayisi10000);
        dest.writeString(dilSecim);
        dest.writeString(eepromData37);
        dest.writeString(eepromData38);
        dest.writeString(eepromData39);
        dest.writeString(eepromData40);
        dest.writeString(eepromData41);
        dest.writeString(eepromData42);
        dest.writeString(eepromData43);
        dest.writeString(eepromData44);
        dest.writeString(eepromData45);
        dest.writeString(eepromData46);
        dest.writeString(eepromData47);
        dest.writeString(lcdBacklightSure);
    }

    public String getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(String ownerUser) {
        this.ownerUser = ownerUser;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getDevirmeYuruyusSecim() {
        return devirmeYuruyusSecim;
    }

    public void setDevirmeYuruyusSecim(String devirmeYuruyusSecim) {
        this.devirmeYuruyusSecim = devirmeYuruyusSecim;
    }

    public String getCalismaSekli() {
        return calismaSekli;
    }

    public void setCalismaSekli(String calismaSekli) {
        this.calismaSekli = calismaSekli;
    }

    public String getEmniyetCercevesi() {
        return emniyetCercevesi;
    }

    public void setEmniyetCercevesi(String emniyetCercevesi) {
        this.emniyetCercevesi = emniyetCercevesi;
    }

    public String getYavaslamaLimit() {
        return yavaslamaLimit;
    }

    public void setYavaslamaLimit(String yavaslamaLimit) {
        this.yavaslamaLimit = yavaslamaLimit;
    }

    public String getAltLimit() {
        return altLimit;
    }

    public void setAltLimit(String altLimit) {
        this.altLimit = altLimit;
    }

    public String getKapiTablaAcKonum() {
        return kapiTablaAcKonum;
    }

    public void setKapiTablaAcKonum(String kapiTablaAcKonum) {
        this.kapiTablaAcKonum = kapiTablaAcKonum;
    }

    public String getBasincSalteri() {
        return basincSalteri;
    }

    public void setBasincSalteri(String basincSalteri) {
        this.basincSalteri = basincSalteri;
    }

    public String getKapiSecimleri() {
        return kapiSecimleri;
    }

    public void setKapiSecimleri(String kapiSecimleri) {
        this.kapiSecimleri = kapiSecimleri;
    }

    public String getKapiAcTipi() {
        return kapiAcTipi;
    }

    public void setKapiAcTipi(String kapiAcTipi) {
        this.kapiAcTipi = kapiAcTipi;
    }

    public String getKapi1Tip() {
        return kapi1Tip;
    }

    public void setKapi1Tip(String kapi1Tip) {
        this.kapi1Tip = kapi1Tip;
    }

    public String getKapi1AcSure() {
        return kapi1AcSure;
    }

    public void setKapi1AcSure(String kapi1AcSure) {
        this.kapi1AcSure = kapi1AcSure;
    }

    public String getKapi2Tip() {
        return kapi2Tip;
    }

    public void setKapi2Tip(String kapi2Tip) {
        this.kapi2Tip = kapi2Tip;
    }

    public String getKapi2AcSure() {
        return kapi2AcSure;
    }

    public void setKapi2AcSure(String kapi2AcSure) {
        this.kapi2AcSure = kapi2AcSure;
    }

    public String getKapitablaTip() {
        return kapitablaTip;
    }

    public void setKapitablaTip(String kapitablaTip) {
        this.kapitablaTip = kapitablaTip;
    }

    public String getKapiTablaAcSure() {
        return kapiTablaAcSure;
    }

    public void setKapiTablaAcSure(String kapiTablaAcSure) {
        this.kapiTablaAcSure = kapiTablaAcSure;
    }

    public String getYukariYavasLimit() {
        return yukariYavasLimit;
    }

    public void setYukariYavasLimit(String yukariYavasLimit) {
        this.yukariYavasLimit = yukariYavasLimit;
    }

    public String getDevirmeYukariIleriLimit() {
        return devirmeYukariIleriLimit;
    }

    public void setDevirmeYukariIleriLimit(String devirmeYukariIleriLimit) {
        this.devirmeYukariIleriLimit = devirmeYukariIleriLimit;
    }

    public String getDevirmeAsagiGeriLimit() {
        return devirmeAsagiGeriLimit;
    }

    public void setDevirmeAsagiGeriLimit(String devirmeAsagiGeriLimit) {
        this.devirmeAsagiGeriLimit = devirmeAsagiGeriLimit;
    }

    public String getDevirmeSilindirTipi() {
        return devirmeSilindirTipi;
    }

    public void setDevirmeSilindirTipi(String devirmeSilindirTipi) {
        this.devirmeSilindirTipi = devirmeSilindirTipi;
    }

    public String getPlatformSilindirTipi() {
        return platformSilindirTipi;
    }

    public void setPlatformSilindirTipi(String platformSilindirTipi) {
        this.platformSilindirTipi = platformSilindirTipi;
    }

    public String getYukariValfTmr() {
        return yukariValfTmr;
    }

    public void setYukariValfTmr(String yukariValfTmr) {
        this.yukariValfTmr = yukariValfTmr;
    }

    public String getAsagiValfTmr() {
        return asagiValfTmr;
    }

    public void setAsagiValfTmr(String asagiValfTmr) {
        this.asagiValfTmr = asagiValfTmr;
    }

    public String getDevirmeYukariIleriTmr() {
        return devirmeYukariIleriTmr;
    }

    public void setDevirmeYukariIleriTmr(String devirmeYukariIleriTmr) {
        this.devirmeYukariIleriTmr = devirmeYukariIleriTmr;
    }

    public String getDevirmeAsagiGeriTmr() {
        return devirmeAsagiGeriTmr;
    }

    public void setDevirmeAsagiGeriTmr(String devirmeAsagiGeriTmr) {
        this.devirmeAsagiGeriTmr = devirmeAsagiGeriTmr;
    }

    public String getMakineCalismaTmr() {
        return makineCalismaTmr;
    }

    public void setMakineCalismaTmr(String makineCalismaTmr) {
        this.makineCalismaTmr = makineCalismaTmr;
    }

    public String getBuzzer() {
        return buzzer;
    }

    public void setBuzzer(String buzzer) {
        this.buzzer = buzzer;
    }

    public String getDemoMode() {
        return demoMode;
    }

    public void setDemoMode(String demoMode) {
        this.demoMode = demoMode;
    }

    public String getCalismaSayisi1() {
        return calismaSayisi1;
    }

    public void setCalismaSayisi1(String calismaSayisi1) {
        this.calismaSayisi1 = calismaSayisi1;
    }

    public String getCalismaSayisi10() {
        return calismaSayisi10;
    }

    public void setCalismaSayisi10(String calismaSayisi10) {
        this.calismaSayisi10 = calismaSayisi10;
    }

    public String getCalismaSayisi100() {
        return calismaSayisi100;
    }

    public void setCalismaSayisi100(String calismaSayisi100) {
        this.calismaSayisi100 = calismaSayisi100;
    }

    public String getCalismaSayisi1000() {
        return calismaSayisi1000;
    }

    public void setCalismaSayisi1000(String calismaSayisi1000) {
        this.calismaSayisi1000 = calismaSayisi1000;
    }

    public String getCalismaSayisi10000() {
        return calismaSayisi10000;
    }

    public void setCalismaSayisi10000(String calismaSayisi10000) {
        this.calismaSayisi10000 = calismaSayisi10000;
    }

    public String getDilSecim() {
        return dilSecim;
    }

    public void setDilSecim(String dilSecim) {
        this.dilSecim = dilSecim;
    }

    public String getEepromData37() {
        return eepromData37;
    }

    public void setEepromData37(String eepromData37) {
        this.eepromData37 = eepromData37;
    }

    public String getEepromData38() {
        return eepromData38;
    }

    public void setEepromData38(String eepromData38) {
        this.eepromData38 = eepromData38;
    }

    public String getEepromData39() {
        return eepromData39;
    }

    public void setEepromData39(String eepromData39) {
        this.eepromData39 = eepromData39;
    }

    public String getEepromData40() {
        return eepromData40;
    }

    public void setEepromData40(String eepromData40) {
        this.eepromData40 = eepromData40;
    }

    public String getEepromData41() {
        return eepromData41;
    }

    public void setEepromData41(String eepromData41) {
        this.eepromData41 = eepromData41;
    }

    public String getEepromData42() {
        return eepromData42;
    }

    public void setEepromData42(String eepromData42) {
        this.eepromData42 = eepromData42;
    }

    public String getEepromData43() {
        return eepromData43;
    }

    public void setEepromData43(String eepromData43) {
        this.eepromData43 = eepromData43;
    }

    public String getEepromData44() {
        return eepromData44;
    }

    public void setEepromData44(String eepromData44) {
        this.eepromData44 = eepromData44;
    }

    public String getEepromData45() {
        return eepromData45;
    }

    public void setEepromData45(String eepromData45) {
        this.eepromData45 = eepromData45;
    }

    public String getEepromData46() {
        return eepromData46;
    }

    public void setEepromData46(String eepromData46) {
        this.eepromData46 = eepromData46;
    }

    public String getEepromData47() {
        return eepromData47;
    }

    public void setEepromData47(String eepromData47) {
        this.eepromData47 = eepromData47;
    }

    public String getLcdBacklightSure() {
        return lcdBacklightSure;
    }

    public void setLcdBacklightSure(String lcdBacklightSure) {
        this.lcdBacklightSure = lcdBacklightSure;
    }
}
