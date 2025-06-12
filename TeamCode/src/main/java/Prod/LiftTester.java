package Prod;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp(name = "Lift PID Fixed", group = "Concept")
public class LiftTester extends LinearOpMode {

    public static double kP = 0.02;
    public static double kG = 0.12;
    public static double kI = 0.001;
    public static double kD = 0;

    public static double liftTargetPosition = 0;
    public static double maxPosition = 650;
    public static double liftTargetChangeSpeed = 2000;

    private Motor leftLift, rightLift;
    PIDController leftLiftPidController = new PIDController(kP, kI, kD);
    PIDController rightLiftPidController = new PIDController(kP, kI , kD);
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        leftLift = new Motor(hardwareMap, "Llift");
        rightLift = new Motor(hardwareMap, "Rlift");

        leftLift.setInverted(false);
        rightLift.setInverted(true);

        leftLift.resetEncoder();
        rightLift.resetEncoder();

        leftLift.setRunMode(Motor.RunMode.RawPower);
        rightLift.setRunMode(Motor.RunMode.RawPower);

        waitForStart();

        while (opModeIsActive()) {
            double elapsedTime = timer.seconds();
            timer.reset();

            double liftPower = 0;

            if (gamepad2.left_trigger > 0)
                liftPower = -1;
            if (gamepad2.right_trigger > 0)
                liftPower = 1;

            if (liftTargetPosition < 0 && !gamepad2.back)
                liftTargetPosition = 0;
            else if (liftTargetPosition > maxPosition)
                liftTargetPosition = maxPosition;

            if (gamepad1.b){
                liftTargetPosition = 650;
            }

            //if (gamepad2.options){
            //    rightLift.resetEncoder();
            //    leftLift.resetEncoder();
            //    liftTargetPosition = 0;
            //}

            liftTargetPosition += elapsedTime * liftPower * liftTargetChangeSpeed;

            double leftLiftCurrent = leftLift.getCurrentPosition();
            double leftLiftPower = leftLiftPidController.calculate(leftLiftCurrent, liftTargetPosition);

            double rightLiftCurrent = rightLift.getCurrentPosition();
            double rightLiftPower = rightLiftPidController.calculate(rightLiftCurrent, liftTargetPosition);

            leftLift.set(-leftLiftPower);
            rightLift.set(rightLiftPower);

            telemetry.addData("Target", liftTargetPosition);
            telemetry.addData("Left Pos", leftLift.getCurrentPosition());
            telemetry.addData("Right Pos", rightLift.getCurrentPosition());
            telemetry.addData("Left Power", leftLiftPower);
            telemetry.addData("Right Power", rightLiftPower);
            telemetry.addData("kP", kP);
            telemetry.addData("kG (gravity feedforward)", kG);
            telemetry.update();
        }
    }

    private void resetEncoders() {
        leftLift.resetEncoder();
        rightLift.resetEncoder();
        liftTargetPosition = 0;
    }
}
