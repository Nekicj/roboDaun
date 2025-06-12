package OpModes;


import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import Controllers.ActionsController;
import Controllers.OuttakeController;

@Config
@TeleOp(name = "Main Teleop",group = "Competition")
public class AlohaTeleop extends LinearOpMode {
    private ActionsController actionsController;
    private OuttakeController outtakeController;
    private GamepadEx driver1;

    @Override
    public void runOpMode(){

        driver1 = new GamepadEx(gamepad1);
        actionsController = new ActionsController(hardwareMap);
        outtakeController = new OuttakeController();

        outtakeController.initialize(hardwareMap,
                "OuttakeClaw",
                "ClawRotate",
                "OuttakeArmLeft",
                "OuttakeArmRight",
                true);
        telemetry.addData("Status, ","Initialized");
        waitForStart();
        while (opModeIsActive()){
            driver1.readButtons();



            telemetry.addData("Status", "Running");
            telemetry.addData("Action Busy", actionsController.isBusy() ? "YES" : "NO");
            telemetry.update();

        }

    }

}
