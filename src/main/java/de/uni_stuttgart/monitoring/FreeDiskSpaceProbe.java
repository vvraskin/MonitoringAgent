package de.uni_stuttgart.monitoring;

import java.io.File;

import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;


public class FreeDiskSpaceProbe {
	Sigar sigarImpl;
	SigarProxy sigar;
	public FreeDiskSpaceProbe (){
		this.sigarImpl = new Sigar();
		this.sigar=SigarProxyCache.newInstance(sigarImpl);
	}
	/**
	 * Get free disk space in Mb
	 * @param fsRoot
	 * @return
	 * @throws SigarException
	 */
	public long getFreeDiskSpace(String fsRoot) throws SigarException
	{
		long freeSpace = 0;
		long metricSigar = 0;
		long metricJava = 0;
		File file = new File(fsRoot);
		FileSystemUsage fsUsage = null;
		fsUsage = sigar.getFileSystemUsage(fsRoot);
		metricSigar = fsUsage.getFree() / 1024;
		metricJava = file.getFreeSpace() / 1024 / 1024;
		// return the smallest value of both metrics if they differ
		if(metricSigar == metricJava || metricSigar < metricJava)
			freeSpace = metricSigar;
		else 
			freeSpace = metricJava;
		
		return freeSpace;
	}
}
