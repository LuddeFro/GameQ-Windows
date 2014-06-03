
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;



public class PacketHandler {
	
	
	
	public PacketHandler() {
		
		
		List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs 
		StringBuilder errbuf = new StringBuilder(); // For any error msgs 
		//list devices on the system
		int r = Pcap.findAllDevs(alldevs, errbuf); 
		Main.alert("it works!");
		
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) { 
        	
            System.err.printf("Can't read list of devices, error is %s", errbuf  
                .toString());  
            
            return;  
        }  
        System.out.println("Network devices found:");  
        int i = 0;  
        for (PcapIf device : alldevs) {  
            String description =  
                (device.getDescription() != null) ? device.getDescription()  
                    : "No description available";  
            System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
            
            //monitor each device
            (new Thread(new PCapThread(device, errbuf))).start();
            
            
        }  
        System.out.println("interfaces: " + i);
        
        
	}
	
}
