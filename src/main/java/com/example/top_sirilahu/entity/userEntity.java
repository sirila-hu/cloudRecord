package com.example.top_sirilahu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_user")
public class userEntity implements UserDetails
{
    @Id
    @JSONField(name = "UID")
    private long UID;

    @Column(name = "username")
    @JSONField(name = "username")
    private String username;

    @Column(name = "password")
    @JSONField(serialize=false)
    private String password;

    @Column(name = "authority")
    @JSONField(name = "authority")
    private String authority;

    @Column(name = "status")
    @JSONField(name = "status")
    private byte status;

    public userEntity() {
    }

    public userEntity(long UID, String username, String password, String authority, byte status) {
        this.UID = UID;
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.status = status;
    }

    public void setUID(long UID) {
        this.UID = UID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getUID() {
        return UID;
    }

    public String getUsername() {
        return username;
    }

    @Override
    @JSONField(serialize=false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize=false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JSONField(serialize=false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //判断账号是否启用
    @Override
    public boolean isEnabled() {
        if (status == 1)
        {
            return true;
        }
        return false;
    }

    @Override
    @JSONField(serialize=false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + authority));
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthority() {
        return authority;
    }

    public byte getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof  UserDetails ? this.username.equals(((UserDetails) obj).getUsername()) : false;
    }
}
