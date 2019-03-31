package jun.safe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import java.util.List;

/**
 * Created by zkn on 2018/3/24.
 */

public class MyGridlayout extends GridView {
    private final int ROW_NUMBER = 3;


    public MyGridlayout(Context context) {
        super(context);
    }

    public MyGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
