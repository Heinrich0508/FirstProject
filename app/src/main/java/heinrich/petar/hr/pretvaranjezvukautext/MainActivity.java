package heinrich.petar.hr.pretvaranjezvukautext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.widget.AbsListView.CHOICE_MODE_NONE;


public class MainActivity extends AppCompatActivity {


//microfon code
public static final int REQUEST_CODE_SPEECH_INPUT = 1000;

//shared preferences key
public static final String KEY_TAG_1 = "KEY_TAG_808";
public static final String KEY_TAG_2 = "KEY_TAG_211";
private static int oneItem = 0;
public static int counter = 0;
public static int counte2r = 0;
public static int save = -1;

    ImageButton btn_plus;
    ImageButton btn_minus;
    ImageButton btn_remove;
    ImageButton btn_checkIn;



    // views
    TextView mTextView, testTextView;
    TextView tvShowNameOfProduct;
    ImageButton mVoiceButton;
    ListView listItems;
    String myWord = null;
    Activity context ;
    Context contextOut;

    Parcelable state;
    Bundle bundle;


    ArrayList<String> result = new ArrayList<String>();
    public static ArrayList<Products>listOFAllProducts = new ArrayList<Products>();
    public static ArrayList<Products>specialProductsInList = new ArrayList<Products>();
    public static ArrayList<Integer>listOfcheckedItems = new ArrayList<Integer>();



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





        SharedPreferences sharedPreferences = getSharedPreferences(KEY_TAG_1,Context.MODE_PRIVATE);



        if (sharedPreferences.contains(KEY_TAG_2)) {


            Gson gson = new Gson();
            String json = sharedPreferences.getString("MyObject", "");
            String json2 = sharedPreferences.getString("MyObject3", "");
          //  String json3 = sharedPreferences.getString("MyObject4", "");
            Type type = new TypeToken<ArrayList<Products>>() {}.getType();
            Type type2 = new TypeToken<ArrayList<Integer>>() {}.getType();
          //  Type type3 = new TypeToken<ArrayList<ListView>>() {}.getType();
            specialProductsInList =  gson.fromJson(json, type);
            listOfcheckedItems = gson.fromJson(json2,type2);
          //  listItems = gson.fromJson(json3,type3);
            if (specialProductsInList == null){
                specialProductsInList =  new ArrayList<Products>();
            }
            if (listOfcheckedItems == null){
                listOfcheckedItems = new ArrayList<>();
            }
            Log.d("counterLog",""+listOfcheckedItems.size()+" size IN ONCREATE"); //full

            refreshViewList();


            for (Integer iObject : listOfcheckedItems) {
                 listItems.setItemChecked(iObject, true);
                    listItems.performItemClick(listItems,iObject,listItems.getItemIdAtPosition(iObject));



                Log.d("iobjectSh: ", " " + iObject + " boolean " + listItems.isFocusable());
            }

            Log.d("contextINa"," ");
            final CustomListView  customListView = new CustomListView(MainActivity.this,specialProductsInList,listOfcheckedItems);
            listItems.setAdapter(customListView);
            customListView.notifyDataSetChanged();




        }

