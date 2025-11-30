package kr.ac.nsu.hakbokgs.main.store.taesan.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B)\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0006J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J-\u0010\u0012\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u0014H\u00d6\u0001J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\u0019\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0014H\u00d6\u0001R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\b\"\u0004\b\f\u0010\nR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\b\"\u0004\b\u000e\u0010\n\u00a8\u0006!"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/taesan/db/Size;", "Landroid/os/Parcelable;", "baekban", "Lkr/ac/nsu/hakbokgs/main/store/taesan/db/SizeOption;", "jeongsik", "basic", "(Lkr/ac/nsu/hakbokgs/main/store/taesan/db/SizeOption;Lkr/ac/nsu/hakbokgs/main/store/taesan/db/SizeOption;Lkr/ac/nsu/hakbokgs/main/store/taesan/db/SizeOption;)V", "getBaekban", "()Lkr/ac/nsu/hakbokgs/main/store/taesan/db/SizeOption;", "setBaekban", "(Lkr/ac/nsu/hakbokgs/main/store/taesan/db/SizeOption;)V", "getBasic", "setBasic", "getJeongsik", "setJeongsik", "component1", "component2", "component3", "copy", "describeContents", "", "equals", "", "other", "", "hashCode", "toString", "", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
@androidx.annotation.Keep()
@kotlinx.parcelize.Parcelize()
public final class Size implements android.os.Parcelable {
    @org.jetbrains.annotations.Nullable()
    private kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption baekban;
    @org.jetbrains.annotations.Nullable()
    private kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption jeongsik;
    @org.jetbrains.annotations.Nullable()
    private kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption basic;
    
    public Size(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption baekban, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption jeongsik, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption basic) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption getBaekban() {
        return null;
    }
    
    public final void setBaekban(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption getJeongsik() {
        return null;
    }
    
    public final void setJeongsik(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption getBasic() {
        return null;
    }
    
    public final void setBasic(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption p0) {
    }
    
    public Size() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kr.ac.nsu.hakbokgs.main.store.taesan.db.Size copy(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption baekban, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption jeongsik, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.taesan.db.SizeOption basic) {
        return null;
    }
    
    @java.lang.Override()
    public int describeContents() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
    
    @java.lang.Override()
    public void writeToParcel(@org.jetbrains.annotations.NotNull()
    android.os.Parcel parcel, int flags) {
    }
}