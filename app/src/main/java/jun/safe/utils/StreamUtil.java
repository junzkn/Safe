package jun.safe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zkn on 2018/3/12.
 */

public class StreamUtil {

    /**
     *
     * @param is
     * @return String
     */
    public static String streamToString(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int temp = -1 ;
        try {
            while((temp=is.read(buffer))!=-1){
                bos.write(buffer,0,temp);
            }
            return bos.toString() ;
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                bos.close();is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null ;
    }
}
