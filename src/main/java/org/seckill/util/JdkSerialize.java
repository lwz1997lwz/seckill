package org.seckill.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**ͨ��jdkʵ�����л�
 * @author viruser
 *
 */
public class JdkSerialize {
    
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            byte[] b = bos.toByteArray();
            return b;
        } catch (IOException e) {
            System.out.println("���л�ʧ�� Exception:" + e.toString());
            return null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException ex) {
                System.out.println("io could not close:" + ex.toString());
            }
        }
    }
    
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // �����л�
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("bytes Could not deserialize:" + e.toString());
            return null;
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException ex) {
                System.out.println("LogManage Could not serialize:" + ex.toString());
            }
        }
    }
}
