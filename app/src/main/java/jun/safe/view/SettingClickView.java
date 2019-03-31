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

public class SettingClickView extends RelativeLayout {

    private final TextView tv_setting_title;
    private final TextView tv_setting_content;

    public SettingClickView(Context context) {
        this(context,null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context,R.layout.setting_item2,this) ;
        tv_setting_title = this.findViewById(R.id.tv_setting_title);
        tv_setting_content = this.findViewById(R.id.tv_setting_content);
    }

    public void setTitle(String title){
        tv_setting_title.setText(title);
    }
    public void setContent(String content){
        tv_setting_content.setText("("+content+")");
    }



}
