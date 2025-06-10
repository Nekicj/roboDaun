package Prod;

// ... остальные импорты и объявления

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import Controllers.VisionController;

@Config
@TeleOp(name="Robot Centric", group = "Concept")
public class DriveRobotCentricTeleOp extends LinearOpMode {
    private VisionController vision;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor rearLeft;
    private DcMotor rearRight;

    ElapsedTime elapsedTimer = new ElapsedTime();

    private Motor  extendoMotor = null;
    PIDController extendoPidController = new PIDController(0.01, 0, 0);

    public static double extendoTargetPosition = 0;
    double extendoMaxPosition = 2100;
    public static double extendoTargetChangeSpeed = 2000;

    public static boolean IS_BUSKET = false;
    public static boolean BlUE_ALIANCE = false;


    @Override
    public void runOpMode() {


        vision = new VisionController();
        vision.initialize(hardwareMap, false, false);

        MultipleTelemetry multiTelemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );


        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        rearLeft = hardwareMap.get(DcMotor.class, "backLeft");
        rearRight = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        rearRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        extendoMotor = new Motor(hardwareMap, "extendo",Motor.GoBILDA.RPM_312);

        extendoMotor.setInverted(false);
        extendoMotor.setRunMode(Motor.RunMode.RawPower);
        extendoMotor.resetEncoder();

        waitForStart();

        while (opModeIsActive()) {
            double elapsedTime = elapsedTimer.milliseconds() / 1000.0;
            elapsedTimer.reset();

            double extendoInputPower = 0;

            if (gamepad1.left_trigger > 0 ) {
                extendoInputPower = -0.2;
            }
            if (gamepad1.right_trigger > 0 ) {
                extendoInputPower = 0.2;
            }


            if (extendoTargetPosition < 0 && !gamepad1.back)
                extendoTargetPosition = 0;
            else if (extendoTargetPosition > extendoMaxPosition)
                extendoTargetPosition = extendoMaxPosition;

            extendoTargetPosition += elapsedTime * extendoInputPower * extendoTargetChangeSpeed;

            double extendoCurrent = extendoMotor.getCurrentPosition();
            double extendoOutputPower = extendoPidController.calculate(extendoCurrent, extendoTargetPosition);

            extendoMotor.set(extendoOutputPower);

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double fl = y + x + rx;
            double fr = y - x - rx;
            double rl = y - x + rx;
            double rr = y + x - rx;

            double max = Math.max(Math.max(Math.abs(fl), Math.abs(fr)),
                    Math.max(Math.abs(rl), Math.abs(rr)));
            if (max > 1) {
                fl /= max;
                fr /= max;
                rl /= max;
                rr /= max;
            }

            frontLeft.setPower(fl);
            frontRight.setPower(fr);
            rearLeft.setPower(rl);
            rearRight.setPower(rr);

            VisionController.BlobInfo blob = vision.getBiggestBlobInfo(
                    multiTelemetry,
                    true,
                    BlUE_ALIANCE,
                    IS_BUSKET
            );

            if (blob != null) {
                double angle = blob.angle;
                double position = angle / 180.0;
            }

            multiTelemetry.update();
        }
    }

}