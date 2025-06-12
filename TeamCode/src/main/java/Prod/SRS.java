//package Prod;
//
//
//import com.acmerobotics.dashboard.config.Config;
//import com.arcrobotics.ftclib.gamepad.GamepadEx;
//import com.arcrobotics.ftclib.gamepad.GamepadKeys;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.Gamepad;
//
//import Controllers.ActionsController;
//import Controllers.OuttakeController;
//
//@Config
//@TeleOp(name = "SRS",group = "Prod")
//public class SRS extends LinearOpMode {
//    private ActionsController actionsController;
//    private OuttakeController outtakeController;
//    private OuttakeController servocontroller;
//    private GamepadEx driver1;
//
//    @Override
//    public void runOpMode(){
//
//        driver1 = new GamepadEx(gamepad1);
//        actionsController = new ActionsController(hardwareMap);
//        outtakeController = new OuttakeController();
//        servocontroller= new OuttakeController();
//
//        outtakeController.initialize(hardwareMap,"OuttakeClaw",true);
//        servocontroller.initialize(hardwareMap,"Servo1",false);
//        telemetry.addData("Status, ","Initialized");
//        waitForStart();
//        while (opModeIsActive()){
//            driver1.readButtons();
//
//            if (driver1.wasJustPressed(GamepadKeys.Button.B)){
//                outtakeController.setClawPosition(0);
//                servocontroller.setClawPosition(0);
//            }
//            else if(driver1.wasJustPressed(GamepadKeys.Button.A)){
//                outtakeController.setClawPosition(1);
//                servocontroller.setClawPosition(1);
//            }
//            else if(driver1.wasJustPressed(GamepadKeys.Button.X)){
//                outtakeController.setClawPosition(0.5);
//                servocontroller.setClawPosition(0.5);
//            }
//            actionsController.update();
//
//            telemetry.addData("Status", "Running");
//            telemetry.addData("Action Busy", actionsController.isBusy() ? "YES" : "NO");
//            telemetry.update();
//
//        }
//
//    }
//
//}
