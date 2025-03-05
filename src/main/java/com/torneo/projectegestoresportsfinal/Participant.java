package com.torneo.projectegestoresportsfinal;

public class Participant {
    private String name;
    private String nickname;
    private String team;
    private int score;

    public Participant(String name, String nickname, String team, int score) {
        this.name = name;
        this.nickname = nickname;
        this.team = team;
        this.score = score;
    }

    public Participant() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return name + " (" + score + " pts)";
    }
}
