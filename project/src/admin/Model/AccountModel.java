package admin.Model;

public class AccountModel {
    private int userId;
    private String username;
    private String password;
    private long balance;      
    private String createdAt;  

    public AccountModel(int userId, String username, String password, long balance, String createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public AccountModel(String username, String password, long balance, String createdAt) {
        this(0, username, password, balance, createdAt);
    }

    // Getter
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public long getBalance() { return balance; }
    public String getCreatedAt() { return createdAt; }

    // Setter
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setBalance(long balance) { this.balance = balance; }
}
