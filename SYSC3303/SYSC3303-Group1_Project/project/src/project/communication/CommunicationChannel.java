package project.communication;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import project.Async;

/**
 * A communication channel used for two-way asynchronous communication.
 *
 * A channel must connect to the recipient first before sending messages.
 *
 * This is not necessary for a recipient to receive data, as when there are
 * messages received, the channel will connect to the sender automatically if
 * not connected.
 */
public class CommunicationChannel implements Closeable {
	private Set<Subscriber> subscribers = new HashSet<>();
	private InetAddress recipientAddress = null;
	private Integer recipientPort = null;
	private DatagramSocket socket;

	private Future<?> backgroundReceivingTask;

	public interface Subscriber {
		/**
		 * Called when the channel has received data.
		 * @param channel The channel itself
		 * @param data The data received
		 */
		default public void channelReceivedData(CommunicationChannel channel, byte[] data) {}

		/**
		 * Called when the channel is closed.
		 * @param channel The channel itself
		 */
		default public void channelIsClosed(CommunicationChannel channel) {}
	}

	public CommunicationChannel() { this(null); }

	public CommunicationChannel(Integer port) {
		try {
			socket = port == null
				? new DatagramSocket()
				: new DatagramSocket(port);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		backgroundReceivingTask = Async.run(() -> {
			// receives data while not closed
			while (!socket.isClosed()) {
				try {
					var data = receive();
					if (data.length > 0) {
						Async.run(() -> {
							// informs each subscriber that the channel has received some data
							subscribers.stream().forEach((s) -> {
								s.channelReceivedData(this, data);
							});
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Connects the channel to a recipient if the channel has not been
	 * connected.
	 * @param recipientAddress
	 * @param recipientPort
	 */
	public void connect(InetAddress recipientAddress, int recipientPort) {
		if (isConnected())
			return;
		this.recipientAddress = recipientAddress;
		this.recipientPort = recipientPort;
	}

	/**
	 * Disconnects the channel with its current recipient so that the channel
	 * can connect to others if needed.
	 */
	public void disconnect() {
		recipientAddress = null;
		recipientPort = null;
	}

	public boolean isConnected() { return recipientAddress != null && recipientPort != null; }

	/**
	 * A recipient must subscribe to this channel in order to receive messages
	 * through the channel.
	 * @param subscriber The recipient
	 */
	public void subscribe(Subscriber subscriber) {
		subscribers.add(subscriber);
	}

	public boolean unsubscribe(Subscriber subscriber) {
		return subscribers.remove(subscriber);
	}

	/**
	 * Publishes a message to the recipient.
	 *
	 * @param data The message to be sent
	 * @throws IOException Thrown when the packet cannot be sent to the recipient
	 * @throws RuntimeException Thrown when the channel has not been connecteed
	 *                          to a recipient
	 */
	public void publish(byte[] data) throws IOException, RuntimeException {
		if (!isConnected())
			throw new RuntimeException("The communication channel must be connected before publishing messages");
		DatagramPacket packet = new DatagramPacket(
			data, data.length, recipientAddress, recipientPort
		);
		socket.send(packet);
	}

	/**
	 * Publishes a serializable object to the recipient.
	 *
	 * @param message The message that will be sent to the recipient
	 * @throws IOException Thrown when the packet cannot be sent to the recipient
	 * @throws RuntimeException Thrown when the channel has not been connecteed
	 *                          to a recipient
	 */
	public <T extends Serializable> void publish(Message<T> message) throws IOException, RuntimeException {
		byte[] data = null;

		try (
			var bos = new ByteArrayOutputStream();
			var oos = new ObjectOutputStream(bos);
		) {
			oos.writeObject(message.type());
			oos.writeByte(0);
			oos.writeObject(message.content());
			data = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (data != null)
			publish(data);
	}

	private byte[] receive() throws IOException { return receive(1024); }

	private byte[] receive(int bufferLength) throws IOException {
		byte[] buffer = new byte[bufferLength];
		var packet = new DatagramPacket(buffer, buffer.length);

		try {
			socket.receive(packet);
		} catch (SocketException e) {
			return new byte[0];
		}

		if (!isConnected())
			connect(packet.getAddress(), packet.getPort());

		return packet.getData();
	}

	@Override
	public void close() throws IOException {
		synchronized (this) {
			if (backgroundReceivingTask.isCancelled() || socket.isClosed())
				return;
			backgroundReceivingTask.cancel(true);
			socket.close();
			Async.run(() -> {
				subscribers.stream().forEach((s) -> {
					s.channelIsClosed(this);
				});
			});
		}
	}
}
