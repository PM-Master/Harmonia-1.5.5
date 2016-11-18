/* This software was developed by employees of the National Institute of
 * Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 15 United States Code Section 105, works of NIST
 * employees are not subject to copyright protection in the United States
 * and are considered to be in the public domain.  As a result, a formal
 * license is not needed to use the software.
 * 
 * This software is provided by NIST as a service and is expressly
 * provided "AS IS".  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
 * OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
 * AND DATA ACCURACY.  NIST does not warrant or make any representations
 * regarding the use of the software or the results thereof including, but
 * not limited to, the correctness, accuracy, reliability or usefulness of
 * the software.
 * 
 * Permission to use this software is contingent upon your acceptance
 * of the terms of this agreement.
 */
package gov.nist.csd.pm.common.net;

import gov.nist.csd.pm.common.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * This class implements a PM packet manager. A PM packet manager is used to
 * send, receive, and forward PM packets.
 * 
 * @author steveq@nist.gov
 * @version $Revision: 1.1 $, $Date: 2008/07/16 17:02:58 $
 * @since 6.0
 */
public class PacketManager {
	static Log log = new Log(Log.Level.INFO, true);

	/***************************************************************************
	 * Constants
	 **************************************************************************/

	/** The fixed length of the packet type field (in bytes). */
	public static final int PACKET_TYPE_FIELD_SIZE = 1;

	/** The fixed length of the packet type field (in bytes). */
	public static final int MAX_READ_LENGTH = 16384;

	/***************************************************************************
	 * Methods
	 **************************************************************************/

	/**
	 * Send a PM packet containing a byte array. This method is intended for
	 * sending byte arrays generated by applications.
	 * 
	 * @param bytes
	 *            The (binary) byte array data.
	 * @param outputStream
	 *            The output stream on which to send the PM packet.
	 * @throws PacketException
	 *             If file is null.
	 */

    public static void sendPacket(byte[] bytes, OutputStream outputStream){
        sendPacket(bytes, 0, bytes.length, outputStream);
    }

	public static void sendPacket(byte[] bytes, int offset, int length, OutputStream outputStream) {

		try {
            length = length < 0 ? 0 : length;
			Packet packet = new Packet();
			packet.addBinaryItem(length);
			byte[] packetPayloadLengthBytes = ByteUtil.intToBytes(packet
					.getPayloadLength());
			ArrayList<Item> items = packet.getItems();
			byte[] itemHeader = items.get(0).getItemHeader();

			byte[] packetHeader = new byte[Packet.LENGTH_FIELD_SIZE
					+ itemHeader.length];

			System.arraycopy(packetPayloadLengthBytes, 0, packetHeader, 0,
					Packet.LENGTH_FIELD_SIZE);
			System.arraycopy(itemHeader, 0, packetHeader,
					Packet.LENGTH_FIELD_SIZE, itemHeader.length);
			outputStream.write(packetHeader);
			// outputStream.flush();
			outputStream.write(bytes, offset, length);
			outputStream.flush();

System.out.println("Successfully sent packet: "	+ (packetHeader.length + bytes.length) + " bytes");

			bytes = null;

		} catch (PacketException pe) {

			pe.printStackTrace();

		} catch (ItemException ie) {

			ie.printStackTrace();

		} catch (IOException ioe) {

			ioe.printStackTrace();

		}

	}

	/**
	 * Send a PM packet.
	 * 
	 * @param packet
	 *            The PM packet to send.
	 * @param outputStream
	 *            The output stream on which to send the PM packet.
	 * @throws PacketException
	 *             If packet is null.
	 */
	public static void sendPacket(Packet packet, OutputStream outputStream)
			throws PacketException {

		if (packet == null)
			throw new PacketException("Cannot send null packet.");

		try {

			// Send byte buffer here
			byte[] bytes = packet.getBytes();

			outputStream.write(bytes);
			outputStream.flush();

//System.out.println("Successfully sent packet: " + bytes.length + " bytes");

		} catch (IOException ioe) {

			ioe.printStackTrace();

		}
	}

