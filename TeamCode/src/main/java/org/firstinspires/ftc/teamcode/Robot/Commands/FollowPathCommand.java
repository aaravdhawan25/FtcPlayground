package org.firstinspires.ftc.teamcode.Robot.Commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandGroupBase;
import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.PathChain;

public class FollowPathCommand extends CommandBase {

    private Follower follower;
    private PathChain path;

    private boolean holdEnd = true;
    public FollowPathCommand(Follower follower, PathChain path){
        this.follower = follower;
        this.path = path;
    }

    @Override
    public void initialize() {
        follower.followPath(path, holdEnd);
    }

    @Override
    public boolean isFinished(){
        return !follower.isBusy();
    }
}
