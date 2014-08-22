package com.techjar.hexwool.network;

import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.techjar.hexwool.network.packet.*;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public abstract class PacketHandler implements IPacketHandler {
    public static Map<Class, PacketType> reverseLookup = new HashMap<Class, PacketType>();
    
    public static enum PacketType {
        GUI_ACTION(PacketGuiAction.class);
        
        public final Class packetClass;
        public final Constructor constructor;
        
        private PacketType(Class c) {
            packetClass = c;
            try {
                constructor = packetClass.getConstructor(DataInputStream.class);
            } catch (Exception ex) {
                throw new RuntimeException("Packet class is not valid, must have constructor which passes DataInputStream");
            }
            PacketHandler.reverseLookup.put(packetClass, this);
        }
        
        public HexWoolPacket parsePacket(DataInputStream input) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            return (HexWoolPacket)constructor.newInstance(input);
        }
        
        public static PacketType getPacket(int id) {
            return values()[id];
        }
        
        public static PacketType getEnum(Class c) {
            return PacketHandler.reverseLookup.get(c);
        }
        
        public static int getId(Class c) {
            return PacketHandler.reverseLookup.get(c).ordinal();
        }
    }
}
