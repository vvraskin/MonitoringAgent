package de.uni_stuttgart.monitoring;


import java.util.ArrayList;

import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

public class NetworkStateProbe {
	
	Sigar sigarImpl;
	SigarProxy sigar;
	public NetworkStateProbe(){
		this.sigarImpl = new Sigar();
		
	}
	/**
	 * Provide channel width in Mbit/sec, retrieve used bandwidth in percentage from this value.
	 * Receiver bandwidth is considered here
	 * @param channelWidth
	 * @return
	 * @throws InterruptedException 
	 * @throws SigarException 
	 */
	public double getAverageUsedDownloadBandwidth(int channelWidth) throws SigarException, InterruptedException{
		double percentage = 100.0;
		// make both values of the same scope, KBytes/s
		double channelWidthInKBytesPerSecond = ((channelWidth/8)*1024);
		double rxRateInKBytesPerSecond = this.getAverageRxRate()/1024;
		percentage = (rxRateInKBytesPerSecond*percentage)/channelWidthInKBytesPerSecond;
		
		// round to 3 symbols after the dot
		int roundedValue = (int) (percentage*1000); 
		percentage = (double) roundedValue/1000;
		return percentage;
	}
	/**
	 * Provide channel width in Mbit/sec, retrieve used bandwidth in percentage from this value.
	 * Transmitter bandwidth is considered here
	 * @param channelWidth
	 * @return
	 * @throws InterruptedException 
	 * @throws SigarException 
	 */
	public double getAverageUsedUploadBandwidth(int channelWidth) throws SigarException, InterruptedException{
		double percentage = 100.0;
		// make both values of the same scope, KBytes/s
		double channelWidthInKBytesPerSecond = ((channelWidth/8)*1024);
		double rxRateInKBytesPerSecond = this.getAverageTxRate()/1024;
		percentage = (rxRateInKBytesPerSecond*percentage)/channelWidthInKBytesPerSecond;
		
		// round to 3 symbols after the dot
		int roundedValue = (int) (percentage*1000); 
		percentage = (double) roundedValue/1000;
		return percentage;
	}
	/**
	 * Average rate of received bytes in bytes per second. Blocking method, execute it in a separate thread
	 * @return
	 * @throws SigarException
	 * @throws InterruptedException
	 */
	public double getAverageRxRate() throws SigarException, InterruptedException
	{
		int smallCycle = 1000;
		int bigCycle = 5000;
		NetInterfaceStat netStat = null;
		long rxBytesLastCycle = 0;
		long rxBytesNewCycle = 0;
		ArrayList<Long> rxBytesTotal = new ArrayList<Long>();
		long averageBytesPerSecond; 
		
		// measure received bytes n times, where n is bigCycle/smallCycle
		for(int i=0;i<=bigCycle; i+=smallCycle)
		{
			rxBytesNewCycle = 0;
			this.sigar=SigarProxyCache.newInstance(sigarImpl);
			
			// measure the number of received bytes on all network interfaces
			for(String ni : sigar.getNetInterfaceList())
			{
				netStat = this.sigar.getNetInterfaceStat(ni);
				if(i==0)
					rxBytesLastCycle+=netStat.getRxBytes();
				else
					rxBytesNewCycle+=netStat.getRxBytes();
			}
			// we are interested on the changed values
			if(rxBytesNewCycle - rxBytesLastCycle > 0)
			{
				rxBytesTotal.add(rxBytesNewCycle-rxBytesLastCycle);
				rxBytesLastCycle = rxBytesNewCycle;
			}
			// sleep till the next measure cycle
			Thread.sleep(smallCycle);
		}
		// get average value for all non zero measurements
		averageBytesPerSecond = this.calculateAverageRate(rxBytesTotal);
		
		return averageBytesPerSecond;
	}
	/**
	 * Average rate of transmitted bytes in bytes per second. Blocking method, execute it in a separate thread
	 * @return
	 * @throws SigarException
	 * @throws InterruptedException
	 */
	public double getAverageTxRate() throws SigarException, InterruptedException
	{
		int smallCycle = 1000;
		int bigCycle = 5000;
		NetInterfaceStat netStat = null;
		long txBytesLastCycle = 0;
		long txBytesNewCycle = 0;
		ArrayList<Long> txBytesTotal = new ArrayList<Long>();
		long averageBytesPerSecond; 
		
		// measure transmitted bytes during bigCycle period
		for(int i=0;i<=bigCycle; i+=smallCycle)
		{
			txBytesNewCycle = 0;
			this.sigar=SigarProxyCache.newInstance(sigarImpl);
			
			// measure the number of received bytes on all interfaces
			for(String ni : sigar.getNetInterfaceList())
			{
				netStat = this.sigar.getNetInterfaceStat(ni);
				if(i==0)
					txBytesLastCycle+=netStat.getTxBytes();
				else
					txBytesNewCycle+=netStat.getTxBytes();
			}
			// we are interested on the changed values
			if(txBytesNewCycle - txBytesLastCycle > 0)
			{
				txBytesTotal.add(txBytesNewCycle-txBytesLastCycle);
				txBytesLastCycle = txBytesNewCycle;
			}
				
			Thread.sleep(smallCycle);
		}
		// get average value for all non zero measurements
		averageBytesPerSecond = this.calculateAverageRate(txBytesTotal);
		
		return averageBytesPerSecond;
	}
	//TODO: substitute by some more beautiful calculation
	private long calculateAverageRate(ArrayList<Long> measurements){
		long result=0;
		for(Long m : measurements)
			result += m;
		if(result!=0)
			result = result/measurements.size();
		return result;
	}
	
		}


