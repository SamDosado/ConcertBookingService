package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a User Object
 * id          the generated id of the user object
 * username    the string for the username
 * password    the string for the password
 * version     the version of the user object
 */

@Entity
@Table(name="USERS")
public class User{
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private int version;

    public User() {

    }

    // This constructor is used in UserMapper.java but it doesnt create a id or version
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password, int version) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.version = version;
    }

    public Long getId(){ return id;}

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    // The equals function may need to be changed as we may not always have an ID from the UserMapper
    @Override
    public boolean equals(Object other){
        if(!(other instanceof User))
            return false;
        if (other == this)
            return true;

        User o = (User) other;

        return new EqualsBuilder().append(username, o.getUsername()).append(password, o.getPassword()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getClass().getName()).append(id).append(username).append(password).append(version).toHashCode();
    }

}
