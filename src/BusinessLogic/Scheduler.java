package BusinessLogic;

import Model.Server;
import Model.Task;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new ArrayList<>();

        // create server object
        // create thread with the object
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            servers.add(server);
            Thread t = new Thread(server);
            t.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        // apply strategy pattern to instantiate the strategy with the concrete class
        // strategy corresponding to policy
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task t) {
        // call the strategy addTask method
        strategy.addTask(servers, t);
    }

    public List<Server> getServers() {
        return servers;
    }
}
