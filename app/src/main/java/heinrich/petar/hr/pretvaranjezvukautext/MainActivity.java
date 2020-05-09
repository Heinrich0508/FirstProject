package heinrich.petar.hr.pretvaranjezvukautext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {


    //microfon code
    public static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    //shared preferences key enter tag
    public static final String KEY_TAG_1 = "KEY_TAG_808";

    // counter for remove items
    public static int counter = 0;

    //buttons ADD and TAKE counters
    private int BTN_ADD_COUNTER = 1;
    private int BTN_TAKE_OFF = 1;

    //position from clicked item 's list
    private static int OUT_POSITION = 0;

    // views
    ImageButton btn_plus,btn_minus,btn_remove,btn_checkIn,btn_undo,mVoiceButton;
    
    Button banAccept;
   
    TextView mTextView, testTextView,tvShowNameOfProduct,tvGram,tvKg,tvDag,tvLitra,tvKom,tvBoca,tvPak;

    TextSwitcher mTextSwitcher;

    EditText tvSetPriceLine;

    SeekBar mSeekBar;

    ListView listItems;

    String myWord = null;

    //todo - konstante koje postavljaju boje u 2 dialogu
    private static final Integer SELECT_COLOR_MEASURE = R.color.selectedColor;
    private static final Integer DEF_COLOR_MEASURE = R.color.defaultColor;
    private static final Integer SHOW_MEASURE_MAIN = R.color.showMeasure;

    //todo- defaultna mjera "kom"
    private static final String DEF_MEASURE = "kom";


    //todo lista u kojoj se pohranuje rezultat koji dolazi iz vana
    ArrayList<String> result = new ArrayList<String>();

    //todo glavna lista u kojoj se nalaze sve komponete svakog produkta
    public static ArrayList<Products> listOFAllProducts = new ArrayList<Products>();
    // todo glavna sporedna lista koja se uvijek puni nakon svake akcije
    public static ArrayList<Products> specialProductsInList = new ArrayList<Products>();

    //todo-dialog liste:
    //todo - 1 prima podatak za seekbar
    ArrayList<Integer>seekBarStopped = new ArrayList<Integer>();
    //todo - 2 prima podatak koja je mjera pisala
    ArrayList<String>measureLastShowed = new ArrayList<String>();
    //todo - 3 prima pdatak koja je cjena zadnja zabilježena
    ArrayList<String>priceLastShowed = new ArrayList<String>();


    /**
     * kompletno ova situacija sa arraysim se moze bolje organizirat
     */
// todo array sadrzi imena mjernih jedinica koje se pozivaju ovisno o odabranom, razmaknute su zbog pozicjoniranja na sredinu
   String[] arrayOfMeasuresWhitBlankspace ={"     gram"," dekagram","   kilogram","      litra","   komad","    boca","    paket"};
   // todo -array puno ime mjere ispisuje u Toastu
   String[] arrayOfMeasuressToast ={"gram","dekagram","kilogram","litra","komad","boca","paket"};
   //todo - ovaj se konačno ispisuje na listi
   String[] arrayOfMeasureShowOnList ={"g","dag","kg","l","kom","boca","pak"};



    //LOG TAGS
    private static final String TAG1 = "TAG1 ";

// todo- 1 dialog se okida na jedan klik na listu, drugi na long
    AlertDialog showDialogOnLongClick;
    AlertDialog showDialogOneClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAllInList();

        mTextView = findViewById(R.id.textTv);
        mVoiceButton = findViewById(R.id.btnVoice);
        listItems = findViewById(R.id.listItems);
        //Martin
        testTextView = findViewById(R.id.testView);

        sharedPrfesMenager();

            if (specialProductsInList == null){
                specialProductsInList =  new ArrayList<Products>();
            }
            refreshViewList();

        //button click
        mVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
                        //martinTest("sir, kruh");
            }
        });

        //TODO - OD OVUDA POCINJU SVE AKCIJE KOJE SU PROGRAMIRANE NA 1 CLICK 
        //                      ****START****
        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
             final  Products products = specialProductsInList.get(position);
                counter = position;

                showDialogOneClick =  openDialogBoxAndShowNameOfProduct(position);



                /**
                 * This button will remove item from list and delete him
                 */
                btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        specialProductsInList.remove(counter);

                        Toast.makeText(MainActivity.this, "Obrisano !!", Toast.LENGTH_SHORT).show();

                        setSharedPrefernces();
                        showDialogOneClick.dismiss();
                        refreshViewList();
                    }
                });


                /**
                 * The button will change the color of that item in the list
                 */
                btn_checkIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        
                        
                        //todo - mičem ga kada sam ga odabrao
                        specialProductsInList.remove(position);
                        setProductOnSpecificPositionInList(products,10001,true);
                        specialProductsInList.get(specialProductsInList.size()-1).setImageCheckedItem(R.drawable.check_min);


                        setSharedPrefernces();
                        showDialogOneClick.dismiss();
                        refreshViewList();
                    }
                });

                btn_undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        products.setImageCheckedItem(R.drawable.white_background_min);

                        specialProductsInList.remove(position);
                        setProductOnSpecificPositionInList(products,0,false);

                        refreshViewList();
                        setSharedPrefernces();
                        showDialogOneClick.dismiss();
                    }
                });


                //todo gumb metoda za dodavvanje++
                btn_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantityOfItem = 0;
                        String quantityOfItemString =specialProductsInList.get(position).getQuantity();
                        //ako nema nist setiram nulu
                        if (quantityOfItemString.equals("")) {
                            specialProductsInList.get(position).setQuantity(""+BTN_ADD_COUNTER);

                            Toast.makeText(MainActivity.this, "+"+BTN_ADD_COUNTER, Toast.LENGTH_SHORT).show();
                        }else {
                            quantityOfItem = Integer.parseInt(quantityOfItemString);
                            quantityOfItem += BTN_ADD_COUNTER;
                            specialProductsInList.get(position).setQuantity("" + quantityOfItem);

                            Toast.makeText(MainActivity.this, "+"+quantityOfItem, Toast.LENGTH_SHORT).show();
                        }

                        setSharedPrefernces();
                        refreshViewList();
                    }
                });

                //todo gumb metoda za oduzimanje--
                btn_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quantityOfItem = 0;
                        String quantityOfItemString =specialProductsInList.get(position).getQuantity();
                        //ako nema nist setiram nulu
                        if (quantityOfItemString.equals("")) {
                            specialProductsInList.get(position).setQuantity(""+0);
                        }else {
                            quantityOfItem = Integer.parseInt(quantityOfItemString);
                            quantityOfItem -= BTN_TAKE_OFF;
                            if (quantityOfItem<0){
                                quantityOfItem = 0;
                            }else{
                                specialProductsInList.get(position).setQuantity("" + quantityOfItem);
                                Toast.makeText(MainActivity.this, "-"+quantityOfItem, Toast.LENGTH_SHORT).show();
                            }

                        }
                        setSharedPrefernces();
                        refreshViewList();
                    }
                });
            }
        });
        //                          TODO *****END*****

        // todo - OD OVUDA POČINJU SVE AKCIJE KOJE SE IZVRŠAVAJU NA LONG CLICK
        //                      **********start********
        
        listItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                OUT_POSITION = position;
                showDialogOnLongClick = openPriceDialogBox(position);
                return true;
            }
        });
    }
                           
    
    
    


    //todo - metoda otvara alert dialog i ispisuje naslov produkta na koji je korisnik kliknuo i brine
    // o postavljanju chek in gumba ili undo gumba
        private AlertDialog openDialogBoxAndShowNameOfProduct(int position){
            Products p = specialProductsInList.get(position);
            Integer getCheck = R.drawable.check_min;
            boolean verifiyCheckButton = p.getImageCheckedItem().equals(getCheck);
        final  Products products = specialProductsInList.get(position);
        final AlertDialog.Builder aDialogBulder = new AlertDialog.Builder(MainActivity.this);
        final LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.dialog_layout,null);
        btn_plus = (ImageButton) dialogView.findViewById(R.id.btn_plus);
        btn_minus = (ImageButton)dialogView.findViewById(R.id.btn_minus);
        btn_remove = (ImageButton)dialogView.findViewById(R.id.btn_remove);
        btn_undo = (ImageButton)dialogView.findViewById(R.id.btn_undo);
            if (verifiyCheckButton){
                btn_checkIn = (ImageButton)dialogView.findViewById(R.id.btn_checkIn);
                btn_checkIn.setVisibility(View.GONE);
                btn_undo.setVisibility(View.VISIBLE);
            }else{
                btn_checkIn = (ImageButton)dialogView.findViewById(R.id.btn_checkIn);
            }
        tvShowNameOfProduct = (TextView) dialogView.findViewById(R.id.tvShowNameOfProduct);
        tvShowNameOfProduct.setText(products.getFullNameOfProduct());
        tvShowNameOfProduct.setTextColor(Color.BLUE);
        aDialogBulder.setView(dialogView);
        final AlertDialog alertDialog = aDialogBulder.create();
        alertDialog.show();
        return alertDialog;
    }

    private AlertDialog openPriceDialogBox(int position){
       final Products p = specialProductsInList.get(position);
        final AlertDialog.Builder aDialogBulder = new AlertDialog.Builder(MainActivity.this);
        final LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.dialog_price_layout,null);



    //todo - new attributes
        tvShowNameOfProduct = (TextView)dialogView.findViewById(R.id.tvShowNameOfProduct);
        tvSetPriceLine = (EditText) dialogView.findViewById(R.id.tvSetPriceLine);
        banAccept = (Button)dialogView.findViewById(R.id.btnAcept);

        tvGram = (TextView)dialogView.findViewById(R.id.tvGram);
        tvDag = (TextView)dialogView.findViewById(R.id.tvDag);
        tvKg = (TextView)dialogView.findViewById(R.id.tvKg);
        tvLitra = (TextView)dialogView.findViewById(R.id.tvLitra);
        tvKom = (TextView)dialogView.findViewById(R.id.tvKom);
        tvBoca = (TextView)dialogView.findViewById(R.id.tvBoca);
        tvPak = (TextView)dialogView.findViewById(R.id.tvPak);

        tvShowNameOfProduct.setText(p.getFullNameOfProduct());


        //todo - shared prefs menager koji ce ucitat zadnje stanje na dialogu
       /* if (!priceLastShowed.isEmpty() ){

            sharedPrefsMenagerDialog();
            tvSetPriceLine.setText(priceLastShowed.get(0));

        }*/

        setFocusOnEditTextDialog(tvSetPriceLine);





        //todo - neznam kako ovo točno funkcionira trebalo bi mi iz upisanih brojeva stvorit broj na 2 decimale
     //   tvSetPriceLine.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5,2)});


        final TextView[] tvTest = {null};

        mTextSwitcher =(TextSwitcher)dialogView.findViewById(R.id.mTextSwitcher);
        mSeekBar = (SeekBar)dialogView.findViewById(R.id.seekBar);
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                 tvTest[0] = new TextView(MainActivity.this);
                tvTest[0].setTextColor(ContextCompat.getColor(MainActivity.this, SHOW_MEASURE_MAIN));
                tvTest[0].setTextSize(20);
                return tvTest[0];
            }
        });


        final int[] senderInformationFlag = {-1};
        //todo - defaultna metoda koja postavja text,boju i odabranu def mjernu jedinicu
        setDefaultMeasureAndColor(mTextSwitcher,mSeekBar,tvKom,senderInformationFlag);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                setDefaultColor();
                if (progress <= 100 ){
               mTextSwitcher.setText(arrayOfMeasuresWhitBlankspace[0]);
               tvGram.setTextColor(ContextCompat.getColor(MainActivity.this, SELECT_COLOR_MEASURE));
                    senderInformationFlag[0] = 0;
           }else if (progress <= 200 ){
               mTextSwitcher.setText(arrayOfMeasuresWhitBlankspace[1]);
               tvDag.setTextColor(ContextCompat.getColor(MainActivity.this, SELECT_COLOR_MEASURE));
                    senderInformationFlag[0] = 1;
           }else if (progress <= 300){
               mTextSwitcher.setText(arrayOfMeasuresWhitBlankspace[2]);
               tvKg.setTextColor(ContextCompat.getColor(MainActivity.this, SELECT_COLOR_MEASURE));
                    senderInformationFlag[0] = 2;
           }else if (progress <= 400){
               mTextSwitcher.setText(arrayOfMeasuresWhitBlankspace[3]);
               tvLitra.setTextColor(ContextCompat.getColor(MainActivity.this, SELECT_COLOR_MEASURE));
                    senderInformationFlag[0] = 3;
           }else if (progress <= 500){
               mTextSwitcher.setText(arrayOfMeasuresWhitBlankspace[4]);
               tvKom.setTextColor(ContextCompat.getColor(MainActivity.this, SELECT_COLOR_MEASURE));
                    senderInformationFlag[0] = 4;
           }else if (progress <= 600){
               mTextSwitcher.setText(arrayOfMeasuresWhitBlankspace[5]);
               tvBoca.setTextColor(ContextCompat.getColor(MainActivity.this, SELECT_COLOR_MEASURE));
                    senderInformationFlag[0] = 5;
           }else if (progress <= 700){
               mTextSwitcher.setText(arrayOfMeasuresWhitBlankspace[6]);
               tvPak.setTextColor(ContextCompat.getColor(MainActivity.this, SELECT_COLOR_MEASURE));
                    senderInformationFlag[0] = 6;
           }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        //todo - kad se stisne ovaj gumb cjena i mjera odlaze na listu
        banAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyEditText(tvSetPriceLine);
                String price = tvSetPriceLine.getText().toString();
                String measureToast = getNameOfMeasureToast(senderInformationFlag);
                String measureListShow = getNameOfMeasureShowOnList(senderInformationFlag);
                Toast.makeText(MainActivity.this, " "+p.getFullNameOfProduct()+", "+price+" kn,"+measureToast, Toast.LENGTH_LONG).show();
                p.setPriceOfProduct(price+" kn");
                p.setMeasure(measureListShow);

                // todo - add in list for shared prfes
                seekBarStopped.add(150);
                measureLastShowed.add(measureListShow);
                priceLastShowed.add(price);
                setSharedPrefernces();
                //setDialogSharedPrefs();

                showDialogOnLongClick.dismiss();

            }
        });




        aDialogBulder.setView(dialogView);
        final AlertDialog alertDialog = aDialogBulder.create();
        alertDialog.show();
        return alertDialog;
    }

    //todo - defaultna metoda koja postavja text,boju i odabranu def mjernu jedinicu
    private void setDefaultMeasureAndColor(TextSwitcher mTextSwitcher,SeekBar mSeekBar,TextView tvKom,int[]senderInformationFlag){
        // todo sharedPrefs
      /*  if (!measureLastShowed.isEmpty()){
            sharedPrefsMenagerDialog();
            mTextSwitcher.setCurrentText(measureLastShowed.get(OUT_POSITION));*/

            mSeekBar.setMax(700);
            mSeekBar.setProgress(420);
            mTextSwitcher.setCurrentText(arrayOfMeasuresWhitBlankspace[4]);
            tvKom.setTextColor(ContextCompat.getColor(MainActivity.this, SELECT_COLOR_MEASURE));
            senderInformationFlag[0] = 4;



    }

    private String getNameOfMeasureToast(int[] senderInformationFlag) {
        if (senderInformationFlag[0] == 0) {
            return " " + arrayOfMeasuressToast[0] + " ";
        } else if (senderInformationFlag[0] == 1) {
            return " " + arrayOfMeasuressToast[1] + " ";
        }else if (senderInformationFlag[0] == 2) {
            return " " + arrayOfMeasuressToast[2] + " ";
        }else if (senderInformationFlag[0] == 3) {
            return " " + arrayOfMeasuressToast[3] + " ";
        }else if (senderInformationFlag[0] == 4) {
            return " " + arrayOfMeasuressToast[4] + " ";
        }else if (senderInformationFlag[0] == 5) {
            return " " + arrayOfMeasuressToast[5] + " ";
        }else if (senderInformationFlag[0] == 6) {
            return " " + arrayOfMeasuressToast[6] + " ";
        }
        else {
            return "";
        }
    }

    private String getNameOfMeasureShowOnList(int[] senderInformationFlag) {
        if (senderInformationFlag[0] == 0) {
            return " " + arrayOfMeasureShowOnList[0] + " ";
        } else if (senderInformationFlag[0] == 1) {
            return " " + arrayOfMeasureShowOnList[1] + " ";
        }else if (senderInformationFlag[0] == 2) {
            return " " + arrayOfMeasureShowOnList[2] + " ";
        }else if (senderInformationFlag[0] == 3) {
            return " " + arrayOfMeasureShowOnList[3] + " ";
        }else if (senderInformationFlag[0] == 4) {
            return " " + arrayOfMeasureShowOnList[4] + " ";
        }else if (senderInformationFlag[0] == 5) {
            return " " + arrayOfMeasureShowOnList[5] + " ";
        }else if (senderInformationFlag[0] == 6) {
            return " " + arrayOfMeasureShowOnList[6] + " ";
        }
        else {
            return "";
        }
    }

    private void verifyEditText(EditText editText){
        String validText = editText.getText().toString();
        int lenght = validText.length();
        boolean noDecimalPlaces = !validText.contains(".");
        if (lenght > 0){
            if (noDecimalPlaces){
                    tvSetPriceLine.setText(validText+".00");
            }


        }
    }


    //todo - postavlja defaultnu boju na pokazivačima mjera
    private void setDefaultColor(){
        tvGram.setTextColor(ContextCompat.getColor(MainActivity.this,DEF_COLOR_MEASURE));
        tvDag.setTextColor(ContextCompat.getColor(MainActivity.this, DEF_COLOR_MEASURE));
        tvKg.setTextColor(ContextCompat.getColor(MainActivity.this, DEF_COLOR_MEASURE));
        tvLitra.setTextColor(ContextCompat.getColor(MainActivity.this, DEF_COLOR_MEASURE));
        tvKom.setTextColor(ContextCompat.getColor(MainActivity.this, DEF_COLOR_MEASURE));
        tvBoca.setTextColor(ContextCompat.getColor(MainActivity.this, DEF_COLOR_MEASURE));
        tvPak.setTextColor(ContextCompat.getColor(MainActivity.this,DEF_COLOR_MEASURE));
    }


    private void setDialogSharedPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_TAG_1, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonSeekBarStopped = gson.toJson(seekBarStopped);
        String jsonPrice = gson.toJson(priceLastShowed);
        String jsonShowMeasure = gson.toJson(measureLastShowed);
        prefsEditor.putString("DialogObject",jsonSeekBarStopped);
        prefsEditor.putString("DialogObject1",jsonPrice);
        prefsEditor.putString("DialogObject2",jsonShowMeasure);
        prefsEditor.apply();
    }

    //todo metoda setira preference
    private void setSharedPrefernces(){
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_TAG_1, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonRemovedItems = gson.toJson(specialProductsInList);
        String jsonAddDeletedItems = gson.toJson(specialProductsInList);
        String jsonChekedItems = gson.toJson(specialProductsInList);
        String jsonQuantityAdd = gson.toJson(specialProductsInList);
        String jsonQuantityTake = gson.toJson(specialProductsInList);
        String jsonUndoPosition = gson.toJson(specialProductsInList);
        String jsonPriceFromDialog = gson.toJson(specialProductsInList);
        prefsEditor.putString("MyObject", jsonRemovedItems);
        prefsEditor.putString("MyObject2", jsonAddDeletedItems);
        prefsEditor.putString("MyObject5", jsonChekedItems);
        prefsEditor.putString("MyObject6", jsonQuantityAdd);
        prefsEditor.putString("MyObject7", jsonQuantityTake);
        prefsEditor.putString("MyObject8", jsonUndoPosition);
        prefsEditor.putString("MyObject9", jsonPriceFromDialog);
        prefsEditor.apply();
    }

    private void sharedPrefsMenagerDialog(){
        Gson gson = new Gson();
        Type typeInt = new TypeToken<ArrayList<Integer>>() { }.getType();
        Type typeString = new TypeToken<ArrayList<String>>() { }.getType();
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_TAG_1, Context.MODE_PRIVATE);

      /*  String jsonSeekBarStopped = sharedPreferences.getString("DialogObject", "");
        seekBarStopped = gson.fromJson(jsonSeekBarStopped, typeInt);*/

        String jsonPrice = sharedPreferences.getString("DialogObject1", "");
        priceLastShowed = gson.fromJson(jsonPrice, typeString);

        String jsonShowMeasure = sharedPreferences.getString("DialogObject2", "");
        measureLastShowed = gson.fromJson(jsonShowMeasure, typeString);

    }

    //todo - metoda upravlja sa shared preferencima - dohvaca ih
    private void sharedPrfesMenager() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Products>>() { }.getType();
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_TAG_1, Context.MODE_PRIVATE);

        String jsonRemovedItems = sharedPreferences.getString("MyObject", "");
        specialProductsInList = gson.fromJson(jsonRemovedItems, type);

        String jsonSelectedItem = sharedPreferences.getString("MyObject2", "");
        specialProductsInList = gson.fromJson(jsonSelectedItem, type);

        String addDeletAllProductsJson = sharedPreferences.getString("MyObject5", "");
        specialProductsInList = gson.fromJson(addDeletAllProductsJson, type);

        String addBtn = sharedPreferences.getString("MyObject6", "");
        specialProductsInList = gson.fromJson(addBtn, type);

        String takeBtn = sharedPreferences.getString("MyObject7", "");
        specialProductsInList = gson.fromJson(takeBtn, type);

        String undoPosition = sharedPreferences.getString("MyObject8", "");
        specialProductsInList = gson.fromJson(undoPosition, type);

        String priceFromDialog = sharedPreferences.getString("MyObject9", "");
        specialProductsInList = gson.fromJson(priceFromDialog, type);
    }



    //todo - metoda postavlja fokus na edit text u dilaogu
