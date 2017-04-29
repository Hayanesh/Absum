package com.hayanesh.absum;

/**
 * Created by Hayanesh on 29-Mar-17.
 */

public class Aspects {
    String name;
    String score;
    String summary;
    String pos,neg;
    String id;
    public  Aspects() {}
    public Aspects(String id,String name,String score,String summary,String pos,String neg)
    {
        super();
        this.id = id;
        this.name = name;
        this.score = score;
        this.summary = summary;
        this.pos = pos;
        this.neg = neg;

    }
    public void setId(String id)
    {
        this.id = id;
    }
    public void setPos(String pos)
    {
        this.pos = pos;
    }
    public void setNeg(String neg)
    {
        this.neg = neg;

    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setScore(String score)
    {
        this.score = score;
    }
    public void setSummary(String summary)
    {
        this.summary = summary;
    }
    public String getName()
    {
        return name;
    }
    public String getScore()
    {
        return score;
    }
    public String getSummary()
    {
        return summary;
    }
    public String getPos(){return pos;}
    public String getNeg(){return neg;}
    public String  getId(){return id;}
}
