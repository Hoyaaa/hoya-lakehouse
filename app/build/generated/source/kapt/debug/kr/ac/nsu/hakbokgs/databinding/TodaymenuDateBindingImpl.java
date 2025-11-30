package kr.ac.nsu.hakbokgs.databinding;
import kr.ac.nsu.hakbokgs.R;
import kr.ac.nsu.hakbokgs.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class TodaymenuDateBindingImpl extends TodaymenuDateBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public TodaymenuDateBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private TodaymenuDateBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[1]
            );
        this.dateNum.setTag(null);
        this.dateText.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.btnDateNum == variableId) {
            setBtnDateNum((int) variable);
        }
        else if (BR.btnDateText == variableId) {
            setBtnDateText((androidx.databinding.ObservableField<java.lang.String>) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setBtnDateNum(int BtnDateNum) {
        this.mBtnDateNum = BtnDateNum;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.btnDateNum);
        super.requestRebind();
    }
    public void setBtnDateText(@Nullable androidx.databinding.ObservableField<java.lang.String> BtnDateText) {
        updateRegistration(0, BtnDateText);
        this.mBtnDateText = BtnDateText;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.btnDateText);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeBtnDateText((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeBtnDateText(androidx.databinding.ObservableField<java.lang.String> BtnDateText, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        int btnDateNum = mBtnDateNum;
        androidx.databinding.ObservableField<java.lang.String> btnDateText = mBtnDateText;
        java.lang.String btnDateTextGet = null;
        java.lang.String stringValueOfBtnDateNum = null;

        if ((dirtyFlags & 0x6L) != 0) {



                // read String.valueOf(btnDateNum)
                stringValueOfBtnDateNum = java.lang.String.valueOf(btnDateNum);
        }
        if ((dirtyFlags & 0x5L) != 0) {



                if (btnDateText != null) {
                    // read btnDateText.get()
                    btnDateTextGet = btnDateText.get();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.dateNum, stringValueOfBtnDateNum);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.dateText, btnDateTextGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): btnDateText
        flag 1 (0x2L): btnDateNum
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}