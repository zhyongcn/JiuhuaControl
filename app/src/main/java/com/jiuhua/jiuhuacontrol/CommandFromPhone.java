package com.jiuhua.jiuhuacontrol;

public class CommandFromPhone {

    private String topic;
    private int qos;
    private String message;
    private boolean retained;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }
}
