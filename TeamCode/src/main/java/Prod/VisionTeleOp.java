package Prod;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import Controllers.VisionController;

@Config
@TeleOp(name = "Vision TeleOp",group = "Concept")
public class VisionTeleOp extends LinearOpMode {
    private VisionController vision;
    private Servo targetServo;
    public static boolean BlUE_ALIANCE = true;
    public static boolean IS_BUSKET = false;

    @Override
    public void runOpMode() {
        vision = new VisionController();
        vision.initialize(hardwareMap, BlUE_ALIANCE,IS_BUSKET); // true для синего альянса

        targetServo = hardwareMap.get(Servo.class, "Intake");

        MultipleTelemetry multiTelemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );

        waitForStart();

        while (opModeIsActive()) {
            VisionController.BlobInfo blob = vision.getBiggestBlobInfo(
                    multiTelemetry,
                    true,
                    BlUE_ALIANCE,
                    IS_BUSKET
            );

            if (blob != null) {
                double angle = blob.angle;
                double position = angle / 180.0;
                targetServo.setPosition(position);
            }

            multiTelemetry.update();
        }
    }
}