package org.firstinspires.ftc.teamcode.Robot.Subsystems;

import static org.firstinspires.ftc.teamcode.Utils.Constants.ShooterConstants.errorThreshold;
import static org.firstinspires.ftc.teamcode.Utils.Constants.ShooterConstants.targetRPM;

import com.aaravdhawan25.pidautotuner.PIDFController;
import com.arcrobotics.ftclib.command.Subsystem;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Utils.Constants.CameraConstants;
import org.firstinspires.ftc.teamcode.Utils.Constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.Utils.MyTelem;

public class Shooter implements Subsystem {

    public DcMotorEx shooterOne, shooterTwo;
    public Servo hoodServo;
    PIDController shooterController;

    ShooterState state  = ShooterState.IDLE;

    public Shooter(DcMotorEx shooterOne, DcMotorEx shooterTwo, Servo hoodServo){
        this.hoodServo = hoodServo;
        this.shooterOne = shooterOne;
        this.shooterTwo = shooterTwo;
    }


    public void setState(ShooterState state){
        this.state = state;
        switch (state){
            case IDLE:
                setHood(ShooterConstants.HOOD_MIN);
                setShooterPID(ShooterConstants.idleRPM);
                break;
            case CLOSE:
                setHood(ShooterConstants.HOOD_MAX);
                setShooterPID(ShooterConstants.CLOSE_RPM);
                break;
            case MATH:
                setHood(getHood(CameraConstants.distanceToGoalLL));
                calculateRPM(CameraConstants.distanceToGoalLL);
                break;
        }

    }

    @Override
    public void periodic(){
        setState(state);
    }


    public double calculateRPM(double distance){
        return -0.105746 * Math.pow(distance, 2) +32.33112*distance +1558.21332;
    }

    public ShooterState getState(){
        return state;
    }

    public double toRPM(double tps){
        return tps * 60.0 / ShooterConstants.TICKS_PER_REV;
    }



    public void setShooterPID(double rpm){
        double velocity = Math.abs(shooterOne.getVelocity());
        double currentRPM = (toRPM(velocity));

        shooterController.setPID(ShooterConstants.kP, ShooterConstants.kI, ShooterConstants.kD);
        double power = shooterController.calculate(currentRPM, rpm);
        power += (rpm > 0) ? (ShooterConstants.kF * (rpm / ShooterConstants.MAX_RPM)) : 0.0;
        power = Range.clip(power, 0, 1);

        shooterOne.setPower(power);
        shooterTwo.setPower(power);

        MyTelem.addData("Shooter Current RPM", currentRPM);
        MyTelem.addData("Shooter Target RPM", rpm);
    }



    public void setHood(double pos){
        hoodServo.setPosition(pos);
    }

    public boolean atTargetSpeed(){
        return Math.abs(targetRPM - getShooterRPM()) < errorThreshold && state != ShooterState.IDLE;
    }

    public double getShooterRPM() {
        double velocity = shooterOne.getVelocity();
        return (velocity * 60.0) / ShooterConstants.TICKS_PER_REV;
    }

    public double getHood(double dist){
        return dist < 100 ? ShooterConstants.HOOD_MAX : ShooterConstants.HOOD_MID;
    }

    public enum ShooterState{
        CLOSE,
        MATH,
        IDLE
    }

}
