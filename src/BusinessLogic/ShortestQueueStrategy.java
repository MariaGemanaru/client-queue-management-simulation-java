package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        if (!servers.isEmpty()) {
            Server best = servers.get(0); //pp ca primul server are coada cea mai scurta
            //calc dimensiunea totala a cozii best
            //nr clienti in asteptare+1 daca exista deja un client care e servit in acel moment
            int minSize = best.getTasks().size() + (best.getCurrentTask() != null ? 1 : 0);
            //parcurg toate cozile ca s-o gasesc pe cea mai scurta
            for (Server s : servers) {
                //marimea fiecarei cozi=clienti in asteotare+task activ daca exista
                int currentSize = s.getTasks().size() + (s.getCurrentTask() != null ? 1 : 0);
                //daca are size ul mai bun ca best ul, updatez coada cea mai buna
                if (currentSize < minSize) {
                    best = s;
                    minSize = currentSize;
                }
            }
            //adaug clientul in coada cea mai scurta
            best.addTask(t);
        }
    }
}

