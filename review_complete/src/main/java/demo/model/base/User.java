package demo.model.base;

public abstract class User {
    protected String userId;
    protected String name;
    protected int age;
    
    public User(String userId, String name, int age) {
        this.userId = userId;
        this.name = name;
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    protected abstract void setCourse(String course);
}
