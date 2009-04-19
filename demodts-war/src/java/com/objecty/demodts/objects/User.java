package com.objecty.demodts.objects;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = 3256718498544564022L;
    private Long id;
    private String username;
    private String password;
    private String email;
    private Date created;	

    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof User)) return false;
        final User can = (User) other;
        return id.toString().equals(can.getId().toString());
    }
	
    public int hashCode() {
        int result;
        result = getId().hashCode();
        return result;
    }

    public String toString() {
        // each object of class we "identify" by it's ID
        return (id.toString());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
		
    public Date getCreated() {
	return created;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}