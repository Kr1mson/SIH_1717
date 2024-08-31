package com.example.voiceversa;

public class CourseModel {

    private String course_name;
    private int imgid;
    private String ver;
    private int languageCode;

    public CourseModel(String course_name, int imgid,String ver) {
        this.course_name = course_name;
        this.imgid = imgid;
        this.ver=ver;
    }
    public String getVer(){
        return  ver;
    }
    public void setVer(String ver){
        this.ver=ver;
    }

    public String getCourse_name() {
        return course_name;
    }


    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}