	/**
	 * Send a PM packet containing a binary stream. This method is intended for
	 * sending files or other binary payload.
	 * 
	 * @param inputStream
	 *            The input stream containing the binary payload.
	 * @param payloadLength
	 *            The length of the binary payload.
	 * @param outputStream
	 *            The output stream on which to send the PM packet.
	 * @throws PacketException
	 *             If file is null.
	 */
	public static void sendPacket(InputStream inputStream, int payloadLength,
			OutputStream outputStream) throws PacketException {

		try {

			Packet packet = new Packet();
			packet.addBinaryItem(payloadLength);
			byte[] packetPayloadLengthBytes = ByteUtil.intToBytes(packet
					.getPayloadLength());
			ArrayList<Item> items = packet.getItems();
			byte[] itemHeader = items.get(0).getItemHeader();

			byte[] packetHeader = new byte[Packet.LENGTH_FIELD_SIZE
					+ itemHeader.length];

			System.arraycopy(packetPayloadLengthBytes, 0, packetHeader, 0,
					Packet.LENGTH_FIELD_SIZE);
			System.arraycopy(itemHeader, 0, packetHeader,
					Packet.LENGTH_FIELD_SIZE, itemHeader.length);
			outputStream.write(packetHeader);
			//outputStream.flush();

			int totalBytesRead = 0;
			byte[] payloadBytes = new byte[MAX_READ_LENGTH];

			for (;;) {
				int bytesRead = inputStream.read(payloadBytes);
				if (bytesRead == -1)
					System.out.println("Read end of file with -1");

				totalBytesRead += bytesRead;
				outputStream.write(payloadBytes, 0, bytesRead);
				// outputStream.flush();

				if (totalBytesRead == payloadLength) {

					outputStream.flush();
					inputStream.close();

//System.out.println("Successfully sent packet: " + (totalBytesRead + packetHeader.length) + " bytes");

					break;
				}

			}

		} catch (ItemException ie) {

			ie.printStackTrace();

		} catch (PacketException pe) {

			pe.printStackTrace();

		} catch (IOException ioe) {

			ioe.printStackTrace();

		}

	}

  public static void copyBytes(InputStream inputStream, int payloadLength,
  OutputStream outputStream) throws Exception {

    int totalBytesRead = 0;
    byte[] payloadBytes = new byte[MAX_READ_LENGTH];

    for (;;) {
      int bytesRead = inputStream.read(payloadBytes);

      totalBytesRead += bytesRead;
      outputStream.write(payloadBytes, 0, bytesRead);

      if (totalBytesRead == payloadLength) {
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        break;
      }
    }
  }

