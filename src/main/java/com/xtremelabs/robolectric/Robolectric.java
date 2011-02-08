package com.xtremelabs.robolectric;

import android.app.*;
import android.appwidget.*;
import android.bluetooth.*;
import android.content.*;
import android.content.res.*;
import android.database.sqlite.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.hardware.Camera;
import android.location.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;

import java.util.*;

import org.apache.http.*;
import org.apache.http.impl.client.*;

import com.xtremelabs.robolectric.bytecode.*;
import com.xtremelabs.robolectric.shadows.*;
import com.xtremelabs.robolectric.tester.org.apache.http.*;
import com.xtremelabs.robolectric.util.*;

@SuppressWarnings({"UnusedDeclaration"})
public class Robolectric {
    public static Application application;

    public static <T> T newInstanceOf(Class<T> clazz) {
        return RobolectricInternals.newInstanceOf(clazz);
    }

    public static Object newInstanceOf(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz != null) {
                return newInstanceOf(clazz);
            }
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    public static void bindShadowClass(Class<?> shadowClass) {
        RobolectricInternals.bindShadowClass(shadowClass);
    }

    public static void bindDefaultShadowClasses() {
        bindShadowClasses(getDefaultShadowClasses());
    }

    public static void bindShadowClasses(List<Class<?>> shadowClasses) {
        for (Class<?> shadowClass : shadowClasses) {
            bindShadowClass(shadowClass);
        }
    }

    /**
     * Invoke this utility method in tests to reveal which Android api classes and methods are being invoked
     * for which there are no shadows or shadow methods. This helps expose which methods are being invoked
     * either by a third party library or application code which need new shadow methods to be written. Generates
     * output for the current test only.
     */
    public static void logMissingInvokedShadowMethods() {
        ShadowWrangler.getInstance().logMissingInvokedShadowMethods();
    }

    public static List<Class<?>> getDefaultShadowClasses() {
        return Arrays.asList(
                ShadowAbsoluteLayout.class,
                ShadowAbsSpinner.class,
                ShadowAbstractCursor.class,
                ShadowActivity.class,
                ShadowAdapterView.class,
                ShadowAddress.class,
                ShadowAlarmManager.class,
                ShadowAlertDialog.class,
                ShadowAlertDialog.ShadowBuilder.class,
                ShadowApplication.class,
                ShadowAppWidgetManager.class,
                ShadowArrayAdapter.class,
                ShadowAssetManager.class,
                ShadowAsyncTask.class,
                ShadowAudioManager.class,
                ShadowBaseAdapter.class,
                ShadowBitmap.class,
                ShadowBitmapDrawable.class,
                ShadowBitmapFactory.class,
                ShadowBluetoothAdapter.class,
                ShadowBluetoothDevice.class,
                ShadowBundle.class,
                ShadowCamera.class,
                ShadowCameraParameters.class,
                ShadowCameraSize.class,
                ShadowCanvas.class,
                ShadowColorMatrix.class,
                ShadowColorMatrixColorFilter.class,
                ShadowColorStateList.class,
                ShadowComponentName.class,
                ShadowCompoundButton.class,
                ShadowConfiguration.class,
                ShadowConnectivityManager.class,
                ShadowContentResolver.class,
                ShadowContentValues.class,
                ShadowContext.class,
                ShadowContextWrapper.class,
                ShadowContextThemeWrapper.class,
                ShadowCookieManager.class,
                ShadowDefaultRequestDirector.class,
                ShadowDisplay.class,
                ShadowDrawable.class,
                ShadowDialog.class,
                ShadowEditText.class,
                ShadowExpandableListView.class,
                ShadowFloatMath.class,
                ShadowGeocoder.class,
                ShadowGeoPoint.class,
                ShadowHandler.class,
                ShadowImageView.class,
                ShadowIntent.class,
                ShadowIntentFilter.class,
                ShadowItemizedOverlay.class,
                ShadowLayoutInflater.class,
                ShadowLayoutParams.class,
                ShadowListActivity.class,
                ShadowListView.class,
                ShadowLocation.class,
                ShadowLocationManager.class,
                ShadowLooper.class,
                ShadowMapController.class,
                ShadowMapActivity.class,
                ShadowMapView.class,
                ShadowMatrix.class,
                ShadowMediaPlayer.class,
                ShadowMediaRecorder.class,
                ShadowMediaStore.ShadowImages.ShadowMedia.class,
                ShadowMenuInflater.class,
                ShadowMotionEvent.class,
                ShadowNotification.class,
                ShadowNotificationManager.class,
                ShadowNetworkInfo.class,
                ShadowOverlayItem.class,
                ShadowPaint.class,
                ShadowPath.class,
                ShadowPendingIntent.class,
                ShadowPoint.class,
                ShadowPointF.class,
                ShadowPowerManager.class,
                ShadowPreferenceManager.class,
                ShadowRect.class,
                ShadowRemoteViews.class,
                ShadowResources.class,
                ShadowResources.ShadowTheme.class,
                ShadowService.class,
                ShadowSettings.class,
                ShadowSettings.ShadowSecure.class,
                ShadowSettings.ShadowSystem.class,
                ShadowSpannableStringBuilder.class,
                ShadowSQLiteDatabase.class,
                ShadowSQLiteCursor.class,
                ShadowSQLiteOpenHelper.class,
                ShadowSQLiteQueryBuilder.class,
                ShadowSQLiteStatement.class,
                ShadowSurfaceView.class,
                ShadowTextUtils.class,
                ShadowTextView.class,
                ShadowToast.class,
                ShadowTypedArray.class,
                ShadowTypedValue.class,
                ShadowURLSpan.class,
                ShadowView.class,
                ShadowViewGroup.class,
                ShadowViewStub.class,
                ShadowWebSettings.class,
                ShadowWebView.class,
                ShadowWifiManager.class,
                ShadowWindow.class,
                ShadowZoomButtonsController.class
        );
    }

