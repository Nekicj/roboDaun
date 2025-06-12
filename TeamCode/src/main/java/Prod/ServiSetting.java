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
@TeleOp(name = "ONE SERVO", group = "Concept")
public class ServiSetting extends LinearOpMode {

    private Servo Servo1 = null;



    public static double Servo1Pos = 0.4;

    private GamepadEx driver1;
    @Override
    public void runOpMode() throws InterruptedException {
        Servo1 =  hardwareMap.get(Servo.class,"OuttakeClaw");

        driver1 = new GamepadEx(gamepad1);


        waitForStart();

        while (opModeIsActive()) {
            driver1.readButtons();

            if (driver1.wasJustPressed(GamepadKeys.Button.B)) {
                Servo1.setPosition(Servo1Pos);
            }

            telemetry.addData("Arm ",Servo1.getController());
            telemetry.update();
        }

    }
}
