package org.firstinspires.ftc.teamcode.Robot.Subsystems;

import com.arcrobotics.ftclib.command.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utils.Constants.BlockerConstants;

public class Blocker implements Subsystem {
    Servo blockerServo;

    BlockerState state;

    public Blocker(Servo blockerServo){
        this.blockerServo = blockerServo;
    }

    public void setPos(double pos){
        blockerServo.setPosition(pos);
    }

    public BlockerState getState(){
        return state;
    }

    public void setState(BlockerState state){
        this.state = state;
        switch (state){
            case OPEN:
                setPos(BlockerConstants.blockerOpen);
                break;
            case CLOSED:
                setPos(BlockerConstants.blockerClosed);
                break;
        }
    }

    @Override
    public void periodic(){
        setState(state);
    }

    public enum BlockerState{
        CLOSED,
        OPEN
    }

}
