package de.uni_stuttgart.monitoring;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Return latency when connecting to the given host in milliseconds
 * @author Vadim Raskin
 *
 */
public class AverageLatencyProbe {
	public double getAverageLatence(String ip, int port) throws IOException{
		long latency = 0;
		int loopPeriod = 5;
		long startTime;
		long endTime;
		InetAddress address = InetAddress.getByName(ip); 
		SocketAddress socketAddress = new InetSocketAddress(address,port);
		// calculate average for several latency values
		for(int i = 0; i<loopPeriod; i++)
		{
			Socket s = new Socket();
			startTime = System.currentTimeMillis();
			s.connect(socketAddress);
			endTime = System.currentTimeMillis();
			latency += endTime - startTime;
			s.close();
		}
		return latency/loopPeriod;
	}

}
