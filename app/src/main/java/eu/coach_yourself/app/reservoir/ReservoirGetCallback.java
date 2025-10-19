package eu.coach_yourself.app.reservoir;

public interface ReservoirGetCallback<T> {
    public void onSuccess(T object);

    public void onFailure(Exception e);
}
