package actuator;


import java.util.List;

import br.ufsc.ine.agent.context.communication.Actuator;
import exemplo.Main;
import javafx.application.Platform;

public class ToDodge  extends Actuator {

    public void act(List<String> list) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Main.toDodge();
            }
        });

    }
}
