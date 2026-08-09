package org.firstinspires.ftc.teamcode.Robot.Subsystems;

import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Utils.Constants.IntakeConstants;

public class Intake implements Subsystem {
    DcMotorEx intakeMotor, transferMotor;

    IntakeState state;

    public Intake(DcMotorEx intakeMotor, DcMotorEx transferMotor){
        this.intakeMotor = intakeMotor;
        this.transferMotor = transferMotor;
    }

    public IntakeState getState(){
        return state;
    }

    public void setPower(double power, double powerT){
        intakeMotor.setPower(power);
        transferMotor.setPower(powerT);

    }

    public void setState(IntakeState state){
        this.state = state;
        switch (state){
            case ON:
                setPower(IntakeConstants.intakePowerON, IntakeConstants.intakePowerON);
                break;
            case OFF:
                setPower(IntakeConstants.intakePowerOFF, IntakeConstants.intakePowerOFF);
                break;
            case REVERSE:
                setPower(IntakeConstants.intakePowerREV, IntakeConstants.intakePowerREV);
                break;
        }
    }


    @Override
    public void periodic(){
        setState(state);
    }

    public enum IntakeState{
        ON,
        OFF,
        REVERSE
    }
}
