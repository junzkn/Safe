package jun.safe.bean;

/**
 * Created by zkn on 2018/3/21.
 */

public class BlockBean {
    public  String phone ;
    public  String mode ;

    public BlockBean(String phone , String mode) {
        this.mode = mode ;
        this.phone = phone ;
    }
    public BlockBean() {
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
