package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        //lista de servere sa nu fie goala
        if (!servers.isEmpty()) {
            //presupun ca primul server e cel mai bun=timpul cel mai mic de asteptare
            Server best = servers.get(0);
            for (Server s : servers) {  //parcurg toate serverele si l caut pe cel cu tAstp cel mai mic
                if (s.getWaitingPeriod() < best.getWaitingPeriod()) {
                    //daca gasesc un server mai bun, il retin ca fiind cel mai potrivit
                    best = s;
                }
            }
            //adaug clientul in coada cea mai buna
            best.addTask(t);
        }
    }
}