    public static void resetStaticState() {
        ShadowWrangler.getInstance().silence();
        Robolectric.application = new Application();
        ShadowBitmapFactory.reset();
    }

    public static <T> T directlyOn(T shadowedObject) {
        return RobolectricInternals.directlyOn(shadowedObject);
    }

    public static ShadowDrawable shadowOf(Drawable instance) {
        return (ShadowDrawable) shadowOf_(instance);
    }

    public static ShadowToast shadowOf(Toast instance) {
        return (ShadowToast) shadowOf_(instance);
    }

    public static ShadowNetworkInfo shadowOf(NetworkInfo instance) {
        return (ShadowNetworkInfo) shadowOf_(instance);
    }

    public static ShadowConnectivityManager shadowOf(ConnectivityManager instance) {
        return (ShadowConnectivityManager) shadowOf_(instance);
    }

    public static ShadowBitmapDrawable shadowOf(BitmapDrawable instance) {
        return (ShadowBitmapDrawable) shadowOf_(instance);
    }

    public static ShadowZoomButtonsController shadowOf(ZoomButtonsController instance) {
        return (ShadowZoomButtonsController) shadowOf_(instance);
    }

    public static ShadowListView shadowOf(ListView instance) {
        return (ShadowListView) shadowOf_(instance);
    }

    public static ShadowExpandableListView shadowOf(ExpandableListView instance) {
        return (ShadowExpandableListView) shadowOf_(instance);
    }

    public static ShadowActivity shadowOf(Activity instance) {
        return (ShadowActivity) shadowOf_(instance);
    }

    public static ShadowContextWrapper shadowOf(ContextWrapper instance) {
        return (ShadowContextWrapper) shadowOf_(instance);
    }

    public static ShadowApplication shadowOf(Application instance) {
        return (ShadowApplication) shadowOf_(instance);
    }

    public static ShadowContext shadowOf(Context instance) {
        return (ShadowContext) shadowOf_(instance);
    }

    public static ShadowPaint shadowOf(Paint instance) {
        return (ShadowPaint) shadowOf_(instance);
    }

    public static ShadowPath shadowOf(Path instance) {
        return (ShadowPath) shadowOf_(instance);
    }

