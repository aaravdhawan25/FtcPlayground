package org.firstinspires.ftc.teamcode.Robot.Commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.Robot.MyRobot;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Robot.Subsystems.Shooter;

public class IntakeCommand extends SequentialCommandGroup {
    public IntakeCommand(MyRobot robot, Intake.IntakeState state){
        addRequirements(robot.intake);
        addCommands(
                new InstantCommand(()->robot.intake.setState(state), robot.intake)
        );
    }
}
