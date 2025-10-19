package eu.coach_yourself.app.callbackinterface;


public interface ServerResponse {
    void onSuccess(String statusCode, String serverResponse);
    void onFailed(String statusCode, String serverResponse);
}
