package org.flysky.coder.vo.user;

public class LoginDataWithSessionID extends UserData {
    private String sessionId;

    public LoginDataWithSessionID(){
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
