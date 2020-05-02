package heinrich.petar.hr.pretvaranjezvukautext;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
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

//LOG TAGS


public class CustomListView extends ArrayAdapter<Products>  {
    private Activity context;
    private ArrayList<Products> productsList = new ArrayList<Products>();
    private ArrayList<Integer> listIds = new ArrayList<Integer>();
    private ArrayList<View> viewList = new ArrayList<View>();

    private static final String TAG1 = "TAG2 ";
    private static final String TAG3 = "TAG3 ";


    public CustomListView(Activity context, ArrayList<Products> products,ArrayList<Integer>listIds) {
        //dodati layout
        super(context, R.layout.listview_layout,products);
        this.context = context;
        this.productsList = products;
        this.listIds = listIds;
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

            // final View view = super.getView(position, convertView, parent);


            viewHolder=new ViewHolder(r);

             /*if (listIds.size() >0) {
               Log.d(TAG1, "size: " + listIds.size() + " get 0 " + listIds.get(0));

                 Log.d(TAG1,"VANI SAM ************ " );
                 for (Integer i : listIds){
                     Log.d(TAG1,"pocetak petlje ************ " + i);
                     if (i.equals(products.getId())){
                         Log.d(TAG1,"zeleno ************ " + products.getFullNameOfProduct());
                    viewHolder.tvw1.setTextColor(Color.GREEN);
                }else if (!i.equals(products.getId())){
                    viewHolder.tvw1.setTextColor(Color.RED);
                         Log.d(TAG1,"crveno ************ " + products.getFullNameOfProduct());

                 }
                tag(listIds,0);
             }
             }*/
             for (Products p: productsList){
                 Log.d(TAG1,"product "+p.getFullNameOfProduct());
             }
            for (Integer p: listIds){
                Log.d(TAG1,"ids "+p);
            }


         /*    if (listIds.size()>0) {
                 Log.d(TAG1, "positions: " + position + " getitempos " + productsList.get(position).getId()+ " "+productsList.get(position).getFullNameOfProduct());
                 String text = null;
                 text = products.getFullNameOfProduct().toString();
                 Log.d(TAG3, "name: out " + text);
                 if (products.getFullNameOfProduct().equals("Kupljeno *")) {
                         viewHolder.tvw1.setTextColor(Color.BLUE);
                         Log.d(TAG3, "name: " + text);

                         viewHolder.tvw1.setPaintFlags(viewHolder.tvw1.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

                     }



                        // if (listIds.get(position)!=null) {
                           //  Log.d(TAG1, "get: " + listIds.get(position));
                         //}
                    // }else{
                       //  viewHolder.tvw1.setTextColor(Color.BLUE);
                   //  }
                 }*/




            r.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder)r.getTag();



        }
          viewHolder.ivw.setImageResource(products.getImageOfProduct());
          viewHolder.ivw2.setImageResource(products.getImageCheckedItem());

          viewHolder.tvw1.setText(products.getFullNameOfProduct());
          viewHolder.tvw2.setText(products.getQuantity());
          viewHolder.tvw3.setText(products.getCrossedFullNameOfProduct());
          viewHolder.tvw4.setText(products.getPriceOfProduct());

          Log.d(TAG3,"priceof product "+  viewHolder.tvw4.getText().toString());



        return r;
    }



    class ViewHolder{

        TextView tvw1;
        TextView tvw2;

        TextView tvw3;
        TextView tvw4;

        ImageView ivw;
        ImageView ivw2;

        ListView listView;
        ViewHolder(View v){
            tvw1=v.findViewById(R.id.tvNameOfProduct);
            tvw2=v.findViewById(R.id.tvQuntity);

            tvw3=v.findViewById(R.id.tvCrossedNameOfProduct);
            tvw4=v.findViewById(R.id.tvPrice);

            ivw=v.findViewById(R.id.imViewOfProduct);
            ivw2=v.findViewById(R.id.imViewCheck);

            listView =v.findViewById(R.id.listItems);

        }
    }


}
