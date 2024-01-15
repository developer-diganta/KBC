package user;

public class User {
    private String name;
    private Integer earnings;

    public User() {
        this.earnings = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEarnings() {
        return earnings;
    }

    public void addEarnings(Integer earnings) {
        this.earnings = this.earnings + earnings;
    }
}
