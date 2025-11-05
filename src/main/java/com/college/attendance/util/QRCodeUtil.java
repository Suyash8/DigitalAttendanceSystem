package com.college.attendance.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeUtil {

    /**
     * Generates a QR code image from a given text.
     * @param text The text to encode in the QR code.
     * @param width The desired width of the QR code image.
     * @param height The desired height of the QR code image.
     * @return A byte array representing the QR code image in PNG format.
     * @throws WriterException if there is an error during QR code generation.
     * @throws IOException if there is an error writing the image to the byte stream.
     */
    public static byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // A ByteArrayOutputStream is an in-memory stream that we can write the image to.
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

        // Write the BitMatrix to the stream as a PNG image.
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        // Return the raw bytes of the generated image.
        return pngOutputStream.toByteArray();
    }

    // Optional: A main method to quickly test the QR code generation
    // and save it as a file to see if it works.
    public static void main(String[] args) {
        try {
            String text = "Hello, World!";
            byte[] qrImage = generateQRCodeImage(text, 350, 350);

            // This part is just for testing - it saves the generated image to your project root.
            java.nio.file.Path path = java.nio.file.Paths.get("test-qrcode.png");
            java.nio.file.Files.write(path, qrImage);

            System.out.println("Successfully generated test-qrcode.png in project root.");

        } catch (WriterException | IOException e) {
            System.err.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        }
    }
}