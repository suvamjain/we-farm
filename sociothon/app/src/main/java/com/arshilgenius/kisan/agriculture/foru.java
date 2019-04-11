package com.arshilgenius.kisan.agriculture;

/**
 * Created by humra on 2/10/2017.
 */
public class foru {

  private String post;
  private long time;
  long stackId;

  public foru() {
    /*Blank default constructor essential for Firebase*/
  }
  public foru(String a)
  {}

  public String getPost() {
      return post;
  }

  public void setPost(String post) {
        this.post = post;
    }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public long getStackId() {
    return stackId;
  }

  public void setStackId(long stackId) {
    this.stackId = stackId;
  }

  //Getters and setters

}

