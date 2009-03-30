package org.s449;

import java.awt.event.*;
import java.util.*;

/**
 * A transfer proxy server that transfers a list of ports. Note that
 *  ESPERANZA cannot be transferred in this manner and so therefore
 *  cannot be implemented. Broadcasting also cannot be implemented
 *  due to the static broadcasting restriction on servers.
 * 
 * @author Stephen Carlson
 * @version 4.0.0
 */
public class Transfer implements ActionListener {
	/**
	 * The parent status object.
	 */
	private ScoutStatus status;
	/**
	 * The list of port listeners.
	 */
	private List listeners;
	/**
	 * Running?
	 */
	private boolean running;

	/**
	 * Creates a transfer server.
	 * 
	 * @param stat the scout status
	 */
	public Transfer(ScoutStatus stat) {
		status = stat;
		listeners = new LinkedList();
		running = false;
	}

	public void start() {
		String host = status.getRemoteHost();
		Set ports = new TreeSet();
		ports.add(new Integer(Constants.WEB_PORT));
		ports.add(new Integer(Constants.BULK_PORT));
		ports.add(new Integer(Constants.CONTROL_PORT));
		Iterator it = ports.iterator();
		PortListener pl;
		while (it.hasNext()) {
			pl = new PortListener(host, ((Integer)it.next()).intValue());
			listeners.add(pl);
			pl.start();
		}
		running = true;
	}
	public void stop() {
		Iterator it = listeners.iterator();
		while (it.hasNext()) {
			((PortListener)it.next()).close();
			it.remove();
		}
		running = false;
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("s_transfer")) {
			boolean toRun = status.isTransferRunning();
			if (toRun && !running) start();
			else if (!toRun && running) stop();
		}
	}
}