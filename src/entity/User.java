package entity;

import ui.login.LogoutCallback;

public abstract class User implements IdAssignable, CsvConvertible {
    private int id;
    private String username;
    private String password;
    private String name;
    private String lastname;
    private Gender gender;
    private String phoneNum;
    private String address;

    public User() {}

    public User(int id, String username, String password, String name, String lastname, Gender gender, String phoneNum, String address){
        setId(id);
        setUsername(username);
        setPassword(password);
        setName(name);
        setLastname(lastname);
        setGender(gender);
        setPhoneNum(phoneNum);
        setAddress(address);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User id(int id) {
        setId(id);
        return this;
    }

    public User username(String username) {
        setUsername(username);
        return this;
    }

    public User password(String password) {
        setPassword(password);
        return this;
    }

    public User name(String name) {
        setName(name);
        return this;
    }

    public User lastname(String lastname) {
        setLastname(lastname);
        return this;
    }

    public User gender(Gender gender) {
        setGender(gender);
        return this;
    }

    public User phoneNum(String phoneNum) {
        setPhoneNum(phoneNum);
        return this;
    }

    public User address(String address) {
        setAddress(address);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return id == user.id;
    }

    abstract public String[] toCsv();

    abstract public void showGUI(LogoutCallback logoutCallback);
}
