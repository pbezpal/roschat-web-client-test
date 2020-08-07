package client.tools;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import static chat.ros.testing2.data.LoginData.HOST_SERVER;

public class SSHGetCommand {

    private static JSch ssh = new JSch();
    private static  String line;

    public static String isCheckQuerySSH(String command){
        try {
            Session s = ssh.getSession("root", HOST_SERVER, 2222);
            s.setPassword("Art7Tykx78Dp");
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            s.setConfig(config);
            s.connect();

            Channel c = s.openChannel("exec");
            ChannelExec ce = (ChannelExec) c;
            ce.setCommand(command);
            ce.connect();

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            line = reader.readLine();

            ce.disconnect();
            s.disconnect();

            return line;

        } catch (JSchException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

    }
}
