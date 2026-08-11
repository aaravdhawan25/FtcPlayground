package org.firstinspires.ftc.teamcode.Robot.Commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.Robot.Robot;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.LLCam;

public class PointToGoalCommand extends SequentialCommandGroup {

    public PointToGoalCommand(Robot robot, LLCam.CamState state){
        addCommands(
                new InstantCommand(() -> robot.cam.setState(state), robot.cam)
        );
    }

}
