package eu.coach_yourself.app.reservoir;

public interface ReservoirPutCallback {
    public void onSuccess();

    public void onFailure(Exception e);
}
