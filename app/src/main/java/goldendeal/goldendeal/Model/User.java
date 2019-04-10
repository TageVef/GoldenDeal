package goldendeal.goldendeal.Model;

public class User {

    private String userID;
    private String phoneNum;
    private String email;
    private Boolean role;
    private String theme;
    private String password;
    private String language;

    public User() {

        email = "none";
        role = Boolean.FALSE;
        theme = "none";
        language = "Norsk";
    }

    public User(String userID, String phoneNum, String email, Boolean role, String theme, String password) {
        this.userID = userID;
        this.phoneNum = phoneNum;
        this.email = email;
        this.role = role;
        this.theme = theme;
        this.password = password;
        language = "Norsk";
    }

    public User(String userID, String phoneNum, String email, Boolean role, String theme, String password, String language) {
        this.userID = userID;
        this.phoneNum = phoneNum;
        this.email = email;
        this.role = role;
        this.theme = theme;
        this.password = password;
        this.language = language;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public Boolean getRole() {
        return role;
    }

    public void setRole(Boolean role) {
        this.role = role;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguage() {return language;}

    public void setLanguage(String language) { this.language = language; }
}