    public static ShadowListActivity shadowOf(ListActivity instance) {
        return (ShadowListActivity) shadowOf_(instance);
    }

    public static ShadowHandler shadowOf(Handler instance) {
        return (ShadowHandler) shadowOf_(instance);
    }

    public static ShadowColorMatrix shadowOf(ColorMatrix instance) {
        return (ShadowColorMatrix) shadowOf_(instance);
    }

    public static ShadowIntent shadowOf(Intent instance) {
        return (ShadowIntent) shadowOf_(instance);
    }

    public static ShadowView shadowOf(View instance) {
        return (ShadowView) shadowOf_(instance);
    }

    public static ShadowViewGroup shadowOf(ViewGroup instance) {
        return (ShadowViewGroup) shadowOf_(instance);
    }

    public static ShadowWebSettings shadowOf(WebSettings instance) {
        return (ShadowWebSettings) shadowOf_(instance);
    }

    public static ShadowWebView shadowOf(WebView instance) {
        return (ShadowWebView) shadowOf_(instance);
    }

    public static ShadowAdapterView shadowOf(AdapterView instance) {
        return (ShadowAdapterView) shadowOf_(instance);
    }

    public static ShadowTextView shadowOf(TextView instance) {
        return (ShadowTextView) shadowOf_(instance);
    }

    public static ShadowImageView shadowOf(ImageView instance) {
        return (ShadowImageView) shadowOf_(instance);
    }

    public static ShadowRemoteViews shadowOf(RemoteViews instance) {
        return (ShadowRemoteViews) shadowOf_(instance);
    }

    public static ShadowDialog shadowOf(Dialog instance) {
        return (ShadowDialog) shadowOf_(instance);
    }

    public static ShadowDefaultRequestDirector shadowOf(DefaultRequestDirector instance) {
        return (ShadowDefaultRequestDirector) shadowOf_(instance);
    }

    public static ShadowAlertDialog shadowOf(AlertDialog instance) {
        return (ShadowAlertDialog) shadowOf_(instance);
    }

    public static ShadowLooper shadowOf(Looper instance) {
        return (ShadowLooper) shadowOf_(instance);
    }

    public static ShadowCanvas shadowOf(Canvas instance) {
        return (ShadowCanvas) shadowOf_(instance);
    }

    public static ShadowLocationManager shadowOf(LocationManager instance) {
        return (ShadowLocationManager) shadowOf_(instance);
    }

    public static ShadowAppWidgetManager shadowOf(AppWidgetManager instance) {
        return (ShadowAppWidgetManager) shadowOf_(instance);
    }

    public static ShadowResources shadowOf(Resources instance) {
        return (ShadowResources) shadowOf_(instance);
    }

    public static ShadowLayoutInflater shadowOf(LayoutInflater instance) {
        return (ShadowLayoutInflater) shadowOf_(instance);
    }

    public static ShadowMenuInflater shadowOf(MenuInflater instance) {
        return (ShadowMenuInflater) shadowOf_(instance);
    }

    public static ShadowDisplay shadowOf(Display instance) {
        return (ShadowDisplay) shadowOf_(instance);
    }

    public static ShadowAudioManager shadowOf(AudioManager instance) {
        return (ShadowAudioManager) shadowOf_(instance);
    }

    public static ShadowGeocoder shadowOf(Geocoder instance) {
        return (ShadowGeocoder) shadowOf_(instance);
    }

    public static ShadowSQLiteDatabase shadowOf(SQLiteDatabase other) {
        return (ShadowSQLiteDatabase) Robolectric.shadowOf_(other);
    }

    public static ShadowSQLiteCursor shadowOf(SQLiteCursor other) {
        return (ShadowSQLiteCursor) Robolectric.shadowOf_(other);
    }

    public static ShadowSQLiteOpenHelper shadowOf(SQLiteOpenHelper other) {
        return (ShadowSQLiteOpenHelper) Robolectric.shadowOf_(other);
    }

    public static ShadowSQLiteQueryBuilder shadowOf(SQLiteQueryBuilder other) {
        return (ShadowSQLiteQueryBuilder) Robolectric.shadowOf_(other);
    }