        //button click
        mVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
                //martinTest("sir, kruh");


            }
        });
        //refreshViewList();
        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
             final  Products products = specialProductsInList.get(position);
                counter = position;

                //tvShowNameOfProduct =(TextView)view.findViewById(R.id.tvShowNameOfProduct);

               // tvShowNameOfProduct.setBackgroundResource(R.color.colorPrimary);

                final AlertDialog.Builder aDialogBulder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.dialog_layout,null);
                btn_plus = (ImageButton) dialogView.findViewById(R.id.btn_plus);
                btn_minus = (ImageButton)dialogView.findViewById(R.id.btn_minus);
                btn_remove = (ImageButton)dialogView.findViewById(R.id.btn_remove);
                btn_checkIn = (ImageButton)dialogView.findViewById(R.id.btn_checkIn);
                tvShowNameOfProduct = (TextView) dialogView.findViewById(R.id.tvShowNameOfProduct);
                tvShowNameOfProduct.setText(products.getFullNameOfProduct());
                aDialogBulder.setView(dialogView);
                final AlertDialog alertDialog = aDialogBulder.create();
                alertDialog.show();
                /**
                 * This button will remove item from list and delete him
                 */
                btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        specialProductsInList.remove(counter);
                        if (listOfcheckedItems!=null){
                            listOfcheckedItems.remove(products.getId());
                        }
                        Toast.makeText(MainActivity.this, "Obrisano !!", Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = getSharedPreferences(KEY_TAG_1,Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(specialProductsInList);
                        String json2 = gson.toJson(listOfcheckedItems);
                       // String json3 = gson.toJson(listItems);
                        prefsEditor.putString("MyObject", json);
                        prefsEditor.putString("MyObject3", json2);
                       // prefsEditor.putString("MyObject4", json3);
                        prefsEditor.apply();
                        alertDialog.dismiss();
                        refreshViewList();
                    }
                });


                /**
                 * The button will change the color of that item in the list
                 */
                btn_checkIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO - boja se mora postaviti na dodir gumba
                        //TODO -ova metoda !!
                        //v.setBackgroundColor(Color.BLUE);
                        //view.setBackgroundColor(Color.BLUE);
                       // parent.getChildAt(position).setBackgroundColor(Color.BLUE);
                       // v.setBackgroundColor(Color.DKGRAY);

                        LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
                        final View view1 = layoutInflater.inflate(R.layout.listview_layout,null);


                        //tvShowNameOfProduct = (TextView) view1.findViewById(R.id.tvShowNameOfProduct);

                       // tvShowNameOfProduct.setBackgroundResource(R.color.colorPrimary);
                        save = position;

             /**
               * This part of code verify duplicated elements in list, if find them remove it.
               */
                        listOfcheckedItems.add(position);

                        counter = 0;
                        for (int i = 0; i < listOfcheckedItems.size(); i++) {
                            if (position == listOfcheckedItems.get(i)) {

                               oneItem++; // 1 banane , kivi 2
                               if (position == listOfcheckedItems.get(i) && oneItem>1) {
                                   Log.d("inFormation", "" + listOfcheckedItems.get(i) + " zbroj: " + oneItem + " size: " + listOfcheckedItems.size());
                                   listOfcheckedItems.remove(listOfcheckedItems.get(i));
                                   oneItem = 0;
                               }
                            }
                        }
                        oneItem = 0;
                        Log.d("counterLog", "" + listOfcheckedItems.size() + " BEFORE");




                        SharedPreferences sharedPreferences = getSharedPreferences(KEY_TAG_1, Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listOfcheckedItems);
                        prefsEditor.putString("MyObject3", json);
                        prefsEditor.apply();
                        Log.d("counterLog", "" + listOfcheckedItems.size() + " AFTER");
                        alertDialog.dismiss();




                        refreshViewList();




                        Toast.makeText(MainActivity.this, "Martin", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "msg 2", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Viktor", Toast.LENGTH_SHORT).show();




                        Log.d("contextINsert "," "+getApplicationContext()+ " c");

                    }
                });
            }
        });
    }




