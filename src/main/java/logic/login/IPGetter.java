package logic.login;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by chrx on 5/27/17.
 */
public class IPGetter {

    public static String getIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> ia = ni.getInetAddresses();
                while(ia.hasMoreElements()){
                    InetAddress addr = ia.nextElement();
                    if(addr instanceof Inet4Address) {
                        if(!addr.isMulticastAddress() && !addr.isLoopbackAddress() && !addr.isAnyLocalAddress())
                            return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(getIP());
    }
}
