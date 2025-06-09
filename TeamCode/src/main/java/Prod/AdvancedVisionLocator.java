package Prod;

import android.util.Size;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ColorSpace;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

import org.opencv.core.*;

import java.util.*;

@Config
@TeleOp(name = "Stable Vision Locator", group = "Concept")
public class AdvancedVisionLocator extends LinearOpMode {

    public static double SERVO_MIN = 0.0;
    public static double SERVO_MAX = 1.0;
    public static boolean INVERT_SERVO = false;
    private static final double SERVO_180_RANGE = 180.0/270.0;

    public static double MIN_AREA = 5000;
    public static double MAX_AREA = 30000;
    public static int BLUR_SIZE = 7;
    public static int ANGLE_BUFFER = 10;

    // Исправление: Используем публичные поля вместо getWidth()/getHeight()
    private static final Size RESOLUTION = new Size(320, 240);
    private final LinkedList<Double> angleHistory = new LinkedList<>();
    private double normalAngle = 0;

    private Servo IntakeServo = null;

    @Override
    public void runOpMode() {
        ColorBlobLocatorProcessor colorProcessor = createColorProcessor();

        VisionPortal portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(colorProcessor)
                .setCameraResolution(RESOLUTION)
                .setAutoStopLiveView(true)
                .build();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.startCameraStream(portal, 20);

        IntakeServo = hardwareMap.get(Servo.class, "Intake");

        IntakeServo.setDirection(Servo.Direction.REVERSE);
        IntakeServo.setPosition(0);


        waitForStart();

        long lastUpdateTime = System.currentTimeMillis();

        while (opModeIsActive()) {
            List<ColorBlobLocatorProcessor.Blob> blobs = processBlobs(colorProcessor);

            if (System.currentTimeMillis() - lastUpdateTime > 100) {
                updateIntakePosition(blobs);
                updateTelemetry(blobs, dashboard.getTelemetry());
                lastUpdateTime = System.currentTimeMillis();
            }

        }
    }

    private ColorBlobLocatorProcessor createColorProcessor() {
        return new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(new ColorRange(
                        ColorSpace.YCrCb,
                        new Scalar(0, 150, 50),
                        new Scalar(255, 255, 150)))
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                .setRoi(ImageRegion.entireFrame())
                .setDrawContours(true)
                .setBlurSize(BLUR_SIZE)
                .build();
    }

    private void updateIntakePosition(List<ColorBlobLocatorProcessor.Blob> blobs) {
        if (!blobs.isEmpty()) {
            ColorBlobLocatorProcessor.Blob primaryBlob = Collections.max(blobs,
                    Comparator.comparingDouble(b -> b.getContour().size().area()));

            RotatedRect rect = primaryBlob.getBoxFit();
            double angle = calculateStableAngle(rect);

            double servoPosition = convertAngleToServo(angle);

            if(INVERT_SERVO) {
                servoPosition = SERVO_180_RANGE - servoPosition;
            }

            servoPosition = SERVO_MIN + servoPosition;

            servoPosition = clamp(servoPosition, 0.0, SERVO_180_RANGE);

            IntakeServo.setPosition(servoPosition);
        }
    }
    private double convertAngleToServo(double objectAngle) {
        double clampedAngle = clamp(objectAngle, 0.0, 180.0);
        return (clampedAngle / 180.0) * SERVO_180_RANGE;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    private List<ColorBlobLocatorProcessor.Blob> processBlobs(ColorBlobLocatorProcessor processor) {
        List<ColorBlobLocatorProcessor.Blob> blobs = processor.getBlobs();
        ColorBlobLocatorProcessor.Util.filterByArea(MIN_AREA, MAX_AREA, blobs);
        return blobs;
    }

    private void updateTelemetry(List<ColorBlobLocatorProcessor.Blob> blobs, Telemetry telemetry) {
        telemetry.clear();

        if (blobs.isEmpty()) {
            telemetry.addLine("No objects detected");
        } else {
            ColorBlobLocatorProcessor.Blob primaryBlob = Collections.max(blobs,
                    Comparator.comparingDouble(b -> b.getContour().size().area()));

            RotatedRect rect = primaryBlob.getBoxFit();
            double angle = calculateStableAngle(rect);
            Point position = getRelativePosition(rect.center);

            normalAngle = angle;

            telemetry.addData("Объект", "Угол %.1f | X: %.1f | Y: %.1f",
                    angle, position.x, position.y);
        }

        telemetry.update(); // Единый вызов update()
    }

    private double calculateStableAngle(RotatedRect rect) {
        double angle = rect.angle;
        if (rect.size.width < rect.size.height) angle += 90;
        angle = (angle + 360) % 180;

        angleHistory.add(angle);
        if (angleHistory.size() > ANGLE_BUFFER) angleHistory.removeFirst();

        return median(angleHistory);
    }

    private Point getRelativePosition(Point imagePoint) {
        double pixelsPerCm = 50;
        double cameraTilt = Math.toRadians(30);

        double x = (imagePoint.x - RESOLUTION.getWidth()/2.0) / pixelsPerCm;
        double y = (RESOLUTION.getHeight()/2.0 - imagePoint.y) / (pixelsPerCm * Math.cos(cameraTilt));

        return new Point(x, y);
    }

    private double median(List<Double> values) {
        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        int middle = sorted.size()/2;
        return sorted.size() % 2 == 1 ?
                sorted.get(middle) :
                (sorted.get(middle-1) + sorted.get(middle)) / 2.0;
    }
}