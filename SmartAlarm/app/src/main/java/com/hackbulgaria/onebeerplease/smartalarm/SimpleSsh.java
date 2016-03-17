package com.hackbulgaria.onebeerplease.smartalarm;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;

/**
 * Just a simple ssh shell in java
 * Created by plamen on 16.03.16.
 */
public class SimpleSsh {
    private JSch sch;
    private String hostname;
    private String username;
    private String password;
    private Session session;
    private Channel channel;

    public SimpleSsh(String hostname, String username, String password) throws JSchException {
        sch = new JSch();
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        session = sch.getSession(username, hostname, 22);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(3000);
    }

    public PrintStream openShell(OutputStream out) throws JSchException, IOException {
        channel = session.openChannel("shell");
        PrintStream shellStream = new PrintStream(channel.getOutputStream());
        channel.setOutputStream(out);
        channel.connect(3000);
        return shellStream;
    }
}
