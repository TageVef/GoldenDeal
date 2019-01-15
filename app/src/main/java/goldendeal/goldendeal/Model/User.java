package goldendeal.goldendeal.Model;

public class User {
    private int ID;
    private String phoneNum;
    private String email;
    private Boolean access;
    private String theme;

    public User() {
    }

    public User(int ID, String phoneNum, String email, Boolean access) {
        this.ID = ID;
        this.phoneNum = phoneNum;
        this.email = email;
        this.access = access;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAccess() {
        return access;
    }

    public void setAccess(Boolean access) {
        this.access = access;
    }
}
