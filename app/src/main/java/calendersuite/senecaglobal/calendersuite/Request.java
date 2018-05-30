package calendersuite.senecaglobal.calendersuite;

import java.util.Date;

public class Request {

    private Date date;
    private String message;
    private String senderid;
    private String receiverid;


    public Date getDate(){

        return date;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public String getSenderid() {
        return senderid;
    }

    public String getMessage() {
        return message;
    }


    public Request(Date date,String message,String senderid,String receiverid){
        this.date = date;
        this.message =message;
        this.senderid =senderid;
        this.receiverid = receiverid;

    }
}
