package Controllers;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class OuttakeController {
    private Servo claw = null;

    public enum Servos{
        CLAW_OPEN(0),
        CLAW_CLOSE(1);

        private final double position;
        Servos(double pos) {this.position = pos;}


        public double getPos() {return position;}

    }

    public void initialize(HardwareMap hardwareMap, String servoName,boolean isReversed){
        initialize(hardwareMap,servoName,isReversed,false);
    }

    public void initialize(HardwareMap hardwareMap,String servoName,boolean isReversed ,boolean isClawOpen){
        claw = hardwareMap.get(Servo.class,servoName);
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
