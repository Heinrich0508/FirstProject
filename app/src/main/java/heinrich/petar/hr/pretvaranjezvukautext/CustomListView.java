package heinrich.petar.hr.pretvaranjezvukautext;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;




public class CustomListView extends ArrayAdapter<Products>  {
    private Activity context;
    private ArrayList<Products> productsList = new ArrayList<Products>();


    //LOG TAGS
    private static final String TAG3 = "TAG3 ";


    public CustomListView(Activity context, ArrayList<Products> products) {
        //dodati layout
        super(context, R.layout.listview_layout,products);
        this.context = context;
        this.productsList = products;
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Products products = productsList.get(position);

        View r=convertView;
        ViewHolder viewHolder = null;

        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)r.getTag();
        }
        viewHolder.ivw.setImageResource(products.getImageOfProduct());
        viewHolder.ivw2.setImageResource(products.getImageCheckedItem());

        viewHolder.tvw1.setText(products.getFullNameOfProduct());
        viewHolder.tvw2.setText(products.getQuantity());
        viewHolder.tvw3.setText(products.getPriceOfProduct());
        viewHolder.tvw4.setText(products.getMeasure());

        Log.d(TAG3,"price of product "+  viewHolder.tvw3.getText().toString());
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


            tvw3=v.findViewById(R.id.tvPrice);
            tvw4=v.findViewById(R.id.tvBrotherQuantity);


            ivw=v.findViewById(R.id.imViewOfProduct);
            ivw2=v.findViewById(R.id.imViewCheck);

            listView =v.findViewById(R.id.listItems);

        }
    }


}




