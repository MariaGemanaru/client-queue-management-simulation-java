package Model;

public class Task {
    private int ID;
    private int arrivalTime;
    private int serviceTime;

    private int startProcessingTime;
    private int originalServiceTime;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getID() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return "(" +
                "ID=" + ID +
                ", arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                ')';
    }

    public int getOriginalServiceTime() {
        return originalServiceTime;
    }

    public void setOriginalServiceTime(int originalServiceTime) {
        this.originalServiceTime = originalServiceTime;
    }

    public int getStartProcessingTime() {
        return startProcessingTime;
    }

    public void setStartProcessingTime(int startProcessingTime) {
        this.startProcessingTime = startProcessingTime;
    }
}
