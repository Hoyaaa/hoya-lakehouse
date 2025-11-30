package kr.ac.nsu.hakbokgs.main.store.taesan.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0010\u0011B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/taesan/main/MenuAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lkr/ac/nsu/hakbokgs/main/store/taesan/db/Menu;", "Lkr/ac/nsu/hakbokgs/main/store/taesan/main/MenuAdapter$MenuViewHolder;", "onClick", "Lkotlin/Function1;", "", "(Lkotlin/jvm/functions/Function1;)V", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "MenuDiffCallback", "MenuViewHolder", "app_release"})
public final class MenuAdapter extends androidx.recyclerview.widget.ListAdapter<kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu, kr.ac.nsu.hakbokgs.main.store.taesan.main.MenuAdapter.MenuViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu, kotlin.Unit> onClick = null;
    
    public MenuAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu, kotlin.Unit> onClick) {
        super(null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kr.ac.nsu.hakbokgs.main.store.taesan.main.MenuAdapter.MenuViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    kr.ac.nsu.hakbokgs.main.store.taesan.main.MenuAdapter.MenuViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/taesan/main/MenuAdapter$MenuDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lkr/ac/nsu/hakbokgs/main/store/taesan/db/Menu;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_release"})
    public static final class MenuDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu> {
        @org.jetbrains.annotations.NotNull()
        public static final kr.ac.nsu.hakbokgs.main.store.taesan.main.MenuAdapter.MenuDiffCallback INSTANCE = null;
        
        private MenuDiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu oldItem, @org.jetbrains.annotations.NotNull()
        kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu oldItem, @org.jetbrains.annotations.NotNull()
        kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu newItem) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0006R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0012"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/taesan/main/MenuAdapter$MenuViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "onClick", "Lkotlin/Function1;", "Lkr/ac/nsu/hakbokgs/main/store/taesan/db/Menu;", "", "(Landroid/view/View;Lkotlin/jvm/functions/Function1;)V", "currentMenu", "menuImageView", "Landroid/widget/ImageView;", "menuTextView", "Landroid/widget/TextView;", "getOnClick", "()Lkotlin/jvm/functions/Function1;", "bind", "menu", "app_release"})
    public static final class MenuViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu, kotlin.Unit> onClick = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView menuTextView = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView menuImageView = null;
        @org.jetbrains.annotations.Nullable()
        private kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu currentMenu;
        
        public MenuViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu, kotlin.Unit> onClick) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlin.jvm.functions.Function1<kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu, kotlin.Unit> getOnClick() {
            return null;
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu menu) {
        }
    }
}