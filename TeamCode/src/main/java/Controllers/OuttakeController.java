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
        OUTTAKE_TAKE_SPECIMEN(0.3),
        OUTTAKE_PUSH_SPECIMEN(0.7),

        CLAW_OPEN(0),
        CLAW_CLOSE(1),

        CLAW_ROTATE_TAKE_SPECIMEN(0.3),
        CLAW_ROTATE_PUSH_SPECIMEN(0.7);



        private final double position;
        Servos(double pos) {this.position = pos;}


        public double getPos() {return position;}

    }

    public void initialize(HardwareMap hardwareMap, String clawServoName,String clawRotateServo,String outtakeArmLeft,String outtakeArmRight,boolean isReversed){
        initialize(hardwareMap,clawServoName,clawRotateServo,outtakeArmLeft,outtakeArmRight,isReversed,false);
    }

    public void initialize(HardwareMap hardwareMap,String clawServoName,String clawRotateServo,String outtakeArmLeft,String outtakeArmRight,boolean isReversed ,boolean isClawOpen){
        claw = hardwareMap.get(Servo.class,clawServoName);

        clawRotate = hardwareMap.get(Servo.class,clawRotateServo);

        armLeft =  hardwareMap.get(Servo.class,outtakeArmLeft);
        armRight = hardwareMap.get(Servo.class,outtakeArmRight);

        if (isReversed){claw.setDirection(Servo.Direction.REVERSE);}else {claw.setDirection(Servo.Direction.FORWARD);}


        if (!isClawOpen) claw.setPosition(Servos.CLAW_CLOSE.getPos());

    }

    public void setClawOpen(){
        claw.setPosition(Servos.CLAW_OPEN.getPos());
    }

    public void setClawClose(){
        claw.setPosition(Servos.CLAW_CLOSE.getPos());
    }


    public void setClawPosition(double servoPosition){claw.setPosition(servoPosition);}
    public double getPosition(){
        return claw.getPosition();
    }


}
