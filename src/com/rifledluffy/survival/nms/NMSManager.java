package com.rifledluffy.survival.nms;

import com.rifledluffy.survival.nms.handlers.NMSHandler;
import com.rifledluffy.survival.nms.handlers.NMSHandler_1_13_R1;
import com.rifledluffy.survival.nms.handlers.NMSHandler_1_13_R2;

public class NMSManager {
	
	NMSHandler handler;
	public String notify;
	
	public void setup() {
		try {
			Class.forName("net.minecraft.server.v1_13_R1.MinecraftServer");
			handler = new NMSHandler_1_13_R1();
			notify = "Server is 1.13";
		} catch (ClassNotFoundException e) {}
		
		try {
			Class.forName("net.minecraft.server.v1_13_R2.MinecraftServer");
			handler = new NMSHandler_1_13_R2();
			notify = "Server is 1.13.1";
		} catch (ClassNotFoundException e) {}
	}
	
	public NMSHandler getNavigation() {
		return this.handler;
	}

}