    public static ShadowContentValues shadowOf(ContentValues other) {
        return (ShadowContentValues) Robolectric.shadowOf_(other);
    }

    public static ShadowCamera shadowOf(Camera instance) {
        return (ShadowCamera) shadowOf_(instance);
    }

    public static ShadowCameraParameters shadowOf(Camera.Parameters instance) {
        return (ShadowCameraParameters) shadowOf_(instance);
    }

    public static ShadowCameraSize shadowOf(Camera.Size instance) {
        return (ShadowCameraSize) shadowOf_(instance);
    }

    public static ShadowMediaPlayer shadowOf(MediaPlayer instance) {
        return (ShadowMediaPlayer) shadowOf_(instance);
    }

    public static ShadowMediaRecorder shadowOf(MediaRecorder instance) {
        return (ShadowMediaRecorder) shadowOf_(instance);
    }

    public static ShadowAssetManager shadowOf(AssetManager instance) {
        return (ShadowAssetManager) Robolectric.shadowOf_(instance);
    }

    public static ShadowAlarmManager shadowOf(AlarmManager instance) {
        return (ShadowAlarmManager) Robolectric.shadowOf_(instance);
    }

    public static ShadowConfiguration shadowOf(Configuration instance) {
        return (ShadowConfiguration) Robolectric.shadowOf_(instance);
    }

    public static ShadowBitmap shadowOf(Bitmap other) {
        return (ShadowBitmap) Robolectric.shadowOf_(other);
    }

    public static ShadowBluetoothAdapter shadowOf(BluetoothAdapter other) {
        return (ShadowBluetoothAdapter) Robolectric.shadowOf_(other);
    }

    public static ShadowBluetoothDevice shadowOf(BluetoothDevice other) {
        return (ShadowBluetoothDevice) Robolectric.shadowOf_(other);
    }

    public static ShadowMatrix shadowOf(Matrix other) {
        return (ShadowMatrix) Robolectric.shadowOf_(other);
    }

    public static ShadowMotionEvent shadowOf(MotionEvent other) {
        return (ShadowMotionEvent) Robolectric.shadowOf_(other);
    }

    public static ShadowNotificationManager shadowOf(NotificationManager other) {
        return (ShadowNotificationManager) Robolectric.shadowOf_(other);
    }

    public static ShadowNotification shadowOf(Notification other) {
        return (ShadowNotification) Robolectric.shadowOf_(other);
    }

    @SuppressWarnings({"unchecked"})
    public static <P, R> P shadowOf_(R instance) {
        return (P) ShadowWrangler.getInstance().shadowOf(instance);
    }

    /**
     * Runs any background tasks previously queued by {@link android.os.AsyncTask#execute(Object[])}.
     * <p/>
     * <p/>
     * Note: calling this method does not pause or un-pause the scheduler.
     */
    public static void runBackgroundTasks() {
        getBackgroundScheduler().advanceBy(0);
    }

    /**
     * Runs any immediately runnable tasks previously queued on the UI thread,
     * e.g. by {@link Activity#runOnUiThread(Runnable)} or {@link android.os.AsyncTask#onPostExecute(Object)}.
     * <p/>
     * <p/>
     * Note: calling this method does not pause or un-pause the scheduler.
     */
    public static void runUiThreadTasks() {
        getUiThreadScheduler().advanceBy(0);
    }

    /**
     * Sets up an HTTP response to be returned by calls to Apache's {@code HttpClient} implementers.
     *
     * @param statusCode   the status code of the response
     * @param responseBody the body of the response
     */
    public static void addPendingHttpResponse(int statusCode, String responseBody) {
        getFakeHttpLayer().addPendingHttpResponse(statusCode, responseBody);
    }

    /**
     * Sets up an HTTP response to be returned by calls to Apache's {@code HttpClient} implementers.
     *
     * @param httpResponse the response
     */
    public static void addPendingHttpResponse(HttpResponse httpResponse) {
        getFakeHttpLayer().addPendingHttpResponse(httpResponse);
    }

