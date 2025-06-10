package Prod;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp(name = "Lift PID Test", group = "Linear OpMode")
public class LiftTester extends LinearOpMode {

    // ===== Параметры PID =====
    public static double kP = 0.03;
    public static double kI = 0.000;
    public static double kD = 0.002;

    // ===== Целевая позиция =====
    public static double liftTargetPosition = 0;

    // Скорость изменения цели (триггерами)
    public static double liftTargetChangeSpeed = 1000; // ticks per second

    // ===== Приватные поля =====
    private DcMotorEx leftLift;
    private DcMotorEx rightLift;

    private PIDController leftPID = new PIDController(kP, kI, kD);
    private PIDController rightPID = new PIDController(kP, kI, kD);

    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {

        // ===== Инициализация моторов =====
        leftLift = hardwareMap.get(DcMotorEx.class, "Llift");
        rightLift = hardwareMap.get(DcMotorEx.class, "Rlift");

        leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
        rightLift.setDirection(DcMotorSimple.Direction.REVERSE);

        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {

            // Обновляем PID параметры каждый цикл (для Dashboard)
            leftPID.setPID(kP, kI, kD);
            rightPID.setPID(kP, kI, kD);

            // ===== Управление целевой позицией через триггеры =====
            double deltaTime = timer.seconds();
            timer.reset();

            double triggerInput = gamepad2.right_trigger - gamepad2.left_trigger;
            liftTargetPosition += triggerInput * liftTargetChangeSpeed * deltaTime;

            // Ограничение целевой позиции (например, 0–3000)
            liftTargetPosition = Math.max(0, Math.min(liftTargetPosition, 3000));

            // ===== Получение текущих позиций =====
            double leftPos = leftLift.getCurrentPosition();
            double rightPos = rightLift.getCurrentPosition();

            // ===== Расчёт PID =====
            double leftPower = leftPID.calculate(leftPos, liftTargetPosition);
            double rightPower = rightPID.calculate(rightPos, liftTargetPosition);

            // ===== Anti-deadzone (опционально) =====
            leftPower = applyMinimumPower(leftPower, 0.05);
            rightPower = applyMinimumPower(rightPower, 0.05);

            // ===== Применение мощности =====
            leftLift.setPower(leftPower);
            rightLift.setPower(rightPower);

            // ===== Телеметрия =====
            telemetry.addData("Target", liftTargetPosition);
            telemetry.addData("Left Pos", leftPos);
            telemetry.addData("Right Pos", rightPos);
            telemetry.addData("Left Power", leftPower);
            telemetry.addData("Right Power", rightPower);
            telemetry.update();
        }
    }

    private double applyMinimumPower(double power, double min) {
        if (Math.abs(power) < min && Math.abs(liftTargetPosition - leftLift.getCurrentPosition()) > 10) {
            return Math.copySign(min, power);
        }
        return power;
    }
}
