package com.tarks.favorite.core.global;

/**
 * Created by JHRunning on 11/16/14.srl//user_srl//name//content//date//status//privacy
 */

public class CommentClass {
    public int srl;
    public int user_srl;
    public String name;
    public String content;
    public Long date;
    public int status;
    public int privacy;
    public int you_comment_status;



    public CommentClass(){}

    public CommentClass(int _srl, int _user_srl, String _name, String _content, Long _date, int _status, int _privacy, int _you_comment_status) {
        this.srl = _srl;
        this.user_srl = _user_srl;
        this.name = _name;
        this.content = _content;
        this.date = _date;
        this.status = _status;
        this.privacy = _privacy;
        this.you_comment_status = _you_comment_status;
    }

}
