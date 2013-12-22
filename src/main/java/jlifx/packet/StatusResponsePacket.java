package jlifx.packet;

public class StatusResponsePacket {
	private final Packet packet;

	public StatusResponsePacket(Packet packet) {
		this.packet = packet;
	}

	public byte[] getTargetMac() {
		return packet.getTargetMac();
	}

	public String getBulbName() {
		StringBuffer result = new StringBuffer();
		byte[] payload = packet.getPayload();
		if (payload.length > 16) {
			for (int i = 17; i < payload.length; i++) {
				if (payload[i] == 0x00)
					break;
				result.append((char) (payload[i] & 0xFF));
			}
		}
		return result.toString();
	}

}
