package com.techjar.hexwool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;

public class Util {
    private static boolean packeting = false;
    private static ByteArrayOutputStream packetByteStream;
    private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String rgbToHex(int dec) {
        StringBuilder hexBuilder = new StringBuilder(6);
        hexBuilder.setLength(6);
        for (int i = 5; i >= 0; i--) {
            int j = dec & 0x0F;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= 4;
        }
        return hexBuilder.toString();
    }

    public static DataOutputStream startPacket(int id) {
        if (packeting) throw new IllegalStateException("Already writing a packet");
        packeting = true;
        packetByteStream = new ByteArrayOutputStream();
        packetByteStream.write(id);
        return new DataOutputStream(packetByteStream);
    }
    
    public static Packet250CustomPayload finishPacket() {
        if (!packeting) throw new IllegalStateException("Not writing a packet");
        Packet250CustomPayload packet = new Packet250CustomPayload(HexWool.networkChannel, packetByteStream.toByteArray());
        try { packetByteStream.close(); } catch (IOException ex) {}
        packetByteStream = null;
        packeting = false;
        return packet;
    }
}
