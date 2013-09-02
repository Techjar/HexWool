package com.techjar.hexwool.network.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.techjar.hexwool.network.HexWoolPacket;

import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet1GuiAction extends HexWoolPacket {
    public Packet1GuiAction(int action, String message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bos);
        data.write(getPacketId());
        data.write(action);
        data.close();
    }
    
    @Override
    public int getPacketId() {
        return 1;
    }
    
    public void processClient() {
        
    }
}
