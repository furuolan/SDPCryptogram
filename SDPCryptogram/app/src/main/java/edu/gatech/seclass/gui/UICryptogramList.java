package edu.gatech.seclass.gui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import edu.gatech.seclass.sdpcryptogram.Cryptogram;
import edu.gatech.seclass.sdpcryptogram.DBHelper;
import edu.gatech.seclass.sdpcryptogram.R;
import edu.gatech.seclass.utilities.CryptogramsUtil;
import edu.gatech.seclass.utilities.ExternalWebService;

/**
 * Created by praveena on 7/3/17.
 */

public class UICryptogramList extends AppCompatActivity {

    //DBHelper sdpdb ;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // sdpdb = new DBHelper(this);
        Bundle bundle = getIntent().getExtras();
        username= bundle.getString("username");
        setContentView(R.layout.activity_cryptogram_list);
        ArrayList<CryptogramsUtil> getCryptograms= Cryptogram.getCryptograms(this,username);
        //ArrayList<CryptogramsUtil> getCryptograms= sdpdb.displayCryptogramsWithStatus1(username);
        ListView cryptList = (ListView) findViewById(R.id.crypt_list);
        if(getCryptograms != null) {
            CryptogramAdapter cryptogramAdapter = new CryptogramAdapter(this,R.layout.cryptogram_list,getCryptograms);
            cryptList.setAdapter(cryptogramAdapter);
            cryptList.setOnItemClickListener(new ItemList());
        }
        else
        {
            Toast.makeText(this,"No Cryptograms found", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public class CryptogramAdapter extends ArrayAdapter<CryptogramsUtil>
    {
        private Context mcontext;
        private int mresource;
        /**
         * Constructor
         *
         * @param context  The current context.
         * @param resource The resource ID for a layout file containing a TextView to use when
         *                 instantiating views.
         * @param objects  The objects to represent in the ListView.
         */
        public CryptogramAdapter(@NonNull Context context, @LayoutRes int resource,
                                 @NonNull ArrayList<CryptogramsUtil> objects) {
            super(context, resource, objects);
            mcontext = context;
            mresource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            String id = getItem(position).getId();
            String cryptogram = getItem(position).getCryptogram();
            String status = getItem(position).getStatus();
            String incorrectcount = getItem(position).getIncorrectcount();

            CryptogramsUtil addCryptogram = new CryptogramsUtil(id,cryptogram,status,incorrectcount);

            LayoutInflater inflator = LayoutInflater.from(mcontext);
            convertView = inflator.inflate(mresource,parent,false);

            TextView tvID = (TextView)convertView.findViewById(R.id.tvid);
            TextView tvpuzzle = (TextView)convertView.findViewById(R.id.tvcrypt);
            TextView tvstatus = (TextView)convertView.findViewById(R.id.tvstatus);
            TextView tvincorrect = (TextView)convertView.findViewById(R.id.tvincorrect);

            tvID.setText(id);
            tvpuzzle.setText(cryptogram);
            tvstatus.setText(status);
            if(incorrectcount == null)
            {
                incorrectcount = "0";
            }
            tvincorrect.setText(incorrectcount);

            return  convertView;
        }
    }

    class ItemList implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ViewGroup viewg=(ViewGroup)view;
            TextView tvcryptID=(TextView)viewg.findViewById(R.id.tvid);
            TextView tvcrypt=(TextView)viewg.findViewById(R.id.tvcrypt);
            int cryptID = Integer.parseInt (tvcryptID.getText().toString());
            String crypt  = tvcrypt.getText().toString();

            Intent newIntent = new Intent(UICryptogramList.this, UISolveCryptogram.class);

            Bundle bundle = new Bundle();
            bundle.putString("crypt", crypt);
            bundle.putInt("cryptID", cryptID);
            bundle.putString("username",username);
            newIntent.putExtras(bundle);
            startActivity(newIntent);



        }
    }

    public void onReturnClick(View view)
    {
        onBackPressed();
    }


    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {

        Intent newIntent = new Intent(UICryptogramList.this, UIPlayerHomePage.class);
        Bundle bundle = new Bundle();
        bundle.putString("username",username);
        newIntent.putExtras(bundle);
        startActivity(newIntent);
    }
}