        /**
	 * Receive a PM packet. If outputStream is not null and received packet
	 * contains a BINARY item, the received file will be written to the provided
	 * outputStream. If a file is received but outputStream is null, a
	 * PacketException will be thrown.
	 * 
	 * @param bufInputStream
	 *            The input stream from which to receive the packet.
	 * @param outputStream
	 *            The output stream to write incoming file data (if it exists).
	 * @throws PacketException
	 */
	public static Packet receivePacket(InputStream bufInputStream,
			OutputStream outputStream) throws PacketException {

		Packet packet = null;

		try {
			log.debug("PacketManager.receivePacket() TRACE 1");
			// Get message length header and first item type field
			byte[] packetPayloadLengthBytes = new byte[Packet.LENGTH_FIELD_SIZE];
			System.out.println("PacketManager.receivePacket() TRACE 2: packetLength: " + packetPayloadLengthBytes.length);
			if (bufInputStream.read(packetPayloadLengthBytes, 0,
					Packet.LENGTH_FIELD_SIZE) == -1){
				log.warn("No bytes to read from input stream. Returning null packet.");
				return null;
			}
			log.debug("PacketManager.receivePacket() TRACE 3");
			int payloadLength = ByteUtil.bytesToInt(packetPayloadLengthBytes);
			log.debug("PacketManager.receiptPacket() TRACE 4");
			if (payloadLength == 0) {
				log.debug("PacketManager.receiptPacket() TRACE 5: received empty payload. Creating empty packet.");
				// Handle empty packet by returning an empty packet
				packet = new Packet();
			} else if (payloadLength > 0) {
				log.debug("PacketManager.receiptPacket() TRACE 6: received non-empty packet.");
				// Get item type
				byte[] itemTypeBytes = new byte[1];
				if (bufInputStream.read(itemTypeBytes, 0,
						Item.ITEM_TYPE_FIELD_SIZE) == -1)
					throw new PacketException("Error reading item type field.");

				ItemType itemType = ItemType.getType(itemTypeBytes[0]);

				if (itemType != ItemType.BINARY) {
					System.out.println("ITEM TYPE IS NOT ITEM TYPE. BINARY");
					// Set up byte array for entire packet
					byte[] packetBytes = new byte[Packet.LENGTH_FIELD_SIZE
							+ payloadLength];

					// Copy length header field
					System.arraycopy(packetPayloadLengthBytes, 0, packetBytes,
							0, Packet.LENGTH_FIELD_SIZE);
					int offset = Packet.LENGTH_FIELD_SIZE;
					System.out.println("Before arrayCopy");
					// Copy first item type
					System.arraycopy(itemTypeBytes, 0, packetBytes, offset,
							Item.ITEM_TYPE_FIELD_SIZE);
					offset += Item.ITEM_TYPE_FIELD_SIZE;

					// Get remainder of packet
					payloadLength = payloadLength - Item.ITEM_TYPE_FIELD_SIZE;

					int totalPayloadBytesRead = 0;
					for (;;) {
						byte[] bytes = new byte[MAX_READ_LENGTH];
						int bytesRead = bufInputStream.read(bytes, 0,
								MAX_READ_LENGTH); //this was MAX_READ_LENGTH
						// System.out.println("BytesRead: " + bytesRead);
						if (bytesRead == -1)
							throw new PacketException("Error reading packet.");
						System.arraycopy(bytes, 0, packetBytes, offset,
								bytesRead);
						offset += bytesRead;
						totalPayloadBytesRead += bytesRead;

						if (totalPayloadBytesRead == payloadLength) {
							log.debug("Successfully received packet of non-binary data");
							packet = new Packet(packetBytes);
							break;
						}
					}
				} else if (itemType == ItemType.BINARY) {
					System.out.println("ITEM TYPE IS BINARY");
					if (outputStream == null) {
						log.warn("outputStream is null");
						throw new PacketException(
								"Error: Null output stream for BINARY data.");
					}
					byte[] binaryPayloadLengthBytes = new byte[Item.LENGTH_FIELD_SIZE];
					if (bufInputStream.read(binaryPayloadLengthBytes, 0,
							Item.LENGTH_FIELD_SIZE) == -1){
						System.out.println("ERROR READING ITM LENG");
						throw new PacketException(
								"Error reading item length field.");
					}
					int binaryPayloadLength = ByteUtil
							.bytesToInt(binaryPayloadLengthBytes);

					int totalPayloadBytesRead = 0;
					while (totalPayloadBytesRead < binaryPayloadLength) {
						byte[] bytes = new byte[MAX_READ_LENGTH];
						int bytesRead = bufInputStream.read(bytes, 0,
								MAX_READ_LENGTH);
						if (bytesRead == -1)
							throw new PacketException("Error reading packet.");
						outputStream.write(bytes, 0, bytesRead);
						totalPayloadBytesRead += bytesRead;
					}
                    outputStream.flush();
                    outputStream.close();
                    packet = new Packet();
                    packet.addBinaryItem(binaryPayloadLength);
                    packet.successfullyReceivedBinaryStream = true;
					log.debug("Successfully received packet of binary data");
				}
			}
		} catch (SocketException se) {
			System.out.println(se.getMessage());
			System.out.println("Remote connection closed.");

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return packet;
	}


   /**
    * Receive but discard a PM packet. This method is used when the server
    * is forced by the client to receive a packet, but the server cannot
    * handle it (for reasons including the client does not have permission
    * to send a file). In order to avoid an OutOfMemory exception, the server
    * must consume (read) the bytes from the input stream, but does not
    * write the received data to any object or file (the server will simply
    * discard the received bytes read). Since the read bytes are discarded,
    * this method does not return a packet.
    *
    * @param bufInputStream
    *            The input stream from which to receive the packet.
    * @throws PacketException
    */
   public static void discardReceivedPacket(InputStream bufInputStream)
           throws PacketException {

       try {

           // Get message length header and first item type field
           byte[] packetPayloadLengthBytes = new byte[Packet.LENGTH_FIELD_SIZE];

           if (bufInputStream.read(packetPayloadLengthBytes, 0,
                   Packet.LENGTH_FIELD_SIZE) == -1)
               return;

           int payloadLength = ByteUtil.bytesToInt(packetPayloadLengthBytes);

           if (payloadLength == 0) {
              
               return;

           } else if (payloadLength > 0) {

               // Get item type
               byte[] itemTypeBytes = new byte[1];
               if (bufInputStream.read(itemTypeBytes, 0,
                       Item.ITEM_TYPE_FIELD_SIZE) == -1)
                   return;

               ItemType itemType = ItemType.getType(itemTypeBytes[0]);

               if (itemType != ItemType.BINARY) {

                   // Set up byte array for entire packet
                   byte[] packetBytes = new byte[Packet.LENGTH_FIELD_SIZE
                           + payloadLength];

                   int offset = Packet.LENGTH_FIELD_SIZE;

                   offset += Item.ITEM_TYPE_FIELD_SIZE;

                   // Get remainder of packet
                   payloadLength = payloadLength - Item.ITEM_TYPE_FIELD_SIZE;

                   int totalPayloadBytesRead = 0;

                   for (;;) {

                       byte[] bytes = new byte[MAX_READ_LENGTH];
                       int bytesRead = bufInputStream.read(bytes, 0,
                               MAX_READ_LENGTH);
                       // System.out.println("BytesRead: " + bytesRead);
                       if (bytesRead == -1)
                           return;

                       offset += bytesRead;
                       totalPayloadBytesRead += bytesRead;

                       if (totalPayloadBytesRead == payloadLength) {

                           System.out.println("Successfully received packet: "
                                   + packetBytes.length + " bytes");

                           break;

                       }

                   }

               } else if (itemType == ItemType.BINARY) {

                   byte[] binaryPayloadLengthBytes = new byte[Item.LENGTH_FIELD_SIZE];
                   if (bufInputStream.read(binaryPayloadLengthBytes, 0,
                           Item.LENGTH_FIELD_SIZE) == -1)
                       return;
                   int binaryPayloadLength = ByteUtil
                           .bytesToInt(binaryPayloadLengthBytes);

                   int totalPayloadBytesRead = 0;
                   for (;;) {

                       byte[] bytes = new byte[MAX_READ_LENGTH];
                       int bytesRead = bufInputStream.read(bytes, 0,
                               MAX_READ_LENGTH);
                       if (bytesRead == -1)
                           return;

                       totalPayloadBytesRead += bytesRead;

                       if (totalPayloadBytesRead == binaryPayloadLength) {
                          
                           System.out.println("Successfully discarded " +
                                   "received BINARY " +
                                   "packet: "
                                   + (totalPayloadBytesRead
                                   + Packet.LENGTH_FIELD_SIZE
                                   + Item.ITEM_TYPE_FIELD_SIZE
                                   + Item.LENGTH_FIELD_SIZE)
                                   + " bytes");
                          
                           break;

                       }

                   }

               }

           }

       } catch (SocketException se) {

           System.out.println("Remote connection closed.");

       } catch (IOException ioe) {
           ioe.printStackTrace();
       }

       ///return packet;
   }
        
	/**
	 * Forward data from an input stream to an output stream. This method is
	 * particularly useful for forwarding file data from the server to the
	 * application via a proxy.
	 * 
	 * @param inputStream
	 *            The input stream from which to forward bytes.
	 * @param outputStream
	 *            The output stream on which to forward bytes.
	 */
	public static void forwardBytes(InputStream inputStream,
			OutputStream outputStream) {

		int packetPayloadLength = 0;
		int bytesRead = 0;

		try {

			// Get message length header and first item type field
			byte[] packetPayloadBytes = new byte[Packet.LENGTH_FIELD_SIZE];

			bytesRead = inputStream.read(packetPayloadBytes, 0,
					Packet.LENGTH_FIELD_SIZE);

			if (bytesRead == -1) {
				// System.out.println("End of stream");
				return;
			} else {
				// Forward length field
				outputStream.write(packetPayloadBytes);
				//outputStream.flush();
			}

			// Get payload length
			packetPayloadLength = ByteUtil.bytesToInt(packetPayloadBytes);

			byte[] bytes = new byte[MAX_READ_LENGTH];
			int totalBytesRead = 0;

			for (;;) {
				bytesRead = inputStream.read(bytes, 0, MAX_READ_LENGTH);
				if (bytesRead == -1) {
					System.out.println("End of stream");
					break;
				}
				// System.out.println("bytesForwarded: " + bytesRead);

				outputStream.write(bytes, 0, bytesRead);
				//outputStream.flush();

				totalBytesRead += bytesRead;

				if (totalBytesRead == packetPayloadLength) {

					outputStream.flush();

//System.out.println("Successfully forwarded packet: "
//  + (totalBytesRead + packetPayloadBytes.length)
//  + " bytes");

					break;

				}
			}

		} catch (SocketException se) {

			System.out.println("socket closed");

		} catch (IOException ioe) {

			ioe.printStackTrace();

		}
	}
}