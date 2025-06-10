package Controllers;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.Motor;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class LiftController {
    // HARDWARE ====================================================================================
    private Motor leftLift = null;
    private Motor rightLift = null;
    PIDController leftLiftPidController = new PIDController(0.01, 0, 0);
    PIDController rightLiftPidController = new PIDController(0.01, 0, 0);

    // VARIABLES ===================================================================================

    public enum Position{
        HOME(0),
        MAX(650);

        Position(int pos){
            this.position = pos;
        }
        private int position;

        public double getPos() {
            return position;
        }

    }

    public static double target = Position.HOME.getPos();
    public static double liftTargetChangeSpeed = 3000;
    public static double tolerance = 20;
    ElapsedTime elapsedTimer = new ElapsedTime();



    public void initialize(HardwareMap hardwareMap){
        leftLift = new Motor(hardwareMap,"Llift",560,1200);
        rightLift = new Motor(hardwareMap, "Rlift", 560,1200);

        leftLift.setInverted(true);
        rightLift.setInverted(false);

        leftLift.setRunMode(Motor.RunMode.RawPower);
        rightLift.setRunMode(Motor.RunMode.RawPower);

        leftLift.resetEncoder();
        rightLift.resetEncoder();

        //leftLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        //rightLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        //leftLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        //rightLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        elapsedTimer.reset();
    }


    public  void update(){
        update(0);
    }
    public void update(double liftPower) {
        double elapsedTime = elapsedTimer.milliseconds() / 1000.0;
        elapsedTimer.reset();


        if (target < 0 && !gamepad2.back)
            target = 0;
        else if (target > Position.MAX.getPos())
            target = Position.MAX.getPos();


        target += elapsedTime * liftPower * liftTargetChangeSpeed;

        double leftLiftCurrent = leftLift.getCurrentPosition();
        double leftLiftPower = leftLiftPidController.calculate(leftLiftCurrent, target);

        double rightLiftCurrent = rightLift.getCurrentPosition();
        double rightLiftPower = rightLiftPidController.calculate(rightLiftCurrent, target);

        leftLift.set(leftLiftPower);
        rightLift.set(rightLiftPower);
    }

    public void setTargetPosition(double targetPosition){
        target = targetPosition;
    }

    public double getCurrentPosition(){
        return target;
    }

    public boolean isAtPosition() {
        return Math.abs(leftLift.getCurrentPosition() - target) < tolerance
                && Math.abs(rightLift.getCurrentPosition() - target) < tolerance;
    }


    public void showLogs(Telemetry telemetry) {
        telemetry.addData("L pos", leftLift.getCurrentPosition());
        telemetry.addData("R pos", rightLift.getCurrentPosition());
        telemetry.addData("lift target", target);
    }


}
