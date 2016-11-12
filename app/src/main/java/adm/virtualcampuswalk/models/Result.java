package adm.virtualcampuswalk.models;

import java.util.List;

/**
 * Created by mariusz on 12.11.16.
 */

public class Result<T> {
    private T value;
    private boolean isSuccess;
    private List<String> messages;

    public Result(T value, boolean isSuccess, List<String> messages) {
        this.value = value;
        this.isSuccess = isSuccess;
        this.messages = messages;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Result{" +
                "value=" + value +
                ", isSuccess=" + isSuccess +
                ", messages=" + messages +
                '}';
    }
}
