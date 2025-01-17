package frc.robot;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;

public final class CTREConfigs {
    public TalonFXConfiguration swerveAngleFXConfig;
    public TalonFXConfiguration swerveDriveFXConfig;
    public CANCoderConfiguration swerveCanCoderConfig;

    public CTREConfigs(){
        swerveAngleFXConfig = new TalonFXConfiguration();
        swerveDriveFXConfig = new TalonFXConfiguration();
        swerveCanCoderConfig = new CANCoderConfiguration();

        /* Swerve Angle Motor Configurations */
        SupplyCurrentLimitConfiguration angleSupplyLimit = new SupplyCurrentLimitConfiguration(
            Constants.kSwerve.angleEnableCurrentLimit, 
            Constants.kSwerve.angleContinuousCurrentLimit, 
            Constants.kSwerve.anglePeakCurrentLimit, 
            Constants.kSwerve.anglePeakCurrentDuration);

        swerveAngleFXConfig.slot0.kP = Constants.kSwerve.angleKP;
        swerveAngleFXConfig.slot0.kI = Constants.kSwerve.angleKI;
        swerveAngleFXConfig.slot0.kD = Constants.kSwerve.angleKD;
        swerveAngleFXConfig.slot0.kF = Constants.kSwerve.angleKF;
        swerveAngleFXConfig.supplyCurrLimit = angleSupplyLimit;

        /* Swerve Drive Motor Configuration */
        SupplyCurrentLimitConfiguration driveSupplyLimit = new SupplyCurrentLimitConfiguration(
            Constants.kSwerve.driveEnableCurrentLimit, 
            Constants.kSwerve.driveContinuousCurrentLimit, 
            Constants.kSwerve.drivePeakCurrentLimit, 
            Constants.kSwerve.drivePeakCurrentDuration);

        swerveDriveFXConfig.slot0.kP = Constants.kSwerve.driveKP;
        swerveDriveFXConfig.slot0.kI = Constants.kSwerve.driveKI;
        swerveDriveFXConfig.slot0.kD = Constants.kSwerve.driveKD;
        swerveDriveFXConfig.slot0.kF = Constants.kSwerve.driveKF;        
        swerveDriveFXConfig.supplyCurrLimit = driveSupplyLimit;
        swerveDriveFXConfig.openloopRamp = Constants.kSwerve.openLoopRamp;
        swerveDriveFXConfig.closedloopRamp = Constants.kSwerve.closedLoopRamp;
        
        /* Swerve CANCoder Configuration */
        swerveCanCoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        swerveCanCoderConfig.sensorDirection = Constants.kSwerve.canCoderInvert;
        swerveCanCoderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
    }
}