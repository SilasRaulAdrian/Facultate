package org.example.domain;

public class Client {
    private final long clientId;
    private final String name;
    private final int fidelityGrade;
    private final int age;
    private final Hobby hobby;

    public Client(long clientId, String name, int fidelityGrade, int age, Hobby hobby) {
        this.clientId = clientId;
        this.name = name;
        this.fidelityGrade = fidelityGrade;
        this.age = age;
        this.hobby = hobby;
    }

    public long getClientId() { return clientId; }
    public String getName() { return name; }
    public int getFidelityGrade() { return fidelityGrade; }
    public int getAge() { return age; }
    public Hobby getHobby() { return hobby; }
}