private void setFocusOnEditTextDialog(final EditText editText){
    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            editText.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputMethodManager= (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
    });
    editText.requestFocus();
}

    //add all to list
    public static void setAllInList(){

        listOFAllProducts.add(new Products(-1,".*KRUH.*","KRUH","",R.drawable.bread_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(0,".*SIR.*","SIR","",R.drawable.cheese_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(1,".*SALAM.*","SALAMA","",R.drawable.salami_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(2,".*PIV.*","PIVA","",R.drawable.beer,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(3,".*ČAJ.*","ČAJ","",R.drawable.caj_2,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(4,".*ČOKOLAD.*","ČOKOLADA","",R.drawable.chocolate,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(5,".*VIN.*","VINO","",R.drawable.vino_2,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(6,".*ANANAS.*","ANANAS","",R.drawable.penaple,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(7,".*GROŽĐ.*","GROŽĐE","",R.drawable.grozd,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(8,".*KAV.*","KAVA","",R.drawable.cofee_cup,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(9,".*KRUMPIR.*","KRUMPIR","",R.drawable.potato,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(10,".*NARANČ.*","NARANČA","",R.drawable.orange,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(11,".*SOL.*","SOL","",R.drawable.sol_2,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(12,".*PEKMEZ.*","PEKMEZ","",R.drawable.jam,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(13,".*JAGOD.*","JAGODE","",R.drawable.jagoda,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(14,".*RIB.*","RIBE","",R.drawable.fish_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(15,".*JABUK.*","JABUKA","",R.drawable.apple_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(16,".*JAJ.*","JAJA","",R.drawable.egg_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(17,".*MLIJEK.*","MLIJEKO","",R.drawable.milk_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(18,".*MED.*","MED","",R.drawable.apitherapy_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(19,".*RAKIJ.*","RAKIJA","",R.drawable.brandy_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(20,".*BROKUL.*","BROKULA","",R.drawable.broccoli_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(21,".*MRKV.*","MRKVA","",R.drawable.carrot_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(22,".*ČIPS.*","ČIPS","",R.drawable.chips_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(23,".*NESCAFE.*","NESCAFE","",R.drawable.coffee_cup_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(24,".*KVAS.*","KVASAC","",R.drawable.dough_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(25,".*COCA-COL.*","COCA-COLA","",R.drawable.drink_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(26,".*BRAŠN.*","BRAŠNO","",R.drawable.flour_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(27,".*ČEŠNJ.*","ČEŠNJAK","",R.drawable.garlic_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(28,".*KIV.*","KIVI","",R.drawable.kiwi_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(29,".*TJESTE.*","TJESTENINA","",R.drawable.macaroni_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(30,".*MAJONEZ.*","MAJONEZA","",R.drawable.mayonnaise_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(31,".*KOKIC.*","KOKICE","",R.drawable.popcorn_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(32,".*ŽGANC.*","ŽGANCI","",R.drawable.porridge_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(33,".*RIŽ.*","RIŽA","",R.drawable.rice_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(34,".*PAP.*","PAPAR","",R.drawable.salt_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(35,".*ŠPAGET.*","ŠPAGETI","",R.drawable.spaghetti_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(36,".*MINERAL.* VOD.*","MINERALNA VODA","",R.drawable.sparkling_water_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(37,".*ŠEĆER.*","ŠEĆER","",R.drawable.sugar_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(38,".*MASLA.*","MASLAC","",R.drawable.toast_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(39,".*VEGET.*","VEGETA","",R.drawable.vegetable_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(40,".*VOD.*","VODA","",R.drawable.water_bottle_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(41,".*JOGURT.*","JOGURT","",R.drawable.yogurt_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(42,".*CIKL.*","CIKLA","",R.drawable.beetroot_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(43,".*DETERDŽENT.*","DETERDŽENT","",R.drawable.bleach_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(44,".*PILETIN.*","PILETINA","",R.drawable.chicken_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(45,".*KROASAN.*","KROASAN","",R.drawable.croissant_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(46,".*MASLINO.* ULJ.*","MASLINOVO ULJE","",R.drawable.dippel_oil_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(47,".*MES.*","MESO","",R.drawable.meat_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(48,".*SOK.*","SOK","",R.drawable.orange_juice_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(49,".*SVINJETIN.*","SVINJETINA","",R.drawable.pig_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(50,".*PARADAJZ.*","PARADAJZ","",R.drawable.tomato_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(51,".*PAST.*ZUBE.*","ZUBNA PASTA","",R.drawable.toothbrush_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(52,".*JANJETIN.*","JANJETINA","",R.drawable.lamb_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(53,".*MAHUN.*","MAHUNE","",R.drawable.green_beans_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(54,".*KUPUS.*","KUPUS","",R.drawable.vegetarian_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(55,".*PERŠIN.*","PERŠIN","",R.drawable.parsley_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(56,".*KRASTAV.*","KRASTAVAC","",R.drawable.cucumber_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(57,".*KISE.* KRASTAV.*","KISELI KRASTAVAC","",R.drawable.jar_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(58,".* LUK.*","LUK","",R.drawable.onion_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(59,".*PAPRIK.*","PAPRIKA","",R.drawable.paprika_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(60,".*FEFERON.*","FEFERON","",R.drawable.chili_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(61,".*SALAT.*","SALATA","",R.drawable.salad_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(62,".*GRAH.*","GARAH","",R.drawable.bean_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(63,".*LJEŠNJ.*","LJEŠNJAK","",R.drawable.nut_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(64,".*WC PAPIR.*","WC PAPIR","",R.drawable.wc_papir_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(65,".*SMRZNUT.* POVRĆ.*","SMRZNUTO POVRĆE","",R.drawable.vegetable_garden_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(66,".*OCA.*","OCAT","",R.drawable.vinegar_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(67,".*ORAH.*","ORAH","",R.drawable.walnut_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(68,".*VOĆN.* JOGURT.*","VOĆNI JOGURT","",R.drawable.yogurt_voc_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(69,".*BANAN.*","BANANE","",R.drawable.banana_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(70,".*UGLJEN.*","UGLJEN","",R.drawable.coal_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(71,".*POMFR.*","POMFRIT","",R.drawable.pomes_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(72,".*ŠKAMP.*","ŠKAMPE","",R.drawable.skamp_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(73,".*PORILUK.*","PORILUK","",R.drawable.leek_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(74,".*GRAŠ.*","GARAŠAK","",R.drawable.legume_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(75,".*LIMU.*","LIMUN","",R.drawable.lemon_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(76,".*KRUŠK.*","KRUŠKA","",R.drawable.pear_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(77,".*PRAŠ.*ZA.*VE.*","PRAŠAK ZA PRANJE VEŠA","",R.drawable.powder_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(78,".*SAPUN.*","SAPUN","",R.drawable.soap_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(79,".*JUH.*","JUHA","",R.drawable.soup_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(80,".*AVOKAD.*","AVOKADO","",R.drawable.avocado_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(81,".*HAMBURGER.*","HAMBURGER","",R.drawable.burger_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(82,".*GRICKALIC.*","GRICKALICE","",R.drawable.chocolate_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(83,".*CRVEN.*PAP.*","CRVENA PAPRIKA","",R.drawable.crvena_pa_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(84,".*PUDING.*","PUDING","",R.drawable.dessert_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(85,".*GLJIV.*","GLJIVE","",R.drawable.gljive_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(86,".*KIKI.*","KIKI - RIKI","",R.drawable.kiki_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(87,".*PAPIR.*ZA.*PEČ.*","PAPIR ZA PEČENJE","",R.drawable.papir_za_kuh_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(88,".*LJEK.*","LJEK","",R.drawable.pharmacy_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(89,".*ŠAMPINJON.*","ŠAMPINJONI","",R.drawable.sampinjoni_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(90,".*SIR.*NAMAZ.*","SIRNI NAMAZ","",R.drawable.sirni_namaz_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(91,".*ŠKOLJK.*","ŠKOLJKE","",R.drawable.clam_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(92,".*GRIZ.*","GRIZ","",R.drawable.couscous_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(93,".*KNEDL.*","KNEDLE","",R.drawable.dumpling_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(94,".*SPUŽV.*","SPUŽVA","",R.drawable.spuzva_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(95,".*PARMEZAN.*","PARMEZAN","",R.drawable.grater_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(96,".*ŠUNK.*","ŠUNKA","",R.drawable.ham_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(97,".*SLADOLED.*","SLADOLED","",R.drawable.sladoled_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(98,".*KEČAP.*","KEČAP","",R.drawable.kecap_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(99,".*PARADAJZ.*TUB.*","PARADAJZ U TUBI","",R.drawable.par_tuba_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(100,".*KOBAS.*","KOBASE","",R.drawable.kobasa_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(101,".*PAŠTET.*","PAŠTETA","",R.drawable.pate_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(102,".*BONBONJER.*","BONBONJERA","",R.drawable.snack_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(103,".*ULJ.*","ULJE","",R.drawable.oil_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(104,".*ŠLAG.*","ŠLAG","",R.drawable.slag_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(105,".*ŠPEK.*","ŠPEK","",R.drawable.bacon_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(106,".*KOLAČ.*","KOLAČ","",R.drawable.kolac_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(107,".*TORT.*","TORTA","",R.drawable.torta_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(108,".*BONBON.*","BONBONI","",R.drawable.candy_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(109,".*BAT.*","BATAK","",R.drawable.chicke2n_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(110,".*SNIKERS.*","SNIKERS","",R.drawable.cokoladica_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(111,".*KONDOM.*","KONDOM","",R.drawable.condom_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(112,".*PJEN.*ZA.*B.*","PJENA ZA BRIJANJE","",R.drawable.shave_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(113,".*ČETKIC.*","ČETKICA ZA ZUBE","",R.drawable.toothbrush2_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(114,".*VLAŽN.*M.*C.*","VLAŽNE MARAMICE","",R.drawable.wet_wipes_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(115,".*MARAMIC.*","MARAMICE","",R.drawable.sneezing_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(116,".*TENISIC.*","TENISICE","",R.drawable.feet_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(117,".*CEDEVIT.*","CEDEVITA","",R.drawable.juice_box_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(118,".*BATERIJ.*","BATERIJA","",R.drawable.power_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(119,".*KEKS.*","KEKSI","",R.drawable.biscuit_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(120,".*MLINIC.*","MLINICI","",R.drawable.tortillas_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(121,".*KUKURUZ.*","KUKURUZ","",R.drawable.corn_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(122,".*VRHNJ.* I S.*","VRHNJE I SIR","",R.drawable.cream_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(123,".*MLAD.* LUK.*","MLADI LUK","",R.drawable.onion_young_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(124,".*MARELIC.*","MARELICA","",R.drawable.apricot_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(125,".*ŽITARIC.*","ŽITARICE","",R.drawable.cereal_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(126,".*KAP.*","KAPA","",R.drawable.cap_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(127,".*DASK.* ZA .*REZANJE.*","DASKA ZA REZANJE","",R.drawable.knife_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(128,".*TREŠNJ.*","TREŠNJA","",R.drawable.fruit_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(129,".*BOROVNIC.*","BOROVNICA","",R.drawable.blueberry_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(130,".*PREGAČ.*","PREGAČA","",R.drawable.apron_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(131,".*KABANIC.*","KABANIC","",R.drawable.raincoat_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(132,".*KOŠULJ.*","KOŠULJA","",R.drawable.clothes_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(133,".*MAJCA.*KR.*","MAJCA KRATKIH RUKAVA","",R.drawable.dirtyshirt_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(134,".*ČARAP.*","ČARAPE","",R.drawable.socks_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(135,".*VEST.*","VESTA","",R.drawable.clothesdif_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(136,".*ČIZM.*","ČIZME","",R.drawable.boot_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(137,".*SANDAL.*","SANDALE","",R.drawable.sandals_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(138,".*NAOČAL.*","NAOČALE","",R.drawable.glasses_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(139,".*SUNČ.* NAOČ.*","SUNČANE NAOČALE","",R.drawable.fashion_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(140,".*TEPIH.*","TEPIH","",R.drawable.carpet_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(141,".*ŽARULJ.*","ŽARULJA","",R.drawable.idea_min,"",R.drawable.white_background_min,DEF_MEASURE));
        listOFAllProducts.add(new Products(142,".*KART.*","KARTE","",R.drawable.casino_min,"",R.drawable.white_background_min,DEF_MEASURE));
    }

        private void refreshViewList(){
        final CustomListView  customListView = new CustomListView(MainActivity.this,specialProductsInList);
        listItems.setAdapter(customListView);
        customListView.notifyDataSetChanged();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void speak(){
        //intetnt
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hr-HR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Šta ćeš danas kupit?");
        //start intent
        try {
            Log.d("tryBlok: ","da");
         startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e){

            // todo PETAR NE KUZIM KAD SE IZVRSAVA OVAJ EXEPTION NIKAK DA GA HANDLA
            //  zato sam dodao if uvjet   if (data !=null) udi je označeno sa */*/*
            Log.d("tryBlok: ","ne");
            Toast.makeText(this, "Nisam te čuo"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{

                // todo */*/*
                if (data !=null) {
                result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (resultCode == RESULT_OK ) {
                    myWord = result.get(0).toUpperCase(); //my word je string

                    //Martin START filter

                    Filter filter = new Filter();
                    String[] convertedString = filter.stringToArray(myWord);
                 //   String test = filter.readListReturnAdjectives(convertedString);
                 //   testTextView.setText(test);

                    //Martin END
                    readList(myWord);
                }


                }
                else {
                    Toast.makeText(this, "Nisam razumio", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //TODO - MARTIN
    private void readList(String myWord){
        //Martin START filter
       /* String realAdjective=null;

        Filter filter = new Filter();
        String[] convertedString = filter.stringToArray(myWord);

        String returnedAdjectiveToTheProduct = filter.readListReturnAdjectives(convertedString);

        //initialize adjectives list
        //Adjectives adjectives = new Adjectives();
        StringBuilder builder = new StringBuilder();

        for (int i =0; i < convertedString.length;i++){
            builder.append(returnedAdjectiveToTheProduct);
            builder.append(", ");
        }

        testTextView.setText(builder.toString());*/
        //Martin END
        for (Products products : listOFAllProducts){
            if (myWord.contains(products.getFullNameOfProduct()) || myWord.matches(products.getRootProduct())){
                //String potentialAdjective =
                setProductOnSpecificPositionInList(products,0,false);
            }
            if (myWord.equals("OBRIŠI SVE") || myWord.equals("POBRIŠI SVE") || myWord.equals("IZBRIŠI SVE")){
                specialProductsInList.clear();

            }
        }

        setSharedPrefernces();
        refreshViewList();
    }

    //todo - ovom metodo stavljas produkt na zeljno mjesto u listi

    /**
     * Metoda odlucuje na koju poziciju ce biti dodan item u listi.
     * prvi uvjet - se koristi ako item zelimo pozicjonirat na pocetak liste,
     * a moze se korisitit i za bilo koju drugu poziciju osim zadnje
     * drugi uvjet - se koristi za pozicjoniranje na zadnje mjesto u listi
     * (u drugom uvjetu se ne korisit pozicija (int positionInList)
     * pa se moze stavit bilo koji br. npr.1025)
     * @param products - objekt nad kojim se vrsi promjena
     * @param positionInList - pozicija na koje se objekt postavlja
     * @param endPosition - potvrda dali je pozicija zadnja ili ne
     */
    private void setProductOnSpecificPositionInList(Products products,int positionInList,boolean endPosition){
        if (specialProductsInList.size()>0 && !endPosition){
            specialProductsInList.add(positionInList,products);
        }else if (endPosition){
            specialProductsInList.add(products);
        }else{
            specialProductsInList.add(products);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
////martin test
    private void martinTest (String test){
        StringBuilder builder = new StringBuilder();
        Filter filter = new Filter();

        String [] array = filter.stringToArray(test);

        for (int i =0; i < array.length;i++){
            builder.append("testString");
            builder.append(", ");
        }

        //testTextView.setText(builder.toString());

        testTextView.setText(builder.toString());
    }


}
