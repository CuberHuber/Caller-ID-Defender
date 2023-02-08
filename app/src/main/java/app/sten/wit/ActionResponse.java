package app.sten.wit;

public class ActionResponse {

    public static int STATUS_EXIST_ORGANIZATION = 1;
    public static int STATUS_NOT_EXIST_ORGANIZATION = 2;
    public static int STATUS_UNKNOWN_ORGANIZATION = 3;
    public int Status;
    public String Info;

    public ActionResponse(int status, String info) {
        Status = status;
        Info = info;
    }
}
