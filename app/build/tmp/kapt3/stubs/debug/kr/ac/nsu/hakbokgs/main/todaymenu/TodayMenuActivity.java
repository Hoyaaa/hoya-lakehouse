package kr.ac.nsu.hakbokgs.main.todaymenu;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\b2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u0010\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\bH\u0002J\"\u0010\u0019\u001a\u00020\u00132\u0018\u0010\u001a\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u0010\u0012\u0004\u0012\u00020\u00130\u001bH\u0002J\u0012\u0010\u001c\u001a\u00020\u00132\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0014J$\u0010\u001f\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\b2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u0010\u0010!\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lkr/ac/nsu/hakbokgs/main/todaymenu/TodayMenuActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lkr/ac/nsu/hakbokgs/databinding/ActivityTodaymenuBinding;", "calendar", "Ljava/util/Calendar;", "currentDayOfWeek", "", "currentMonth", "currentTime", "Ljava/time/LocalDate;", "currentWeek", "tag", "", "weekMenuData", "", "Lkr/ac/nsu/hakbokgs/main/todaymenu/db/TodayMenu;", "activateBtn", "", "day", "dateButtons", "", "Landroid/view/View;", "getDateNum", "loadWeeklyMenu", "callback", "Lkotlin/Function1;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "selectBtnState", "testButtons", "showTodayMenu", "app_debug"})
public final class TodayMenuActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tag = "TodayMenuActivity";
    private kr.ac.nsu.hakbokgs.databinding.ActivityTodaymenuBinding binding;
    private java.util.List<kr.ac.nsu.hakbokgs.main.todaymenu.db.TodayMenu> weekMenuData;
    @org.jetbrains.annotations.NotNull()
    private final java.time.LocalDate currentTime = null;
    private final int currentDayOfWeek = 0;
    private final int currentMonth = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Calendar calendar = null;
    private final int currentWeek = 0;
    
    public TodayMenuActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadWeeklyMenu(kotlin.jvm.functions.Function1<? super java.util.List<kr.ac.nsu.hakbokgs.main.todaymenu.db.TodayMenu>, kotlin.Unit> callback) {
    }
    
    private final int getDateNum(int day) {
        return 0;
    }
    
    private final void selectBtnState(int day, java.util.Map<java.lang.Integer, ? extends android.view.View> testButtons) {
    }
    
    private final void showTodayMenu(int day) {
    }
    
    private final void activateBtn(int day, java.util.Map<java.lang.Integer, ? extends android.view.View> dateButtons) {
    }
}