//TODO - PRIVREMENO
    public static StateListDrawable makeSelector(int color) {
        StateListDrawable res = new StateListDrawable();
        res.setExitFadeDuration(400);
        res.setAlpha(45);
        res.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(color));
        res.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
        return res;
    }

    //add all to list
    public static void setAllInList(){
        listOFAllProducts.add(new Products(0,".*KRUH.*","KRUH","količina: ",R.drawable.bread_min));
        listOFAllProducts.add(new Products(1,".*SIR.*","SIR","količina: ",R.drawable.cheese_min));
       listOFAllProducts.add(new Products(2,".*SALAM.*","SALAMA","količina: ",R.drawable.salami_min));
        listOFAllProducts.add(new Products(3,".*PIV.*","PIVA","količina: ",R.drawable.beer));
        listOFAllProducts.add(new Products(4,".*ČAJ.*","ČAJ","količina: ",R.drawable.caj_2));
        listOFAllProducts.add(new Products(5,".*ČOKOLAD.*","ČOKOLADA","količina: ",R.drawable.chocolate));
        listOFAllProducts.add(new Products(6,".*VIN.*","VINO","količina: ",R.drawable.vino_2));
        listOFAllProducts.add(new Products(6,".*ANANAS.*","ANANAS","količina: ",R.drawable.penaple));
        listOFAllProducts.add(new Products(7,".*GROŽĐ.*","GROŽĐE","količina: ",R.drawable.grozd));
        listOFAllProducts.add(new Products(8,".*KAV.*","KAVA","količina: ",R.drawable.cofee_cup));
        listOFAllProducts.add(new Products(9,".*KRUMPIR.*","KRUMPIR","količina: ",R.drawable.potato));
        listOFAllProducts.add(new Products(10,".*NARANČ.*","NARANČA","količina: ",R.drawable.orange));
        listOFAllProducts.add(new Products(11,".*SOL.*","SOL","količina: ",R.drawable.sol_2));
        listOFAllProducts.add(new Products(12,".*PEKMEZ.*","PEKMEZ","količina: ",R.drawable.jam));
        listOFAllProducts.add(new Products(13,".*JAGOD.*","JAGODE","količina: ",R.drawable.jagoda));
        listOFAllProducts.add(new Products(14,".*RIB.*","RIBE","količina: ",R.drawable.fish_min));
        listOFAllProducts.add(new Products(15,".*JABUK.*","JABUKA","količina: ",R.drawable.apple_min));
        listOFAllProducts.add(new Products(16,".*JAJ.*","JAJA","količina: ",R.drawable.egg_min));
        listOFAllProducts.add(new Products(17,".*MLIJEK.*","MLIJEKO","količina: ",R.drawable.milk_min));
        listOFAllProducts.add(new Products(18,".*MED.*","MED","količina: ",R.drawable.apitherapy_min));
        listOFAllProducts.add(new Products(19,".*RAKIJ.*","RAKIJA","količina: ",R.drawable.brandy_min));
        listOFAllProducts.add(new Products(20,".*BROKUL.*","BROKULA","količina: ",R.drawable.broccoli_min));
        listOFAllProducts.add(new Products(21,".*MRKV.*","MRKVA","količina: ",R.drawable.carrot_min));
        listOFAllProducts.add(new Products(22,".*ČIPS.*","ČIPS","količina: ",R.drawable.chips_min));
        listOFAllProducts.add(new Products(23,".*NESCAFE.*","NESCAFE","količina: ",R.drawable.coffee_cup_min));
        listOFAllProducts.add(new Products(24,".*KVAS.*","KVASAC","količina: ",R.drawable.dough_min));
        listOFAllProducts.add(new Products(25,".*COCA-COL.*","COCA-COLA","količina: ",R.drawable.drink_min));
        listOFAllProducts.add(new Products(26,".*BRAŠN.*","BRAŠNO","količina: ",R.drawable.flour_min));
        listOFAllProducts.add(new Products(27,".*ČEŠNJ.*","ČEŠNJAK","količina: ",R.drawable.garlic_min));
        listOFAllProducts.add(new Products(28,".*KIV.*","KIVI","količina: ",R.drawable.kiwi_min));
        listOFAllProducts.add(new Products(29,".*TJESTE.*","TJESTENINA","količina: ",R.drawable.macaroni_min));
        listOFAllProducts.add(new Products(30,".*MAJONEZ.*","MAJONEZA","količina: ",R.drawable.mayonnaise_min));
        listOFAllProducts.add(new Products(31,".*KOKIC.*","KOKICE","količina: ",R.drawable.popcorn_min));
        listOFAllProducts.add(new Products(32,".*ŽGANC.*","ŽGANCI","količina: ",R.drawable.porridge_min));
        listOFAllProducts.add(new Products(33,".*RIŽ.*","RIŽA","količina: ",R.drawable.rice_min));
        listOFAllProducts.add(new Products(34,".*PAP.*","PAPAR","količina: ",R.drawable.salt_min));
        listOFAllProducts.add(new Products(35,".*ŠPAGET.*","ŠPAGETI","količina: ",R.drawable.spaghetti_min));
        listOFAllProducts.add(new Products(36,".*MINERAL.* VOD.*","MINERALNA VODA","količina: ",R.drawable.sparkling_water_min));
        listOFAllProducts.add(new Products(37,".*ŠEĆER.*","ŠEĆER","količina: ",R.drawable.sugar_min));
        listOFAllProducts.add(new Products(38,".*MASLA.*","MASLAC","količina: ",R.drawable.toast_min));
        listOFAllProducts.add(new Products(39,".*VEGET.*","VEGETA","količina: ",R.drawable.vegetable_min));
        listOFAllProducts.add(new Products(40,".*VOD.*","VODA","količina: ",R.drawable.water_bottle_min));
        listOFAllProducts.add(new Products(41,".*JOGURT.*","JOGURT","količina: ",R.drawable.yogurt_min));
        listOFAllProducts.add(new Products(42,".*CIKL.*","CIKLA","količina: ",R.drawable.beetroot_min));
        listOFAllProducts.add(new Products(43,".*DETERDŽENT.*","DETERDŽENT","količina: ",R.drawable.bleach_min));
        listOFAllProducts.add(new Products(44,".*PILETIN.*","PILETINA","količina: ",R.drawable.chicken_min));
        listOFAllProducts.add(new Products(45,".*KROASAN.*","KROASAN","količina: ",R.drawable.croissant_min));
        listOFAllProducts.add(new Products(46,".*MASLINO.* ULJ.*","MASLINOVO ULJE","količina: ",R.drawable.dippel_oil_min));
        listOFAllProducts.add(new Products(47,".*MES.*","MESO","količina: ",R.drawable.meat_min));
        listOFAllProducts.add(new Products(48,".*SOK.*","SOK","količina: ",R.drawable.orange_juice_min));
        listOFAllProducts.add(new Products(49,".*SVINJETIN.*","SVINJETINA","količina: ",R.drawable.pig_min));
        listOFAllProducts.add(new Products(50,".*PARADAJZ.*","PARADAJZ","količina: ",R.drawable.tomato_min));
        listOFAllProducts.add(new Products(51,".*PAST.*ZUBE.*","ZUBNA PASTA","količina: ",R.drawable.toothbrush_min));
        listOFAllProducts.add(new Products(52,".*JANJETIN.*","JANJETINA","količina: ",R.drawable.lamb_min));
        listOFAllProducts.add(new Products(53,".*MAHUN.*","MAHUNE","količina: ",R.drawable.green_beans_min));
        listOFAllProducts.add(new Products(54,".*KUPUS.*","KUPUS","količina: ",R.drawable.vegetarian_min));
        listOFAllProducts.add(new Products(55,".*PERŠIN.*","PERŠIN","količina: ",R.drawable.parsley_min));
        listOFAllProducts.add(new Products(56,".*KRASTAV.*","KRASTAVAC","količina: ",R.drawable.cucumber_min));
        listOFAllProducts.add(new Products(57,".*KISE.* KRASTAV.*","KISELI KRASTAVAC","količina: ",R.drawable.jar_min));
        listOFAllProducts.add(new Products(58,".* LUK.*","LUK","količina: ",R.drawable.onion_min));
        listOFAllProducts.add(new Products(59,".*PAPRIK.*","PAPRIKA","količina: ",R.drawable.paprika_min));
        listOFAllProducts.add(new Products(60,".*FEFERON.*","FEFERON","količina: ",R.drawable.chili_min));
        listOFAllProducts.add(new Products(61,".*SALAT.*","SALATA","količina: ",R.drawable.salad_min));
        listOFAllProducts.add(new Products(62,".*GRAH.*","GARAH","količina: ",R.drawable.bean_min));
        listOFAllProducts.add(new Products(63,".*LJEŠNJ.*","LJEŠNJAK","količina: ",R.drawable.nut_min));
        listOFAllProducts.add(new Products(64,".*WC PAPIR.*","WC PAPIR","količina: ",R.drawable.wc_papir_min));
        listOFAllProducts.add(new Products(65,".*SMRZNUT.* POVRĆ.*","SMRZNUTO POVRĆE","količina: ",R.drawable.vegetable_garden_min));
        listOFAllProducts.add(new Products(66,".*OCA.*","OCAT","količina: ",R.drawable.vinegar_min));
        listOFAllProducts.add(new Products(67,".*ORAH.*","ORAH","količina: ",R.drawable.walnut_min));
        listOFAllProducts.add(new Products(68,".*VOĆN.* JOGURT.*","VOĆNI JOGURT","količina: ",R.drawable.yogurt_voc_min));
        listOFAllProducts.add(new Products(69,".*BANAN.*","BANANE","količina: ",R.drawable.banana_min));
        listOFAllProducts.add(new Products(70,".*UGLJEN.*","UGLJEN","količina: ",R.drawable.coal_min));
        listOFAllProducts.add(new Products(71,".*POMFR.*","POMFRIT","količina: ",R.drawable.pomes_min));
        listOFAllProducts.add(new Products(72,".*ŠKAMP.*","ŠKAMPE","količina: ",R.drawable.skamp_min));
        listOFAllProducts.add(new Products(73,".*PORILUK.*","PORILUK","količina: ",R.drawable.leek_min));
        listOFAllProducts.add(new Products(74,".*GRAŠ.*","GARAŠAK","količina: ",R.drawable.legume_min));
        listOFAllProducts.add(new Products(75,".*LIMU.*","LIMUN","količina: ",R.drawable.lemon_min));
        listOFAllProducts.add(new Products(76,".*KRUŠK.*","KRUŠKA","količina: ",R.drawable.pear_min));
        listOFAllProducts.add(new Products(77,".*PRAŠ.*ZA.*VE.*","PRAŠAK ZA PRANJE VEŠA","količina: ",R.drawable.powder_min));
        listOFAllProducts.add(new Products(78,".*SAPUN.*","SAPUN","količina: ",R.drawable.soap_min));
        listOFAllProducts.add(new Products(79,".*JUH.*","JUHA","količina: ",R.drawable.soup_min));
        listOFAllProducts.add(new Products(80,".*AVOKAD.*","AVOKADO","količina: ",R.drawable.avocado_min));
        listOFAllProducts.add(new Products(81,".*HAMBURGER.*","HAMBURGER","količina: ",R.drawable.burger_min));
        listOFAllProducts.add(new Products(82,".*GRICKALIC.*","GRICKALICE","količina: ",R.drawable.chocolate_min));
        listOFAllProducts.add(new Products(83,".*CRVEN.*PAP.*","CRVENA PAPRIKA","količina: ",R.drawable.crvena_pa_min));
        listOFAllProducts.add(new Products(84,".*PUDING.*","PUDING","količina: ",R.drawable.dessert_min));
        listOFAllProducts.add(new Products(85,".*GLJIV.*","GLJIVE","količina: ",R.drawable.gljive_min));
        listOFAllProducts.add(new Products(86,".*KIKI.*","KIKI - RIKI","količina: ",R.drawable.kiki_min));
        listOFAllProducts.add(new Products(87,".*PAPIR.*ZA.*PEČ.*","PAPIR ZA PEČENJE","količina: ",R.drawable.papir_za_kuh_min));
        listOFAllProducts.add(new Products(88,".*LJEK.*","LJEK","količina: ",R.drawable.pharmacy_min));
        listOFAllProducts.add(new Products(89,".*ŠAMPINJON.*","ŠAMPINJONI","količina: ",R.drawable.sampinjoni_min));
        listOFAllProducts.add(new Products(90,".*SIR.*NAMAZ.*","SIRNI NAMAZ","količina: ",R.drawable.sirni_namaz_min));
        listOFAllProducts.add(new Products(91,".*ŠKOLJK.*","ŠKOLJKE","količina: ",R.drawable.clam_min));
        listOFAllProducts.add(new Products(92,".*GRIZ.*","GRIZ","količina: ",R.drawable.couscous_min));
        listOFAllProducts.add(new Products(93,".*KNEDL.*","KNEDLE","količina: ",R.drawable.dumpling_min));
        listOFAllProducts.add(new Products(94,".*SPUŽV.*","SPUŽVA","količina: ",R.drawable.spuzva_min));
        listOFAllProducts.add(new Products(95,".*PARMEZAN.*","PARMEZAN","količina: ",R.drawable.grater_min));
        listOFAllProducts.add(new Products(96,".*ŠUNK.*","ŠUNKA","količina: ",R.drawable.ham_min));
        listOFAllProducts.add(new Products(97,".*SLADOLED.*","SLADOLED","količina: ",R.drawable.sladoled_min));
        listOFAllProducts.add(new Products(98,".*KEČAP.*","KEČAP","količina: ",R.drawable.kecap_min));
        listOFAllProducts.add(new Products(99,".*PARADAJZ.*TUB.*","PARADAJZ U TUBI","količina: ",R.drawable.par_tuba_min));
        listOFAllProducts.add(new Products(100,".*KOBAS.*","KOBASE","količina: ",R.drawable.kobasa_min));
        listOFAllProducts.add(new Products(101,".*PAŠTET.*","PAŠTETA","količina: ",R.drawable.pate_min));
        listOFAllProducts.add(new Products(102,".*BONBONJER.*","BONBONJERA","količina: ",R.drawable.snack_min));
        listOFAllProducts.add(new Products(103,".*ULJ.*","ULJE","količina: ",R.drawable.oil_min));
        listOFAllProducts.add(new Products(104,".*ŠLAG.*","ŠLAG","količina: ",R.drawable.slag_min));
        listOFAllProducts.add(new Products(105,".*ŠPEK.*","ŠPEK","količina: ",R.drawable.bacon_min));
        listOFAllProducts.add(new Products(106,".*KOLAČ.*","KOLAČ","količina: ",R.drawable.kolac_min));
        listOFAllProducts.add(new Products(107,".*TORT.*","TORTA","količina: ",R.drawable.torta_min));
        listOFAllProducts.add(new Products(108,".*BONBON.*","BONBONI","količina: ",R.drawable.candy_min));
        listOFAllProducts.add(new Products(109,".*BAT.*","BATAK","količina: ",R.drawable.chicke2n_min));
        listOFAllProducts.add(new Products(110,".*SNIKERS.*","SNIKERS","količina: ",R.drawable.cokoladica_min));
        listOFAllProducts.add(new Products(111,".*KONDOM.*","KONDOM","količina: ",R.drawable.condom_min));
        listOFAllProducts.add(new Products(112,".*PJEN.*ZA.*B.*","PJENA ZA BRIJANJE","količina: ",R.drawable.shave_min));
        listOFAllProducts.add(new Products(113,".*ČETKIC.*","ČETKICA ZA ZUBE","količina: ",R.drawable.toothbrush2_min));
        listOFAllProducts.add(new Products(114,".*VLAŽN.*M.*C.*","VLAŽNE MARAMICE","količina: ",R.drawable.wet_wipes_min));
        listOFAllProducts.add(new Products(115,".*MARAMIC.*","MARAMICE","količina: ",R.drawable.sneezing_min));
        listOFAllProducts.add(new Products(116,".*TENISIC.*","TENISICE","količina: ",R.drawable.feet_min));
        listOFAllProducts.add(new Products(117,".*CEDEVIT.*","CEDEVITA","količina: ",R.drawable.juice_box_min));
        listOFAllProducts.add(new Products(118,".*BATERIJ.*","BATERIJA","količina: ",R.drawable.power_min));
        listOFAllProducts.add(new Products(119,".*KEKS.*","KEKSI","količina: ",R.drawable.biscuit_min));
        listOFAllProducts.add(new Products(120,".*MLINIC.*","MLINICI","količina: ",R.drawable.tortillas_min));
        listOFAllProducts.add(new Products(121,".*KUKURUZ.*","KUKURUZ","količina: ",R.drawable.corn_min));
        listOFAllProducts.add(new Products(122,".*VRHNJ.* I S.*","VRHNJE I SIR","količina: ",R.drawable.cream_min));
        listOFAllProducts.add(new Products(123,".*MLAD.* LUK.*","MLADI LUK","količina: ",R.drawable.onion_young_min));
        listOFAllProducts.add(new Products(124,".*MARELIC.*","MARELICA","količina: ",R.drawable.apricot_min));
        listOFAllProducts.add(new Products(125,".*ŽITARIC.*","ŽITARICE","količina: ",R.drawable.cereal_min));
        listOFAllProducts.add(new Products(126,".*KAP.*","KAPA","količina: ",R.drawable.cap_min));
        listOFAllProducts.add(new Products(127,".*DASK.* ZA .*REZANJE.*","DASKA ZA REZANJE","količina: ",R.drawable.knife_min));
        listOFAllProducts.add(new Products(128,".*TREŠNJ.*","TREŠNJA","količina: ",R.drawable.fruit_min));
        listOFAllProducts.add(new Products(129,".*BOROVNIC.*","BOROVNICA","količina: ",R.drawable.blueberry_min));
        listOFAllProducts.add(new Products(130,".*PREGAČ.*","PREGAČA","količina: ",R.drawable.apron_min));
        listOFAllProducts.add(new Products(131,".*KABANIC.*","KABANIC","količina: ",R.drawable.raincoat_min));
        listOFAllProducts.add(new Products(132,".*KOŠULJ.*","KOŠULJA","količina: ",R.drawable.clothes_min));
        listOFAllProducts.add(new Products(133,".*MAJCA.*KR.*","MAJCA KRATKIH RUKAVA","količina: ",R.drawable.dirtyshirt_min));
        listOFAllProducts.add(new Products(134,".*ČARAP.*","ČARAPE","količina: ",R.drawable.socks_min));
        listOFAllProducts.add(new Products(135,".*VEST.*","VESTA","količina: ",R.drawable.clothesdif_min));
        listOFAllProducts.add(new Products(136,".*ČIZM.*","ČIZME","količina: ",R.drawable.boot_min));
        listOFAllProducts.add(new Products(137,".*SANDAL.*","SANDALE","količina: ",R.drawable.sandals_min));
        listOFAllProducts.add(new Products(138,".*NAOČAL.*","NAOČALE","količina: ",R.drawable.glasses_min));
        listOFAllProducts.add(new Products(139,".*SUNČ.* NAOČ.*","SUNČANE NAOČALE","količina: ",R.drawable.fashion_min));
        listOFAllProducts.add(new Products(140,".*TEPIH.*","TEPIH","količina: ",R.drawable.carpet_min));
        listOFAllProducts.add(new Products(141,".*ŽARULJ.*","ŽARULJA","količina: ",R.drawable.idea_min));
        listOFAllProducts.add(new Products(142,".*KART.*","KARTE","količina: ",R.drawable.casino_min));
    }

        private void refreshViewList(){
        final CustomListView  customListView = new CustomListView(MainActivity.this,specialProductsInList,listOfcheckedItems);
        listItems.setAdapter(customListView);
        customListView.notifyDataSetChanged();

            Log.d("counterLog",""+listOfcheckedItems.size()+" IN REFRESH");
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
         startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e){
            Toast.makeText(this, "Nisam te čuo"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (resultCode == RESULT_OK && data != null) {
                     myWord = result.get(0).toUpperCase(); //my word je string

                     //Martin START filter

                    Filter filter = new Filter();
                    String[] convertedString = filter.stringToArray(myWord);
                    String test = filter.readListReturnAdjectives(convertedString);
                    testTextView.setText(test);

                    //Martin END
                    readList(myWord);
                }
                else{
                    Toast.makeText(this, "Nisam razumio", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //TODO - MARTIN
    private void readList(String myWord){
        for (Products products : listOFAllProducts){
            if (myWord.contains(products.getFullNameOfProduct()) || myWord.matches(products.getRootProduct())){
                //String potentialAdjective =
                specialProductsInList.add(products);
            }
            if (myWord.equals("OBRIŠI SVE")){
                specialProductsInList.clear();
                listOfcheckedItems.clear();
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(KEY_TAG_1,Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(specialProductsInList);
        String json2 = gson.toJson(listOfcheckedItems);
        prefsEditor.putString("MyObject", json);
        prefsEditor.putString("MyObject3", json2);
        prefsEditor.apply();
        prefsEditor.apply();
        refreshViewList();
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
