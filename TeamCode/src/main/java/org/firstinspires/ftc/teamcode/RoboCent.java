package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class RoboCent extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private Servo claw;
    private DcMotor armLeft;
    private DcMotor armRight;

    int armLeftUp = -1550;
    int armLeftBar = -1000;
    int armLeftDown = 1;
    int armRightUp = 1550;
    int armRightBar = 1000;
    int armRightDown = -1;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        armLeft = hardwareMap.get(DcMotor.class, "slides_left");
        armRight = hardwareMap.get(DcMotor.class, "slides_right");

        claw = hardwareMap.get(Servo.class, "claw");

        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armLeft.setTargetPosition(armLeftDown);
        armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setTargetPosition(armRightDown);
        armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        claw.setPosition(0.0);

        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("mecFrontLeft");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("mecBackLeft");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("mecFrontRight");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("mecBackRight");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            // slides
            if (gamepad2.y) {
                armLeft.setTargetPosition(armLeftUp);
                armRight.setTargetPosition(armRightUp);
                armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLeft.setPower(0.5);
                armRight.setPower(0.5);
            }

            if (gamepad2.b) {
                armLeft.setTargetPosition(armLeftDown);
                armRight.setTargetPosition(armRightDown);
                armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLeft.setPower(0.5);
                armRight.setPower(0.5);
            }

            if (gamepad2.start) {
                armLeft.setTargetPosition(armLeftBar);
                armRight.setTargetPosition(armRightBar);
                armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armLeft.setPower(0.5);
                armRight.setPower(0.5);
            }

            telemetry.addData("Status", "Run Time: " + runtime.toString());


            telemetry.addData("Left Power", armLeft.getPower());
            telemetry.addData("Right Power", armRight.getPower());

            telemetry.addData("Left Pos", armLeft.getCurrentPosition());
            telemetry.addData("Right Pos", armRight.getCurrentPosition());

            telemetry.addData("Left Target", armLeft.getTargetPosition());
            telemetry.addData("Right Target", armRight.getTargetPosition());

            telemetry.update();
        }
    }
}