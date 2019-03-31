package jun.safe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jun.safe.R;

/**
 * Created by zkn on 2018/3/14.
 */

public class SettingItemView extends RelativeLayout {
    private final String SPACE = "http://schemas.android.com/apk/res/jun.safe" ;
    private String desTitle  ;
    private String desOff  ;
    private String desOn   ;
    private final TextView tv_setting_title;
    private final TextView tv_setting_content;
    private final CheckBox cb_setting_select;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(context,R.layout.setting_item,this) ;
        tv_setting_title = this.findViewById(R.id.tv_setting_title);
        cb_setting_select = this.findViewById(R.id.cb_setting_select);
        tv_setting_content = this.findViewById(R.id.tv_setting_content);
        initAttrs(attrs) ;
        tv_setting_title.setText(desTitle);
    }

    /**
     * 获取属性值
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        desTitle = attrs.getAttributeValue(SPACE,"desTitle") ;
        desOff = attrs.getAttributeValue(SPACE, "desOff");
        desOn = attrs.getAttributeValue(SPACE, "desOn");
    }


    /**
     *
     * @return 返回是否选中
     */
    public boolean isCheck(){
        return cb_setting_select.isChecked() ;
    }

    public void setCheck(boolean isCheck){
        cb_setting_select.setChecked(isCheck);
        if(isCheck){
            tv_setting_content.setText(desOn);
        }else {
            tv_setting_content.setText(desOff);
        }
    }


}
