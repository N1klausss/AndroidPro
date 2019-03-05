package com.example.a58204.lab10;

/**
 * Created by 58204 on 2017/12/9.
 */

public class Member {
    private String name;
    private String birth;
    private String gift;

    public Member(){}

    public Member(String name, String birth, String gift)
    {
        this.name = name;
        this.birth = birth;
        this.gift = gift;
    }

    public String getName()
    {
        return this.name;
    }
    public String getBirth()
    {
        return this.birth;
    }
    public String getGift()
    {
        return this.gift;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setBirth(String birth)
    {
        this.birth = birth;
    }
    public void setGift(String gift)
    {
        this.gift = gift;
    }
}
