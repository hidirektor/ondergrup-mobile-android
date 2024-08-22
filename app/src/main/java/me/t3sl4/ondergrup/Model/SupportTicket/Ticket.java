package me.t3sl4.ondergrup.Model.SupportTicket;

public class Ticket {
    private int id;
    private String ownerID;
    private String title;
    private String subject;
    private String ticketStatus;
    private String responses;
    private long createdDate;

    // Constructor
    public Ticket(int id, String ownerID, String title, String subject, String ticketStatus, String responses, long createdDate) {
        this.id = id;
        this.ownerID = ownerID;
        this.title = title;
        this.subject = subject;
        this.ticketStatus = ticketStatus;
        this.responses = responses;
        this.createdDate = createdDate;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getResponses() {
        return responses;
    }

    public void setResponses(String responses) {
        this.responses = responses;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
}