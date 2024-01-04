package com.profitz.app.promos.adapters;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public abstract class ECCardContentListItemAdapterPro<T> extends ArrayAdapter<T> {
    private boolean zeroItemsMode = true;

    public ECCardContentListItemAdapterPro(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }

    @Override
    public final int getCount() {
        return zeroItemsMode ? 0 : super.getCount();
    }

    @NonNull
    @Override
    public abstract View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent);

    final void enableZeroItemsModePro() {
        this.zeroItemsMode = true;
        notifyDataSetChanged();
    }

  final void disableZeroItemsModePro() {
        this.zeroItemsMode = false;
        notifyDataSetChanged();
    }

}
