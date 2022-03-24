package xyz.groax;

import com.google.gson.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.nio.charset.*;
import org.apache.logging.log4j.*;
import java.io.*;
import java.net.*;

public class Utils
{
    private static final JsonParser parser = new JsonParser();
    
    public static JsonElement decrypt(final File file) {
            if (!file.exists() || !file.isFile()) {
                return null;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] secret = new byte[8];
            byte[] key = new byte[16];
            try {
                final FileInputStream fileInputStream = new FileInputStream(file);
                final DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                try {
                    if (dataInputStream.read() != 31) {
                        return null;
                    }
                    dataInputStream.read(secret, 0, secret.length);
                    byte[] temp = new byte[dataInputStream.readInt()];
                    System.arraycopy(temp, 0, key, 0, temp.length);
                    dataInputStream.read(key, 0, key.length);
                    final byte[] array3 = new byte[1024];
                    int read;
                    while ((read = dataInputStream.read(array3, 0, array3.length)) > -1) {
                        byteArrayOutputStream.write(array3, 0, read);
                    }
                }
                finally {
                    fileInputStream.close();
                    dataInputStream.close();
                }
                final byte[] array4 = byteArrayOutputStream.toByteArray();
                final SecretKeySpec secretKeySpec = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(generateKey().toCharArray(), secret, 65536, 128)).getEncoded(), "AES");
                final Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
                instance.init(2, secretKeySpec, new IvParameterSpec(key));
                return Utils.parser.parse(new String(instance.doFinal(array4), StandardCharsets.UTF_8));
            } 
            catch (Exception ex) {
                LogManager.getRootLogger().log(Level.WARN, "Bir hata olu\u015ftu!", (Throwable)ex);
                return null;
            }
    }


    private static String generateKey() {
        String var0 = System.getenv("COMPUTERNAME");
            if (var0 != null && !var0.isEmpty()) {
                return var0;
            }
            try {
                var0 = InetAddress.getLocalHost().getHostName();
                if (var0 != null && !var0.isEmpty()) {
                    return var0;
                }
            } catch (UnknownHostException ignored) {  }

            return System.getProperty("user.name");
    }

}
