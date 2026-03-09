package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private volatile boolean running;

    private Task currentTask;
    private int simulationTime; // actualizat de SimulationManager

    public Server() {
        //initializare queue si waitingPeriod
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.running = true;
    }

    public void addTask(Task newTask) {
        //adaugare task in coada
        //incrementare waitingPeriod
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }
//    public void stop() {
//        running = false;
//    }
    @Override
    public void run() {
        while (running) {
            try {
                //take next task from queue
                currentTask = tasks.take();
                //setez startprocessingTime exact cand incepe simularea
                currentTask.setStartProcessingTime(simulationTime);
                //stop the thread for a time wqual with the task's processing time
                //decrement waitingPeriod
                while (currentTask.getServiceTime() > 0 && running) {
                    Thread.sleep(1000);
                    currentTask.setServiceTime(currentTask.getServiceTime() - 1);
                    waitingPeriod.decrementAndGet();
                }
                currentTask = null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    //setter apelat de SimulationManager la fiecare pas
    public void setSimulationTime(int time) {
        this.simulationTime = time;
    }

    public List<Task> getTasksList() {
        return new ArrayList<>(tasks);
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }
}
