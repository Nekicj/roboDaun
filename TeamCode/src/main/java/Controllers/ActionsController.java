package Controllers;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class ActionsController {
    private final LiftController liftController;
    private final OuttakeController outtakeController;

    private final CommandScheduler BusketScheduler = new CommandScheduler();

    private final ElapsedTime actionTimer = new ElapsedTime();

    public ActionsController(HardwareMap hardwareMap){
        liftController = new LiftController();
        outtakeController = new OuttakeController();

        liftController.initialize(hardwareMap);
        outtakeController.initialize(hardwareMap);
    }

    public void update(){
        liftController.update();
        BusketScheduler.update();
    }

    public void toBusket(){

        BusketScheduler.clearQueue();
        BusketScheduler.setAutoReset(true);

        BusketScheduler.scheduleCommand(() -> {liftController.setTargetPosition(500);});
        BusketScheduler.scheduleDelay(0.2);
        //BusketScheduler.scheduleCommand(() -> {outtakeController.setClawOpen();});
        BusketScheduler.scheduleCommand(outtakeController::setClawOpen);

        BusketScheduler.start();

    }



    public boolean isBusy() {
        return BusketScheduler.isFinished();
    }


}
