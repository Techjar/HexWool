package com.techjar.hexwool.network;

import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public abstract class HexWoolPacket {
    protected Packet250CustomPayload packet;
    
    public abstract int getPacketId();
    
    public void processClient(INetworkManager manager, HexWoolPacket packet, Player player) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not implement a client side packet handler.");
    }
    
    public void processServer(INetworkManager manager, HexWoolPacket packet, Player player) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not implement a server side packet handler.");
    }
    
    public Packet250CustomPayload getPacket() {
        return packet;
    }
}
