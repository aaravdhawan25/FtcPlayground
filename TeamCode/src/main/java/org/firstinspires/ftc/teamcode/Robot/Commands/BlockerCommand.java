package org.firstinspires.ftc.teamcode.Robot.Commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.Robot.MyRobot;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Blocker;

public class BlockerCommand extends SequentialCommandGroup {
    public BlockerCommand(MyRobot robot, Blocker.BlockerState state){
        addCommands(
                new InstantCommand(()-> robot.blocker.setState(state), robot.blocker)
        );
    }
}
