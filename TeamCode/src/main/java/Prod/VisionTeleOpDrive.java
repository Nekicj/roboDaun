package Prod;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import Controllers.VisionController;

@Config
@TeleOp(name = "Auto Extendo Centering", group = "Concept")
public class VisionTeleOpDrive extends LinearOpMode {
    private VisionController vision;
    private Motor extendoMotor;
    private PIDController extendoPidController = new PIDController(0.01, 0, 0);

    public static double CENTERING_SPEED = 0.4;    // Скорость коррекции
    public static double PIXEL_TO_POS_RATIO = 2.5; // Пиксели -> позиция мотора
    public static double MAX_EXTENDO_POS = 2100;   // Максимальная позиция
    public static int DEADZONE = 15;               // Зона нечувствительности

    private ElapsedTime elapsedTimer = new ElapsedTime();
    private double extendoTargetPosition = 0;

    @Override
    public void runOpMode() {
        vision = new VisionController();
        vision.initialize(hardwareMap, false, false);

        extendoMotor = new Motor(hardwareMap, "extendo", Motor.GoBILDA.RPM_312);
        extendoMotor.setInverted(false);
        extendoMotor.setRunMode(Motor.RunMode.RawPower);
        extendoMotor.resetEncoder();

        MultipleTelemetry multiTelemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );

        waitForStart();

        while (opModeIsActive()) {
            double elapsedTime = elapsedTimer.milliseconds() / 1000.0;
            elapsedTimer.reset();

            handleManualControl(elapsedTime);

            // auto correct
            VisionController.BlobInfo blob = vision.getBiggestBlobInfo(
                    multiTelemetry, true, false, false);

            if (blob != null) {
                updateExtendoPosition(blob);
            }

            updateExtendoMotor();

            updateTelemetry(multiTelemetry, blob);
        }
    }

    private void handleManualControl(double elapsedTime) {
        double manualInput = 0;
        if (gamepad1.left_trigger > 0) manualInput = -0.2;
        if (gamepad1.right_trigger > 0) manualInput = 0.2;

        extendoTargetPosition += manualInput * elapsedTime * 7000;
        extendoTargetPosition = clamp(extendoTargetPosition, 0, MAX_EXTENDO_POS);
    }

    private void updateExtendoPosition(VisionController.BlobInfo blob) {
        int deltaY = blob.deltaY;

        if (Math.abs(deltaY) > DEADZONE) {
            double correction = -deltaY * PIXEL_TO_POS_RATIO * CENTERING_SPEED;
            extendoTargetPosition += correction;
            extendoTargetPosition = clamp(extendoTargetPosition, 0, MAX_EXTENDO_POS);
        }
    }

    private void updateExtendoMotor() {
        double currentPos = extendoMotor.getCurrentPosition();
        double power = extendoPidController.calculate(currentPos, extendoTargetPosition);
        extendoMotor.set(power);
    }

    private void updateTelemetry(MultipleTelemetry telemetry, VisionController.BlobInfo blob) {
        telemetry.addData("Target Position", extendoTargetPosition);
        telemetry.addData("Current Position", extendoMotor.getCurrentPosition());
        telemetry.addData("Motor Power", extendoMotor.get());

        if (blob != null) {
            telemetry.addLine("--- Vision Data ---");
            telemetry.addData("Delta X", blob.deltaX);
            telemetry.addData("Delta Y", blob.deltaY);
            telemetry.addData("Angle", blob.angle);
        }
        telemetry.update();
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}