package frc.robot.commands.PathPlannerCommands;

import java.util.HashMap;
import java.util.List;
import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.RamseteAutoBuilder;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class PathPlannerLoadEventMapCommand extends InstantCommand {
  private final String filename;
  private final HashMap<String, Command> eventmap;

  public PathPlannerLoadEventMapCommand(String fileName, HashMap<String, Command> eventMap) {
    this.filename = fileName;
    this.eventmap = eventMap;
  }

  @Override
  public void initialize() {
    List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup(filename, new PathConstraints(2, 2));

    HashMap<String, Command> eventMap = eventmap;

    SwerveAutoBuilder  autoBuilder = new SwerveAutoBuilder (
        RobotContainer.swerveDrive::getPose,
        RobotContainer.swerveDrive::resetOdometry,
        Constants.DriveConstants.kDriveKinematics,
        new PIDConstants(5.0, 0.0, 0.0), // PID constants to correct for translation error (used to create the X and Y PID controllers)
        new PIDConstants(0.5, 0.0, 0.0), // PID constants to correct for rotation error (used to create the rotation controller)
        RobotContainer.swerveDrive::setModuleStates,
        eventMap,
        true,
        RobotContainer.swerveDrive);

    //RamseteAutoBuilder autoBuilder2 = new RamseteAutoBuilder(null, null, null, null, null, null, null, null, eventMap, null)

    autoBuilder.fullAuto(pathGroup).schedule();
  }
}
