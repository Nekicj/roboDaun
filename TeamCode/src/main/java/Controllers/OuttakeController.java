package Controllers;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class OuttakeController {
    private Servo claw = null;

    public enum Servos{
        CLAW_OPEN(0.7),
        CLAW_CLOSE(0.5);

        private final double position;
        Servos(double pos) {this.position = pos;}


        public double getPos() {return position;}

    }

    public void initialize(HardwareMap hardwareMap){
        initialize(hardwareMap,false);
    }

    public void initialize(HardwareMap hardwareMap, boolean isClawOpen){
        claw = hardwareMap.get(Servo.class,"OuttakeClaw");
        claw.setDirection(Servo.Direction.FORWARD);

        if (!isClawOpen) claw.setPosition(Servos.CLAW_OPEN.getPos());

    }

    public void setClawOpen(){
        claw.setPosition(Servos.CLAW_OPEN.getPos());
    }

    public void setClawClose(){
        claw.setPosition(Servos.CLAW_CLOSE.getPos());
    }



    public double getPosition(){
        return claw.getPosition();
    }


}
