package me.t3sl4.ondergrup.Util.HTTP;

public class RequestURLs {
    public static String BASE_URL = "http://85.95.231.92:3000";

    public static String loginURLPrefix = "/api/login";
    public static String getPassURLPrefix = "/api/getCipheredPass";
    public static String directLoginURLPrefix = "/api/directLogin";
    public static String registerURLPrefix = "/api/register";
    public static String otpURLPrefix = "/api/sendOTP";


    public static String profileInfoURLPrefix = "/api/users/profileInfo/";
    public static String updatePassURLPrefix = "/api/users/updatePass";
    public static String updateProfileURLPrefix = "/api/users/updateProfile";
    public static String wholeProfileURLPrefix = "/api/users/getWholeProfileInfo";
    

    public static String uploadURLPrefix = "/api/fileSystem/upload";
    public static String downloadPhotoURLPrefix = "/api/fileSystem/downloadPhoto";

    public RequestURLs() {

    }
}
