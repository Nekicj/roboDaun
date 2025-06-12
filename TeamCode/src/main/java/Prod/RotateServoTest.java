package Prod;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp(name = "RotateServo", group = "Concept")
public class RotateServoTest extends LinearOpMode {

    private Servo Servo1 = null;
    private Servo Servo2 = null;



    public static double Servospos = 0.72;

    public static double rotatePos = 0.46;

    private GamepadEx driver1;
    @Override
    public void runOpMode() throws InterruptedException {
        Servo1 =  hardwareMap.get(Servo.class,"IntakeArm");
        Servo2 = hardwareMap.get(Servo.class,"IntakeRotate");

        driver1 = new GamepadEx(gamepad1);


        waitForStart();

        while (opModeIsActive()) {
            driver1.readButtons();

            if (driver1.wasJustPressed(GamepadKeys.Button.B)) {
                Servo1.setPosition(Servospos);
            }
            if (gamepad1.left_trigger > 0){
                rotatePos += 0.001;
            }
            if (gamepad1.right_trigger > 0){
                rotatePos -= 0.001;
            }

            if(rotatePos > 1){
                rotatePos = 1;
            }
            if(rotatePos <0){
                rotatePos = 0;
            }

            Servo2.setPosition(rotatePos);

            telemetry.addData("grdaus",rotatePos);
            telemetry.update();
        }

    }
}
