package ibermatica_project.tests;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import ibermatica_project.model.base.DataBase;
import ibermatica_project.model.base.User;

public class TestEncrypt {

    static DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);

    public static void main(String[] args) throws Exception {
        test();

        ArrayList<User> userlist = new ArrayList<User>();
        userlist = db.searchAllUsers();

        System.out.println(userlist.get(0));

        byte[] bytes = stringToByte(userlist.get(0).getPassword());
        String s = decipher(bytes);
        System.out.println(s);
    }

    private static void test() throws Exception {
        String password = "Admin123";

        System.out.println(password);
        byte[] bytes = encriptPass(password);

        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i]);
        }

        System.out.print(byteToString(bytes));

        String s = decipher(bytes);
        System.out.println("\n\n"+s);
    }

    public static byte[] encriptPass(String pass) throws Exception {
        final byte[] bytes = pass.getBytes("UTF-8");
        final Cipher aes = getCipher(true);
        final byte[] encriptyon = aes.doFinal(bytes);

        return encriptyon;
    }

    public static String decipher(byte[] encriptyon) throws Exception {
        final Cipher aes = getCipher(false);
        final byte[] bytes = aes.doFinal(encriptyon);
        final String unencrypted = new String(bytes, "UTF-8");

        return unencrypted;
    }

    private static Cipher getCipher(boolean toEncript) throws Exception {
        final String keyText = "áÁéÉíÍóÓúÚüÜñÑ1234567890!#%$&()=%¡'+`´ç´-.,;:_";
        final MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(keyText.getBytes("UTF-8"));
        final SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");

        final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        if (toEncript) {
            aes.init(Cipher.ENCRYPT_MODE, key);
        } else {
            aes.init(Cipher.DECRYPT_MODE, key);
        }

        return aes;
    }

    public static byte[] stringToByte(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

        return bytes;
    }

    public static String byteToString(byte[] bytes) {
        String str = new String(bytes, StandardCharsets.UTF_8);

        return str;
    }
}
