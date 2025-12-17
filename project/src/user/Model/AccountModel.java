package user.Model;

public class AccountModel {
    private int userId;
    private String username;
    private String password;
    private Double balance;
    private String createdAt;

    public AccountModel(int userId, String username, String password, Double balance, String createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public AccountModel() {

    }

    public AccountModel(String username, String password, Double balance, String createdAt) {
        this(0, username, password, balance, createdAt);
    }

    // Getter
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Double getBalance() {
        return balance;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Setter
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }

}
