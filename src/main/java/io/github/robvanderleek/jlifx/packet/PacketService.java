package io.github.robvanderleek.jlifx.packet;

import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.bulb.BulbMeshFirmwareStatus;
import io.github.robvanderleek.jlifx.common.Color;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PacketService {
    private static final Log LOG = LogFactory.getLog(PacketService.class);

    public void sendPowerManagementPacket(Bulb bulb, boolean on) throws IOException {
        Packet packet = new PowerManagementPacket(bulb.getMacAddress(), on);
        bulb.sendPacket(packet);
    }

    public void sendColorManagementPacket(Bulb bulb, Color color, int fadetime, float brightness) throws IOException {
        Packet packet = new ColorManagementPacket(bulb.getMacAddress(), color, fadetime, brightness);
        bulb.sendPacket(packet);
    }

    public void sendSetDimAbsolutePacket(Bulb bulb, float brightness) throws IOException {
        Packet packet = new SetDimAbsolutePacket(brightness);
        bulb.sendPacket(packet);
    }

    public Optional<StatusResponsePacket> sendStatusRequestPacket(Bulb bulb) {
        Packet packet = new StatusRequestPacket();
        try {
            List<Packet> responsePackets = bulb.sendPacketAndGetResponses(packet);
            return responsePackets.stream().map(StatusResponsePacket::new)
                                  .filter(p -> p.getType() == StatusResponsePacket.TYPE).findFirst();
        } catch (IOException e) {
            LOG.error(e);
            return Optional.empty();
        }
    }

    public Optional<GroupResponsePacket> sendGroupRequestPacket(Bulb bulb) {
        Packet packet = new GroupRequestPacket();
        try {
            List<Packet> responsePackets = bulb.sendPacketAndGetResponses(packet);
            return responsePackets.stream().map(GroupResponsePacket::new)
                                  .filter(p -> p.getType() == GroupResponsePacket.TYPE).findFirst();
        } catch (IOException e) {
            LOG.error(e);
            return Optional.empty();
        }
    }

    void sendWifiInfoRequestPacket(Bulb bulb) throws IOException {
        Packet packet = new WifiInfoRequestPacket();
        bulb.sendPacketAndGetResponse(packet);
    }

    public void sendSetColorZonePacket(Bulb bulb, int fadetime, short firstZone, short lastZone, Color color,
                                       float brightness, Apply apply) throws IOException {
        Packet packet =
                new SetColorZones(bulb.getMacAddress(), firstZone, lastZone, color, fadetime, apply, brightness);
        bulb.sendPacket(packet);
    }

    public void sendMultiZoneEffectPacket(Bulb bulb, SetMultiZoneEffect.Type type, int speed, long duration,
                                          SetMultiZoneEffect.SpeedDirection speedDirection) throws IOException {
        Packet packet = new SetMultiZoneEffect(bulb.getMacAddress(), type, speed, duration, speedDirection);
        bulb.sendPacket(packet);
    }

    public BulbMeshFirmwareStatus getMeshFirmwareStatus(Bulb bulb) throws IOException {
        Packet packet = new MeshFirmwareRequestPacket();
        Packet responsePacket = bulb.sendPacketAndGetResponse(packet);
        return BulbMeshFirmwareStatus.fromPacket(responsePacket);
    }

    public enum Apply {
        NO_APPLY, APPLY, APPLY_ONLY
    }

}