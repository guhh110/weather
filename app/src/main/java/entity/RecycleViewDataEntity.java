package entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by guhh on 2017/11/24.
 */

public class RecycleViewDataEntity implements MultiItemEntity{
    public static final int TYPE_DF = 0;
    public static final int TYPE_SG = 1;


    private Object object;
    @Override
    public int getItemType() {
        if(object instanceof Daily_forecast){
            return TYPE_DF;
        }else if(object instanceof SuggestionEntity){
            return TYPE_SG;
        }else{
            return -1;
        }

    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
