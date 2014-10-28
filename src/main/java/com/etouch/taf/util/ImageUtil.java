package com.etouch.taf.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;

/**
 * This class contains utility methods for comparing Images.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class ImageUtil {
	
	private static Log log = LogUtil.getLog(ImageUtil.class);

	public static boolean compareImagesUsingMD5(String currentScreenShotFile,
			String goldImageScreenShotFile ) {
		boolean isMD5ValueSame = false;
		try {
			File currentScreenShot = new File(currentScreenShotFile);
			File goldImageScreenShot = new File(goldImageScreenShotFile);
			// Calculate MD5 value for the image found environment being checked
			// (e.g. QA)
			String md5ValueForQA = getMG5ValueForImage(currentScreenShot);
			// Calculate MD5 value for the golden image
			String md5ValueForProd = getMG5ValueForImage(goldImageScreenShot);

			isMD5ValueSame = md5ValueForQA.equals(md5ValueForProd);
		} catch (IOException iex) {
			iex.printStackTrace();
		} catch (NoSuchAlgorithmException noAlgo) {
			noAlgo.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return isMD5ValueSame;

	}

	/**
	 * Calculates the MD5 Value for a given image
	 * 
	 * @param imgFile
	 *            File object for a screenshot/image
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	private static String getMG5ValueForImage(File imgFile) throws IOException,
			NoSuchAlgorithmException, Exception {
		BufferedImage buffImg = ImageIO.read(imgFile);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(buffImg, "png", outputStream);
//		byte[] data = outputStream.toByteArray();
		byte[] data = null;
		if(OSUtil.is64BitOS() == true) {
			data = Base64.decodeBase64(outputStream.toByteArray());	
		} else if(OSUtil.is32BitOS() == true) {
			data = outputStream.toByteArray();
		}
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data);
		byte[] hash = md.digest();
		return getHexValueForBytes(hash);
	}
	/**
	* Converts the MD5 byte value to a Hex String
	* @param inBytes
	* @return
	* @throws Exception
	*/
	private static String getHexValueForBytes(byte[] inBytes) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < inBytes.length; i++) { // for loop ID:1
			hexString.append(Integer.toString((inBytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		} // Belongs to for loop ID:1
		return hexString.toString();
	} // Belongs to returnHex class
}
