package BusinessLogic;

import GUI.SimulationFrame;
import Model.Task;
import Model.Server;
import Utils.LogSaver;

import java.util.*;

public class SimulationManager implements Runnable {
    //data read from UI
    public int timeLimit = 100;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;
    public int maxArrivalTime = 100;
    public int minArrivalTime = 10;
    public int numberOfServers = 3;
    public int numberOfClients = 100;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> generatedTasks;
    private Map<Integer, Integer> peakLoad = new HashMap<>();
    private List<Task> allTasks = new ArrayList<>();

    public SimulationManager(SimulationFrame frame) {
        //initialize frame
        this.frame = frame;
        readInputData();
        //initialize scheduler
        scheduler = new Scheduler(numberOfServers, 0);
        //initialize selection strategy
        scheduler.changeStrategy(selectionPolicy);
        //generate ranfom of clients and store them to generatedTasks
        generateRandomTasks();
        allTasks.addAll(generatedTasks);
    }

    private void readInputData() {
        try {
            numberOfClients = Integer.parseInt(frame.getNoClients());
            numberOfServers = Integer.parseInt(frame.getNoQueues());
            timeLimit = Integer.parseInt(frame.getSimulationTime());
            minArrivalTime = Integer.parseInt(frame.getMinArrival());
            maxArrivalTime = Integer.parseInt(frame.getMaxArrival());
            minProcessingTime = Integer.parseInt(frame.getMinService());
            maxProcessingTime = Integer.parseInt(frame.getMaxService());

            String policy = frame.getSelectionPolicy();
            if (policy.equals("Shortest Queue")) {
                selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
            } else {
                selectionPolicy = SelectionPolicy.SHORTEST_TIME;
            }

        } catch (NumberFormatException e) {
            frame.showError("Invalid input data. Please fill in all fields correctly.");
        }
    }

    private void generateRandomTasks() {
        generatedTasks = new ArrayList<>();
        Random r = new Random();
        for (int i = 1; i <= numberOfClients; i++) {
            int arrival = r.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int service = r.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;

            Task task = new Task(i, arrival, service);
            task.setOriginalServiceTime(service);
            generatedTasks.add(task);
        }
        //sortez in functie de arrivalTime
        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }

    private boolean areQueuesEmpty() {
        for (Server server : scheduler.getServers()) {
            if (!server.getTasks().isEmpty() || server.getCurrentTask() != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        int currentTime = 0;
        //daca simularea n a ajuns la capat si exista clienti care trebuie procesati
        while (currentTime < timeLimit && (!generatedTasks.isEmpty() || !areQueuesEmpty())) {
            //lista temporara cu toti clientii la momentul curent
            List<Task> toDispatch = new ArrayList<>();
            for (Task t : generatedTasks) {
                //parcurg clientii si verific timpul de sosire cu cel curr
                if (t.getArrivalTime() == currentTime) {
                    toDispatch.add(t);
                }
            }
            //trimit fiecare client catre o coada pe baza strategiei selectate
            for (Task t : toDispatch) {
                scheduler.dispatchTask(t);
            }
            //clientii ajunsi in coada ii sterg din lista de asteptare
            generatedTasks.removeAll(toDispatch);
            //afisare in interfata timpii curenti si lista cu clientii in asteotare
            frame.appendLog("Time: " + currentTime);
            frame.appendLog("Waiting tasks: " + generatedTasks);

            int i = 1; //la numerotare cozi
            int totalServiceTime = 0; //imi trebuie la peak time
            //parcurg cozile si calculez timpul de procesare pt fiecare
            for (Server server : scheduler.getServers()) {
                int queueServiceTime = 0;
                if (server.getCurrentTask() != null) {
                    //daca coada are un client in procesare fox acum, adaug timpul lui de procesare la timpul cozii, asa e logica
                    queueServiceTime += server.getCurrentTask().getServiceTime();
                }
                //adaug si timpul de procesare al tuturor clientilor din coada
                for (Task task : server.getTasks()) {
                    queueServiceTime += task.getServiceTime();
                }
                //timpul de la coada curenta se adauga la totalul de cozi
                totalServiceTime += queueServiceTime;
                //afisez starea fiecarei cozi: cine e procesat acuma si cine asteapta sa fie procesat
                frame.appendLog("Queue " + i++ + ": processing: " + server.getCurrentTask() + " " + server.getTasksList());
            }
            //salvez in peak timpul curr si timpul total de incarcare, trebuie pt peak our
            peakLoad.put(currentTime, totalServiceTime);
            //trec la urmatorul moment
            currentTime++;
            for (Server server : scheduler.getServers()) {
                server.setSimulationTime(currentTime); // trimite timpul catre toate cozile ca sa ppt scadea timpul de procesare al clientilor
            }
            try {
                //pauza de o secunda intre fiecare pas, asa vrea cerinta, sa imite timpul real
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double totalWaiting = 0; //pt average waiting time
        double totalService = 0; //si avg service time
        //calc waiting tmp pt fiecare client
        for (Task task : allTasks) {
            int waiting = task.getStartProcessingTime() - task.getArrivalTime();
            System.out.println(task.getStartProcessingTime() + " " + task.getArrivalTime() + "\n");
            //le adun pt media totala
            totalWaiting += waiting;
            totalService += task.getOriginalServiceTime();
        }

        double avgWaiting = totalWaiting / allTasks.size();
        double avgService = totalService / allTasks.size();

        int peakHour = -1;
        int maxLoad = -1;
        //parcurg momenentele de simulare ot a gasi peak hour
        for (Map.Entry<Integer, Integer> entry : peakLoad.entrySet()) {
            if (entry.getValue() > maxLoad) {
                maxLoad = entry.getValue();
                peakHour = entry.getKey();
            }
        }

        frame.appendLog("");
        frame.appendLog(" *SIMULATION COMPLETE*");
        frame.appendLog("Total clients: " + numberOfClients);
        frame.appendLog("Queues: " + numberOfServers);
        frame.appendLog("Simulation time: " + timeLimit);
        frame.appendLog(String.format("Average waiting time: %.2f", avgWaiting));
        frame.appendLog(String.format("Average service time: %.2f", avgService));
        frame.appendLog("Peak hour: " + peakHour + " sec");
    }

    public static void main(String[] args) {
        SimulationFrame frame = new SimulationFrame();
        frame.setVisible(true);

        frame.getStartButton().addActionListener(e -> {
            SimulationManager gen = new SimulationManager(frame);
            Thread t = new Thread(gen);
            t.start();
        });

        frame.getSaveLogButton().addActionListener(e -> {
            LogSaver.saveLogToFile(frame.getLogContent(), frame);
        });
    }
}
