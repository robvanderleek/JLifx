package io.github.robvanderleek.jlifx.packet;

import io.github.robvanderleek.jlifx.common.MacAddress;

import java.util.Random;

public class SetMultiZoneEffect extends Packet {

    Random random = new Random();

    public SetMultiZoneEffect(MacAddress macAddress, Type type, int speed, long duration,
                              SpeedDirection speedDirection) {
        setTargetMac(macAddress);
        setType(new byte[]{(byte) 0xFC, (byte) 0x01});

        byte[] instanceid = intToBytes(random.nextInt(Integer.MAX_VALUE));
        byte[] speedBytes = intToBytes(speed);
        byte[] durationBytes = longToBytes(duration);
        byte[] payload = new byte[]{instanceid[3], instanceid[2], instanceid[1], instanceid[0],
                (byte) (type == Type.OFF ? 0x00 : 0x01), //type
                0x00, 0x00, //reserved
                speedBytes[3], speedBytes[2], speedBytes[1], speedBytes[0], //speed
                durationBytes[7], durationBytes[6], durationBytes[5], durationBytes[4], //duration 0
                durationBytes[3], durationBytes[2], durationBytes[1], durationBytes[0], //duration 1
                0x00, 0x00, 0x00, 0x00, //reserved
                0x00, 0x00, 0x00, 0x00, //reserved
                0x00, 0x00, 0x00, 0x00, //parameter 0
                (byte) (speedDirection == SpeedDirection.TOWARDS_CONTROLLER ? 0x00 : 0x01), 0x00, 0x00, 0x00,
                //parameter 1
                0x00, 0x00, 0x00, 0x00, //parameter 2
                0x00, 0x00, 0x00, 0x00, //parameter 3
                0x00, 0x00, 0x00, 0x00, //parameter 4
                0x00, 0x00, 0x00, 0x00, //parameter 5
                0x00, 0x00, 0x00, 0x00, //parameter 6
                0x00, 0x00, 0x00, 0x00  //parameter 7
        };

        setPayload(payload);
    }

    public enum Type {
        OFF, MOVE
    }

    public enum SpeedDirection {
        TOWARDS_CONTROLLER, AWAY_CONTROLLER
    }

}
