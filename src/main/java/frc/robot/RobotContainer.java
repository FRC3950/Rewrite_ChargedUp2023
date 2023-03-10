package frc.robot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.*;
import frc.robot.autos.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    /* Controllers */
    private final Joystick driver = new Joystick(0);
    private final Joystick manipulate = new Joystick(1);
    //private final Joystick buttonBox = new Joystick(1);

    /* Drive Controls */
    private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;

    /* Driver Buttons */
    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kRightBumper.value);
    private final JoystickButton startHorizontalDrive = new JoystickButton(driver, XboxController.Button.kBack.value);

    //private final JoystickButton testButton = new JoystickButton(buttonBox, 12);

    /* Subsystems */
    private final Swerve s_Swerve = new Swerve();
    private final Intake s_Intake = new Intake();
    private final Telescope s_Telescope = new Telescope();
    private final Wrist s_Wrist = new Wrist();
    private final ArmSubsystem s_Arm = new ArmSubsystem();

    /* Commands */
    private final AutoBalanceCommand balanceCommand = new AutoBalanceCommand(s_Swerve);
    private final exampleAuto exampleAuto = new exampleAuto(s_Swerve);
    private final Command a = s_Arm.zeroSensorFalcons();

    private final SequentialCommandGroup armToAngle = new ArmToAngleGroup(s_Arm);

    
    

    /* Auto Commands */
    private SendableChooser<Command> autoChooser = new SendableChooser<>();




    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {

        s_Swerve.setDefaultCommand(
                new TeleopSwerve(
                        s_Swerve,
                        () -> -driver.getRawAxis(translationAxis),
                        () -> -driver.getRawAxis(strafeAxis),
                        () -> -driver.getRawAxis(rotationAxis),
                        () -> robotCentric.getAsBoolean()));


       s_Telescope.setDefaultCommand(new TelescopeBangBang(s_Telescope, () -> manipulate.getRawAxis(5)));
       // s_Arm.setDefaultCommand(new ArmPercentCommand(s_Arm, () -> manipulate.getRawAxis(4)));
        s_Wrist.setDefaultCommand(new WristPercentCommand(s_Wrist, () -> manipulate.getRawAxis(1)));

        // Configure the button bindings
        configureButtonBindings();

        // Autochooser
        createAllAutoPathCommandsBasedOnPathDirectory();
        autoChooser.addOption("Example S Curve", exampleAuto);
        SmartDashboard.putData("Auto Selection", autoChooser);

       
        SmartDashboard.putData(s_Arm);
        SmartDashboard.putData("Akjkjtuo Balance", balanceCommand);
        SmartDashboard.putData("Move arm to pos", new InstantCommand(() -> s_Arm.moveArmToPostionCommand()));

        SmartDashboard.putData("lock arm", new InstantCommand(s_Arm::lockArm));
        SmartDashboard.putData("open arm",new InstantCommand(s_Arm::unlockArm));

        SmartDashboard.putData("reset Encoders For Arm", new InstantCommand(s_Arm::resetEncoderCountArmMotors));

        SmartDashboard.putData("toggle telescope brake", new InstantCommand(s_Telescope::toggleBrake));

        SmartDashboard.putData("RiseArm", armToAngle);

    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
     * it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        //testButton.onTrue(new InstantCommand(() -> System.out.println("Button pressed")));
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro())); //Button this way might be 'safer' since the button is private/final when defined outside the constructor. 
        //Honestly not sure what the best practice is or if it matters. Probably would never collide if we kept it public/changeable... idk?

        new JoystickButton(driver, XboxController.Button.kB.value) // Should eventually do all buttons like this?
            .whileTrue(balanceCommand);

         new JoystickButton(driver, XboxController.Button.kLeftBumper.value)
             .onTrue(new runPathAuto(s_Swerve, Constants.PathPlannerSimpleTrajectories.advanceNorth_22inches));

        

           

        startHorizontalDrive.whileTrue(s_Swerve.driveHorizontalCommand());
            //This demonstrates Instance Command FActory Methods - it's cool :D
            //It turns to Zero Heading, might need to add PID or change to CLOSED LOOP
        new JoystickButton(driver, XboxController.Button.kA.value)
            .whileTrue(s_Swerve.turnToZeroCommand());

       
        // new JoystickButton(manipulate, XboxController.Button.kA.value)
        //     .whileTrue(new PIDCommand(
        //         new PIDController(s_Wrist.getPIDDashboardConstants()[0], s_Wrist.getPIDDashboardConstants()[1], s_Wrist.getPIDDashboardConstants()[2]), 
        //         s_Wrist::getWristEncoder, 
        //         () -> Constants.kIntake.encoderLimit, 
        //         (output) -> {s_Wrist.setSpeed(output);}, s_Wrist
        //     )
        // ); Example of an inline PID command 

        new JoystickButton(manipulate, XboxController.Button.kB.value)
            .onTrue(new WristPIDCommand(s_Wrist, 0)
        );
        new JoystickButton(manipulate, XboxController.Button.kA.value)
            .onTrue(new WristPIDCommand(s_Wrist, Constants.kIntake.encoderLimit)
        );
        new JoystickButton(manipulate, XboxController.Button.kX.value)
            .whileTrue(new StartEndCommand(() -> s_Intake.setIntake(0.75),  () -> s_Intake.setIntake(0.0), s_Intake));
        
        new JoystickButton(manipulate, XboxController.Button.kY.value)
            .whileTrue(new StartEndCommand(() -> s_Intake.setIntake(-0.75), () -> s_Intake.setIntake(0.0), s_Intake));
        
        new JoystickButton(manipulate, XboxController.Button.kRightBumper.value)
            .onTrue(new InstantCommand(s_Intake::toggleSolenoid, s_Intake));

    }

    /**
     * This method creates a {@link runPathAuto} command for each saved path and
     * adds the command to autoChooser for selection.
     */
    public void createAllAutoPathCommandsBasedOnPathDirectory() {

        File folder = new File(Filesystem.getDeployDirectory() + "/pathplanner/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().split("\\.") != null) {
                    PathPlannerTrajectory loadedTrajectory = PathPlanner.loadPath(file.getName().split("\\.")[0],
                            new PathConstraints(3, 3));
                    autoChooser.addOption("A_" + file.getName().split("\\.")[0],
                            new runPathAuto(s_Swerve, loadedTrajectory));
                }
            }
        }
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        // return new exampleAuto(s_Swerve);
        return autoChooser.getSelected();
    }
}