    /**
     * Accessor to obtain HTTP requests made during the current test in the order in which they were made.
     *
     * @param index index of the request to retrieve.
     * @return the requested request.
     */
    public static HttpRequest getSentHttpRequest(int index) {
        return ShadowDefaultRequestDirector.getSentHttpRequest(index);
    }

    /**
     * Accessor to obtain metadata for an HTTP request made during the current test in the order in which they were made.
     *
     * @param index index of the request to retrieve.
     * @return the requested request metadata.
     */
    public static HttpRequestInfo getSentHttpRequestInfo(int index) {
        return ShadowDefaultRequestDirector.getSentHttpRequestInfo(index);
    }

    /**
     * Adds an HTTP response rule. The response will be returned when the rule is matched.
     *
     * @param method   method to match.
     * @param uri      uri to match.
     * @param response response to return when a match is found.
     */
    public static void addHttpResponseRule(String method, String uri, HttpResponse response) {
        getFakeHttpLayer().addHttpResponseRule(method, uri, response);
    }

    /**
     * Adds an HTTP response rule with a default method of GET. The response will be returned when the rule is matched.
     *
     * @param uri      uri to match.
     * @param response response to return when a match is found.
     */
    public static void addHttpResponseRule(String uri, HttpResponse response) {
        getFakeHttpLayer().addHttpResponseRule(uri, response);
    }

    /**
     * Adds an HTTP response rule. The response will be returned when the rule is matched.
     *
     * @param uri      uri to match.
     * @param response response to return when a match is found.
     */
    public static void addHttpResponseRule(String uri, String response) {
        getFakeHttpLayer().addHttpResponseRule(uri, response);
    }

    /**
     * Adds an HTTP response rule. The response will be returned when the rule is matched.
     *
     * @param requestMatcher custom {@code RequestMatcher}.
     * @param response       response to return when a match is found.
     */
    public static void addHttpResponseRule(RequestMatcher requestMatcher, HttpResponse response) {
        getFakeHttpLayer().addHttpResponseRule(requestMatcher, response);
    }

    public static FakeHttpLayer getFakeHttpLayer() {
        return getShadowApplication().getFakeHttpLayer();
    }

    public static void setDefaultHttpResponse(int statusCode, String responseBody) {
        getFakeHttpLayer().setDefaultHttpResponse(statusCode, responseBody);
    }

    public static void setDefaultHttpResponse(HttpResponse defaultHttpResponse) {
        getFakeHttpLayer().setDefaultHttpResponse(defaultHttpResponse);
    }

    public static void pauseLooper(Looper looper) {
        ShadowLooper.pauseLooper(looper);
    }

    public static void unPauseLooper(Looper looper) {
        ShadowLooper.unPauseLooper(looper);
    }

    public static void pauseMainLooper() {
        ShadowLooper.pauseMainLooper();
    }

    public static void unPauseMainLooper() {
        ShadowLooper.unPauseMainLooper();
    }

    public static Scheduler getUiThreadScheduler() {
        return shadowOf(Looper.getMainLooper()).getScheduler();
    }

    public static Scheduler getBackgroundScheduler() {
        return getShadowApplication().getBackgroundScheduler();
    }

    public static ShadowApplication getShadowApplication() {
        return shadowOf(Robolectric.application);
    }

    /**
     * Calls {@code performClick()} on a {@code View} after ensuring that it and its ancestors are visible and that it
     * is enabled.
     *
     * @param view the view to click on
     * @return true if {@code View.OnClickListener}s were found and fired, false otherwise.
     * @throws RuntimeException if the preconditions are not met.
     */
    public static boolean clickOn(View view) {
        return shadowOf(view).checkedPerformClick();
    }

    public static String visualize(View view) {
        Canvas canvas = new Canvas();
        view.draw(canvas);
        return shadowOf(canvas).getDescription();
    }

    public static String visualize(Canvas canvas) {
        return shadowOf(canvas).getDescription();
    }

    public static String visualize(Bitmap bitmap) {
        return shadowOf(bitmap).getDescription();
    }
}
