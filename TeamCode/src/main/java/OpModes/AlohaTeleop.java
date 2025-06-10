package OpModes;


import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import Controllers.ActionsController;

@Config
@TeleOp(name = "Main Teleop",group = "Competition")
public class AlohaTeleop extends LinearOpMode {
    private ActionsController actionsController;
    private GamepadEx driver1;

    @Override
    public void runOpMode(){
        actionsController = new ActionsController(hardwareMap);

        telemetry.addData("Status, ","Initialized");
        waitForStart();
        while (opModeIsActive()){
            GamepadEx driver1 = new GamepadEx(gamepad1);

            if(driver1.wasJustPressed(GamepadKeys.Button.B)){
                actionsController.toBusket();
            }

            actionsController.update();

            telemetry.addData("Status", "Running");
            telemetry.addData("Action Busy", actionsController.isBusy() ? "YES" : "NO");
            telemetry.update();


        }

    }

}
