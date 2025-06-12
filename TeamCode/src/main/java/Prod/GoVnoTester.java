package Prod;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "ODO",group = "Concept")
public class GoVnoTester extends LinearOpMode {

    private Motor leftLift;
    private Motor rightLift;

    @Override
    public void runOpMode(){
        leftLift = new Motor(hardwareMap, "Llift");
        rightLift = new Motor(hardwareMap, "Rlift");

        leftLift.setInverted(true);
        rightLift.setInverted(false);

        leftLift.setRunMode(Motor.RunMode.RawPower);
        rightLift.setRunMode(Motor.RunMode.RawPower);

        leftLift.resetEncoder();
        rightLift.resetEncoder();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            GamepadEx driver1 = new GamepadEx(gamepad1);

            if (driver1.wasJustPressed(GamepadKeys.Button.B)){
                leftLift.set(3);
                rightLift.set(3);
            }
            else if(driver1.wasJustPressed(GamepadKeys.Button.X)){
                leftLift.set(-3);
                rightLift.set(-3);
            }

            driver1.readButtons();

        }
    }
}
