//package OpModes;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//import Controllers.VisionController;
//import Controllers.VisionControllerERD;
//@Config
//public class VisionTeleOpERD extends LinearOpMode {
//    // PID коэффициенты
//    public static double xkP = 0.003, xkI = 0.001, xkD = 0.0005;
//    public static double ykP = 0.004, ykI = 0.001, ykD = 0.0005;
//    public static double anglekP = 0.01, anglekI = 0.001, anglekD = 0.005;
//    public static double extendoKP = 0.05;
//
//    // Допуски
//    public static double POSITION_THRESHOLD = 10; // пикселей
//    public static double ANGLE_THRESHOLD = 2; // градусы
//
//    @Override
//    public void runOpMode(){
//        // Инициализация компонентов
//        VisionController visionController = new VisionController(hardwareMap, getAllianceColor());
//        ExtendoController extendoController = new ExtendoController(hardwareMap);
//
//        // Настройка HDrive
//        Motor leftBack = new Motor(hardwareMap, "leftBack");
//        Motor leftFront = new Motor(hardwareMap, "leftFront");
//        Motor rightBack = new Motor(hardwareMap, "rightBack");
//        Motor rightFront = new Motor(hardwareMap, "rightFront");
//        HDrive drive = new HDrive(leftFront, rightFront, leftBack, rightBack);
//
//        // PID контроллеры
//        PIDController xPID = new PIDController(xkP, xkI, xkD);
//        PIDController yPID = new PIDController(ykP, ykI, ykD);
//        PIDController anglePID = new PIDController(anglekP, anglekI, anglekD);
//
//        waitForStart();
//
//        while (opModeIsActive()) {
//            VisionController.Location location = visionController.getLargestLocation(false);
//
//            if (!Double.isNaN(location.x) && !Double.isNaN(location.y)) {
//                // Расчет ошибок
//                double xError = location.x;
//                double yError = location.y;
//                double angleError = location.angle;
//
//                // Вычисление управляющих сигналов
//                double xPower = xPID.calculate(xError, 0);
//                double yPower = yPID.calculate(yError, 0);
//                double anglePower = anglePID.calculate(angleError, 0);
//
//                // Управление выдвижным механизмом
//                double extendoPower = yError * extendoKP;
//                extendoController.setPower(extendoPower);
//
//                // Преобразование в управления для HDrive
//                drive.driveRobotCentric(
//                        limitPower(yPower),  // Forward/Backward
//                        limitPower(xPower),  // Strafe
//                        limitPower(anglePower) // Rotation
//                );
//
//                // Проверка достижения цели
//                if (Math.abs(xError) < POSITION_THRESHOLD &&
//                        Math.abs(yError) < POSITION_THRESHOLD &&
//                        Math.abs(angleError) < ANGLE_THRESHOLD) {
//                    stopMotors();
//                    telemetry.addData("Status", "Centered!");
//                }
//            } else {
//                stopMotors();
//                telemetry.addData("Status", "No target");
//            }
//
//            updateTelemetry(location);
//            sleep(20);
//        }
//    }
//
//    private void stopMotors() {
//        drive.stop();
//        extendoController.stop();
//    }
//
//    private double limitPower(double power) {
//        return Math.max(-1.0, Math.min(1.0, power));
//    }
//
//    private void updateTelemetry(VisionController.Location loc) {
//        telemetry.addData("X Error", loc.x);
//        telemetry.addData("Y Error", loc.y);
//        telemetry.addData("Angle Error", loc.angle);
//        telemetry.addData("Extendo Power", extendoController.getCurrentPower());
//        telemetry.update();
//    }
//}