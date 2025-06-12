package Controllers;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class IntakeController {
    private Servo intakeClaw = null;
    private Servo clawRotate = null;
    private Servo intakeRotate = null;
    private Servo intakeArm = null;
    private Servo intakeKrutilka = null;

    public static double INTAKE_ARM_TRANSFER_A = 1;
    public static double INTAKE_KRUTILKA_TRANSFER_A = 0.65;

    public static enum Servos{
        CLAW_OPEN(0.4),
        CLAW_CLOSE(0.3),

        INTAKE_ARM_AIM(0.72),
        INTAKE_KRUTILKA_AIM(0.1),

        INTAKE_ARM_TAKE(0.67),
        INTAKE_KRUTILKA_TAKE(0.08),

        INTAKE_ARM_TRANSFER(INTAKE_ARM_TRANSFER_A),
        INTAKE_KRUTILKA_TRANSFER(INTAKE_KRUTILKA_TRANSFER_A);




        private final double position;
        Servos(double pos) {this.position = pos;}


        public double getPos() {return position;}

    }

    public void initialize(HardwareMap hardwareMap,String clawServoName,String clawRotateName,String intakeRotateName,String intakeArmName,String intakeKrutilkaName){
        initialize(hardwareMap,clawServoName, clawRotateName,intakeRotateName,intakeArmName,intakeKrutilkaName,false);
    }

    public void initialize(HardwareMap hardwareMap,String clawServoName, String clawRotateName,String intakeRotateName,String intakeArmName,String intakeKrutilkaName,boolean isClawOpen){
        intakeClaw = hardwareMap.get(Servo.class,clawServoName);

        clawRotate = hardwareMap.get(Servo.class,clawRotateName);

        intakeRotate = hardwareMap.get(Servo.class,intakeRotateName);

        intakeArm =  hardwareMap.get(Servo.class,intakeArmName);

        intakeKrutilka = hardwareMap.get(Servo.class,intakeKrutilkaName);


        if (!isClawOpen) intakeClaw.setPosition(Servos.CLAW_CLOSE.getPos());

    }

    public void setClawOpen(){
        intakeClaw.setPosition(Servos.CLAW_OPEN.getPos());
    }

    public void setClawClose(){
        intakeClaw.setPosition(Servos.CLAW_CLOSE.getPos());
    }

    public void setIntakeAim(){
        intakeArm.setPosition(Servos.INTAKE_ARM_AIM.getPos());
        intakeKrutilka.setPosition(Servos.INTAKE_KRUTILKA_AIM.getPos());
    }

    public void setIntakeTake(){
        intakeArm.setPosition(Servos.INTAKE_ARM_TAKE.getPos());
        intakeKrutilka.setPosition(Servos.INTAKE_KRUTILKA_TAKE.getPos());

    }

    public void setIntakeToTransfer(){
        intakeArm.setPosition(Servos.INTAKE_ARM_TRANSFER.getPos());
        intakeKrutilka.setPosition(Servos.INTAKE_KRUTILKA_TRANSFER.getPos());
    }



    public void setClawPosition(double servoPosition){intakeClaw.setPosition(servoPosition);}
    public double getPosition(){
        return intakeClaw.getPosition();
    }


}
