package com.usermanagement.Dto;

public class UserDto {
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public int getActive() {
        return Active;
    }

    public void setActive(int active) {
        Active = active;
    }

    private int Active=1;

    public int getDeActive() {
        return deActive;
    }

    public void setDeActive(int deActive) {
        this.deActive = deActive;
    }

    private int deActive=0;

    }

