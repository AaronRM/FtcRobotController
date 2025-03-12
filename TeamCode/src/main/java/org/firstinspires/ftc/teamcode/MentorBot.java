package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "MentorBot", group = "Linear Opmode")
public class MentorBot extends LinearOpMode {
    @Override
    public void runOpMode() {
        CRServo liftLower, liftUpper;
        Servo clawLeft, clawRight;
        DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;

        // Initialize the hardware variables
        liftLower = hardwareMap.get(CRServo.class, "servo0"); // 0
        liftUpper = hardwareMap.get(CRServo.class, "servo1"); // 1
        clawLeft = hardwareMap.get(Servo.class, "servo2"); // 2
        clawRight = hardwareMap.get(Servo.class, "servo3"); // 3

        frontLeftDrive = hardwareMap.get(DcMotor.class, "mfl"); // 0
        frontRightDrive = hardwareMap.get(DcMotor.class, "mfr"); // 1
        backLeftDrive = hardwareMap.get(DcMotor.class, "mbl"); // 2
        backRightDrive = hardwareMap.get(DcMotor.class, "mbr"); // 3

        // Set the direction of the motors
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Use gamepad1 left stick y-axis to control the left side motors
            double leftPower = -gamepad1.left_stick_y;

            // Use gamepad1 right stick y-axis to control the right side motors
            double rightPower = -gamepad1.right_stick_y;

            // Set the power to the motors
            frontLeftDrive.setPower(leftPower);
            backLeftDrive.setPower(leftPower);
            frontRightDrive.setPower(rightPower);
            backRightDrive.setPower(rightPower);

            // Use gamepad2 left stick y-axis to control liftLower
            double liftLowerSpeed = -gamepad2.left_stick_y;
            liftLower.setPower(liftLowerSpeed);

            // Use gamepad2 right stick y-axis to control liftUpper
            double liftUpperSpeed = -gamepad2.right_stick_y;
            liftUpper.setPower(liftUpperSpeed);

            // Use gamepad2 left stick x-axis to control clawLeft
            double clawLeftPosition = (gamepad2.left_stick_x + 1) / 2; // Convert from -1 to 1 range to 0 to 1 range
            clawLeft.setPosition(clawLeftPosition);

            // Use gamepad2 right stick x-axis to control clawRight
            double clawRightPosition = (gamepad2.right_stick_x + 1) / 2; // Convert from -1 to 1 range to 0 to 1 range
            clawRight.setPosition(clawRightPosition);

            // Show the current motor powers on the telemetry
            telemetry.addData("Front Left Drive Power", leftPower);
            telemetry.addData("Back Left Drive Power", leftPower);
            telemetry.addData("Front Right Drive Power", rightPower);
            telemetry.addData("Back Right Drive Power", rightPower);

            // Show the current servo positions on the telemetry
            telemetry.addData("Lift Lower Speed", liftLowerSpeed);
            telemetry.addData("Lift Upper Speed", liftUpperSpeed);
            telemetry.addData("Claw Left Position", clawLeftPosition);
            telemetry.addData("Claw Right Position", clawRightPosition);

            telemetry.update();
        }
    }
}
