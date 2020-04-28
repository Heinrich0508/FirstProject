package heinrich.petar.hr.pretvaranjezvukautext;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static heinrich.petar.hr.pretvaranjezvukautext.MainActivity.listOfcheckedItems;

public class CustomListView extends ArrayAdapter<Products>  {
    private Activity context;
    private ArrayList<Products> productsList = new ArrayList<Products>();
    private ArrayList<Integer> listOfCheckedItems = new ArrayList<Integer>();
    private ArrayList<View> viewList = new ArrayList<View>();
    Products listRemeber;
    int positionOfRedId;
    private boolean checked;
   // ListView listView;

    private static int counter = 0;

    public CustomListView(Activity context, ArrayList<Products> products,ArrayList<Integer> listOfCheckedItems) {
        //dodati layout
        super(context, R.layout.listview_layout,products);

        this.context = context;
        this.productsList = products;
        this.listOfCheckedItems = listOfCheckedItems;
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       Products products = productsList.get(position);
       Products products2 = getItem(position);
        View r=convertView;
        ViewHolder viewHolder = null;




        if (r==null){


            LayoutInflater layoutInflater = context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout,null,true);

            viewHolder=new ViewHolder(r);






            r.setTag(viewHolder);



            int po = getPosition(products2);
            Log.d("position","po "+po+ " position "+position);




        }
        else{
            viewHolder=(ViewHolder)r.getTag();
        }
          viewHolder.ivw.setImageResource(products.getImageOfProduct());
          viewHolder.tvw1.setText(products.getFullNameOfProduct());
          viewHolder.tvw2.setText(products.getDescriptionOfProduct());
          viewHolder.positionView = positionOfRedId;


        Log.d("viewHolder",""+viewHolder.positionView+" == "+positionOfRedId);





        return r;
    }



    class ViewHolder{

        TextView tvw1;
        TextView tvw2;
        ImageView ivw;
        ListView listView;
        int positionView;
        ViewHolder(View v){
            tvw1=v.findViewById(R.id.tvNameOfProduct);
            tvw2=v.findViewById(R.id.tvPriceOfProduct);
            ivw=v.findViewById(R.id.imViewOfProduct);
            listView =v.findViewById(R.id.listItems);


            Log.d("listViewW "," "+tvw2);



        }
    }


}
