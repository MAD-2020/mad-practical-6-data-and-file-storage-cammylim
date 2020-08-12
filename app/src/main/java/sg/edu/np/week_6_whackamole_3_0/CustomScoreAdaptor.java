package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    UserData data;
    private OnItemClickListener onItemClickListener;
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public CustomScoreAdaptor(UserData userdata){
        data = userdata;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener{
        void ItemClick(int position);
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.level_select,parent,false);
        return new CustomScoreViewHolder(item,onItemClickListener);
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){
        ArrayList<Integer> level_list = data.getLevels();
        ArrayList<Integer> score_list = data.getScores();
        String level = "Level "+level_list.get(position);
        String score = "Highest Score: "+score_list.get(position);
        holder.level.setText(level);
        holder.score.setText(score);
        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        //Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
    }

    public int getItemCount(){
        return data.getLevels().size();
    }
}