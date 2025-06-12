package OpModes;


import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import Controllers.ActionsController;
import Controllers.ExtendController;
import Controllers.IntakeController;
import Controllers.OuttakeController;

@Config
@TeleOp(name = "Main Teleop",group = "Competition")
public class AlohaTeleop extends LinearOpMode {
    private ActionsController actionsController;
    private OuttakeController outtakeController;

    private GamepadEx driver1;
    private GamepadEx driver2;

    private boolean isIntakeTaken = false;
    private boolean isExtended = false;
    private boolean isIntakeOpen = false;

    private boolean isBusket = false;

    @Override
    public void runOpMode(){

        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
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
            driver2.readButtons();

            if (driver2.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)){ //TAKE THIS SHIT
                actionsController.toTakeSpecimen();
            }

            if (driver2.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)){
                actionsController.toPushSpecimen();
            }

            if (driver1.wasJustPressed(GamepadKeys.Button.X)){
                if (!isExtended){
                    actionsController.toIntakeAim();
                    actionsController.setExtendTarget(ExtendController.Positions.EXTEND_MAX.getPos());
                    isExtended = true;
                }else{
                    isExtended = false;
                    actionsController.setTransfer();
                    actionsController.setExtendTarget(0);
                }
            }

            else if (driver1.wasJustPressed(GamepadKeys.Button.A) && isExtended){
                if(!isIntakeTaken){
                    actionsController.toIntakeTake();
                    isIntakeTaken = true;
                }else{
                    actionsController.setIntakeClaw(true);
                    isIntakeTaken = false;
                }
            }

            if (driver1.wasJustPressed(GamepadKeys.Button.B)){
                isIntakeOpen = !isIntakeOpen;
                actionsController.setClaws(isIntakeOpen);
            }



            telemetry.addData("Status", "Running");
            telemetry.addData("isTaken",isIntakeTaken);
            telemetry.addData("Action Busy", actionsController.isBusy() ? "YES" : "NO");
            telemetry.update();

            actionsController.update();


        }
    }

}
