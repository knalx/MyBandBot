package com.knalx.model;

import java.util.Set;

/**
 * Created by knalx on 16.06.16.
 */
public class Band {
    private String name;
    private Set<Member> members;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }
}
