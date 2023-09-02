package inc.seven.smsfilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<SmsModel> smsModelArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<SmsModel> smsModelArrayList) {
        this.context = context;
        this.smsModelArrayList = smsModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sms_list_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtSender.setText(smsModelArrayList.get(position).senderName);
        holder.txtMessage.setText(smsModelArrayList.get(position).senderMessage);
    }

    @Override
    public int getItemCount() {
        return smsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtSender, txtMessage;
            public ViewHolder(View itemView){
                super(itemView);

                txtSender = itemView.findViewById(R.id.txtSender);
                txtMessage = itemView.findViewById(R.id.txtMessage);
            }
        }

}
