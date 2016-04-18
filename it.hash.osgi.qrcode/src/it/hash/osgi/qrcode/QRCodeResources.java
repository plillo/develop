package it.hash.osgi.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import io.swagger.annotations.Api;

@Api
@Path("/qrcode/v1.0")
public class QRCodeResources {

	@GET
	@Produces("image/png")
    @io.swagger.annotations.ApiOperation(value = "getQRCode", notes = "getQRCode")
	public Response getQRCode(@Context HttpServletRequest request) {
		String myCodeText = "http://www.hash.it/";

		@SuppressWarnings("unchecked")
		Enumeration<String> parNames = request.getParameterNames();
		
		// PARAMETERS
		while (parNames.hasMoreElements()) {
			String name = (String) parNames.nextElement();
			String value = request.getParameter(name);

			// Set text
			if (name.equals("text")) {
				try {
					myCodeText = value;
				} catch (NumberFormatException e) {
				}
				continue;
			}
		}

		int size = 1000;
		try {
			Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
			int CrunchifyWidth = byteMatrix.getWidth();
			BufferedImage bufferedImage = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
			bufferedImage.createGraphics();

			Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < CrunchifyWidth; i++) {
				for (int j = 0; j < CrunchifyWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}

			// RESPONSE
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(bufferedImage, "png", baos);
			} catch (IOException e) {
				return Response.serverError().build();
			}
			byte[] imageData = baos.toByteArray();

			// uncomment line below to send non-streamed
			// return Response.ok(imageData).build();

			// return streamed data
			return Response.ok(new ByteArrayInputStream(imageData)).build();
			
		} catch (WriterException e) {
			e.printStackTrace();
		}
		
		return Response.serverError().build();
	}
}
