package Controllers;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class OuttakeController {
    private Servo claw = null;
    private Servo clawRotate = null;
    private Servo armLeft = null;
    private Servo armRight = null;

    public static enum Servos{
        OUTTAKE_TAKE_SPECIMEN(0.85),
        OUTTAKE_PUSH_SPECIMEN(0.2),

        CLAW_OPEN(0.65),
        CLAW_CLOSE(0.82),

        CLAW_ROTATE_TAKE_SPECIMEN(0.36),
        CLAW_ROTATE_PUSH_SPECIMEN(0.2),

        CLAW_ROTATE_TRANSFER(0.25);



        private final double position;
        Servos(double pos) {this.position = pos;}


        public double getPos() {return position;}

    }

    public void initialize(HardwareMap hardwareMap, String clawServoName,String clawRotateServo,String outtakeArmLeft,String outtakeArmRight){
        initialize(hardwareMap,clawServoName,clawRotateServo,outtakeArmLeft,outtakeArmRight,false);
    }

    public void initialize(HardwareMap hardwareMap,String clawServoName,String clawRotateServo,String outtakeArmLeft,String outtakeArmRight,boolean isClawOpen){
        claw = hardwareMap.get(Servo.class,clawServoName);

        clawRotate = hardwareMap.get(Servo.class,clawRotateServo);

        armLeft =  hardwareMap.get(Servo.class,outtakeArmLeft);
        armRight = hardwareMap.get(Servo.class,outtakeArmRight);

        armLeft.setDirection(Servo.Direction.REVERSE);
        armRight.setDirection(Servo.Direction.FORWARD);


        if (!isClawOpen) claw.setPosition(Servos.CLAW_CLOSE.getPos());

    }

    public void setClawOpen(){
        claw.setPosition(Servos.CLAW_OPEN.getPos());
    }

    public void setClawClose(){
        claw.setPosition(Servos.CLAW_CLOSE.getPos());
    }

    public void setOuttakeToTake(){
        armLeft.setPosition(Servos.OUTTAKE_TAKE_SPECIMEN.getPos());
        armRight.setPosition(Servos.OUTTAKE_TAKE_SPECIMEN.getPos());
    }

    public void setOuttakeToPush(){
        armLeft.setPosition(Servos.OUTTAKE_PUSH_SPECIMEN.getPos());
        armRight.setPosition(Servos.OUTTAKE_PUSH_SPECIMEN.getPos());
    }

    public void setClawRotateToTake(){
        clawRotate.setPosition(Servos.CLAW_ROTATE_TAKE_SPECIMEN.getPos());
    }

    public void setClawRotateToPush(){
        clawRotate.setPosition(Servos.CLAW_ROTATE_PUSH_SPECIMEN.getPos());
    }

    public void setOuttakeToTransfer(){
        armLeft.setPosition(Servos.OUTTAKE_PUSH_SPECIMEN.getPos());
        armRight.setPosition(Servos.OUTTAKE_PUSH_SPECIMEN.getPos());
        clawRotate.setPosition(Servos.CLAW_ROTATE_TRANSFER.getPos());
    }


    public void setClawPosition(double servoPosition){claw.setPosition(servoPosition);}
    public double getPosition(){
        return claw.getPosition();
    }


}
