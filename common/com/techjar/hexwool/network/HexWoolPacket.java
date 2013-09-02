package com.techjar.hexwool.network;

import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public abstract class HexWoolPacket {
    protected Packet250CustomPayload packet;
    
    public void processClient(INetworkManager network, Player player) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not implement a client side packet handler.");
    }
    
    public void processServer(INetworkManager network, Player player) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not implement a server side packet handler.");
    }
    
    public Packet250CustomPayload getPacket() {
        return packet;
    }
    
    public int getPacketId() {
        return PacketHandler.PacketType.getId(getClass());
    }
    
    public PacketHandler.PacketType getPacketEnum() {
        return PacketHandler.PacketType.getEnum(getClass());
    }
}
