package com.techjar.hexwool.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerServer extends PacketHandler {
    @Override
    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player) {
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
            int packetId = input.read();
            HexWoolPacket pkt = PacketHandler.PacketType.getPacket(packetId).parsePacket(input);
            pkt.processServer(network, player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
