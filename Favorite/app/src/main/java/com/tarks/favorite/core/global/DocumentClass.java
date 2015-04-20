package com.tarks.favorite.core.global;

/**
 * Created by JHRunning on 11/16/14.
 */

public class DocumentClass {
    public int srl;
    public int user_srl;
    public String name;
    public String content;
    public int comments;
    public int status;



    public DocumentClass(){}

    public DocumentClass(int _srl, int _user_srl, String _name, String _content, int  _comments,  int _status) {
        this.srl = _srl;
        this.user_srl = _user_srl;
        this.name = _name;
        this.content = _content;
        this.comments = _comments;
        this.status = _status;
    }

}
